package spreadsheet;

/**
 * Macro to assign a linear sequence of values to a single row or column range.
 */
public class RangeAssignMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double startValue;
  private final double increment;

  /**
   * Constructor.
   * @param fromRow starting row, zero-based
   * @param fromCol starting column, zero-based
   * @param toRow ending row, zero-based
   * @param toCol ending column, zero-based
   * @param startValue initial value for sequence
   * @param increment increment between values
   * @throws IllegalArgumentException if invalid parameters or not strictly row or column range
   */
  public RangeAssignMacro(int fromRow, int fromCol, int toRow, int toCol,
                          double startValue, double increment) {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
      throw new IllegalArgumentException("Indices must be non-negative");
    }
    boolean isRowRange = (fromRow == toRow && toCol >= fromCol);
    boolean isColRange = (fromCol == toCol && toRow >= fromRow);
    if (!isRowRange && !isColRange) {
      throw new IllegalArgumentException("Range must be a single row or single column");
    }
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.startValue = startValue;
    this.increment = increment;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }
    if (fromRow == toRow) {
      // Single row range
      double val = startValue;
      for (int c = fromCol; c <= toCol; c++) {
        sheet.set(fromRow, c, val);
        val += increment;
      }
    } else {
      // Single column range
      double val = startValue;
      for (int r = fromRow; r <= toRow; r++) {
        sheet.set(r, fromCol, val);
        val += increment;
      }
    }
  }
}
