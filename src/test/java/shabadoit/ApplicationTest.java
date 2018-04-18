package shabadoit;

import shabadoit.controller.HomeController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    @Test
    public void testApp() {
        HomeController hc = new HomeController();
        String result = hc.home();
        assertEquals(result, "Nothing to see here yet");
    }
}
