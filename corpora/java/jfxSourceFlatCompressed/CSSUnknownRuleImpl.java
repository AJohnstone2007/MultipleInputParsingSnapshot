package com.sun.webkit.dom;
import org.w3c.dom.css.CSSUnknownRule;
public class CSSUnknownRuleImpl extends CSSRuleImpl implements CSSUnknownRule {
CSSUnknownRuleImpl(long peer) {
super(peer);
}
static CSSUnknownRule getImpl(long peer) {
return (CSSUnknownRule)create(peer);
}
}
