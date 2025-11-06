package spreadsheet;

/**
 * Extension of SpreadSheet to accept macros.
 */
public interface SpreadSheetWithMacro extends SpreadSheet {
  /**
   * Execute a given macro on this spreadsheet.
   * @param m the macro to execute
   */
  void execute(SpreadSheetMacro m);
}