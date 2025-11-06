import java.util.ArrayList;
import java.util.List;

/**
 * Represent a pill add monitor.
 */
public class PillAddMonitor extends PillCounterDecorator {
  private int currentAddCalls = 0;
  private final List<Integer> addCounts = new ArrayList<>();

  public PillAddMonitor(PillCounter delegate) {
    super(delegate);
  }

  @Override
  public void addPill(int count) {
    currentAddCalls++;
    super.addPill(count);
  }

  @Override
  public void reset() {
    // save the count for this “bottle”
    addCounts.add(currentAddCalls);
    currentAddCalls = 0;
    super.reset();
  }

  /**
   * Get the add counts.
   *
   * @return a list of the number of addPill(...) calls that happened between each reset
   */
  public List<Integer> getAddCounts() {
    return new ArrayList<>(addCounts);
  }
}
