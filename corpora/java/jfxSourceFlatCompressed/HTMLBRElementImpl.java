package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLBRElement;
public class HTMLBRElementImpl extends HTMLElementImpl implements HTMLBRElement {
HTMLBRElementImpl(long peer) {
super(peer);
}
static HTMLBRElement getImpl(long peer) {
return (HTMLBRElement)create(peer);
}
public String getClear() {
return getClearImpl(getPeer());
}
native static String getClearImpl(long peer);
public void setClear(String value) {
setClearImpl(getPeer(), value);
}
native static void setClearImpl(long peer, String value);
}
