package spreadsheet;

/**
 * Macro to compute the average of a rectangular range and store it in a destination cell.
 */
public class AverageMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final int destRow;
  private final int destCol;

  /**
   * Constructor.
   * @param fromRow starting row, zero-based
   * @param fromCol starting column, zero-based
   * @param toRow ending row, zero-based
   * @param toCol ending column, zero-based
   * @param destRow destination row for average, zero-based
   * @param destCol destination column for average, zero-based
   * @throws IllegalArgumentException if invalid indices
   */
  public AverageMacro(int fromRow, int fromCol, int toRow, int toCol,
                      int destRow, int destCol) {
    if (fromRow < 0 || fromCol < 0 || toRow < fromRow || toCol < fromCol ||
            destRow < 0 || destCol < 0) {
      throw new IllegalArgumentException("Invalid parameters for average macro");
    }
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.destRow = destRow;
    this.destCol = destCol;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }
    double sum = 0.0;
    int count = 0;
    for (int r = fromRow; r <= toRow; r++) {
      for (int c = fromCol; c <= toCol; c++) {
        sum += sheet.get(r, c);
        count++;
      }
    }
    double avg = (count == 0) ? 0.0 : (sum / count);
    sheet.set(destRow, destCol, avg);
  }
}
