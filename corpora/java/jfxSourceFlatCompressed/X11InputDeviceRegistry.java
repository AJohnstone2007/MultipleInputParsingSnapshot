package com.sun.glass.ui.monocle;
import com.sun.glass.events.MouseEvent;
class X11InputDeviceRegistry extends InputDeviceRegistry {
private MouseState state;
private static X xLib = X.getX();
X11InputDeviceRegistry() {
InputDevice device = new InputDevice() {
@Override
public boolean isTouch() {
return false;
}
@Override
public boolean isMultiTouch() {
return false;
}
@Override
public boolean isRelative() {
return true;
}
@Override
public boolean is5Way() {
return false;
}
@Override
public boolean isFullKeyboard() {
return false;
}
};
Thread x11InputThread = new Thread(() -> {
NativePlatform platform =
NativePlatformFactory.getNativePlatform();
X11Screen screen = (X11Screen) platform.getScreen();
long display = screen.getDisplay();
long window = screen.getNativeHandle();
RunnableProcessor runnableProcessor =
platform.getRunnableProcessor();
runnableProcessor.invokeLater(() -> {
devices.add(device);
});
state = new MouseState();
X.XEvent event = new X.XEvent();
while (true) {
xLib.XNextEvent(display, event.p);
if (X.XEvent.getWindow(event.p) != window) {
continue;
}
processXEvent(event, runnableProcessor);
}
});
x11InputThread.setName("X11 Input");
x11InputThread.setDaemon(true);
x11InputThread.start();
}
private void processXEvent(X.XEvent event,
RunnableProcessor runnableProcessor) {
switch (X.XEvent.getType(event.p)) {
case X.ButtonPress: {
X.XButtonEvent buttonEvent = new X.XButtonEvent(event);
int button = X.XButtonEvent.getButton(buttonEvent.p);
runnableProcessor.invokeLater(new ButtonPressProcessor(button));
break;
}
case X.ButtonRelease: {
X.XButtonEvent buttonEvent = new X.XButtonEvent(event);
int button = X.XButtonEvent.getButton(buttonEvent.p);
runnableProcessor.invokeLater(
new ButtonReleaseProcessor(button));
break;
}
case X.MotionNotify: {
X.XMotionEvent motionEvent = new X.XMotionEvent(event);
int x = X.XMotionEvent.getX(motionEvent.p);
int y = X.XMotionEvent.getY(motionEvent.p);
runnableProcessor.invokeLater(new MotionProcessor(x, y));
break;
}
}
}
private class ButtonPressProcessor implements Runnable {
private int button;
ButtonPressProcessor(int button) {
this.button = button;
}
@Override
public void run() {
MouseInput.getInstance().getState(state);
int glassButton = buttonToGlassButton(button);
if (glassButton != MouseEvent.BUTTON_NONE) {
state.pressButton(glassButton);
}
MouseInput.getInstance().setState(state, false);
}
}
private class ButtonReleaseProcessor implements Runnable {
private int button;
ButtonReleaseProcessor(int button) {
this.button = button;
}
@Override
public void run() {
MouseInput.getInstance().getState(state);
int glassButton = buttonToGlassButton(button);
if (glassButton != MouseEvent.BUTTON_NONE) {
state.releaseButton(glassButton);
}
MouseInput.getInstance().setState(state, false);
}
}
private class MotionProcessor implements Runnable {
private int x;
private int y;
MotionProcessor(int x, int y) {
this.x = x;
this.y = y;
}
@Override
public void run() {
MouseInput.getInstance().getState(state);
state.setX(x);
state.setY(y);
MouseInput.getInstance().setState(state, false);
}
}
private static int buttonToGlassButton(int button) {
switch (button) {
case X.Button1: return MouseEvent.BUTTON_LEFT;
case X.Button2: return MouseEvent.BUTTON_OTHER;
case X.Button3: return MouseEvent.BUTTON_RIGHT;
case X.Button8: return MouseEvent.BUTTON_BACK;
case X.Button9: return MouseEvent.BUTTON_FORWARD;
default: return MouseEvent.BUTTON_NONE;
}
}
}
