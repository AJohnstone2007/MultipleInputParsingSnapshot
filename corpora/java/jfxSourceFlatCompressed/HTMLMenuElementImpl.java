package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLMenuElement;
public class HTMLMenuElementImpl extends HTMLElementImpl implements HTMLMenuElement {
HTMLMenuElementImpl(long peer) {
super(peer);
}
static HTMLMenuElement getImpl(long peer) {
return (HTMLMenuElement)create(peer);
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
