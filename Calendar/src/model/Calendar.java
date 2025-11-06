package model;

import java.util.HashMap;
import model.Properties;

public interface Calendar {
  /**
   * Creates and event with the given parameters and adds it to the events list in this calendar.
   * @param subject the subject of the event.
   * @param start the start LocalDateTime of the event.
   * @param optionals the hashmap that contains additional features of the event.
   * @return the newly created event.
   */
  public IEvent createEvent(String subject, String start, HashMap<Properties, String> optionals);

  /**
   * Creates a series of events as a list of events using the given parameters
   * and adds it to the map of event series' in this calendar.
   * @param subject the subject of the events.
   * @param start the start date of the events.
   * @param optionals the hashmap that contains additional features for the events.
   * @param weekDays the weekdays that the event should be repeated on.
   * @param endDate the end date of the events.
   */
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                String endDate);

  /**
   * Creates a series of events as a list of events using the given parameters
   * and adds it to the map of event series' in this calendar.
   * @param subject the subject of the events.
   * @param start the start date of the events.
   * @param optionals the hashmap that contains additional features for the events.
   * @param weekDays the weekdays that the event should be repeated on.
   * @param repeatTimes the total number of events you want in your series.
   */
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                int repeatTimes);

  /**
   * Edits or adds a property of an event.
   * @param subject the subject of the event you want to change.
   * @param start the start date of the event you want to change.
   * @param end the end date of the event you want to change.
   * @param property the property of the event you want to change
   * @param newValue the new value you want to change the property to.
   * @return the newly changed event.
   */
  public IEvent editProperty(String subject, String start, String end, Properties property,
                           String newValue);

  /**
   * Edits or adds properties of an event series.
   * @param subject the subject of the events you want to change.
   * @param start the start date of the events you want to change.
   * @param properties the property of the events you want to change.
   * @param newValue the new value you want to change the property to.
   */
  public void editProperties(String subject, String start, Properties properties, String newValue);

  /**
   * edit series
   * @param subject
   * @param start
   * @param properties
   * @param newValue
   */
  public void editSeriesProperties(String subject, String start, Properties properties,
                                   String newValue);

  public String showEvents(String date);

  public String showEvents(String start, String end);

  public String showStatus(String time);
}
