package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLTableCaptionElement;
public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements HTMLTableCaptionElement {
HTMLTableCaptionElementImpl(long peer) {
super(peer);
}
static HTMLTableCaptionElement getImpl(long peer) {
return (HTMLTableCaptionElement)create(peer);
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
