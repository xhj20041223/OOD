package spreadsheet;

/**
 * Command interface representing a macro operation on a spreadsheet.
 */
public interface SpreadSheetMacro {
  /**
   * Execute this macro on the given spreadsheet.
   * @param sheet the spreadsheet to operate on
   */
  void execute(SpreadSheet sheet);
}