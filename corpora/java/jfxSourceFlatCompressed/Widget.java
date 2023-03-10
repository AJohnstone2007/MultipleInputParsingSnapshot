package test.javafx.fxml;
import com.sun.javafx.beans.IDProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
@IDProperty("id")
@DefaultProperty("children")
public class Widget {
private StringProperty id = new SimpleStringProperty();
private StringProperty name = new SimpleStringProperty();
private IntegerProperty number = new SimpleIntegerProperty();
private ObservableList<Widget> children = FXCollections.observableArrayList();
private ObservableSet<String> set = FXCollections.observableSet();
private ObservableMap<String, Object> properties = FXCollections.observableHashMap();
private BooleanProperty enabledProperty = new SimpleBooleanProperty(true);
private ArrayList<String> styles = new ArrayList<String>();
private ArrayList<String> values = new ArrayList<String>();
private float[] ratios = new float[]{};
private String[] names = new String[]{};
public static final String ALIGNMENT_KEY = "alignment";
public static final int TEN = 10;
private EventHandler<ActionEvent> actionHandler;
public Widget() {
this(null);
}
public Widget(String name) {
setName(name);
}
public String getId() {
return id.get();
}
public void setId(String value) {
id.set(value);
}
public StringProperty idProperty() {
return id;
}
public String getName() {
return name.get();
}
public void setName(String value) {
name.set(value);
}
public StringProperty nameProperty() {
return name;
}
public int getNumber() {
return number.get();
}
public void setNumber(int value) {
number.set(value);
}
public IntegerProperty numberProperty() {
return number;
}
public ObservableList<Widget> getChildren() {
return children;
}
public ObservableMap<String, Object> getProperties() {
return properties;
}
public boolean isEnabled() {
return enabledProperty.get();
}
public void setEnabled(boolean value) {
enabledProperty.set(value);
}
public BooleanProperty enabledProperty() {
return enabledProperty;
}
public List<String> getStyles() {
return styles;
}
public List<String> getValues() {
return values;
}
public void setValues(List<String> values) {
if (values == null) {
throw new IllegalArgumentException();
}
this.values = new ArrayList<String>();
this.values.addAll(values);
}
public float[] getRatios() {
return Arrays.copyOf(ratios, ratios.length);
}
public void setRatios(float[] ratios) {
this.ratios = Arrays.copyOf(ratios, ratios.length);
}
public String[] getNames() {
return Arrays.copyOf(names, names.length);
}
public void setNames(String[] names) {
this.names = Arrays.copyOf(names, names.length);
}
public ObservableSet<String> getSet() {
return set;
}
public static Alignment getAlignment(Widget widget) {
return (Alignment)widget.getProperties().get(ALIGNMENT_KEY);
}
public static void setAlignment(Widget widget, Alignment alignment) {
widget.getProperties().put(ALIGNMENT_KEY, alignment);
}
public final void setOnAction(EventHandler<ActionEvent> value) {
actionHandler = value;
}
public final EventHandler<ActionEvent> getOnAction() {
return actionHandler;
}
public final void fire() {
actionHandler.handle(new ActionEvent());
}
}
