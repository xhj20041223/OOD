package vehicle;

/**
 * A manual transmission for controlling speed and gear shifts.
 * All operations return a new, immutable transmission instance.
 */
public interface ManualTransmission {

  /**
   * Shifts the gear up by one if current speed meets the next gear's minimum.
   *
   * @return a new ManualTransmission reflecting the state after the shift
   */
  ManualTransmission increaseGear();

  /**
   * Shifts the gear down by one if current speed does not exceed the previous gear's maximum.
   *
   * @return a new ManualTransmission reflecting the state after the shift
   */
  ManualTransmission decreaseGear();

  /**
   * Increases the speed by one unit, if within the current gear limits.
   *
   * @return a new ManualTransmission reflecting the state after accelerating
   */
  ManualTransmission increaseSpeed();

  /**
   * Decreases the speed by one unit, if above the current gear's minimum.
   *
   * @return a new ManualTransmission reflecting the state after decelerating
   */
  ManualTransmission decreaseSpeed();

  /**
   * Get the current speed of the vehicle.
   *
   * @return the current speed
   */
  int getSpeed();

  /**
   * Get the current gear of the vehicle.
   *
   * @return the current gear index
   */
  int getGear();

  /**
   * Get the current status of the vehicle.
   *
   * @return the status message of the last operation
   */
  String getStatus();
}
