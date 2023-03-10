package com.sun.glass.ui.monocle;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import java.util.Map;
final class MonocleView extends View {
MonocleView() {
}
private boolean cursorVisibility;
private boolean resetCursorVisibility = false;
private static long multiClickTime = 500;
private static int multiClickMaxX = 20;
private static int multiClickMaxY = 20;
private int x;
private int y;
static long _getMultiClickTime() {
return multiClickTime;
}
static int _getMultiClickMaxX() {
return multiClickMaxX;
}
static int _getMultiClickMaxY() {
return multiClickMaxY;
}
@Override
protected void _enableInputMethodEvents(long ptr, boolean enable) {
}
@Override
protected long _getNativeView(long ptr) {
return ptr;
}
@Override
protected int _getX(long ptr) {
return x;
}
@Override
protected int _getY(long ptr) {
return y;
}
@Override
protected void _scheduleRepaint(long ptr) {
}
@Override protected void _uploadPixels(long nativeViewPtr, Pixels pixels) {
if (getWindow() != null) {
NativeScreen screen =
NativePlatformFactory.getNativePlatform().getScreen();
Window window = getWindow();
screen.uploadPixels(pixels.getPixels(),
x + window.getX(), y + window.getY(),
pixels.getWidth(), pixels.getHeight(),
window.getAlpha());
}
}
@Override
protected void notifyKey(int type, int keyCode, char[] keyChars,
int modifiers) {
super.notifyKey(type, keyCode, keyChars, modifiers);
}
@Override
protected void notifyMouse(int type, int button,
int x, int y, int xAbs, int yAbs, int modifiers,
boolean isPopupTrigger, boolean isSynthesized) {
super.notifyMouse(type, button, x, y, xAbs, yAbs, modifiers,
isPopupTrigger,
isSynthesized);
}
@Override
protected void notifyScroll(int x, int y, int xAbs, int yAbs,
double deltaX, double deltaY, int modifiers,
int lines, int chars,
int defaultLines, int defaultChars,
double xMultiplier, double yMultiplier) {
super.notifyScroll(x, y, xAbs, yAbs, deltaX, deltaY,
modifiers, lines, chars,
defaultLines, defaultChars, xMultiplier,
yMultiplier);
}
void notifyRepaint() {
super.notifyRepaint(x, y, getWidth(), getHeight());
}
@Override
protected void notifyResize(int width, int height) {
super.notifyResize(width, height);
}
@Override
protected void notifyView(int viewEvent) {
super.notifyView(viewEvent);
}
@Override
protected int notifyDragEnter(int x, int y, int absx, int absy, int recommendedDropAction) {
return super.notifyDragEnter(x, y, absx, absy, recommendedDropAction);
}
@Override
protected void notifyDragLeave() {
super.notifyDragLeave();
}
@Override
protected int notifyDragDrop(int x, int y, int absx, int absy, int recommendedDropAction) {
return super.notifyDragDrop(x, y, absx, absy, recommendedDropAction);
}
@Override
protected int notifyDragOver(int x, int y, int absx, int absy, int recommendedDropAction) {
return super.notifyDragOver(x, y, absx, absy, recommendedDropAction);
}
@Override
protected void notifyDragEnd(int performedAction) {
super.notifyDragEnd(performedAction);
}
@Override
protected void notifyMenu(int x, int y, int xAbs, int yAbs, boolean isKeyboardTrigger) {
super.notifyMenu(x, y, xAbs, yAbs, isKeyboardTrigger);
}
@Override
protected int _getNativeFrameBuffer(long ptr) {
return 0;
}
@Override
protected long _create(Map caps) {
return 1l;
}
@Override
protected void _setParent(long ptr, long parentPtr) {
}
@Override
protected boolean _close(long ptr) {
return true;
}
@Override
protected boolean _enterFullscreen(long ptr, boolean animate,
boolean keepRatio,
boolean hideCursor) {
MonocleWindowManager wm = MonocleWindowManager.getInstance();
MonocleWindow focusedWindow = wm.getFocusedWindow();
if (focusedWindow != null) {
focusedWindow.setFullScreen(true);
}
if (hideCursor) {
resetCursorVisibility = true;
NativeCursor nativeCursor =
NativePlatformFactory.getNativePlatform().getCursor();
cursorVisibility = nativeCursor.getVisiblity();
nativeCursor.setVisibility(false);
}
return true;
}
@Override
protected void _exitFullscreen(long ptr, boolean animate) {
MonocleWindowManager wm = MonocleWindowManager.getInstance();
MonocleWindow focusedWindow = wm.getFocusedWindow();
if (focusedWindow != null) {
focusedWindow.setFullScreen(false);
}
if (resetCursorVisibility) {
resetCursorVisibility = false;
NativeCursor nativeCursor =
NativePlatformFactory.getNativePlatform().getCursor();
nativeCursor.setVisibility(cursorVisibility);
}
}
@Override
public String toString() {
return "MonocleView["
+ x + "," + y
+ "+" + getWidth() + "x" + getHeight()
+ "]";
}
@Override
protected void _begin(long ptr) {
}
@Override
protected void _end(long ptr) {
}
}
