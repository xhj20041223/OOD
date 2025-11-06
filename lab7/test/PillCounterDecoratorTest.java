import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for pill counter decorator.
 */
public class PillCounterDecoratorTest {
  @Test
  public void usage() {
    PillCounter counter = new PillCounterDecorator(new LoggingPillCounter());
    assertTrue(conveyerBelt(counter));
  }

  // exactly the same heavy‐use scenario as in PillCounterBeginTest
  private boolean conveyerBelt(PillCounter counter) {
    // make 100 bottles of 100 pills each
    for (int bottle = 0; bottle < 100; bottle++) {
      for (int pill = 0; pill < 100; pill++) {
        counter.addPill(1);
      }
      assertEquals(100, counter.getPillCount());
      counter.reset();
    }
    // make 1000 bottles of 20 pills each
    for (int bottle = 0; bottle < 1000; bottle++) {
      for (int pill = 0; pill < 20; pill += 4) {
        counter.addPill(4);
      }
      assertEquals(20, counter.getPillCount());
      counter.reset();
    }
    // make 500 bottles of 200 pills each
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
