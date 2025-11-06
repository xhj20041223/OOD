package initial;

/**
 * Represent a GradeSchema that support adding several items with a combined weight such that the
 * weight is equally distributed among the items.
 * All other methods are extend from GradeSchema, so it also support all other methods that
 * are supported in GradeSchema
 */

public interface IEquallyWeightedGradeSchema extends GradeSchema {
  /**
   * Adds several new equally weighted gradable items into this grading schema.
   *
   * @param name   the name of the gradable items to be added
   * @param weight the weight of the gradable item, between 0 and 100%
   * @param number the number of items to be added
   * @throws IllegalArgumentException if the name of the item is null, if an item with the
   * specified name already exists in the schema,
   *                                  or if the specified weight is not between 0 and 100
   */
  public void addGradeableItem(String name, double weight, int number) throws IllegalArgumentException;
}
