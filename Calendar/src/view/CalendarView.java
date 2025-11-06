package view;

import java.io.IOException;

/**
 * Represents a view for a calendar application.
 */
public class CalendarView implements ICalendarView {
  private final Appendable appendable;

  /**
   * constructs a view for a calendar application.
   *
   * @param appendable the destination to which the view will write messages and errors.
   */
  public CalendarView(Appendable appendable) {
    this.appendable = appendable;
  }

  /**
   * displays the given message to the console.
   *
   * @param message the message to be displayed.
   */
  @Override
  public void showMessage(String message) {
    try {
      appendable.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to show message", e);
    }
  }

  /**
   * displays the given error message to the console.
   *
   * @param errorMessage the error message to be displayed.
   */
  @Override
  public void showError(String errorMessage) {
    try {
      appendable.append("Error: ").append(errorMessage).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to show error", e);
    }
  }
}
