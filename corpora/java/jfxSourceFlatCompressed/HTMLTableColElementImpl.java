package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLTableColElement;
public class HTMLTableColElementImpl extends HTMLElementImpl implements HTMLTableColElement {
HTMLTableColElementImpl(long peer) {
super(peer);
}
static HTMLTableColElement getImpl(long peer) {
return (HTMLTableColElement)create(peer);
}
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
public String getCh() {
return getChImpl(getPeer());
}
native static String getChImpl(long peer);
public void setCh(String value) {
setChImpl(getPeer(), value);
}
native static void setChImpl(long peer, String value);
public String getChOff() {
return getChOffImpl(getPeer());
}
native static String getChOffImpl(long peer);
public void setChOff(String value) {
setChOffImpl(getPeer(), value);
}
native static void setChOffImpl(long peer, String value);
public int getSpan() {
return getSpanImpl(getPeer());
}
native static int getSpanImpl(long peer);
public void setSpan(int value) {
setSpanImpl(getPeer(), value);
}
native static void setSpanImpl(long peer, int value);
public String getVAlign() {
return getVAlignImpl(getPeer());
}
native static String getVAlignImpl(long peer);
public void setVAlign(String value) {
setVAlignImpl(getPeer(), value);
}
native static void setVAlignImpl(long peer, String value);
public String getWidth() {
return getWidthImpl(getPeer());
}
native static String getWidthImpl(long peer);
public void setWidth(String value) {
setWidthImpl(getPeer(), value);
}
native static void setWidthImpl(long peer, String value);
}
