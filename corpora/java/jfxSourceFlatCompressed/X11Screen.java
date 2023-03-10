package com.sun.glass.ui.monocle;
import com.sun.glass.ui.Pixels;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
class X11Screen implements NativeScreen {
private int depth;
private int nativeFormat;
private int width;
private int height;
private long nativeHandle;
private long display;
private static X xLib = X.getX();
X11Screen(boolean showCursor) {
xLib.XInitThreads();
display = xLib.XOpenDisplay(null);
xLib.XLockDisplay(display);
if (display == 0l) {
throw new NullPointerException("Cannot open X11 display");
}
long screen = xLib.DefaultScreenOfDisplay(display);
X.XSetWindowAttributes attrs = new X.XSetWindowAttributes();
attrs.setEventMask(attrs.p,
X.ButtonPressMask | X.ButtonReleaseMask
| X.PointerMotionMask);
long cwMask = X.CWEventMask;
if (!showCursor) {
cwMask |= X.CWCursorMask;
attrs.setCursor(attrs.p, X.None);
}
int x = 0;
int y = 0;
int w = xLib.WidthOfScreen(screen);
int h = xLib.HeightOfScreen(screen);
boolean fullScreen = true;
@SuppressWarnings("removal")
String geometry =
AccessController.doPrivileged((PrivilegedAction<String>) () ->
System.getProperty("x11.geometry"));
if (geometry != null) {
try {
String size;
if (geometry.contains(",")) {
String location;
int i = geometry.indexOf("+");
if (i >= 0) {
location = geometry.substring(0, i);
size = geometry.substring(i + 1);
} else {
location = geometry;
size = "";
}
i = location.indexOf(",");
x = Integer.parseInt(location.substring(0, i));
y = Integer.parseInt(location.substring(i + 1));
fullScreen = false;
} else {
size = geometry;
}
if (size.length() > 0) {
int i = size.indexOf("x");
w = Integer.parseInt(size.substring(0, i));
h = Integer.parseInt(size.substring(i + 1));
fullScreen = false;
}
} catch (NumberFormatException e) {
System.err.println("Cannot parse geometry string: '"
+ geometry + "'");
}
}
if (fullScreen) {
X.XSetWindowAttributes.setOverrideRedirect(attrs.p, true);
cwMask |= X.CWOverrideRedirect;
}
long window = xLib.XCreateWindow(
display,
xLib.RootWindowOfScreen(screen),
x, y, w, h,
0,
X.CopyFromParent,
X.InputOutput,
X.CopyFromParent,
cwMask,
attrs.p);
xLib.XMapWindow(display, window);
if (fullScreen) {
X.XClientMessageEvent event = new X.XClientMessageEvent(
new X.XEvent());
X.XEvent.setWindow(event.p, window);
X.XClientMessageEvent.setMessageType(event.p,
xLib.XInternAtom(display,
"_NET_WM_STATE",
false)
);
X.XClientMessageEvent.setFormat(event.p, 32);
X.XClientMessageEvent.setDataLong(event.p, 0, X._NET_WM_STATE_ADD);
X.XClientMessageEvent.setDataLong(event.p, 1,
xLib.XInternAtom(display,
"_NET_WM_STATE_FULLSCREEN",
false)
);
X.XClientMessageEvent.setDataLong(event.p, 2, 0);
xLib.XSendEvent(display, xLib.RootWindowOfScreen(screen),
false,
X.SubstructureRedirectMask | X.SubstructureNotifyMask,
event.p);
xLib.XGrabKeyboard(display, window, true,
X.GrabModeAsync, X.GrabModeAsync, X.CurrentTime);
}
xLib.XStoreName(display, window, "JavaFX framebuffer container");
xLib.XSync(display, false);
int[] widthA = new int[1];
int[] heightA = new int[1];
int[] depthA = new int[1];
xLib.XGetGeometry(display, window, null, null, null, widthA,
heightA, null, depthA);
xLib.XUnlockDisplay(display);
width = widthA[0];
height = heightA[0];
depth = depthA[0];
nativeFormat = Pixels.Format.BYTE_BGRA_PRE;
nativeHandle = window;
}
@Override
public int getDepth() {
return depth;
}
@Override
public int getNativeFormat() {
return nativeFormat;
}
@Override
public int getWidth() {
return width;
}
@Override
public int getHeight() {
return height;
}
@Override
public float getScale() {
return 1.0f;
}
@Override
public long getNativeHandle() {
return nativeHandle;
}
@Override
public int getDPI() {
return 96;
}
@Override
public void shutdown() {
}
long getDisplay() {
return display;
}
@Override
public void uploadPixels(Buffer b,
int x, int y, int width, int height,
float alpha) {
}
@Override
public void swapBuffers() {
}
@Override
public ByteBuffer getScreenCapture() {
return null;
}
}
