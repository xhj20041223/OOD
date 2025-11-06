package spreadsheet;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;


/**
 * Tests for MacroSpreadSheetController.
 */
public class MacroSpreadSheetControllerTest {
  @Test
  public void testBulkAssignViaController() throws IOException {
    SpreadSheet underlying = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl();

    // The input script: bulk-assign-value A 1 B 2 7.7  q
    StringReader reader = new StringReader("bulk-assign-value A 1 B 2 7.7 q");
    StringWriter writer = new StringWriter();
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, reader, writer);
    controller.run();

    // A1 (0,0), A2 (0,1), B1 (1,0), B2 (1,1) → all should be 7.7
    assertEquals(7.7, sheet.get(0, 0), 0.0001);
    assertEquals(7.7, sheet.get(0, 1), 0.0001);
    assertEquals(7.7, sheet.get(1, 0), 0.0001);
    assertEquals(7.7, sheet.get(1, 1), 0.0001);
  }
}
