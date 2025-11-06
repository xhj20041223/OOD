import model.IEvent;
import model.Properties;
import model.SimpleCalendar;

import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.util.HashMap;

import javax.swing.*;

import static org.junit.Assert.*;

public class CalendarTest {

  @Test
  public void testEventSeriesCreationAndEdits() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();

    optionals.put(Properties.END_TIME, "2025-05-05T11:00");
    cal.createEventSeries("First", "2025-05-05T10:00", optionals, "MW", 6);

    String expected05 = "• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07 = "• An event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12 = "• An event: First will be from 2025-05-12T10:00 to 2025-05-12T11:00.";
    String expected14 = "• An event: First will be from 2025-05-14T10:00 to 2025-05-14T11:00.";
    String expected19 = "• An event: First will be from 2025-05-19T10:00 to 2025-05-19T11:00.";
    String expected21 = "• An event: First will be from 2025-05-21T10:00 to 2025-05-21T11:00.";

    assertEquals(expected05, cal.showEvents("2025-05-05"));
    assertEquals(expected07, cal.showEvents("2025-05-07"));
    assertEquals(expected12, cal.showEvents("2025-05-12"));
    assertEquals(expected14, cal.showEvents("2025-05-14"));
    assertEquals(expected19, cal.showEvents("2025-05-19"));
    assertEquals(expected21, cal.showEvents("2025-05-21"));

    cal.editProperties("First", "2025-05-12T10:00", Properties.SUBJECT, "Second");

    String expected05_a = "• An event: First will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07_a = "• An event: First will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12_a = "• An event: Second will be from 2025-05-12T10:00 to 2025-05-12T11:00.";
    String expected14_a = "• An event: Second will be from 2025-05-14T10:00 to 2025-05-14T11:00.";
    String expected19_a = "• An event: Second will be from 2025-05-19T10:00 to 2025-05-19T11:00.";
    String expected21_a = "• An event: Second will be from 2025-05-21T10:00 to 2025-05-21T11:00.";

    assertEquals(expected05_a, cal.showEvents("2025-05-05"));
    assertEquals(expected07_a, cal.showEvents("2025-05-07"));
    assertEquals(expected12_a, cal.showEvents("2025-05-12"));
    assertEquals(expected14_a, cal.showEvents("2025-05-14"));
    assertEquals(expected19_a, cal.showEvents("2025-05-19"));
    assertEquals(expected21_a, cal.showEvents("2025-05-21"));

    cal.editSeriesProperties("First", "2025-05-05T10:00", Properties.SUBJECT, "Third");

    String expected05_b = "• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07_b = "• An event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12_b = "• An event: Third will be from 2025-05-12T10:00 to 2025-05-12T11:00.";
    String expected14_b = "• An event: Third will be from 2025-05-14T10:00 to 2025-05-14T11:00.";
    String expected19_b = "• An event: Third will be from 2025-05-19T10:00 to 2025-05-19T11:00.";
    String expected21_b = "• An event: Third will be from 2025-05-21T10:00 to 2025-05-21T11:00.";

    assertEquals(expected05_b, cal.showEvents("2025-05-05"));
    assertEquals(expected07_b, cal.showEvents("2025-05-07"));
    assertEquals(expected12_b, cal.showEvents("2025-05-12"));
    assertEquals(expected14_b, cal.showEvents("2025-05-14"));
    assertEquals(expected19_b, cal.showEvents("2025-05-19"));
    assertEquals(expected21_b, cal.showEvents("2025-05-21"));

    cal.editProperties("Third", "2025-05-12T10:00", Properties.START_TIME, "2025-05-12T10:30");

    String expected05_c = "• An event: Third will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07_c = "• An event: Third will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12_c = "• An event: Third will be from 2025-05-12T10:30 to 2025-05-12T11:00.";
    String expected14_c = "• An event: Third will be from 2025-05-14T10:30 to 2025-05-14T11:00.";
    String expected19_c = "• An event: Third will be from 2025-05-19T10:30 to 2025-05-19T11:00.";
    String expected21_c = "• An event: Third will be from 2025-05-21T10:30 to 2025-05-21T11:00.";

    assertEquals(expected05_c, cal.showEvents("2025-05-05"));
    assertEquals(expected07_c, cal.showEvents("2025-05-07"));
    assertEquals(expected12_c, cal.showEvents("2025-05-12"));
    assertEquals(expected14_c, cal.showEvents("2025-05-14"));
    assertEquals(expected19_c, cal.showEvents("2025-05-19"));
    assertEquals(expected21_c, cal.showEvents("2025-05-21"));

    cal.editProperties("Third", "2025-05-05T10:00", Properties.SUBJECT, "Fourth");

    String expected05_d = "• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07_d = "• An event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12_d = "• An event: Third will be from 2025-05-12T10:30 to 2025-05-12T11:00.";
    String expected14_d = "• An event: Third will be from 2025-05-14T10:30 to 2025-05-14T11:00.";
    String expected19_d = "• An event: Third will be from 2025-05-19T10:30 to 2025-05-19T11:00.";
    String expected21_d = "• An event: Third will be from 2025-05-21T10:30 to 2025-05-21T11:00.";

    assertEquals(expected05_d, cal.showEvents("2025-05-05"));
    assertEquals(expected07_d, cal.showEvents("2025-05-07"));
    assertEquals(expected12_d, cal.showEvents("2025-05-12"));
    assertEquals(expected14_d, cal.showEvents("2025-05-14"));
    assertEquals(expected19_d, cal.showEvents("2025-05-19"));
    assertEquals(expected21_d, cal.showEvents("2025-05-21"));

