package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLAppletElement;
public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement {
HTMLAppletElementImpl(long peer) {
super(peer);
}
static HTMLAppletElement getImpl(long peer) {
return (HTMLAppletElement)create(peer);
}
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
public String getAlt() {
return getAltImpl(getPeer());
}
native static String getAltImpl(long peer);
public void setAlt(String value) {
setAltImpl(getPeer(), value);
}
native static void setAltImpl(long peer, String value);
public String getArchive() {
return getArchiveImpl(getPeer());
}
native static String getArchiveImpl(long peer);
public void setArchive(String value) {
setArchiveImpl(getPeer(), value);
}
native static void setArchiveImpl(long peer, String value);
public String getCode() {
return getCodeImpl(getPeer());
}
native static String getCodeImpl(long peer);
public void setCode(String value) {
setCodeImpl(getPeer(), value);
}
native static void setCodeImpl(long peer, String value);
public String getCodeBase() {
return getCodeBaseImpl(getPeer());
}
native static String getCodeBaseImpl(long peer);
public void setCodeBase(String value) {
setCodeBaseImpl(getPeer(), value);
}
native static void setCodeBaseImpl(long peer, String value);
public String getHeight() {
return getHeightImpl(getPeer());
}
native static String getHeightImpl(long peer);
public void setHeight(String value) {
setHeightImpl(getPeer(), value);
}
native static void setHeightImpl(long peer, String value);
public String getHspace() {
return getHspaceImpl(getPeer())+"";
}
native static int getHspaceImpl(long peer);
public void setHspace(String value) {
setHspaceImpl(getPeer(), Integer.parseInt(value));
}
native static void setHspaceImpl(long peer, int value);
public String getName() {
return getNameImpl(getPeer());
}
native static String getNameImpl(long peer);
public void setName(String value) {
setNameImpl(getPeer(), value);
}
native static void setNameImpl(long peer, String value);
public String getObject() {
return getObjectImpl(getPeer());
}
native static String getObjectImpl(long peer);
public void setObject(String value) {
setObjectImpl(getPeer(), value);
}
native static void setObjectImpl(long peer, String value);
public String getVspace() {
return getVspaceImpl(getPeer())+"";
}
native static int getVspaceImpl(long peer);
public void setVspace(String value) {
setVspaceImpl(getPeer(), Integer.parseInt(value));
}
native static void setVspaceImpl(long peer, int value);
public String getWidth() {
return getWidthImpl(getPeer());
}
native static String getWidthImpl(long peer);
public void setWidth(String value) {
setWidthImpl(getPeer(), value);
}
native static void setWidthImpl(long peer, String value);
}
