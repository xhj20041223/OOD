/**
 * Represent a pill batch add monitor.
 */
public class PillBatchAddCounter extends PillCounterDecorator {
  private int pendingPills = 0;

  public PillBatchAddCounter(PillCounter delegate) {
    super(delegate);
  }

  @Override
  public void addPill(int count) {
    // accumulate locally, don’t forward immediately
    pendingPills += count;
  }

  @Override
  public int getPillCount() {
    // flush once before retrieving
    if (pendingPills > 0) {
      super.addPill(pendingPills);
      pendingPills = 0;
    }
    return super.getPillCount();
  }

  @Override
  public void reset() {
    // flush any leftovers before resetting
    if (pendingPills > 0) {
      super.addPill(pendingPills);
      pendingPills = 0;
    }
    super.reset();
  }
}
