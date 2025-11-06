package controller;

import model.CalendarProperties;
import model.IUser;
import model.Properties;
import view.ICalendarView;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Calendar controller class.
 * Handles user input and feeds it to the model.
 */
public class UserCalendarController implements ICalendarController {
  private Readable readable;
  private ICalendarView view;
  private IUser user;
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * constructs a new calendar controller.
   *
   * @param user     the user to run the program on.
   * @param readable the user input.
   * @param view     the view to pass the output to.
   */
  public UserCalendarController(IUser user, Readable readable, ICalendarView view) {
    this.user = user;
    this.readable = readable;
    this.view = view;
  }

  /**
   * The main method that relinquishes control of the application to the controller.
   */
  public void control() {
    Scanner sc = new Scanner(readable);
    boolean quit = false;
    writeMessage("Running Calendar App ...");
    writeMessage("Please create a new calendar.");
    writeMessage("Type instruction: ");
    String line = "";

    while (!quit && sc.hasNextLine()) {
      line = sc.nextLine().trim();
      if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
        quit = true;
      } else if (!line.isEmpty()) {
        Scanner lineScanner = new Scanner(line);
        String userInstruction = lineScanner.next();
        processCommand(userInstruction, lineScanner, user);
      }

      if (!quit) {
        writeMessage("Type instruction: ");
      }
    }

