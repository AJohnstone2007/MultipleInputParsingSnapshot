package test.javafx.scene.control.cell;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.junit.Before;
import org.junit.Test;
public class CheckBoxTreeCellTest {
private SimpleBooleanProperty booleanProperty;
private Callback<TreeItem<Object>, ObservableValue<Boolean>> callback;
private StringConverter<TreeItem<Object>> converter;
@Before public void setup() {
booleanProperty = new SimpleBooleanProperty(false);
callback = param -> booleanProperty;
converter = new StringConverter<TreeItem<Object>>() {
@Override public String toString(TreeItem<Object> object) {
return null;
}
@Override public TreeItem<Object> fromString(String string) {
return null;
}
};
}
@Test public void testStatic_forTreeView_callback_ensureCellFactoryIsNotNull() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView();
assertNotNull(cellFactory);
}
@Test public void testStatic_forTreeView_callback_ensureCellFactoryCreatesCells() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView();
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell);
}
@Test public void testStatic_forTreeView_callback_ensureCellHasNonNullSelectedStateCallback() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView();
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getSelectedStateCallback());
}
@Test public void testStatic_forTreeView_callback_ensureCellHasNonNullStringConverter() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView();
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getConverter());
}
@Test public void testStatic_forTreeView_callback1_ensureCellFactoryIsNotNull() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback);
assertNotNull(cellFactory);
}
@Test public void testStatic_forTreeView_callback1_ensureCellFactoryCreatesCells() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell);
}
@Test public void testStatic_forTreeView_callback1_ensureCellHasNonNullSelectedStateCallback() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getSelectedStateCallback());
}
@Test public void testStatic_forTreeView_callback1_ensureCellHasNonNullStringConverter() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getConverter());
}
@Test public void testStatic_forTreeView_callback_2_ensureCellFactoryIsNotNull() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback, converter);
assertNotNull(cellFactory);
}
@Test public void testStatic_forTreeView_callback_2_ensureCellFactoryCreatesCells() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback, converter);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell);
}
@Test public void testStatic_forTreeView_callback_2_ensureCellHasNonNullSelectedStateCallback() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback, converter);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getSelectedStateCallback());
}
@Test public void testStatic_forTreeView_callback_2_ensureCellHasSetStringConverter() {
assertFalse(booleanProperty.get());
Callback<TreeView<Object>, TreeCell<Object>> cellFactory = CheckBoxTreeCell.forTreeView(callback, converter);
TreeView<Object> treeView = new TreeView<>();
CheckBoxTreeCell<Object> cell = (CheckBoxTreeCell<Object>)cellFactory.call(treeView);
assertNotNull(cell.getConverter());
assertEquals(converter, cell.getConverter());
}
@Test public void testConstructor_noArgs_defaultCallbackIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
assertNotNull(cell.getSelectedStateCallback());
}
@Test public void testConstructor_noArgs_defaultStringConverterIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
assertNotNull(cell.getConverter());
}
@Test public void testConstructor_noArgs_defaultStyleClass() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
assertTrue(cell.getStyleClass().contains("check-box-tree-cell"));
}
@Test public void testConstructor_noArgs_defaultGraphicIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
assertNull(cell.getGraphic());
}
@Test public void testConstructor_getSelectedProperty_selectedPropertyIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
assertEquals(callback, cell.getSelectedStateCallback());
}
@Test public void testConstructor_getSelectedProperty_defaultStringConverterIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
assertNotNull(cell.getConverter());
}
@Test public void testConstructor_getSelectedProperty_defaultStyleClass() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
assertTrue(cell.getStyleClass().contains("check-box-tree-cell"));
}
@Test public void testConstructor_getSelectedProperty_defaultGraphicIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
assertNull(cell.getGraphic());
}
@Test public void testConstructor_getSelectedProperty_converter_selectedPropertyIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback, converter);
assertEquals(callback, cell.getSelectedStateCallback());
}
@Test public void testConstructor_getSelectedProperty_converter_defaultStringConverterIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback, converter);
assertNotNull(cell.getConverter());
assertEquals(converter, cell.getConverter());
}
@Test public void testConstructor_getSelectedProperty_converter_defaultStyleClass() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback, converter);
assertTrue(cell.getStyleClass().contains("check-box-tree-cell"));
}
@Test public void testConstructor_getSelectedProperty_converter_defaultGraphicIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback, converter);
assertNull(cell.getGraphic());
}
@Test(expected=NullPointerException.class)
public void test_getSelectedPropertyIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(null);
cell.updateItem("TEST", false);
}
@Test public void test_updateItem_isEmpty_graphicIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
cell.updateItem("TEST", true);
assertNull(cell.getGraphic());
}
@Test public void test_updateItem_isEmpty_textIsNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>();
cell.updateItem("TEST", true);
assertNull(cell.getText());
}
@Test public void test_updateItem_isNotEmpty_graphicIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
cell.updateItem("TEST", false);
assertNotNull(cell.getGraphic());
assertTrue(cell.getGraphic() instanceof CheckBox);
}
@Test public void test_updateItem_isNotEmpty_textIsNotNull() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
TreeItem<Object> treeItem = new TreeItem<Object>("TREEITEM TEST");
cell.updateTreeItem(treeItem);
cell.updateItem("TEST", false);
assertNotNull(cell.getText());
assertEquals("TREEITEM TEST", cell.getText());
}
@Test public void test_updateItem_isNotEmpty_textIsNotNull_nullConverter() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
cell.setConverter(null);
TreeItem<Object> treeItem = new TreeItem<Object>("TREEITEM TEST");
cell.updateTreeItem(treeItem);
cell.updateItem("TEST", false);
assertNotNull(cell.getText());
assertEquals(treeItem.toString(), cell.getText());
}
@Test public void test_updateItem_isNotEmpty_textIsNotNull_nonNullConverter() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
cell.setConverter(new StringConverter<TreeItem<Object>>() {
@Override public TreeItem<Object> fromString(String string) {
return null;
}
@Override public String toString(TreeItem<Object> object) {
return "CONVERTED";
}
});
cell.updateItem("TEST", false);
assertNotNull(cell.getText());
assertEquals("CONVERTED", cell.getText());
}
@Test public void test_booleanPropertyChangeUpdatesCheckBoxSelection() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
cell.updateItem("TEST", false);
CheckBox cb = (CheckBox)cell.getGraphic();
assertFalse(cb.isSelected());
booleanProperty.set(true);
assertTrue(cb.isScaleShape());
booleanProperty.set(false);
assertFalse(cb.isSelected());
}
@Test public void test_checkBoxSelectionUpdatesBooleanProperty() {
CheckBoxTreeCell<Object> cell = new CheckBoxTreeCell<>(callback);
cell.updateItem("TEST", false);
CheckBox cb = (CheckBox)cell.getGraphic();
assertFalse(booleanProperty.get());
cb.setSelected(true);
assertTrue(booleanProperty.get());
cb.setSelected(false);
assertFalse(booleanProperty.get());
}
}
