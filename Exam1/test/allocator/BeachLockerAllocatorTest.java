package allocator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public final class BeachLockerAllocatorTest {
  @Test
  public void testRentMore() {
    LockerAllocator<Bag> locker = new BeachLockerAllocator(1, 100);
    try {
      for (int i = 1; i <= 100; i++) {
        assertEquals(i, locker.rent());
      }
    } catch (Exception e) {
      fail("should able to store 100 lockers but it did not");
    }
    try {
      locker.rent();
      fail("able to rent more locker than the capacity");
    } catch (Exception ignored) {
    }
  }


  @Test
  public void testOutOfCommision() {
    LockerAllocator<Bag> locker = new BeachLockerAllocator(2000, 2010);
    for (int i = 1; i <= 5; i++) {
      assertEquals(i + 1999, locker.rent());
    }
    locker.out(2005);
    locker.out(2007);
    locker.out(2009);
    assertEquals(2006, locker.rent());
    locker.free(2000);
    locker.serviced(2005);
    assertEquals(2000, locker.rent());
    assertEquals(2005, locker.rent());
  }
}