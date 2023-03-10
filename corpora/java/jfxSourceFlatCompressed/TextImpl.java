package com.sun.webkit.dom;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;
public class TextImpl extends CharacterDataImpl implements Text {
TextImpl(long peer) {
super(peer);
}
static Text getImpl(long peer) {
return (Text)create(peer);
}
public String getWholeText() {
return getWholeTextImpl(getPeer());
}
native static String getWholeTextImpl(long peer);
public Text splitText(int offset) throws DOMException
{
return TextImpl.getImpl(splitTextImpl(getPeer()
, offset));
}
native static long splitTextImpl(long peer
, int offset);
public Text replaceWholeText(String content) throws DOMException
{
return TextImpl.getImpl(replaceWholeTextImpl(getPeer()
, content));
}
native static long replaceWholeTextImpl(long peer
, String content);
public boolean isElementContentWhitespace() {
throw new UnsupportedOperationException("Not supported yet.");
}
}
