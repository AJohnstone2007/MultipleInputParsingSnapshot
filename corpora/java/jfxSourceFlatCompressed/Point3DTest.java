package test.javafx.geometry;
import javafx.geometry.Point3D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
public class Point3DTest {
@Test
public void testDistance() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(1, 0, 0);
Point3D p3 = new Point3D(1, 1, 1);
assertEquals(1, p2.distance(p1), 1e-100);
assertEquals(1, p2.distance(0, 0, 0), 1e-100);
assertEquals(1.41421356, p2.distance(p3), 1e-5);
assertEquals(1.7320508, p1.distance(p3), 1e-5);
assertEquals(1.7320508, p1.distance(1, 1, 1), 1e-5);
}
@Test
public void testEquals() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 1, 0);
Point3D p3 = new Point3D(1, 0, 1);
Point3D p4 = new Point3D(1, 0, 0);
assertTrue(p1.equals(p1));
assertTrue(p1.equals(new Point3D(0, 0, 0)));
assertFalse(p1.equals(new Object()));
assertFalse(p1.equals(p2));
assertFalse(p1.equals(p3));
assertFalse(p3.equals(p4));
}
@Test
public void testHash() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 1, 0);
Point3D p3 = new Point3D(0, 1, 0);
assertEquals(p3.hashCode(), p2.hashCode());
assertFalse(p1.hashCode() == p2.hashCode());
}
@Test
public void testAdd() {
Point3D p1 = new Point3D(2, 4, 8);
Point3D p2 = new Point3D(-1, 3, 5);
assertEquals(new Point3D(1, 7, 13), p1.add(p2));
assertEquals(new Point3D(1, 7, 13), p1.add(-1, 3, 5));
}
@Test(expected=NullPointerException.class)
public void testAddNull() {
Point3D point = new Point3D(1, 2, 3);
point.add(null);
}
@Test
public void testSubtract() {
Point3D p1 = new Point3D(2, 4, 8);
Point3D p2 = new Point3D(-1, 3, 6);
assertEquals(new Point3D(3, 1, 2), p1.subtract(p2));
assertEquals(new Point3D(3, 1, 2), p1.subtract(-1, 3, 6));
}
@Test(expected=NullPointerException.class)
public void testSubtractNull() {
Point3D point = new Point3D(1, 2, 3);
point.subtract(null);
}
@Test
public void testMultiplyByNumber() {
Point3D p1 = new Point3D(2, 4, 8);
Point3D p2 = new Point3D(-1, 3, 6);
assertEquals(new Point3D(4, 8, 16), p1.multiply(2));
assertEquals(new Point3D(1, -3, -6), p2.multiply(-1));
assertEquals(new Point3D(0, 0, 0), p1.multiply(0));
}
@Test
public void testNormalize() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 0, 1);
Point3D p3 = new Point3D(1, 1, 1);
Point3D p4 = new Point3D(120, -350, 430);
double sqrt3 = Math.sqrt(3);
double sqrt4 = Math.sqrt(321800);
assertEquals(new Point3D(0, 0, 0), p1.normalize());
assertEquals(new Point3D(0, 0, 1), p2.normalize());
assertEquals(new Point3D(1 / sqrt3, 1 / sqrt3, 1 /sqrt3), p3.normalize());
assertEquals(new Point3D(120 / sqrt4, -350 / sqrt4, 430 / sqrt4), p4.normalize());
}
@Test
public void testMidpoint() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(1, -2, 3);
assertEquals(new Point3D(0.5, -1, 1.5), p1.midpoint(p2));
assertEquals(new Point3D(0.5, -1, 1.5), p1.midpoint(1, -2, 3));
}
@Test(expected=NullPointerException.class)
public void testMidpointNull() {
Point3D point = new Point3D(1, 2, 3);
point.midpoint(null);
}
@Test
public void testVectorAngle() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 0, 1);
Point3D p3 = new Point3D(0, 1, 1);
Point3D p4 = new Point3D(-1, 0, 1);
Point3D p5 = new Point3D(0, 1, 1);
assertEquals(Double.NaN, p1.angle(p2), 0.000001);
assertEquals(0, p3.angle(p3), 0.000001);
assertEquals(0, p3.angle(p5), 0.000001);
assertEquals(45, p2.angle(p3), 0.000001);
assertEquals(45, p2.angle(p4), 0.000001);
assertEquals(135, p2.angle(-1, 0, -1), 0.000001);
}
@Test(expected=NullPointerException.class)
public void testVectorAngleNull() {
Point3D point = new Point3D(1, 2, 3);
point.angle(null);
}
@Test
public void testVectorAngleTooClose() {
Point3D p1 = new Point3D(-0.8944271909999159, 0.0, 0.4472135954999579);
Point3D p2 = new Point3D(-0.894427190999924, 4.061090000458082E-14, 0.4472135954999417);
assertEquals(0.0, p1.angle(p2), 0.000001);
assertEquals(0.0, p2.angle(p1), 0.000001);
}
@Test
public void testVectorAngleTooOpposite() {
Point3D p1 = new Point3D(-0.8944271909999159, 0.0, 0.4472135954999579);
Point3D p2 = new Point3D(0.894427190999924, -4.061090000458082E-14, -0.4472135954999417);
assertEquals(180.0, p1.angle(p2), 0.000001);
assertEquals(180.0, p2.angle(p1), 0.000001);
}
@Test
public void testPointAngle() {
Point3D p1 = new Point3D(2, 2, 2);
Point3D p2 = new Point3D(0, 2, 0);
Point3D p3 = new Point3D(-3, 2, -3);
Point3D p4 = new Point3D(-7, 2, -7);
Point3D p5 = new Point3D(-3, 4, -3);
Point3D p6 = new Point3D(-3, 2, -10);
assertEquals(180, p2.angle(p1, p3), 0.000001);
assertEquals(90, p3.angle(p5, p6), 0.000001);
assertEquals(0, p2.angle(p3, p4), 0.000001);
assertEquals(Double.NaN, p2.angle(p2, p4), 0.000001);
}
@Test(expected=NullPointerException.class)
public void testPointAngle1Null() {
Point3D point = new Point3D(1, 2, 3);
point.angle(null, new Point3D(2, 8, 4));
}
@Test(expected=NullPointerException.class)
public void testPointAngle2Null() {
Point3D point = new Point3D(1, 2, 3);
point.angle(new Point3D(8, 5, 3), null);
}
@Test
public void testPointAngleTooClose() {
Point3D p1 = new Point3D(-0.8944271909999159, 0.0, 0.4472135954999579);
Point3D v = new Point3D(0.0, 0.0, 0.0);
Point3D p2 = new Point3D(-0.894427190999924, 4.061090000458082E-14, 0.4472135954999417);
assertEquals(0.0, v.angle(p1, p2), 0.000001);
assertEquals(0.0, v.angle(p2, p1), 0.000001);
}
@Test
public void testPointAngleTooOpposite() {
Point3D p1 = new Point3D(-0.8944271909999159, 0.0, 0.4472135954999579);
Point3D v = new Point3D(0.0, 0.0, 0.0);
Point3D p2 = new Point3D(0.894427190999924, -4.061090000458082E-14, -0.4472135954999417);
assertEquals(180.0, v.angle(p1, p2), 0.000001);
assertEquals(180.0, v.angle(p2, p1), 0.000001);
}
@Test
public void testMagnitude() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 1, 0);
Point3D p3 = new Point3D(1, -10, 20);
assertEquals(0, p1.magnitude(), 0.000001);
assertEquals(1, p2.magnitude(), 0.000001);
assertEquals(Math.sqrt(501), p3.magnitude(), 0.000001);
}
@Test
public void testDotProduct() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(1, 1, 1);
Point3D p3 = new Point3D(2, -2, 3);
Point3D p4 = new Point3D(-4, 5, 6);
assertEquals(0, p1.dotProduct(p4), 0.000001);
assertEquals(7, p2.dotProduct(p4), 0.000001);
assertEquals(0, p3.dotProduct(p4), 0.000001);
assertEquals(20, p3.dotProduct(-4, -5, 6), 0.000001);
}
@Test(expected=NullPointerException.class)
public void testDotProductNull() {
Point3D point = new Point3D(1, 2, 3);
point.dotProduct(null);
}
@Test
public void testCrossProduct() {
Point3D p1 = new Point3D(0, 0, 0);
Point3D p2 = new Point3D(0, 0, 3);
Point3D p3 = new Point3D(0, 2, 0);
assertEquals(new Point3D(0, 0, 0), p1.crossProduct(p3));
assertEquals(new Point3D(0, 0, 0), p2.crossProduct(p1));
assertEquals(new Point3D(-6, 0, 0), p2.crossProduct(p3));
assertEquals(new Point3D(6, 0, 0), p3.crossProduct(p2));
}
@Test(expected=NullPointerException.class)
public void testCrossProductNull() {
Point3D point = new Point3D(1, 2, 3);
point.crossProduct(null);
}
@Test
public void testInterpolate() {
Point3D p1 = new Point3D(1, 0, 3.5);
Point3D p2 = new Point3D(0, -1, -1);
assertEquals("t=0: should return the initial point", p1, p1.interpolate(p2, 0));
assertEquals("t=1: should return the final point", p2, p1.interpolate(p2, 1));
testNearEquality("t=0.25: should return 25% from the inital point", 0.75, -0.25, 2.375, p1.interpolate(p2, 0.25));
testNearEquality("t=0.5: should return 50% from the inital point", 0.5, -0.5, 1.25, p1.interpolate(p2, 0.5));
testNearEquality("t=0.75: should return 75% from the inital point", 0.25, -0.75, 0.125, p1.interpolate(p2, 0.75));
testNearEquality("t=0.000001: should return 0.0001% from the inital point",
0.999999, -0.000001, 3.4999955, p1.interpolate(p2, 0.000001));
testNearEquality("t=0.999999: should return 99.9999% from the inital point",
0.000001, -0.999999, -0.9999955, p1.interpolate(p2, 0.999999));
}
private void testNearEquality(String message, double expectedX, double expectedY, double expectedZ, Point3D result) {
assertEquals(message, expectedX, result.getX(), 1e-15);
assertEquals(message, expectedY, result.getY(), 1e-15);
assertEquals(message, expectedZ, result.getZ(), 1e-15);
}
@Test
public void testToString() {
Point3D p1 = new Point3D(0, 0, 0);
assertNotNull(p1.toString());
}
}
