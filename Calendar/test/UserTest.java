import model.IEvent;
import model.Properties;
import model.SingleUser;
import model.CalendarProperties;

import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.time.DateTimeException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * tests for User class.
 */
public class UserTest {

  @Test
  public void testEventSeriesCreationAndEdits() {
    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();

    optionals.put(Properties.END_TIME, "2025-05-05T11:00");
    singleUser.createEventSeries(
            "First", "2025-05-05T10:00", optionals, "MW", 6);


    assertEquals("• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: First " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: First will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: First will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: First will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    singleUser.editProperties("First", "2025-05-12T10:00", Properties.SUBJECT, "Second");

    assertEquals("• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Second " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: Second will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: Second will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: Second will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    singleUser.editSeriesProperties("First", "2025-05-05T10:00", Properties.SUBJECT, "Third");

    assertEquals("• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:00 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:00 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:00 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:00 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    singleUser.editProperties("Third", "2025-05-12T10:00", Properties.START_TIME, "2025-05-12T10" +
            ":30");

    assertEquals("• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    singleUser.editProperties("Third", "2025-05-05T10:00", Properties.SUBJECT, "Fourth");

    assertEquals("• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Third " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Third will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Third will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Third will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));

    singleUser.editSeriesProperties("Third", "2025-05-12T10:30", Properties.SUBJECT, "Fifth");

    assertEquals("• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.\n• An " +
            "event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.\n• An event: Fifth " +
            "will be from 2025-05-12T10:30 to 2025-05-12T11:00.\n• An event: Fifth will be from " +
            "2025-05-14T10:30 to 2025-05-14T11:00.\n• An event: Fifth will be from " +
            "2025-05-19T10:30 to 2025-05-19T11:00.\n• An event: Fifth will be from " +
            "2025-05-21T10:30 to 2025-05-21T11:00.", singleUser.showEvents("2025-05-01T10:00",
            "2025-05-25T10:00"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventEndBeforeStartThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-05-05T08:00");
    singleUser.createEvent("InvalidTime", "2025-05-05T10:00", optionals);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-10T12:00");
    singleUser.createEvent("DupEvent", "2025-06-10T10:00", optionals);
    singleUser.createEvent("DupEvent", "2025-06-10T10:00", optionals);
  }

  @Test
  public void testCreateFullDayEvent() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    singleUser.createEvent("Holiday", "2025-07-04", optionals);

    String result = singleUser.showEvents("2025-07-04");
    assertEquals("• An event: Holiday will be from 2025-07-04T08:00 to 2025-07-04T17:00.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidWeekdayThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    singleUser.createEventSeries("SeriesErr", "2025-08-01T10:00",
            optionals, "MXF", "2025-08-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNoSuchEventThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-09-01T12:00");
    singleUser.createEvent("ExistEvent", "2025-09-01T10:00", optionals);
    singleUser.editProperty("NotExists", "2025-09-01T10:00",
            "2025-09-01T12:00", Properties.DESCRIPTION, "NewDesc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventMultipleMatchesThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-09-11T09:00");
    singleUser.createEvent("Multi", "2025-09-11T08:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-09-12T10:00");
    singleUser.createEvent("Multi", "2025-09-12T09:00", optionals2);

    singleUser.editProperty("Multi",
            "2025-09-10T08:00", "2025-09-11T09:00",
            Properties.DESCRIPTION, "Desc");
  }

  @Test
  public void testShowEventsDateRangeNoEventsReturnsEmpty() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-10-01T11:00");
    singleUser.createEvent("Single", "2025-10-01T10:00", optionals);

    String result = singleUser.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("", result);
  }

  @Test
  public void testShowStatusFreeAndBusy() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-11-05T11:00");
    singleUser.createEvent("CheckStatus", "2025-11-05T10:00", optionals);

    String busy = singleUser.showStatus("2025-11-05T10:30");
    assertEquals("You have an event at that time", busy);

    String free = singleUser.showStatus("2025-11-05T12:00");
    assertEquals("You are free at that time", free);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventDuplicateAfterEditThrows() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-12-01T11:00");
    singleUser.createEvent("A", "2025-12-01T10:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-12-01T11:00");
    singleUser.createEvent("B", "2025-12-01T10:00", optionals2);

    singleUser.editProperty("B", "2025-12-01T10:00", "2025-12-01T11:00",
            Properties.SUBJECT, "A");
  }


  @Test
  public void testSingleEventCreationAndShow() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-08-20T15:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "Desc");
    opts.put(Properties.STATUS, "private");
    IEvent single = singleUser.createEvent("Solo", "2025-08-20T14:00", opts);
    String result = singleUser.showEvents("2025-08-20");
    assertEquals("• A private event: Solo will be from 2025-08-20T14:00 to 2025-08-20T15:00 in " +
            "person.\nDescription: Desc.", result);
  }

  @Test(expected = DateTimeParseException.class)
  public void testShowEventsInvalidDateFormat() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.showEvents("2025-13-01");
  }

  @Test
  public void testShowEventsRangeMultiDayOverlap() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");

    singleUser.createEvent("OverlapBefore", "2025-10-01T23:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T01:00");
      }
    });

    singleUser.createEvent("Inside", "2025-10-02T10:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T11:00");
      }
    });

    singleUser.createEvent("After", "2025-10-03T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-03T10:00");
      }
    });

    String result = singleUser.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("• An event: OverlapBefore will be from 2025-10-01T23:00 to 2025-10-02T01:00.\n•" +
            " An event: Inside will be from 2025-10-02T10:00 to 2025-10-02T11:00.", result);
  }

  @Test
  public void testShowStatusAtBoundaries() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.createEvent("Boundary", "2025-11-05T10:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-11-05T11:00");
      }
    });
    String atStart = singleUser.showStatus("2025-11-05T10:00");
    String atEnd = singleUser.showStatus("2025-11-05T11:01");
    assertEquals("You have an event at that time", atStart);
    assertEquals("You are free at that time", atEnd);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNonExistentEvent() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.editProperties("NoEvent", "2025-01-01T00:00", Properties.SUBJECT, "NewSubject");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidPattern() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "XYZ", 5);
  }

  @Test
  public void testCreateEventSeriesNonPositiveCount() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    singleUser.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "M", 0);
    assertEquals("", singleUser.showEvents("2025-07-01T09:00", "2025-08-01T09:00"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUseNonexistentCalendarThrows() {
    SingleUser user = new SingleUser();
    user.useCalendar("NoCal");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDuplicateCalendarThrows() {
    SingleUser user = new SingleUser();
    user.createCalendar("C1", "America/New_York");
    user.createCalendar("C1", "Europe/London");
  }

  @Test(expected = DateTimeException.class)
  public void testCreateCalendarInvalidTimezoneThrows() {
    SingleUser user = new SingleUser();
    user.createCalendar("C1", "Invalid/Zone");
  }

  @Test(expected = IllegalStateException.class)
  public void testEditCancerNonexistentCalendarThrows() {
    SingleUser user = new SingleUser();
    user.editCalendar("NoCal", CalendarProperties.NAME, "NewName");
  }

  @Test(expected = IllegalStateException.class)
  public void testEditCalendarNameToDuplicateThrows() {
    SingleUser user = new SingleUser();
    user.createCalendar("A", "America/New_York");
    user.createCalendar("B", "America/New_York");
    user.editCalendar("A", CalendarProperties.NAME, "B");
  }

  @Test(expected = DateTimeException.class)
  public void testEditCalendarTimezone() {
    SingleUser user = new SingleUser();
    user.createCalendar("C", "America/New_York");
    user.editCalendar("C", CalendarProperties.TIMEZONE, "Invalid/Zone");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateEventWithoutCurrentCalendarThrows() {
    SingleUser user = new SingleUser();
    user.createEvent("Evt", "2025-06-01T10:00", new HashMap<>());
  }

  @Test(expected = IllegalStateException.class)
  public void testCopyEventWithoutCurrentCalendarThrows() {
    SingleUser user = new SingleUser();
    user.copyEvent("X", "2025-06-01T10:00", "Any", "2025-07-01T10:00");
  }

  @Test(expected = IllegalStateException.class)
  public void testCopyEventToNonexistentTargetCalendarThrows() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.useCalendar("Src");
    user.copyEvent("X", "2025-06-01T10:00", "NoCal", "2025-07-01T10:00");
  }

  @Test(expected = IllegalStateException.class)
  public void testCopyEventsWithoutCurrentCalendarThrows() {
    SingleUser user = new SingleUser();
    user.copyEvents("2025-06-01", "Any", "2025-07-01");
  }

  @Test
  public void testCopyEventAcrossCalendars() {
    SingleUser user = new SingleUser();
    user.createCalendar("C1", "America/New_York");
    user.createCalendar("C2", "America/New_York");
    user.useCalendar("C1");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T11:00");
    user.createEvent("E", "2025-06-15T10:00", opts);
    user.copyEvent("E", "2025-06-15T10:00", "C2", "2025-07-01T12:00");
    user.useCalendar("C2");
    String out = user.showEvents("2025-07-01");
    assertEquals("• An event: E will be from 2025-07-01T12:00 to 2025-07-01T13:00.", out);
  }

  @Test
  public void testCopyEventsOnDateThroughUser() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.createCalendar("Dst", "America/New_York");
    user.useCalendar("Src");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-20T12:00");
    user.createEvent("A", "2025-06-18T09:00", opts);
    user.copyEvents("2025-06-18", "Dst", "2025-07-01");
    user.useCalendar("Dst");
    String out = user.showEvents("2025-07-01");
    assertEquals("• An event: A will be from 2025-07-01T09:00 to 2025-07-01T12:00.", out);
  }

  @Test(expected = DateTimeException.class)
  public void testEditCalendarTimezoneThrows() {
    SingleUser user = new SingleUser();
    user.createCalendar("C", "America/New_York");
    user.editCalendar("C", CalendarProperties.TIMEZONE, "Invalid/Zone");
  }

  @Test
  public void testCopySingleEventAcrossCalendarsWithTimezoneConversion() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.createCalendar("Dst", "America/Los_Angeles");
    user.useCalendar("Src");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-15T11:00");
    user.createEvent("E", "2025-06-15T10:00", opts);
    user.copyEvent("E", "2025-06-15T10:00", "Dst", "2025-07-01T00:00");
    user.useCalendar("Dst");
    String out = user.showEvents("2025-07-01");
    assertEquals("• An event: E will be from 2025-07-01T00:00 to 2025-07-01T01:00.", out);
  }

  @Test
  public void testCopyEventsOnDateThroughUserWithTimezone() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.createCalendar("Dst", "America/Los_Angeles");
    user.useCalendar("Src");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-18T10:00");
    user.createEvent("A", "2025-06-18T09:00", opts);
    user.copyEvents("2025-06-18", "Dst", "2025-07-01");
    user.useCalendar("Dst");
    String out = user.showEvents("2025-07-01");
    assertEquals("• An event: A will be from 2025-07-01T06:00 to 2025-07-01T07:00.", out);
  }

  @Test
  public void testCopySeriesSubsetAcrossCalendarsThroughUser() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.createCalendar("Dst", "America/Los_Angeles");
    user.useCalendar("Src");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-03T10:00");
    user.createEventSeries("SeriesEvent", "2025-06-01T09:00", opts, "MTWRFSU", 3);
    user.copyEvents("2025-06-02", "2025-06-03", "Dst", "2025-07-01");
    user.useCalendar("Dst");
    String out = user.showEvents("2025-07-01T00:00", "2025-07-31T23:59");
    assertEquals("• An event: SeriesEvent will be from 2025-07-01T06:00 to 2025-07-01T07:00.\n• " +
            "An event: SeriesEvent will be from 2025-07-02T06:00 to 2025-07-02T07:00.", out);
  }

  @Test
  public void testEditSeriesAfterCopyThroughUser() {
    SingleUser user = new SingleUser();
    user.createCalendar("Src", "America/New_York");
    user.createCalendar("Dst", "America/New_York");
    user.useCalendar("Src");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-03T10:00");
    user.createEventSeries("SeriesEvent", "2025-06-01T09:00", opts, "MTWRFSU", 3);
    user.copyEvents("2025-06-02", "2025-06-03", "Dst", "2025-07-01");
    user.useCalendar("Dst");
    user.editSeriesProperties("SeriesEvent", "2025-07-01T09:00", Properties.SUBJECT,
            "RenamedSeries");
    String res = user.showEvents("2025-07-01T00:00", "2025-07-31T23:59");
    assertEquals("• An event: RenamedSeries will be from 2025-07-01T09:00 to 2025-07-01T10:00.\n•" +
            " An event: RenamedSeries will be from 2025-07-02T09:00 to 2025-07-02T10:00.", res);
  }

  @Test
  public void testSwitchCalendarScope() {
    SingleUser user = new SingleUser();
    user.createCalendar("A", "America/New_York");
    user.createCalendar("B", "America/New_York");
    user.useCalendar("A");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    user.createEvent("X", "2025-08-01T10:00", optionals);
    user.useCalendar("B");
    assertEquals("", user.showEvents("2025-08-01"));
    user.useCalendar("A");
    String outA = user.showEvents("2025-08-01");
    assertEquals("• An event: X will be from 2025-08-01T10:00 to 2025-08-01T11:00.", outA);
  }

  @Test
  public void testEditCalendarValidTimezoneThroughUser() {
    SingleUser user = new SingleUser();
    user.createCalendar("C", "America/New_York");
    user.createCalendar("E", "America/Los_Angeles");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-01T11:00");
    user.useCalendar("C");
    user.createEvent("E", "2025-06-01T10:00", optionals);
    user.editCalendar("C", CalendarProperties.TIMEZONE, "America/Los_Angeles");
    user.useCalendar("E");
    user.createEvent("E", "2025-06-01T10:00", optionals);
    user.useCalendar("C");
    String result = user.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T10:00 to 2025-06-01T11:00.", result);
  }


  @Test(expected = IllegalStateException.class)
  public void testSwitchToNonexistentCalendar() {
    SingleUser user = new SingleUser();
    user.useCalendar("NoCal");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDuplicateCalendar() {
    SingleUser user = new SingleUser();
    user.createCalendar("Cal", "America/New_York");
    user.createCalendar("Cal", "America/New_York");
  }

  @Test(expected = IllegalStateException.class)
  public void testShowEventsWithoutCalendar() {
    SingleUser user = new SingleUser();
    user.showEvents("2025-06-01");
  }

  @Test(expected = IllegalStateException.class)
  public void testShowStatusWithoutCalendar() {
    SingleUser user = new SingleUser();
    user.showStatus("2025-06-01T10:00");
  }


  @Test
  public void testAllDayEventCreation() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    singleUser.createEvent("AD", "2025-06-10", opts);
    String result = singleUser.showEvents("2025-06-10");
    assertEquals("• An event: AD will be from 2025-06-10T08:00 to 2025-06-10T17:00.", result);
  }

  @Test
  public void testSeriesSameDayEvents() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    singleUser.createEventSeries("TS", "2025-06-01T09:00", opts, "MW", "2025-06-10");
    String out = singleUser.showEvents("2025-06-02T00:00", "2025-06-04T23:59");
    String expected =
            "• An event: TS will be from 2025-06-02T09:00 to 2025-06-02T10:00.\n" +
                    "• An event: TS will be from 2025-06-04T09:00 to 2025-06-04T10:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testSeriesRepeatsOnWeekdays() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    singleUser.createEventSeries("WD", "2025-06-01", opts, "TR", "2025-06-07");
    String out = singleUser.showEvents("2025-06-01T00:00", "2025-06-07T23:59");
    String expected =
            "• An event: WD will be from 2025-06-03T08:00 to 2025-06-03T17:00.\n" +
                    "• An event: WD will be from 2025-06-05T08:00 to 2025-06-05T17:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testEditSingleProperty() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    singleUser.createEvent("E", "2025-06-01T09:00", opts);
    singleUser.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00",
            Properties.LOCATION, "physical");
    String out = singleUser.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.", out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditAllBySubject() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    singleUser.createEvent("A", "2025-06-01", opts);
    singleUser.createEvent("A", "2025-06-02", opts);
    singleUser.editProperties("A", "2025-06-01T08:00", Properties.STATUS, "PRIVATE");

  }

  @Test
  public void testEditFromSpecificDateTime() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    singleUser.createEventSeries("S", "2025-06-01", opts, "MW", "2025-06-08");
    singleUser.editProperties("S", "2025-06-04T08:00", Properties.STATUS, "PRIVATE");
    String out = singleUser.showEvents("2025-06-02T00:00", "2025-06-04T23:59");
    String expected =
            "• An event: S will be from 2025-06-02T08:00 to 2025-06-02T17:00.\n" +
                    "• A private event: S will be from 2025-06-04T08:00 to 2025-06-04T17:00.";
    assertEquals(expected, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditInvalidPropertyValue() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    singleUser.createEvent("E", "2025-06-01T09:00", opts);
    singleUser.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00",
            Properties.STATUS, "UNKNOWN");
  }

  @Test
  public void testQueryEventsOnSpecificDay() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-02T10:00");
    singleUser.createEvent("A", "2025-06-01T09:00", opts);
    singleUser.createEvent("B", "2025-06-02T09:00", opts);
    String out = singleUser.showEvents("2025-06-01");
    assertEquals("• An event: A will be from 2025-06-01T09:00 to 2025-06-02T10:00.", out);
  }

  @Test
  public void testPrintDetailsForPublicEvent() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "desc");
    opts.put(Properties.STATUS, "PUBLIC");
    singleUser.createEvent("E", "2025-06-01T09:00", opts);
    String out = singleUser.showEvents("2025-06-01");
    assertEquals(
            "• A public event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.\n" +
                    "Description: desc.",
            out
    );
  }

  @Test
  public void testPrintFormatMultipleEvents() {

    SingleUser singleUser = new SingleUser();
    singleUser.createCalendar("EST", "America/New_York");
    singleUser.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T12:00");
    singleUser.createEvent("A", "2025-06-01T09:00", opts);
    singleUser.createEvent("B", "2025-06-01T11:00", opts);
    String out = singleUser.showEvents("2025-06-01");
    String expected =
            "• An event: A will be from 2025-06-01T09:00 to 2025-06-01T12:00.\n" +
                    "• An event: B will be from 2025-06-01T11:00 to 2025-06-01T12:00.";
    assertEquals(expected, out);
  }
}
