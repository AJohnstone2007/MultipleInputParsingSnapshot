package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLParamElement;
public class HTMLParamElementImpl extends HTMLElementImpl implements HTMLParamElement {
HTMLParamElementImpl(long peer) {
super(peer);
}
static HTMLParamElement getImpl(long peer) {
return (HTMLParamElement)create(peer);
}
public String getName() {
return getNameImpl(getPeer());
}
native static String getNameImpl(long peer);
public void setName(String value) {
setNameImpl(getPeer(), value);
}
native static void setNameImpl(long peer, String value);
public String getType() {
return getTypeImpl(getPeer());
}
native static String getTypeImpl(long peer);
public void setType(String value) {
setTypeImpl(getPeer(), value);
}
native static void setTypeImpl(long peer, String value);
public String getValue() {
return getValueImpl(getPeer());
}
native static String getValueImpl(long peer);
public void setValue(String value) {
setValueImpl(getPeer(), value);
}
native static void setValueImpl(long peer, String value);
public String getValueType() {
return getValueTypeImpl(getPeer());
}
native static String getValueTypeImpl(long peer);
public void setValueType(String value) {
setValueTypeImpl(getPeer(), value);
}
native static void setValueTypeImpl(long peer, String value);
}
