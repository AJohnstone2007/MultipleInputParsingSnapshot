package com.sun.javafx.font;
import com.sun.javafx.geom.transform.BaseTransform;
class PrismFont implements PGFont {
private String name;
private float fontSize;
protected FontResource fontResource;
private int features;
PrismFont(FontResource fontResource, String name, float size) {
this.fontResource = fontResource;
this.name = name;
this.fontSize = size;
}
public String getFullName() {
return fontResource.getFullName();
}
public String getFamilyName() {
return fontResource.getFamilyName();
}
public String getStyleName() {
return fontResource.getStyleName();
}
@Override public int getFeatures() {
return features;
}
public String getName() {
return name;
}
public float getSize() {
return fontSize;
}
public FontStrike getStrike(BaseTransform transform) {
return fontResource.getStrike(fontSize, transform);
}
public FontStrike getStrike(BaseTransform transform,
int smoothingType) {
return fontResource.getStrike(fontSize, transform, smoothingType);
}
public FontResource getFontResource() {
return fontResource;
}
@Override
public boolean equals(Object obj) {
if (obj == null) {
return false;
}
if (!(obj instanceof PrismFont)) {
return false;
}
final PrismFont other = (PrismFont) obj;
return
this.fontSize == other.fontSize &&
this.fontResource.equals(other.fontResource);
}
private int hash;
@Override
public int hashCode() {
if (hash != 0) {
return hash;
}
else {
hash = 497 + Float.floatToIntBits(fontSize);
hash = 71 * hash + fontResource.hashCode();
return hash;
}
}
}
