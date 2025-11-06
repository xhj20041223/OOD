package burrito;

/**
 * This interface represents all the operations offered by a burrito.
 */
public interface Burrito {
  /**
   * Calculate and return the cost of this burrito using its ingredients.
   *
   * @return the cost of this burrito in USD, MM.XX format
   */
  double cost();

  /**
   * Add the specified protein in the specified portion size to this burrito.
   *
   * @param p    the protein to be added
   * @param size the portion size of this protein
   */
  void addProtein(Protein p, PortionSize size);

  /**
   * Add a specific topping in the specified size to this burrito.
   *
   * @param t    the topping to be added
   * @param size the portion size of this topping
   */
  void addTopping(Topping t, PortionSize size);

  /**
   * Set the size of this burrito.
   *
   * @param size the size of this burrito
   */
  void setSize(Size size);

  /**
   * Determines if the specified topping is on this burrito and if so, return its portion.
   *
   * @param name the name of the topping
   * @return the portion of this topping on this burrito, or null if the given topping is not
   *          on this burrito
   */
  PortionSize hasTopping(Topping name);

  /**
   * Determines if the specified protein is on this burrito and if so, return its portion.
   *
   * @param name the name of the protein
   * @return the portion of this protein on this burrito, or null if the given protein is not
   *          on this pizza
   */
  PortionSize hasProtein(Protein name);

}
