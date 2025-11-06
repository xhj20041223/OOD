import controller.GUIController;
import controller.IGuiCalendarController;
import model.IEvent;
import model.Properties;
import model.SingleUser;
import model.CalendarProperties;
import view.GuiCalendarView;
import view.IGuiCalendarView;

import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.time.DateTimeException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * tests for User class.
 */
public class GUIControllerTest {
  SingleUser singleUser;
  IGuiCalendarView view;
  IGuiCalendarController gui;

  @Before
  public void setUp() {
    singleUser = new SingleUser();
    view = new GuiCalendarView();
    gui = new GUIController(singleUser, view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventEndBeforeStartThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-05-05T08:00");
    gui.createEvent("InvalidTime", "2025-05-05T10:00", optionals);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateDuplicateEventThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-10T12:00");
    gui.createEvent("DupEvent", "2025-06-10T10:00", optionals);
    gui.createEvent("DupEvent", "2025-06-10T10:00", optionals);
  }

  @Test
  public void testCreateFullDayEvent() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    gui.createEvent("Holiday", "2025-07-04", optionals);

    String result = gui.showEvents("2025-07-04");
    assertEquals("• An event: Holiday will be from 2025-07-04T08:00 to 2025-07-04T17:00.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidWeekdayThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    gui.createEventSeries("SeriesErr", "2025-08-01T10:00",
            optionals, "MXF", "2025-08-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditPropertyNoSuchEventThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-09-01T12:00");
    gui.createEvent("ExistEvent", "2025-09-01T10:00", optionals);
    gui.editProperty("NotExists", "2025-09-01T10:00",
            "2025-09-01T12:00", Properties.DESCRIPTION, "NewDesc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventMultipleMatchesThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-09-11T09:00");
    gui.createEvent("Multi", "2025-09-11T08:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-09-12T10:00");
    gui.createEvent("Multi", "2025-09-12T09:00", optionals2);

    gui.editProperty("Multi",
            "2025-09-10T08:00", "2025-09-11T09:00",
            Properties.DESCRIPTION, "Desc");
  }

  @Test
  public void testShowEventsDateRangeNoEventsReturnsEmpty() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-10-01T11:00");
    gui.createEvent("Single", "2025-10-01T10:00", optionals);

    String result = gui.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditEventDuplicateAfterEditThrows() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> optionals1 = new HashMap<>();
    optionals1.put(Properties.END_TIME, "2025-12-01T11:00");
    gui.createEvent("A", "2025-12-01T10:00", optionals1);

    HashMap<Properties, String> optionals2 = new HashMap<>();
    optionals2.put(Properties.END_TIME, "2025-12-01T11:00");
    gui.createEvent("B", "2025-12-01T10:00", optionals2);

