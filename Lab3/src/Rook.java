/**
 * Represents a rook which moves horizontally or vertically any number of squares.
 */
public class Rook extends AbstractChessPiece {
  /**
   * Constructs a rook at the given position and color.
   *
   * @param row   the row position (0-7)
   * @param col   the column position (0-7)
   * @param color the piece color
   */
  public Rook(int row, int col, Color color) {
    super(row, col, color);
  }

  @Override
  protected boolean validMove(int row, int col) {
    return row == getRow() || col == getColumn();
  }
}
