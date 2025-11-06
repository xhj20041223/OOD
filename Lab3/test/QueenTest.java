/**
 * Test suite for Queen class, verifying its combined horizontal, vertical, and diagonal movement
 * and killing patterns.
 */
public class QueenTest extends AbstractChessPieceTest {
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Queen(row, col, color);
  }

  @Override
  protected void setupResults(int row, int col) {
    // horizontal and vertical
    for (int i = 0; i < 8; i++) {
      results[row][i] = true;
      results[i][col] = true;
    }
    // diagonals
    for (int d = 0; d < 8; d++) {
      if (row + d < 8 && col + d < 8) {
        results[row + d][col + d] = true;
      }
      if (row + d < 8 && col - d >= 0) {
        results[row + d][col - d] = true;
      }
      if (row - d >= 0 && col + d < 8) {
        results[row - d][col + d] = true;
      }
      if (row - d >= 0 && col - d >= 0) {
        results[row - d][col - d] = true;
      }
    }
    results[row][col] = false;
  }
}
