import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * the test cases for MyDate.
 */

public class MyDateTest {

  // Test valid dates and toString formatting
  @Test
  public void testConstructorAndToString() {
    assertEquals("2021-01-31", new MyDate(31, 1, 2021).toString());
    assertEquals("2020-02-29", new MyDate(29, 2, 2020).toString());
    assertEquals("0005-05-05", new MyDate(5, 5, 5).toString());
    assertEquals("0009-09-09", new MyDate(9, 9, 9).toString());
  }

  // Test invalid constructor inputs
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidMonthLow() {
    new MyDate(10, 0, 2021);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidMonthHigh() {
    new MyDate(1, 13, 2021);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidDayLow() {
    new MyDate(0, 1, 2021);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidDayHigh() {
    new MyDate(32, 1, 2021);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidDayForApril() {
    new MyDate(31, 4, 2021);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidFebNonLeap() {
    new MyDate(29, 2, 2019);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorYearOutOfRangeLow() {
    new MyDate(-1, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorYearOutOfRangeHigh() {
    new MyDate(1, 1, 10000);
  }

  // Test advance method for various scenarios
  @Test
  public void testAdvance() {
    MyDate d = new MyDate(15, 1, 2021);
    d.advance(1);
    assertEquals("2021-01-16", d.toString());

    d = new MyDate(31, 1, 2021);
    d.advance(1);
    assertEquals("2021-02-01", d.toString());

    d = new MyDate(31, 12, 2021);
    d.advance(1);
    assertEquals("2022-01-01", d.toString());

    d = new MyDate(28, 2, 2020);
    d.advance(1);
    assertEquals("2020-02-29", d.toString());
    d.advance(1);
    assertEquals("2020-03-01", d.toString());

    d = new MyDate(28, 2, 2019);
    d.advance(1);
    assertEquals("2019-03-01", d.toString());

    d = new MyDate(1, 3, 2021);
    d.advance(-1);
    assertEquals("2021-02-28", d.toString());

  }

  @Test
  public void testAdvanceManyDays() {
    MyDate d = new MyDate(1, 1, 2021);
    d.advance(365);
    assertEquals("2022-01-01", d.toString());

    d = new MyDate(1, 3, 21);
    d.advance(-1000000);
    assertEquals("0000-01-01", d.toString());
  }
}
