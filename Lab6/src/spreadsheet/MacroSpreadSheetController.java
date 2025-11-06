package spreadsheet;

import java.util.Scanner;

/**
 * Controller that supports macros, extending the basic SpreadSheetController.
 */
public class MacroSpreadSheetController extends SpreadSheetController {

  /**
   * Expose run() so tests can invoke it directly.
   */
  public void run() {
    super.control();
  }

  /**
   * Constructor matching the superclass.
   */
  public MacroSpreadSheetController(SpreadSheet sheet, Readable readable, Appendable appendable) {
    super(sheet, readable, appendable);
  }

  @Override
  protected void processCommand(String userInstruction, Scanner sc, SpreadSheet sheet) {
    switch (userInstruction) {
      case "bulk-assign-value": {
        int fromRow = getRowNum(sc.next());
        int fromCol = sc.nextInt() - 1;
        int toRow = getRowNum(sc.next());
        int toCol = sc.nextInt() - 1;
        double value = sc.nextDouble();
        if (!(sheet instanceof SpreadSheetWithMacro)) {
          throw new IllegalArgumentException("Sheet does not support macros");
        }
        BulkAssignMacro bam = new BulkAssignMacro(fromRow, fromCol, toRow, toCol, value);
        ((SpreadSheetWithMacro) sheet).execute(bam);
        break;
      }
      case "average": {
        int fromRow = getRowNum(sc.next());
        int fromCol = sc.nextInt() - 1;
        int toRow = getRowNum(sc.next());
        int toCol = sc.nextInt() - 1;
        int destRow = getRowNum(sc.next());
        int destCol = sc.nextInt() - 1;
        if (!(sheet instanceof SpreadSheetWithMacro)) {
          throw new IllegalArgumentException("Sheet does not support macros");
        }
        AverageMacro am = new AverageMacro(fromRow, fromCol, toRow, toCol, destRow, destCol);
        ((SpreadSheetWithMacro) sheet).execute(am);
        break;
      }
      case "range-assign": {
        int fromRow = getRowNum(sc.next());
        int fromCol = sc.nextInt() - 1;
        int toRow = getRowNum(sc.next());
        int toCol = sc.nextInt() - 1;
        double startVal = sc.nextDouble();
        double inc = sc.nextDouble();
        if (!(sheet instanceof SpreadSheetWithMacro)) {
          throw new IllegalArgumentException("Sheet does not support macros");
        }
        RangeAssignMacro ram = new RangeAssignMacro(fromRow, fromCol, toRow, toCol, startVal, inc);
        ((SpreadSheetWithMacro) sheet).execute(ram);
        break;
      }
      default: {
        super.processCommand(userInstruction, sc, sheet);
      }
    }
  }

  @Override
  protected void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: " + System.lineSeparator());
    writeMessage("assign-value row-num col-num value (set a cell to a value)"
            + System.lineSeparator());
    writeMessage("print-value row-num col-num (print the value at a given cell)"
            + System.lineSeparator());
    writeMessage("bulk-assign-value from-row-num from-col-num to-row-num to-col-num value (set a "
            + "range of cells to a value)" + System.lineSeparator());
    writeMessage("range-assign from-row-num from-col-num to-row-num to-col-num start-value " +
            "increment (set a row or column of cells to a range of values starting at the given " +
            "value and advancing by the increment)" + System.lineSeparator());
    writeMessage("average from-row-num from-col-num to-row-num to-col-num dest-row-num " +
            "dest-col-num (compute the average of a range of cells and put it at the given " +
            "location)" + System.lineSeparator());
    writeMessage("menu (Print supported instruction list)" + System.lineSeparator());
    writeMessage("q or quit (quit the program) " + System.lineSeparator());
  }
}
