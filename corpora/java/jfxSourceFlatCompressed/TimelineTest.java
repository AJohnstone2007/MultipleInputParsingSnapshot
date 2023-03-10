package test.javafx.animation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import com.sun.javafx.tk.Toolkit;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.junit.Before;
import org.junit.Test;
public class TimelineTest {
private static final double DEFAULT_RATE = 1.0;
private static final int DEFAULT_REPEAT_COUNT = 1;
private static final boolean DEFAULT_AUTO_REVERSE = false;
private static final double EPSILON = 1e-12;
private Timeline timeline;
@Before
public void setUp() {
timeline = new Timeline();
}
@Test
public void testDefaultValues() {
final Duration oneSec = Duration.millis(1000);
final KeyFrame kf0 = new KeyFrame(Duration.ZERO);
final KeyFrame kf1 = new KeyFrame(oneSec, "oneSec");
final Timeline timeline0 = new Timeline();
assertTrue(timeline0.getKeyFrames().isEmpty());
assertEquals(DEFAULT_RATE, timeline0.getRate(), EPSILON);
assertEquals(0.0, timeline0.getCurrentRate(), EPSILON);
assertEquals(Duration.ZERO, timeline0.getCycleDuration());
assertEquals(Duration.ZERO, timeline0.getTotalDuration());
assertEquals(Duration.ZERO, timeline0.getCurrentTime());
assertEquals(DEFAULT_REPEAT_COUNT, timeline0.getCycleCount());
assertEquals(DEFAULT_AUTO_REVERSE, timeline0.isAutoReverse());
assertEquals(Status.STOPPED, timeline0.getStatus());
assertEquals(6000.0 / Toolkit.getToolkit().getPrimaryTimer().getDefaultResolution(), timeline0.getTargetFramerate(), EPSILON);
assertEquals(null, timeline0.getOnFinished());
assertTrue(timeline0.getCuePoints().isEmpty());
final Timeline timeline1 = new Timeline(42.0);
assertTrue(timeline1.getKeyFrames().isEmpty());
assertEquals(DEFAULT_RATE, timeline1.getRate(), EPSILON);
assertEquals(0.0, timeline1.getCurrentRate(), EPSILON);
assertEquals(Duration.ZERO, timeline1.getCycleDuration());
assertEquals(Duration.ZERO, timeline1.getTotalDuration());
assertEquals(Duration.ZERO, timeline1.getCurrentTime());
assertEquals(DEFAULT_REPEAT_COUNT, timeline1.getCycleCount());
assertEquals(DEFAULT_AUTO_REVERSE, timeline1.isAutoReverse());
assertEquals(Status.STOPPED, timeline1.getStatus());
assertEquals(42.0, timeline1.getTargetFramerate(), EPSILON);
assertEquals(null, timeline1.getOnFinished());
assertTrue(timeline1.getCuePoints().isEmpty());
final Timeline timeline2 = new Timeline(kf0, kf1);
assertEquals(Arrays.asList(kf0, kf1), timeline2.getKeyFrames());
assertEquals(DEFAULT_RATE, timeline2.getRate(), EPSILON);
assertEquals(0.0, timeline2.getCurrentRate(), EPSILON);
assertEquals(oneSec, timeline2.getCycleDuration());
assertEquals(oneSec, timeline2.getTotalDuration());
assertEquals(Duration.ZERO, timeline2.getCurrentTime());
assertEquals(DEFAULT_REPEAT_COUNT, timeline2.getCycleCount());
assertEquals(DEFAULT_AUTO_REVERSE, timeline2.isAutoReverse());
assertEquals(Status.STOPPED, timeline2.getStatus());
assertEquals(6000.0 / Toolkit.getToolkit().getPrimaryTimer().getDefaultResolution(), timeline2.getTargetFramerate(), EPSILON);
assertEquals(null, timeline2.getOnFinished());
assertEquals(Collections.singletonMap("oneSec", oneSec), timeline2.getCuePoints());
final Timeline timeline3 = new Timeline(42.0, kf0, kf1);
assertEquals(Arrays.asList(kf0, kf1), timeline3.getKeyFrames());
assertEquals(DEFAULT_RATE, timeline3.getRate(), EPSILON);
assertEquals(0.0, timeline3.getCurrentRate(), EPSILON);
assertEquals(oneSec, timeline3.getCycleDuration());
assertEquals(oneSec, timeline3.getTotalDuration());
assertEquals(Duration.ZERO, timeline3.getCurrentTime());
assertEquals(DEFAULT_REPEAT_COUNT, timeline3.getCycleCount());
assertEquals(DEFAULT_AUTO_REVERSE, timeline3.isAutoReverse());
assertEquals(Status.STOPPED, timeline3.getStatus());
assertEquals(42.0, timeline1.getTargetFramerate(), EPSILON);
assertEquals(null, timeline3.getOnFinished());
assertEquals(Collections.singletonMap("oneSec", oneSec), timeline2.getCuePoints());
}
@Test
public void testKeyFrames() {
final Duration oneSec = Duration.millis(1000);
final KeyFrame kf0 = new KeyFrame(Duration.ZERO);
final KeyFrame kf1 = new KeyFrame(oneSec, "oneSec");
timeline.getKeyFrames().add(kf1);
assertEquals(Collections.singletonList(kf1), timeline.getKeyFrames());
assertEquals(Collections.singletonMap("oneSec", oneSec), timeline.getCuePoints());
assertEquals(oneSec, timeline.getCycleDuration());
timeline.getKeyFrames().clear();
assertTrue(timeline.getKeyFrames().isEmpty());
assertEquals(Collections.emptyMap(), timeline.getCuePoints());
assertEquals(Duration.ZERO, timeline.getCycleDuration());
timeline.getKeyFrames().addAll(kf1, kf0);
assertEquals(Arrays.asList(kf1, kf0), timeline.getKeyFrames());
assertEquals(Collections.singletonMap("oneSec", oneSec), timeline.getCuePoints());
assertEquals(oneSec, timeline.getCycleDuration());
timeline.getKeyFrames().remove(kf1);
assertEquals(Collections.singletonList(kf0), timeline.getKeyFrames());
assertEquals(Collections.emptyMap(), timeline.getCuePoints());
assertEquals(Duration.ZERO, timeline.getCycleDuration());
}
}
