// Calculator.java

package calculator;

/**
 * Represents a simple calculator that processes single-character inputs
 * (digits, operators, clear, equals) to update its internal state
 * and can report the current display string.
 */
public interface Calculator {

  /**
   * Process a single-character input and return a new Calculator
   * instance reflecting the updated state. The original instance
   * must remain unchanged.
   *
   * @param input the character to process:
   *              '0'–'9' for digits
   *              '+' , '-' , '*' for operators
   *              '=' to compute the result
   *              'C' to clear all inputs
   * @return a new Calculator object representing the state after processing this input
   * @throws IllegalArgumentException if the input character or sequence
   *                                  is invalid in the current state
   */
  Calculator input(char input);

  /**
   * Return the current string that would appear on the calculator's display.
   *
   * @return "" if no inputs have been entered yet
   *         the current input sequence (e.g. "32+24") or the computed
   *         result (e.g. "56")
   */
  String getResult();
}
