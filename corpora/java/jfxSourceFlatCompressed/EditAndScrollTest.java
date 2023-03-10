package test.javafx.scene.control.skin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static test.com.sun.javafx.scene.control.infrastructure.VirtualizedControlTestUtils.*;
import static org.junit.Assert.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
public class EditAndScrollTest {
private Scene scene;
private Stage stage;
private Pane root;
int rows;
int editingRow;
@Test
public void testTreeTableViewEditingAfterMouseOnVerticalScrollBar() {
TreeTableView<?> control = createAndShowTreeTableView(true);
TreeTablePosition<?, ?> editingItem = new TreeTablePosition(control, editingRow, control.getColumns().get(0));
fireMouseOnVerticalTrack(control);
assertEquals(editingItem, control.getEditingCell());
}
@Test
public void testTreeTableViewEditingAfterMouseOnHorizontalScrollBar() {
TreeTableView<?> control = createAndShowTreeTableView(true);
TreeTablePosition<?, ?> editingItem = new TreeTablePosition(control, editingRow, control.getColumns().get(0));
fireMouseOnHorizontalTrack(control);
assertEquals(editingItem, control.getEditingCell());
}
@Test
public void testTreeTableViewFocusedAfterMouseOnVerticalScrollBar() {
TreeTableView<?> control = createAndShowTreeTableView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.VERTICAL);
}
@Test
public void testTreeTableViewFocusedAfterMouseOnHorizontalScrollBar() {
TreeTableView<?> control = createAndShowTreeTableView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.HORIZONTAL);
}
@Test
public void testTreeTableViewEditing() {
TreeTableView<?> control = createAndShowTreeTableView(true);
assertEquals(rows + 1, control.getExpandedItemCount());
assertEquals(100, scene.getWidth(), 1);
assertEquals(330, scene.getHeight(), 1);
assertTrue("sanity: vertical scrollbar visible for list " ,
getHorizontalScrollBar(control).isVisible());
assertTrue("sanity: vertical scrollbar visible for list " ,
getVerticalScrollBar(control).isVisible());
TreeTablePosition<?, ?> editingItem = new TreeTablePosition(control, editingRow, control.getColumns().get(0));
assertEquals("control must be editing at", editingItem, control.getEditingCell());
}
private TreeTableView<?> createAndShowTreeTableView(boolean startEdit) {
TreeItem<MenuItem> root = new TreeItem<>(new MenuItem("root"));
root.setExpanded(true);
ObservableList<String> baseData = createData(rows, false);
baseData.forEach(s -> root.getChildren().add(new TreeItem<>(new MenuItem(s))));
TreeTableView<MenuItem> control = new TreeTableView<>(root);
control.setEditable(true);
TreeTableColumn<MenuItem, String> first = createTreeTableColumn();
control.getColumns().addAll(first);
for (int i = 0; i < 10; i++) {
control.getColumns().add(createTreeTableColumn());
}
showControl(control, true, 100, 330);
if (startEdit) {
control.edit(editingRow, first);
}
return control;
}
private TreeTableColumn<MenuItem, String> createTreeTableColumn() {
TreeTableColumn<MenuItem, String> first = new TreeTableColumn<>("Text");
first.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
first.setCellValueFactory(new TreeItemPropertyValueFactory<>("text"));
return first;
}
@Test
public void testTableViewEditingAfterMouseOnVerticalScrollBar() {
TableView<?> control = createAndShowTableView(true);
TablePosition<?, ?> editingItem = new TablePosition(control, editingRow, control.getColumns().get(0));
fireMouseOnVerticalTrack(control);
assertEquals(editingItem, control.getEditingCell());
}
@Test
public void testTableViewEditingAfterMouseOnHorizontalScrollBar() {
TableView<?> control = createAndShowTableView(true);
TablePosition<?, ?> editingItem = new TablePosition(control, editingRow, control.getColumns().get(0));
fireMouseOnHorizontalTrack(control);
assertEquals(editingItem, control.getEditingCell());
}
@Test
public void testTableViewFocusedAfterMouseOnVerticalScrollBar() {
TableView<?> control = createAndShowTableView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.VERTICAL);
}
@Test
public void testTableViewFocusedAfterMouseOnHorizontalScrollBar() {
TableView<?> control = createAndShowTableView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.HORIZONTAL);
}
@Test
public void testTableViewEditing() {
TableView<?> control = createAndShowTableView(true);
assertEquals(rows, control.getItems().size());
assertEquals(100, scene.getWidth(), 1);
assertEquals(330, scene.getHeight(), 1);
assertTrue("sanity: vertical scrollbar visible for list " ,
getHorizontalScrollBar(control).isVisible());
assertTrue("sanity: vertical scrollbar visible for list " ,
getVerticalScrollBar(control).isVisible());
TablePosition<?, ?> editingItem = new TablePosition(control, editingRow, control.getColumns().get(0));
assertEquals("control must be editing at", editingItem, control.getEditingCell());
}
private TableView<?> createAndShowTableView(boolean startEdit) {
TableView<MenuItem> control = new TableView<>();
createData(60, false).forEach(s -> control.getItems().add(new MenuItem(s)));
control.setEditable(true);
TableColumn<MenuItem, String> first = createColumn();
control.getColumns().addAll(first);
for (int i = 0; i < 10; i++) {
control.getColumns().add(createColumn());
}
showControl(control, true, 100, 330);
if (startEdit) {
control.edit(editingRow, first);
}
return control;
}
private TableColumn<MenuItem, String> createColumn() {
TableColumn<MenuItem, String> first = new TableColumn<>("Text");
first.setCellFactory(TextFieldTableCell.forTableColumn());
first.setCellValueFactory(new PropertyValueFactory<>("text"));
return first;
}
@Test
public void testTreeViewEditingAfterMouseOnVerticalScrollBar() {
TreeView<?> control = createAndShowTreeView(true);
TreeItem<?> editingItem = control.getTreeItem(editingRow);
fireMouseOnVerticalTrack(control);
assertEquals(editingItem, control.getEditingItem());
}
@Test
public void testTreeViewEditingAfterMouseOnHorizontalScrollBar() {
TreeView<?> control = createAndShowTreeView(true);
TreeItem<?> editingItem = control.getTreeItem(editingRow);
fireMouseOnHorizontalTrack(control);
assertEquals(editingItem, control.getEditingItem());
}
@Test
public void testTreeViewFocusedAfterMouseOnVerticalScrollBar() {
TreeView<?> control = createAndShowTreeView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.VERTICAL);
}
@Test
public void testTreeViewFocusedAfterMouseOnHorizontalScrollBar() {
TreeView<?> control = createAndShowTreeView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.HORIZONTAL);
}
@Test
public void testTreeViewEditing() {
TreeView<?> control = createAndShowTreeView(true);
assertEquals(rows + 1, control.getExpandedItemCount());
assertEquals(100, scene.getWidth(), 1);
assertEquals(330, scene.getHeight(), 1);
assertTrue("sanity: vertical scrollbar visible for list " ,
getHorizontalScrollBar(control).isVisible());
assertTrue("sanity: vertical scrollbar visible for list " ,
getVerticalScrollBar(control).isVisible());
TreeItem<?> editingItem = control.getTreeItem(editingRow);
assertEquals("control must be editing at", editingItem, control.getEditingItem());
}
private TreeView<?> createAndShowTreeView(boolean startEdit) {
TreeItem<String> root = new TreeItem<>("root");
root.setExpanded(true);
ObservableList<String> baseData = createData(rows, true);
baseData.forEach(s -> root.getChildren().add(new TreeItem<>(s)));
TreeView<String> control = new TreeView<>(root);
control.setEditable(true);
control.setCellFactory(TextFieldTreeCell.forTreeView(new DefaultStringConverter()));
showControl(control, true, 100, 330);
if (startEdit) {
TreeItem<String> editingItem = control.getTreeItem(editingRow);
control.edit(editingItem);
}
return control;
}
@Test
public void testListViewEditingAfterMouseOnVerticalScrollBar() {
ListView<?> control = createAndShowListView(true);
fireMouseOnVerticalTrack(control);
assertEquals(editingRow, control.getEditingIndex());
}
@Test
public void testListViewEditingAfterMouseOnHorizontalScrollBar() {
ListView<?> control = createAndShowListView(true);
fireMouseOnHorizontalTrack(control);
assertEquals(editingRow, control.getEditingIndex());
}
@Test
public void testListViewFocusedAfterMouseOnVerticalScrollBar() {
ListView<?> control = createAndShowListView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.VERTICAL);
}
@Test
public void testListViewFocusedAfterMouseOnHorizontalScrollBar() {
ListView<?> control = createAndShowListView(false);
assertFocusedAfterMouseOnScrollBar(control, Orientation.HORIZONTAL);
}
@Test
public void testListViewEditing() {
ListView<?> control = createAndShowListView(true);
assertEquals(rows, control.getItems().size());
assertEquals(100, scene.getWidth(), 1);
assertEquals(330, scene.getHeight(), 1);
assertTrue("sanity: vertical scrollbar visible for list " ,
getHorizontalScrollBar(control).isVisible());
assertTrue("sanity: vertical scrollbar visible for list " ,
getVerticalScrollBar(control).isVisible());
assertEquals("control must be editing at", editingRow, control.getEditingIndex());
}
private ListView<?> createAndShowListView(boolean startEdit) {
ObservableList<String> baseData = createData(rows, true);
ListView<String> control = new ListView<>(baseData);
control.setEditable(true);
control.setCellFactory(TextFieldListCell.forListView(new DefaultStringConverter()));
showControl(control, true, 100, 330);
if (startEdit) {
control.edit(editingRow);
}
return control;
}
private void assertFocusedAfterMouseOnScrollBar(Control control, Orientation dir) {
Button focusableControl = new Button("dummy");
showControl(focusableControl, true);
if (dir == Orientation.HORIZONTAL) {
fireMouseOnHorizontalTrack(control);
} else {
fireMouseOnVerticalTrack(control);
}
assertEquals("virtualized control must be focusOwner after mouse on scrollbar",
control, scene.getFocusOwner());
}
private ObservableList<String> createData(int size, boolean wide) {
ObservableList<String> data = FXCollections.observableArrayList();
String item = wide ? "something that really really guarantees a horizontal scrollbar is visible  " : "item";
for (int i = 0; i < size; i++) {
data.add(item + i);
}
return data;
}
protected void showControl(Control control, boolean focused) {
showControl(control, focused, -1, -1);
}
protected void showControl(Control control, boolean focused, double width, double height) {
if (root == null) {
root = new VBox();
if (width > 0) {
scene = new Scene(root, width, height);
} else {
scene = new Scene(root);
}
stage = new Stage();
stage.setScene(scene);
}
if (!root.getChildren().contains(control)) {
root.getChildren().add(control);
}
stage.show();
if (focused) {
stage.requestFocus();
control.requestFocus();
assertTrue(control.isFocused());
assertSame(control, scene.getFocusOwner());
}
}
@Before public void setup() {
Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
if (throwable instanceof RuntimeException) {
throw (RuntimeException)throwable;
} else {
Thread.currentThread().getThreadGroup().uncaughtException(thread, throwable);
}
});
editingRow = 1;
rows = 60;
}
@After public void cleanup() {
if (stage != null) stage.hide();
Thread.currentThread().setUncaughtExceptionHandler(null);
}
}
