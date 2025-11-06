package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.time.DayOfWeek;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class SimpleCalendar implements Calendar {
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private final HashSet<ArrayList<IEvent>> eventSerieses;
  private final ArrayList<IEvent> events;

  public SimpleCalendar() {
    this.events = new ArrayList<IEvent>();
    this.eventSerieses = new HashSet<ArrayList<IEvent>>();
  }

  @Override
  public IEvent createEvent(String subject, String start, HashMap<Properties, String> optionals) {
    Event.EventBuilder builder = new Event.EventBuilder();
    builder.setSubject(subject);
    if (optionals.containsKey(Properties.END_TIME)) {
      if (LocalDateTime.parse(start, FORMATTER).isAfter(LocalDateTime.parse(optionals.get(Properties.END_TIME), FORMATTER))) {
        throw new IllegalArgumentException("End time should be after the start time");
      }
      builder.setStart(LocalDateTime.parse(start, FORMATTER));
      builder.setEnd(LocalDateTime.parse(optionals.get(Properties.END_TIME), FORMATTER));
    } else {
      builder.setFullDayDate(LocalDate.parse(start, ISO_LOCAL_DATE));
    }
    if (optionals.containsKey(Properties.DESCRIPTION)) {
      builder.setDescription(optionals.get(Properties.DESCRIPTION));
    }
    if (optionals.containsKey(Properties.STATUS)) {
      builder.setStatus(optionals.get(Properties.STATUS));
    }
    if (optionals.containsKey(Properties.LOCATION)) {
      builder.setLocation(optionals.get(Properties.LOCATION));
    }
    IEvent event = builder.build();
    if (this.events.contains(event)) {
      throw new IllegalArgumentException("Event already exists");
    }
    this.events.add(event);
    Collections.sort(this.events);
    return event;
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                String endDate) {
    ArrayList<IEvent> eventSeries = new ArrayList<IEvent>();
    ArrayList<DayOfWeek> days = this.convertWeekDays(weekDays);
    LocalDate startDate;
    if (optionals.containsKey(Properties.END_TIME)) {
      startDate = LocalDate.from(LocalDateTime.parse(start, FORMATTER));
    } else {
      startDate = LocalDate.parse(start, ISO_LOCAL_DATE);
    }
    while (!startDate.isAfter(LocalDate.parse(endDate, ISO_LOCAL_DATE))) {
      if (days.contains(startDate.getDayOfWeek())) {
        if (optionals.containsKey(Properties.END_TIME)) {
          optionals.replace(Properties.END_TIME,
                  startDate.format(ISO_LOCAL_DATE) + optionals.get(Properties.END_TIME).substring(10));
          eventSeries.add(this.createEvent(subject,
                  startDate.format(ISO_LOCAL_DATE) + start.substring(10), optionals));

        } else {
          eventSeries.add(this.createEvent(subject, startDate.format(ISO_LOCAL_DATE), optionals));
        }
      }
      startDate = startDate.plusDays(1);
    }
    this.eventSerieses.add(eventSeries);
  }

  @Override
  public void createEventSeries(String subject, String start,
                                HashMap<Properties, String> optionals, String weekDays,
                                int repeatTimes) {
    ArrayList<IEvent> eventSeries = new ArrayList<IEvent>();
    ArrayList<DayOfWeek> days = this.convertWeekDays(weekDays);
    LocalDate startDate;
    if (optionals.containsKey(Properties.END_TIME)) {
      startDate = LocalDate.from(LocalDateTime.parse(start, FORMATTER));
    } else {
      startDate = LocalDate.parse(start, ISO_LOCAL_DATE);
    }
    while (repeatTimes > 0) {
      if (days.contains(startDate.getDayOfWeek())) {
        if (optionals.containsKey(Properties.END_TIME)) {
          optionals.replace(Properties.END_TIME,
                  startDate.format(ISO_LOCAL_DATE) + optionals.get(Properties.END_TIME).substring(10));
          eventSeries.add(this.createEvent(subject,
                  startDate.format(ISO_LOCAL_DATE) + start.substring(10), optionals));

        } else {
          eventSeries.add(this.createEvent(subject, startDate.format(ISO_LOCAL_DATE), optionals));
        }
        repeatTimes--;
      }
      startDate = startDate.plusDays(1);
    }
    this.eventSerieses.add(eventSeries);
  }

  // TODO
  @Override
  public IEvent editProperty(String subject, String start, String end, Properties property,
                             String newValue) {
    IEvent target = this.findEvent(subject, start, end);
    return this.editEvent(target, property, newValue);
  }

  // TODO
  @Override
  public void editProperties(String subject, String start, Properties properties, String newValue) {
    ArrayList<IEvent> targets;
    try {
      targets = this.findEventSeries(subject, start);
    } catch (Exception e) {
      IEvent target = this.findEvent(subject, start);
      this.editProperty(subject, target.getStartDateTime().format(FORMATTER),
              target.getEndDateTime().format(FORMATTER), properties, newValue);
      return;
    }

    if (properties == Properties.START_TIME) {
      ArrayList<IEvent> newSeries = new ArrayList<IEvent>();
      for (IEvent target : targets) {
        if (!target.getStartDateTime().isBefore(LocalDateTime.parse(start, FORMATTER))) {
          newSeries.add(this.editEvent(target, properties,
                  target.getStartDateTime().format(ISO_LOCAL_DATE) + newValue.substring(10)));
        }
      }
      this.eventSerieses.add(newSeries);
      targets.removeAll(newSeries);
    } else if (properties == Properties.END_TIME) {
      for (IEvent target : targets) {
        if (!target.getStartDateTime().isBefore(LocalDateTime.parse(start, FORMATTER))) {
          this.editEvent(target, properties,
                  target.getEndDateTime().format(ISO_LOCAL_DATE) + newValue.substring(10));
        }
      }
    } else {
      for (IEvent target : targets) {
        if (!target.getStartDateTime().isBefore(LocalDateTime.parse(start, FORMATTER))) {
          this.editEvent(target, properties, newValue);
        }
      }
    }
  }

  // TODO
  @Override
  public void editSeriesProperties(String subject, String start, Properties properties,
                                   String newValue) {
    ArrayList<IEvent> targets;
    try {
      targets = this.findEventSeries(subject, start);
    } catch (Exception e) {
      IEvent target = this.findEvent(subject, start);
      this.editProperty(subject, target.getStartDateTime().format(FORMATTER),
              target.getEndDateTime().format(FORMATTER), properties,
              target.getStartDateTime().format(ISO_LOCAL_DATE) + newValue.substring(10));
      return;
    }

    if (properties == Properties.START_TIME) {
      ArrayList<IEvent> newSeries = new ArrayList<IEvent>();
      for (IEvent target : targets) {
        newSeries.add(this.editEvent(target, properties, newValue));
      }
      this.eventSerieses.add(newSeries);
      targets.removeAll(newSeries);
    } else if (properties == Properties.END_TIME) {
      for (IEvent target : targets) {
        this.editEvent(target, properties,
                target.getEndDateTime().format(ISO_LOCAL_DATE) + newValue.substring(10));
      }
    } else {
      for (IEvent target : targets) {
        this.editEvent(target, properties, newValue);

      }
    }
  }

  @Override
  public String showEvents(String date) {
    StringBuilder result = new StringBuilder();
    LocalDate localDate = LocalDate.parse(date, ISO_LOCAL_DATE);
    for (IEvent event : this.events) {
      if (!localDate.isBefore(event.getStartDateTime().toLocalDate())
              && !localDate.isAfter(event.getEndDateTime().toLocalDate())) {
        if (result.length() > 0) {
          result.append(System.lineSeparator());
        }
        result.append(event.toString());
      } else if (!localDate.isAfter(event.getStartDateTime().toLocalDate())) {
        break;
      }
    }
    return result.toString();
  }

  @Override
  public String showEvents(String start, String end) {
    StringBuilder result = new StringBuilder();
    LocalDateTime startTime = LocalDateTime.parse(start, FORMATTER);
    LocalDateTime endTime = LocalDateTime.parse(end, FORMATTER);
    for (IEvent event : this.events) {
      if (!startTime.isAfter(event.getEndDateTime()) && !endTime.isBefore(event.getStartDateTime())) {
        if (result.length() > 0) {
          result.append(System.lineSeparator());
        }
        result.append(event.toString());
      } else if (endTime.isBefore(event.getStartDateTime())) {
        break;
      }
    }
    return result.toString();
  }

  @Override
  public String showStatus(String time) {
    LocalDateTime dateTime = LocalDateTime.parse(time, FORMATTER);
    for (IEvent event : this.events) {
      if (!dateTime.isBefore(event.getStartDateTime()) && !dateTime.isAfter(event.getEndDateTime())) {
        return "You have an event at that time";
      } else if (!dateTime.isAfter(event.getStartDateTime())) {
        break;
      }
    }
    return "You are free at that time";
  }

  // find an event with the targeted subject, start time and end time
// an IllegalArgumentException will throw if there is no such event
  private IEvent findEvent(String subject, String start, String end) {
    IEvent target =
            new Event.EventBuilder().setSubject(subject).setStart(LocalDateTime.parse(start,
                    FORMATTER)).setEnd(LocalDateTime.parse(end, FORMATTER)).build();
    for (IEvent event : events) {
      if (event.equals(target)) {
        return event;
      }
    }
    throw new IllegalArgumentException("No such event");
  }

  // find an event after the start date time with the targeted subject
// an IllegalArgumentException will throw if there is no such event
// an IllegalArgumentException will throw if multiple events satisfied the requirement
  private IEvent findEvent(String subject, String start) {
    IEvent target = null;
    for (IEvent event : events) {
      if (event.getSubject().equals(subject) && event.getStartDateTime().isAfter(LocalDateTime.parse(start, FORMATTER))) {
        if (target != null) {
          throw new IllegalArgumentException("Multiple events satisfied");
        }
        target = event;
      }
    }
    if (target == null) {
      throw new IllegalArgumentException("No such event");
    } else {
      return target;
    }
  }

  // find the event series after the start date with the targeted subject
// an IllegalArgumentException will throw if there is no such event series
  private ArrayList<IEvent> findEventSeries(String subject, String start) {
    for (ArrayList<IEvent> events : eventSerieses) {
      for (IEvent event : events) {
        if (event.getSubject().equals(subject) && LocalDateTime.parse(start, FORMATTER).isBefore(event.getStartDateTime())) {
          return events;
        }
      }
    }
    throw new IllegalArgumentException("No such Event Series");
  }

  // convert String weekDays to ArrayList<DayOfWeek>
// an IllegalArgumentException will throw if the character is invalid
  private ArrayList<DayOfWeek> convertWeekDays(String weekDays) {

    ArrayList<DayOfWeek> days = new ArrayList<DayOfWeek>();
    for (char c : weekDays.toCharArray()) {
      switch (c) {
        case 'M':
          days.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          days.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          days.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          days.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          days.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          days.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          days.add(DayOfWeek.SUNDAY);
          break;
        default:
          throw new IllegalArgumentException("Invalid weekday: " + c);
      }
    }
    if (days.isEmpty()) {
      throw new IllegalArgumentException("No available weekday");
    }
    return days;
  }

  // edit the property of the targeted event with the newValue
  // will return the updated IEvent if succeed
  private IEvent editEvent(IEvent target, Properties property, String newValue) {
    switch (property) {
      case SUBJECT:
        if (this.events.contains(
                new Event.EventBuilder()
                        .setSubject(newValue)
                        .setStart(target.getStartDateTime())
                        .setEnd(target.getEndDateTime()).build())) {
          throw new IllegalArgumentException("Event after editing already exists");
        }
        target.setSubject(newValue);
        break;
      case DESCRIPTION:
        target.setDescription(newValue);
        break;
      case STATUS:
        target.setStatus(Status.valueOf(newValue.toUpperCase()));
        break;
      case LOCATION:
        target.setLocation(Location.valueOf(newValue.toUpperCase()));
        break;
      case START_TIME:
        if (this.events.contains(
                new Event.EventBuilder()
                        .setSubject(target.getSubject())
                        .setStart(LocalDateTime.parse(newValue, FORMATTER))
                        .setEnd(target.getEndDateTime()).build())) {
          throw new IllegalArgumentException("Event after editing already exists");
        }
        if (!LocalDateTime.parse(newValue, FORMATTER).equals(target.getStartDateTime())) {
          target.setStartDateTime(LocalDateTime.parse(newValue, FORMATTER));
          Collections.sort(this.events);
        }
        break;
      case END_TIME:
        if (this.events.contains(
                new Event.EventBuilder()
                        .setSubject(target.getSubject())
                        .setStart(target.getStartDateTime())
                        .setEnd(LocalDateTime.parse(newValue, FORMATTER)).build())) {
          throw new IllegalArgumentException("Event after editing already exists");
        }
        target.setEndDateTime(LocalDateTime.parse(newValue, FORMATTER));
        Collections.sort(this.events);
        break;
      default:
        break;
    }
    return target;
  }
}
