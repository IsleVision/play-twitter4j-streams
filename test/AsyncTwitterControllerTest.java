import controllers.AssetsFinder;
import controllers.AsyncTwitterController;
import models.WordsFrequency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import play.test.WithApplication;
import services.WordsFrequencyService;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static play.test.Helpers.contentAsString;

/**
 * @author
 * @date 2020-11-08
 */
@RunWith(MockitoJUnitRunner.class)
public class AsyncTwitterControllerTest extends WithApplication {

    @Mock
    WordsFrequencyService wordsFrequencyService;

    private AsyncTwitterController asyncTwitterController;

    @Before
    public void setUp() {
        // commonPool here is just for test.
        HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
        AssetsFinder assetsFinder = app.injector().instanceOf(AssetsFinder.class);
        asyncTwitterController = new AsyncTwitterController(assetsFinder, ec,wordsFrequencyService);
    }

    @Test
    public void testFrequency() throws ExecutionException, InterruptedException {

        Stream<WordsFrequency> mockedStream = Arrays.stream(new WordsFrequency[]{
                new WordsFrequency("java", (long) 3),
                new WordsFrequency("play", (long) 2),
                new WordsFrequency("sbt", (long) 1)
        });
        when(wordsFrequencyService.countFrequency("java")).thenReturn(mockedStream);

        CompletableFuture<Result> stage = (CompletableFuture<Result>) asyncTwitterController.frequency("java");

        assertThat(stage.get())
                .matches(r -> contentAsString(r).contains("<td style=\"text-align: center\">java</td><td style=\"text-align: center\">3</td>"))
                .matches(r -> contentAsString(r).contains("<td style=\"text-align: center\">play</td><td style=\"text-align: center\">2</td>"))
                .matches(r -> contentAsString(r).contains("<td style=\"text-align: center\">sbt</td><td style=\"text-align: center\">1</td>"));

    }
}
