package controller;
import model.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Calendar controller class.
 * Handles user input and feeds it to the model.
 */
public class CalendarController {
  private Readable readable;
  private Appendable appendable;
  private Calendar calendar;
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public CalendarController(Calendar calendar, Readable readable, Appendable appendable) {
    this.calendar = calendar;
    this.readable = readable;
    this.appendable = appendable;
  }

  /**
   * The main method that relinquishes control of the application to the controller.
   */
  public void control() {
    Scanner sc = new Scanner(readable);
    boolean quit = false;
    writeMessage("Running Calendar App ..." + System.lineSeparator());
    writeMessage("Type instruction: ");
    String line = "";
    while (!quit && sc.hasNextLine()) {
      line = sc.nextLine().trim();

      if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
        quit = true;
      } else if (!line.isEmpty()) {
        Scanner lineScanner = new Scanner(line);
        String userInstruction = lineScanner.next();
        processCommand(userInstruction, lineScanner, calendar);
      }

      if (!quit) {
        writeMessage("Type instruction: ");
      }
    }
    if (!line.equals("quit") && !line.equals("q")) {
      System.err.println(System.lineSeparator() + "There is no quit in the commands file");
    }
    System.exit(0);
  }

  /**
   * processes user command.
   * @param userInstruction user command as a string.
   * @param sc is a scanner used to detect use input and tokenize input.
   * @param calendar the calendar being operated on.
   */
  protected void processCommand(String userInstruction, Scanner sc, Calendar calendar) {
    //try {
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
        default: 
          writeMessage("Undefined instruction: " + userInstruction + System.lineSeparator());
      }
//    } catch (IllegalArgumentException e) {
//      //writeMessage("Error: " + e.getMessage() + System.lineSeparator());
//    }
  }

  private void createCommandHelper(Scanner sc) {
    if (!sc.next().equals("event")) {
      throw new IllegalArgumentException("Expected 'event' after 'create'");
    } //the word after "create" has to be "event"

    String subject = parseSubject(sc);
    String nextWord = sc.next();
    HashMap<Properties, String> optionals = new HashMap<>();

    if (nextWord.equals("from")) {
      // model.Event with specific time range
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
          sc.next(); // consume "times"
          calendar.createEventSeries(subject, startDateTime, optionals, weekdays, times);
          System.out.println("New event series created");
        } else if (forOrUntil.equals("until")) {
          String endDate = sc.next();
          calendar.createEventSeries(subject, startDateTime, optionals, weekdays, endDate);
          System.out.println("New event series created");
        }
      } else {
        // Single event
        calendar.createEvent(subject, startDateTime, optionals);
        System.out.println("New event created");
      }

    } else if (nextWord.equals("on")) {
      //all day event
      String date = sc.next();

      //check if it repeats
      if (sc.hasNext() && sc.next().equals("repeats")) {
        String weekdays = sc.next();
        String forOrUntil = sc.next();

        if (forOrUntil.equals("for")) {
          int times = sc.nextInt();
          calendar.createEventSeries(subject, date, optionals, weekdays, times);
          System.out.println("New all day event series created");
        } else if (forOrUntil.equals("until")) {
          String endDate = sc.next();
          calendar.createEventSeries(subject, date, optionals, weekdays, endDate);
          System.out.println("New all day event series created");
        }
      } else {
        // Single all day event
        calendar.createEvent(subject, date, optionals);
        System.out.println("All day event created");
      }
    }
  }

  private void editCommandHelper(Scanner sc) {
    String editType = sc.next(); //"event", "events", or "series"
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
      calendar.editProperty(subject, startDateTime, endDateTime, prop, newValue);
      System.out.println("edited event");

    } else {
      String withWord = sc.next();
      if (!withWord.equals("with")) {
        throw new IllegalArgumentException("Expected 'with' before new value");
      }
      String newValue = parseSubject(sc);

      Properties prop = Properties.valueOf(property.toUpperCase().replace(" ", "_"));

      if (editType.equals("events")) {
        calendar.editProperties(subject, startDateTime, prop, newValue);
        System.out.println("edited events");
      } else if (editType.equals("series")) {
        calendar.editSeriesProperties(subject, startDateTime, prop, newValue);
        System.out.println("edited series");
      }
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
      String result = calendar.showEvents(date);
      writeMessage(result + System.lineSeparator());
    } else if (onOrFrom.equals("from")) {
      String startDate = sc.next();
      String toWord = sc.next(); // "to"
      if (!toWord.equals("to")) {
        throw new IllegalArgumentException("Expected 'to' in date range");
      }
      String endDate = sc.next();
      String result = calendar.showEvents(startDate, endDate);
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
    String result = calendar.showStatus(dateTime);
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
    try {
      appendable.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}