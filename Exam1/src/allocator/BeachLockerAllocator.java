package allocator;


import java.util.ArrayList;

/**
 * This class represents a locker allocator at a beach location.
 * <p>
 * This class assumes that the id of a locker is a function of
 * the approximate distance of that locker from the checkout counter.
 * In other words, lockers closer to the counter have a smaller ID
 * than those farther away.
 * <p>
 * Furthermore it assumes that all locker numbers are within a
 * contiguous range (e.g. 2, 3, 4, 5, 6, 7, 8).
 * While the locker numbers should always be positive,
 * they can start and end at any number.
 * <p>
 * In order to help their customers and also optimize the cleanup time
 * at the end of the day, this allocator always tries to allocate
 * a free locker that is closest to the checkout counter.
 * <p>
 * One of the desired features of this allocator is that
 * it <b>quickly</b> finds a locker to be rented.
 */
public class BeachLockerAllocator implements LockerAllocator<Bag> {
  // represent the available lockers
  private ArrayList<Integer> lockers;
  // represent the outed lockers
  private ArrayList<Integer> outs;
  // represent the possible max id of the lockers
  private int max;
  // represent the possible max id of the lockers
  private int min;


  /**
   * Construct a new beach locker allocator.
   * All numbers within the specified range are assumed
   * to be functional and rent-able.
   *
   * @param minLockerNumber the smallest locker number to be used (inclusive)
   * @param maxLockerNumber the largest locker number to be used (inclusive)
   * @throws IllegalArgumentException if the max locker number is not greater than the min locker
   *                                  number
   *                                  or any part of the given range is non-positive.
   */
  public BeachLockerAllocator(int minLockerNumber, int maxLockerNumber) throws IllegalArgumentException {
    this.lockers = new ArrayList<Integer>();
    for (int i = minLockerNumber; i <= maxLockerNumber; i++) {
      this.lockers.add(i);
    }
    this.outs = new ArrayList<Integer>();
    this.max = maxLockerNumber;
    this.min = minLockerNumber;
  }

  @Override
  public int rent() throws IllegalStateException {
    if (this.lockers.isEmpty()) {
      throw new IllegalStateException("all lockers are used");
    } else {
      return this.lockers.remove(0);
    }
  }

  @Override
  public void free(int id) throws IllegalArgumentException {
    if(id < this.lockers.get(0)){
      this.lockers.set(0, id);
    }
    else {
      for (int i = 0; i < this.lockers.size() - 1; i++) {
        if (this.lockers.get(i) < id && this.lockers.get(i + 1) > id) {
          this.lockers.add(i + 1, id);
          return;
        }
      }
    }
  }

  @Override
  public void deposit(int id, Bag equipment) throws IllegalArgumentException {

  }

  @Override
  public Bag get(int id) throws IllegalArgumentException {
    return null;
  }

  /**
   * Mark a locker "Out of Commission" and it will not able to be used temporarily
   *
   * @param id the unique integer ID of the locker to be marked
   * @throws IllegalArgumentException if the id represents an already marked
   *                                  locker, or is an invalid number, or it
   *                                  is in rental.
   */
  public void out(int id) throws IllegalArgumentException {
    if (id < this.min || id > this.max) {
      throw new IllegalArgumentException("invalid id");
    } else if (!this.lockers.contains(id)) {
      throw new IllegalArgumentException("locker in rental");
    } else if (this.outs.contains(id)) {
      throw new IllegalArgumentException("already out of commission");
    } else {
      this.outs.add(id);
      for (int i = 0; i < this.lockers.size(); i++) {
        if (this.lockers.get(i) == id) {
          this.lockers.remove(i);
        }
      }
    }
  }


  /**
   * Unmark a "Out of Commission" locker and unable it to be used again
   *
   * @param id the unique integer ID of the locker to be unmarked
   * @throws IllegalArgumentException if the id represents an already unmarked
   *                                  locker, or is an invalid number.
   */

  public void serviced(int id) throws IllegalArgumentException {

    if (id < this.min || id > this.max) {
      throw new IllegalArgumentException("invalid id");
    } else if (!this.outs.contains(id)) {
      throw new IllegalArgumentException("locker is not out of commission");
    } else {
      for (int i = 0; i < this.outs.size(); i++) {
        if (this.outs.get(i) == id) {
          this.outs.remove(i);
        }
      }
      if(id < this.lockers.get(0)){
        this.lockers.add(0, id);
      }
      else {
        for (int i = 0; i < this.lockers.size() - 1; i++) {
          if (this.lockers.get(i) < id && this.lockers.get(i + 1) > id) {
            this.lockers.add(i + 1, id);
            return;
          }
        }
      }
    }
  }
}
