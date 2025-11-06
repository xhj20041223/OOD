package model;

import java.time.LocalDateTime;

public interface IEvent extends Comparable<IEvent> {
  // Compare two events is equal or not with the same subject, start date/time and end date/time.
  public LocalDateTime getStartDateTime();
  public LocalDateTime getEndDateTime();
  public String getSubject();
  public String getDescription();
  public Location getLocation();
  public Status getStatus();
  public int compareTo(IEvent event);
  public void setSubject(String subject);
  public void setStartDateTime(LocalDateTime startDateTime);
  public void setEndDateTime(LocalDateTime endDateTime);
  public void setDescription(String description);
  public void setLocation(Location location);
  public void setStatus(Status status);
}
