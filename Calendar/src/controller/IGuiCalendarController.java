package controller;

import java.util.HashMap;

import model.IEvent;
import model.Properties;

/**
 * interface for a GUI calendar controller.
 */
public interface IGuiCalendarController {
  /**
   * Creates and event with the given parameters and adds it to the events list in this calendar.
   *
   * @param subject   the subject of the event.
   * @param start     the start LocalDateTime of the event.
   * @param optionals the hashmap that contains additional features of the event.
   * @return the newly created event.
   */
  IEvent createEvent(String subject, String start, HashMap<Properties, String> optionals);

  /**
   * Creates a series of events as a list of events using the given parameters
   * and adds it to the map of event series' in this calendar.
   *
   * @param subject   the subject of the events.
   * @param start     the start date of the events.
   * @param optionals the hashmap that contains additional features for the events.
   * @param weekDays  the weekdays that the event should be repeated on.
   * @param endDate   the end date of the events.
   */
  void createEventSeries(String subject, String start,
                         HashMap<Properties, String> optionals, String weekDays,
                         String endDate);

  /**
   * Creates a series of events as a list of events using the given parameters
   * and adds it to the map of event series' in this calendar.
   *
   * @param subject     the subject of the events.
   * @param start       the start date of the events.
   * @param optionals   the hashmap that contains additional features for the events.
   * @param weekDays    the weekdays that the event should be repeated on.
   * @param repeatTimes the total number of events you want in your series.
   */
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                int repeatTimes);

  /**
   * Edits or adds a property of an event.
   *
   * @param subject  the subject of the event you want to change.
   * @param start    the start date of the event you want to change.
   * @param end      the end date of the event you want to change.
   * @param property the property of the event you want to change.
   * @param newValue the new value you want to change the property to.
   * @return the newly changed event.
   */
  public IEvent editProperty(String subject, String start, String end, Properties property,
                             String newValue);

  /**
   * lists the events on the given date.
   *
   * @param date represents a date of a day with year,month, and day.
   * @return a list of the events on the given date.
   */
  public String showEvents(String date);

  /**
   * lists the events between the two given dates.
   *
   * @param start the start date.
   * @param end the end date.
   * @return a list of the events between the two given dates.
   */
  public String showEvents(String start, String end);

  /**
   * Creates a new calendar with the given name and timezone and associates it with this user.
   *
   * @param name     the name of the calendar to create.
   * @param timezone the timezone identifier for the calendar (e.g., "America/New_York").
   */
  public void createCalendar(String name, String timezone);

  /**
   * Switches this user's active calendar to the one identified by the given name.
   *
   * @param name the name of the calendar to activate.
   */
  public void useCalendar(String name);

}
