package test.javafx.scene;
import com.sun.javafx.scene.NodeHelper;
import test.com.sun.javafx.pgstub.StubToolkit;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.Group;
import javafx.scene.GroupShim;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.ParentShim;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class ParentTest {
private StubToolkit toolkit;
private Stage stage;
@Before
public void setUp() {
toolkit = (StubToolkit) Toolkit.getToolkit();
stage = new Stage();
}
@After
public void tearDown() {
stage.close();
}
@Test
public void testGroupLookupCorrectId() {
Rectangle rectA = new Rectangle();
Rectangle rectB = new Rectangle();
Rectangle rectC = new Rectangle();
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Rectangle rect4 = new Rectangle();
Rectangle rect5 = new Rectangle();
Rectangle rect6 = new Rectangle();
Rectangle rectE = new Rectangle();
Rectangle rectF = new Rectangle();
rectA.setId("a");
rectB.setId("b");
rectC.setId("c");
rect1.setId("1");
rect2.setId("2");
rect3.setId("3");
rect4.setId("4");
rect5.setId("5");
rect6.setId("6");
rectE.setId("e");
rectF.setId("f");
Group groupD = new Group(rect1, rect2, rect3, rect4, rect5, rect6);
groupD.setId("d");
Group group = new Group(rectA, rectB, rectC, groupD, rectE, rectF);
assertEquals(rect1, group.lookup("#1"));
assertEquals(rect2, group.lookup("#2"));
assertEquals(rect3, group.lookup("#3"));
assertEquals(rect4, group.lookup("#4"));
assertEquals(rect5, group.lookup("#5"));
assertEquals(rect6, group.lookup("#6"));
assertEquals(rectA, group.lookup("#a"));
assertEquals(rectB, group.lookup("#b"));
assertEquals(rectC, group.lookup("#c"));
assertEquals(groupD, group.lookup("#d"));
assertEquals(rectE, group.lookup("#e"));
assertEquals(rectF, group.lookup("#f"));
}
@Test
public void testGroupLookupBadId() {
Rectangle rectA = new Rectangle();
Rectangle rectB = new Rectangle();
Rectangle rectC = new Rectangle();
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Rectangle rect4 = new Rectangle();
Rectangle rect5 = new Rectangle();
Rectangle rect6 = new Rectangle();
Rectangle rectE = new Rectangle();
Rectangle rectF = new Rectangle();
rectA.setId("a");
rectB.setId("b");
rectC.setId("c");
rect1.setId("1");
rect2.setId("2");
rect3.setId("3");
rect4.setId("4");
rect5.setId("5");
rect6.setId("6");
rectE.setId("e");
rectF.setId("f");
Group groupD = new Group(rect1, rect2, rect3, rect4, rect5, rect6);
groupD.setId("d");
Group group = new Group(rectA, rectB, rectC, groupD, rectE, rectF);
assertNull(group.lookup("#4444"));
}
@Test
public void testRemoveChild() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Rectangle rect4 = new Rectangle();
Rectangle rect5 = new Rectangle();
Rectangle rect6 = new Rectangle();
Group g = new Group();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2, rect3, rect4, rect5, rect6);
toolkit.fireTestPulse();
ParentShim.getChildren(g).remove(rect6);
toolkit.fireTestPulse();
final NGGroup peer = NodeHelper.getPeer(g);
assertEquals(5, ParentShim.getChildren(g).size());
assertEquals(5, peer.getChildren().size());
ParentShim.getChildren(g).remove(rect1);
toolkit.fireTestPulse();
assertEquals(4, ParentShim.getChildren(g).size());
assertEquals(4, peer.getChildren().size());
ParentShim.getChildren(g).remove(rect3);
toolkit.fireTestPulse();
assertEquals(3, ParentShim.getChildren(g).size());
assertEquals(3, peer.getChildren().size());
}
@Test
public void testSetChild() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Group g = new Group();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
ParentShim.getChildren(g).set(1, rect3);
toolkit.fireTestPulse();
assertEquals(2, ParentShim.getChildren(g).size());
assertEquals(2, ((NGGroup)NodeHelper.getPeer(g)).getChildren().size());
}
@Test
public void testSetSameChild() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Group g = new Group();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
ParentShim.getChildren(g).set(1, rect2);
toolkit.fireTestPulse();
assertEquals(2, ParentShim.getChildren(g).size());
assertEquals(2, ((NGGroup)NodeHelper.getPeer(g)).getChildren().size());
}
@Test
public void testRemoveAddSameChild() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Rectangle rect4 = new Rectangle();
Rectangle rect5 = new Rectangle();
Rectangle rect6 = new Rectangle();
Group g = new Group();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2, rect3, rect4, rect5, rect6);
toolkit.fireTestPulse();
ParentShim.getChildren(g).remove(rect6);
ParentShim.getChildren(g).add(rect6);
toolkit.fireTestPulse();
assertEquals(6, ParentShim.getChildren(g).size());
assertEquals(6, ((NGGroup)NodeHelper.getPeer(g)).getChildren().size());
}
@Test
public void testRemoveAddDiferentChild() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Rectangle rect4 = new Rectangle();
Rectangle rect5 = new Rectangle();
Rectangle rect6 = new Rectangle();
Group g = new Group();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2, rect3, rect4, rect5);
toolkit.fireTestPulse();
ParentShim.getChildren(g).remove(rect5);
ParentShim.getChildren(g).add(rect6);
toolkit.fireTestPulse();
assertEquals(5, ParentShim.getChildren(g).size());
assertEquals(5, ((NGGroup)NodeHelper.getPeer(g)).getChildren().size());
}
@Test
public void testGetChildrenUnmodifiable() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Group g = new Group();
ParentShim.getChildren(g).addAll(rect1,rect2,rect3);
assertEquals(3, g.getChildrenUnmodifiable().size());
assertSame(rect1, g.getChildrenUnmodifiable().get(0));
assertSame(rect2, g.getChildrenUnmodifiable().get(1));
assertSame(rect3, g.getChildrenUnmodifiable().get(2));
}
@Test
public void testGetChildrenUnmodifiableCantBeModified() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
Rectangle rect3 = new Rectangle();
Group g = new Group();
ParentShim.getChildren(g).addAll(rect1,rect2,rect3);
try {
g.getChildrenUnmodifiable().add(new Rectangle());
fail("UnsupportedOperationException should have been thrown.");
} catch (UnsupportedOperationException uoe) {
}
}
@Test
public void testRequestLayoutWithoutParent() {
Group g = new Group();
g.layout();
g.requestLayout();
}
@Test
public void testRequestLayoutWithoutScene() {
Group g = new Group();
g.setManaged(false);
g.layout();
g.requestLayout();
}
@Test
public void testRequestLayoutClearsCache() {
Group g = new Group();
Rectangle r = new Rectangle(100,200);
ParentShim.getChildren(g).add(r);
g.requestLayout();
assertEquals(100, g.prefWidth(-1), 1e-100);
assertEquals(200, g.prefHeight(-1), 1e-100);
r.setWidth(150);
r.setHeight(250);
g.requestLayout();
assertEquals(150, g.prefWidth(-1), 1e-100);
assertEquals(250, g.prefHeight(-1), 1e-100);
}
@Test
public void testPrefWidthIncludesChildLayoutX() {
Rectangle r = new Rectangle(10,10,100,100);
r.setLayoutX(10);
MockParent p = new MockParent(r);
assertEquals(120, p.prefWidth(-1), 0);
}
@Test
public void testPrefHeightIncludesChildLayoutY() {
Rectangle r = new Rectangle(10,10,100,100);
r.setLayoutY(10);
MockParent p = new MockParent(r);
assertEquals(120, p.prefHeight(-1), 0);
}
@Test
public void testDuplicates() {
Group g = new Group();
Rectangle r1 = new Rectangle();
Rectangle r2 = new Rectangle();
Rectangle r3 = new Rectangle();
Rectangle r4 = new Rectangle();
try {
ParentShim.getChildren(g).addAll(r1, r2, r3, r4);
ParentShim.getChildren(g).add(r2);
fail();
} catch (IllegalArgumentException e) {
}
}
@Test(expected=NullPointerException.class)
public void testAddingNullChild() {
Group g = new Group();
ParentShim.getChildren(g).add(null);
}
@Test(expected=NullPointerException.class)
public void testNullCheckIsDoneBeforeTestForDuplicates() {
Group g = new Group();
ParentShim.getChildren(g).addAll(null, new Rectangle(), null);
}
@Test(expected=IllegalArgumentException.class)
public void testAddingClipNodeTwice() {
Group g = new Group();
Node clipParent = new Rectangle();
Node clipNode = new Rectangle();
clipParent.setClip(clipNode);
try {
ParentShim.getChildren(g).add(clipNode);
fail();
} catch (IllegalArgumentException e) {
}
ParentShim.getChildren(g).add(clipNode);
}
@Test
public void testAddingFixedClipNode() {
Group g = new Group();
Node clipParent = new Rectangle();
Node clipNode = new Rectangle();
clipParent.setClip(clipNode);
try {
ParentShim.getChildren(g).add(clipNode);
fail();
} catch (IllegalArgumentException e) {
}
clipParent.setClip(null);
ParentShim.getChildren(g).add(clipNode);
}
@Test(expected=IllegalArgumentException.class)
public void testFalsePermutation() {
Group g = new Group();
Rectangle r1 = new Rectangle();
Rectangle r2 = new Rectangle();
Rectangle r3 = new Rectangle();
Rectangle r4 = new Rectangle();
ParentShim.getChildren(g).addAll(r1, r2, r3, r4);
ParentShim.getChildren(g).setAll(r1, r2, r2, r4);
}
@Test
public void testFalseDuplicates() {
Group g = new Group();
Rectangle r1 = new Rectangle();
Rectangle r2 = new Rectangle();
Rectangle r3 = new Rectangle();
Rectangle r4 = new Rectangle();
ParentShim.getChildren(g).addAll(r1, r2);
try {
ParentShim.getChildren(g).addAll(r3, r4, r2);
fail();
} catch (IllegalArgumentException e) {
}
try {
ParentShim.getChildren(g).add(r3);
} catch (IllegalArgumentException e) {
fail();
}
}
@Test
public void nodeCanBeAddedDuringLayout() {
final Rectangle rect = new Rectangle(100, 100);
final Group g = new Group(rect);
Group root = new Group() {
@Override protected void layoutChildren() {
ParentShim.getChildren(this).setAll(g);
}
};
Scene scene = new Scene(root);
stage.setScene(scene);
stage.show();
}
private static class LGroup extends Group {
private boolean layoutCalled;
@Override
public void requestLayout() {
super.requestLayout();
layoutCalled = true;
}
public void assertAndClear(boolean b) {
assertEquals(b, layoutCalled);
layoutCalled = false;
}
public void clear() {
layoutCalled = false;
}
}
@Test
public void testRequestLayoutCall() {
Rectangle rect1 = new Rectangle();
Rectangle rect2 = new Rectangle();
LGroup g = new LGroup();
Scene s = new Scene(g);
stage.setScene(s);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
g.clear();
rect1.setLayoutX(10);
g.assertAndClear(true);
toolkit.fireTestPulse();
rect1.setLayoutX(10);
g.assertAndClear(false);
toolkit.fireTestPulse();
rect2.setLayoutX(10);
g.assertAndClear(true);
toolkit.fireTestPulse();
rect2.setLayoutY(10);
g.assertAndClear(true);
toolkit.fireTestPulse();
rect1.setLayoutX(10);
g.assertAndClear(false);
toolkit.fireTestPulse();
rect2.setLayoutY(10);
g.assertAndClear(false);
toolkit.fireTestPulse();
rect1.resizeRelocate(5, 5, 10, 10);
g.assertAndClear(true);
toolkit.fireTestPulse();
rect2.resizeRelocate(5, 5, 10, 10);
g.assertAndClear(true);
toolkit.fireTestPulse();
rect1.resizeRelocate(5, 5, 10, 10);
g.assertAndClear(false);
toolkit.fireTestPulse();
}
@Test
public void requestLayoutAlwaysCalledUpToTheLayoutRoot() {
final Group root = new Group();
final LGroup lroot = new LGroup();
lroot.setManaged(false);
ParentShim.getChildren(root).add(lroot);
final LGroup sub = new LGroup();
ParentShim.getChildren(lroot).add(sub);
lroot.clear();
sub.clear();
root.layout();
lroot.assertAndClear(false);
sub.assertAndClear(false);
sub.requestLayout();
lroot.assertAndClear(true);
sub.assertAndClear(true);
sub.requestLayout();
lroot.assertAndClear(true);
sub.assertAndClear(true);
}
@Test
public void unmanagedParentTest() {
final LGroup innerGroup = new LGroup();
innerGroup.setManaged(false);
final LGroup outerGroup = new LGroup();
outerGroup.setManaged(false);
Scene s = new Scene(outerGroup);
innerGroup.assertAndClear(false);
outerGroup.assertAndClear(true);
outerGroup.getChildren().add(innerGroup);
innerGroup.requestLayout();
innerGroup.assertAndClear(true);
outerGroup.assertAndClear(true);
outerGroup.getChildren().remove(innerGroup);
innerGroup.assertAndClear(false);
outerGroup.assertAndClear(false);
final LGroup intermediate = new LGroup();
intermediate.setManaged(false);
intermediate.setAutoSizeChildren(false);
outerGroup.getChildren().add(intermediate);
innerGroup.assertAndClear(false);
intermediate.assertAndClear(true);
outerGroup.assertAndClear(true);
innerGroup.requestLayout();
intermediate.getChildren().add(innerGroup);
innerGroup.assertAndClear(true);
intermediate.assertAndClear(true);
outerGroup.assertAndClear(false);
}
@Test
public void requestLayoutTriggersPulse() {
final Group root = new Group();
final LGroup lroot = new LGroup();
lroot.setManaged(false);
ParentShim.getChildren(root).add(lroot);
final LGroup sub = new LGroup();
ParentShim.getChildren(lroot).add(sub);
toolkit.clearPulseRequested();
sub.requestLayout();
Scene scene = new Scene(root);
assertTrue(toolkit.isPulseRequested());
toolkit.clearPulseRequested();
root.layout();
sub.requestLayout();
assertTrue(toolkit.isPulseRequested());
}
@Test
public void requestLayoutNotPropagatingDuringLayout() {
final LGroup lroot = new LGroup();
lroot.setManaged(false);
final LGroup sub = new LGroup() {
@Override
protected void layoutChildren() {
GroupShim.layoutChildren((Group)getParent());
requestLayout();
}
};
ParentShim.getChildren(lroot).add(sub);
lroot.clear();
sub.clear();
sub.requestLayout();
lroot.assertAndClear(true);
sub.assertAndClear(true);
lroot.layout();
lroot.assertAndClear(false);
sub.assertAndClear(true);
}
@Test
public void testChildrenPermutationInvalidatesManagedChildrenAndLayout() {
LGroup root = new LGroup();
Rectangle r1 = new Rectangle();
Rectangle r2 = new Rectangle();
ParentShim.getChildren(root).addAll(r1, r2);
root.clear();
ParentShim.getManagedChildren(root).equals(ParentShim.getChildren(root));
ParentShim.getChildren(root).setAll(r2, r1);
ParentShim.getManagedChildren(root).equals(ParentShim.getChildren(root));
root.assertAndClear(true);
r2.toFront();
ParentShim.getManagedChildren(root).equals(ParentShim.getChildren(root));
root.assertAndClear(true);
}
@Test
public void newChildInvalidatesLayoutWhenLayoutBoundsAreValidatedImmediately() {
Group root = new Group();
final AtomicBoolean layoutCalled = new AtomicBoolean();
final AtomicBoolean testReady = new AtomicBoolean();
LGroup sub = new LGroup() {
@Override
protected void layoutChildren() {
if (testReady.get()) {
assertAndClear(true);
layoutCalled.set(true);
}
}
};
ParentShim.getChildren(root).add(sub);
root.getLayoutBounds();
sub.getBoundsInParent();
root.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
});
sub.clear();
testReady.set(true);
ParentShim.getChildren(sub).add(new Rectangle());
assertTrue(layoutCalled.get());
}
@Test
public void sceneListenerCanAddChild() {
final Group root = new Group();
final Scene scene = new Scene(root, 600, 450);
final Group child = new Group(new Group(), new Group(), new Group());
ParentShim.getChildren(child).get(1).sceneProperty().addListener(o -> ParentShim.getChildren(child).add(2, new Group()));
ParentShim.getChildren(root).add(child);
assertSame(scene, ParentShim.getChildren(child).get(3).getScene());
}
@Test
public void testPickingChildNode() {
Rectangle rect1 = new Rectangle();
rect1.setX(10);
rect1.setY(10);
rect1.setWidth(100);
rect1.setHeight(100);
Rectangle rect2 = new Rectangle();
rect2.setX(10);
rect2.setY(10);
rect2.setWidth(100);
rect2.setHeight(100);
Group g = new Group();
Scene scene = new Scene(g);
stage.setScene(scene);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
PickResultChooser res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(50, 50, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertSame(rect2, res.getIntersectedNode());
res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(0, 0, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertNull(res.getIntersectedNode());
}
@Test
public void testPickingChildNodeWithViewOrderSet() {
Rectangle rect1 = new Rectangle();
rect1.setX(10);
rect1.setY(10);
rect1.setWidth(100);
rect1.setHeight(100);
rect1.setViewOrder(-1);
Rectangle rect2 = new Rectangle();
rect2.setX(10);
rect2.setY(10);
rect2.setWidth(100);
rect2.setHeight(100);
Group g = new Group();
Scene scene = new Scene(g);
stage.setScene(scene);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
PickResultChooser res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(50, 50, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertSame(rect1, res.getIntersectedNode());
res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(0, 0, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertNull(res.getIntersectedNode());
}
@Test
public void testPickingChildNodeWithDirtyViewOrder() {
Rectangle rect1 = new Rectangle();
rect1.setX(10);
rect1.setY(10);
rect1.setWidth(100);
rect1.setHeight(100);
rect1.setViewOrder(-1);
Rectangle rect2 = new Rectangle();
rect2.setX(10);
rect2.setY(10);
rect2.setWidth(100);
rect2.setHeight(100);
Group g = new Group();
Scene scene = new Scene(g);
stage.setScene(scene);
stage.show();
ParentShim.getChildren(g).addAll(rect1, rect2);
toolkit.fireTestPulse();
ParentShim.getChildren(g).remove(rect1);
PickResultChooser res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(50, 50, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertSame(rect2, res.getIntersectedNode());
res = new PickResultChooser();
NodeHelper.pickNode(g, new PickRay(0, 0, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), res);
assertNull(res.getIntersectedNode());
}
public static class MockParent extends Parent {
public MockParent(Node... children) {
ParentShim.getChildren(this).addAll(children);
}
}
}
