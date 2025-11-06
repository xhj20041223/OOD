/**
 represent a date, including year, month and day.
 */
public class MyDate {
  int year;
  int month;
  int day;

  /**
  initiate the date by given year month and day.

   @param day the day of the date
   @param month the month of the date
   @param year the year of the date
   */

  public MyDate(int day, int month, int year) {
    this.year = year;
    this.month = month;
    this.day = day;
    if (!this.isValid()) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  // check whether the given year, month, and day is valid
  boolean isValid() {
    if (this.year < 0 || this.year > 9999) {
      return false;
    }
    if (this.month < 1 || this.month > 12) {
      return false;
    }
    if (this.day < 1 || this.day > 31) {
      return false;
    }
    if (this.day == 31 && (this.month == 2 || this.month == 4 || this.month == 6
            || this.month == 9 || this.month == 11)) {
      return false;
    }
    if (this.month == 2 && this.day == 30) {
      return false;
    }
    if (this.month == 2 && this.day == 29) {
      return this.isLeapYear();
    }
    return true;
  }

  // check whether the year is leap year
  boolean isLeapYear() {
    return this.year % 4 == 0 && this.year % 100 != 0 || this.year % 400 == 0;
  }

  /**
   advance the date by the given days.
   */

  public void advance(int days) {
    this.day += days;
    this.rearrange();
  }

  // rearrange and formate the day, month, and year
  void rearrange() {
    if (this.year < 0) {
      this.year = 0;
      this.month = 1;
      this.day = 1;
      return;
    }
    if (this.month > 12) {
      this.year += this.month / 12;
      this.month %= 12;
      this.rearrange();
    } else if (this.month < 1) {
      this.year--;
      this.month += 12;
      this.rearrange();
    }

    if (this.day > 31 && (this.month == 12 || this.month == 1 || this.month == 3 || this.month == 5
            || this.month == 7 || this.month == 8 || this.month == 10)) {
      this.day -= 31;
      this.month++;
      this.rearrange();
    } else if (this.day > 30
            && (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11)) {
      this.day -= 30;
      this.month++;
      this.rearrange();
    } else if (this.month == 2 && this.day > 29 && this.isLeapYear()) {
      this.day -= 29;
      this.month++;
      this.rearrange();
    } else if (this.month == 2 && this.day > 28 && !this.isLeapYear()) {
      this.day -= 28;
      this.month++;
      this.rearrange();
    } else if (this.day < 1) {
      this.month--;
      if (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11) {
        this.day += 30;
      } else if (this.month == 0 || this.month == 1 || this.month == 3 || this.month == 5
              || this.month == 7 || this.month == 8 || this.month == 10) {
        this.day += 31;
      } else if (this.isLeapYear()) {
        this.day += 29;
      } else {
        this.day += 28;
      }
      this.rearrange();
    }
  }

  @Override
  public String toString() {
    return String.format("%04d-%02d-%02d", year, month, day);
  }
}
