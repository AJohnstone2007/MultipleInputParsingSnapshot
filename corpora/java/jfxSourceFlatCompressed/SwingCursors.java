package com.sun.javafx.embed.swing;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
public class SwingCursors {
private static Cursor createCustomCursor(ImageCursorFrame cursorFrame) {
Toolkit awtToolkit = Toolkit.getDefaultToolkit();
double imageWidth = cursorFrame.getWidth();
double imageHeight = cursorFrame.getHeight();
Dimension nativeSize = awtToolkit.getBestCursorSize((int)imageWidth, (int)imageHeight);
double scaledHotspotX = cursorFrame.getHotspotX() * nativeSize.getWidth() / imageWidth;
double scaledHotspotY = cursorFrame.getHotspotY() * nativeSize.getHeight() / imageHeight;
Point hotspot = new Point((int)scaledHotspotX, (int)scaledHotspotY);
BufferedImage awtImage = SwingFXUtils.fromFXImage(
com.sun.javafx.tk.Toolkit.getImageAccessor().fromPlatformImage(cursorFrame.getPlatformImage()), null);
return awtToolkit.createCustomCursor(awtImage, hotspot, null);
}
public static Cursor embedCursorToCursor(CursorFrame cursorFrame) {
switch (cursorFrame.getCursorType()) {
case DEFAULT:
return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
case CROSSHAIR:
return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
case TEXT:
return Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
case WAIT:
return Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
case SW_RESIZE:
return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
case SE_RESIZE:
return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
case NW_RESIZE:
return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
case NE_RESIZE:
return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
case N_RESIZE:
case V_RESIZE:
return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
case S_RESIZE:
return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
case W_RESIZE:
case H_RESIZE:
return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
case E_RESIZE:
return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
case OPEN_HAND:
case CLOSED_HAND:
case HAND:
return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
case MOVE:
return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
case DISAPPEAR:
return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
case NONE:
return null;
case IMAGE:
return createCustomCursor((ImageCursorFrame) cursorFrame);
}
return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
}
public static javafx.scene.Cursor embedCursorToCursor(Cursor cursor) {
if (cursor == null) {
return javafx.scene.Cursor.DEFAULT;
}
switch (cursor.getType()) {
case Cursor.DEFAULT_CURSOR:
return javafx.scene.Cursor.DEFAULT;
case Cursor.CROSSHAIR_CURSOR:
return javafx.scene.Cursor.CROSSHAIR;
case Cursor.E_RESIZE_CURSOR:
return javafx.scene.Cursor.E_RESIZE;
case Cursor.HAND_CURSOR:
return javafx.scene.Cursor.HAND;
case Cursor.MOVE_CURSOR:
return javafx.scene.Cursor.MOVE;
case Cursor.N_RESIZE_CURSOR:
return javafx.scene.Cursor.N_RESIZE;
case Cursor.NE_RESIZE_CURSOR:
return javafx.scene.Cursor.NE_RESIZE;
case Cursor.NW_RESIZE_CURSOR:
return javafx.scene.Cursor.NW_RESIZE;
case Cursor.S_RESIZE_CURSOR:
return javafx.scene.Cursor.S_RESIZE;
case Cursor.SE_RESIZE_CURSOR:
return javafx.scene.Cursor.SE_RESIZE;
case Cursor.SW_RESIZE_CURSOR:
return javafx.scene.Cursor.SW_RESIZE;
case Cursor.TEXT_CURSOR:
return javafx.scene.Cursor.TEXT;
case Cursor.W_RESIZE_CURSOR:
return javafx.scene.Cursor.W_RESIZE;
case Cursor.WAIT_CURSOR:
return javafx.scene.Cursor.WAIT;
default:
return javafx.scene.Cursor.DEFAULT;
}
}
}
