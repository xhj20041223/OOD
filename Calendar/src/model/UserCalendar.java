package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

/**
 * a user calendar implementation.
 */
public class UserCalendar extends SimpleCalendar implements IUserCalendar {
  private ZoneId zoneId;
  private String name;

  /**
   * constructor for UserCalendar.
   */
  public UserCalendar(ZoneId zoneId, String name) {
    super();
    this.zoneId = zoneId;
    this.name = name;
  }

  /**
   * Adds the given event to the list of events.
   *
   * @param event the event to be added.
   */
  @Override
  public void addEvent(IEvent event) {
    if (this.events.contains(event)) {
      throw new IllegalArgumentException("Such event already exists in this calendar");
    }
    this.events.add(event);
    Collections.sort(this.events);
  }

  @Override
  public void addEvent(IEvent event, ZoneId zoneId) {
    LocalDateTime newStart =
            event.getStartDateTime()
                    .atZone(zoneId).withZoneSameInstant(this.zoneId).toLocalDateTime();
    if (newStart.isBefore(event.getEndDateTime())) {
      event.setStartDateTime(newStart);
      event.setEndDateTime(event.getEndDateTime().atZone(zoneId).withZoneSameInstant(this.zoneId)
              .toLocalDateTime());
    } else {
      event.setEndDateTime(event.getEndDateTime().atZone(zoneId).withZoneSameInstant(this.zoneId)
              .toLocalDateTime());
      event.setStartDateTime(newStart);
    }
    if (this.events.contains(event)) {
      throw new IllegalArgumentException("Such event already exists in this calendar");
    }
    this.events.add(event);
    Collections.sort(this.events);
  }

  @Override
  public void addEventSeries(ArrayList<IEvent> events) {
    this.eventSeriesList.add(events);
  }

  @Override
  public void copyEvent(String name, LocalDateTime time, IUserCalendar target,
                        LocalDateTime targetTime) {
    IEvent targetEvent = this.copySingleEvent(this.findEvent(name, time.format(FORMATTER)));
    if (targetTime.isBefore(targetEvent.getEndDateTime())) {
      targetEvent.setStartDateTime(targetTime);
      targetEvent.setEndDateTime(targetEvent.getEndDateTime()
              .plusMinutes(targetEvent.getStartDateTime()
                      .until(targetTime, ChronoUnit.MINUTES)));
    } else {
      targetEvent.setEndDateTime(targetEvent.getEndDateTime()
              .plusMinutes(targetEvent.getStartDateTime()
                      .until(targetTime, ChronoUnit.MINUTES)));
      targetEvent.setStartDateTime(targetTime);
    }
    target.addEvent(targetEvent);
  }

  @Override
  public void copyEvents(LocalDate date, IUserCalendar target, LocalDate targetDate) {
    for (IEvent event : this.events) {
      if (!date.isAfter(event.getStartDateTime().toLocalDate())
              && !date.isBefore(event.getStartDateTime().toLocalDate())) {
        IEvent newEvent = this.copySingleEvent(event);
        if (targetDate.isBefore(newEvent.getEndDateTime().toLocalDate())) {
          newEvent.setStartDateTime(LocalDateTime.of(targetDate,
                  event.getStartDateTime().toLocalTime()));
          newEvent.setEndDateTime(LocalDateTime.of(targetDate,
                  event.getEndDateTime().toLocalTime()));
        } else {
          newEvent.setEndDateTime(LocalDateTime.of(targetDate,
                  event.getEndDateTime().toLocalTime()));
          newEvent.setStartDateTime(LocalDateTime.of(targetDate,
                  event.getStartDateTime().toLocalTime()));
        }
        try {
          target.addEvent(newEvent, zoneId);
        } catch (Exception ignored) {
        }
      } else if (!date.isAfter(event.getStartDateTime().toLocalDate())) {
        break;
      }
    }
  }

