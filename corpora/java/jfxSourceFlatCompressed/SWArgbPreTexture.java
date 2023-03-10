package com.sun.prism.sw;
import java.nio.Buffer;
import java.nio.IntBuffer;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
class SWArgbPreTexture extends SWTexture {
private int data[];
private int offset;
private boolean hasAlpha = true;
SWArgbPreTexture(SWResourceFactory factory, WrapMode wrapMode, int w, int h) {
super(factory, wrapMode, w, h);
offset = 0;
}
SWArgbPreTexture(SWArgbPreTexture sharedTex, WrapMode altMode) {
super(sharedTex, altMode);
this.data = sharedTex.data;
this.offset = sharedTex.offset;
this.hasAlpha = sharedTex.hasAlpha;
}
int[] getDataNoClone() {
return data;
}
int getOffset() {
return offset;
}
boolean hasAlpha() {
return hasAlpha;
}
@Override
public PixelFormat getPixelFormat() {
return PixelFormat.INT_ARGB_PRE;
}
@Override
public void update(Buffer buffer, PixelFormat format, int dstx, int dsty,
int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush)
{
if (PrismSettings.debug) {
System.out.println("ARGB_PRE TEXTURE, Pixel format: " + format + ", buffer: " + buffer);
System.out.println("dstx:" + dstx + " dsty:" + dsty);
System.out.println("srcx:" + srcx + " srcy:" + srcy + " srcw:" + srcw + " srch:" + srch + " srcscan: " + srcscan);
}
this.checkDimensions(dstx+srcw, dsty+srch);
this.allocate();
final PixelGetter getter;
switch (format) {
case BYTE_RGB:
getter = ByteRgb.getter;
this.hasAlpha = false;
break;
case INT_ARGB_PRE:
getter = IntArgbPre.getter;
srcscan = srcscan >> 2;
this.hasAlpha = true;
break;
case BYTE_BGRA_PRE:
getter = ByteBgraPre.getter;
this.hasAlpha = true;
break;
case BYTE_GRAY:
getter = ByteGray.getter;
this.hasAlpha = false;
break;
default:
throw new UnsupportedOperationException("!!! UNSUPPORTED PIXEL FORMAT: " + format);
}
PixelConverter converter = PixelUtils.getConverter(getter, IntArgbPre.setter);
buffer.position(0);
converter.convert(buffer, (srcy * srcscan) + srcx, srcscan,
IntBuffer.wrap(this.data), (dsty * physicalWidth) + dstx, physicalWidth, srcw, srch);
}
@Override
public void update(MediaFrame frame, boolean skipFlush) {
if (PrismSettings.debug) {
System.out.println("Media Pixel format: " + frame.getPixelFormat());
}
frame.holdFrame();
if (frame.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
MediaFrame f = frame.convertToFormat(PixelFormat.INT_ARGB_PRE);
frame.releaseFrame();
frame = f;
}
final int stride = frame.strideForPlane(0) / 4;
IntBuffer ib = frame.getBufferForPlane(0).asIntBuffer();
if (ib.hasArray()) {
this.allocated = false;
this.offset = 0;
this.physicalWidth = stride;
this.data = ib.array();
} else {
this.allocate();
for (int i = 0; i < contentHeight; i++) {
ib.position(offset + i*stride);
ib.get(this.data, i*physicalWidth, contentWidth);
}
}
frame.releaseFrame();
}
void checkDimensions(int srcw, int srch) {
if (srcw < 0) {
throw new IllegalArgumentException("srcw must be >=0");
}
if (srch < 0) {
throw new IllegalArgumentException("srch must be >=0");
}
if (srcw > this.physicalWidth) {
throw new IllegalArgumentException("srcw exceeds WIDTH");
}
if (srch > this.physicalHeight) {
throw new IllegalArgumentException("srch exceeds HEIGHT");
}
}
void applyCompositeAlpha(float alpha) {
if (allocated) {
int finalAlpha;
this.hasAlpha = this.hasAlpha || (alpha < 1f);
for (int i = 0; i < this.data.length; i++) {
finalAlpha = ((int)((this.data[i] >> 24) * alpha + 0.5f)) & 0xFF;
this.data[i] = (finalAlpha << 24) | (this.data[i] & 0xFFFFFF);
}
} else {
throw new IllegalStateException("Cannot apply composite alpha to texture with non-allocated data");
}
}
void allocateBuffer() {
this.data = new int[physicalWidth * physicalHeight];
}
Texture createSharedLockedTexture(WrapMode altMode) {
return new SWArgbPreTexture(this, altMode);
}
}
