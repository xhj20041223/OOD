package spreadsheet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for BulkAssignMacro.
 */
public class BulkAssignMacroTest {
  @Test
  public void testValidRange() {
    SpreadSheet underlying = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl();
    BulkAssignMacro macro = new BulkAssignMacro(1, 1, 2, 3, 5.5);
    sheet.execute(macro);

    // All cells in the range [1..2]×[1..3] should be 5.5
    for (int r = 1; r <= 2; r++) {
      for (int c = 1; c <= 3; c++) {
        Assert.assertEquals(5.5, sheet.get(r, c), 0.0001);
      }
    }
    // A cell outside remains empty (0)
    Assert.assertTrue(sheet.isEmpty(0, 0));
    Assert.assertEquals(0.0, sheet.get(0, 0), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRangeNegativeIndices() {
    new BulkAssignMacro(-1, 0, 2, 2, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRangeFromGreaterThanTo() {
    new BulkAssignMacro(3, 1, 2, 5, 2.0);
  }
}
