package com.sun.javafx.scene.control.behavior;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.Properties;
import com.sun.javafx.scene.control.skin.FXVK;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.application.ConditionalFeature;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Clipboard;
import com.sun.javafx.scene.control.inputmap.InputMap;
import com.sun.javafx.scene.control.inputmap.InputMap.Mapping;
import com.sun.javafx.scene.control.inputmap.KeyBinding;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.text.Bidi;
import java.util.function.Predicate;
import static com.sun.javafx.PlatformUtil.isLinux;
import static com.sun.javafx.PlatformUtil.isMac;
import static com.sun.javafx.PlatformUtil.isWindows;
import static com.sun.javafx.scene.control.inputmap.KeyBinding.OptionalBoolean;
import static com.sun.javafx.scene.control.skin.resources.ControlResources.getString;
import static javafx.scene.control.skin.TextInputControlSkin.TextUnit;
import static javafx.scene.control.skin.TextInputControlSkin.Direction;
import static com.sun.javafx.scene.control.inputmap.InputMap.KeyMapping;
import static com.sun.javafx.scene.control.inputmap.InputMap.MouseMapping;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.*;
public abstract class TextInputControlBehavior<T extends TextInputControl> extends BehaviorBase<T> {
static final boolean SHOW_HANDLES = Properties.IS_TOUCH_SUPPORTED;
public static final String DISABLE_FORWARD_TO_PARENT = "TextInputControlBehavior.disableForwardToParent";
final T textInputControl;
protected ContextMenu contextMenu;
private InvalidationListener textListener = observable -> invalidateBidi();
private final InputMap<T> inputMap;
public TextInputControlBehavior(T c) {
super(c);
this.textInputControl = c;
inputMap = createInputMap();
final Predicate<KeyEvent> validWhenEditable = e -> !c.isEditable();
final Predicate<KeyEvent> validOnWindows = e -> !PlatformUtil.isWindows();
final Predicate<KeyEvent> validOnLinux = e -> !PlatformUtil.isLinux();
KeyMapping cancelEditMapping;
KeyMapping fireMapping;
KeyMapping consumeMostPressedEventsMapping;
addDefaultMapping(inputMap,
keyMapping(RIGHT, e -> nextCharacterVisually(true)),
keyMapping(LEFT, e -> nextCharacterVisually(false)),
keyMapping(UP, e -> c.home()),
keyMapping(HOME, e -> c.home()),
keyMapping(DOWN, e -> c.end()),
keyMapping(END, e -> c.end()),
fireMapping = keyMapping(ENTER, this::fire),
keyMapping(new KeyBinding(HOME).shortcut(), e -> c.home()),
keyMapping(new KeyBinding(END).shortcut(), e -> c.end()),
keyMapping(new KeyBinding(BACK_SPACE), e -> deletePreviousChar(), validWhenEditable),
keyMapping(new KeyBinding(BACK_SPACE).shift(), e -> deletePreviousChar(), validWhenEditable),
keyMapping(new KeyBinding(DELETE), e -> deleteNextChar(), validWhenEditable),
keyMapping(new KeyBinding(X).shortcut(), e -> cut(), validWhenEditable),
keyMapping(new KeyBinding(CUT), e -> cut(), validWhenEditable),
keyMapping(new KeyBinding(C).shortcut(), e -> c.copy()),
keyMapping(new KeyBinding(INSERT).shortcut(), e -> c.copy()),
keyMapping(COPY, e -> c.copy()),
keyMapping(new KeyBinding(V).shortcut(), e -> paste(), validWhenEditable),
keyMapping(new KeyBinding(PASTE), e -> paste(), validWhenEditable),
keyMapping(new KeyBinding(INSERT).shift(), e -> paste(), validWhenEditable),
keyMapping(new KeyBinding(RIGHT).shift(), e -> selectRight()),
keyMapping(new KeyBinding(LEFT).shift(), e -> selectLeft()),
keyMapping(new KeyBinding(UP).shift(), e -> selectHome()),
keyMapping(new KeyBinding(DOWN).shift(), e -> selectEnd()),
keyMapping(new KeyBinding(HOME).shortcut().shift(), e -> selectHome()),
keyMapping(new KeyBinding(END).shortcut().shift(), e -> selectEnd()),
keyMapping(new KeyBinding(A).shortcut(), e -> c.selectAll()),
new KeyMapping(new KeyBinding(TAB), FocusTraversalInputMap::traverseNext),
new KeyMapping(new KeyBinding(TAB).shift(), FocusTraversalInputMap::traversePrevious),
new KeyMapping(new KeyBinding(TAB).ctrl(), FocusTraversalInputMap::traverseNext),
new KeyMapping(new KeyBinding(TAB).ctrl().shift(), FocusTraversalInputMap::traversePrevious),
cancelEditMapping = new KeyMapping(ESCAPE, this::cancelEdit),
keyMapping(new KeyBinding(Z).shortcut(), e -> undo()),
keyMapping(new KeyBinding(null, KEY_TYPED)
.alt(OptionalBoolean.ANY)
.shift(OptionalBoolean.ANY)
.ctrl(OptionalBoolean.ANY)
.meta(OptionalBoolean.ANY),
this::defaultKeyTyped),
consumeMostPressedEventsMapping =
keyMapping(new KeyBinding(null, KEY_PRESSED).shift(OptionalBoolean.ANY),
e -> { if (!e.getCode().isFunctionKey()) e.consume(); }),
new KeyMapping(new KeyBinding(DIGIT9).ctrl().shift(), e -> {
FXVK.toggleUseVK(textInputControl);
}, p -> !PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)),
new MouseMapping(MouseEvent.MOUSE_PRESSED, this::mousePressed),
new MouseMapping(MouseEvent.MOUSE_DRAGGED, this::mouseDragged),
new MouseMapping(MouseEvent.MOUSE_RELEASED, this::mouseReleased),
new InputMap.Mapping<ContextMenuEvent>(ContextMenuEvent.CONTEXT_MENU_REQUESTED, this::contextMenuRequested) {
@Override public int getSpecificity(Event event) {
return 1;
}
}
);
cancelEditMapping.setAutoConsume(false);
fireMapping.setAutoConsume(false);
consumeMostPressedEventsMapping.setAutoConsume(false);
InputMap<T> macOsInputMap = new InputMap<>(c);
macOsInputMap.setInterceptor(e -> !PlatformUtil.isMac());
macOsInputMap.getMappings().addAll(
keyMapping(new KeyBinding(HOME).shift(), e -> selectHomeExtend()),
keyMapping(new KeyBinding(END).shift(), e -> selectEndExtend()),
keyMapping(new KeyBinding(LEFT).shortcut(), e -> c.home()),
keyMapping(new KeyBinding(RIGHT).shortcut(), e -> c.end()),
keyMapping(new KeyBinding(LEFT).alt(), e -> leftWord()),
keyMapping(new KeyBinding(RIGHT).alt(), e -> rightWord()),
keyMapping(new KeyBinding(DELETE).alt(), e -> deleteNextWord()),
keyMapping(new KeyBinding(BACK_SPACE).alt(), e -> deletePreviousWord()),
keyMapping(new KeyBinding(BACK_SPACE).shortcut(), e -> deleteFromLineStart()),
keyMapping(new KeyBinding(Z).shortcut().shift(), e -> redo()),
keyMapping(new KeyBinding(LEFT).shortcut().shift(), e -> selectHomeExtend()),
keyMapping(new KeyBinding(RIGHT).shortcut().shift(), e -> selectEndExtend()),
keyMapping(new KeyBinding(LEFT).shift().alt(), e -> selectLeftWord()),
keyMapping(new KeyBinding(RIGHT).shift().alt(), e -> selectRightWord())
);
addDefaultChildMap(inputMap, macOsInputMap);
InputMap<T> nonMacOsInputMap = new InputMap<>(c);
nonMacOsInputMap.setInterceptor(e -> PlatformUtil.isMac());
nonMacOsInputMap.getMappings().addAll(
keyMapping(new KeyBinding(HOME).shift(), e -> selectHome()),
keyMapping(new KeyBinding(END).shift(), e -> selectEnd()),
keyMapping(new KeyBinding(LEFT).ctrl(), e -> leftWord()),
keyMapping(new KeyBinding(RIGHT).ctrl(), e -> rightWord()),
keyMapping(new KeyBinding(H).ctrl(), e -> deletePreviousChar()),
keyMapping(new KeyBinding(DELETE).ctrl(), e -> deleteNextWord()),
keyMapping(new KeyBinding(BACK_SPACE).ctrl(), e -> deletePreviousWord()),
keyMapping(new KeyBinding(BACK_SLASH).ctrl(), e -> c.deselect()),
keyMapping(new KeyBinding(Y).ctrl(), e -> redo(), validOnWindows),
keyMapping(new KeyBinding(Z).ctrl().shift(), e -> redo(), validOnLinux),
keyMapping(new KeyBinding(LEFT).ctrl().shift(), e -> selectLeftWord()),
keyMapping(new KeyBinding(RIGHT).ctrl().shift(), e -> selectRightWord())
);
addDefaultChildMap(inputMap, nonMacOsInputMap);
addKeyPadMappings(inputMap);
textInputControl.textProperty().addListener(textListener);
contextMenu = new ContextMenu();
}
@Override public InputMap<T> getInputMap() {
return inputMap;
}
protected void addKeyPadMappings(InputMap<T> map) {
InputMap<T> tmpMap = new InputMap<>(getNode());
for (Object o : map.getMappings()) {
if (o instanceof KeyMapping) {
KeyMapping mapping = (KeyMapping)o;
KeyBinding kb = (KeyBinding)mapping.getMappingKey();
if (kb.getCode() != null) {
KeyCode newCode = null;
switch (kb.getCode()) {
case LEFT: newCode = KP_LEFT; break;
case RIGHT: newCode = KP_RIGHT; break;
case UP: newCode = KP_UP; break;
case DOWN: newCode = KP_DOWN; break;
default:
}
if (newCode != null) {
KeyBinding newkb = new KeyBinding(newCode).shift(kb.getShift())
.ctrl(kb.getCtrl())
.alt(kb.getAlt())
.meta(kb.getMeta());
tmpMap.getMappings().add(new KeyMapping(newkb, mapping.getEventHandler()));
}
}
}
}
if (map == getInputMap()) {
for (Mapping<?> mapping : tmpMap.getMappings()) {
addDefaultMapping(map, mapping);
}
} else {
for (Object o : tmpMap.getMappings()) {
map.getMappings().add((KeyMapping)o);
}
}
tmpMap.dispose();
for (Object o : map.getChildInputMaps()) {
addKeyPadMappings((InputMap<T>)o);
}
}
protected KeyMapping keyMapping(final KeyCode keyCode, final EventHandler<KeyEvent> eventHandler) {
return keyMapping(new KeyBinding(keyCode), eventHandler);
}
protected KeyMapping keyMapping(KeyBinding keyBinding, final EventHandler<KeyEvent> eventHandler) {
return keyMapping(keyBinding, eventHandler, null);
}
protected KeyMapping keyMapping(KeyBinding keyBinding, final EventHandler<KeyEvent> eventHandler,
Predicate<KeyEvent> interceptor) {
return new KeyMapping(keyBinding,
e -> {
setCaretAnimating(false);
eventHandler.handle(e);
setCaretAnimating(true);
},
interceptor);
}
@Override public void dispose() {
textInputControl.textProperty().removeListener(textListener);
super.dispose();
}
protected abstract void deleteChar(boolean previous);
protected abstract void replaceText(int start, int end, String txt);
protected abstract void setCaretAnimating(boolean play);
protected abstract void deleteFromLineStart();
protected abstract void mousePressed(MouseEvent e);
protected abstract void mouseDragged(MouseEvent e);
protected abstract void mouseReleased(MouseEvent e);
protected abstract void contextMenuRequested(ContextMenuEvent e);
private void defaultKeyTyped(KeyEvent event) {
final TextInputControl textInput = getNode();
if (!textInput.isEditable() || textInput.isDisabled()) return;
String character = event.getCharacter();
if (character.length() == 0) return;
if (event.isControlDown() || event.isAltDown() || (isMac() && event.isMetaDown())) {
if (!((event.isControlDown() || isMac()) && event.isAltDown())) return;
}
setEditing(true);
if (character.charAt(0) > 0x1F
&& character.charAt(0) != 0x7F
&& !event.isMetaDown()) {
final IndexRange selection = textInput.getSelection();
final int start = selection.getStart();
final int end = selection.getEnd();
replaceText(start, end, character);
}
setEditing(false);
}
private Bidi bidi = null;
private Boolean mixed = null;
private Boolean rtlText = null;
Bidi getRawBidi() {
return bidi;
}
private void invalidateBidi() {
bidi = null;
mixed = null;
rtlText = null;
}
private Bidi getBidi() {
if (bidi == null) {
bidi = new Bidi(textInputControl.textProperty().getValueSafe(),
(textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT)
? Bidi.DIRECTION_RIGHT_TO_LEFT
: Bidi.DIRECTION_LEFT_TO_RIGHT);
}
return bidi;
}
protected boolean isMixed() {
if (mixed == null) {
mixed = getBidi().isMixed();
}
return mixed;
}
protected boolean isRTLText() {
if (rtlText == null) {
Bidi bidi = getBidi();
rtlText =
(bidi.isRightToLeft() ||
(isMixed() &&
textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT));
}
return rtlText;
}
private void nextCharacterVisually(boolean moveRight) {
if (isMixed()) {
TextInputControlSkin<?> skin = (TextInputControlSkin<?>)textInputControl.getSkin();
skin.moveCaret(TextUnit.CHARACTER, moveRight ? Direction.RIGHT : Direction.LEFT, false);
} else if (moveRight != isRTLText()) {
textInputControl.forward();
} else {
textInputControl.backward();
}
}
private void selectLeft() {
if (isRTLText()) {
textInputControl.selectForward();
} else {
textInputControl.selectBackward();
}
}
private void selectRight() {
if (isRTLText()) {
textInputControl.selectBackward();
} else {
textInputControl.selectForward();
}
}
private void deletePreviousChar() {
setEditing(true);
deleteChar(true);
setEditing(false);
}
private void deleteNextChar() {
setEditing(true);
deleteChar(false);
setEditing(false);
}
protected void deletePreviousWord() {
setEditing(true);
TextInputControl textInputControl = getNode();
int end = textInputControl.getCaretPosition();
if (end > 0) {
textInputControl.previousWord();
int start = textInputControl.getCaretPosition();
replaceText(start, end, "");
}
setEditing(false);
}
protected void deleteNextWord() {
setEditing(true);
TextInputControl textInputControl = getNode();
int start = textInputControl.getCaretPosition();
if (start < textInputControl.getLength()) {
nextWord();
int end = textInputControl.getCaretPosition();
replaceText(start, end, "");
}
setEditing(false);
}
public void deleteSelection() {
setEditing(true);
TextInputControl textInputControl = getNode();
IndexRange selection = textInputControl.getSelection();
if (selection.getLength() > 0) {
deleteChar(false);
}
setEditing(false);
}
public void cut() {
setEditing(true);
getNode().cut();
setEditing(false);
}
public void paste() {
setEditing(true);
getNode().paste();
setEditing(false);
}
public void undo() {
setEditing(true);
getNode().undo();
setEditing(false);
}
public void redo() {
setEditing(true);
getNode().redo();
setEditing(false);
}
protected void selectPreviousWord() {
getNode().selectPreviousWord();
}
public void selectNextWord() {
TextInputControl textInputControl = getNode();
if (isMac() || isLinux()) {
textInputControl.selectEndOfNextWord();
} else {
textInputControl.selectNextWord();
}
}
private void selectLeftWord() {
if (isRTLText()) {
selectNextWord();
} else {
selectPreviousWord();
}
}
private void selectRightWord() {
if (isRTLText()) {
selectPreviousWord();
} else {
selectNextWord();
}
}
protected void selectWord() {
final TextInputControl textInputControl = getNode();
textInputControl.previousWord();
if (isWindows()) {
textInputControl.selectNextWord();
} else {
textInputControl.selectEndOfNextWord();
}
if (SHOW_HANDLES && contextMenu.isShowing()) {
populateContextMenu();
}
}
protected void selectAll() {
getNode().selectAll();
if (SHOW_HANDLES && contextMenu.isShowing()) {
populateContextMenu();
}
}
protected void previousWord() {
getNode().previousWord();
}
protected void nextWord() {
TextInputControl textInputControl = getNode();
if (isMac() || isLinux()) {
textInputControl.endOfNextWord();
} else {
textInputControl.nextWord();
}
}
private void leftWord() {
if (isRTLText()) {
nextWord();
} else {
previousWord();
}
}
private void rightWord() {
if (isRTLText()) {
previousWord();
} else {
nextWord();
}
}
protected void fire(KeyEvent event) { }
protected void cancelEdit(KeyEvent event) { };
protected void selectHome() {
getNode().selectHome();
}
protected void selectEnd() {
getNode().selectEnd();
}
protected void selectHomeExtend() {
getNode().extendSelection(0);
}
protected void selectEndExtend() {
TextInputControl textInputControl = getNode();
textInputControl.extendSelection(textInputControl.getLength());
}
private boolean editing = false;
protected void setEditing(boolean b) {
editing = b;
}
public boolean isEditing() {
return editing;
}
protected void populateContextMenu() {
TextInputControl textInputControl = getNode();
boolean editable = textInputControl.isEditable();
boolean hasText = (textInputControl.getLength() > 0);
boolean hasSelection = (textInputControl.getSelection().getLength() > 0);
boolean allSelected = (textInputControl.getSelection().getLength() == textInputControl.getLength());
boolean maskText = (textInputControl instanceof PasswordField);
ObservableList<MenuItem> items = contextMenu.getItems();
if (SHOW_HANDLES) {
items.clear();
if (!maskText && hasSelection) {
if (editable) {
items.add(cutMI);
}
items.add(copyMI);
}
if (editable && Clipboard.getSystemClipboard().hasString()) {
items.add(pasteMI);
}
if (hasText && !allSelected) {
if (!hasSelection && !(textInputControl instanceof PasswordField)) {
items.add(selectWordMI);
}
items.add(selectAllMI);
}
selectWordMI.getProperties().put("refreshMenu", Boolean.TRUE);
selectAllMI.getProperties().put("refreshMenu", Boolean.TRUE);
} else {
if (editable) {
items.setAll(undoMI, redoMI, cutMI, copyMI, pasteMI, deleteMI,
separatorMI, selectAllMI);
} else {
items.setAll(copyMI, separatorMI, selectAllMI);
}
undoMI.setDisable(!getNode().isUndoable());
redoMI.setDisable(!getNode().isRedoable());
cutMI.setDisable(maskText || !hasSelection);
copyMI.setDisable(maskText || !hasSelection);
pasteMI.setDisable(!Clipboard.getSystemClipboard().hasString());
deleteMI.setDisable(!hasSelection);
}
}
private static class ContextMenuItem extends MenuItem {
ContextMenuItem(final String action, EventHandler<ActionEvent> onAction) {
super(getString("TextInputControl.menu." + action));
setOnAction(onAction);
}
}
private final MenuItem undoMI = new ContextMenuItem("Undo", e -> undo());
private final MenuItem redoMI = new ContextMenuItem("Redo", e -> redo());
private final MenuItem cutMI = new ContextMenuItem("Cut", e -> cut());
private final MenuItem copyMI = new ContextMenuItem("Copy", e -> getNode().copy());
private final MenuItem pasteMI = new ContextMenuItem("Paste", e -> paste());
private final MenuItem deleteMI = new ContextMenuItem("DeleteSelection", e -> deleteSelection());
private final MenuItem selectWordMI = new ContextMenuItem("SelectWord", e -> selectWord());
private final MenuItem selectAllMI = new ContextMenuItem("SelectAll", e -> selectAll());
private final MenuItem separatorMI = new SeparatorMenuItem();
}
