package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder for burritos using the recursive-generics pattern.
 *
 * @param <T> the concrete builder type
 */
public abstract class BurritoBuilder<T extends BurritoBuilder<T>> {
  protected Size size;  // must be set by caller
  protected final List<Protein> proteins = new ArrayList<>();
  protected final List<PortionSize> proteinSizes = new ArrayList<>();
  protected final List<Topping> toppings = new ArrayList<>();
  protected final List<PortionSize> toppingSizes = new ArrayList<>();

  /**
   * Set the size of this burrito (required).
   *
   * @param size the desired size
   * @return this builder
   */
  public T size(Size size) {
    this.size = size;
    return returnBuilder();
  }

  /**
   * Add a protein in the specified portion.
   *
   * @param protein the protein to add
   * @param portion the portion size of the protein
   * @return this builder
   */
  public T addProtein(Protein protein, PortionSize portion) {
    proteins.add(protein);
    proteinSizes.add(portion);
    return returnBuilder();
  }

  /**
   * Add a topping in the specified portion.
   *
   * @param topping the topping to add
   * @param portion the portion size of the topping
   * @return this builder
   */
  public T addTopping(Topping topping, PortionSize portion) {
    toppings.add(topping);
    toppingSizes.add(portion);
    return returnBuilder();
  }

  /**
   * Construct the final burrito (immutable).
   *
   * @return a built, immutable burrito
   * @throws IllegalStateException if required fields (e.g., size) are not set
   */
  public abstract betterburrito.ObservableBurrito build();

  /**
   * Return this builder (for chaining).
   *
   * @return this
   */
  protected abstract T returnBuilder();
}
