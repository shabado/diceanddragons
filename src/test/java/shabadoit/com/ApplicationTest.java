package shabadoit.com;

import shabadoit.com.controller.HomeContoller;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    @Test
    public void testApp() {
        HomeContoller hc = new HomeContoller();
        String result = hc.home();
        assertEquals(result, "Nothing to see here yet");
    }
}
