import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Abstract test suite providing common tests for all chess pieces.
 * Subclasses supply specific creation logic and expected move patterns.
 */
public abstract class AbstractChessPieceTest {
  protected boolean[][] results;

  @Before
  public void setup() {
    results = new boolean[8][8];
    initializeResults();
  }

  protected abstract ChessPiece createPiece(int row, int col, Color color);

  protected abstract void setupResults(int row, int col);

  private void initializeResults() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        results[i][j] = false;
      }
    }
  }

  @Test
  public void testGetters() {
    for (Color c : Color.values()) {
      for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
          ChessPiece piece = createPiece(row, col, c);
          assertEquals(row, piece.getRow());
          assertEquals(col, piece.getColumn());
          assertEquals(c, piece.getColor());
        }
      }
    }
  }

  @Test
  public void testInvalidConstructions() {
    for (Color c : Color.values()) {
      for (int i = 0; i < 8; i++) {
        try {
          createPiece(i, -1, c);
          fail("Expected IllegalArgumentException for invalid column");
        } catch (IllegalArgumentException ignored) {
        }
        try {
          createPiece(-1, i, c);
          fail("Expected IllegalArgumentException for invalid row");
        } catch (IllegalArgumentException ignored) {
        }
      }
    }
  }

  @Test
  public void testCanMove() {
    for (Color c : Color.values()) {
      for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
          initializeResults();
          setupResults(row, col);
          ChessPiece piece = createPiece(row, col, c);
          for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
              if (i == row && j == col) {
                continue;
              }
              assertEquals("Unexpected canMove for " + this.getClass().getSimpleName() +
                              " from (" + row + "," + col + ") to (" + i + "," + j + ")",
                      results[i][j], piece.canMove(i, j));
            }
          }
        }
      }
    }
  }

  @Test
  public void testCanKill() {
    for (Color c : Color.values()) {
      for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
          initializeResults();
          setupResults(row, col);
          ChessPiece piece = createPiece(row, col, c);
          for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
              if (i == row && j == col) {
                continue;
              }
              Color enemyColor =
                      Color.values()[(c.ordinal() + 1) % Color.values().length];
              assertEquals("Unexpected canKill for " + this.getClass().getSimpleName() +
                              " from (" + row + "," + col + ") to (" + i + "," + j + ")",
                      results[i][j], piece.canKill(createPiece(i, j, enemyColor)));
            }
          }
        }
      }
    }
  }
}
