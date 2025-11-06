package initial;

import java.util.Map;

/**
 * Represent a course include a grade schema. This course support following abilities:
 * 1. The ability to register and withdraw students.
 * 2. The ability to add graded work to the course, as the course progresses. For example, add
 * "Assignment 1" at the beginning of the course, then add other assignments and exams over time.
 * 3. The ability to enter scores for students for specific graded work.
 * 4. The ability to query the current numeric grade of a student.
 */
public interface Course {

  /**
   * Registers a new student into this course.
   *
   * @param student the unique name of the student to be registered in this course
   * @throws IllegalArgumentException if the name of the student is null,
   *                                  or if a student with the specified name already exists in the
   *                                  course
   */
  void register(String student) throws IllegalArgumentException;

  /**
   * Withdraws a existing student out of this course.
   *
   * @param student the unique name of the student to be withdraw in this course
   * @throws IllegalArgumentException if the name of the student is null,
   *                                  or if a student with the specified name have not registered
   *                                  this course
   */
  void withdraw(String student) throws IllegalArgumentException;

  /**
   * Adds a new gradable item into this grading schema of the course.
   *
   * @param name   the unique name of the gradable item to be added
   * @param weight the weight of the gradable item, between 0 and 100%
   * @throws IllegalArgumentException if the name of the item is null,
   *                                  an item with the specified name already exists in the course,
   *                                  or if the specified weight is not between 0 and 100
   */
  void addGradeableItem(String name, double weight) throws IllegalArgumentException;

  /**
   * Enter the score of the specific student.
   *
   * @param student the targeted student to be added score
   * @param score   the scores by a student, as a map of (name, percentage-score).
   *                The percentage scores are numbers between 0 and 100
   * @throws IllegalArgumentException if the argument is null,
   *                                  the targeted student did not exist in the course
   *                                  a percentage-score is greater than 100 or less than 0
   *                                  or the grading item did not exist in the course
   */
  void addScore(String student, Map<String, Double> score) throws IllegalArgumentException;

  /**
   * Get the numeric grade of the targeted student.
   * It is possible that this does not add up to 100.
   * If nothing is putting yet, it will return 0.
   *
   * @param student the targeted student to be added score
   * @return the numeric grade of the targeted student
   * @throws IllegalArgumentException if the argument is null,
   *                                  or the targeted student did not exist in the course
   */
  double getStudentScore(String student) throws IllegalArgumentException;
}
