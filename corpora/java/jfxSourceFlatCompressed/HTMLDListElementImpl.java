package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLDListElement;
public class HTMLDListElementImpl extends HTMLElementImpl implements HTMLDListElement {
HTMLDListElementImpl(long peer) {
super(peer);
}
static HTMLDListElement getImpl(long peer) {
return (HTMLDListElement)create(peer);
}
public boolean getCompact() {
return getCompactImpl(getPeer());
}
native static boolean getCompactImpl(long peer);
public void setCompact(boolean value) {
setCompactImpl(getPeer(), value);
}
native static void setCompactImpl(long peer, boolean value);
}
