package vehicle;

/**
 * A manual transmission implementation with exactly five gears.
 * Each gear is defined by a low/high speed range.  The ranges
 * must be strictly increasing and the first gear must start at 0.
 * Operations include increasing/decreasing speed and shifting gears.
 */
public class RegularManualTransmission implements ManualTransmission {
  private int speed = 0;
  private int gear = 0;
  private final Gear[] gears = new Gear[5];
  private String status = "OK: everything is OK.";

  /**
   * Constructs a transmission with five gears.
   *
   * @param l1 low for gear 0 (must be 0)
   * @param h1 high for gear 0
   * @param l2 low for gear 1
   * @param h2 high for gear 1
   * @param l3 low for gear 2
   * @param h3 high for gear 2
   * @param l4 low for gear 3
   * @param h4 high for gear 3
   * @param l5 low for gear 4
   * @param h5 high for gear 4
   * @throws IllegalArgumentException if l1 != 0 or adjacent gears
   *                                  do not satisfy the increasing rule
   */
  public RegularManualTransmission(
          int l1, int h1,
          int l2, int h2,
          int l3, int h3,
          int l4, int h4,
          int l5, int h5) {
    if (l1 != 0) {
      throw new IllegalArgumentException("First gear low must be 0");
    }
    this.gears[0] = new Gear(l1, h1);
    this.gears[1] = new Gear(l2, h2);
    this.gears[2] = new Gear(l3, h3);
    this.gears[3] = new Gear(l4, h4);
    this.gears[4] = new Gear(l5, h5);

    for (int i = 0; i < 4; i++) {
      if (!this.gears[i].smallerThan(this.gears[i + 1])) {
        throw new IllegalArgumentException("vehicle.gear ranges must be strictly increasing");
      }
    }
  }

  @Override
  public String getStatus() {
    return this.status;
  }

  @Override
  public int getSpeed() {
    return this.speed;
  }

  @Override
  public int getGear() {
    return this.gear + 1;
  }

  @Override
  public ManualTransmission increaseSpeed() {
    if (!atMaxSpeed()) {
      this.speed++;
      this.status = "OK: everything is OK.";
      if (this.gear < 4 && this.speed >= this.gears[this.gear + 1].getLow()) {
        this.status = "OK: you may increase the gear.";
      }
    } else if (this.gear == 4 && this.speed == this.gears[4].getHigh()) {
      this.status = "Cannot increase speed. Reached maximum speed.";
    } else {
      this.status = "Cannot increase speed, increase gear first.";
    }
    return this;
  }

  @Override
  public ManualTransmission decreaseSpeed() {
    if (!atMinSpeed()) {
      this.speed--;
      this.status = "OK: everything is OK.";
      if (this.gear > 0 && this.speed <= this.gears[this.gear - 1].getHigh()) {
        this.status = "OK: you may decrease the gear.";
      }
    } else if (this.gear == 0 && this.speed == 0) {
      this.status = "Cannot decrease speed. Reached minimum speed.";
    } else {
      this.status = "Cannot decrease speed, decrease gear first.";
    }
    return this;
  }

  @Override
  public ManualTransmission increaseGear() {
    if (this.gear < 4) {
      if (this.speed >= this.gears[this.gear + 1].getLow()) {
        gear++;
        this.status = "OK: everything is OK.";
      } else {
        this.status = "Cannot increase gear, increase speed first.";
      }
    } else {
      this.status = "Cannot increase gear. Reached maximum gear.";
    }
    return this;
  }

  @Override
  public ManualTransmission decreaseGear() {
    if (this.gear > 0) {
      if (this.speed <= this.gears[this.gear - 1].getHigh()) {
        gear--;
        this.status = "OK: everything is OK.";
      } else {
        this.status = "Cannot decrease gear, decrease speed first.";
      }
    } else {
      this.status = "Cannot decrease gear. Reached minimum gear.";
    }
    return this;
  }

  private boolean atMaxSpeed() {
    return this.speed == this.gears[this.gear].getHigh();
  }

  private boolean atMinSpeed() {
    return this.speed == this.gears[this.gear].getLow();
  }
}
