package view;

import controller.IGuiCalendarController;

/**
 * interface for a Graphical User Interface of a calendar app.
 */
public interface IGuiCalendarView {
  void addListener(IGuiCalendarController controller);

  public void updateCalendarList(String[] calendarNames);
}
