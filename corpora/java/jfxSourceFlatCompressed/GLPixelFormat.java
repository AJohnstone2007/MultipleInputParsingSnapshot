package com.sun.prism.es2;
import java.lang.annotation.Native;
import java.security.AccessController;
import java.security.PrivilegedAction;
class GLPixelFormat {
final private Attributes attributes;
final private long nativeScreen;
private long nativePFInfo;
private static int defaultDepthSize;
private static int defaultBufferSize;
static {
@SuppressWarnings("removal")
var dummy = AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
defaultDepthSize = Integer.getInteger("prism.glDepthSize", 24);
defaultBufferSize = Integer.getInteger("prism.glBufferSize", 32);
return null;
});
}
GLPixelFormat(long nativeScreen, Attributes attributes) {
this.nativeScreen = nativeScreen;
this.attributes = attributes;
}
Attributes getAttributes() {
return attributes;
}
long getNativeScreen() {
return nativeScreen;
}
void setNativePFInfo(long nativePFInfo) {
this.nativePFInfo = nativePFInfo;
}
long getNativePFInfo() {
return nativePFInfo;
}
static class Attributes {
@Native final static int RED_SIZE = 0;
@Native final static int GREEN_SIZE = 1;
@Native final static int BLUE_SIZE = 2;
@Native final static int ALPHA_SIZE = 3;
@Native final static int DEPTH_SIZE = 4;
@Native final static int DOUBLEBUFFER = 5;
@Native final static int ONSCREEN = 6;
@Native final static int NUM_ITEMS = 7;
private boolean onScreen;
private boolean doubleBuffer;
private int alphaSize;
private int blueSize;
private int greenSize;
private int redSize;
private int depthSize;
Attributes() {
onScreen = true;
doubleBuffer = true;
depthSize = defaultDepthSize;
switch (defaultBufferSize) {
case 32:
redSize = greenSize = blueSize = alphaSize = 8;
break;
case 24:
redSize = greenSize = blueSize = 8;
alphaSize = 0;
break;
case 16:
redSize = blueSize = 5;
greenSize = 6;
alphaSize = 0;
break;
default:
throw new IllegalArgumentException("color buffer size "
+ defaultBufferSize + " not supported");
}
}
boolean isOnScreen() {
return onScreen;
}
boolean isDoubleBuffer() {
return doubleBuffer;
}
int getDepthSize() {
return depthSize;
}
int getAlphaSize() {
return alphaSize;
}
int getBlueSize() {
return blueSize;
}
int getGreenSize() {
return greenSize;
}
int getRedSize() {
return redSize;
}
void setOnScreen(boolean os) {
onScreen = os;
}
void setDoubleBuffer(boolean db) {
doubleBuffer = db;
}
void setDepthSize(int ds) {
depthSize = ds;
}
void setAlphaSize(int as) {
alphaSize = as;
}
void setBlueSize(int bs) {
blueSize = bs;
}
void setGreenSize(int gs) {
greenSize = gs;
}
void setRedSize(int rs) {
redSize = rs;
}
@Override
public String toString() {
return "onScreen: " + onScreen
+ "redSize : " + redSize + ", "
+ "greenSize : " + greenSize + ", "
+ "blueSize : " + blueSize + ", "
+ "alphaSize : " + alphaSize + ", "
+ "depthSize : " + depthSize + ", "
+ "doubleBuffer : " + doubleBuffer;
}
}
}
