/**
 * Test suite for Bishop class, verifying its diagonal movement and killing patterns.
 */
public class BishopTest extends AbstractChessPieceTest {
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Bishop(row, col, color);
  }

  @Override
  protected void setupResults(int row, int col) {
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
