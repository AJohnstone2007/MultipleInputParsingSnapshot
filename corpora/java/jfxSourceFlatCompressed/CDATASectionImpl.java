package com.sun.webkit.dom;
import org.w3c.dom.CDATASection;
public class CDATASectionImpl extends TextImpl implements CDATASection {
CDATASectionImpl(long peer) {
super(peer);
}
static CDATASection getImpl(long peer) {
return (CDATASection)create(peer);
}
}
