package vehicle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for RegularManualTransmission using overlapping gear ranges.
 * Covers constructor validation, speed changes, and gear shifts.
 */
public class RegularManualTransmissionTest {

  // Helper to build a 5-gear transmission with overlaps:
  // Gear 0: 0–15, Gear 1: 10–25, Gear 2: 20–35, Gear 3: 30–45, Gear 4: 40–50
  private RegularManualTransmission createStandard() {
    return new RegularManualTransmission(
            0, 15,
            10, 25,
            20, 35,
            30, 45,
            40, 50
    );
  }

  @Test
  public void testConstructorValidInitialState() {
    RegularManualTransmission t = createStandard();
    assertEquals(0, t.getSpeed());
    assertEquals(1, t.getGear());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorFirstGearLowMustBeZero() {
    new RegularManualTransmission(
            1, 15,    // invalid first-gear low
            10, 25,
            20, 35,
            30, 45,
            40, 50
    );
  }

  @Test
  public void testConstructorLowGreaterThanHighThrows() {
    try {
      new RegularManualTransmission(
              0, 15,
              12, 11,  // l2 > h2
              20, 35,
              30, 45,
              40, 50
      );
      fail("Expected IllegalArgumentException for low > high in a gear");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid gear: low >= high", e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNonCoverageThrows() {
    // Gap between h0=15 and l1=17 leaves speed 16 uncovered
    new RegularManualTransmission(
            0, 15,
            17, 25,
            20, 35,
            30, 45,
            40, 50
    );
  }

  @Test
  public void testConstructorAdjacentOverlapAllowed() {
    // Adjacent overlap is fine
    RegularManualTransmission t = new RegularManualTransmission(
            0, 15,
            10, 25,
            20, 35,
            30, 45,
            40, 50
    );
    assertEquals(0, t.getSpeed());
    assertEquals(1, t.getGear());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNonAdjacentOverlapThrows() {
    // Gear3 overlaps Gear1 non-adjacently (l3 < h1)
    new RegularManualTransmission(
            0, 15,
            10, 25,
            5, 35,   // invalid non-adjacent overlap
            30, 45,
            40, 50
    );
  }

  @Test
  public void testIncreaseSpeedNormal() {
    RegularManualTransmission t = createStandard();
    t.increaseSpeed();  // 0 → 1
    assertEquals(1, t.getSpeed());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test
  public void testIncreaseSpeedMayIncreaseGear() {
    RegularManualTransmission t = createStandard();
    // Accelerate up to the lower bound of Gear 1 (10)
    for (int i = 0; i < 10; i++) {
      t.increaseSpeed();
    }
    assertEquals(10, t.getSpeed());
    assertEquals("OK: you may increase the gear.", t.getStatus());
    assertEquals(1, t.getGear());
  }

  @Test
  public void testIncreaseSpeedAtSpeedLimit() {
    RegularManualTransmission t = createStandard();
    // Accelerate to the max speed (50)
    for (int i = 0; i < 50; i++) {
      t.increaseSpeed();
    }
    assertEquals(15, t.getSpeed());
    t.increaseSpeed();
    assertEquals(15, t.getSpeed());
    assertEquals("Cannot increase speed, increase gear first.", t.getStatus());
  }

  @Test
  public void testIncreaseGearRequiresSpeed() {
    RegularManualTransmission t = createStandard();
    t.increaseGear();
    assertEquals(1, t.getGear());
    assertEquals("Cannot increase gear, increase speed first.", t.getStatus());
  }

  @Test
  public void testIncreaseGearSuccess() {
    RegularManualTransmission t = createStandard();
    for (int i = 0; i < 10; i++) {
      t.increaseSpeed();
    }
    t.increaseGear();
    assertEquals(2, t.getGear());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test
  public void testDecreaseSpeedNormal() {
    RegularManualTransmission t = createStandard();
    for (int i = 0; i < 5; i++) {
      t.increaseSpeed();
    }
    t.decreaseSpeed();
    assertEquals(4, t.getSpeed());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test
  public void testDecreaseSpeedAtMinSpeedNeutral() {
    RegularManualTransmission t = createStandard();
    t.decreaseSpeed();
    assertEquals(0, t.getSpeed());
    assertEquals("Cannot decrease speed. Reached minimum speed.", t.getStatus());
  }

  @Test
  public void testDecreaseSpeedRequiresGear() {
    RegularManualTransmission t = createStandard();
    for (int i = 0; i < 10; i++) {
      t.increaseSpeed();
    }
    t.increaseGear();
    t.decreaseSpeed();
    assertEquals(10, t.getSpeed());
    assertEquals("Cannot decrease speed, decrease gear first.", t.getStatus());
  }

  @Test
  public void testDecreaseGearRequiresSpeed() {
    RegularManualTransmission t = createStandard();
    for (int i = 0; i < 15; i++) {
      t.increaseSpeed();
    }
    t.increaseGear();
    t.increaseSpeed();
    t.decreaseGear();
    assertEquals(2, t.getGear());
    assertEquals("Cannot decrease gear, decrease speed first.", t.getStatus());
  }

  @Test
  public void testDecreaseGearSuccess() {
    RegularManualTransmission t = createStandard();
    for (int i = 0; i < 10; i++) {
      t.increaseSpeed();
    }
    t.increaseGear();
    // slow down below Gear 0→15 threshold
    for (int i = 0; i < 2; i++) {
      t.decreaseSpeed();
    }
    t.decreaseGear();
    assertEquals(1, t.getGear());
    assertEquals("OK: everything is OK.", t.getStatus());
  }

  @Test
  public void testDecreaseGearAtMinGear() {
    RegularManualTransmission t = createStandard();
    t.decreaseGear();
    assertEquals(1, t.getGear());
    assertEquals("Cannot decrease gear. Reached minimum gear.", t.getStatus());
  }
}
