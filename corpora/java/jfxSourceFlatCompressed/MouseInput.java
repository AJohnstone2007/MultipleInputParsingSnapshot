package com.sun.glass.ui.monocle;
import com.sun.glass.events.MouseEvent;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import java.util.BitSet;
class MouseInput {
private static MouseInput instance = new MouseInput();
private MouseState state = new MouseState();
private IntSet buttons = new IntSet();
private boolean dragInProgress = false;
private int dragButton = MouseEvent.BUTTON_NONE;
private MonocleView dragView = null;
private BitSet dragActions = new BitSet();
private static final int DRAG_ENTER = 1;
private static final int DRAG_LEAVE = 2;
private static final int DRAG_OVER = 3;
private static final int DRAG_DROP = 4;
static MouseInput getInstance() {
return instance;
}
void getState(MouseState result) {
state.copyTo(result);
}
void setState(MouseState newState, boolean synthesized) {
if (MonocleSettings.settings.traceEvents) {
MonocleTrace.traceEvent("Set %s", newState);
}
NativeScreen screen = NativePlatformFactory.getNativePlatform().getScreen();
int x = Math.max(0, Math.min(newState.getX(), screen.getWidth() - 1));
int y = Math.max(0, Math.min(newState.getY(), screen.getHeight() - 1));
newState.setX(x);
newState.setY(y);
MonocleWindow oldWindow = state.getWindow(false, null);
boolean recalculateWindow = state.getButtonsPressed().isEmpty();
MonocleWindow window = newState.getWindow(recalculateWindow, null);
MonocleView view = (window == null) ? null : (MonocleView) window.getView();
if (oldWindow != window && oldWindow != null) {
if (!oldWindow.isEnabled()) {
RunnableProcessor.runLater(() -> {
MonocleWindowManager.getInstance().notifyFocusDisabled(oldWindow);
});
} else {
MonocleView oldView = (MonocleView) oldWindow.getView();
if (oldView != null) {
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = state.getModifiers() | keyState.getModifiers();
int button = state.getButton();
boolean isPopupTrigger = false;
int oldX = state.getX();
int oldY = state.getY();
int oldRelX = oldX - oldWindow.getX();
int oldRelY = oldY - oldWindow.getY();
try {
postMouseEvent(oldView, MouseEvent.EXIT, button,
oldRelX, oldRelY, oldX, oldY,
modifiers, isPopupTrigger, synthesized);
} catch (RuntimeException e) {
Application.reportException(e);
}
}
}
}
boolean newAbsoluteLocation = state.getX() != x || state.getY() != y;
if (newAbsoluteLocation) {
NativePlatformFactory.getNativePlatform()
.getCursor().setLocation(x, y);
}
if (view == null) {
newState.copyTo(state);
return;
}
if (window != null && !window.isEnabled()) {
newState.copyTo(state);
RunnableProcessor.runLater(() -> {
MonocleWindowManager.getInstance().notifyFocusDisabled(window);
});
return;
}
int relX = x - window.getX();
int relY = y - window.getY();
if (oldWindow != window && view != null) {
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = state.getModifiers() | keyState.getModifiers();
int button = state.getButton();
boolean isPopupTrigger = false;
postMouseEvent(view, MouseEvent.ENTER, button,
relX, relY, x, y,
modifiers, isPopupTrigger, synthesized);
}
if (oldWindow != window | newAbsoluteLocation) {
boolean isDrag = !state.getButtonsPressed().isEmpty();
int eventType = isDrag ? MouseEvent.DRAG : MouseEvent.MOVE;
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = state.getModifiers() | keyState.getModifiers();
int button = state.getButton();
boolean isPopupTrigger = false;
postMouseEvent(view, eventType, button,
relX, relY, x, y,
modifiers, isPopupTrigger, synthesized);
}
newState.getButtonsPressed().difference(buttons, state.getButtonsPressed());
if (!buttons.isEmpty()) {
MouseState pressState = new MouseState();
state.copyTo(pressState);
for (int i = 0; i < buttons.size(); i++) {
int button = buttons.get(i);
pressState.pressButton(button);
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = pressState.getModifiers() | keyState.getModifiers();
boolean isPopupTrigger = false;
postMouseEvent(view, MouseEvent.DOWN, button,
relX, relY, x, y,
modifiers, isPopupTrigger,
synthesized);
}
}
buttons.clear();
state.getButtonsPressed().difference(buttons,
newState.getButtonsPressed());
if (!buttons.isEmpty()) {
MouseState releaseState = new MouseState();
state.copyTo(releaseState);
for (int i = 0; i < buttons.size(); i++) {
int button = buttons.get(i);
releaseState.releaseButton(button);
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = releaseState.getModifiers() | keyState.getModifiers();
boolean isPopupTrigger = false;
postMouseEvent(view, MouseEvent.UP, button,
relX, relY, x, y,
modifiers, isPopupTrigger,
synthesized);
}
}
buttons.clear();
if (newState.getWheel() != state.getWheel()) {
double dY;
switch (newState.getWheel()) {
case MouseState.WHEEL_DOWN: dY = -1.0; break;
case MouseState.WHEEL_UP: dY = 1.0; break;
default: dY = 0.0; break;
}
if (dY != 0.0) {
KeyState keyState = new KeyState();
KeyInput.getInstance().getState(keyState);
int modifiers = newState.getModifiers() | keyState.getModifiers();
RunnableProcessor.runLater(() -> {
view.notifyScroll(relX, relY, x, y, 0.0, dY,
modifiers, 1, 0, 0, 0, 1.0, 1.0);
});
}
newState.setWheel(MouseState.WHEEL_NONE);
}
newState.copyTo(state);
}
private void postMouseEvent(MonocleView view, int eventType, int button,
int relX, int relY, int x, int y,
int modifiers, boolean isPopupTrigger, boolean synthesized) {
RunnableProcessor.runLater(() -> {
notifyMouse(view, eventType, button,
relX, relY, x, y,
modifiers, isPopupTrigger, synthesized);
});
}
private void notifyMouse(MonocleView view, int eventType, int button,
int relX, int relY, int x, int y,
int modifiers, boolean isPopupTrigger, boolean synthesized) {
switch (eventType) {
case MouseEvent.DOWN: {
if (dragButton == MouseEvent.BUTTON_NONE) {
dragButton = button;
}
break;
}
case MouseEvent.UP: {
if (dragButton == button) {
dragButton = MouseEvent.BUTTON_NONE;
if (dragInProgress) {
try {
view.notifyDragDrop(relX, relY, x, y,
Clipboard.ACTION_MOVE);
} catch (RuntimeException e) {
Application.reportException(e);
}
try {
view.notifyDragEnd(Clipboard.ACTION_MOVE);
} catch (RuntimeException e) {
Application.reportException(e);
}
((MonocleApplication) Application.GetApplication()).leaveDndEventLoop();
dragActions.clear();
dragView = null;
dragInProgress = false;
}
}
break;
}
case MouseEvent.DRAG: {
if (dragButton != MouseEvent.BUTTON_NONE) {
if (dragInProgress) {
if (dragView == view && dragActions.isEmpty()) {
try {
view.notifyDragEnter(relX, relY, x, y,
Clipboard.ACTION_MOVE);
} catch (RuntimeException e) {
Application.reportException(e);
}
dragActions.set(DRAG_ENTER);
} else if (dragView == view && dragActions.get(DRAG_ENTER)) {
try {
view.notifyDragOver(relX, relY, x, y,
Clipboard.ACTION_MOVE);
} catch (RuntimeException e) {
Application.reportException(e);
}
dragActions.set(DRAG_OVER);
} else if (dragView != view) {
if (dragView != null) {
try {
dragView.notifyDragLeave();
} catch (RuntimeException e) {
Application.reportException(e);
}
}
try {
view.notifyDragEnter(relX, relY, x, y,
Clipboard.ACTION_MOVE);
} catch (RuntimeException e) {
Application.reportException(e);
}
dragActions.clear();
dragActions.set(DRAG_ENTER);
dragView = view;
}
return;
} else {
if (dragView == null) {
dragView = view;
}
}
}
break;
}
}
view.notifyMouse(eventType, button,
relX, relY, x, y,
modifiers, isPopupTrigger,
synthesized);
}
void notifyDragStart() {
dragInProgress = true;
}
}