    if (!quit) {
      writeMessage("No 'quit' command found. Exiting Program...");
    }
  }


  /**
   * processes user command.
   *
   * @param userInstruction user command as a string.
   * @param sc              is a scanner used to detect use input and tokenize input.
   * @param user            the user being operated on.
   */
  protected void processCommand(String userInstruction, Scanner sc, IUser user) {
    try {
      switch (userInstruction) {
        case "create":
          createCommandHelper(sc);
          break;
        case "edit":
          editCommandHelper(sc);
          break;
        case "print":
          printCommandHelper(sc);
          break;
        case "show":
          showCommandHelper(sc);
          break;
        case "use":
          useCommandHelper(sc);
          break;
        case "copy":
          copyCommandHelper(sc);
          break;
        default:
          writeMessage("Undefined instruction: " + userInstruction + System.lineSeparator());
      }
    } catch (IllegalArgumentException e) {
      writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  private void useCommandHelper(Scanner sc) {
    String nextWord = sc.next();

    if (!nextWord.equals("calendar")) {
      throw new IllegalArgumentException("Command following 'use' must be 'calendar'");
    }
    nextWord = sc.next();
    if (!nextWord.equals("--name")) {
      throw new IllegalArgumentException("Command following 'calendar' must be '--name'");
    }

    String subject = parseSubject(sc);

    try {
      user.useCalendar(subject);
      writeMessage("Using Calendar: " + subject);
    } catch (IllegalStateException e) {
      writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  private void copyCommandHelper(Scanner sc) {
    String nextWord = sc.next();

    if (nextWord.equals("event")) {
      String eventName = parseSubject(sc);
      String startTime;
      String calendarName;
      String targetStartTime;

      if (!sc.next().equals("on")) {
        throw new IllegalArgumentException("Command following event name must be 'on'");
      }
      startTime = sc.next();

      if (!sc.next().equals("--target")) {
        throw new IllegalArgumentException("Command following date time must be '--target'");
      }
      calendarName = parseSubject(sc);

      if (!sc.next().equals("to")) {
        throw new IllegalArgumentException("Command following target calendar name must be 'to'");
      }
      targetStartTime = sc.next();

      try {
        user.copyEvent(eventName, startTime, calendarName, targetStartTime);
        writeMessage("Event copied successfully");
      } catch (IllegalStateException e) {
        throw new IllegalArgumentException("Error: " + e.getMessage() + System.lineSeparator());
      }
    } else if (nextWord.equals("events")) {
      String followingWord = sc.next();

      if (followingWord.equals("on")) {
        String startDate = sc.next();
        String calendarName;
        String targetStartDate;

        if (!sc.next().equals("--target")) {
          throw new IllegalArgumentException("Command following date must be '--target'");
        }
        calendarName = parseSubject(sc);

        if (!sc.next().equals("to")) {
          throw new IllegalArgumentException("Command following target calendar name must be 'to'");
        }
        targetStartDate = sc.next();

        try {
          user.copyEvents(startDate, calendarName, targetStartDate);
          writeMessage("Events copied successfully");
        } catch (IllegalStateException e) {
          throw new IllegalArgumentException(e.getMessage());
        }

      } else if (followingWord.equals("between")) {
        String startDate = sc.next();
        String endDate;
        String calendarName;
        String targetDate;

        if (!sc.next().equals("and")) {
          throw new IllegalArgumentException("Command following start date must be 'and'");
        }
        endDate = sc.next();

        if (!sc.next().equals("--target")) {
          throw new IllegalArgumentException("Command following end date must be '--target'");
        }
        calendarName = parseSubject(sc);

        if (!sc.next().equals("to")) {
          throw new IllegalArgumentException("Command following target calendar name must be 'to'");
        }
        targetDate = sc.next();

        try {
          user.copyEvents(startDate, endDate, calendarName, targetDate);
          writeMessage("Events copied successfully");
        } catch (IllegalStateException e) {
          throw new IllegalArgumentException(e.getMessage());
        }

      } else {
        throw new IllegalArgumentException("Command following 'events' must be 'on' or 'between'");
      }
    } else {
      throw new IllegalArgumentException("Command following 'copy' must be 'event' or 'events'");
    }
  }

  private void createEventHelper(Scanner sc) {
    String subject = parseSubject(sc);
    String nextWord = sc.next();
    HashMap<Properties, String> optionals = new HashMap<>();

    if (nextWord.equals("from")) {
      String startDateTime = sc.next();
      String toWord = sc.next();
      if (!toWord.equals("to")) {
        throw new IllegalArgumentException("Expected 'to' after start time");
      }
      String endDateTime = sc.next();
      optionals.put(Properties.END_TIME, endDateTime);

      //check if it repeats
      if (sc.hasNext() && sc.next().equals("repeats")) {
        String weekdays = sc.next();
        String forOrUntil = sc.next();

        if (forOrUntil.equals("for")) {
          int times = sc.nextInt();
          sc.next();
          user.createEventSeries(subject, startDateTime, optionals, weekdays, times);
          writeMessage("New event series created");
        } else if (forOrUntil.equals("until")) {
          String endDate = sc.next();
          user.createEventSeries(subject, startDateTime, optionals, weekdays, endDate);
          writeMessage("New event series created");
        }
      } else {
        //single event
        user.createEvent(subject, startDateTime, optionals);
        writeMessage("New event created");
      }

    } else if (nextWord.equals("on")) {
      //all day event
      String date = sc.next();

      if (sc.hasNext() && sc.next().equals("repeats")) {
        String weekdays = sc.next();
        String forOrUntil = sc.next();

        if (forOrUntil.equals("for")) {
          int times = sc.nextInt();
          user.createEventSeries(subject, date, optionals, weekdays, times);
          writeMessage("New all day event series created");
        } else if (forOrUntil.equals("until")) {
          String endDate = sc.next();
          user.createEventSeries(subject, date, optionals, weekdays, endDate);
          writeMessage("New all day event series created");
        }
      } else {
        //single all day event
        user.createEvent(subject, date, optionals);
        writeMessage("All day event created");
      }
    }
  }

  private void createCalendarHelper(Scanner sc) {
    String nextWord = sc.next();
    if (!nextWord.equals("--name")) {
      throw new IllegalArgumentException("Command following calendar must be '--name'");
    }
    String subject = parseSubject(sc);

    nextWord = sc.next();
    if (!nextWord.equals("--timezone")) {
      throw new IllegalArgumentException("Command following name must be '--timezone'");
    }

    String zoneid = sc.next();

    try {
      user.createCalendar(subject, zoneid);
      writeMessage("New calendar created: " + subject);
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid time time zone: " + zoneid);
    } catch (IllegalStateException e) {
      writeMessage(e.getMessage());
    }
  }

  private void createCommandHelper(Scanner sc) {
    String createType = sc.next();
    if (createType.equals("event")) {
      try {
        createEventHelper(sc);
      } catch (IllegalStateException e) {
        writeMessage("Error: " + e.getMessage() + System.lineSeparator());
      }
    } else if (createType.equals("calendar")) {
      createCalendarHelper(sc);
    } else {
      throw new IllegalArgumentException("Undefined instruction: create " + createType);
    }
  }

  private void editCommandHelper(Scanner sc) {
    String editType = sc.next();

    if (editType.equals("calendar")) {
      editCalendarHelper(sc);
      return;
    }

    String property = sc.next();
    String subject = parseSubject(sc);
    String fromWord = sc.next();

    if (!fromWord.equals("from")) {
      throw new IllegalArgumentException("Expected 'from' in edit command");
    }

    String startDateTime = sc.next();

    if (editType.equals("event")) {
      String toWord = sc.next();
      if (!toWord.equals("to")) {
        throw new IllegalArgumentException("Expected 'to' after start time in event edit");
      }
      String endDateTime = sc.next();
      String withWord = sc.next();
      if (!withWord.equals("with")) {
        throw new IllegalArgumentException("Expected 'with' before new value");
      }
      String newValue = parseSubject(sc); //multiword

      Properties prop = Properties.valueOf(property.toUpperCase().replace(" ", "_"));
      user.editProperty(subject, startDateTime, endDateTime, prop, newValue);
      writeMessage("edited event");

    } else {
      String withWord = sc.next();
      if (!withWord.equals("with")) {
        throw new IllegalArgumentException("Expected 'with' before new value");
      }
      String newValue = parseSubject(sc);

      Properties prop = Properties.valueOf(property.toUpperCase().replace(" ", "_"));

      if (editType.equals("events")) {
        user.editProperties(subject, startDateTime, prop, newValue);
        writeMessage("edited events");
      } else if (editType.equals("series")) {
        user.editSeriesProperties(subject, startDateTime, prop, newValue);
        writeMessage("edited series");
      }
    }
  }

  private void editCalendarHelper(Scanner sc) {
    String nameFlag = sc.next();
    if (!nameFlag.equals("--name")) {
      throw new IllegalArgumentException("Expected '--name' flag");
    }
    String calendarName = parseSubject(sc);

    String propertyFlag = sc.next();
    if (!propertyFlag.equals("--property")) {
      throw new IllegalArgumentException("Expected '--property' flag");
    }
    String propertyName = sc.next();
    String newValue = parseSubject(sc);

    try {
      if (propertyName.equals("name")) {
        user.editCalendar(calendarName, CalendarProperties.NAME, newValue);
        writeMessage("Calendar name updated successfully");
      } else if (propertyName.equals("timezone")) {
        user.editCalendar(calendarName, CalendarProperties.TIMEZONE, newValue);
        writeMessage("Calendar timezone updated successfully");
      } else {
        throw new IllegalArgumentException("Invalid property:  " + propertyName + ". Use 'name' " +
                "or 'timezone'");
      }
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException(e.getMessage());
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid timezone: " + newValue);
    }
  }

  private void printCommandHelper(Scanner sc) {
    String eventsWord = sc.next();
    if (!eventsWord.equals("events")) {
      throw new IllegalArgumentException("Expected 'events' after 'print'");
    }

    String onOrFrom = sc.next();

    if (onOrFrom.equals("on")) {
      String date = sc.next();
      try {
        String result = user.showEvents(date);
        writeMessage(result + System.lineSeparator());
      } catch (IllegalStateException e) {
        writeMessage("Error: " + e.getMessage() + System.lineSeparator());
      }
    } else if (onOrFrom.equals("from")) {
      String startDate = sc.next();
      String toWord = sc.next();
      if (!toWord.equals("to")) {
        throw new IllegalArgumentException("Expected 'to' in date range");
      }
      String endDate = sc.next();
      String result = user.showEvents(startDate, endDate);
      writeMessage(result + System.lineSeparator());
    }
  }

  private void showCommandHelper(Scanner sc) {
    String statusWord = sc.next();
    if (!statusWord.equals("status")) {
      throw new IllegalArgumentException("Expected 'status' after 'show'");
    }

    String onWord = sc.next();
    if (!onWord.equals("on")) {
      throw new IllegalArgumentException("Expected 'on' after 'status'");
    }

    String dateTime = sc.next();
    String result = user.showStatus(dateTime);
    writeMessage(result + System.lineSeparator());
  }

  //parses the next word/words presumably the subject
  private String parseSubject(Scanner sc) {
    String subject = sc.next();

    //check if multi word subject
    if (subject.startsWith("\"")) {
      StringBuilder sb = new StringBuilder();
      sb.append(subject.substring(1)); //remove opening quote

      while (sc.hasNext()) {
        String word = sc.next();
        if (word.endsWith("\"")) {
          //add a space then the word minus the double quote
          sb.append(" ").append(word.substring(0, word.length() - 1));
          break;
        } else {
          sb.append(" ").append(word); //add a space and then the word
        }
      }
      return sb.toString();
    }

    return subject;
  }

  private void writeMessage(String message) throws IllegalStateException {
    view.showMessage(message);
  }
}