import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for pill add monitor.
 */
public class PillAddMonitorTest {
  @Test
  public void monitorUsage() {
    PillAddMonitor monitor = new PillAddMonitor(new LoggingPillCounter());
    assertTrue(conveyerBelt(monitor));
    // print to see the pattern: [100 x100-times, 5 x1000-times, 100 x500-times]
    System.out.println("Add calls per bottle: " + monitor.getAddCounts());
  }

  private boolean conveyerBelt(PillCounter counter) {
    for (int bottle = 0; bottle < 100; bottle++) {
      for (int pill = 0; pill < 100; pill++) {
        counter.addPill(1);
      }
      assertEquals(100, counter.getPillCount());
      counter.reset();
    }
    for (int bottle = 0; bottle < 1000; bottle++) {
      for (int pill = 0; pill < 20; pill += 4) {
        counter.addPill(4);
      }
      assertEquals(20, counter.getPillCount());
      counter.reset();
    }
    for (int bottle = 0; bottle < 500; bottle++) {
      for (int pill = 0; pill < 200; pill += 2) {
        counter.addPill(2);
      }
      assertEquals(200, counter.getPillCount());
      counter.reset();
    }
    return true;
  }
}
