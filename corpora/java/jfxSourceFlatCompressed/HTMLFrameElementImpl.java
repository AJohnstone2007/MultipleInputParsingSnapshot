package com.sun.webkit.dom;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;
import org.w3c.dom.views.AbstractView;
public class HTMLFrameElementImpl extends HTMLElementImpl implements HTMLFrameElement {
HTMLFrameElementImpl(long peer) {
super(peer);
}
static HTMLFrameElement getImpl(long peer) {
return (HTMLFrameElement)create(peer);
}
public String getFrameBorder() {
return getFrameBorderImpl(getPeer());
}
native static String getFrameBorderImpl(long peer);
public void setFrameBorder(String value) {
setFrameBorderImpl(getPeer(), value);
}
native static void setFrameBorderImpl(long peer, String value);
public String getLongDesc() {
return getLongDescImpl(getPeer());
}
native static String getLongDescImpl(long peer);
public void setLongDesc(String value) {
setLongDescImpl(getPeer(), value);
}
native static void setLongDescImpl(long peer, String value);
public String getMarginHeight() {
return getMarginHeightImpl(getPeer());
}
native static String getMarginHeightImpl(long peer);
public void setMarginHeight(String value) {
setMarginHeightImpl(getPeer(), value);
}
native static void setMarginHeightImpl(long peer, String value);
public String getMarginWidth() {
return getMarginWidthImpl(getPeer());
}
native static String getMarginWidthImpl(long peer);
public void setMarginWidth(String value) {
setMarginWidthImpl(getPeer(), value);
}
native static void setMarginWidthImpl(long peer, String value);
public String getName() {
return getNameImpl(getPeer());
}
native static String getNameImpl(long peer);
public void setName(String value) {
setNameImpl(getPeer(), value);
}
native static void setNameImpl(long peer, String value);
public boolean getNoResize() {
return getNoResizeImpl(getPeer());
}
native static boolean getNoResizeImpl(long peer);
public void setNoResize(boolean value) {
setNoResizeImpl(getPeer(), value);
}
native static void setNoResizeImpl(long peer, boolean value);
public String getScrolling() {
return getScrollingImpl(getPeer());
}
native static String getScrollingImpl(long peer);
public void setScrolling(String value) {
setScrollingImpl(getPeer(), value);
}
native static void setScrollingImpl(long peer, String value);
public String getSrc() {
return getSrcImpl(getPeer());
}
native static String getSrcImpl(long peer);
public void setSrc(String value) {
setSrcImpl(getPeer(), value);
}
native static void setSrcImpl(long peer, String value);
public Document getContentDocument() {
return DocumentImpl.getImpl(getContentDocumentImpl(getPeer()));
}
native static long getContentDocumentImpl(long peer);
public AbstractView getContentWindow() {
return DOMWindowImpl.getImpl(getContentWindowImpl(getPeer()));
}
native static long getContentWindowImpl(long peer);
public String getLocation() {
return getLocationImpl(getPeer());
}
native static String getLocationImpl(long peer);
public void setLocation(String value) {
setLocationImpl(getPeer(), value);
}
native static void setLocationImpl(long peer, String value);
public int getWidth() {
return getWidthImpl(getPeer());
}
native static int getWidthImpl(long peer);
public int getHeight() {
return getHeightImpl(getPeer());
}
native static int getHeightImpl(long peer);
}
