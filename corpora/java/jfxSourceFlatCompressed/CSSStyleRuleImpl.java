package com.sun.webkit.dom;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
public class CSSStyleRuleImpl extends CSSRuleImpl implements CSSStyleRule {
CSSStyleRuleImpl(long peer) {
super(peer);
}
static CSSStyleRule getImpl(long peer) {
return (CSSStyleRule)create(peer);
}
public String getSelectorText() {
return getSelectorTextImpl(getPeer());
}
native static String getSelectorTextImpl(long peer);
public void setSelectorText(String value) {
setSelectorTextImpl(getPeer(), value);
}
native static void setSelectorTextImpl(long peer, String value);
public CSSStyleDeclaration getStyle() {
return CSSStyleDeclarationImpl.getImpl(getStyleImpl(getPeer()));
}
native static long getStyleImpl(long peer);
}
