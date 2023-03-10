package com.sun.webkit.dom;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
public class DocumentFragmentImpl extends NodeImpl implements DocumentFragment {
DocumentFragmentImpl(long peer) {
super(peer);
}
static DocumentFragment getImpl(long peer) {
return (DocumentFragment)create(peer);
}
public HTMLCollection getChildren() {
return HTMLCollectionImpl.getImpl(getChildrenImpl(getPeer()));
}
native static long getChildrenImpl(long peer);
public Element getFirstElementChild() {
return ElementImpl.getImpl(getFirstElementChildImpl(getPeer()));
}
native static long getFirstElementChildImpl(long peer);
public Element getLastElementChild() {
return ElementImpl.getImpl(getLastElementChildImpl(getPeer()));
}
native static long getLastElementChildImpl(long peer);
public int getChildElementCount() {
return getChildElementCountImpl(getPeer());
}
native static int getChildElementCountImpl(long peer);
public Element getElementById(String elementId)
{
return ElementImpl.getImpl(getElementByIdImpl(getPeer()
, elementId));
}
native static long getElementByIdImpl(long peer
, String elementId);
public Element querySelector(String selectors) throws DOMException
{
return ElementImpl.getImpl(querySelectorImpl(getPeer()
, selectors));
}
native static long querySelectorImpl(long peer
, String selectors);
public NodeList querySelectorAll(String selectors) throws DOMException
{
return NodeListImpl.getImpl(querySelectorAllImpl(getPeer()
, selectors));
}
native static long querySelectorAllImpl(long peer
, String selectors);
}
