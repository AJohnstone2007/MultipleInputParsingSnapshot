package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLDivElement;
public class HTMLDivElementImpl extends HTMLElementImpl implements HTMLDivElement {
HTMLDivElementImpl(long peer) {
super(peer);
}
static HTMLDivElement getImpl(long peer) {
return (HTMLDivElement)create(peer);
}
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
}
