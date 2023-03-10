package com.sun.webkit.dom;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.stylesheets.StyleSheet;
public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
ProcessingInstructionImpl(long peer) {
super(peer);
}
static Node getImpl(long peer) {
return (Node)create(peer);
}
public String getTarget() {
return getTargetImpl(getPeer());
}
native static String getTargetImpl(long peer);
public StyleSheet getSheet() {
return StyleSheetImpl.getImpl(getSheetImpl(getPeer()));
}
native static long getSheetImpl(long peer);
}
