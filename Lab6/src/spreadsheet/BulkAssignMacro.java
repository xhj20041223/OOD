package spreadsheet;

/**
 * Macro to assign a specific value to every cell in a rectangular range.
 */
public class BulkAssignMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double value;

  /**
   * Constructor.
   * @param fromRow starting row (inclusive), zero-based
   * @param fromCol starting column (inclusive), zero-based
   * @param toRow ending row (inclusive), zero-based
   * @param toCol ending column (inclusive), zero-based
   * @param value value to assign
   * @throws IllegalArgumentException if indices are invalid
   */
  public BulkAssignMacro(int fromRow, int fromCol, int toRow, int toCol, double value) {
    if (fromRow < 0 || fromCol < 0 || toRow < fromRow || toCol < fromCol) {
      throw new IllegalArgumentException("Invalid range for bulk assign");
    }
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.value = value;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }
    for (int r = fromRow; r <= toRow; r++) {
      for (int c = fromCol; c <= toCol; c++) {
        sheet.set(r, c, value);
      }
    }
  }
}
