package calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Unit tests for {@link SimpleCalculator}, covering basic arithmetic,
 * chained operations, clear behavior, and overflow handling.
 */
public class SimpleCalculatorTest {

  /**
   * Before any input, getResult() should return an empty string.
   */
  @Test
  public void testInitialResultIsEmpty() {
    Calculator calc = new SimpleCalculator();
    assertEquals("", calc.getResult());
  }

  /**
   * Sequence 3 2 + 2 4 = should display each step and then the final getResult 56.
   */
  @Test
  public void testBasicSequenceDisplaysInputsAndFinalResult() {
    Calculator calc = new SimpleCalculator();
    assertEquals("", calc.getResult());

    calc = calc.input('3');
    assertEquals("3", calc.getResult());

    calc = calc.input('2');
    assertEquals("32", calc.getResult());

    calc = calc.input('+');
    assertEquals("32+", calc.getResult());

    calc = calc.input('2');
    assertEquals("32+2", calc.getResult());

    calc = calc.input('4');
    assertEquals("32+24", calc.getResult());

    calc = calc.input('=');
    assertEquals("56", calc.getResult());
  }

  /**
   * Pressing '=' multiple times should keep the same getResult.
   */
  @Test
  public void testMultipleEqualsKeepSameResult() {
    Calculator calc = new SimpleCalculator()
            .input('3')
            .input('2')
            .input('+')
            .input('2')
            .input('4')
            .input('=')
            .input('=')
            .input('=');
    assertEquals("56", calc.getResult());
  }

  /**
   * Enter multiple '0' as first inputs.
   */
  @Test
  public void testFirstInputZeroResult() {
    Calculator calc = new SimpleCalculator()
            .input('0')
            .input('0')
            .input('0')
            .input('-')
            .input('3')
            .input('=')
            .input('=')
            .input('=');
    assertEquals("-3", calc.getResult());
  }

  /**
   * After '=', entering a digit starts a new calculation.
   */
  @Test
  public void testDigitAfterEqualsResetsForNewCalculation() {
    Calculator calc = new SimpleCalculator()
            .input('3').input('2')
            .input('+').input('2').input('4')
            .input('=');
    assertEquals("56", calc.getResult());

    calc = calc.input('7');
    assertEquals("7", calc.getResult());

    calc = calc.input('+').input('3').input('=');
    assertEquals("10", calc.getResult());
  }

  /**
   * After '=', entering an operator continues from the previous getResult.
   */
  @Test
  public void testOperatorAfterEqualsContinuesFromResult() {
    Calculator calc = new SimpleCalculator()
            .input('4').input('2')
            .input('*').input('2')
            .input('=');
    assertEquals("84", calc.getResult());

    calc = calc.input('*').input('2').input('=');
    assertEquals("168", calc.getResult());
  }

  /**
   * Negative getResults are allowed even though negative inputs are not.
   */
  @Test
  public void testNegativeResultAllowed() {
    Calculator calc = new SimpleCalculator()
            .input('5').input('-').input('8').input('=');
    assertEquals("-3", calc.getResult());
  }

  /**
   * 'C' clears the calculator to empty state.
   */
  @Test
  public void testClearResetsCalculator() {
    Calculator calc = new SimpleCalculator()
            .input('1').input('2')
            .input('C');
    assertEquals("", calc.getResult());
  }

  /**
   * First input must be a digit; otherwise IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFirstInputThrows() {
    new SimpleCalculator().input('+');
  }

  /**
   * Ending with an operator then '=' throws IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCannotEndWithOperator() {
    Calculator calc = new SimpleCalculator()
            .input('3').input('2').input('+');
    calc.input('=');
  }

  /**
   * Illegal characters should throw IllegalArgumentException immediately.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCharacterInputThrows() {
    new SimpleCalculator().input('x');
  }

  /**
   * Operand overflow (more than 10 digits) throws and retains previous value.
   */
  @Test
  public void testOperandOverflowThrowsAndRetainsValue() {
    Calculator calc = new SimpleCalculator();
    for (int i = 0; i < 10; i++) {
      calc = calc.input('1');
    }
    assertEquals("1111111111", calc.getResult());
    try {
      calc = calc.input('1');
      fail("Expected IllegalArgumentException on operand overflow");
    } catch (IllegalArgumentException e) {
      assertEquals("1111111111", calc.getResult());
    }
  }

