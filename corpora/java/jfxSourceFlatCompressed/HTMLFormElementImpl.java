package com.sun.webkit.dom;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;
public class HTMLFormElementImpl extends HTMLElementImpl implements HTMLFormElement {
HTMLFormElementImpl(long peer) {
super(peer);
}
static HTMLFormElement getImpl(long peer) {
return (HTMLFormElement)create(peer);
}
public String getAcceptCharset() {
return getAcceptCharsetImpl(getPeer());
}
native static String getAcceptCharsetImpl(long peer);
public void setAcceptCharset(String value) {
setAcceptCharsetImpl(getPeer(), value);
}
native static void setAcceptCharsetImpl(long peer, String value);
public String getAction() {
return getActionImpl(getPeer());
}
native static String getActionImpl(long peer);
public void setAction(String value) {
setActionImpl(getPeer(), value);
}
native static void setActionImpl(long peer, String value);
public String getAutocomplete() {
return getAutocompleteImpl(getPeer());
}
native static String getAutocompleteImpl(long peer);
public void setAutocomplete(String value) {
setAutocompleteImpl(getPeer(), value);
}
native static void setAutocompleteImpl(long peer, String value);
public String getEnctype() {
return getEnctypeImpl(getPeer());
}
native static String getEnctypeImpl(long peer);
public void setEnctype(String value) {
setEnctypeImpl(getPeer(), value);
}
native static void setEnctypeImpl(long peer, String value);
public String getEncoding() {
return getEncodingImpl(getPeer());
}
native static String getEncodingImpl(long peer);
public void setEncoding(String value) {
setEncodingImpl(getPeer(), value);
}
native static void setEncodingImpl(long peer, String value);
public String getMethod() {
return getMethodImpl(getPeer());
}
native static String getMethodImpl(long peer);
public void setMethod(String value) {
setMethodImpl(getPeer(), value);
}
native static void setMethodImpl(long peer, String value);
public String getName() {
return getNameImpl(getPeer());
}
native static String getNameImpl(long peer);
public void setName(String value) {
setNameImpl(getPeer(), value);
}
native static void setNameImpl(long peer, String value);
public boolean getNoValidate() {
return getNoValidateImpl(getPeer());
}
native static boolean getNoValidateImpl(long peer);
public void setNoValidate(boolean value) {
setNoValidateImpl(getPeer(), value);
}
native static void setNoValidateImpl(long peer, boolean value);
public String getTarget() {
return getTargetImpl(getPeer());
}
native static String getTargetImpl(long peer);
public void setTarget(String value) {
setTargetImpl(getPeer(), value);
}
native static void setTargetImpl(long peer, String value);
public HTMLCollection getElements() {
return HTMLCollectionImpl.getImpl(getElementsImpl(getPeer()));
}
native static long getElementsImpl(long peer);
public int getLength() {
return getLengthImpl(getPeer());
}
native static int getLengthImpl(long peer);
public void submit()
{
submitImpl(getPeer());
}
native static void submitImpl(long peer);
public void reset()
{
resetImpl(getPeer());
}
native static void resetImpl(long peer);
public boolean checkValidity()
{
return checkValidityImpl(getPeer());
}
native static boolean checkValidityImpl(long peer);
}
