package com.sun.scenario.effect.impl.sw.java;
import java.nio.FloatBuffer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState.PassType;
public class JSWLinearConvolvePeer extends JSWEffectPeer<LinearConvolveRenderState> {
public JSWLinearConvolvePeer(FilterContext fctx, Renderer r, String uniqueName) {
super(fctx, r, uniqueName);
}
private Rectangle getResultBounds(LinearConvolveRenderState lcrstate,
Rectangle outputClip,
ImageData... inputDatas)
{
Rectangle r = inputDatas[0].getTransformedBounds(null);
r = lcrstate.getPassResultBounds(r, outputClip);
return r;
}
@Override
public ImageData filter(Effect effect,
LinearConvolveRenderState lcrstate,
BaseTransform transform,
Rectangle outputClip,
ImageData... inputs)
{
setRenderState(lcrstate);
Rectangle dstRawBounds = getResultBounds(lcrstate, null, inputs);
Rectangle dstBounds = new Rectangle(dstRawBounds);
dstBounds.intersectWith(outputClip);
setDestBounds(dstBounds);
int dstw = dstBounds.width;
int dsth = dstBounds.height;
HeapImage src = (HeapImage)inputs[0].getUntransformedImage();
int srcw = src.getPhysicalWidth();
int srch = src.getPhysicalHeight();
int srcscan = src.getScanlineStride();
int[] srcPixels = src.getPixelArray();
Rectangle src0Bounds = inputs[0].getUntransformedBounds();
BaseTransform src0Transform = inputs[0].getTransform();
Rectangle src0NativeBounds = new Rectangle(0, 0, srcw, srch);
setInputBounds(0, src0Bounds);
setInputTransform(0, src0Transform);
setInputNativeBounds(0, src0NativeBounds);
HeapImage dst = (HeapImage)getRenderer().getCompatibleImage(dstw, dsth);
setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
int dstscan = dst.getScanlineStride();
int[] dstPixels = dst.getPixelArray();
int count = lcrstate.getPassKernelSize();
FloatBuffer weights_buf = lcrstate.getPassWeights();
PassType type = lcrstate.getPassType();
if (!src0Transform.isIdentity() ||
!dstBounds.contains(dstRawBounds.x, dstRawBounds.y))
{
type = PassType.GENERAL_VECTOR;
}
if (count >= 0) {
type = PassType.GENERAL_VECTOR;
}
if (type == PassType.HORIZONTAL_CENTERED) {
float[] weights_arr = new float[count * 2];
weights_buf.get(weights_arr, 0, count);
weights_buf.rewind();
weights_buf.get(weights_arr, count, count);
filterHV(dstPixels, dstw, dsth, 1, dstscan,
srcPixels, srcw, srch, 1, srcscan,
weights_arr);
} else if (type == PassType.VERTICAL_CENTERED) {
float[] weights_arr = new float[count * 2];
weights_buf.get(weights_arr, 0, count);
weights_buf.rewind();
weights_buf.get(weights_arr, count, count);
filterHV(dstPixels, dsth, dstw, dstscan, 1,
srcPixels, srch, srcw, srcscan, 1,
weights_arr);
} else {
float[] weights_arr = new float[count];
weights_buf.get(weights_arr, 0, count);
float[] srcRect = new float[8];
int nCoords = getTextureCoordinates(0, srcRect,
src0Bounds.x, src0Bounds.y,
src0NativeBounds.width,
src0NativeBounds.height,
dstBounds, src0Transform);
float srcx0 = srcRect[0] * srcw;
float srcy0 = srcRect[1] * srch;
float dxcol, dycol, dxrow, dyrow;
if (nCoords < 8) {
dxcol = (srcRect[2] - srcRect[0]) * srcw / dstBounds.width;
dycol = 0f;
dxrow = 0f;
dyrow = (srcRect[3] - srcRect[1]) * srch / dstBounds.height;
} else {
dxcol = (srcRect[4] - srcRect[0]) * srcw / dstBounds.width;
dycol = (srcRect[5] - srcRect[1]) * srch / dstBounds.height;
dxrow = (srcRect[6] - srcRect[0]) * srcw / dstBounds.width;
dyrow = (srcRect[7] - srcRect[1]) * srch / dstBounds.height;
}
float[] offset_arr = lcrstate.getPassVector();
float deltax = offset_arr[0] * srcw;
float deltay = offset_arr[1] * srch;
float offsetx = offset_arr[2] * srcw;
float offsety = offset_arr[3] * srch;
filterVector(dstPixels, dstw, dsth, dstscan,
srcPixels, srcw, srch, srcscan,
weights_arr, count,
srcx0, srcy0,
offsetx, offsety,
deltax, deltay,
dxcol, dycol, dxrow, dyrow);
}
return new ImageData(getFilterContext(), dst, dstBounds);
}
private static final float cmin = 1f;
private static final float cmax = 254f + 15f/16f;
protected void filterVector(int dstPixels[], int dstw, int dsth, int dstscan,
int srcPixels[], int srcw, int srch, int srcscan,
float weights[], int count,
float srcx0, float srcy0,
float offsetx, float offsety,
float deltax, float deltay,
float dxcol, float dycol, float dxrow, float dyrow)
{
int dstrow = 0;
float fvals[] = new float[4];
srcx0 += (dxrow + dxcol) * 0.5f;
srcy0 += (dyrow + dycol) * 0.5f;
for (int dy = 0; dy < dsth; dy++) {
float srcx = srcx0;
float srcy = srcy0;
for (int dx = 0; dx < dstw; dx++) {
fvals[0] = fvals[1] = fvals[2] = fvals[3] = 0.0f;
float sampx = srcx + offsetx;
float sampy = srcy + offsety;
for (int i = 0; i < count; ++i) {
laccumsample(srcPixels, sampx, sampy,
srcw, srch, srcscan,
weights[i], fvals);
sampx += deltax;
sampy += deltay;
}
dstPixels[dstrow + dx] =
(((fvals[FVALS_A] < cmin) ? 0 : ((fvals[FVALS_A] > cmax) ? 255 : ((int) fvals[FVALS_A]))) << 24) +
(((fvals[FVALS_R] < cmin) ? 0 : ((fvals[FVALS_R] > cmax) ? 255 : ((int) fvals[FVALS_R]))) << 16) +
(((fvals[FVALS_G] < cmin) ? 0 : ((fvals[FVALS_G] > cmax) ? 255 : ((int) fvals[FVALS_G]))) << 8) +
(((fvals[FVALS_B] < cmin) ? 0 : ((fvals[FVALS_B] > cmax) ? 255 : ((int) fvals[FVALS_B]))) );
srcx += dxcol;
srcy += dycol;
}
srcx0 += dxrow;
srcy0 += dyrow;
dstrow += dstscan;
}
}
protected void filterHV(int dstPixels[], int dstcols, int dstrows, int dcolinc, int drowinc,
int srcPixels[], int srccols, int srcrows, int scolinc, int srowinc,
float weights[])
{
int kernelSize = weights.length / 2;
float cvals[] = new float[kernelSize * 4];
int dstrow = 0;
int srcrow = 0;
for (int r = 0; r < dstrows; r++) {
int dstoff = dstrow;
int srcoff = srcrow;
for (int i = 0; i < cvals.length; i++) {
cvals[i] = 0f;
}
int koff = kernelSize;
for (int c = 0; c < dstcols; c++) {
int i = (kernelSize - koff) * 4;
int rgb = (c < srccols) ? srcPixels[srcoff] : 0;
cvals[i+0] = (rgb >>> 24);
cvals[i+1] = (rgb >> 16) & 0xff;
cvals[i+2] = (rgb >> 8) & 0xff;
cvals[i+3] = (rgb ) & 0xff;
if (--koff <= 0) {
koff += kernelSize;
}
float suma = 0;
float sumr = 0;
float sumg = 0;
float sumb = 0;
for (i = 0; i < cvals.length; i += 4) {
float factor = weights[koff + (i>>2)];
suma += cvals[i+0] * factor;
sumr += cvals[i+1] * factor;
sumg += cvals[i+2] * factor;
sumb += cvals[i+3] * factor;
}
dstPixels[dstoff] =
(((suma < cmin) ? 0 : ((suma > cmax) ? 255 : ((int) suma))) << 24) +
(((sumr < cmin) ? 0 : ((sumr > cmax) ? 255 : ((int) sumr))) << 16) +
(((sumg < cmin) ? 0 : ((sumg > cmax) ? 255 : ((int) sumg))) << 8) +
(((sumb < cmin) ? 0 : ((sumb > cmax) ? 255 : ((int) sumb))) );
dstoff += dcolinc;
srcoff += scolinc;
}
dstrow += drowinc;
srcrow += srowinc;
}
}
}
