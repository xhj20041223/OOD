package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * represent a user of the calendar app.
 */
public class SingleUser implements IUser {
  ArrayList<IUserCalendar> calendars;
  IUserCalendar currentCalendar;
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  public SingleUser() {
    calendars = new ArrayList<IUserCalendar>();
  }

  @Override
  public IEvent createEvent(String subject, String start, HashMap<Properties, String> optionals) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    return this.currentCalendar.createEvent(subject, start, optionals);
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                String endDate) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.createEventSeries(subject, start, optionals, weekDays, endDate);
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                int repeatTimes) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.createEventSeries(subject, start, optionals, weekDays, repeatTimes);
  }

  @Override
  public IEvent editProperty(String subject, String start, String end, Properties property,
                             String newValue) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    return this.currentCalendar.editProperty(subject, start, end, property, newValue);
  }

  @Override
  public void editProperties(String subject, String start, Properties properties, String newValue) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.editProperties(subject, start, properties, newValue);

  }

  @Override
  public void editSeriesProperties(String subject, String start, Properties properties,
                                   String newValue) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.editSeriesProperties(subject, start, properties, newValue);
  }

  @Override
  public String showEvents(String date) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    return this.currentCalendar.showEvents(date);
  }

  @Override
  public String showEvents(String start, String end) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    return this.currentCalendar.showEvents(start, end);
  }

  @Override
  public String showStatus(String time) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    return this.currentCalendar.showStatus(time);
  }

  @Override
  public void createCalendar(String name, String timezone) {
    for (IUserCalendar calendar : calendars) {
      if (calendar.getCalendarName().equals(name)) {
        throw new IllegalStateException("calendar already exists");
      }
    }
    this.calendars.add(new UserCalendar(ZoneId.of(timezone), name));
  }

  @Override
  public void useCalendar(String name) {
    this.currentCalendar = this.findCalendar(name);
  }

  @Override
  public void editCalendar(String name, CalendarProperties property, String newValue) {
    if (property == CalendarProperties.NAME) {
      for (IUserCalendar calendar : calendars) {
        if (calendar.getCalendarName().equals(newValue)) {
          throw new IllegalStateException("calendar after modify already exists");
        }
      }
    }
    this.findCalendar(name).editProperty(property, newValue);
  }

  @Override
  public void copyEvent(String subject, String time, String targetCalendar, String targetTime) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.copyEvent(
            subject,
            LocalDateTime.parse(time, FORMATTER),
            findCalendar(targetCalendar),
            LocalDateTime.parse(targetTime, FORMATTER)
    );

  }

  @Override
  public void copyEvents(String date, String target, String targetDate) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.copyEvents(LocalDate.parse(date, ISO_LOCAL_DATE),
            this.findCalendar(target),
            LocalDate.parse(targetDate, ISO_LOCAL_DATE));
  }

  @Override
  public void copyEvents(String start, String end, String target, String targetDate) {
    if (currentCalendar == null) {
      throw new IllegalStateException("have not assigned current calendar yet");
    }
    this.currentCalendar.copyEvents(LocalDate.parse(start, ISO_LOCAL_DATE), LocalDate.parse(end,
            ISO_LOCAL_DATE), this.findCalendar(target), LocalDate.parse(targetDate,
            ISO_LOCAL_DATE));
  }

  private IUserCalendar findCalendar(String name) {
    for (IUserCalendar calendar : calendars) {
      if (calendar.getCalendarName().equals(name)) {
        return calendar;
      }
    }
    throw new IllegalStateException("can't find calendar with name  " + name);
  }
}
