package test.javafx.scene.layout;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.junit.Test;
import static org.junit.Assert.*;
public class BorderStrokeStyleTest {
@Test public void instanceCreation() {
BorderStrokeStyle style = new BorderStrokeStyle(null, null, null, 1, 2, null);
assertEquals(StrokeType.CENTERED, style.getType());
assertEquals(StrokeLineJoin.MITER, style.getLineJoin());
assertEquals(StrokeLineCap.BUTT, style.getLineCap());
assertEquals(1, style.getMiterLimit(), 0);
assertEquals(2, style.getDashOffset(), 0);
assertEquals(0, style.getDashArray().size());
}
@Test public void instanceCreation2() {
List<Double> dashArray = new ArrayList<Double>();
dashArray.add(1.0);
dashArray.add(4.0);
BorderStrokeStyle style = new BorderStrokeStyle(StrokeType.OUTSIDE, StrokeLineJoin.BEVEL, StrokeLineCap.SQUARE,
10, 0, dashArray);
assertEquals(StrokeType.OUTSIDE, style.getType());
assertEquals(StrokeLineJoin.BEVEL, style.getLineJoin());
assertEquals(StrokeLineCap.SQUARE, style.getLineCap());
assertEquals(10, style.getMiterLimit(), 0);
assertEquals(0, style.getDashOffset(), 0);
assertEquals(2, style.getDashArray().size());
assertEquals(1.0, style.getDashArray().get(0), 0);
assertEquals(4.0, style.getDashArray().get(1), 0);
}
@Test(expected = UnsupportedOperationException.class)
public void dashArrayIsImmutable() {
BorderStrokeStyle style = new BorderStrokeStyle(null, null, null, 10, 2, null);
style.getDashArray().add(1.0);
}
@Test public void changesToDashArrayPassedToConstructorHaveNoEffect() {
List<Double> dashArray = new ArrayList<Double>();
BorderStrokeStyle style = new BorderStrokeStyle(null, null, null, 1, 2, dashArray);
dashArray.add(4.0);
assertEquals(0, style.getDashArray().size());
}
@Test public void identity() {
BorderStrokeStyle style = new BorderStrokeStyle(null, null, null, 10, 2, null);
assertEquals(style, style);
assertEquals(style.hashCode(), style.hashCode());
}
@Test public void equality() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 2, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, null, 10, 2, null);
assertEquals(a, b);
assertEquals(a.hashCode(), b.hashCode());
}
@Test public void equality2() {
BorderStrokeStyle a = new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 2, null);
BorderStrokeStyle b = new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 2, null);
assertEquals(a, b);
assertEquals(a.hashCode(), b.hashCode());
}
@Test public void equality3() {
BorderStrokeStyle a = new BorderStrokeStyle(null, StrokeLineJoin.ROUND, null, 10, 2, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, StrokeLineJoin.ROUND, null, 10, 2, null);
assertEquals(a, b);
assertEquals(a.hashCode(), b.hashCode());
}
@Test public void equality4() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, StrokeLineCap.ROUND, 10, 2, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, StrokeLineCap.ROUND, 10, 2, null);
assertEquals(a, b);
assertEquals(a.hashCode(), b.hashCode());
}
@Test public void equality5() {
List<Double> dashArray1 = new ArrayList<Double>();
dashArray1.add(1.0);
dashArray1.add(4.0);
List<Double> dashArray2 = new ArrayList<Double>();
dashArray2.add(1.0);
dashArray2.add(4.0);
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 2, dashArray1);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, null, 10, 2, dashArray2);
assertEquals(a, b);
assertEquals(a.hashCode(), b.hashCode());
}
@Test public void notEqual() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null);
assertFalse(a.equals(b));
}
@Test public void notEqual2() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, StrokeLineJoin.ROUND, null, 10, 0, null);
assertFalse(a.equals(b));
}
@Test public void notEqual3() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, StrokeLineCap.ROUND, 10, 0, null);
assertFalse(a.equals(b));
}
@Test public void notEqual4() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, null, 20, 0, null);
assertFalse(a.equals(b));
}
@Test public void notEqual5() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, null, 10, 1, null);
assertFalse(a.equals(b));
}
@Test public void notEqual6() {
List<Double> dashArray1 = new ArrayList<Double>();
dashArray1.add(1.0);
dashArray1.add(4.0);
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
BorderStrokeStyle b = new BorderStrokeStyle(null, null, null, 10, 0, dashArray1);
assertFalse(a.equals(b));
}
@Test public void notEqualWithNull() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
assertFalse(a.equals(null));
}
@Test public void notEqualWithRandom() {
BorderStrokeStyle a = new BorderStrokeStyle(null, null, null, 10, 0, null);
assertFalse(a.equals("Some random string"));
}
}
