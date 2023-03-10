package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLUListElement;
public class HTMLUListElementImpl extends HTMLElementImpl implements HTMLUListElement {
HTMLUListElementImpl(long peer) {
super(peer);
}
static HTMLUListElement getImpl(long peer) {
return (HTMLUListElement)create(peer);
}
public boolean getCompact() {
return getCompactImpl(getPeer());
}
native static boolean getCompactImpl(long peer);
public void setCompact(boolean value) {
setCompactImpl(getPeer(), value);
}
native static void setCompactImpl(long peer, boolean value);
public String getType() {
return getTypeImpl(getPeer());
}
native static String getTypeImpl(long peer);
public void setType(String value) {
setTypeImpl(getPeer(), value);
}
native static void setTypeImpl(long peer, String value);
}
