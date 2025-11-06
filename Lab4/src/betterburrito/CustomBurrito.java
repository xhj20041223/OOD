package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a custom burrito that can have an arbitrary number
 * of proteins and toppings, both of arbitrary portion sizes.
 * Instances are immutable; use the builder to construct.
 */
public class CustomBurrito implements ObservableBurrito {
  private final Size size;
  private final Map<Protein, PortionSize> proteins;
  private final Map<Topping, PortionSize> toppings;

  // package-private constructor for builders
  CustomBurrito(Size size,
                Map<Protein, PortionSize> proteins,
                Map<Topping, PortionSize> toppings) {
    this.size = size;
    this.proteins = proteins;
    this.toppings = toppings;
  }

  @Override
  public double cost() {
    double total = 0.0;
    for (Map.Entry<Protein, PortionSize> e : proteins.entrySet()) {
      total += e.getKey().getCost() * e.getValue().getCostMultipler();
    }
    for (Map.Entry<Topping, PortionSize> e : toppings.entrySet()) {
      total += e.getKey().getCost() * e.getValue().getCostMultipler();
    }
    return total + size.getBaseCost();
  }

  @Override
  public PortionSize hasTopping(Topping name) {
    return toppings.get(name);
  }

  @Override
  public PortionSize hasProtein(Protein name) {
    return proteins.get(name);
  }

  /**
   * Builder for CustomBurrito.
   */
  public static class CustomBurritoBuilder extends BurritoBuilder<CustomBurritoBuilder> {
    public CustomBurritoBuilder() {
      super();
    }

    @Override
    public ObservableBurrito build() {
      if (size == null) {
        throw new IllegalStateException("Size must be set before building");
      }
      Map<Protein, PortionSize> pm = new HashMap<>();
      for (int i = 0; i < proteins.size(); i++) {
        pm.put(proteins.get(i), proteinSizes.get(i));
      }
      Map<Topping, PortionSize> tm = new HashMap<>();
      for (int i = 0; i < toppings.size(); i++) {
        tm.put(toppings.get(i), toppingSizes.get(i));
      }
      return new CustomBurrito(size, pm, tm);
    }

    @Override
    protected CustomBurritoBuilder returnBuilder() {
      return this;
    }
  }
}