    gui.editProperty("B", "2025-12-01T10:00", "2025-12-01T11:00",
            Properties.SUBJECT, "A");
  }


  @Test
  public void testSingleEventCreationAndShow() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-08-20T15:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "Desc");
    opts.put(Properties.STATUS, "private");
    IEvent single = gui.createEvent("Solo", "2025-08-20T14:00", opts);
    String result = gui.showEvents("2025-08-20");
    assertEquals("• A private event: Solo will be from 2025-08-20T14:00 to 2025-08-20T15:00 in " +
            "person.\nDescription: Desc.", result);
  }

  @Test(expected = DateTimeParseException.class)
  public void testShowEventsInvalidDateFormat() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    gui.showEvents("2025-13-01");
  }

  @Test
  public void testShowEventsRangeMultiDayOverlap() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");

    gui.createEvent("OverlapBefore", "2025-10-01T23:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T01:00");
      }
    });

    gui.createEvent("Inside", "2025-10-02T10:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-02T11:00");
      }
    });

    gui.createEvent("After", "2025-10-03T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-10-03T10:00");
      }
    });

    String result = gui.showEvents("2025-10-02T00:00", "2025-10-02T23:59");
    assertEquals("• An event: OverlapBefore will be from 2025-10-01T23:00 to 2025-10-02T01:00.\n•" +
            " An event: Inside will be from 2025-10-02T10:00 to 2025-10-02T11:00.", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEventSeriesInvalidPattern() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    gui.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "XYZ", 5);
  }

  @Test
  public void testCreateEventSeriesNonPositiveCount() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    gui.createEventSeries("Test", "2025-07-01T09:00", new HashMap<Properties, String>() {
      {
        put(Properties.END_TIME, "2025-07-01T10:00");
      }
    }, "M", 0);
    assertEquals("", gui.showEvents("2025-07-01T09:00", "2025-08-01T09:00"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUseNonexistentCalendarThrows() {

    gui.useCalendar("NoCal");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDuplicateCalendarThrows() {

    gui.createCalendar("C1", "America/New_York");
    gui.createCalendar("C1", "Europe/London");
  }

  @Test(expected = DateTimeException.class)
  public void testCreateCalendarInvalidTimezoneThrows() {

    gui.createCalendar("C1", "Invalid/Zone");
  }

  @Test(expected = IllegalStateException.class)
  public void testEditCalendarNameToDuplicateThrows() {
    gui.createCalendar("A", "America/New_York");
    gui.createCalendar("B", "America/New_York");
    singleUser.editCalendar("A", CalendarProperties.NAME, "B");
  }

  @Test(expected = DateTimeException.class)
  public void testEditCalendarTimezone() {
    gui.createCalendar("C", "America/New_York");
    singleUser.editCalendar("C", CalendarProperties.TIMEZONE, "Invalid/Zone");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateEventWithoutCurrentCalendarThrows() {
    gui.createEvent("Evt", "2025-06-01T10:00", new HashMap<>());
  }

  @Test(expected = IllegalStateException.class)
  public void testCopyEventToNonexistentTargetCalendarThrows() {
    gui.createCalendar("Src", "America/New_York");
    gui.useCalendar("Src");
    singleUser.copyEvent("X", "2025-06-01T10:00", "NoCal", "2025-07-01T10:00");
  }

  @Test
  public void testSwitchCalendarScope() {
    gui.createCalendar("A", "America/New_York");
    gui.createCalendar("B", "America/New_York");
    gui.useCalendar("A");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-08-01T11:00");
    gui.createEvent("X", "2025-08-01T10:00", optionals);
    gui.useCalendar("B");
    assertEquals("", gui.showEvents("2025-08-01"));
    gui.useCalendar("A");
    String outA = gui.showEvents("2025-08-01");
    assertEquals("• An event: X will be from 2025-08-01T10:00 to 2025-08-01T11:00.", outA);
  }

  @Test
  public void testEditCalendarValidTimezoneThroughUser() {
    gui.createCalendar("C", "America/New_York");
    gui.createCalendar("E", "America/Los_Angeles");
    HashMap<Properties, String> optionals = new HashMap<>();
    optionals.put(Properties.END_TIME, "2025-06-01T11:00");
    gui.useCalendar("C");
    gui.createEvent("E", "2025-06-01T10:00", optionals);
    singleUser.editCalendar("C", CalendarProperties.TIMEZONE, "America/Los_Angeles");
    gui.useCalendar("E");
    gui.createEvent("E", "2025-06-01T10:00", optionals);
    gui.useCalendar("C");
    String result = singleUser.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T10:00 to 2025-06-01T11:00.", result);
  }


  @Test(expected = IllegalStateException.class)
  public void testSwitchToNonexistentCalendar() {
    gui.useCalendar("NoCal");
  }

  @Test(expected = IllegalStateException.class)
  public void testCreateDuplicateCalendar() {
    gui.createCalendar("Cal", "America/New_York");
    gui.createCalendar("Cal", "America/New_York");
  }

  @Test(expected = IllegalStateException.class)
  public void testShowEventsWithoutCalendar() {
    gui.showEvents("2025-06-01");
  }

  @Test
  public void testAllDayEventCreation() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    gui.createEvent("AD", "2025-06-10", opts);
    String result = gui.showEvents("2025-06-10");
    assertEquals("• An event: AD will be from 2025-06-10T08:00 to 2025-06-10T17:00.", result);
  }

  @Test
  public void testSeriesSameDayEvents() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    gui.createEventSeries("TS", "2025-06-01T09:00", opts, "MW", "2025-06-10");
    String out = gui.showEvents("2025-06-02T00:00", "2025-06-04T23:59");
    String expected =
            "• An event: TS will be from 2025-06-02T09:00 to 2025-06-02T10:00.\n" +
                    "• An event: TS will be from 2025-06-04T09:00 to 2025-06-04T10:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testSeriesRepeatsOnWeekdays() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    gui.createEventSeries("WD", "2025-06-01", opts, "TR", "2025-06-07");
    String out = gui.showEvents("2025-06-01T00:00", "2025-06-07T23:59");
    String expected =
            "• An event: WD will be from 2025-06-03T08:00 to 2025-06-03T17:00.\n" +
                    "• An event: WD will be from 2025-06-05T08:00 to 2025-06-05T17:00.";
    assertEquals(expected, out);
  }

  @Test
  public void testEditSingleProperty() {


    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    gui.createEvent("E", "2025-06-01T09:00", opts);
    gui.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00",
            Properties.LOCATION, "physical");
    String out = gui.showEvents("2025-06-01");
    assertEquals("• An event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.", out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEditInvalidPropertyValue() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    gui.createEvent("E", "2025-06-01T09:00", opts);
    gui.editProperty("E", "2025-06-01T09:00", "2025-06-01T10:00",
            Properties.STATUS, "UNKNOWN");
  }

  @Test
  public void testQueryEventsOnSpecificDay() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-02T10:00");
    gui.createEvent("A", "2025-06-01T09:00", opts);
    gui.createEvent("B", "2025-06-02T09:00", opts);
    String out = gui.showEvents("2025-06-01");
    assertEquals("• An event: A will be from 2025-06-01T09:00 to 2025-06-02T10:00.", out);
  }

  @Test
  public void testPrintDetailsForPublicEvent() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T10:00");
    opts.put(Properties.LOCATION, "physical");
    opts.put(Properties.DESCRIPTION, "desc");
    opts.put(Properties.STATUS, "PUBLIC");
    gui.createEvent("E", "2025-06-01T09:00", opts);
    String out = gui.showEvents("2025-06-01");
    assertEquals(
            "• A public event: E will be from 2025-06-01T09:00 to 2025-06-01T10:00 in person.\n" +
                    "Description: desc.",
            out
    );
  }

  @Test
  public void testPrintFormatMultipleEvents() {
    gui.createCalendar("EST", "America/New_York");
    gui.useCalendar("EST");
    HashMap<Properties, String> opts = new HashMap<>();
    opts.put(Properties.END_TIME, "2025-06-01T12:00");
    gui.createEvent("A", "2025-06-01T09:00", opts);
    gui.createEvent("B", "2025-06-01T11:00", opts);
    String out = gui.showEvents("2025-06-01");
    String expected =
            "• An event: A will be from 2025-06-01T09:00 to 2025-06-01T12:00.\n" +
                    "• An event: B will be from 2025-06-01T11:00 to 2025-06-01T12:00.";
    assertEquals(expected, out);
  }
}
