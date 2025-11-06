package spreadsheet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for range assign macro.
 */
public class RangeAssignMacroTest {
  @Test
  public void testRangeAssignRow() {
    SpreadSheet underlying = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl();

    // Assign a row from column 0 through 3 with start=1.0, increment=2.0
    RangeAssignMacro macro = new RangeAssignMacro(0, 0, 0, 3, 1.0, 2.0);
    sheet.execute(macro);

    // Cells (0,0)-(0,3) → {1.0, 3.0, 5.0, 7.0}
    assertEquals(1.0, sheet.get(0, 0), 0.0001);
    assertEquals(3.0, sheet.get(0, 1), 0.0001);
    assertEquals(5.0, sheet.get(0, 2), 0.0001);
    assertEquals(7.0, sheet.get(0, 3), 0.0001);
  }

  @Test
  public void testRangeAssignColumn() {
    SpreadSheet underlying = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl();

    // Assign a column from row 1 through 4 with start=0.5, increment=0.5
    RangeAssignMacro macro = new RangeAssignMacro(1, 2, 4, 2, 0.5, 0.5);
    sheet.execute(macro);

    // Cells (1,2)-(4,2) → {0.5, 1.0, 1.5, 2.0}
    assertEquals(0.5, sheet.get(1, 2), 0.0001);
    assertEquals(1.0, sheet.get(2, 2), 0.0001);
    assertEquals(1.5, sheet.get(3, 2), 0.0001);
    assertEquals(2.0, sheet.get(4, 2), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRangeBothDimensions() {
    // Neither a single row nor a single column
    new RangeAssignMacro(0, 0, 1, 1, 1.0, 1.0);
  }
}
