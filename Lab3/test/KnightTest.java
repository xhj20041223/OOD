/**
 * Test suite for Knight class, verifying its L-shaped movement and killing patterns.
 */
public class KnightTest extends AbstractChessPieceTest {
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Knight(row, col, color);
  }

  @Override
  protected void setupResults(int row, int col) {
    int[][] moves = {
            {row + 2, col + 1}, {row + 2, col - 1},
            {row - 2, col + 1}, {row - 2, col - 1},
            {row + 1, col + 2}, {row + 1, col - 2},
            {row - 1, col + 2}, {row - 1, col - 2}
    };
    for (int[] m : moves) {
      int r = m[0];
      int c = m[1];
      if (r >= 0 && r < 8 && c >= 0 && c < 8) {
        results[r][c] = true;
      }
    }
  }
}
