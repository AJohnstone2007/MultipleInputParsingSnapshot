package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLHRElement;
public class HTMLHRElementImpl extends HTMLElementImpl implements HTMLHRElement {
HTMLHRElementImpl(long peer) {
super(peer);
}
static HTMLHRElement getImpl(long peer) {
return (HTMLHRElement)create(peer);
}
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
public boolean getNoShade() {
return getNoShadeImpl(getPeer());
}
native static boolean getNoShadeImpl(long peer);
public void setNoShade(boolean value) {
setNoShadeImpl(getPeer(), value);
}
native static void setNoShadeImpl(long peer, boolean value);
public String getSize() {
return getSizeImpl(getPeer());
}
native static String getSizeImpl(long peer);
public void setSize(String value) {
setSizeImpl(getPeer(), value);
}
native static void setSizeImpl(long peer, String value);
public String getWidth() {
return getWidthImpl(getPeer());
}
native static String getWidthImpl(long peer);
public void setWidth(String value) {
setWidthImpl(getPeer(), value);
}
native static void setWidthImpl(long peer, String value);
}
