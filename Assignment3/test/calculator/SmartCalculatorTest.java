package calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Unit tests for {@link SmartCalculator}, covering basic arithmetic,
 * chained operations, clear behavior, and overflow handling.
 */
public class SmartCalculatorTest {

  /**
   * Before any input, getResult() should return an empty string.
   */
  @Test
  public void testInitialResultIsEmpty() {
    Calculator calc = new SmartCalculator();
    assertEquals("", calc.getResult());
  }

  /**
   * Sequence 3 2 + 2 4 = should display each step and then the final getResult 56.
   */
  @Test
  public void testBasicSequenceDisplaysInputsAndFinalResult() {
    Calculator calc = new SmartCalculator();
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
   * Pressing '=' multiple times should repeat the last operation.
   */
  @Test
  public void testMultipleEqualsKeepSameResult() {
    Calculator calc = new SmartCalculator()
            .input('3')
            .input('2')
            .input('+')
            .input('2')
            .input('4')
            .input('=')
            .input('=')
            .input('=');
    assertEquals("104", calc.getResult());
  }


  /**
   * After '=', entering a digit starts a new calculation.
   */
  @Test
  public void testDigitAfterEqualsResetsForNewCalculation() {
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
            .input('5').input('-').input('8').input('=');
    assertEquals("-3", calc.getResult());
  }

  /**
   * 'C' clears the calculator to empty state.
   */
  @Test
  public void testClearResetsCalculator() {
    Calculator calc = new SmartCalculator()
            .input('1').input('2')
            .input('C');
    assertEquals("", calc.getResult());
  }

  /**
   * First input must be a digit or '+'; otherwise IllegalArgumentException.
   */
  @Test
  public void testInvalidFirstInputThrows() {
    new SmartCalculator().input('+');
    try {
      new SmartCalculator().input('-');
      fail("should not put operator other than '+' as first input");
    } catch (IllegalArgumentException ignored) {

    }
  }

  /**
   * Ending with an operator then '=' operate with itself.
   */
  @Test
  public void testCannotEndWithOperator() {
    Calculator calc = new SmartCalculator()
            .input('3').input('2').input('+');
    calc = calc.input('=');
    assertEquals("64", calc.getResult());
  }

  /**
   * Illegal characters should throw IllegalArgumentException immediately.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCharacterInputThrows() {
    new SmartCalculator().input('x');
  }

  /**
   * Operand overflow (more than 10 digits) throws and retains previous value.
   */
  @Test
  public void testOperandOverflowThrowsAndRetainsValue() {
    Calculator calc = new SmartCalculator();
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
    Calculator calc = new SmartCalculator();
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
    Calculator calc = new SmartCalculator();
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
    Calculator calc = new SmartCalculator();
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
    Calculator calc = new SmartCalculator()
            .input('1').input('0')
            .input('+')
            .input('5')
            .input('=');
    assertEquals("15", calc.getResult());
  }

  @Test
  public void testHalfwaySimpleAddition() {
    Calculator calc = new SmartCalculator()
            .input('0')
            .input('+');
    assertEquals("0+", calc.getResult());
  }

  /**
   * Change the operator from + to -.
   */
  @Test
  public void testChangingOperator() {
    Calculator calc = new SmartCalculator()
            .input('2').input('0')
            .input('+').input('-')
            .input('7')
            .input('=');
    assertEquals("13", calc.getResult());
  }

  /**
   * Repeat the same operator remain unchanged.
   */
  @Test
  public void testRepeatingOperator() {
    Calculator calc = new SmartCalculator()
            .input('2').input('0')
            .input('-').input('-');
    assertEquals("20-", calc.getResult());

    calc = calc.input('7').input('=');
    assertEquals("13", calc.getResult());
  }

  /**
   * Simple subtraction: 20 - 7 = 13.
   */
  @Test
  public void testSimpleSubtraction() {
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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
    Calculator calc = new SmartCalculator()
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

  /**
   * Multiple '=' for a constant should keep unchanged.
   */
  @Test
  public void testEqualsWithConstant() {
    Calculator calc = new SmartCalculator()
            .input('1')
            .input('=')
            .input('=')
            .input('=');
    assertEquals("1", calc.getResult());
  }

  /**
   * A leading '+' should be silently ignored—display remains empty
   * until a digit is entered.
   */
  @Test
  public void testLeadingPlusIgnored() {
    Calculator calc = new SmartCalculator();
    // '+' alone does not change the blank display
    calc = calc.input('+');
    assertEquals("", calc.getResult());

    // then digits follow normally
    calc = calc.input('3').input('2');
    assertEquals("32", calc.getResult());
  }

  /**
   * The very first input may only be a digit or '+'.
   * '*' should throw immediately.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFirstInputMultiply() {
    new SmartCalculator().input('*');
  }

  /**
   * '=' as first input is illegal.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFirstInputEquals() {
    new SmartCalculator().input('=');
  }

}
