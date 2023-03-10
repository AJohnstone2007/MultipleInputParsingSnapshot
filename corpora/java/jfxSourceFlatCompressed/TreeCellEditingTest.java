package test.javafx.scene.control;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeView.EditEvent;
@RunWith(Parameterized.class)
public class TreeCellEditingTest {
private TreeCell<String> cell;
private TreeView<String> tree;
private ObservableList<TreeItem<String>> model;
private int cellIndex;
private int editingIndex;
@Test
public void testOffEditingIndex() {
cell.updateIndex(editingIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
assertTrue("sanity: cell is editing", cell.isEditing());
cell.updateIndex(cellIndex);
assertEquals("sanity: cell index changed", cellIndex, cell.getIndex());
assertFalse("cell must not be editing on update from editingIndex " + editingIndex
+ " to cellIndex " + cellIndex, cell.isEditing());
}
@Test
public void testCancelOffEditingIndex() {
cell.updateIndex(editingIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
List<EditEvent<String>> events = new ArrayList<>();
tree.setOnEditCancel(events::add);
cell.updateIndex(cellIndex);
assertEquals("sanity: tree editing unchanged", editingItem, tree.getEditingItem());
assertEquals("sanity: editingIndex unchanged", editingIndex, tree.getRow(editingItem));
assertEquals("cell must have fired edit cancel", 1, events.size());
assertEquals("cancel on updateIndex from " + editingIndex + " to " + cellIndex + "\n  ",
editingItem, events.get(0).getTreeItem());
}
@Test
public void testToEditingIndex() {
cell.updateIndex(cellIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
assertFalse("sanity: cell must not be editing", cell.isEditing());
cell.updateIndex(editingIndex);
assertEquals("sanity: cell at editing index", editingIndex, cell.getIndex());
assertTrue("cell must be editing on update from " + cellIndex
+ " to editingIndex " + editingIndex, cell.isEditing());
}
@Test
public void testStartEvent() {
cell.updateIndex(cellIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
List<EditEvent<String>> events = new ArrayList<>();
tree.setOnEditStart(events::add);
cell.updateIndex(editingIndex);
assertEquals("cell must have fired edit start on update from " + cellIndex + " to " + editingIndex,
1, events.size());
assertEquals("treeItem of start event ", editingItem, events.get(0).getTreeItem());
}
@Parameterized.Parameters
public static Collection<Object[]> data() {
Object[][] data = new Object[][] {
{1, 2},
{0, 1},
{1, 0},
{-1, 1},
};
return Arrays.asList(data);
}
public TreeCellEditingTest(int cellIndex, int editingIndex) {
this.cellIndex = cellIndex;
this.editingIndex = editingIndex;
}
@Test
public void testEditOnCellIndex() {
cell.updateIndex(editingIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
assertTrue("sanity: cell must be editing", cell.isEditing());
}
@Test
public void testEditOffCellIndex() {
cell.updateIndex(cellIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
assertFalse("sanity: cell editing must be unchanged", cell.isEditing());
}
@Test
public void testUpdateSameIndexWhileEdititing() {
cell.updateIndex(editingIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
List<EditEvent<String>> events = new ArrayList<>();
tree.setOnEditCancel(events::add);
tree.setOnEditCommit(events::add);
tree.setOnEditStart(events::add);
cell.updateIndex(editingIndex);
assertEquals(editingItem, tree.getEditingItem());
assertTrue(cell.isEditing());
assertEquals(0, events.size());
}
@Test
public void testUpdateSameIndexWhileNotEdititing() {
cell.updateIndex(cellIndex);
TreeItem<String> editingItem = tree.getTreeItem(editingIndex);
tree.edit(editingItem);
List<EditEvent<String>> events = new ArrayList<>();
tree.setOnEditCancel(events::add);
tree.setOnEditCommit(events::add);
tree.setOnEditStart(events::add);
cell.updateIndex(cellIndex);
assertEquals(editingItem, tree.getEditingItem());
assertFalse(cell.isEditing());
assertEquals(0, events.size());
}
@Before public void setup() {
cell = new TreeCell<String>();
model = FXCollections.observableArrayList(new TreeItem<String>("zero"),
new TreeItem<String>("one"), new TreeItem<String>("two"));
TreeItem<String> root = new TreeItem<>("root");
root.getChildren().addAll(model);
root.setExpanded(true);
tree = new TreeView<String>(root);
tree.setEditable(true);
tree.setShowRoot(false);
tree.getFocusModel().focus(-1);
cell.updateTreeView(tree);
assertFalse("sanity: cellIndex not same as editingIndex", cellIndex == editingIndex);
assertTrue("sanity: valid editingIndex", editingIndex < model.size());
}
}
