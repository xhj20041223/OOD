/**
 * Abstract base class for all chess pieces. Handles common properties like position and color,
 * as well as default implementations for canMove boundary checks and canKill logic.
 */
public abstract class AbstractChessPiece implements ChessPiece {
  private final int row;
  private final int col;
  private final Color color;

  /**
   * Constructs a chess piece at the specified position with the given color.
   *
   * @param row   the row position (0-7)
   * @param col   the column position (0-7)
   * @param color the color of the piece (WHITE or BLACK)
   * @throws IllegalArgumentException if row or col is negative
   */
  public AbstractChessPiece(int row, int col, Color color) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Illegal position");
    }
    this.row = row;
    this.col = col;
    this.color = color;
  }

  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int getColumn() {
    return col;
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public boolean canMove(int row, int col) {
    if (row < 0 || col < 0 || row > 7 || col > 7) {
      return false;
    }
    if (row == this.row && col == this.col) {
      return false;
    }
    return validMove(row, col);
  }

  @Override
  public boolean canKill(ChessPiece piece) {
    return this.color != piece.getColor() && canMove(piece.getRow(), piece.getColumn());
  }

  /**
   * Defines piece-specific movement rules. Subclasses implement this to indicate
   * which target cells match their movement pattern.
   *
   * @param row target row
   * @param col target column
   * @return true if the move conforms to this piece's movement pattern
   */
  protected abstract boolean validMove(int row, int col);
}
