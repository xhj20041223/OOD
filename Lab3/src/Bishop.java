/**
 * Represents a bishop which moves diagonally any number of squares.
 */
public class Bishop extends AbstractChessPiece {
  /**
   * Constructs a bishop at the given position and color.
   *
   * @param row   the row position (0-7)
   * @param col   the column position (0-7)
   * @param color the piece color
   */
  public Bishop(int row, int col, Color color) {
    super(row, col, color);
  }

  @Override
  protected boolean validMove(int row, int col) {
    return Math.abs(row - getRow()) == Math.abs(col - getColumn());
  }
}
