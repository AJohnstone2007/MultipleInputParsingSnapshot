package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLHtmlElement;
public class HTMLHtmlElementImpl extends HTMLElementImpl implements HTMLHtmlElement {
HTMLHtmlElementImpl(long peer) {
super(peer);
}
static HTMLHtmlElement getImpl(long peer) {
return (HTMLHtmlElement)create(peer);
}
public String getVersion() {
return getVersionImpl(getPeer());
}
native static String getVersionImpl(long peer);
public void setVersion(String value) {
setVersionImpl(getPeer(), value);
}
native static void setVersionImpl(long peer, String value);
public String getManifest() {
return getManifestImpl(getPeer());
}
native static String getManifestImpl(long peer);
public void setManifest(String value) {
setManifestImpl(getPeer(), value);
}
native static void setManifestImpl(long peer, String value);
}
