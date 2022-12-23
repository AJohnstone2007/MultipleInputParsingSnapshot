package com.sun.webkit.dom;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;
public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement {
HTMLTableRowElementImpl(long peer) {
super(peer);
}
static HTMLTableRowElement getImpl(long peer) {
return (HTMLTableRowElement)create(peer);
}
public int getRowIndex() {
return getRowIndexImpl(getPeer());
}
native static int getRowIndexImpl(long peer);
public int getSectionRowIndex() {
return getSectionRowIndexImpl(getPeer());
}
native static int getSectionRowIndexImpl(long peer);
public HTMLCollection getCells() {
return HTMLCollectionImpl.getImpl(getCellsImpl(getPeer()));
}
native static long getCellsImpl(long peer);
public String getAlign() {
return getAlignImpl(getPeer());
}
native static String getAlignImpl(long peer);
public void setAlign(String value) {
setAlignImpl(getPeer(), value);
}
native static void setAlignImpl(long peer, String value);
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
public String getVAlign() {
return getVAlignImpl(getPeer());
}
native static String getVAlignImpl(long peer);
public void setVAlign(String value) {
setVAlignImpl(getPeer(), value);
}
native static void setVAlignImpl(long peer, String value);
public HTMLElement insertCell(int index) throws DOMException
{
return HTMLElementImpl.getImpl(insertCellImpl(getPeer()
, index));
}
native static long insertCellImpl(long peer
, int index);
public void deleteCell(int index) throws DOMException
{
deleteCellImpl(getPeer()
, index);
}
native static void deleteCellImpl(long peer
, int index);
}
