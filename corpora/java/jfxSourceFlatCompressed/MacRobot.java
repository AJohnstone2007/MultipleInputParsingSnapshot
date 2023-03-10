package com.sun.glass.ui.mac;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.GlassRobot;
import com.sun.glass.ui.Pixels;
final class MacRobot extends GlassRobot {
private long ptr;
private native long _init();
@Override
public void create() {
Application.checkEventThread();
ptr = _init();
}
native private void _destroy(long ptr);
@Override
public void destroy() {
Application.checkEventThread();
if (ptr == 0) {
return;
}
_destroy(ptr);
}
native protected void _keyPress(int code);
@Override
public void keyPress(KeyCode code) {
Application.checkEventThread();
_keyPress(code.getCode());
}
native protected void _keyRelease(int code);
@Override
public void keyRelease(KeyCode code) {
Application.checkEventThread();
_keyRelease(code.getCode());
}
native private void _mouseMove(long ptr, float x, float y);
@Override
public void mouseMove(double x, double y) {
Application.checkEventThread();
if (ptr == 0) {
return;
}
_mouseMove(ptr, (float) x, (float) y);
}
native private void _mousePress(long ptr, int buttons);
@Override
public void mousePress(MouseButton... buttons) {
Application.checkEventThread();
if (ptr == 0) {
return;
}
_mousePress(ptr, GlassRobot.convertToRobotMouseButton(buttons));
}
native private void _mouseRelease(long ptr, int buttons);
@Override
public void mouseRelease(MouseButton... buttons) {
Application.checkEventThread();
if (ptr == 0) {
return;
}
_mouseRelease(ptr, GlassRobot.convertToRobotMouseButton(buttons));
}
native protected void _mouseWheel(int wheelAmt);
@Override
public void mouseWheel(int wheelAmt) {
Application.checkEventThread();
_mouseWheel(wheelAmt);
}
native private float _getMouseX(long ptr);
@Override
public double getMouseX() {
Application.checkEventThread();
if (ptr == 0) {
return 0;
}
return _getMouseX(ptr);
}
native private float _getMouseY(long ptr);
@Override
public double getMouseY() {
Application.checkEventThread();
if (ptr == 0) {
return 0;
}
return _getMouseY(ptr);
}
native protected int _getPixelColor(double x, double y);
@Override
public Color getPixelColor(double x, double y) {
Application.checkEventThread();
return GlassRobot.convertFromIntArgb(_getPixelColor(x, y));
}
native protected Pixels _getScreenCapture(int x, int y, int width, int height, boolean scaleToFit);
@Override
public WritableImage getScreenCapture(WritableImage image, double x, double y, double width,
double height, boolean scaleToFit) {
Application.checkEventThread();
if (width <= 0) {
throw new IllegalArgumentException("width must be > 0");
}
if (height <= 0) {
throw new IllegalArgumentException("height must be > 0");
}
Pixels pixels;
pixels = _getScreenCapture((int) x, (int) y, (int) width, (int) height, scaleToFit);
return convertFromPixels(image, pixels);
}
}
