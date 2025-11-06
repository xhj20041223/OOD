package betterburrito;

import burrito.PortionSize;
import burrito.Protein;
import burrito.Topping;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a veggie burrito. A veggie burrito has black beans,
 * medium salsa, cheese, lettuce, and guacamole, all in the regular portions.
 * Instances are immutable; use the builder to construct.
 */
public class VeggieBurrito implements ObservableBurrito {
  private final CustomBurrito base;

  VeggieBurrito(CustomBurrito base) {
    this.base = base;
  }

  @Override
  public double cost() {
    return base.cost();
  }

  @Override
  public PortionSize hasTopping(Topping name) {
    return base.hasTopping(name);
  }

  @Override
  public PortionSize hasProtein(Protein name) {
    return base.hasProtein(name);
  }

  /**
   * Builder for VeggieBurrito, with convenient noX() methods.
   */
  public static class VeggieBurritoBuilder extends BurritoBuilder<VeggieBurritoBuilder> {
    /**
     * Constructor for VeggieBurritoBuilder.
     */
    public VeggieBurritoBuilder() {
      super();
      // default veggie ingredients
      addProtein(Protein.BlackBeans, PortionSize.Normal);
      addTopping(Topping.MediumSalsa, PortionSize.Normal);
      addTopping(Topping.Cheese, PortionSize.Normal);
      addTopping(Topping.Lettuce, PortionSize.Normal);
      addTopping(Topping.Guacamole, PortionSize.Normal);
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
      CustomBurrito custom = new CustomBurrito(size, pm, tm);
      return new VeggieBurrito(custom);
    }

    @Override
    protected VeggieBurritoBuilder returnBuilder() {
      return this;
    }

    private void removeTopping(Topping t) {
      for (int i = 0; i < toppings.size(); i++) {
        if (toppings.get(i) == t) {
          toppings.remove(i);
          toppingSizes.remove(i);
          break;
        }
      }
    }

    private void removeProtein(Protein p) {
      for (int i = 0; i < proteins.size(); i++) {
        if (proteins.get(i) == p) {
          proteins.remove(i);
          proteinSizes.remove(i);
          break;
        }
      }
    }

    public VeggieBurritoBuilder noCheese() {
      removeTopping(Topping.Cheese);
      return this;
    }

    public VeggieBurritoBuilder noBlackBeans() {
      removeProtein(Protein.BlackBeans);
      return this;
    }

    public VeggieBurritoBuilder noMediumSalsa() {
      removeTopping(Topping.MediumSalsa);
      return this;
    }

    public VeggieBurritoBuilder noLettuce() {
      removeTopping(Topping.Lettuce);
      return this;
    }

    public VeggieBurritoBuilder noGuacamole() {
      removeTopping(Topping.Guacamole);
      return this;
    }
  }
}
