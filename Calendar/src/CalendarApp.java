import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import model.*;
import controller.*;

import model.SimpleCalendar;

import java.io.*;
import java.util.Scanner;
import model.*;

public class CalendarApp {
  public static void main(String[] args) {
    Calendar calendar = new SimpleCalendar();
    Readable input;
    Appendable output = System.out;

    Scanner promptScanner = new Scanner(System.in);
    System.out.println("Set mode: --interactive or --headless <file>.txt");

    while(true) {
      String mode = promptScanner.next().trim();
      if (mode.equals("--headless")) {
        String filePath = promptScanner.next().trim();
        try {
          input = new FileReader(filePath);
          CalendarController controller = new CalendarController(calendar, input, output);
          controller.control();
        } catch (FileNotFoundException e) {
          System.err.println("File not found: " + filePath);
          return;
        }
      }
      else if (mode.equals("q") || mode.equals("quit")) {
        break;
      }
      else if (mode.equals("--interactive")) {
        input = new InputStreamReader(System.in);
        CalendarController controller = new CalendarController(calendar, input, output);
        controller.control();
        break;
      }
      else {
        System.out.println("Unknown mode: " + mode);
        System.out.println("Set mode: --interactive or --headless <file>.txt");
      }
    }

  }
}




