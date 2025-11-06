package model;

/**
 * interface representing a user that can manage multiple calendars.
 */
public interface IUser extends Calendar {

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

  /**
   * Edits a property of one of this user's calendars.
   *
   * @param name     the name of the calendar to modify.
   * @param property the calendar property to update (e.g., name, timezone).
   * @param newValue the new value for the specified property.
   */
  public void editCalendar(String name, CalendarProperties property, String newValue);

  /**
   * Copies a single event identified by subject and time from this user's current calendar
   * into another calendar at a specified time.
   *
   * @param subject        the subject of the event to copy.
   * @param time           the original date/time of the event (ISO-8601 string).
   * @param targetCalendar the name of the calendar to copy the event into.
   * @param targetTime     the date/time at which to insert the copied event (ISO-8601 string).
   */
  public void copyEvent(String subject, String time, String targetCalendar, String targetTime);

  /**
   * Copies all events occurring on the specified date from this user's current calendar
   * into another calendar on a given target date.
   *
   * @param date       the date of events to copy (ISO-8601 string, e.g., "2025-06-15").
   * @param target     the name of the calendar to receive the copied events.
   * @param targetDate the date on which to place the copied events (ISO-8601 string).
   */
  public void copyEvents(String date, String target, String targetDate);

  /**
   * Copies all events within a date/time range from this user's current calendar
   * into another calendar on a specified target date.
   *
   * @param start      the start of the date/time range (ISO-8601 string).
   * @param end        the end of the date/time range (ISO-8601 string).
   * @param target     the name of the calendar to receive the copied events.
   * @param targetDate the date on which to place the copied events (ISO-8601 string).
   */
  public void copyEvents(String start, String end, String target, String targetDate);
}