package test.javafx.scene.paint;
import java.util.Arrays;
import java.util.List;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class RadialGradientTest {
private final Color color1 = Color.rgb(0, 0, 0);
private final Color color2 = Color.rgb(255, 255, 255);
private final Stop stop1 = new Stop(0.1f, color1);
private final Stop stop2 = new Stop(0.2f, color2);
private final Stop[] noStop = new Stop[0];
private final Stop[] oneStop = new Stop[] { stop1 };
private final Stop[] twoStops = new Stop[] { stop1, stop2 };
private final Stop[] twoStopsWithNulls =
new Stop[] { stop1, null, stop2, null };
private final List<Stop> normalizedTwoStops = Arrays.asList(
new Stop(0.0, color1),
stop1, stop2,
new Stop(1.0, color2)
);
@Test
public void testRadialGradient() {
RadialGradient gradient = new RadialGradient(1f, 2f, 3f, 4f, 5f, true,
CycleMethod.REPEAT, twoStops);
assertEquals(1f, gradient.getFocusAngle(), 0.0001);
assertEquals(2f, gradient.getFocusDistance(), 0.0001);
assertEquals(3f, gradient.getCenterX(), 0.0001);
assertEquals(4f, gradient.getCenterY(), 0.0001);
assertEquals(5f, gradient.getRadius(), 0.0001);
assertTrue(gradient.isProportional());
assertEquals(CycleMethod.REPEAT, gradient.getCycleMethod());
assertEquals(normalizedTwoStops, gradient.getStops());
}
@Test
public void testGetStopsNullsRemoved() {
RadialGradient gradient = new RadialGradient(0, 0, 1, 1, 2, true,
CycleMethod.NO_CYCLE, twoStopsWithNulls);
assertEquals(normalizedTwoStops, gradient.getStops());
}
@Test(expected=UnsupportedOperationException.class)
public void testGetStopsCannotChangeGradient() {
RadialGradient gradient = new RadialGradient(0, 0, 1, 1, 2, true,
CycleMethod.NO_CYCLE, twoStops);
List<Stop> returned = gradient.getStops();
returned.set(0, stop2);
}
@Test
public void testEquals() {
RadialGradient basic = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient equal = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStopsWithNulls);
RadialGradient focusAngle = new RadialGradient(2, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient focusDistance = new RadialGradient(1, 1, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient centerX = new RadialGradient(1, 2, 4, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient centerY = new RadialGradient(1, 2, 3, 3, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient radius = new RadialGradient(1, 2, 3, 4, 6, true,
CycleMethod.REPEAT, twoStops);
RadialGradient proportional = new RadialGradient(1, 2, 3, 4, 5, false,
CycleMethod.REPEAT, twoStops);
RadialGradient cycleMethod = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REFLECT, twoStops);
RadialGradient stops = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, oneStop);
assertFalse(basic.equals(null));
assertFalse(basic.equals(color1));
assertFalse(basic.equals(color2));
assertTrue(basic.equals(basic));
assertTrue(basic.equals(equal));
assertFalse(basic.equals(focusAngle));
assertFalse(basic.equals(focusDistance));
assertFalse(basic.equals(centerX));
assertFalse(basic.equals(centerY));
assertFalse(basic.equals(radius));
assertFalse(basic.equals(proportional));
assertFalse(basic.equals(cycleMethod));
assertFalse(basic.equals(stops));
}
@Test
public void testHashCode() {
RadialGradient basic = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient equal = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
RadialGradient different = new RadialGradient(1, 2, 3, 4, 5, false,
CycleMethod.REPEAT, twoStops);
RadialGradient different2 = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, oneStop);
int code = basic.hashCode();
int second = basic.hashCode();
assertTrue(code == second);
assertTrue(code == equal.hashCode());
assertFalse(code == different.hashCode());
assertFalse(code == different2.hashCode());
}
@Test
public void testToString() {
RadialGradient empty = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, noStop);
RadialGradient nonempty = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, twoStops);
String s = empty.toString();
assertNotNull(s);
assertFalse(s.isEmpty());
s = nonempty.toString();
assertNotNull(s);
assertFalse(s.isEmpty());
}
@Test
public void testToStringEquals() {
RadialGradient rg =
RadialGradient.valueOf("radial-gradient(radius 100%, red  0% , blue 30%,  black 100%)");
assertEquals(rg, RadialGradient.valueOf(rg.toString()));
rg = RadialGradient.valueOf("radial-gradient(center 10px 10, radius 100, red 0px, blue 50px,  black 100px)");
assertEquals(rg, RadialGradient.valueOf(rg.toString()));
rg = RadialGradient.valueOf("radial-gradient(radius 10%, red  0%, blue 30%, black 100%)");
assertEquals(rg, RadialGradient.valueOf(rg.toString()));
rg = RadialGradient.valueOf("radial-gradient(focus-angle 3.1415926535rad, radius 10%, red  0%, blue 30%, black 100%)");
assertEquals(rg, RadialGradient.valueOf(rg.toString()));
}
@Test
public void testImpl_getPlatformPaint() {
RadialGradient gradient = new RadialGradient(1, 2, 3, 4, 5, true,
CycleMethod.REPEAT, noStop);
Object paint = Toolkit.getPaintAccessor().getPlatformPaint(gradient);
assertNotNull(paint);
assertSame(paint, Toolkit.getPaintAccessor().getPlatformPaint(gradient));
}
@Test
public void testBuilder() {
RadialGradient gradient = new RadialGradient(
23, 24, 2.0, 3.0, 17,
false, CycleMethod.REPEAT,
new Stop(0, Color.RED),
new Stop(1, Color.BLUE));
assertEquals(2.0, gradient.getCenterX(), 0);
assertEquals(3.0, gradient.getCenterY(), 0);
assertSame(CycleMethod.REPEAT, gradient.getCycleMethod());
assertEquals(23.0, gradient.getFocusAngle(), 0);
assertEquals(24.0, gradient.getFocusDistance(), 0);
assertFalse(gradient.isProportional());
assertEquals(17.0, gradient.getRadius(), 0);
assertEquals(2, gradient.getStops().size());
assertSame(Color.BLUE, gradient.getStops().get(1).getColor());
assertEquals(1.0, gradient.getStops().get(1).getOffset(), 0);
}
@Test(expected=NullPointerException.class)
public void testValueOfNullValue() {
RadialGradient.valueOf(null);
}
@Test(expected=IllegalArgumentException.class)
public void testValueOfEmpty() {
RadialGradient.valueOf("");
}
@Test
public void testValueOfIllegalNotations() {
try {
RadialGradient.valueOf("abcd");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient()");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(red 30%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(radius 10, rgb(0,0,250), rgb(250, 0, 0)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(red 30%,,black 100%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(red 30%,black 100%,)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(,radius 100%, red  0% , blue 30%,  black 100%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(radius 100%,,repeat, red  0% , blue 30%,  black 100%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
}
@Test
public void testValueOfRelativeAbsoluteMixed() {
try {
RadialGradient.valueOf("radial-gradient(center 0% 100%, radius 10px, red  0% , blue 30%,  black 100%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
try {
RadialGradient.valueOf("radial-gradient(center 0 100, radius 10%, red  0% , blue 30%,  black 100%)");
fail("IllegalArgument should have been thrown.");
} catch (IllegalArgumentException iae) {
}
}
@Test
public void testValueOfRelativeValues() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(radius 100%, red  0% , blue 30%,  black 100%)");
RadialGradient expected = new RadialGradient(
0, 0, 0, 0, 1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfAbsoluteValues() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(center 10px 10, radius 100, red 0px, blue 50px,  black 100px)");
RadialGradient expected = new RadialGradient(
0, 0, 10, 10, 100,
false, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.5, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfDefaults() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(radius 10%, red  0%, blue 30%, black 100%)");
RadialGradient expected = new RadialGradient(
0, 0, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfCenter() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(center 20% 40%, radius 10%, red  0%, blue 30%, black 100%)");
RadialGradient expected = new RadialGradient(
0, 0, .2, .4, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfFocusAngle() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(focus-angle 45deg, radius 10%, red  0%, blue 30%, black 100%)");
RadialGradient expected = new RadialGradient(
45, 0, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
actual =
RadialGradient.valueOf("radial-gradient(focus-angle 3.1415926535rad, radius 10%, red  0%, blue 30%, black 100%)");
expected = new RadialGradient(
180, 0, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected.getFocusAngle(), actual.getFocusAngle(), 1e-5);
actual =
RadialGradient.valueOf("radial-gradient(focus-angle 0.5turn, radius 10%, red  0%, blue 30%, black 100%)");
expected = new RadialGradient(
180, 0, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
actual =
RadialGradient.valueOf("radial-gradient(focus-angle 1grad, radius 10%, red  0%, blue 30%, black 100%)");
expected = new RadialGradient(
.9, 0, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfFocusDistance() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(focus-distance 20%, radius 10%, red  0%, blue 30%, black 100%)");
RadialGradient expected = new RadialGradient(
0, .2, 0, 0, .1,
true, CycleMethod.NO_CYCLE,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfCycleMethod() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(radius 10%, repeat, red 0%, blue 30%, black 100%)");
RadialGradient expected = new RadialGradient(
0, 0, 0, 0, .1,
true, CycleMethod.REPEAT,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
actual =
RadialGradient.valueOf("radial-gradient(radius 10%, reflect, red 0%, blue 30%, black 100%)");
expected = new RadialGradient(
0, 0, 0, 0, .1,
true, CycleMethod.REFLECT,
new Stop(0, Color.RED),
new Stop(.3, Color.BLUE),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfSpecifyAll() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(focus-angle 45deg, focus-distance 20%, center 25% 25%, radius 50%, reflect, gray, darkgray 75%, dimgray)");
RadialGradient expected = new RadialGradient(
45, .2, .25, .25, .5,
true, CycleMethod.REFLECT,
new Stop(0, Color.GRAY),
new Stop(.75, Color.DARKGRAY),
new Stop(1.0, Color.DIMGRAY));
assertEquals(expected, actual);
}
@Test
public void testValueOfStopsNormalizeLargerValues() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(radius 10, red  10%, blue 9%, red 8%, black 100%)");
RadialGradient expected = new RadialGradient(
0, 0, 0, 0, 10,
false, CycleMethod.NO_CYCLE,
new Stop(0.1, Color.RED),
new Stop(0.1, Color.BLUE),
new Stop(0.1, Color.RED),
new Stop(1.0, Color.BLACK));
assertEquals(expected, actual);
}
@Test
public void testValueOfColor() {
RadialGradient actual =
RadialGradient.valueOf("radial-gradient(radius 10, rgb(0,0,255), rgb(255, 0,  0))");
RadialGradient expected = new RadialGradient(
0, 0, 0, 0, 10,
false, CycleMethod.NO_CYCLE,
new Stop(0, Color.BLUE),
new Stop(1.0, Color.RED));
assertEquals(expected, actual);
}
@Test
public void testCycleMethodCSSStyle() {
Region region = new Region();
Scene scene = new Scene(region);
RadialGradient rGradient;
region.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg,"
+ " focus-distance 20%, center 25% 25%, radius 50%, reflect, red 25%, black 75%);");
region.applyCss();
rGradient = (RadialGradient) region.backgroundProperty().get().getFills().get(0).getFill();
assertEquals(CycleMethod.REFLECT, rGradient.getCycleMethod());
region.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg,"
+ " focus-distance 20%, center 25% 25%, radius 50%, repeat, red 25%, black 75%);");
region.applyCss();
rGradient = (RadialGradient) region.backgroundProperty().get().getFills().get(0).getFill();
assertEquals(CycleMethod.REPEAT, rGradient.getCycleMethod());
}
}
