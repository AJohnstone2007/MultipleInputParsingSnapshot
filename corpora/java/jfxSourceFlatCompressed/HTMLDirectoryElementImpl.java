package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLDirectoryElement;
public class HTMLDirectoryElementImpl extends HTMLElementImpl implements HTMLDirectoryElement {
HTMLDirectoryElementImpl(long peer) {
super(peer);
}
static HTMLDirectoryElement getImpl(long peer) {
return (HTMLDirectoryElement)create(peer);
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