    cal.editSeriesProperties("Third", "2025-05-12T10:30", Properties.SUBJECT, "Fifth");

    String expected05_e = "• An event: Fourth will be from 2025-05-05T10:00 to 2025-05-05T11:00.";
    String expected07_e = "• An event: Fourth will be from 2025-05-07T10:00 to 2025-05-07T11:00.";
    String expected12_e = "• An event: Fifth will be from 2025-05-12T10:30 to 2025-05-12T11:00.";
    String expected14_e = "• An event: Fifth will be from 2025-05-14T10:30 to 2025-05-14T11:00.";
    String expected19_e = "• An event: Fifth will be from 2025-05-19T10:30 to 2025-05-19T11:00.";
    String expected21_e = "• An event: Fifth will be from 2025-05-21T10:30 to 2025-05-21T11:00.";

    assertEquals(expected05_e, cal.showEvents("2025-05-05"));
    assertEquals(expected07_e, cal.showEvents("2025-05-07"));
    assertEquals(expected12_e, cal.showEvents("2025-05-12"));
    assertEquals(expected14_e, cal.showEvents("2025-05-14"));
    assertEquals(expected19_e, cal.showEvents("2025-05-19"));
    assertEquals(expected21_e, cal.showEvents("2025-05-21"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventEndBeforeStartThrows() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-05-05T08:00");
    cal.createEvent("InvalidTime", "2025-05-05T10:00", optionals);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-10T12:00");
    cal.createEvent("DupEvent", "2025-06-10T10:00", optionals);
    cal.createEvent("DupEvent", "2025-06-10T10:00", optionals);
  }

  @Test
  public void testCreateFullDayEvent() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    cal.createEvent("Holiday", "2025-07-04", optionals);

    String result = cal.showEvents("2025-07-04");
    assertEquals("• An event: Holiday will be from 2025-07-04T08:00 to 2025-07-04T17:00.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidWeekdayThrows() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    cal.createEventSeries("SeriesErr", "2025-08-01T10:00", optionals, "MXF", "2025-08-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNoSuchEventThrows() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-09-01T12:00");
    cal.createEvent("ExistEvent", "2025-09-01T10:00", optionals);
    cal.editProperty("NotExists", "2025-09-01T10:00", "2025-09-01T12:00", Properties.DESCRIPTION,
            "NewDesc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventMultipleMatchesThrows() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-09-11T09:00");
    cal.createEvent("Multi", "2025-09-11T08:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-09-12T10:00");
    cal.createEvent("Multi", "2025-09-12T09:00", optionals2);

    cal.editProperty("Multi", "2025-09-10T08:00", "2025-09-11T09:00", Properties.DESCRIPTION,
            "Desc");
  }

  @Test
  public void testShowEventsDateRangeNoEventsReturnsEmpty() {
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-10-01T11:00");
    cal.createEvent("Single", "2025-10-01T10:00", optionals);

    String result = cal.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("", result);
  }

  @Test
  public void testShowStatusFreeAndBusy() {
    SimpleCalendar cal = new SimpleCalendar();
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
    SimpleCalendar cal = new SimpleCalendar();
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
    SimpleCalendar cal = new SimpleCalendar();
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-08-20T15:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "Desc");
    opts.put(Properties.STATUS, "private");
    IEvent single = cal.createEvent("Solo", "2025-08-20T14:00", opts);
    String result = cal.showEvents("2025-08-20");
    assertEquals("• A private event: Solo will be from 2025-08-20T14:00 to 2025-08-20T15:00 in " +
            "person.\n\tDescription: Desc .", result);
  }

  @Test(expected = DateTimeParseException.class)
  public void testShowEventsInvalidDateFormat() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.showEvents("2025-13-01");
  }

  @Test
  public void testShowEventsRangeMultiDayOverlap() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.createEvent("OverlapBefore", "2025-10-01T23:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-10-02T01:00");
    }});
    cal.createEvent("Inside", "2025-10-02T10:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-10-02T11:00");
    }});
    cal.createEvent("After", "2025-10-03T09:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-10-03T10:00");
    }});
    String result = cal.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertTrue(result.contains("OverlapBefore"));
    assertTrue(result.contains("Inside"));
    assertFalse(result.contains("After"));
  }

  @Test
  public void testShowStatusAtBoundaries() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.createEvent("Boundary", "2025-11-05T10:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-11-05T11:00");
    }});
    String atStart = cal.showStatus("2025-11-05T10:00");
    String atEnd = cal.showStatus("2025-11-05T11:01");
    assertEquals("You have an event at that time", atStart);
    assertEquals("You are free at that time", atEnd);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNonExistentEvent() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.editProperties("NoEvent", "2025-01-01T00:00", Properties.SUBJECT, "NewSubject");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidPattern() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-07-01T10:00");
    }}, "XYZ", 5);
  }

  @Test
  public void testCreateEventSeriesNonPositiveCount() {
    SimpleCalendar cal = new SimpleCalendar();
    cal.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {{
      put(Properties.END_TIME, "2025-07-01T10:00");
    }}, "M", 0);
    assertEquals("", cal.showEvents("2025-07-01T09:00", "2025-08-01T09:00"));
  }
}
