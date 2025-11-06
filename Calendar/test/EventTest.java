import model.Event;
import model.Location;
import model.Status;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class EventTest {

  @Test
  public void testBuildEventWithStartAndEnd() {
    LocalDateTime start = LocalDateTime.parse("2025-06-05T09:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-05T10:00");
    Event e = new Event.EventBuilder()
            .setSubject("Meeting")
            .setStart(start)
            .setEnd(end)
            .build();
    String expected = "• An event: Meeting will be from 2025-06-05T09:00 to 2025-06-05T10:00.";
    assertEquals(expected, e.toString());
    assertEquals("Meeting", e.getSubject());
    assertEquals(start, e.getStartDateTime());
    assertEquals(end, e.getEndDateTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildEventMissingEndThrows() {
    new Event.EventBuilder()
            .setSubject("BadEvent")
            .setStart(LocalDateTime.parse("2025-06-05T09:00"))
            .build();
  }

  @Test
  public void testGetLocationAndStatusExceptions() {
    Event e = new Event.EventBuilder()
            .setSubject("NoLocStatus")
            .setFullDayDate(LocalDate.parse("2025-06-05"))
            .build();
    try {
      e.getLocation();
      fail("Expected IllegalStateException when calling getLocation() without setting location");
    } catch (IllegalStateException ignored) {
    }
    try {
      e.getStatus();
      fail("Expected IllegalStateException when calling getStatus() without setting status");
    } catch (IllegalStateException ignored) {
    }

    Event e2 = new Event.EventBuilder()
            .setSubject("WithLocStatus")
            .setStart(LocalDateTime.parse("2025-06-05T14:00"))
            .setEnd(LocalDateTime.parse("2025-06-05T15:00"))
            .setLocation("ONLINE")
            .setStatus("PUBLIC")
            .build();
    assertEquals(Location.ONLINE, e2.getLocation());
    assertEquals(Status.PUBLIC, e2.getStatus());
  }

  @Test
  public void testEqualsAndCompareTo() {
    Event e1 = new Event.EventBuilder()
            .setSubject("A")
            .setStart(LocalDateTime.parse("2025-06-05T09:00"))
            .setEnd(LocalDateTime.parse("2025-06-05T10:00"))
            .build();
    Event e2 = new Event.EventBuilder()
            .setSubject("A")
            .setStart(LocalDateTime.parse("2025-06-05T09:00"))
            .setEnd(LocalDateTime.parse("2025-06-05T10:00"))
            .build();
    Event e3 = new Event.EventBuilder()
            .setSubject("A")
            .setStart(LocalDateTime.parse("2025-06-05T09:00"))
            .setEnd(LocalDateTime.parse("2025-06-05T11:00"))
            .build();

    assertEquals(e1, e2);
    assertNotEquals(e1, e3);

    assertTrue(e1.compareTo(e3) < 0);
    assertTrue(e3.compareTo(e1) > 0);
    Event e4 = new Event.EventBuilder()
            .setSubject("B")
            .setStart(LocalDateTime.parse("2025-06-05T08:00"))
            .setEnd(LocalDateTime.parse("2025-06-05T09:00"))
            .build();
    assertTrue(e4.compareTo(e1) < 0);
  }

  @Test
  public void testToStringWithAllFieldsSet() {
    Event e = new Event.EventBuilder()
            .setSubject("Party")
            .setStart(LocalDateTime.parse("2025-06-10T20:00"))
            .setEnd(LocalDateTime.parse("2025-06-10T23:00"))
            .setDescription("Birthday celebration")
            .setLocation("PHYSICAL")
            .setStatus("PRIVATE")
            .build();
    String expected = "• A private event: Party will be from 2025-06-10T20:00 to 2025-06-10T23:00" +
            " in person.\n" +
            "\tDescription: Birthday celebration .";
    assertEquals(expected, e.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildEventWithoutEndThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-06-05T09:00");
    new Event.EventBuilder()
            .setSubject("NoEnd")
            .setStart(start)
            .build();
  }

  @Test
  public void testBuildFullDayEvent() {
    LocalDate fullDay = LocalDate.parse("2025-07-01");
    Event e = new Event.EventBuilder()
            .setSubject("FullDayEvent")
            .setFullDayDate(fullDay)
            .build();

    LocalDateTime expectedStart = fullDay.atTime(8, 0);
    LocalDateTime expectedEnd = fullDay.atTime(17, 0);

    assertEquals(expectedStart, e.getStartDateTime());
    assertEquals(expectedEnd, e.getEndDateTime());
    try {
      e.getStatus();
      fail("No IllegalStateException thrown when create event without setting status");
    } catch (IllegalStateException ignored) {
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildEventWithoutSubjectThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-08-10T14:00");
    LocalDateTime end = LocalDateTime.parse("2025-08-10T15:00");
    new Event.EventBuilder()
            .setSubject(null)
            .setStart(start)
            .setEnd(end)
            .build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildEventWithEmptySubjectThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-08-10T14:00");
    LocalDateTime end = LocalDateTime.parse("2025-08-10T15:00");
    new Event.EventBuilder()
            .setSubject("   ")
            .setStart(start)
            .setEnd(end)
            .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetStartDateTimeAfterEndThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-09-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-09-01T12:00");
    Event e = new Event.EventBuilder()
            .setSubject("AdjustStart")
            .setStart(start)
            .setEnd(end)
            .build();
    e.setStartDateTime(LocalDateTime.parse("2025-09-01T13:00"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetEndDateTimeBeforeStartThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-09-02T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-09-02T12:00");
    Event e = new Event.EventBuilder()
            .setSubject("AdjustEnd")
            .setStart(start)
            .setEnd(end)
            .build();
    e.setEndDateTime(LocalDateTime.parse("2025-09-02T09:00"));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetStatusUnspecifiedThrows() {
    LocalDateTime start = LocalDateTime.parse("2025-10-05T08:00");
    LocalDateTime end = LocalDateTime.parse("2025-10-05T09:00");
    Event e = new Event.EventBuilder()
            .setSubject("NoStatus")
            .setStart(start)
            .setEnd(end)
            .build();
    e.getStatus();
  }

  @Test
  public void testCompareTo() {
    LocalDateTime start1 = LocalDateTime.parse("2025-11-01T09:00");
    LocalDateTime end1 = LocalDateTime.parse("2025-11-01T10:00");
    Event e1 = new Event.EventBuilder()
            .setSubject("Event1")
            .setStart(start1)
            .setEnd(end1)
            .build();

    Event e2 = new Event.EventBuilder()
            .setSubject("Event2")
            .setStart(LocalDateTime.parse("2025-11-01T09:00"))
            .setEnd(LocalDateTime.parse("2025-11-01T11:00"))
            .build();
    assertTrue(e1.compareTo(e2) < 0);
    assertTrue(e2.compareTo(e1) > 0);

    Event e3 = new Event.EventBuilder()
            .setSubject("Event3")
            .setStart(LocalDateTime.parse("2025-10-31T16:00"))
            .setEnd(LocalDateTime.parse("2025-10-31T17:00"))
            .build();
    assertTrue(e3.compareTo(e1) < 0);
    assertTrue(e1.compareTo(e3) > 0);
  }



  @Test(expected = IllegalStateException.class)
  public void testBuilderNullStart() {
    new Event.EventBuilder().setSubject("Test").setStart(null).setEnd(LocalDateTime.parse("2025-06-01T10:00")).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderNullEnd() {
    new Event.EventBuilder().setSubject("Test").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuilderEndBeforeStart() {
    new Event.EventBuilder().setSubject("Test").setStart(LocalDateTime.parse("2025-06-01T11:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testBuilderEmptySubject() {
    new Event.EventBuilder().setSubject("   ").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).build();
  }

  @Test
  public void testEqualsAndHashCode() {
    Event e1 = new Event.EventBuilder().setSubject("Meeting").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).build();
    Event e2 = new Event.EventBuilder().setSubject("Meeting").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).build();
    assertEquals(e1, e2);
    assertEquals(e1.hashCode(), e2.hashCode());
  }

  @Test
  public void testCompareToIdenticalTimeDifferentDescription() {
    Event e1 = new Event.EventBuilder().setSubject("A").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).setDescription("Desc1").build();
    Event e2 = new Event.EventBuilder().setSubject("A").setStart(LocalDateTime.parse("2025-06-01T09:00")).setEnd(LocalDateTime.parse("2025-06-01T10:00")).setDescription("Desc2").build();
    assertEquals(0, e1.compareTo(e2));
  }

  @Test
  public void testCompareToDifferentTimes() {
    Event earlier = new Event.EventBuilder().setSubject("Early").setStart(LocalDateTime.parse("2025-06-01T08:00")).setEnd(LocalDateTime.parse("2025-06-01T09:00")).build();
    Event later = new Event.EventBuilder().setSubject("Late").setStart(LocalDateTime.parse("2025-06-01T10:00")).setEnd(LocalDateTime.parse("2025-06-01T11:00")).build();
    assertTrue(earlier.compareTo(later) < 0);
    assertTrue(later.compareTo(earlier) > 0);
  }
}

