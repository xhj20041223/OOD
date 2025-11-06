package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event implements IEvent, Comparable<IEvent> {

  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String description;
  private Location location;
  private Status status;

  private Event(String subject, LocalDateTime start, LocalDateTime end, String description,
                Location location, Status status) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.status = status;
  }

  public static class EventBuilder {
    private String subject;
    private LocalDateTime start;
    private LocalDate fullDayDate;
    private LocalDateTime end;
    private String description;
    private Location location;
    private Status status;

    public EventBuilder() {
      this.description = "";
      this.location = Location.NONSPECIFIC;
      this.status = Status.NONSPECIFIC;
    }

    public EventBuilder setSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public EventBuilder setStart(LocalDateTime start) {
      this.start = start;
      return this;
    }

    public EventBuilder setFullDayDate(LocalDate fullDayDate) {
      this.fullDayDate = fullDayDate;
      return this;
    }

    public EventBuilder setEnd(LocalDateTime end) {
      this.end = end;
      return this;
    }

    public EventBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    public EventBuilder setLocation(String location) {
      this.location = Location.valueOf(location.toUpperCase());
      return this;
    }

    public EventBuilder setStatus(String status) {
      this.status = Status.valueOf(status.toUpperCase());
      return this;
    }

    public Event build() {
      if (this.fullDayDate != null) {
        this.start = this.fullDayDate.atTime(8, 0);
        this.end = this.fullDayDate.atTime(17, 0);
      } else if (this.end == null) {
        throw new IllegalArgumentException("End time cannot be null if the event is not full day " +
                "event");
      }
      if (subject == null || start == null || subject.trim().isEmpty()) {
        throw new IllegalStateException("Event should have at least subject and start time");
      }

      if (this.end.isBefore(this.start)) {
        throw new IllegalArgumentException("End time cannot be before start time");
      }

      return new Event(this.subject, this.start, this.end, this.description, this.location,
              this.status);
    }
  }

  @Override
  public boolean equals(Object event) {
    if (event instanceof Event) {
      IEvent e = (IEvent) event;
      return (this.subject.equals((e.getSubject()))
              && this.getStartDateTime().equals(e.getStartDateTime())
              && this.getEndDateTime().equals(e.getEndDateTime()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.subject.hashCode() + this.getStartDateTime().hashCode() + this.getEndDateTime().hashCode();
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return this.start;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return this.end;
  }

  @Override
  public String getSubject() {
    return this.subject;
  }

  @Override
  public String getDescription() {
    if (this.description.isEmpty()) {
      throw new IllegalStateException("The description is empty");
    }
    return this.description;
  }

  @Override
  public Location getLocation() {
    if (this.location == Location.NONSPECIFIC) {
      throw new IllegalStateException("Event have not specified location");
    }
    return this.location;
  }

  @Override
  public Status getStatus() {
    if (this.status == Status.NONSPECIFIC) {
      throw new IllegalStateException("Event have not specified status");
    }
    return this.status;
  }

  @Override
  public int compareTo(IEvent event) {
    if (this.start.equals(event.getStartDateTime())) {
      return this.end.compareTo(event.getEndDateTime());
    }
    return this.start.compareTo(event.getStartDateTime());
  }

  @Override
  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Override
  public void setStartDateTime(LocalDateTime startDateTime) {
    if (!startDateTime.isBefore(this.end)) {
      throw new IllegalArgumentException("Start time cannot be before end time");
    }
    this.start = startDateTime;
  }

  @Override
  public void setEndDateTime(LocalDateTime endDateTime) {
    if (!endDateTime.isAfter(this.start)) {
      throw new IllegalArgumentException("Start time cannot be before end time");
    }
    this.end = endDateTime;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    switch (this.status) {
      case NONSPECIFIC:
        str.append("• An event: ");
        break;
      case PRIVATE:
        str.append("• A private event: ");
        break;
      case PUBLIC:
        str.append("• A public event: ");
        break;
      default:
        break;
    }
    str.append(this.subject + " will be from " + this.start + " to " + this.end);
    switch (this.location) {
      case ONLINE:
        str.append(" online.");
        break;
      case PHYSICAL:
        str.append(" in person.");
        break;
      default:
        str.append(".");
        break;
    }
    if (!this.description.isEmpty()) {
      str.append("\n\tDescription: " + this.description + " .");
    }
    return str.toString();
  }
}
