import model.IEvent;
import model.Properties;
import model.Calendar;
import model.UserCalendar;
import model.CalendarProperties;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * tests for UserCalendar class.
 */
public class UserCalendarTest {

  @Test
  public void testEventSeriesCreationAndEdits() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();

    optionals.put(Properties.END_TIME, "2025-05-05T11:00");
    cal.createEventSeries(
            "First", "2025-05-05T10:00", optionals, "MW", 6);

    assertEquals("• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: First " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: First will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: First will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: First will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    cal.editProperties("First", "2025-05-12T10:00", Properties.SUBJECT, "Second");

    assertEquals("• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Second " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: Second will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: Second will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: Second will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    cal.editSeriesProperties("First", "2025-05-05T10:00", Properties.SUBJECT, "Third");

    assertEquals("• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    cal.editProperties("Third", "2025-05-12T10:00", Properties.START_TIME, "2025-05-12T10" +
            ":30");

    assertEquals("• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    cal.editProperties("Third", "2025-05-05T10:00", Properties.SUBJECT, "Fourth");

    assertEquals("• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    cal.editSeriesProperties("Third", "2025-05-12T10:30", Properties.SUBJECT, "Fifth");

    assertEquals("• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Fifth " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Fifth will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Fifth will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Fifth will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", cal.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventEndBeforeStartThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-05-05T08:00");
    cal.createEvent("InvalidTime", "2025-05-05T10:00", optionals);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-10T12:00");
    cal.createEvent("DupEvent", "2025-06-10T10:00", optionals);
    cal.createEvent("DupEvent", "2025-06-10T10:00", optionals);
  }

  @Test
  public void testCreateFullDayEvent() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    cal.createEvent("Holiday", "2025-07-04", optionals);

    String result = cal.showEvents("2025-07-04");
    assertEquals("• An event: Holiday will be from 2025-07-04T08:00 to 2025-07-04T17:00.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidWeekdayThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    cal.createEventSeries("SeriesErr", "2025-08-01T10:00",
            optionals, "MXF", "2025-08-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNoSuchEventThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-09-01T12:00");
    cal.createEvent("ExistEvent", "2025-09-01T10:00", optionals);
    cal.editProperty("NotExists", "2025-09-01T10:00",
            "2025-09-01T12:00", Properties.DESCRIPTION, "NewDesc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventMultipleMatchesThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-09-11T09:00");
    cal.createEvent("Multi", "2025-09-11T08:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-09-12T10:00");
    cal.createEvent("Multi", "2025-09-12T09:00", optionals2);

    cal.editProperty("Multi",
            "2025-09-10T08:00", "2025-09-11T09:00",
            Properties.DESCRIPTION, "Desc");
  }

  @Test
  public void testShowEventsDateRangeNoEventsReturnsEmpty() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-10-01T11:00");
    cal.createEvent("Single", "2025-10-01T10:00", optionals);

    String result = cal.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("", result);
  }

  @Test
  public void testShowStatusFreeAndBusy() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-11-05T11:00");
    cal.createEvent("CheckStatus", "2025-11-05T10:00", optionals);

    String busy = cal.showStatus("2025-11-05T10:30");
    assertEquals("You have an event at that time", busy);

    String free = cal.showStatus("2025-11-05T12:00");
    assertEquals("You are free at that time", free);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventDuplicateAfterEditThrows() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-12-01T11:00");
    cal.createEvent("A", "2025-12-01T10:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-12-01T11:00");
    cal.createEvent("B", "2025-12-01T10:00", optionals2);

    cal.editProperty("B", "2025-12-01T10:00", "2025-12-01T11:00",
            Properties.SUBJECT, "A");
  }


