package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLQuoteElement;
public class HTMLQuoteElementImpl extends HTMLElementImpl implements HTMLQuoteElement {
HTMLQuoteElementImpl(long peer) {
super(peer);
}
static HTMLQuoteElement getImpl(long peer) {
return (HTMLQuoteElement)create(peer);
}
public String getCite() {
return getCiteImpl(getPeer());
}
native static String getCiteImpl(long peer);
public void setCite(String value) {
setCiteImpl(getPeer(), value);
}
native static void setCiteImpl(long peer, String value);
}
