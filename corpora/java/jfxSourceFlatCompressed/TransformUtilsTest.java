package test.com.sun.javafx.scene.transform;
import test.com.sun.javafx.test.TransformHelper;
import javafx.scene.transform.Transform;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.transform.TransformUtils;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.transform.TransformShim;
import test.javafx.scene.transform.TransformOperationsTest;
import javafx.scene.transform.Translate;
import static org.junit.Assert.*;
import org.junit.Test;
public class TransformUtilsTest {
@Test
public void shouldCreateCorrectImmutableTransform() {
Transform t = TransformShim.getImmutableTransform(
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
TransformHelper.assertMatrix(t,
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
}
@Test
public void immutableTransformShouldApplyCorrectly() {
Affine3D a = new Affine3D();
a.translate(10, 20);
Transform t = TransformShim.getImmutableTransform(
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
com.sun.javafx.scene.transform.TransformHelper.apply(t,a);
TransformHelper.assertMatrix(a,
1, 2, 3, 14,
5, 6, 7, 28,
9, 10, 11, 12);
}
@Test
public void immutableTransformShouldCopyCorrectly() {
Transform src = TransformShim.getImmutableTransform(
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
Transform t = src.clone();
TransformHelper.assertMatrix(t,
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
}
@Test public void testImmutableTransformToString() {
Transform trans = TransformShim.getImmutableTransform(
1, 2, 3, 4,
5, 6, 7, 8,
9, 10, 11, 12);
String s = trans.toString();
assertNotNull(s);
assertFalse(s.isEmpty());
}
@Test public void testImmutableTransformState() {
int counter = 0;
for (Object o : TransformOperationsTest.getParams()) {
Object[] arr = (Object[]) o;
if (arr[0] instanceof TransformShim.ImmutableTransformShim) {
TransformShim.ImmutableTransformShim t =
(TransformShim.ImmutableTransformShim) arr[0];
TransformHelper.assertStateOk("Checking state of transform #" +
(counter++) + " of TransformOperationsTest", t,
TransformShim.getImmutableState3d(t),
TransformShim.getImmutableState2d(t));
}
}
}
@Test public void testReusedImmutableTransform() {
int counter = 0;
for (Object o : TransformOperationsTest.getParams()) {
Object[] arr = (Object[]) o;
if (arr[0] instanceof TransformShim.ImmutableTransformShim) {
Transform t = (Transform) arr[0];
Transform reuse = TransformUtils.immutableTransform(
new Translate(10, 20));
Transform returned = TransformUtils.immutableTransform(reuse, t);
assertSame("Checking reusing immutable transform to values of #"
+ counter + " of TransformOperationsTest", reuse, returned);
TransformHelper.assertStateOk(
"Checking reusing immutable transform to values of #"
+ counter + " of TransformOperationsTest",
returned,
(TransformShim.getImmutableState3d(returned)),
(TransformShim.getImmutableState2d(returned)));
TransformHelper.assertMatrix("Checking reusing immutable "
+ "transform to values of #" + counter
+ " of TransformOperationsTest", returned, t);
Transform returned2 = TransformUtils.immutableTransform(null, t);
assertNotSame("Checking reusing immutable transform to values of #"
+ counter + " of TransformOperationsTest", returned2, t);
TransformHelper.assertStateOk(
"Checking reusing immutable transform to values of #"
+ counter + " of TransformOperationsTest",
returned2,
TransformShim.getImmutableState3d(returned),
TransformShim.getImmutableState2d(returned));
TransformHelper.assertMatrix("Checking reusing immutable "
+ "transform to values of #" + counter
+ " of TransformOperationsTest", returned2, t);
counter++;
}
}
}
@Test public void testConcatenatedImmutableTransform() {
List<TransformShim.ImmutableTransformShim> ts = new LinkedList<>();
for (Object o : TransformOperationsTest.getParams()) {
Object[] arr = (Object[]) o;
if (arr[0] instanceof TransformShim.ImmutableTransformShim) {
ts.add((TransformShim.ImmutableTransformShim) arr[0]);
}
}
int outer = 0;
for (TransformShim.ImmutableTransformShim t1 : ts) {
int inner = 0;
for (TransformShim.ImmutableTransformShim t2 : ts) {
int orig = 0;
for (TransformShim.ImmutableTransformShim t3 : ts) {
Transform clone = t3.clone();
Transform conc = TransformUtils.immutableTransform(
clone, t1, t2);
assertSame("Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" reusing #" + orig +
" of TransformOperationsTest", clone, conc);
TransformHelper.assertStateOk(
"Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" reusing #" + orig +
" of TransformOperationsTest",
(TransformShim.ImmutableTransformShim) conc,
TransformShim.getImmutableState3d(conc),
TransformShim.getImmutableState2d(conc));
TransformHelper.assertMatrix(
"Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" reusing #" + orig +
" of TransformOperationsTest", conc,
TransformHelper.concatenate(t1, t2));
orig++;
}
Transform conc2 = TransformUtils.immutableTransform(
null, t1, t2);
assertNotSame("Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" of TransformOperationsTest", conc2, t1);
assertNotSame("Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" of TransformOperationsTest", conc2, t2);
TransformHelper.assertStateOk(
"Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" of TransformOperationsTest",
(TransformShim.ImmutableTransformShim) conc2,
TransformShim.getImmutableState3d(conc2),
TransformShim.getImmutableState2d(conc2));
TransformHelper.assertMatrix(
"Checking state of concatenation of "
+ "transform #" + outer + " and #" + inner +
" of TransformOperationsTest", conc2,
TransformHelper.concatenate(t1, t2));
inner++;
}
outer++;
}
}
}
