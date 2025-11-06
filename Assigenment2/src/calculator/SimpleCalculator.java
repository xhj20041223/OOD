package calculator;

import java.util.ArrayList;

/**
 * A {@link Calculator} implementation that supports only 32-bit integer
 * arithmetic (addition, subtraction, multiplication). Each input is a
 * single character, and state is immutable: input() returns a new instance.
 */
public class SimpleCalculator implements Calculator {
  private int digit;
  private final ArrayList<Character> inputs;
  private int fix;

  /**
   * Construct a fresh calculator with an empty input sequence.
   */

  public SimpleCalculator() {
    this.digit = 0;
    this.inputs = new ArrayList<Character>();
    this.fix = 1;
  }

  private SimpleCalculator(int digit, ArrayList<Character> inputs, int fix, char input) {
    this.digit = digit;
    this.inputs = new ArrayList<Character>();
    this.inputs.addAll(inputs);
    this.fix = fix;
    this.add(input);
  }

  @Override
  public Calculator input(char input) {
    if (input == 'C') {
      return new SimpleCalculator();
    }
    return new SimpleCalculator(this.digit, this.inputs, this.fix, input);
  }

  private void add(char input) {
    if (this.inputs.isEmpty()) {
      if (this.isNum(input)) {
        if (input != '0') {
          this.digit++;
        }
      } else {
        throw new IllegalArgumentException("First input must be a number");
      }
    } else if (input == '=') {
      if (!this.isNum(this.inputs.get(this.inputs.size() - 1))) {
        throw new IllegalArgumentException("Cannot calculate when ending with operator");
      }
      this.calculate();
      return;
    } else if (input == '+' || input == '-' || input == '*') {
      if (!this.isNum(this.inputs.get(this.inputs.size() - 1))) {
        throw new IllegalArgumentException("operator in the wrong place");
      }
      this.digit = 0;
    } else if (this.isNum(input)) {
      if (this.digit >= 10) {
        throw new IllegalArgumentException("digit overflow");
      } else if (this.digit == -1) {
        this.inputs.clear();
        this.digit = 0;
        this.add(input);
        return;
      } else if (!(this.digit == 0 && input == '0')) {
        this.digit++;
      }
    } else {
      throw new IllegalArgumentException("illegal input");
    }
    this.inputs.add(input);
  }


  @Override
  public String getResult() {
    if (this.inputs.isEmpty()) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    if (this.fix == -1) {
      result.append('-');
    }
    for (char c : this.inputs) {
      result.append(c);
    }
    return result.toString();
  }

  private void calculate() {
    int result = 0;
    int current = 0;
    char currentOperator;
    if (this.fix == 1) {
      currentOperator = '+';
    } else {
      currentOperator = '-';
    }
    for (char input : this.inputs) {
      if (this.isNum(input)) {
        current = current * 10 + (input - '0');
      } else if (input == '+' || input == '-' || input == '*') {
        result = this.operate(currentOperator, result, current);
        currentOperator = input;
        current = 0;
      }
    }
    result = this.operate(currentOperator, result, current);
    this.inputs.clear();
    if (result >= 0) {
      this.fix = 1;
    } else {
      result *= -1;
      this.fix = -1;
    }
    boolean filling = false;
    for (int i = 10; i >= 0; i--) {
      if (result >= Math.pow(10, i) || filling) {
        filling = true;
        this.inputs.add((char) ('0' + result / Math.pow(10, i)));
        result -= (int) ((int) (result / Math.pow(10, i)) * Math.pow(10, i));
      }
    }
    if (this.inputs.isEmpty()) {
      this.inputs.add('0');
    }
    this.digit = -1;
  }

  private boolean isNum(char input) {
    return (input >= '0' && input <= '9');
  }

  private int operate(char operator, int x, int y) {
    int result = 0;
    if (operator == '+') {
      try {
        result = Math.addExact(x, y);
      } catch (ArithmeticException ignored) {
      }
    } else if (operator == '-') {
      try {
        result = Math.subtractExact(x, y);
      } catch (ArithmeticException ignored) {
      }
    } else {
      try {
        result = Math.multiplyExact(x, y);
      } catch (ArithmeticException ignored) {
      }
    }
    return result;
  }
}
