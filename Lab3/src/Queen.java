/**
 * Represents a queen which moves any number of squares along rank, file, or diagonal.
 */
public class Queen extends AbstractChessPiece {
  /**
   * Constructs a queen at the given position and color.
   *
   * @param row   the row position (0-7)
   * @param col   the column position (0-7)
   * @param color the piece color
   */
  public Queen(int row, int col, Color color) {
    super(row, col, color);
  }

  @Override
  protected boolean validMove(int row, int col) {
    return row == getRow()
            || col == getColumn()
            || Math.abs(row - getRow()) == Math.abs(col - getColumn());
  }
}
