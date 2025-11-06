import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;


import controller.UserCalendarController;
import model.IUser;
import model.SingleUser;
import view.CalendarView;
import view.ICalendarView;


import java.util.Arrays;
import java.util.List;


/**
 * CalendarApp class that calls the controller and starts the program.
 */
public class UserCalendarApp {
  /**
   * main entry point for the program.
   *
   * @param args the arguments passed into the program specified by the user.
   */
  public static void main(String[] args) {
    IUser calendar = new SingleUser();
    Readable input;
    Appendable output = System.out;

    ICalendarView view = new CalendarView(output);

    List<String> argList = Arrays.asList(args);

    if (argList.contains("--interactive")) {
      input = new InputStreamReader(System.in);
      UserCalendarController controller = new UserCalendarController(calendar, input, view);
      controller.control();
    } else if (argList.contains("--headless")) {
      int index = argList.indexOf("--headless");
      if (index + 1 < argList.size()) {
        String filePath = argList.get(index + 1);
        try {
          input = new FileReader(filePath);
          UserCalendarController controller = new UserCalendarController(calendar, input, view);
          controller.control();
        } catch (FileNotFoundException e) {
          System.err.println("File not found: " + filePath);
        }
      } else {
        System.err.println("Please specify file path after --headless");
      }
    } else {
      System.err.println("Use --interactive or --headless <file path>");
    }
  }
}




