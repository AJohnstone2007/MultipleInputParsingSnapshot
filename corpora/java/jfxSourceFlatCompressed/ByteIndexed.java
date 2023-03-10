package com.sun.javafx.image.impl;
import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.tk.Toolkit;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
public class ByteIndexed {
public static BytePixelGetter createGetter(PixelFormat<ByteBuffer> pf) {
return new Getter(pf);
}
public static ByteToBytePixelConverter
createToByteBgraAny(BytePixelGetter src,
BytePixelSetter dst)
{
return new ToByteBgraAnyConverter(src, dst);
}
public static ByteToIntPixelConverter
createToIntArgbAny(BytePixelGetter src,
IntPixelSetter dst)
{
return new ToIntArgbAnyConverter(src, dst);
}
public static class Getter implements BytePixelGetter {
PixelFormat<ByteBuffer> theFormat;
private int precolors[];
private int nonprecolors[];
Getter(PixelFormat<ByteBuffer> pf) {
theFormat = pf;
}
int[] getPreColors() {
if (precolors == null) {
precolors = Toolkit.getImageAccessor().getPreColors(theFormat);
}
return precolors;
}
int[] getNonPreColors() {
if (nonprecolors == null) {
nonprecolors = Toolkit.getImageAccessor().getNonPreColors(theFormat);
}
return nonprecolors;
}
@Override
public AlphaType getAlphaType() {
return theFormat.isPremultiplied()
? AlphaType.PREMULTIPLIED
: AlphaType.NONPREMULTIPLIED;
}
@Override
public int getNumElements() {
return 1;
}
@Override
public int getArgb(byte[] arr, int offset) {
return getNonPreColors()[arr[offset] & 0xff];
}
@Override
public int getArgbPre(byte[] arr, int offset) {
return getPreColors()[arr[offset] & 0xff];
}
@Override
public int getArgb(ByteBuffer buf, int offset) {
return getNonPreColors()[buf.get(offset) & 0xff];
}
@Override
public int getArgbPre(ByteBuffer buf, int offset) {
return getPreColors()[buf.get(offset) & 0xff];
}
}
static int[] getColors(BytePixelGetter getter, PixelSetter setter) {
ByteIndexed.Getter big = (ByteIndexed.Getter) getter;
return (setter.getAlphaType() == AlphaType.PREMULTIPLIED)
? big.getPreColors()
: big.getNonPreColors();
}
public static class ToByteBgraAnyConverter extends BaseByteToByteConverter {
public ToByteBgraAnyConverter(BytePixelGetter getter, BytePixelSetter setter) {
super(getter, setter);
}
@Override
void doConvert(byte[] srcarr, int srcoff, int srcscanbytes,
byte[] dstarr, int dstoff, int dstscanbytes,
int w, int h)
{
int colors[] = getColors(getGetter(), getSetter());
dstscanbytes -= w * 4;
while (--h >= 0) {
for (int x = 0; x < w; x++) {
int argb = colors[srcarr[srcoff + x] & 0xff];
dstarr[dstoff++] = (byte) (argb );
dstarr[dstoff++] = (byte) (argb >> 8);
dstarr[dstoff++] = (byte) (argb >> 16);
dstarr[dstoff++] = (byte) (argb >> 24);
}
srcoff += srcscanbytes;
dstoff += dstscanbytes;
}
}
@Override
void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes,
ByteBuffer dstbuf, int dstoff, int dstscanbytes,
int w, int h)
{
int colors[] = getColors(getGetter(), getSetter());
dstscanbytes -= w * 4;
while (--h >= 0) {
for (int x = 0; x < w; x++) {
int argb = colors[srcbuf.get(srcoff + x) & 0xff];
dstbuf.put(dstoff , (byte) (argb ));
dstbuf.put(dstoff + 1, (byte) (argb >> 8));
dstbuf.put(dstoff + 2, (byte) (argb >> 16));
dstbuf.put(dstoff + 3, (byte) (argb >> 24));
dstoff += 4;
}
srcoff += srcscanbytes;
dstoff += dstscanbytes;
}
}
}
public static class ToIntArgbAnyConverter extends BaseByteToIntConverter {
public ToIntArgbAnyConverter(BytePixelGetter getter, IntPixelSetter setter) {
super(getter, setter);
}
@Override
void doConvert(byte[] srcarr, int srcoff, int srcscanbytes,
int[] dstarr, int dstoff, int dstscanints,
int w, int h)
{
int colors[] = getColors(getGetter(), getSetter());
while (--h >= 0) {
for (int x = 0; x < w; x++) {
dstarr[dstoff + x] = colors[srcarr[srcoff + x] & 0xff];
}
srcoff += srcscanbytes;
dstoff += dstscanints;
}
}
@Override
void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes,
IntBuffer dstbuf, int dstoff, int dstscanints,
int w, int h)
{
int colors[] = getColors(getGetter(), getSetter());
while (--h >= 0) {
for (int x = 0; x < w; x++) {
dstbuf.put(dstoff + x, colors[srcbuf.get(srcoff + x) & 0xff]);
}
srcoff += srcscanbytes;
dstoff += dstscanints;
}
}
}
}
