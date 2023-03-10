package test.javafx.scene.control;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import test.com.sun.javafx.pgstub.StubToolkit;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.control.CustomMenuItem;
import org.junit.Before;
import org.junit.Test;
import static test.com.sun.javafx.scene.control.infrastructure.ControlTestUtils.*;
import static org.junit.Assert.*;
public class CustomMenuItemTest {
private CustomMenuItem customMenuItem, cmi;
private CustomMenuItem customMenuItemOneArg;
private CustomMenuItem customMenuItemTwoArg;
private Node node;
private Toolkit tk;
@Before public void setup() {
tk = (StubToolkit)Toolkit.getToolkit();
node = new Rectangle();
customMenuItem = cmi = new CustomMenuItem();
customMenuItemOneArg = new CustomMenuItem(node);
customMenuItemTwoArg = new CustomMenuItem(node, false);
}
@Test public void defaultConstructorShouldSetStyleClassTo_custommenuitem() {
assertStyleClassContains(customMenuItem, "custom-menu-item");
}
@Test public void oneArgConstructorShouldSetStyleClassTo_checkmenuitem() {
assertStyleClassContains(customMenuItemOneArg, "custom-menu-item");
}
@Test public void twoArgConstructorShouldSetStyleClassTo_checkmenuitem() {
assertStyleClassContains(customMenuItemTwoArg, "custom-menu-item");
}
@Test public void defaultNodeNull() {
assertNull(customMenuItem.getContent());
}
@Test public void oneArgConstructorNodeNotNull() {
assertNotNull(customMenuItemOneArg.getContent());
assertSame(customMenuItemOneArg.getContent(), node);
}
@Test public void checkHideOnClickPropertyBind() {
BooleanProperty objPr = new SimpleBooleanProperty(true);
customMenuItem.hideOnClickProperty().bind(objPr);
assertTrue("hideOnClickProperty cannot be bound", customMenuItem.hideOnClickProperty().getValue());
objPr.setValue(false);
assertFalse("hideOnClickProperty cannot be bound", customMenuItem.hideOnClickProperty().getValue());
}
@Test public void checkContentPropertyBind() {
ObjectProperty objPr = new SimpleObjectProperty<Node>(null);
customMenuItem.contentProperty().bind(objPr);
assertNull("contentProperty cannot be bound", customMenuItem.contentProperty().getValue());
objPr.setValue(node);
assertSame("contentProperty cannot be bound", customMenuItem.contentProperty().getValue(), node);
}
@Test public void contentPropertyHasBeanReference() {
assertSame(customMenuItem, customMenuItem.contentProperty().getBean());
}
@Test public void selectedPropertyHasName() {
assertEquals("content", customMenuItem.contentProperty().getName());
}
@Test public void setContentAndSeeValueIsReflectedInModel() {
customMenuItem.setContent(node);
assertSame(customMenuItem.contentProperty().getValue(), node);
}
@Test public void setContentAndSeeValue() {
customMenuItem.setContent(node);
assertSame(customMenuItem.getContent(), node);
}
@Test public void defaultConstructorShouldHaveNoContent() {
assertNull(cmi.getContent());
}
@Test public void defaultConstructorShouldHaveTrueHideClick() {
assertTrue(cmi.isHideOnClick());
}
@Test public void defaultConstructorShouldHaveNullGraphic() {
assertNull(cmi.getGraphic());
}
@Test public void defaultConstructorShouldHaveNullText() {
assertNull(cmi.getText());
}
@Test public void oneArgConstructorShouldHaveNoContent1() {
CustomMenuItem cmi2 = new CustomMenuItem(null);
assertNull(cmi2.getContent());
}
@Test public void oneArgConstructorShouldHaveNoContent2() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect);
assertSame(rect, cmi2.getContent());
}
@Test public void oneArgConstructorShouldHaveTrueHideClick1() {
CustomMenuItem cmi2 = new CustomMenuItem(null);
assertTrue(cmi2.isHideOnClick());
}
@Test public void oneArgConstructorShouldHaveTrueHideClick2() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect);
assertTrue(cmi2.isHideOnClick());
}
@Test public void oneArgConstructorShouldHaveNullGraphic() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect);
assertNull(cmi2.getGraphic());
}
@Test public void oneArgConstructorShouldHaveNullText() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect);
assertNull(cmi2.getText());
}
@Test public void twoArgConstructorShouldHaveSpecifiedContent1() {
CustomMenuItem cmi2 = new CustomMenuItem(null, true);
assertNull(cmi2.getContent());
}
@Test public void twoArgConstructorShouldHaveSpecifiedContent2() {
CustomMenuItem cmi2 = new CustomMenuItem(null, false);
assertNull(cmi2.getContent());
}
@Test public void twoArgConstructorShouldHaveSpecifiedContent3() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, true);
assertSame(rect, cmi2.getContent());
}
@Test public void twoArgConstructorShouldHaveSpecifiedContent4() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, false);
assertSame(rect, cmi2.getContent());
}
@Test public void twoArgConstructorShouldHaveSpecifiedHideClick1() {
CustomMenuItem cmi2 = new CustomMenuItem(null, true);
assertTrue(cmi2.isHideOnClick());
}
@Test public void twoArgConstructorShouldHaveSpecifiedHideClick2() {
CustomMenuItem cmi2 = new CustomMenuItem(null, false);
assertFalse(cmi2.isHideOnClick());
}
@Test public void twoArgConstructorShouldHaveSpecifiedHideClick3() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, true);
assertTrue(cmi2.isHideOnClick());
}
@Test public void twoArgConstructorShouldHaveSpecifiedHideClick4() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, false);
assertFalse(cmi2.isHideOnClick());
}
@Test public void twoArgConstructorShouldHaveNullGraphic() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, true);
assertNull(cmi2.getGraphic());
}
@Test public void twoArgConstructorShouldHaveNullText() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, true);
assertNull(cmi2.getText());
}
@Test public void resetContent1() {
Rectangle rect = new Rectangle();
cmi.setContent(rect);
assertSame(rect, cmi.getContent());
}
@Test public void resetContent2() {
Rectangle rect = new Rectangle();
CustomMenuItem cmi2 = new CustomMenuItem(rect, true);
Rectangle rect2 = new Rectangle();
cmi2.setContent(rect2);
assertSame(rect2, cmi2.getContent());
}
@Test public void resetContent3() {
Rectangle rect = new Rectangle();
CustomMenuItem mi2 = new CustomMenuItem(rect, true);
Rectangle rect2 = null;
mi2.setContent(rect2);
assertNull(mi2.getContent());
}
@Test public void getUnspecifiedContentProperty1() {
CustomMenuItem cmi2 = new CustomMenuItem();
assertNotNull(cmi2.contentProperty());
assertNull(cmi2.getContent());
}
@Test public void getUnspecifiedContentProperty2() {
CustomMenuItem cmi2 = new CustomMenuItem(null, true);
assertNotNull(cmi2.contentProperty());
}
@Test public void contentCanBeBound() {
Rectangle rect = new Rectangle();
SimpleObjectProperty<Node> other = new SimpleObjectProperty<Node>(cmi, "content", rect);
cmi.contentProperty().bind(other);
assertEquals(rect, cmi.getContent());
}
@Test public void getUnspecifiedHideOnClick() {
assertTrue(cmi.isHideOnClick());
}
@Test public void setTrueHideOnClick() {
cmi.setHideOnClick(true);
assertTrue(cmi.isHideOnClick());
}
@Test public void setFalseHideOnClick() {
cmi.setHideOnClick(false);
assertFalse(cmi.isHideOnClick());
}
@Test public void hideOnClickNotSetButNotNull() {
cmi.hideOnClickProperty();
assertNotNull(cmi.isHideOnClick());
}
@Test public void hideOnClickCanBeBound1() {
SimpleBooleanProperty other = new SimpleBooleanProperty(cmi, "hideOnClick", false);
cmi.hideOnClickProperty().bind(other);
assertEquals(other.get(), cmi.isHideOnClick());
}
@Test public void hideOnClickCanBeBound2() {
SimpleBooleanProperty other = new SimpleBooleanProperty(cmi, "hideOnClick", true);
cmi.hideOnClickProperty().bind(other);
assertEquals(other.get(), cmi.isHideOnClick());
}
}
