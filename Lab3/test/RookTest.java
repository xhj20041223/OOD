/**
 * Test suite for Rook class, verifying its horizontal and vertical movement and killing patterns.
 */
public class RookTest extends AbstractChessPieceTest {
  @Override
  protected ChessPiece createPiece(int row, int col, Color color) {
    return new Rook(row, col, color);
  }

  @Override
  protected void setupResults(int row, int col) {
    for (int i = 0; i < 8; i++) {
      results[row][i] = true;
      results[i][col] = true;
    }
    results[row][col] = false;
  }
}
