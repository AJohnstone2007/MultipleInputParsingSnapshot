package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLLIElement;
public class HTMLLIElementImpl extends HTMLElementImpl implements HTMLLIElement {
HTMLLIElementImpl(long peer) {
super(peer);
}
static HTMLLIElement getImpl(long peer) {
return (HTMLLIElement)create(peer);
}
public String getType() {
return getTypeImpl(getPeer());
}
native static String getTypeImpl(long peer);
public void setType(String value) {
setTypeImpl(getPeer(), value);
}
native static void setTypeImpl(long peer, String value);
public int getValue() {
return getValueImpl(getPeer());
}
native static int getValueImpl(long peer);
public void setValue(int value) {
setValueImpl(getPeer(), value);
}
native static void setValueImpl(long peer, int value);
}