  @Override
  public void copyEvents(LocalDate start, LocalDate end, IUserCalendar target,
                         LocalDate date) {
    for (ArrayList<IEvent> events : this.eventSeriesList) {
      ArrayList<IEvent> newSeries = new ArrayList<>();
      for (IEvent event : events) {
        if (!start.isAfter(event.getStartDateTime().toLocalDate())
                && !end.isBefore(event.getStartDateTime().toLocalDate())) {
          IEvent newEvent = this.copySingleEvent(event);
          if (date.plus(start.until(event.getStartDateTime().toLocalDate()))
                  .isBefore(newEvent.getStartDateTime().toLocalDate())) {
            newEvent.setStartDateTime(LocalDateTime.of(date
                            .plus(start.until(event.getStartDateTime().toLocalDate())),
                    event.getStartDateTime().toLocalTime()));
            newEvent.setEndDateTime(LocalDateTime.of(date.plus(start.until(event.getEndDateTime()
                            .toLocalDate())),
                    event.getEndDateTime().toLocalTime()));
          } else {
            newEvent.setEndDateTime(LocalDateTime.of(date.plus(start.until(event.getEndDateTime()
                            .toLocalDate())),
                    event.getEndDateTime().toLocalTime()));
            newEvent.setStartDateTime(LocalDateTime.of(date.
                            plus(start.until(event.getStartDateTime().toLocalDate())),
                    event.getStartDateTime().toLocalTime()));
          }
          try {
            newSeries.add(newEvent);
            target.addEvent(newEvent, zoneId);
          } catch (Exception ignored) {
          }
        } else if (!date.isAfter(event.getStartDateTime().toLocalDate())) {
          break;
        }
      }
      if (!newSeries.isEmpty()) {
        target.addEventSeries(newSeries);
      }
    }
    for (IEvent event : this.events) {
      if (!start.isAfter(event.getStartDateTime().toLocalDate())
              && !end.isBefore(event.getStartDateTime().toLocalDate())) {
        IEvent newEvent = this.copySingleEvent(event);
        if (date.plus(start.until(event.getStartDateTime().toLocalDate()))
                .isBefore(newEvent.getStartDateTime().toLocalDate())) {
          newEvent.setStartDateTime(LocalDateTime.of(date.plus(start.until(event.getStartDateTime()
                          .toLocalDate())),
                  event.getStartDateTime().toLocalTime()));
          newEvent.setEndDateTime(LocalDateTime.of(date.plus(start.until(event.getEndDateTime()
                          .toLocalDate())),
                  event.getEndDateTime().toLocalTime()));
        } else {
          newEvent.setEndDateTime(LocalDateTime.of(date.plus(start.until(event.getEndDateTime()
                          .toLocalDate())),
                  event.getEndDateTime().toLocalTime()));
          newEvent.setStartDateTime(LocalDateTime.of(date.plus(start.until(event.getStartDateTime()
                          .toLocalDate())),
                  event.getStartDateTime().toLocalTime()));
        }
        try {
          target.addEvent(newEvent, zoneId);
        } catch (Exception ignored) {
        }
      } else if (!date.isAfter(event.getStartDateTime().toLocalDate())) {
        break;
      }
    }

  }

  @Override
  public String getCalendarName() {
    return this.name;
  }

  @Override
  public void editProperty(CalendarProperties properties, String value) {
    switch (properties) {
      case NAME:
        this.name = value;
        break;
      case TIMEZONE:
        this.zoneId = ZoneId.of(value);
        break;
      default:
        break;
    }
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof UserCalendar) {
      return this.name.equals(((UserCalendar) object).getCalendarName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  private IEvent copySingleEvent(IEvent event) {
    Event.EventBuilder newEventBuilder = new Event.EventBuilder();
    newEventBuilder.setStart(event.getStartDateTime())
            .setEnd(event.getEndDateTime())
            .setSubject(event.getSubject());
    try {
      newEventBuilder.setDescription(event.getDescription());
    } catch (Exception ignored) {
    }
    try {
      newEventBuilder.setLocation(String.valueOf(event.getLocation()));
    } catch (Exception ignored) {
    }
    try {
      newEventBuilder.setStatus(String.valueOf(event.getStatus()));
    } catch (Exception ignored) {
    }
    return newEventBuilder.build();
  }

}
