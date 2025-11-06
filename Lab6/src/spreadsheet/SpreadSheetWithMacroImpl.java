package spreadsheet;

/**
 * Implementation of SpreadSheetWithMacro by creating its own underlying SparseSpreadSheet.
 */
public class SpreadSheetWithMacroImpl implements SpreadSheetWithMacro {
  private final SpreadSheet delegate;

  /**
   * No-argument constructor. Internally creates a SparseSpreadSheet as the delegate.
   */
  public SpreadSheetWithMacroImpl() {
    this.delegate = new SparseSpreadSheet();
  }

  @Override
  public void set(int row, int col, double value) {
    delegate.set(row, col, value);
  }

  @Override
  public double get(int row, int col) {
    return delegate.get(row, col);
  }

  @Override
  public boolean isEmpty(int row, int col) {
    return delegate.isEmpty(row, col);
  }

  @Override
  public int getWidth() {
    return delegate.getWidth();
  }

  @Override
  public int getHeight() {
    return delegate.getHeight();
  }

  @Override
  public void execute(SpreadSheetMacro m) {
    if (m == null) {
      throw new IllegalArgumentException("Macro cannot be null");
    }
    m.execute(this);
  }
}
