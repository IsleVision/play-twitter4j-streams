import controllers.AsyncTwitterController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.test.Helpers;
import play.test.TestBrowser;
import play.test.WithBrowser;
import services.WordsFrequencyService;

import static org.junit.Assert.assertTrue;

/**
 * @author
 * a thorough test covering the http process and controller
 */

@RunWith(MockitoJUnitRunner.class)
public class BrowserTest extends WithBrowser {

    @Mock
    WordsFrequencyService wordsFrequencyService;


    @InjectMocks
    AsyncTwitterController asyncTwitterController;
    
    protected TestBrowser provideBrowser(int port) {
        return Helpers.testBrowser(port);
    }

    @Test
    public void testIndex() {
        browser.goTo("http://localhost:" + play.api.test.Helpers.testServerPort());
        assertTrue(browser.pageSource().contains("Welcome To Tweet Analytics"));
    }

}