  /**
   * Leading zeros should be ignored and never overflow.
   */
  @Test
  public void testLeadingZerosAreIgnored() {
    Calculator calc = new SimpleCalculator();
    for (int i = 0; i < 12; i++) {
      calc = calc.input('0');
    }
    calc = calc.input('3').input('=');
    assertEquals("3", calc.getResult());
  }

  /**
   * Arithmetic overflow on addition returns "0" without exception.
   */
  @Test
  public void testArithmeticOverflowOnAddReturnsZero() {
    Calculator calc = new SimpleCalculator();
    String max = String.valueOf(Integer.MAX_VALUE);
    for (char c : max.toCharArray()) {
      calc = calc.input(c);
    }
    calc = calc.input('+').input('1').input('=');
    assertEquals("0", calc.getResult());
  }

  /**
   * After overflow getResult, further operations behave normally.
   */
  @Test
  public void testOverflowResultThenSubtractTen() {
    Calculator calc = new SimpleCalculator();
    String max = String.valueOf(Integer.MAX_VALUE);
    for (char c : max.toCharArray()) {
      calc = calc.input(c);
    }
    calc = calc.input('+')
            .input('1')
            .input('-')
            .input('1')
            .input('0')
            .input('=');
    assertEquals("-10", calc.getResult());
  }

  /**
   * Simple addition: 10 + 5 = 15.
   */
  @Test
  public void testSimpleAddition() {
    Calculator calc = new SimpleCalculator()
            .input('1').input('0')
            .input('+')
            .input('5')
            .input('=');
    assertEquals("15", calc.getResult());
  }

  @Test
  public void testHalfwaySimpleAddition() {
    Calculator calc = new SimpleCalculator()
            .input('0')
            .input('+');
    assertEquals("0+", calc.getResult());
  }

  /**
   * Simple subtraction: 20 - 7 = 13.
   */
  @Test
  public void testSimpleSubtraction() {
    Calculator calc = new SimpleCalculator()
            .input('2').input('0')
            .input('-')
            .input('7')
            .input('=');
    assertEquals("13", calc.getResult());
  }

  /**
   * Simple subtraction: 0 - 13 = -13.
   */
  @Test
  public void testSimpleNegaSubtraction() {
    Calculator calc = new SimpleCalculator()
            .input('0')
            .input('-')
            .input('1').input('3')
            .input('=');
    assertEquals("-13", calc.getResult());
  }

  /**
   * Simple multiplication: 6 * 7 = 42.
   */
  @Test
  public void testSimpleMultiplication() {
    Calculator calc = new SimpleCalculator()
            .input('6')
            .input('*')
            .input('7')
            .input('=');
    assertEquals("42", calc.getResult());
  }

  /**
   * Mixed operations (left-associative): 2 + 3 * 4 = 20.
   */
  @Test
  public void testMixedAddThenMul() {
    Calculator calc = new SimpleCalculator()
            .input('2')
            .input('+')
            .input('3')
            .input('*')
            .input('4')
            .input('=');
    assertEquals("20", calc.getResult());
  }

  /**
   * Mixed operations: 10 * 2 + 5 = 25.
   */
  @Test
  public void testMixedMulThenAdd() {
    Calculator calc = new SimpleCalculator()
            .input('1').input('0')
            .input('*')
            .input('2')
            .input('+')
            .input('5')
            .input('=');
    assertEquals("25", calc.getResult());
  }

  /**
   * Complex chain: 3 + 2 * 4 - 5 = 15.
   */
  @Test
  public void testComplexChainOperations() {
    Calculator calc = new SimpleCalculator()
            .input('3')
            .input('+')
            .input('2')
            .input('*')
            .input('4')
            .input('-')
            .input('5')
            .input('=');
    assertEquals("15", calc.getResult());
  }
}
