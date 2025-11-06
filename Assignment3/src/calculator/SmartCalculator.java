package calculator;

import java.util.ArrayList;

/**
 * A {@link Calculator} implementation that supports only 32-bit integer
 * arithmetic (addition, subtraction, multiplication). Each input is a
 * single character, and state is immutable: input() returns a new instance.
 */
public class SmartCalculator implements Calculator {
  // represent the current digit count
  private int digit;
  // represent the valid inputs so far
  private final ArrayList<Character> inputs;
  // represent what the last operation is before pressing '='
  private ArrayList<Character> lastOperations;
  // represent the positive or negative of the result
  private int fix;

  /**
   * Construct a fresh calculator with an empty input sequence.
   */

  public SmartCalculator() {
    this.digit = 0;
    this.inputs = new ArrayList<Character>();
    this.fix = 1;
    this.lastOperations = new ArrayList<Character>();
    this.lastOperations.add('+');
    this.lastOperations.add('0');
  }

  private SmartCalculator(int digit, ArrayList<Character> inputs, int fix,
                          ArrayList<Character> lastOperations, char input) {
    this.digit = digit;
    this.inputs = new ArrayList<Character>();
    this.inputs.addAll(inputs);
    this.fix = fix;
    this.lastOperations = new ArrayList<Character>();
    this.lastOperations.addAll(lastOperations);
    this.add(input);
  }

  @Override
  public Calculator input(char input) {
    if (input == 'C') {
      return new SmartCalculator();
    }
    return new SmartCalculator(this.digit, this.inputs, this.fix, this.lastOperations, input);
  }

  private void add(char input) {
    if (this.inputs.isEmpty()) {
      if (this.isNum(input)) {
        if (input != '0') {
          this.digit++;
        }
      } else if (input == '+') {
        return;
      } else {
        throw new IllegalArgumentException("First input must be a number");
      }
    } else if (input == '=') {
      if (this.digit == -1) {
        this.inputs.addAll(this.lastOperations);
      }
      this.calculate();
      return;
    } else if (input == '+' || input == '-' || input == '*') {
      if (!this.isNum(this.inputs.get(this.inputs.size() - 1))) {
        this.inputs.remove(this.inputs.size() - 1);
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
    this.lastOperations.clear();
    boolean isConstant = true;
    int result = 0;
    int current = 0;
    char currentOperator;
    if (this.fix == 1) {
      currentOperator = '+';
    } else {
      currentOperator = '-';
    }
    for (int i = 0; i < this.inputs.size() - 1; i++) {
      char input = this.inputs.get(i);
      if (this.isNum(input)) {
        current = current * 10 + (input - '0');

      } else {
        isConstant = false;
        if (i == this.inputs.size() - 1) {
          result = this.operate(input, result, result);
        } else {
          result = this.operate(currentOperator, result, current);
        }
        currentOperator = input;
        current = 0;
      }
    }
    char input = this.inputs.get(this.inputs.size() - 1);
    if (this.isNum(input)) {
      current = current * 10 + (this.inputs.get(this.inputs.size() - 1) - '0');
      this.lastOperations.add(currentOperator);
      if (isConstant) {
        this.lastOperations.add('0');
      } else {
        for (char c : Integer.toString(current).toCharArray()) {
          this.lastOperations.add(c);
        }
      }
      result = this.operate(currentOperator, result, current);
    } else {
      this.lastOperations.add(input);
      for (char c : Integer.toString(current).toCharArray()) {
        this.lastOperations.add(c);
      }
      result = this.operate(input, current, current);
    }
    this.inputs.clear();
    if (result >= 0) {
      this.fix = 1;
    } else {
      result *= -1;
      this.fix = -1;
    }
    for (char c : Integer.toString(result).toCharArray()) {
      this.inputs.add(c);
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
