package vehicle;

/**
 * Represents a single gear in a manual transmission, defining the inclusive
 * minimum and maximum speeds at which this gear is valid.
 */
public class Gear {
  private final int low;
  private final int high;

  /**
   * Constructs a vehicle.Gear with the specified speed range.
   *
   * @param low  the minimum speed (inclusive) for this gear
   * @param high the maximum speed (inclusive) for this gear
   * @throws IllegalArgumentException if low >= high
   */
  public Gear(int low, int high) {
    if (low >= high) {
      throw new IllegalArgumentException("Invalid gear: low >= high");
    }
    this.low = low;
    this.high = high;
  }

  /**
   * Returns the minimum speed for this gear.
   *
   * @return the low speed bound
   */
  public int getLow() {
    return low;
  }

  /**
   * Returns the maximum speed for this gear.
   *
   * @return the high speed bound
   */
  public int getHigh() {
    return high;
  }

  /**
   * Determines whether this gear is strictly "lower" than another gear.
   * That is, this.high < other.high, this.low < other.low, and this.high >= other.low.
   *
   * @param other the vehicle.Gear to compare against
   * @return true if this gear is strictly lower than other; false otherwise
   */
  public boolean smallerThan(Gear other) {
    return this.high < other.getHigh()
            && this.low < other.getLow()
            && this.high >= other.getLow();
  }
}
