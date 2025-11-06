package initial;

import java.util.HashMap;
import java.util.Map;

public class ExtraCreditGradeSchema implements GradeSchema {

  private Map<String, Double> ECWeights;
  private SimpleGradeSchema simpleGradeSchema;

  public ExtraCreditGradeSchema() {
    ECWeights = new HashMap<String, Double>();
    simpleGradeSchema = new SimpleGradeSchema();
  }

  @Override
  public void addGradeableItem(String name, double weight) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name of gradeable item cannot be null");
    }
    else if (name.contains("Extra")) {
      if (ECWeights.containsKey(name)) {
        throw new IllegalArgumentException(
                "Item with name: " + name + " already exists in schema.");
      }
      if ((weight < 0) || (weight > 100)) {
        throw new IllegalArgumentException("Invalid weight, must be between 0 and 100)");
      }
      ECWeights.put(name, weight);
    }
    else {
      simpleGradeSchema.addGradeableItem(name, weight);
    }
  }

  @Override
  public double getWeight(String name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name of item cannot be null");
    }
    if (ECWeights.containsKey(name)) {
      return ECWeights.get(name);
    }
    return simpleGradeSchema.getWeight(name);
  }

  @Override
  public double getWeightedTotal(Map<String, Double> score) throws IllegalArgumentException {
    double weightedTotal = 0;

    if (score==null) {
      throw new IllegalArgumentException("The provided scores map is null");
    }

    for (Map.Entry<String, Double> item : ECWeights.entrySet()) {
      if (score.containsKey(item.getKey()))
      weightedTotal += item.getValue() * score.get(item.getKey());
      score.remove(item.getKey());
    }
    weightedTotal /= 100.0;
    return weightedTotal + simpleGradeSchema.getWeightedTotal(score);
  }

  @Override
  public double getSumOfWeights() {
    return simpleGradeSchema.getSumOfWeights();
  }
}
