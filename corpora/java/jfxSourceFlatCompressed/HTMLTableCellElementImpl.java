package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLTableCellElement;
public class HTMLTableCellElementImpl extends HTMLElementImpl implements HTMLTableCellElement {
HTMLTableCellElementImpl(long peer) {
super(peer);
}
static HTMLTableCellElement getImpl(long peer) {
return (HTMLTableCellElement)create(peer);
}
public int getCellIndex() {
return getCellIndexImpl(getPeer());
}
native static int getCellIndexImpl(long peer);
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
public String getAxis() {
return getAxisImpl(getPeer());
}
native static String getAxisImpl(long peer);
public void setAxis(String value) {
setAxisImpl(getPeer(), value);
}
native static void setAxisImpl(long peer, String value);
public String getBgColor() {
return getBgColorImpl(getPeer());
}
native static String getBgColorImpl(long peer);
public void setBgColor(String value) {
setBgColorImpl(getPeer(), value);
}
native static void setBgColorImpl(long peer, String value);
public String getCh() {
return getChImpl(getPeer());
}
native static String getChImpl(long peer);
public void setCh(String value) {
setChImpl(getPeer(), value);
}
native static void setChImpl(long peer, String value);
public String getChOff() {
return getChOffImpl(getPeer());
}
native static String getChOffImpl(long peer);
public void setChOff(String value) {
setChOffImpl(getPeer(), value);
}
native static void setChOffImpl(long peer, String value);
public int getColSpan() {
return getColSpanImpl(getPeer());
}
native static int getColSpanImpl(long peer);
public void setColSpan(int value) {
setColSpanImpl(getPeer(), value);
}
native static void setColSpanImpl(long peer, int value);
public int getRowSpan() {
return getRowSpanImpl(getPeer());
}
native static int getRowSpanImpl(long peer);
public void setRowSpan(int value) {
setRowSpanImpl(getPeer(), value);
}
native static void setRowSpanImpl(long peer, int value);
public String getHeaders() {
return getHeadersImpl(getPeer());
}
native static String getHeadersImpl(long peer);
public void setHeaders(String value) {
setHeadersImpl(getPeer(), value);
}
native static void setHeadersImpl(long peer, String value);
public String getHeight() {
return getHeightImpl(getPeer());
}
native static String getHeightImpl(long peer);
public void setHeight(String value) {
setHeightImpl(getPeer(), value);
}
native static void setHeightImpl(long peer, String value);
public boolean getNoWrap() {
return getNoWrapImpl(getPeer());
}
native static boolean getNoWrapImpl(long peer);
public void setNoWrap(boolean value) {
setNoWrapImpl(getPeer(), value);
}
native static void setNoWrapImpl(long peer, boolean value);
public String getVAlign() {
return getVAlignImpl(getPeer());
}
native static String getVAlignImpl(long peer);
public void setVAlign(String value) {
setVAlignImpl(getPeer(), value);
}
native static void setVAlignImpl(long peer, String value);
public String getWidth() {
return getWidthImpl(getPeer());
}
native static String getWidthImpl(long peer);
public void setWidth(String value) {
setWidthImpl(getPeer(), value);
}
native static void setWidthImpl(long peer, String value);
public String getAbbr() {
return getAbbrImpl(getPeer());
}
native static String getAbbrImpl(long peer);
public void setAbbr(String value) {
setAbbrImpl(getPeer(), value);
}
native static void setAbbrImpl(long peer, String value);
public String getScope() {
return getScopeImpl(getPeer());
}
native static String getScopeImpl(long peer);
public void setScope(String value) {
setScopeImpl(getPeer(), value);
}
native static void setScopeImpl(long peer, String value);
}
