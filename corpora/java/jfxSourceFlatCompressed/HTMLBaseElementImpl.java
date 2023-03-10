package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLBaseElement;
public class HTMLBaseElementImpl extends HTMLElementImpl implements HTMLBaseElement {
HTMLBaseElementImpl(long peer) {
super(peer);
}
static HTMLBaseElement getImpl(long peer) {
return (HTMLBaseElement)create(peer);
}
public String getHref() {
return getHrefImpl(getPeer());
}
native static String getHrefImpl(long peer);
public void setHref(String value) {
setHrefImpl(getPeer(), value);
}
native static void setHrefImpl(long peer, String value);
public String getTarget() {
return getTargetImpl(getPeer());
}
native static String getTargetImpl(long peer);
public void setTarget(String value) {
setTargetImpl(getPeer(), value);
}
native static void setTargetImpl(long peer, String value);
}
