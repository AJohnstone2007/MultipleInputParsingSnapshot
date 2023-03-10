package test.javafx.scene.layout;
import test.javafx.scene.layout.MockBiased;
import static org.junit.Assert.assertEquals;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.ParentShim;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.junit.Test;
public class AnchorPaneTest {
@Test public void testNoAnchorsSet() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
child.relocate(10, 20);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(420, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(420, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testTopAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(300, anchorpane.minWidth(-1), 1e-100);
assertEquals(410, anchorpane.minHeight(-1), 1e-100);
assertEquals(300, anchorpane.prefWidth(-1), 1e-100);
assertEquals(410, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(10, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(10, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setLeftAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(400, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(400, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testBottomAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setBottomAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(300, anchorpane.minWidth(-1), 1e-100);
assertEquals(410, anchorpane.minHeight(-1), 1e-100);
assertEquals(300, anchorpane.prefWidth(-1), 1e-100);
assertEquals(410, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(90, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testRightAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setRightAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(400, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(400, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(190, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testTopLeftAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setLeftAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(420, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(420, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testTopBottomAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(300, anchorpane.minWidth(-1), 1e-100);
assertEquals(230, anchorpane.minHeight(-1), 1e-100);
assertEquals(300, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(470, child.getHeight(), 1e-100);
}
@Test public void testTopRightAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setRightAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(420, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(420, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(190, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftBottomAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setBottomAnchor(child,20.0);
anchorpane.setLeftAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(420, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(420, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(80, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftRightAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setRightAnchor(child,20.0);
anchorpane.setLeftAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(130, anchorpane.minWidth(-1), 1e-100);
assertEquals(400, anchorpane.minHeight(-1), 1e-100);
assertEquals(330, anchorpane.prefWidth(-1), 1e-100);
assertEquals(400, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(470, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftTopRightAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setRightAnchor(child,20.0);
anchorpane.setLeftAnchor(child, 10.0);
anchorpane.setTopAnchor(child, 30.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(130, anchorpane.minWidth(-1), 1e-100);
assertEquals(430, anchorpane.minHeight(-1), 1e-100);
assertEquals(330, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(30, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(30, child.getLayoutY(), 1e-100);
assertEquals(470, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftBottomRightAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setRightAnchor(child,20.0);
anchorpane.setLeftAnchor(child, 10.0);
anchorpane.setBottomAnchor(child, 30.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(130, anchorpane.minWidth(-1), 1e-100);
assertEquals(430, anchorpane.minHeight(-1), 1e-100);
assertEquals(330, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(70, child.getLayoutY(), 1e-100);
assertEquals(470, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testTopLeftBottomAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
anchorpane.setLeftAnchor(child, 30.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(330, anchorpane.minWidth(-1), 1e-100);
assertEquals(230, anchorpane.minHeight(-1), 1e-100);
assertEquals(330, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(30, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(30, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(470, child.getHeight(), 1e-100);
}
@Test public void testTopRightBottomAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
anchorpane.setRightAnchor(child, 30.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(330, anchorpane.minWidth(-1), 1e-100);
assertEquals(230, anchorpane.minHeight(-1), 1e-100);
assertEquals(330, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(170, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(470, child.getHeight(), 1e-100);
}
@Test public void testAllSidesAnchored() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
anchorpane.setRightAnchor(child, 30.0);
anchorpane.setLeftAnchor(child, 40.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(170, anchorpane.minWidth(-1), 1e-100);
assertEquals(230, anchorpane.minHeight(-1), 1e-100);
assertEquals(370, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(40, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(40, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(430, child.getWidth(), 1e-100);
assertEquals(470, child.getHeight(), 1e-100);
}
@Test public void testAllSidesAnchoredWithPadding() {
AnchorPane anchorpane = new AnchorPane();
anchorpane.setPadding(new Insets(10,20,30,40));
MockResizable child = new MockResizable(100,200, 300,400, 500,600);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
anchorpane.setRightAnchor(child, 30.0);
anchorpane.setLeftAnchor(child, 40.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(230, anchorpane.minWidth(-1), 1e-100);
assertEquals(270, anchorpane.minHeight(-1), 1e-100);
assertEquals(430, anchorpane.prefWidth(-1), 1e-100);
assertEquals(470, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(80, child.getLayoutX(), 1e-100);
assertEquals(30, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(80, child.getLayoutX(), 1e-100);
assertEquals(30, child.getLayoutY(), 1e-100);
assertEquals(370, child.getWidth(), 1e-100);
assertEquals(430, child.getHeight(), 1e-100);
}
@Test public void testNonresizableAllSidesAnchored() {
AnchorPane anchorpane = new AnchorPane();
Rectangle child = new Rectangle(300,400);
anchorpane.setTopAnchor(child,20.0);
anchorpane.setBottomAnchor(child, 10.0);
anchorpane.setRightAnchor(child, 30.0);
anchorpane.setLeftAnchor(child, 40.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(370, anchorpane.minWidth(-1), 1e-100);
assertEquals(430, anchorpane.minHeight(-1), 1e-100);
assertEquals(370, anchorpane.prefWidth(-1), 1e-100);
assertEquals(430, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(40, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(40, child.getLayoutX(), 1e-100);
assertEquals(20, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testAnchorPaneWithHorizontalBiasedChild() {
AnchorPane anchorpane = new AnchorPane();
MockBiased biased = new MockBiased(Orientation.HORIZONTAL, 100,100);
Rectangle rect = new Rectangle(200,200);
AnchorPane.setTopAnchor(biased, 10.0);
AnchorPane.setLeftAnchor(biased, 10.0);
AnchorPane.setRightAnchor(biased, 10.0);
AnchorPane.setTopAnchor(rect, 10.0);
AnchorPane.setLeftAnchor(rect, 10.0);
AnchorPane.setBottomAnchor(rect, 10.0);
AnchorPane.setRightAnchor(rect, 10.0);
ParentShim.getChildren(anchorpane).addAll(biased, rect);
assertEquals(220, anchorpane.prefWidth(-1), 1e-100);
assertEquals(220, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10.0, biased.getLayoutX(), 1e-100);
assertEquals(10.0, biased.getLayoutY(), 1e-100);
assertEquals(200, biased.getLayoutBounds().getWidth(), 1e-100);
assertEquals(50, biased.getLayoutBounds().getHeight(), 1e-100);
assertEquals(10.0, rect.getLayoutX(), 1e-100);
assertEquals(10.0, rect.getLayoutY(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getWidth(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getHeight(), 1e-100);
anchorpane.resize(420, 420);
anchorpane.layout();
assertEquals(10.0, biased.getLayoutX(), 1e-100);
assertEquals(10.0, biased.getLayoutY(), 1e-100);
assertEquals(400, biased.getLayoutBounds().getWidth(), 1e-100);
assertEquals(25, biased.getLayoutBounds().getHeight(), 1e-100);
assertEquals(10, rect.getLayoutX(), 1e-100);
assertEquals(10, rect.getLayoutY(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getWidth(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getHeight(), 1e-100);
}
@Test public void testAnchorPaneWithVerticalBiasedChild() {
AnchorPane anchorpane = new AnchorPane();
MockBiased biased = new MockBiased(Orientation.VERTICAL, 100,100);
Rectangle rect = new Rectangle(200,200);
AnchorPane.setTopAnchor(biased, 10.0);
AnchorPane.setLeftAnchor(biased, 10.0);
AnchorPane.setBottomAnchor(biased, 10.0);
AnchorPane.setTopAnchor(rect, 10.0);
AnchorPane.setLeftAnchor(rect, 10.0);
AnchorPane.setBottomAnchor(rect, 10.0);
AnchorPane.setRightAnchor(rect, 10.0);
ParentShim.getChildren(anchorpane).addAll(biased, rect);
assertEquals(220, anchorpane.prefWidth(-1), 1e-100);
assertEquals(220, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, biased.getLayoutX(), 1e-100);
assertEquals(10, biased.getLayoutY(), 1e-100);
assertEquals(50, biased.getLayoutBounds().getWidth(), 1e-100);
assertEquals(200, biased.getLayoutBounds().getHeight(), 1e-100);
assertEquals(10, rect.getLayoutX(), 1e-100);
assertEquals(10, rect.getLayoutY(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getWidth(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getHeight(), 1e-100);
anchorpane.resize(420, 420);
anchorpane.layout();
assertEquals(10, biased.getLayoutX(), 1e-100);
assertEquals(10, biased.getLayoutY(), 1e-100);
assertEquals(25, biased.getLayoutBounds().getWidth(), 1e-100);
assertEquals(400, biased.getLayoutBounds().getHeight(), 1e-100);
assertEquals(10, rect.getLayoutX(), 1e-100);
assertEquals(10, rect.getLayoutY(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getWidth(), 1e-100);
assertEquals(200, rect.getLayoutBounds().getHeight(), 1e-100);
}
@Test public void testAnchorPaneWithChildPrefSizeLessThanMinSize() {
AnchorPane anchorpane = new AnchorPane();
MockResizable resizable = new MockResizable(30, 30, 20, 20, Double.MAX_VALUE, Double.MAX_VALUE);
ParentShim.getChildren(anchorpane).add(resizable);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, resizable.getLayoutX(), 1e-100);
assertEquals(0, resizable.getLayoutY(), 1e-100);
assertEquals(30, resizable.getLayoutBounds().getWidth(), 1e-100);
assertEquals(30, resizable.getLayoutBounds().getHeight(), 1e-100);
}
@Test public void testAnchorPanePrefHeightWithHorizontalBiasedChild_RT21745() {
AnchorPane anchorpane = new AnchorPane();
AnchorPane internalAnchorpane = new AnchorPane();
MockBiased biased = new MockBiased(Orientation.HORIZONTAL, 30, 256);
ParentShim.getChildren(internalAnchorpane).add(biased);
ParentShim.getChildren(anchorpane).add(internalAnchorpane);
anchorpane.resize(500, 500);
anchorpane.layout();
assertEquals(30, anchorpane.prefWidth(-1), 1e-100);
assertEquals(256, anchorpane.prefHeight(-1), 1e-100);
assertEquals(30, internalAnchorpane.prefWidth(-1), 1e-100);
assertEquals(256, internalAnchorpane.prefHeight(-1), 1e-100);
}
@Test
public void testTopAnchoredMinSizeOverridden() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(300, 400, 100, 100, 500, 600);
anchorpane.setTopAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(300, anchorpane.minWidth(-1), 1e-100);
assertEquals(410, anchorpane.minHeight(-1), 1e-100);
assertEquals(300, anchorpane.prefWidth(-1), 1e-100);
assertEquals(410, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(10, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test
public void testBottomAnchoredMinSizeOverridden() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(300,400, 100,100, 500,600);
anchorpane.setBottomAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(300, anchorpane.minWidth(-1), 1e-100);
assertEquals(410, anchorpane.minHeight(-1), 1e-100);
assertEquals(300, anchorpane.prefWidth(-1), 1e-100);
assertEquals(410, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testLeftAnchoredMinSizeOverridden() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(300,400, 100,100, 500,600);
anchorpane.setLeftAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(400, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(400, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(10, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
@Test public void testRightAnchoredMinSizeOverridden() {
AnchorPane anchorpane = new AnchorPane();
MockResizable child = new MockResizable(300,400, 100,100, 500,600);
anchorpane.setRightAnchor(child, 10.0);
ParentShim.getChildren(anchorpane).add(child);
assertEquals(310, anchorpane.minWidth(-1), 1e-100);
assertEquals(400, anchorpane.minHeight(-1), 1e-100);
assertEquals(310, anchorpane.prefWidth(-1), 1e-100);
assertEquals(400, anchorpane.prefHeight(-1), 1e-100);
anchorpane.autosize();
anchorpane.layout();
assertEquals(0, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
anchorpane.resize(500,500);
anchorpane.layout();
assertEquals(190, child.getLayoutX(), 1e-100);
assertEquals(0, child.getLayoutY(), 1e-100);
assertEquals(300, child.getWidth(), 1e-100);
assertEquals(400, child.getHeight(), 1e-100);
}
}
