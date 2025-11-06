package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * interface for a calendar tied to a specific user, extending base Calendar operations.
 */
public interface IUserCalendar extends Calendar {

  /**
   * Adds the given event to this calendar using its own timezone.
   *
   * @param event the event to add.
   */
  public void addEvent(IEvent event);

  /**
   * Adds the given event to this calendar, interpreting its date/time in the specified time zone.
   *
   * @param event  the event to add.
   * @param zoneId the ZoneId to use when interpreting the event's date/time.
   */
  public void addEvent(IEvent event, ZoneId zoneId);

  /**
   * Adds a series of events to this calendar in one batch.
   *
   * @param events the list of events to add as a series.
   */
  public void addEventSeries(ArrayList<IEvent> events);

  /**
   * Copies a single event identified by name and time from this calendar
   * into another IUserCalendar at a specified target time.
   *
   * @param name       the subject of the event to copy.
   * @param time       the original LocalDateTime of the event.
   * @param target     the IUserCalendar to receive the copied event.
   * @param targetTime the LocalDateTime at which to insert the copied event.
   */
  public void copyEvent(String name, LocalDateTime time, IUserCalendar target,
                        LocalDateTime targetTime);

  /**
   * Copies all events occurring on the specified date from this calendar
   * into another IUserCalendar on a given target date.
   *
   * @param date       the LocalDate of events to copy.
   * @param target     the IUserCalendar to receive the copied events.
   * @param targetDate the LocalDate on which to place the copied events.
   */
  public void copyEvents(LocalDate date, IUserCalendar target, LocalDate targetDate);

  /**
   * Copies all events within the given date range from this calendar
   * into another IUserCalendar on a specified date.
   *
   * @param start  the start LocalDate of the range.
   * @param end    the end LocalDate of the range.
   * @param target the IUserCalendar to receive the copied events.
   * @param date   the LocalDate on which to place the copied events.
   */
  public void copyEvents(LocalDate start, LocalDate end, IUserCalendar target,
                         LocalDate date);

  /**
   * Returns the name of this calendar.
   *
   * @return the calendar’s name.
   */
  public String getCalendarName();

  /**
   * Updates a property of this calendar to the specified value.
   *
   * @param properties the CalendarProperties enum indicating which property to edit.
   * @param value      the new value for the chosen property.
   */
  public void editProperty(CalendarProperties properties, String value);
}

