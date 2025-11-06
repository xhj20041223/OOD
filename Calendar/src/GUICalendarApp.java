import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


import javax.swing.SwingUtilities;

import controller.CalendarController;
import controller.GUIController;
import controller.IGuiCalendarController;
import model.Calendar;
import model.IUser;
import model.SimpleCalendar;
import model.SingleUser;
import view.CalendarView;
import view.GuiCalendarView;
import view.ICalendarView;
import view.IGuiCalendarView;

/**
 * Represents a graphical user interface instance that allows a user to create and edit calendars.
 */
public class GUICalendarApp {

  /**
   * main entry point for the gui program.
   *
   * @param args the arguments passed into the program specified by the user.
   */
  public static void main(String[] args) {
    Calendar calendar = new SimpleCalendar();
    Readable input;
    Appendable output = System.out;

    ICalendarView view = new CalendarView(output);

    List<String> argList = Arrays.asList(args);

    if (argList.contains("--interactive")) {
      input = new InputStreamReader(System.in);
      CalendarController controller = new CalendarController(calendar, input, view);
      controller.control();
    } else if (argList.contains("--headless")) {
      int index = argList.indexOf("--headless");
      if (index + 1 < argList.size()) {
        String filePath = argList.get(index + 1);
        try {
          input = new FileReader(filePath);
          CalendarController controller = new CalendarController(calendar, input, view);
          controller.control();
        } catch (FileNotFoundException e) {
          System.err.println("File not found: " + filePath);
        }
      } else {
        System.err.println("Please specify file path after --headless");
      }
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          IGuiCalendarView view = new GuiCalendarView();
          IUser model = new SingleUser();
          IGuiCalendarController controller = new GUIController(model, view);
        }
      });
    }
  }
}