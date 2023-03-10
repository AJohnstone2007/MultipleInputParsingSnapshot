package com.sun.javafx.webkit;
import com.sun.javafx.scene.NodeHelper;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Window;
import com.sun.javafx.util.Utils;
import com.sun.webkit.CursorManager;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
public final class WebPageClientImpl implements WebPageClient<WebView> {
@SuppressWarnings("removal")
private static final boolean backBufferSupported = Boolean.valueOf(
AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty(
"com.sun.webkit.pagebackbuffer", "true")));
private static WebConsoleListener consoleListener = null;
private final Accessor accessor;
static void setConsoleListener(WebConsoleListener consoleListener) {
WebPageClientImpl.consoleListener = consoleListener;
}
public WebPageClientImpl(Accessor accessor) {
this.accessor = accessor;
}
@Override public void setFocus(boolean focus) {
WebView view = accessor.getView();
if (view != null && focus) {
view.requestFocus();
}
}
@Override public void setCursor(long cursorID) {
WebView view = accessor.getView();
if (view != null) {
Object cursor = CursorManager.getCursorManager().getCursor(cursorID);
view.setCursor((cursor instanceof Cursor) ? (Cursor) cursor : Cursor.DEFAULT);
}
}
private WeakReference<Tooltip> tooltipRef;
private boolean isTooltipRegistered = false;
private String oldTooltipText = "";
@Override public void setTooltip(final String tooltipText) {
WebView view = accessor.getView();
if (tooltipText != null) {
Tooltip tooltip = (tooltipRef == null) ? null : tooltipRef.get();
if (tooltip == null) {
tooltip = new Tooltip(tooltipText);
tooltipRef = new WeakReference<Tooltip>(tooltip);
} else {
tooltip.setText(tooltipText);
if (!oldTooltipText.equals(tooltipText)) {
Tooltip.uninstall(view, tooltip);
isTooltipRegistered = false;
}
}
oldTooltipText = tooltipText;
if (!isTooltipRegistered) {
Tooltip.install(view, tooltip);
isTooltipRegistered = true;
}
} else if (isTooltipRegistered) {
Tooltip tooltip = tooltipRef.get();
if (tooltip != null) {
Tooltip.uninstall(view, tooltip);
}
isTooltipRegistered = false;
}
}
@Override public void transferFocus(boolean forward) {
NodeHelper.traverse(accessor.getView(), forward ? Direction.NEXT : Direction.PREVIOUS);
}
@Override public WCRectangle getScreenBounds(boolean available) {
WebView view = accessor.getView();
Screen screen = Utils.getScreen(view);
if (screen != null) {
Rectangle2D r = available
? screen.getVisualBounds()
: screen.getBounds();
return new WCRectangle(
(float)r.getMinX(), (float)r.getMinY(),
(float)r.getWidth(), (float)r.getHeight());
}
return null;
}
@Override public int getScreenDepth() {
return 24;
}
@Override public WebView getContainer() {
return accessor.getView();
}
@Override public WCPoint screenToWindow(WCPoint ptScreen) {
WebView view = accessor.getView();
Scene scene = view.getScene();
Window window = null;
if (scene != null &&
(window = scene.getWindow()) != null)
{
Point2D pt = view.sceneToLocal(
ptScreen.getX() - window.getX() - scene.getX(),
ptScreen.getY() - window.getY() - scene.getY());
return new WCPoint((float)pt.getX(), (float)pt.getY());
} else {
return new WCPoint(0f, 0f);
}
}
@Override public WCPoint windowToScreen(WCPoint ptWindow) {
WebView view = accessor.getView();
Scene scene = view.getScene();
Window window = null;
if (scene != null &&
(window = scene.getWindow()) != null)
{
Point2D pt = view.localToScene(ptWindow.getX(), ptWindow.getY());
return new WCPoint((float)(pt.getX() + scene.getX() + window.getX()),
(float)(pt.getY() + scene.getY() + window.getY()));
} else {
return new WCPoint(0f, 0f);
}
}
@Override public WCPageBackBuffer createBackBuffer() {
if (isBackBufferSupported()) {
return WCGraphicsManager.getGraphicsManager().createPageBackBuffer();
}
return null;
}
@Override public boolean isBackBufferSupported() {
return backBufferSupported;
}
@Override public void addMessageToConsole(String message, int lineNumber,
String sourceId)
{
if (consoleListener != null) {
try {
consoleListener.messageAdded(accessor.getView(), message, lineNumber, sourceId);
} catch (Exception e) {
e.printStackTrace();
}
}
}
@Override public void didClearWindowObject(long context,
long windowObject)
{
}
}
