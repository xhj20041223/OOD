import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import initial.EquallyWeightedGradeSchema;
import initial.GradeSchema;

import static org.junit.Assert.assertEquals;

public class GradeSchemaTest {
  @Test
  public void testEquallyWeightedGradeSchema() {
    GradeSchema equallyWeightedGradeSchema = new EquallyWeightedGradeSchema();
    equallyWeightedGradeSchema.addGradeableItem("Assignment 1", 5);
    equallyWeightedGradeSchema.addGradeableItem("5 Group", 55);
    equallyWeightedGradeSchema.addGradeableItem("Exam 1", 15);
    assertEquals(5.0, equallyWeightedGradeSchema.getWeight("Assignment 1"), 0.0);
    assertEquals(11.0, equallyWeightedGradeSchema.getWeight("Group 1"), 0.0);
    assertEquals(11.0, equallyWeightedGradeSchema.getWeight("Group 2"), 0.0);
    assertEquals(11.0, equallyWeightedGradeSchema.getWeight("Group 3"), 0.0);
    assertEquals(11.0, equallyWeightedGradeSchema.getWeight("Group 4"), 0.0);
    assertEquals(11.0, equallyWeightedGradeSchema.getWeight("Group 5"), 0.0);
    assertEquals(15.0, equallyWeightedGradeSchema.getWeight("Exam 1"), 0.0);
    assertEquals(75.0, equallyWeightedGradeSchema.getSumOfWeights(), 0.0);
    Map<String, Double> scores = new HashMap<String,Double>();
    scores.put("Assignment 1", 50.0);
    scores.put("Group 1", 50.0);
    scores.put("Group 2", 50.0);
    scores.put("Group 3", 50.0);
    scores.put("Group 4", 50.0);
    scores.put("Group 5", 50.0);
    scores.put("Exam 1", 50.0);
    assertEquals(37.5, equallyWeightedGradeSchema.getWeightedTotal(scores), 0.0);
  }
}
