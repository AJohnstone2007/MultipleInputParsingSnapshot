package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;
public class HTMLMapElementImpl extends HTMLElementImpl implements HTMLMapElement {
HTMLMapElementImpl(long peer) {
super(peer);
}
static HTMLMapElement getImpl(long peer) {
return (HTMLMapElement)create(peer);
}
public HTMLCollection getAreas() {
return HTMLCollectionImpl.getImpl(getAreasImpl(getPeer()));
}
native static long getAreasImpl(long peer);
public String getName() {
return getNameImpl(getPeer());
}
native static String getNameImpl(long peer);
public void setName(String value) {
setNameImpl(getPeer(), value);
}
native static void setNameImpl(long peer, String value);
}
