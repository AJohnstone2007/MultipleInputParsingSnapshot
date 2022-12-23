package com.sun.scenario.effect.impl.state;
import com.sun.scenario.effect.Color4f;
public class BoxShadowState extends BoxBlurState {
private Color4f shadowColor;
private float spread;
@Override
public Color4f getShadowColor() {
return shadowColor;
}
public void setShadowColor(Color4f shadowColor) {
if (shadowColor == null) {
throw new IllegalArgumentException("Color must be non-null");
}
this.shadowColor = shadowColor;
}
@Override
public float getSpread() {
return spread;
}
public void setSpread(float spread) {
if (spread < 0f || spread > 1f) {
throw new IllegalArgumentException("Spread must be in the range [0,1]");
}
this.spread = spread;
}
@Override
public boolean isNop() {
return false;
}
@Override
public boolean isShadow() {
return true;
}
}
