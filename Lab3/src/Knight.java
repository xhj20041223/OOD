/**
 * Represents a knight which moves in an L-shape.
 */
public class Knight extends AbstractChessPiece {
  /**
   * Constructs a knight at the given position and color.
   *
   * @param row   the row position (0-7)
   * @param col   the column position (0-7)
   * @param color the piece color
   */
  public Knight(int row, int col, Color color) {
    super(row, col, color);
  }

  @Override
  protected boolean validMove(int row, int col) {
    int dr = Math.abs(row - getRow());
    int dc = Math.abs(col - getColumn());
    return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
  }
}