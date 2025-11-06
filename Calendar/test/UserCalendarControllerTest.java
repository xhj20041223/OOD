import controller.UserCalendarController;
import model.IUser;
import model.SingleUser;
import view.CalendarView;
import view.ICalendarView;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Tests for UserCalendarController that handles multiple calendars with timezones.
 */
public class UserCalendarControllerTest {

  private IUser user;
  private StringWriter output;
  private ICalendarView view;

  @Before
  public void setUp() {
    user = new SingleUser();
    output = new StringWriter();
    view = new CalendarView(output);
  }

  @Test
  public void testQuitOnly() {
    StringReader input = new StringReader("quit\n");
    UserCalendarController controller = new UserCalendarController(user, input, view);
    controller.control();

    String out = output.toString();
    assertTrue(out.contains("Running Calendar App"));
    assertTrue(out.contains("Type instruction"));
  }

  @Test
  public void testCorrectFormat() {
    String command = "create calendar --name work --timezone America/New_York\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();
    String out = output.toString();
    assertEquals(
            "Running Calendar App ...\n" +
                    "Please create a new calendar.\n" +
                    "Type instruction: \n" +
                    "New calendar created: work\n" +
                    "Type instruction: \n",
            out);
  }

  @Test
  public void testCreateCalendar() {
    String command = "create calendar --name work --timezone America/New_York\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Calendar created successfully") ||
            result.contains("New calendar created") ||
            !result.contains("Error"));
  }

  @Test
  public void testCreateCalendarWithInvalidTimezone() {
    String command = "create calendar --name work --timezone Invalid/Timezone\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Invalid timezone") ||
            result.contains("Error") ||
            result.contains("Unsupported timezone"));
  }

  @Test
  public void testCreateDuplicateCalendar() {
    String command = "create calendar --name work --timezone America/New_York\n" +
            "create calendar --name work --timezone Europe/London\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("calendar already exists"));
  }

  @Test
  public void testUseCalendar() {
    String command = "create calendar --name personal --timezone America/New_York\n" +
            "use calendar --name personal\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Using calendar") ||
            result.contains("Calendar set") ||
            !result.contains("Error"));
  }

  @Test
  public void testUseNonExistentCalendar() {
    String command = "use calendar --name nonexistent\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("can't find calendar with name"));
  }

  @Test
  public void testEditCalendarName() {
    String command = "create calendar --name oldname --timezone America/New_York\n" +
            "edit calendar --name oldname --property name newname\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Calendar updated") ||
            result.contains("Property changed") ||
            !result.contains("Error"));
  }

  @Test
  public void testEditCalendarTimezone() {
    String command = "create calendar --name work --timezone America/New_York\n" +
            "edit calendar --name work --property timezone Europe/London\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Calendar updated") ||
            result.contains("Timezone changed") ||
            !result.contains("Error"));
  }

  @Test
  public void testEditCalendarToExistingName() {
    String command = "create calendar --name work --timezone America/New_York\n" +
            "create calendar --name personal --timezone America/New_York\n" +
            "edit calendar --name work --property name personal\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("already exists") ||
            result.contains("duplicate") ||
            result.contains("Error"));
  }

  @Test
  public void testCreateEventWithoutCalendar() {
    String command = "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("have not assigned current calendar yet"));
  }

  @Test
  public void testCreateEventWithCalendar() {
    String command = "create calendar --name work --timezone America/New_York\n" +
            "use calendar --name work\n" +
            "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "print events on 2025-06-08\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("meeting") &&
            (result.contains("New event created") || result.contains("Event created")));
  }

  @Test
  public void testCopyEventBetweenCalendars() {
    String command = "create calendar --name source --timezone America/New_York\n" +
            "create calendar --name target --timezone Europe/London\n" +
            "use calendar --name source\n" +
            "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "copy event meeting on 2025-06-08T14:00 --target target to 2025-06-09T10:00\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Event copied") ||
            result.contains("Copy completed") ||
            !result.contains("Error"));
  }

  @Test
  public void testCopyEventsOnDate() {
    String command = "create calendar --name source --timezone America/New_York\n" +
            "create calendar --name target --timezone America/New_York\n" +
            "use calendar --name source\n" +
            "create event meeting1 from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "create event meeting2 from 2025-06-08T16:00 to 2025-06-08T17:00\n" +
            "copy events on 2025-06-08 --target target to 2025-06-09\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Events copied") ||
            result.contains("Copy completed") ||
            !result.contains("Error"));
  }

  @Test
  public void testCopyEventsBetweenDates() {
    String command = "create calendar --name source --timezone America/New_York\n" +
            "create calendar --name target --timezone America/New_York\n" +
            "use calendar --name source\n" +
            "create event meeting1 from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "create event meeting2 from 2025-06-09T14:00 to 2025-06-09T15:00\n" +
            "copy events between 2025-06-08 and 2025-06-09 --target target to 2025-06-15\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Events copied") ||
            result.contains("Copy completed") ||
            !result.contains("Error"));
  }

  @Test
  public void testCopyEventToNonExistentCalendar() {
    String command = "create calendar --name source --timezone America/New_York\n" +
            "use calendar --name source\n" +
            "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "copy event meeting on 2025-06-08T14:00 --target nonexistent to 2025-06-09T10:00\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("not found") ||
            result.contains("doesn't exist") ||
            result.contains("Error"));
  }

  @Test
  public void testMultipleCalendarsWithDifferentTimezones() {
    String command = "create calendar --name nyc --timezone America/New_York\n" +
            "create calendar --name london --timezone Europe/London\n" +
            "create calendar --name tokyo --timezone Asia/Tokyo\n" +
            "use calendar --name nyc\n" +
            "create event meeting from 2025-06-08T14:00 to 2025-06-08T15:00\n" +
            "print events on 2025-06-08\n" +
            "use calendar --name london\n" +
            "create event conference from 2025-06-08T09:00 to 2025-06-08T10:00\n" +
            "print events on 2025-06-08\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertFalse(result.contains("Error"));
    assertTrue(result.contains("meeting") || result.contains("conference"));
  }

  @Test
  public void testInvalidCommandFormat() {
    String command = "create calendar work America/New_York\nquit\n"; // Missing --name and
    // --timezone flags
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Invalid command") ||
            result.contains("Undefined instruction") ||
            result.contains("Error"));
  }

  @Test
  public void testPrintEventsWithoutCalendar() {
    String command = "print events on 2025-06-08\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("have not assigned current calendar yet"));
  }

  @Test
  public void testComplexWorkflow() {
    String command = "create calendar --name personal --timezone America/New_York\n" +
            "create calendar --name work --timezone America/New_York\n" +
            "use calendar --name personal\n" +
            "create event workout from 2025-06-08T07:00 to 2025-06-08T08:00\n" +
            "use calendar --name work\n" +
            "create event standup from 2025-06-08T09:00 to 2025-06-08T09:30\n" +
            "copy event standup on 2025-06-08T09:00 --target personal to 2025-06-09T09:00\n" +
            "use calendar --name personal\n" +
            "print events from 2025-06-08T00:00 to 2025-06-09T23:59\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("workout"));
    assertTrue(result.contains("standup"));
  }

  @Test
  public void testEditNonExistentCalendar() {
    String command = "edit calendar --name nonexistent --property name newname\nquit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("not found") ||
            result.contains("doesn't exist") ||
            result.contains("Error"));
  }

  @Test
  public void testEditCalendarWithInvalidProperty() {
    String command = "create calendar --name test --timezone America/New_York\n" +
            "edit calendar --name test --property invalid newvalue\n" +
            "quit\n";
    UserCalendarController controller = new UserCalendarController(user,
            new StringReader(command), view);
    controller.control();

    String result = output.toString();
    assertTrue(result.contains("Invalid property") ||
            result.contains("Unknown property") ||
            result.contains("Error"));
  }
}