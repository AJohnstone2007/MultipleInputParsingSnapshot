package test.javafx.scene.control;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import com.sun.javafx.tk.Toolkit;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.*;
import static org.junit.Assert.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.com.sun.javafx.pgstub.StubToolkit;
import test.com.sun.javafx.scene.control.infrastructure.KeyEventFirer;
@RunWith(Parameterized.class)
public class ComboSpecialKeyTest {
private Toolkit tk;
private Scene scene;
private Stage stage;
private Pane root;
private ComboBoxBase comboBox;
private Supplier<ComboBoxBase> comboFactory;
private boolean editable;
@Test
public void testF4TogglePopup() {
showAndFocus();
comboBox.setEditable(editable);
assertFalse(comboBox.isShowing());
KeyEventFirer firer = new KeyEventFirer(comboBox);
firer.doKeyPress(F4);
assertTrue(failPrefix(), comboBox.isShowing());
firer.doKeyPress(F4);
assertFalse(failPrefix(), comboBox.isShowing());
}
@Test
public void testF4ConsumeFilterNotTogglePopup() {
showAndFocus();
comboBox.setEditable(editable);
List<KeyEvent> events = new ArrayList<>();
comboBox.addEventFilter(KEY_RELEASED, e -> {
if (e.getCode() == F4) {
events.add(e);
e.consume();
}
});
KeyEventFirer firer = new KeyEventFirer(comboBox);
firer.doKeyPress(F4);
assertFalse(failPrefix() + ": popup must not be showing", comboBox.isShowing());
}
protected String failPrefix() {
String failPrefix = comboBox.getClass().getSimpleName() + " editable " + editable;
return failPrefix;
}
@Parameterized.Parameters
public static Collection<Object[]> data() {
Object[][] data = new Object[][] {
{(Supplier)ComboBox::new, false},
{(Supplier)ComboBox::new, true },
{(Supplier)DatePicker::new, false },
{(Supplier)DatePicker::new, true},
{(Supplier)ColorPicker::new, false },
};
return Arrays.asList(data);
}
public ComboSpecialKeyTest(Supplier<ComboBoxBase> factory, boolean editable) {
this.comboFactory = factory;
this.editable = editable;
}
@Test
public void testInitialState() {
assertNotNull(comboBox);
showAndFocus();
List<Node> expected = List.of(comboBox);
assertEquals(expected, root.getChildren());
}
protected void showAndFocus() {
showAndFocus(comboBox);
}
protected void showAndFocus(Node control) {
stage.show();
stage.requestFocus();
control.requestFocus();
assertTrue(control.isFocused());
assertSame(control, scene.getFocusOwner());
}
@After
public void cleanup() {
stage.hide();
}
@Before
public void setup() {
ComboBoxPopupControl c;
tk = (StubToolkit) Toolkit.getToolkit();
root = new VBox();
scene = new Scene(root);
stage = new Stage();
stage.setScene(scene);
comboBox = comboFactory.get();
root.getChildren().addAll(comboBox);
}
}
