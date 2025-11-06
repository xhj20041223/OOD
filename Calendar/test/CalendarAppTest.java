import org.junit.Test;

public class CalendarAppTest {
    @Test
    public void testAppStart() {
        try {
            CalendarApp.main(new String[]{});
        } catch (Exception e) {
            throw new AssertionError("App failed to start: " + e.getMessage(), e);
        }
    }
}