  @Test
  public void testSingleEventCreationAndShow() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-08-20T15:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "Desc");
    opts.put(Properties.STATUS, "private");
    IEvent single = cal.createEvent("Solo", "2025-08-20T14:00", opts);
    String result = cal.showEvents("2025-08-20");
    assertEquals("• A private event: Solo will be from 2025-08-20T14:00 to" +
            " 2025-08-20T15:00 in person.\nDescription: Desc.", result);
  }

  @Test(expected = DateTimeParseException.class)
  public void testShowEventsInvalidDateFormat() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    cal.showEvents("2025-13-01");
  }

  @Test
  public void testShowEventsRangeMultiDayOverlap() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");

    cal.createEvent("OverlapBefore", "2025-10-01T23:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T01:00");
      }
    });

    cal.createEvent("Inside", "2025-10-02T10:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T11:00");
      }
    });

    cal.createEvent("After", "2025-10-03T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-03T10:00");
      }
    });

    String result = cal.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertTrue(result.contains("OverlapBefore"));
    assertTrue(result.contains("Inside"));
    assertFalse(result.contains("After"));
  }

  @Test
  public void testShowStatusAtBoundaries() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    cal.createEvent("Boundary", "2025-11-05T10:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-11-05T11:00");
      }
    });
    String atStart = cal.showStatus("2025-11-05T10:00");
    String atEnd = cal.showStatus("2025-11-05T11:01");
    assertEquals("You have an event at that time", atStart);
    assertEquals("You are free at that time", atEnd);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNonExistentEvent() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    cal.editProperties("NoEvent", "2025-01-01T00:00", Properties.SUBJECT, "NewSubject");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidPattern() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    cal.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "XYZ", 5);
  }

  @Test
  public void testCreateEventSeriesNonPositiveCount() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    cal.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "M", 0);
    assertEquals("", cal.showEvents("2025-07-01T09:00", "2025-08-01T09:00"));
  }

  @Test
  public void testEditCalendarName() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Work");
    assertEquals("Work", cal.getCalendarName());
    cal.editProperty(CalendarProperties.NAME, "Personal");
    assertEquals("Personal", cal.getCalendarName());
  }

  @Test(expected = DateTimeException.class)
  public void testEditCalendarInvalidTimezoneThrows() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Work");
    // invalid timezone should throw DateTimeException
    cal.editProperty(CalendarProperties.TIMEZONE, "Invalid/Zone");
  }

  @Test
  public void testCopySingleEventToDifferentCalendar() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "Src");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "Dst");

    // create event in source
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T10:00");
    src.createEvent("Meeting", "2025-06-15T09:00", opts);

    // copy event to destination at new time
    LocalDateTime srcTime = LocalDateTime.parse("2025-06-15T09:00");
    LocalDateTime dstTime = LocalDateTime.parse("2025-07-01T10:00");
    src.copyEvent("Meeting", srcTime, dst, dstTime);

    String result = dst.showEvents("2025-07-01");
    assertEquals("• An event: Meeting will be from 2025-07-01T10:00 to 2025-07-01T11:00.", result);
  }

  @Test
  public void testCopyEventsOnDate() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "Src");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "Dst");

    HashMap<Properties, String> opts1 = new HashMap<>();
    opts1.put(Properties.END_TIME, "2025-06-15T10:00");
    src.createEvent("EventA", "2025-06-15T09:00", opts1);
    HashMap<Properties, String> opts2 = new HashMap<>();
    opts2.put(Properties.END_TIME, "2025-06-15T11:00");
    src.createEvent("EventB", "2025-06-15T10:00", opts2);

    src.copyEvents(LocalDate.parse("2025-06-15"), dst, LocalDate.parse("2025-07-01"));

    String out = dst.showEvents("2025-07-01");
    assertEquals("• An event: EventA will be from 2025-07-01T09:00 to 2025-07-01T10:00." +
            "\n• An event: EventB will be from 2025-07-01T10:00 to 2025-07-01T11:00.", out);
  }

  @Test
  public void testCopyEventsBetweenDates() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "Src");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "Dst");

    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-21T12:00");
    src.createEvent("A", "2025-06-15T11:00", opts);
    src.createEvent("B", "2025-06-20T14:00", opts);

    // copy events between June 14 and 20 to start at July 1
    src.copyEvents(LocalDate.parse("2025-06-14"), LocalDate.parse("2025-06-20"), dst,
            LocalDate.parse("2025-07-01"));

    String out = dst.showEvents("2025-07-01T00:00", "2025-07-31T23:59");
    assertEquals("• An event: A will be from 2025-07-02T11:00 to 2025-07-08T12:00.\n" +
            "• An event: B will be from 2025-07-07T14:00 to 2025-07-08T12:00.", out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyNonexistentEventThrows() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "C");
    UserCalendar target = new UserCalendar(ZoneId.of("America/New_York"), "T");
    // no such event in src
    cal.copyEvent("X", LocalDateTime.parse("2025-01-01T00:00"), target,
            LocalDateTime.parse("2025-02-01T00:00"));
  }


  @Test
  public void testCopySingleEventToSameCalendar() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "Src");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "Dst");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T10:00");
    src.createEvent("Meeting", "2025-06-15T09:00", opts);
    LocalDateTime srcTime = LocalDateTime.parse("2025-06-15T09:00");
    LocalDateTime dstTime = LocalDateTime.parse("2025-07-01T10:00");
    src.copyEvent("Meeting", srcTime, dst, dstTime);
    String out = dst.showEvents("2025-07-01");
    assertEquals("• An event: Meeting will be from 2025-07-01T10:00 to 2025-07-01T11:00.",
            out);
  }

  @Test
  public void testCopySingleEventToDifferentTimezoneCalendar() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "SrcEST");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/Los_Angeles"), "DstPST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T10:00");
    src.createEvent("EventC", "2025-06-15T09:00", opts);
    src.copyEvent("EventC", LocalDateTime.parse("2025-06-15T09:00"), dst,
            LocalDateTime.parse("2025-07-01T00:00"));
    String out = dst.showEvents("2025-07-01");
    assertEquals("• An event: EventC will be from 2025-07-01T00:00 to 2025-07-01T01:00.",
            out);
  }

  @Test
  public void testCopyEventsOnDateWithTimezoneConversion() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "SrcEST");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/Los_Angeles"), "DstPST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T10:00");
    src.createEvent("EventD", "2025-06-15T09:00", opts);
    src.copyEvents(LocalDate.parse("2025-06-15"), dst, LocalDate.parse("2025-07-01"));
    String out = dst.showEvents("2025-07-01");
    assertEquals("• An event: EventD will be from 2025-07-01T06:00 to 2025-07-01T07:00.",
            out);
  }


  @Test
  public void testCopySeriesSubsetAndEditSeries() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "SrcSeries");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "DstSeries");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-03T10:00");
    opts.put(Properties.STATUS, "private");
    opts.put(Properties.LOCATION, "online");
    src.createEventSeries("SeriesEvent", "2025-06-01T09:00", opts, "MTWRFSU",
            3);
    src.copyEvents(LocalDate.parse("2025-06-02"), LocalDate.parse("2025-06-03"), dst,
            LocalDate.parse("2025-07-01"));
    String out = dst.showEvents("2025-07-01T00:00", "2025-07-31T23:59");
    assertTrue(out.contains("2025-07-01T09:00"));
    assertTrue(out.contains("2025-07-02T09:00"));
    // edit entire series in destination
    dst.editSeriesProperties("SeriesEvent", "2025-07-01T09:00", Properties.SUBJECT,
            "SeriesRenamed");
    String updated = dst.showEvents("2025-07-01T00:00", "2025-07-31T23:59");
    assertEquals("• A private event: SeriesRenamed will be from 2025-07-01T09:00 to" +
            " 2025-07-01T10:00 online.\n• A private event: SeriesRenamed will be from " +
            "2025-07-02T09:00 to 2025-07-02T10:00 online.", updated);
  }

  @Test
  public void testEditPropertySingleEventSuccess() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-09-01T11:00");
    optionals.put(Properties.LOCATION, "online");
    cal.createEvent("Solo", "2025-09-01T10:00", optionals);
    IEvent changed = cal.editProperty("Solo", "2025-09-01T10:00",
            "2025-09-01T11:00", Properties.LOCATION, "physical");
    String result = cal.showEvents("2025-09-01");
    assertEquals("• An event: Solo will be from 2025-09-01T10:00 to " +
            "2025-09-01T11:00 in person.", result);
  }

  @Test
  public void testShowEventsRangeNormal() {
    Calendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> opts1 = new HashMap<>();
    opts1.put(Properties.END_TIME, "2025-08-01T11:00");
    cal.createEvent("A", "2025-08-01T10:00", opts1);
    HashMap<Properties, String> opts2 = new HashMap<>();
    opts2.put(Properties.END_TIME, "2025-08-02T12:00");
    cal.createEvent("B", "2025-08-02T11:00", opts2);
    String out = cal.showEvents("2025-08-01T00:00", "2025-08-03T00:00");
    assertEquals("• An event: A will be from 2025-08-01T10:00 to 2025-08-01T11:00." +
            "\n• An event: B will be from 2025-08-02T11:00 to 2025-08-02T12:00.", out);
  }

  @Test
  public void testEditCalendarValidTimezone() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("E", "2025-06-01T09:00", optionals);
    cal.editProperty(CalendarProperties.TIMEZONE, "America/Los_Angeles");
    String result = cal.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00.",
            result);
  }


  @Test
  public void testZeroDurationEvent() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("Solo", "2025-06-01T10:00", opts);
    String result = cal.showEvents("2025-06-01");
    assertEquals("• An event: Solo will be from 2025-06-01T10:00 to 2025-06-01T10:00.", result);
  }

  @Test
  public void testMultiDayEventAcrossMidnight() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-02T02:00");
    cal.createEvent("Night", "2025-06-01T22:00", opts);
    String r1 = cal.showEvents("2025-06-01");
    String r2 = cal.showEvents("2025-06-02");
    assertEquals("• An event: Night will be from 2025-06-01T22:00 to 2025-06-02T02:00.", r1);
    assertEquals("• An event: Night will be from 2025-06-01T22:00 to 2025-06-02T02:00.", r2);
  }

  @Test
  public void testLeapDayEvent() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2024-02-29T12:00");
    cal.createEvent("Leap", "2024-02-29T10:00", opts);
    String result = cal.showEvents("2024-02-29");
    assertEquals("• An event: Leap will be from 2024-02-29T10:00 to 2024-02-29T12:00.", result);
  }

  @Test
  public void testDSTStartEvent() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-03-09T03:30");
    cal.createEvent("DST", "2025-03-09T01:30", opts);
    String result = cal.showEvents("2025-03-09");
    assertEquals("• An event: DST will be from 2025-03-09T01:30 to 2025-03-09T03:30.", result);
  }

  @Test
  public void testDSTEndEvent() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-11-02T02:30");
    cal.createEvent("DST", "2025-11-02T01:00", opts);
    String result = cal.showEvents("2025-11-02");
    assertEquals("• An event: DST will be from 2025-11-02T01:00 to 2025-11-02T02:30.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditConflictOverlap() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("A", "2025-06-01T08:00", opts);
    cal.createEvent("B", "2025-06-01T10:00", opts);
    cal.editProperty("B", "2025-06-01T10:00", "2025-06-01T11:00",
            Properties.START_TIME, "2025-06-01T09:00");
  }

  @Test
  public void testSimultaneousStartEndEdit() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-07-01T11:00");
    cal.createEvent("X", "2025-07-01T10:00", opts);
    cal.editProperty("X", "2025-07-01T10:00", "2025-07-01T11:00",
            Properties.START_TIME, "2025-07-01T09:30");
    cal.editProperty("X", "2025-07-01T09:30", "2025-07-01T11:00",
            Properties.END_TIME, "2025-07-01T11:30");
    String result = cal.showEvents("2025-07-01");
    assertEquals("• An event: X will be from 2025-07-01T09:30 to 2025-07-01T11:30.", result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEditEmptySubject() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("E", "2025-06-01T09:00", opts);
    cal.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00",
            Properties.SUBJECT, "");
  }

  @Test
  public void testFullSeriesCopy() {
    UserCalendar src = new UserCalendar(ZoneId.of("America/New_York"), "Src");
    UserCalendar dst = new UserCalendar(ZoneId.of("America/New_York"), "Dst");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    src.createEventSeries("S", "2025-06-01T09:00", opts, "M", "2025-06-15");
    src.copyEvents(LocalDate.parse("2025-06-01"), LocalDate.parse("2025-06-15"), dst,
            LocalDate.parse("2025-06-01"));
    String outSrc = src.showEvents("2025-06-01T00:00", "2025-06-15T23:59");
    String outDst = dst.showEvents("2025-06-01T00:00", "2025-06-15T23:59");
    assertEquals(outSrc, outDst);
  }


  @Test
  public void testOptionalFieldsDisplay() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "Meeting");
    opts.put(Properties.STATUS, "PRIVATE");
    cal.createEvent("M", "2025-06-01T09:00", opts);
    String out = cal.showEvents("2025-06-01");
    assertEquals(
            "• A private event: M will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.\n" +
                    "Description: Meeting.",
            out
    );
  }

  @Test(expected = DateTimeParseException.class)
  public void testInvalidOptionalFieldValue() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.STATUS, "UNKNOWN");
    cal.createEvent("E", "2025-06-01T09:00", opts);
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputs() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    cal.createEvent(null, null, null);
  }

  @Test(expected = DateTimeParseException.class)
  public void testDateFormatVariation() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("E", "2025/06/01 09:00", opts);
  }

  @Test
  public void testExceptionMessageMatch() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T08:00");
    try {
      cal.createEvent("E", "2025-06-01T09:00", opts);
      throw new AssertionError("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("End time should be after the start time", e.getMessage());
    }
  }

  @Test
  public void testAllDayEventCreation() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    cal.createEvent("AD", "2025-06-10", opts);
    String result = cal.showEvents("2025-06-10");
    assertEquals("• An event: AD will be from 2025-06-10T08:00 to 2025-06-10T17:00.", result);
  }

  @Test
  public void testSeriesSameDayEvents() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEventSeries("TS", "2025-06-01T09:00", opts, "MW", "2025-06-10");
    String out = cal.showEvents("2025-06-02T00:00", "2025-06-04T23:59");
    String expected =
            "• An event: TS will be from 2025-06-02T09:00 to 2025-06-02T10:00.\n" +
                    "• An event: TS will be from 2025-06-04T09:00 to 2025-06-04T10:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testSeriesRepeatsOnWeekdays() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    cal.createEventSeries("WD", "2025-06-01", opts, "TR", "2025-06-07");
    String out = cal.showEvents("2025-06-01T00:00", "2025-06-07T23:59");
    String expected =
            "• An event: WD will be from 2025-06-03T08:00 to 2025-06-03T17:00.\n" +
                    "• An event: WD will be from 2025-06-05T08:00 to 2025-06-05T17:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testEditSingleProperty() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("E", "2025-06-01T09:00", opts);
    cal.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00", Properties.LOCATION, "physical");
    String out = cal.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.", out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditAllBySubject() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    cal.createEvent("A", "2025-06-01", opts);
    cal.createEvent("A", "2025-06-02", opts);
    cal.editProperties("A", "2025-06-01T08:00", Properties.STATUS, "PRIVATE");

  }

  @Test
  public void testEditFromSpecificDateTime() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    cal.createEventSeries("S", "2025-06-01", opts, "MW", "2025-06-08");
    cal.editProperties("S", "2025-06-04T08:00", Properties.STATUS, "PRIVATE");
    String out = cal.showEvents("2025-06-02T00:00", "2025-06-04T23:59");
    String expected =
            "• An event: S will be from 2025-06-02T08:00 to 2025-06-02T17:00.\n" +
                    "• A private event: S will be from 2025-06-04T08:00 to 2025-06-04T17:00.";
    assertEquals(expected, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditInvalidPropertyValue() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    cal.createEvent("E", "2025-06-01T09:00", opts);
    cal.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00", Properties.STATUS, "UNKNOWN");
  }

  @Test
  public void testQueryEventsOnSpecificDay() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-02T10:00");
    cal.createEvent("A", "2025-06-01T09:00", opts);
    cal.createEvent("B", "2025-06-02T09:00", opts);
    String out = cal.showEvents("2025-06-01");
    assertEquals("• An event: A will be from 2025-06-01T09:00 to 2025-06-02T10:00.", out);
  }

  @Test
  public void testPrintDetailsForPublicEvent() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "desc");
    opts.put(Properties.STATUS, "PUBLIC");
    cal.createEvent("E", "2025-06-01T09:00", opts);
    String out = cal.showEvents("2025-06-01");
    assertEquals(
            "• A public event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.\n" +
                    "Description: desc.",
            out
    );
  }

  @Test
  public void testPrintFormatMultipleEvents() {
    UserCalendar cal = new UserCalendar(ZoneId.of("America/New_York"), "Cal");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T12:00");
    cal.createEvent("A", "2025-06-01T09:00", opts);
    cal.createEvent("B", "2025-06-01T11:00", opts);
    String out = cal.showEvents("2025-06-01");
    String expected =
            "• An event: A will be from 2025-06-01T09:00 to 2025-06-01T12:00.\n" +
                    "• An event: B will be from 2025-06-01T11:00 to 2025-06-01T12:00.";
    assertEquals(expected, out);
  }

}
