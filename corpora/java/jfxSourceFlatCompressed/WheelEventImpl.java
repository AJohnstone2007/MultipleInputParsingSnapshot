package com.sun.webkit.dom;
import org.w3c.dom.views.AbstractView;
public class WheelEventImpl extends MouseEventImpl {
WheelEventImpl(long peer) {
super(peer);
}
static WheelEventImpl getImpl(long peer) {
return (WheelEventImpl)create(peer);
}
public static final int DOM_DELTA_PIXEL = 0x00;
public static final int DOM_DELTA_LINE = 0x01;
public static final int DOM_DELTA_PAGE = 0x02;
public double getDeltaX() {
return getDeltaXImpl(getPeer());
}
native static double getDeltaXImpl(long peer);
public double getDeltaY() {
return getDeltaYImpl(getPeer());
}
native static double getDeltaYImpl(long peer);
public double getDeltaZ() {
return getDeltaZImpl(getPeer());
}
native static double getDeltaZImpl(long peer);
public int getDeltaMode() {
return getDeltaModeImpl(getPeer());
}
native static int getDeltaModeImpl(long peer);
public int getWheelDeltaX() {
return getWheelDeltaXImpl(getPeer());
}
native static int getWheelDeltaXImpl(long peer);
public int getWheelDeltaY() {
return getWheelDeltaYImpl(getPeer());
}
native static int getWheelDeltaYImpl(long peer);
public int getWheelDelta() {
return getWheelDeltaImpl(getPeer());
}
native static int getWheelDeltaImpl(long peer);
public boolean getWebkitDirectionInvertedFromDevice() {
return getWebkitDirectionInvertedFromDeviceImpl(getPeer());
}
native static boolean getWebkitDirectionInvertedFromDeviceImpl(long peer);
public void initWheelEvent(int wheelDeltaX
, int wheelDeltaY
, AbstractView view
, int screenX
, int screenY
, int clientX
, int clientY
, boolean ctrlKey
, boolean altKey
, boolean shiftKey
, boolean metaKey)
{
initWheelEventImpl(getPeer()
, wheelDeltaX
, wheelDeltaY
, DOMWindowImpl.getPeer(view)
, screenX
, screenY
, clientX
, clientY
, ctrlKey
, altKey
, shiftKey
, metaKey);
}
native static void initWheelEventImpl(long peer
, int wheelDeltaX
, int wheelDeltaY
, long view
, int screenX
, int screenY
, int clientX
, int clientY
, boolean ctrlKey
, boolean altKey
, boolean shiftKey
, boolean metaKey);
}
