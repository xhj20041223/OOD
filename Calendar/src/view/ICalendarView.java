package view;

/**
 * View interface for a calendar application.
 */
public interface ICalendarView {

  /**
   * displays the given message to the console.
   *
   * @param message the message to be displayed.
   */
  public void showMessage(String message);

  /**
   * displays the given error message to the console.
   *
   * @param errorMessage the error message to be displayed.
   */
  public void showError(String errorMessage);
}
