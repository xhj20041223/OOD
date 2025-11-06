package spreadsheet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for average macro.
 */
public class AverageMacroTest {
  @Test
  public void testAverageValid() {
    SpreadSheet underlying = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl();

    // Populate a 2×2 block with {2,4;6,8}
    sheet.set(0, 0, 2.0);
    sheet.set(0, 1, 4.0);
    sheet.set(1, 0, 6.0);
    sheet.set(1, 1, 8.0);

    // average over (0,0)-(1,1), store at (2,2)
    AverageMacro macro = new AverageMacro(0, 0, 1, 1, 2, 2);
    sheet.execute(macro);

    // average = (2+4+6+8)/4 = 5.0 → placed at (2,2)
    assertEquals(5.0, sheet.get(2, 2), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAverageInvalidRange() {
    new AverageMacro(1, 1, 0, 0, 2, 2);
  }
}
