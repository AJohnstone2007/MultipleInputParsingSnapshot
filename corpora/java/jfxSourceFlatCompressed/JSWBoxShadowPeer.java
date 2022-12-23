package com.sun.scenario.effect.impl.sw.java;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.BoxShadow;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.BoxRenderState;
public class JSWBoxShadowPeer extends JSWEffectPeer<BoxRenderState> {
public JSWBoxShadowPeer(FilterContext fctx, Renderer r, String uniqueName) {
super(fctx, r, uniqueName);
}
@Override
public ImageData filter(Effect effect,
BoxRenderState brstate,
BaseTransform transform,
Rectangle outputClip,
ImageData... inputs)
{
setRenderState(brstate);
boolean horizontal = (getPass() == 0);
int hinc = horizontal ? brstate.getBoxPixelSize(0) - 1 : 0;
int vinc = horizontal ? 0 : brstate.getBoxPixelSize(1) - 1;
if (hinc < 0) hinc = 0;
if (vinc < 0) vinc = 0;
int iterations = brstate.getBlurPasses();
float spread = brstate.getSpread();
if (horizontal && (iterations < 1 || (hinc < 1 && vinc < 1))) {
inputs[0].addref();
return inputs[0];
}
int growx = (hinc * iterations + 1) & (~0x1);
int growy = (vinc * iterations + 1) & (~0x1);
HeapImage src = (HeapImage)inputs[0].getUntransformedImage();
Rectangle srcr = inputs[0].getUntransformedBounds();
HeapImage cur = src;
int curw = srcr.width;
int curh = srcr.height;
int curscan = cur.getScanlineStride();
int[] curPixels = cur.getPixelArray();
int finalw = curw + growx;
int finalh = curh + growy;
boolean force = !horizontal;
while (force || curw < finalw || curh < finalh) {
int neww = curw + hinc;
int newh = curh + vinc;
if (neww > finalw) neww = finalw;
if (newh > finalh) newh = finalh;
HeapImage dst = (HeapImage)getRenderer().getCompatibleImage(neww, newh);
int newscan = dst.getScanlineStride();
int[] newPixels = dst.getPixelArray();
if (iterations == 0) {
spread = 0f;
}
if (horizontal) {
filterHorizontalBlack(newPixels, neww, newh, newscan,
curPixels, curw, curh, curscan,
spread);
} else if (neww < finalw || newh < finalh) {
filterVerticalBlack(newPixels, neww, newh, newscan,
curPixels, curw, curh, curscan,
spread);
} else {
float shadowColor[] =
brstate.getShadowColor().getPremultipliedRGBComponents();
if (shadowColor[3] == 1f &&
shadowColor[0] == 0f &&
shadowColor[1] == 0f &&
shadowColor[2] == 0f)
{
filterVerticalBlack(newPixels, neww, newh, newscan,
curPixels, curw, curh, curscan,
spread);
} else {
filterVertical(newPixels, neww, newh, newscan,
curPixels, curw, curh, curscan,
spread, shadowColor);
}
}
if (cur != src) {
getRenderer().releaseCompatibleImage(cur);
}
iterations--;
force = false;
cur = dst;
curw = neww;
curh = newh;
curPixels = newPixels;
curscan = newscan;
}
Rectangle resBounds =
new Rectangle(srcr.x - growx/2, srcr.y - growy/2, curw, curh);
return new ImageData(getFilterContext(), cur, resBounds);
}
protected void filterHorizontalBlack(int dstPixels[], int dstw, int dsth, int dstscan,
int srcPixels[], int srcw, int srch, int srcscan,
float spread)
{
int hsize = dstw - srcw + 1;
int amax = hsize * 255;
amax += (255 - amax) * spread;
int kscale = 0x7fffffff / amax;
int amin = (amax / 255);
int srcoff = 0;
int dstoff = 0;
for (int y = 0; y < dsth; y++) {
int suma = 0;
for (int x = 0; x < dstw; x++) {
int rgb;
rgb = (x >= hsize) ? srcPixels[srcoff + x - hsize] : 0;
suma -= (rgb >>> 24);
rgb = (x < srcw) ? srcPixels[srcoff + x] : 0;
suma += (rgb >>> 24);
dstPixels[dstoff + x] =
((suma < amin) ? 0
: ((suma >= amax) ? 0xff000000
: (((suma * kscale) >> 23) << 24)));
}
srcoff += srcscan;
dstoff += dstscan;
}
}
protected void filterVerticalBlack(int dstPixels[], int dstw, int dsth, int dstscan,
int srcPixels[], int srcw, int srch, int srcscan,
float spread)
{
int vsize = dsth - srch + 1;
int amax = vsize * 255;
amax += (255 - amax) * spread;
int kscale = 0x7fffffff / amax;
int amin = (amax / 255);
int voff = vsize * srcscan;
for (int x = 0; x < dstw; x++) {
int suma = 0;
int srcoff = x;
int dstoff = x;
for (int y = 0; y < dsth; y++) {
int rgb;
rgb = (srcoff >= voff) ? srcPixels[srcoff - voff] : 0;
suma -= (rgb >>> 24);
rgb = (y < srch) ? srcPixels[srcoff] : 0;
suma += (rgb >>> 24);
dstPixels[dstoff] =
((suma < amin) ? 0
: ((suma >= amax) ? 0xff000000
: (((suma * kscale) >> 23) << 24)));
srcoff += srcscan;
dstoff += dstscan;
}
}
}
protected void filterVertical(int dstPixels[], int dstw, int dsth, int dstscan,
int srcPixels[], int srcw, int srch, int srcscan,
float spread, float shadowColor[])
{
int vsize = dsth - srch + 1;
int amax = vsize * 255;
amax += (255 - amax) * spread;
int kscalea = 0x7fffffff / amax;
int kscaler = (int) (kscalea * shadowColor[0]);
int kscaleg = (int) (kscalea * shadowColor[1]);
int kscaleb = (int) (kscalea * shadowColor[2]);
kscalea *= shadowColor[3];
int amin = (amax / 255);
int voff = vsize * srcscan;
int shadowRGB =
(((int) (shadowColor[0] * 255)) << 16) |
(((int) (shadowColor[1] * 255)) << 8) |
(((int) (shadowColor[2] * 255)) ) |
(((int) (shadowColor[3] * 255)) << 24);
for (int x = 0; x < dstw; x++) {
int suma = 0;
int srcoff = x;
int dstoff = x;
for (int y = 0; y < dsth; y++) {
int rgb;
rgb = (srcoff >= voff) ? srcPixels[srcoff - voff] : 0;
suma -= (rgb >>> 24);
rgb = (y < srch) ? srcPixels[srcoff] : 0;
suma += (rgb >>> 24);
dstPixels[dstoff] =
((suma < amin) ? 0
: ((suma >= amax) ? shadowRGB
: ((((suma * kscalea) >> 23) << 24) |
(((suma * kscaler) >> 23) << 16) |
(((suma * kscaleg) >> 23) << 8) |
(((suma * kscaleb) >> 23) ))));
srcoff += srcscan;
dstoff += dstscan;
}
}
}
}
