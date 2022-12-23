package javafx.scene.control.skin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.javafx.scene.control.Properties;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
public class ButtonBarSkin extends SkinBase<ButtonBar> {
private static final double GAP_SIZE = 10;
private static final String CATEGORIZED_TYPES = "LRHEYNXBIACO";
private static final double DO_NOT_CHANGE_SIZE = Double.MAX_VALUE - 100;
private HBox layout;
private InvalidationListener buttonDataListener = o -> layoutButtons();
public ButtonBarSkin(final ButtonBar control) {
super(control);
this.layout = new HBox(GAP_SIZE) {
@Override protected void layoutChildren() {
resizeButtons();
super.layoutChildren();
}
};
this.layout.setAlignment(Pos.CENTER);
this.layout.getStyleClass().add("container");
getChildren().add(layout);
layoutButtons();
updateButtonListeners(control.getButtons(), true);
control.getButtons().addListener((ListChangeListener<Node>) c -> {
while (c.next()) {
updateButtonListeners(c.getRemoved(), false);
updateButtonListeners(c.getAddedSubList(), true);
}
layoutButtons();
});
registerChangeListener(control.buttonOrderProperty(), e -> layoutButtons());
registerChangeListener(control.buttonMinWidthProperty(), e -> resizeButtons());
}
private void updateButtonListeners(List<? extends Node> list, boolean buttonsAdded) {
if (list != null) {
for (Node n : list) {
final Map<Object, Object> properties = n.getProperties();
if (properties.containsKey(Properties.BUTTON_DATA_PROPERTY)) {
ObjectProperty<ButtonData> property = (ObjectProperty<ButtonData>) properties.get(Properties.BUTTON_DATA_PROPERTY);
if (property != null) {
if (buttonsAdded) {
property.addListener(buttonDataListener);
} else {
property.removeListener(buttonDataListener);
}
}
}
}
}
}
private void layoutButtons() {
final ButtonBar buttonBar = getSkinnable();
final List<? extends Node> buttons = buttonBar.getButtons();
final double buttonMinWidth = buttonBar.getButtonMinWidth();
String buttonOrder = getSkinnable().getButtonOrder();
layout.getChildren().clear();
if (buttonOrder == null) {
throw new IllegalStateException("ButtonBar buttonOrder string can not be null");
}
if (buttonOrder.equals(ButtonBar.BUTTON_ORDER_NONE)) {
Spacer.DYNAMIC.add(layout, true);
for (Node btn: buttons) {
sizeButton(btn, buttonMinWidth, DO_NOT_CHANGE_SIZE, Double.MAX_VALUE);
layout.getChildren().add(btn);
HBox.setHgrow(btn, Priority.NEVER);
}
} else {
doButtonOrderLayout(buttonOrder);
}
}
private void doButtonOrderLayout(String buttonOrder) {
final ButtonBar buttonBar = getSkinnable();
final List<? extends Node> buttons = buttonBar.getButtons();
final double buttonMinWidth = buttonBar.getButtonMinWidth();
Map<String, List<Node>> buttonMap = buildButtonMap(buttons);
char[] buttonOrderArr = buttonOrder.toCharArray();
int buttonIndex = 0;
Spacer spacer = Spacer.NONE;
for (int i = 0; i < buttonOrderArr.length; i++) {
char type = buttonOrderArr[i];
boolean edgeCase = buttonIndex <= 0 && buttonIndex >= buttons.size()-1;
boolean hasChildren = ! layout.getChildren().isEmpty();
if (type == '+') {
spacer = spacer.replace(Spacer.DYNAMIC);
} else if (type == '_' && hasChildren) {
spacer = spacer.replace(Spacer.FIXED);
} else {
List<Node> buttonList = buttonMap.get(String.valueOf(type).toUpperCase());
if (buttonList != null) {
spacer.add(layout,edgeCase);
for (Node btn: buttonList) {
sizeButton(btn, buttonMinWidth, DO_NOT_CHANGE_SIZE, Double.MAX_VALUE);
layout.getChildren().add(btn);
HBox.setHgrow(btn, Priority.NEVER);
buttonIndex++;
}
spacer = spacer.replace(Spacer.NONE);
}
}
}
boolean isDefaultSet = false;
final int childrenCount = buttons.size();
for (int i = 0; i < childrenCount; i++) {
Node btn = buttons.get(i);
if (btn instanceof Button && ((Button) btn).isDefaultButton()) {
btn.requestFocus();
isDefaultSet = true;
break;
}
}
if (!isDefaultSet) {
for (int i = 0; i < childrenCount; i++) {
Node btn = buttons.get(i);
ButtonData btnData = ButtonBar.getButtonData(btn);
if (btnData != null && btnData.isDefaultButton()) {
btn.requestFocus();
isDefaultSet = true;
break;
}
}
}
}
private void resizeButtons() {
final ButtonBar buttonBar = getSkinnable();
double buttonMinWidth = buttonBar.getButtonMinWidth();
final List<? extends Node> buttons = buttonBar.getButtons();
double widest = buttonMinWidth;
for (Node button : buttons) {
if (ButtonBar.isButtonUniformSize(button)) {
widest = Math.max(button.prefWidth(-1), widest);
}
}
for (Node button : buttons) {
if (ButtonBar.isButtonUniformSize(button)) {
sizeButton(button, DO_NOT_CHANGE_SIZE, widest, DO_NOT_CHANGE_SIZE);
}
}
}
private void sizeButton(Node btn, double min, double pref, double max) {
if (btn instanceof Region) {
Region regionBtn = (Region)btn;
if (min != DO_NOT_CHANGE_SIZE) {
regionBtn.setMinWidth(min);
}
if (pref != DO_NOT_CHANGE_SIZE) {
regionBtn.setPrefWidth(pref);
}
if (max != DO_NOT_CHANGE_SIZE) {
regionBtn.setMaxWidth(max);
}
}
}
private String getButtonType(Node btn) {
ButtonData buttonType = ButtonBar.getButtonData(btn);
if (buttonType == null) {
buttonType = ButtonData.OTHER;
}
String typeCode = buttonType.getTypeCode();
typeCode = typeCode.length() > 0? typeCode.substring(0,1): "";
return CATEGORIZED_TYPES.contains(typeCode.toUpperCase())? typeCode : ButtonData.OTHER.getTypeCode();
}
private Map<String, List<Node>> buildButtonMap( List<? extends Node> buttons ) {
Map<String, List<Node>> buttonMap = new HashMap<>();
for (Node btn : buttons) {
if ( btn == null ) continue;
String type = getButtonType(btn);
List<Node> typedButtons = buttonMap.get(type);
if ( typedButtons == null ) {
typedButtons = new ArrayList<Node>();
buttonMap.put(type, typedButtons);
}
typedButtons.add( btn );
}
return buttonMap;
}
private enum Spacer {
FIXED {
@Override protected Node create(boolean edgeCase) {
if ( edgeCase ) return null;
Region spacer = new Region();
ButtonBar.setButtonData(spacer, ButtonData.SMALL_GAP);
spacer.setMinWidth(GAP_SIZE);
HBox.setHgrow(spacer, Priority.NEVER);
return spacer;
}
},
DYNAMIC {
@Override protected Node create(boolean edgeCase) {
Region spacer = new Region();
ButtonBar.setButtonData(spacer, ButtonData.BIG_GAP);
spacer.setMinWidth(edgeCase ? 0 : GAP_SIZE);
HBox.setHgrow(spacer, Priority.ALWAYS);
return spacer;
}
@Override public Spacer replace(Spacer spacer) {
return FIXED == spacer? this: spacer;
}
},
NONE;
protected Node create(boolean edgeCase) {
return null;
}
public Spacer replace(Spacer spacer) {
return spacer;
}
public void add(Pane pane, boolean edgeCase) {
Node spacer = create(edgeCase);
if (spacer != null) {
pane.getChildren().add(spacer);
}
}
}
}