package test.com.sun.javafx.scene.traversal;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SceneTraversalEngine;
import com.sun.javafx.scene.traversal.SceneTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalEngine;
import com.sun.javafx.scene.traversal.TraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import com.sun.javafx.scene.traversal.TraverseListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
@RunWith(Parameterized.class)
public final class TraversalTest {
private final int fromNumber;
private final Direction direction;
private final int toNumber;
private final int toNumberTransformed;
private Stage stage;
private Scene scene;
private Node[] keypadNodes;
private SceneTraversalEngine traversalEngine;
@Parameters
public static Collection data() {
return Arrays.asList(new Object[][] {
{ 5, Direction.LEFT, 4, 8 },
{ 5, Direction.RIGHT, 6, 2 },
{ 5, Direction.UP, 2, 4 },
{ 5, Direction.DOWN, 8, 6 },
{ 5, Direction.PREVIOUS, 4, 4 },
{ 5, Direction.NEXT, 6, 6 },
{ 4, Direction.LEFT, 4, 7 },
{ 6, Direction.RIGHT, 6, 3 },
{ 2, Direction.UP, 2, 1 },
{ 8, Direction.DOWN, 8, 9 },
{ 4, Direction.PREVIOUS, 3, 3 },
{ 1, Direction.PREVIOUS, 9, 9 },
{ 6, Direction.NEXT, 7, 7 },
{ 9, Direction.NEXT, 1, 1 },
{ 2, Direction.RIGHT, 3, 2 },
{ 8, Direction.LEFT, 7, 8 },
{ 4, Direction.UP, 1, 4 },
{ 6, Direction.DOWN, 9, 6 },
{ 8, Direction.PREVIOUS, 7, 7 },
{ 7, Direction.PREVIOUS, 6, 6 },
{ 2, Direction.NEXT, 3, 3 },
{ 3, Direction.NEXT, 4, 4 }
});
}
public TraversalTest(final int fromNumber,
final Direction direction,
final int toNumber,
final int toNumberTransformed) {
this.fromNumber = fromNumber;
this.direction = direction;
this.toNumber = toNumber;
this.toNumberTransformed = toNumberTransformed;
}
@Before
public void setUp() {
stage = new Stage();
scene = new Scene(new Group(), 500, 500);
stage.setScene(scene);
traversalEngine = new SceneTraversalEngine(scene);
keypadNodes = createKeypadNodesInScene(scene, traversalEngine);
stage.show();
stage.requestFocus();
}
@After
public void tearDown() {
stage = null;
scene = null;
keypadNodes = null;
traversalEngine = null;
}
@Test
public void untransformedTraversalTest() {
keypadNodes[fromNumber - 1].requestFocus();
traversalEngine.trav(keypadNodes[fromNumber - 1], direction);
assertTrue(keypadNodes[toNumber - 1].isFocused());
}
@Test
public void transformedTraversalTest() {
scene.getRoot().setRotate(90);
keypadNodes[fromNumber - 1].requestFocus();
traversalEngine.trav(keypadNodes[fromNumber - 1], direction);
assertTrue(keypadNodes[toNumberTransformed - 1].isFocused());
}
@Test
public void traverseListenerTest() {
final TraverseListenerImpl traverseListener =
new TraverseListenerImpl();
traversalEngine.addTraverseListener(traverseListener);
keypadNodes[fromNumber - 1].requestFocus();
traversalEngine.trav(keypadNodes[fromNumber - 1], direction);
if (fromNumber != toNumber) {
assertEquals(1, traverseListener.getCallCounter());
assertSame(keypadNodes[toNumber - 1],
traverseListener.getLastNode());
} else {
assertEquals(0, traverseListener.getCallCounter());
}
}
private static Node[] createKeypadNodesInScene(
final Scene scene,
final TraversalEngine traversalEngine) {
final Node[] keypad = new Node[9];
int index = 0;
for (int row = 0; row < 3; ++row) {
for (int column = 0; column < 3; ++column) {
final Node keyNode = new Rectangle(10 + column * 50,
10 + row * 50,
40, 40);
keyNode.setFocusTraversable(true);
keypad[index++] = keyNode;
((Group)scene.getRoot()).getChildren().add(keyNode);
}
}
return keypad;
}
private static final class TraverseListenerImpl
implements TraverseListener {
private int callCounter;
private Node lastNode;
public int getCallCounter() {
return callCounter;
}
public Node getLastNode() {
return lastNode;
}
@Override
public void onTraverse(final Node node, final Bounds bounds) {
++callCounter;
lastNode = node;
}
}
}
