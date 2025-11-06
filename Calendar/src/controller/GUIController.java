package controller;

import model.IEvent;
import model.IUser;
import model.Properties;
import view.IGuiCalendarView;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Calendar controller class.
 * Handles user input and feeds it to the model.
 */
public class GUIController implements IGuiCalendarController {
  private IUser user;
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * constructs a new calendar controller.
   *
   * @param user the user to run the program on.
   * @param view the view to pass the output to.
   */

  public GUIController(IUser user, IGuiCalendarView view) {
    this.user = user;
    view.addListener(this);
  }


  @Override
  public IEvent createEvent(String subject, String start, HashMap<Properties, String> optionals) {
    if (subject.trim().contains(" ")) {
      subject = "\"" + subject.trim() + "\"";
    }
    return user.createEvent(subject, start, optionals);
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                String endDate) {
    if (subject.trim().contains(" ")) {
      subject = "\"" + subject.trim() + "\"";
    }
    user.createEventSeries(subject, start, optionals, weekDays, endDate);
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                int repeatTimes) {
    if (subject.trim().contains(" ")) {
      subject = "\"" + subject.trim() + "\"";
    }
    user.createEventSeries(subject, start, optionals, weekDays, repeatTimes);
  }

  @Override
  public IEvent editProperty(String subject, String start, String end, Properties property,
                             String newValue) {
    if (subject.trim().contains(" ")) {
      subject = "\"" + subject.trim() + "\"";
    }
    return user.editProperty(subject, start, end, property, newValue);
  }

  @Override
  public String showEvents(String date) {
    return user.showEvents(date);
  }

  @Override
  public String showEvents(String start, String end) {
    return user.showEvents(start, end);
  }

  @Override
  public void createCalendar(String name, String timezone) {
    user.createCalendar(name, timezone);
  }

  @Override
  public void useCalendar(String name) {
    user.useCalendar(name);
  }
}