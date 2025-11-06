package initial;

public class EquallyWeightedGradeSchema extends SimpleGradeSchema implements IEquallyWeightedGradeSchema{
  @Override
  public void addGradeableItem(String name, double weight, int number) throws IllegalArgumentException {
    for (int i = 1 ; i <= number ; i++) {
      super.addGradeableItem(name + " " + i, weight/number);
    }
  }
}
