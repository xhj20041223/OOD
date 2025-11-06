import controller.CalendarController;
import model.Calendar;
import model.SimpleCalendar;
import view.CalendarView;
import view.ICalendarView;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Tests for CalendarController.
 */
public class CalendarControllerTest {

  private Calendar calendar;
  private StringWriter output;
  private ICalendarView view;

  @Before
  public void setUp() {
    calendar = new SimpleCalendar();
    output = new StringWriter();
    view = new CalendarView(output);
  }

  @Test
  public void testQuitOnly() {
    StringReader input = new StringReader("quit\n");
    CalendarController controller = new CalendarController(calendar, input, view);
    controller.control();

    String out = output.toString();
    assertTrue(out.contains("Running Calendar App ..."));
    assertTrue(out.contains("Type instruction:"));
  }

  @Test
  public void testCreateEventFrom() {
    String command = "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "print events on 2025-06-08\nquit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String result = output.toString();
    System.out.println("Output: \n " + output);
    assertTrue(result.contains("New event created"));
    assertTrue(result.contains("meeting"));
    assertTrue(result.contains("2025-06-08T14:00"));
    assertTrue(result.contains("2025-06-08T15:00"));
  }

  @Test
  public void testCreateAllDayEvent() {
    String command = "create event OOD on 2025-06-10\n" +
            "print events on 2025-06-10\nquit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String result = output.toString();
    System.out.println("Output: \n " + output);
    assertTrue(result.contains("All day event created"));
    assertTrue(result.contains("OOD"));
  }

  @Test
  public void testInvalidCommand() {
    String command = "not a command\nquit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Undefined instruction: not"));
  }

  @Test
  public void testPrintEventInRange() {
    String command =
            "create event \"Working with Thomas\" from 2025-06-09T10:00 to 2025-06-09T11:00\n" +
                    "print events from 2025-06-01T00:00 to 2025-06-30T23:00\nquit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String result = output.toString();
    System.out.println("Output: \n " + output);
    assertTrue(result.contains("New event created"));
    assertTrue(result.contains("Working with Thomas"));
  }

  @Test
  public void testPrintMultipleEventsInRange() {
    String command =
            "create event \"Working with Thomas\" from 2025-06-09T10:00 to 2025-06-09T11:00\n" +
                    "create event \"Working by myself :(\" on 2025-06-06\n" +
                    "print events from 2025-06-01T00:00 to 2025-06-30T23:00\nquit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String result = output.toString();
    System.out.println("Output: \n " + output);
    assertTrue(result.contains("New event created"));
    assertTrue(result.contains("All day event created"));
    assertTrue(result.contains("Working with Thomas"));
    assertTrue(result.contains("Working by myself"));
  }

  @Test
  public void testCreateMultiDayEvent() {
    String command =
            "create event trip from 2025-06-01T08:00 to 2025-06-03T20:00\n" +
                    "print events from 2025-06-01T00:00 to 2025-06-04T00:00\n" +
                    "quit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String out = output.toString();
    assertTrue(out.contains("New event created"));
    assertTrue(out.contains("trip"));
  }

  @Test
  public void testCreateInvalidEvent() {
    String command = "create event error from 2025-06-05T10:00 to 2025-06-04T08:00";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();

    String out = output.toString();
    assertTrue(out.contains("End time should be after the start time"));
  }

  @Test
  public void testSeriesUntil() {
    String command =
            "create event trip on 2025-06-09 repeats MTW until 2025-06-17\n" +
                    "print events on 2025-06-18\n" + //this should not return an event
                    "quit\n";
    CalendarController controller =
            new CalendarController(calendar, new StringReader(command), view);
    controller.control();
    String out = output.toString();
    assertFalse(out.contains("trip"));
  }

}
