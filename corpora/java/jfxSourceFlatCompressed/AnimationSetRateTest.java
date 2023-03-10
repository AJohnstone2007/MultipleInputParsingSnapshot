package test.javafx.animation;
import javafx.animation.Animation.Status;
import javafx.util.Duration;
import test.com.sun.scenario.animation.shared.ClipEnvelopeMock;
import org.junit.Before;
import org.junit.Test;
import com.sun.scenario.animation.shared.SingleLoopClipEnvelopeShim;
import static org.junit.Assert.*;
public class AnimationSetRateTest {
private static final double EPSILON = 1e-12;
private AbstractPrimaryTimerMock timer;
private AnimationImpl animation;
private ClipEnvelopeMock clipEnvelope;
@Before
public void setUp() throws Exception {
timer = new AbstractPrimaryTimerMock();
clipEnvelope = new ClipEnvelopeMock();
animation = new AnimationImpl(timer, clipEnvelope, 1);
animation.shim_setCycleDuration(Duration.millis(1000));
clipEnvelope.setAnimation(animation);
}
private void assertAnimation(double rate, double currentRate, Status status, boolean addedToPrimaryTimer) {
assertEquals(rate, animation.getRate(), EPSILON);
assertEquals(currentRate, animation.getCurrentRate(), EPSILON);
assertEquals(status, animation.getStatus());
assertEquals(addedToPrimaryTimer, timer.containsPulseReceiver(animation.shim_pulseReceiver()));
}
@Test
public void testSetRate() {
animation.play();
animation.setRate(3.0);
assertAnimation(3.0, 3.0, Status.RUNNING, true);
animation.setRate(-2.0);
assertAnimation(-2.0, -2.0, Status.RUNNING, true);
animation.setRate(-2.5);
assertAnimation(-2.5, -2.5, Status.RUNNING, true);
animation.setRate(1.5);
assertAnimation(1.5, 1.5, Status.RUNNING, true);
animation.setCurrentRate(-1.5);
animation.setRate(2.2);
assertAnimation(2.2, -2.2, Status.RUNNING, true);
animation.setRate(-1.8);
assertAnimation(-1.8, 1.8, Status.RUNNING, true);
animation.setRate(-1.3);
assertAnimation(-1.3, 1.3, Status.RUNNING, true);
animation.setRate(0.5);
assertAnimation(0.5, -0.5, Status.RUNNING, true);
}
@Test
public void testSetRateOfStoppedAnimation() {
animation.setRate(2.0);
assertAnimation(2.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(2.0, 2.0, Status.RUNNING, true);
animation.stop();
animation.setRate(-1.0);
assertAnimation(-1.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(-1.0, -1.0, Status.RUNNING, true);
animation.stop();
animation.setRate(3.0);
assertAnimation(3.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(3.0, 3.0, Status.RUNNING, true);
animation.stop();
animation.setRate(0);
assertAnimation(0.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.stop();
animation.setRate(1.5);
assertAnimation(1.5, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(1.5, 1.5, Status.RUNNING, true);
animation.stop();
animation.setRate(0);
assertAnimation(0.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.stop();
animation.setRate(-0.5);
assertAnimation(-0.5, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(-0.5, -0.5, Status.RUNNING, true);
animation.stop();
animation.setRate(0);
assertAnimation(0.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.stop();
animation.setRate(-2.3);
assertAnimation(-2.3, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(-2.3, -2.3, Status.RUNNING, true);
animation.stop();
animation.setRate(0);
assertAnimation(0.0, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.stop();
animation.setRate(1.7);
assertAnimation(1.7, 0.0, Status.STOPPED, false);
animation.play();
assertAnimation(1.7, 1.7, Status.RUNNING, true);
}
@Test
public void testSetRateToZeroForRunningAnimation() {
animation.play();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(3.0);
assertAnimation(3.0, 3.0, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(-2.0);
assertAnimation(-2.0, -2.0, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(-2.5);
assertAnimation(-2.5, -2.5, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(1.5);
assertAnimation(1.5, 1.5, Status.RUNNING, true);
animation.setCurrentRate(-1.5);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(2.2);
assertAnimation(2.2, -2.2, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(-1.8);
assertAnimation(-1.8, 1.8, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(-1.3);
assertAnimation(-1.3, 1.3, Status.RUNNING, true);
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.setRate(0.5);
assertAnimation(0.5, -0.5, Status.RUNNING, true);
}
@Test
public void testSetRateOfPausedAnimation() {
animation.play();
animation.pause();
animation.setRate(3.0);
assertAnimation(3.0, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(3.0, 3.0, Status.RUNNING, true);
animation.pause();
animation.setRate(-2.0);
assertAnimation(-2.0, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-2.0, -2.0, Status.RUNNING, true);
animation.pause();
animation.setRate(-2.5);
assertAnimation(-2.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-2.5, -2.5, Status.RUNNING, true);
animation.pause();
animation.setRate(1.5);
assertAnimation(1.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(1.5, 1.5, Status.RUNNING, true);
animation.setCurrentRate(-1.5);
animation.pause();
animation.setRate(2.2);
assertAnimation(2.2, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(2.2, -2.2, Status.RUNNING, true);
animation.pause();
animation.setRate(-1.8);
assertAnimation(-1.8, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-1.8, 1.8, Status.RUNNING, true);
animation.pause();
animation.setRate(-1.3);
assertAnimation(-1.3, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-1.3, 1.3, Status.RUNNING, true);
animation.pause();
animation.setRate(0.5);
assertAnimation(0.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(0.5, -0.5, Status.RUNNING, true);
}
@Test
public void testSetRateToZeroForPausedAnimation() {
animation.play();
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(0.0, 0.0, Status.RUNNING, false);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(3.0);
assertAnimation(3.0, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(3.0, 3.0, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(-2.0);
assertAnimation(-2.0, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-2.0, -2.0, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(-2.5);
assertAnimation(-2.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-2.5, -2.5, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(1.5);
assertAnimation(1.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(1.5, 1.5, Status.RUNNING, true);
animation.setCurrentRate(-1.5);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(2.2);
assertAnimation(2.2, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(2.2, -2.2, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(-1.8);
assertAnimation(-1.8, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-1.8, 1.8, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(-1.3);
assertAnimation(-1.3, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(-1.3, 1.3, Status.RUNNING, true);
animation.pause();
animation.setRate(0.0);
assertAnimation(0.0, 0.0, Status.PAUSED, false);
animation.setRate(0.5);
assertAnimation(0.5, 0.0, Status.PAUSED, false);
animation.play();
assertAnimation(0.5, -0.5, Status.RUNNING, true);
}
@Test
public void testFlipRateAndPlayForPausedNonEmbeddedAnimation() {
var clip = new SingleLoopClipEnvelopeShim(animation);
animation.setClipEnvelope(clip);
animation.setRate(0.2);
animation.play();
clip.timePulse(10);
animation.pause();
long timeBefore = clip.getTicks();
animation.setRate(-0.2);
animation.play();
clip.timePulse(5);
animation.pause();
long timeAfter = clip.getTicks();
assertEquals("A pulse to 10 at rate 0.2 with deltaTicks = 0 should reach 10 * 0.2 = 2", 2, timeBefore);
assertEquals("A pulse to 5 at rate -0.2 with deltaTicks = 4 should reach 4 + 5 * (-0.2) = 3", 3, timeAfter);
}
}
