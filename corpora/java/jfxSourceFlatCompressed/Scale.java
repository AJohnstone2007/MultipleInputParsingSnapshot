package javafx.scene.transform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
public class Scale extends Transform {
public Scale() {
}
public Scale(double x, double y) {
setX(x);
setY(y);
}
public Scale(double x, double y, double pivotX, double pivotY) {
this(x, y);
setPivotX(pivotX);
setPivotY(pivotY);
}
public Scale(double x, double y, double z) {
this(x, y);
setZ(z);
}
public Scale(double x, double y, double z, double pivotX, double pivotY, double pivotZ) {
this(x, y, pivotX, pivotY);
setZ(z);
setPivotZ(pivotZ);
}
private DoubleProperty x;
public final void setX(double value) {
xProperty().set(value);
}
public final double getX() {
return x == null ? 1.0F : x.get();
}
public final DoubleProperty xProperty() {
if (x == null) {
x = new DoublePropertyBase(1.0F) {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "x";
}
};
}
return x;
}
private DoubleProperty y;
public final void setY(double value) {
yProperty().set(value);
}
public final double getY() {
return y == null ? 1.0F : y.get();
}
public final DoubleProperty yProperty() {
if (y == null) {
y = new DoublePropertyBase(1.0F) {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "y";
}
};
}
return y;
}
private DoubleProperty z;
public final void setZ(double value) {
zProperty().set(value);
}
public final double getZ() {
return z == null ? 1.0F : z.get();
}
public final DoubleProperty zProperty() {
if (z == null) {
z = new DoublePropertyBase(1.0F) {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "z";
}
};
}
return z;
}
private DoubleProperty pivotX;
public final void setPivotX(double value) {
pivotXProperty().set(value);
}
public final double getPivotX() {
return pivotX == null ? 0.0 : pivotX.get();
}
public final DoubleProperty pivotXProperty() {
if (pivotX == null) {
pivotX = new DoublePropertyBase() {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "pivotX";
}
};
}
return pivotX;
}
private DoubleProperty pivotY;
public final void setPivotY(double value) {
pivotYProperty().set(value);
}
public final double getPivotY() {
return pivotY == null ? 0.0 : pivotY.get();
}
public final DoubleProperty pivotYProperty() {
if (pivotY == null) {
pivotY = new DoublePropertyBase() {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "pivotY";
}
};
}
return pivotY;
}
private DoubleProperty pivotZ;
public final void setPivotZ(double value) {
pivotZProperty().set(value);
}
public final double getPivotZ() {
return pivotZ == null ? 0.0 : pivotZ.get();
}
public final DoubleProperty pivotZProperty() {
if (pivotZ == null) {
pivotZ = new DoublePropertyBase() {
@Override
public void invalidated() {
transformChanged();
}
@Override
public Object getBean() {
return Scale.this;
}
@Override
public String getName() {
return "pivotZ";
}
};
}
return pivotZ;
}
@Override
public double getMxx() {
return getX();
}
@Override
public double getMyy() {
return getY();
}
@Override
public double getMzz() {
return getZ();
}
@Override
public double getTx() {
return (1-getX()) * getPivotX();
}
@Override
public double getTy() {
return (1-getY()) * getPivotY();
}
@Override
public double getTz() {
return (1-getZ()) * getPivotZ();
}
@Override
boolean computeIs2D() {
return getZ() == 1.0;
}
@Override
boolean computeIsIdentity() {
return getX() == 1.0 && getY() == 1.0 && getZ() == 1.0;
}
@Override
void fill2DArray(double[] array) {
final double sx = getX();
final double sy = getY();
array[0] = sx;
array[1] = 0.0;
array[2] = (1-sx) * getPivotX();
array[3] = 0.0;
array[4] = sy;
array[5] = (1-sy) * getPivotY();
}
@Override
void fill3DArray(double[] array) {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
array[0] = sx;
array[1] = 0.0;
array[2] = 0.0;
array[3] = (1-sx) * getPivotX();
array[4] = 0.0;
array[5] = sy;
array[6] = 0.0;
array[7] = (1-sy) * getPivotY();
array[8] = 0.0;
array[9] = 0.0;
array[10] = sz;
array[11] = (1-sz) * getPivotZ();
}
@Override
public Transform createConcatenation(Transform transform) {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
if (transform instanceof Scale) {
final Scale other = (Scale) transform;
if (other.getPivotX() == getPivotX()
&& other.getPivotY() == getPivotY()
&& other.getPivotZ() == getPivotZ()) {
return new Scale(
sx * other.getX(),
sy * other.getY(),
sz * other.getZ(),
getPivotX(), getPivotY(), getPivotZ());
}
}
if (transform instanceof Translate) {
final Translate t = (Translate) transform;
final double tx = t.getX();
final double ty = t.getY();
final double tz = t.getZ();
if ((tx == 0.0 || (sx != 1.0 && sx != 0.0)) &&
(ty == 0.0 || (sy != 1.0 && sy != 0.0)) &&
(tz == 0.0 || (sz != 1.0 && sz != 0.0))) {
return new Scale(
sx, sy, sz,
(sx != 1.0 ? sx * tx / (1 - sx) : 0) + getPivotX(),
(sy != 1.0 ? sy * ty / (1 - sy) : 0) + getPivotY(),
(sz != 1.0 ? sz * tz / (1 - sz) : 0) + getPivotZ());
}
}
if (transform instanceof Affine) {
Affine a = (Affine) transform.clone();
a.prepend(this);
return a;
}
final double txx = transform.getMxx();
final double txy = transform.getMxy();
final double txz = transform.getMxz();
final double ttx = transform.getTx();
final double tyx = transform.getMyx();
final double tyy = transform.getMyy();
final double tyz = transform.getMyz();
final double tty = transform.getTy();
final double tzx = transform.getMzx();
final double tzy = transform.getMzy();
final double tzz = transform.getMzz();
final double ttz = transform.getTz();
return new Affine(
sx * txx, sx * txy, sx * txz, sx * ttx + (1 - sx) * getPivotX(),
sy * tyx, sy * tyy, sy * tyz, sy * tty + (1 - sy) * getPivotY(),
sz * tzx, sz * tzy, sz * tzz, sz * ttz + (1 - sz) * getPivotZ());
}
@Override
public Scale createInverse() throws NonInvertibleTransformException {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
if (sx == 0.0 || sy == 0.0 || sz == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
return new Scale(1.0 / sx, 1.0 / sy, 1.0 / sz,
getPivotX(), getPivotY(), getPivotZ());
}
@Override
public Scale clone() {
return new Scale(getX(), getY(), getZ(),
getPivotX(), getPivotY(), getPivotZ());
}
@Override
public Point2D transform(double x, double y) {
ensureCanTransform2DPoint();
final double mxx = getX();
final double myy = getY();
return new Point2D(
mxx * x + (1 - mxx) * getPivotX(),
myy * y + (1 - myy) * getPivotY());
}
@Override
public Point3D transform(double x, double y, double z) {
final double mxx = getX();
final double myy = getY();
final double mzz = getZ();
return new Point3D(
mxx * x + (1 - mxx) * getPivotX(),
myy * y + (1 - myy) * getPivotY(),
mzz * z + (1 - mzz) * getPivotZ());
}
@Override
void transform2DPointsImpl(double[] srcPts, int srcOff,
double[] dstPts, int dstOff, int numPts) {
final double xx = getX();
final double yy = getY();
final double px = getPivotX();
final double py = getPivotY();
while (--numPts >= 0) {
final double x = srcPts[srcOff++];
final double y = srcPts[srcOff++];
dstPts[dstOff++] = xx * x + (1 - xx) * px;
dstPts[dstOff++] = yy * y + (1 - yy) * py;
}
}
@Override
void transform3DPointsImpl(double[] srcPts, int srcOff,
double[] dstPts, int dstOff, int numPts) {
final double xx = getX();
final double yy = getY();
final double zz = getZ();
final double px = getPivotX();
final double py = getPivotY();
final double pz = getPivotZ();
while (--numPts >= 0) {
dstPts[dstOff++] = xx * srcPts[srcOff++] + (1 - xx) * px;
dstPts[dstOff++] = yy * srcPts[srcOff++] + (1 - yy) * py;
dstPts[dstOff++] = zz * srcPts[srcOff++] + (1 - zz) * pz;
}
}
@Override
public Point2D deltaTransform(double x, double y) {
ensureCanTransform2DPoint();
return new Point2D(
getX() * x,
getY() * y);
}
@Override
public Point3D deltaTransform(double x, double y, double z) {
return new Point3D(
getX() * x,
getY() * y,
getZ() * z);
}
@Override
public Point2D inverseTransform(double x, double y)
throws NonInvertibleTransformException {
ensureCanTransform2DPoint();
final double sx = getX();
final double sy = getY();
if (sx == 0.0 || sy == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
final double mxx = 1.0 / sx;
final double myy = 1.0 / sy;
return new Point2D(
mxx * x + (1 - mxx) * getPivotX(),
myy * y + (1 - myy) * getPivotY());
}
@Override
public Point3D inverseTransform(double x, double y, double z)
throws NonInvertibleTransformException {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
if (sx == 0.0 || sy == 0.0 || sz == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
final double mxx = 1.0 / sx;
final double myy = 1.0 / sy;
final double mzz = 1.0 / sz;
return new Point3D(
mxx * x + (1 - mxx) * getPivotX(),
myy * y + (1 - myy) * getPivotY(),
mzz * z + (1 - mzz) * getPivotZ());
}
@Override
void inverseTransform2DPointsImpl(double[] srcPts, int srcOff,
double[] dstPts, int dstOff, int numPts)
throws NonInvertibleTransformException {
final double sx = getX();
final double sy = getY();
if (sx == 0.0 || sy == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
final double xx = 1.0 / sx;
final double yy = 1.0 / sy;
final double px = getPivotX();
final double py = getPivotY();
while (--numPts >= 0) {
dstPts[dstOff++] = xx * srcPts[srcOff++] + (1 - xx) * px;
dstPts[dstOff++] = yy * srcPts[srcOff++] + (1 - yy) * py;
}
}
@Override
void inverseTransform3DPointsImpl(double[] srcPts, int srcOff,
double[] dstPts, int dstOff, int numPts)
throws NonInvertibleTransformException {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
if (sx == 0.0 || sy == 0.0 || sz == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
final double xx = 1.0 / sx;
final double yy = 1.0 / sy;
final double zz = 1.0 / sz;
final double px = getPivotX();
final double py = getPivotY();
final double pz = getPivotZ();
while (--numPts >= 0) {
dstPts[dstOff++] = xx * srcPts[srcOff++] + (1 - xx) * px;
dstPts[dstOff++] = yy * srcPts[srcOff++] + (1 - yy) * py;
dstPts[dstOff++] = zz * srcPts[srcOff++] + (1 - zz) * pz;
}
}
@Override
public Point2D inverseDeltaTransform(double x, double y)
throws NonInvertibleTransformException {
ensureCanTransform2DPoint();
final double sx = getX();
final double sy = getY();
if (sx == 0.0 || sy == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
return new Point2D(
(1.0 / sx) * x,
(1.0 / sy) * y);
}
@Override
public Point3D inverseDeltaTransform(double x, double y, double z)
throws NonInvertibleTransformException {
final double sx = getX();
final double sy = getY();
final double sz = getZ();
if (sx == 0.0 || sy == 0.0 || sz == 0.0) {
throw new NonInvertibleTransformException(
"Zero scale is not invertible");
}
return new Point3D(
(1.0 / sx) * x,
(1.0 / sy) * y,
(1.0 / sz) * z);
}
@Override
public String toString() {
final StringBuilder sb = new StringBuilder("Scale [");
sb.append("x=").append(getX());
sb.append(", y=").append(getY());
sb.append(", z=").append(getZ());
sb.append(", pivotX=").append(getPivotX());
sb.append(", pivotY=").append(getPivotY());
sb.append(", pivotZ=").append(getPivotZ());
return sb.append("]").toString();
}
@Override
void apply(final Affine3D trans) {
if (getPivotX() != 0 || getPivotY() != 0 || getPivotZ() != 0) {
trans.translate(getPivotX(), getPivotY(), getPivotZ());
trans.scale(getX(), getY(), getZ());
trans.translate(-getPivotX(), -getPivotY(), -getPivotZ());
} else {
trans.scale(getX(), getY(), getZ());
}
}
@Override
BaseTransform derive(BaseTransform trans) {
if (isIdentity()) {
return trans;
}
if (getPivotX() != 0 || getPivotY() != 0 || getPivotZ() != 0) {
trans = trans.deriveWithTranslation(getPivotX(), getPivotY(), getPivotZ());
trans = trans.deriveWithScale(getX(), getY(), getZ());
return trans.deriveWithTranslation(-getPivotX(), -getPivotY(), -getPivotZ());
} else {
return trans.deriveWithScale(getX(), getY(), getZ());
}
}
@Override
void validate() {
getX(); getPivotX();
getY(); getPivotY();
getZ(); getPivotZ();
}
@Override
void appendTo(Affine a) {
a.appendScale(getX(), getY(), getZ(),
getPivotX(), getPivotY(), getPivotZ());
}
@Override
void prependTo(Affine a) {
a.prependScale(getX(), getY(), getZ(),
getPivotX(), getPivotY(), getPivotZ());
}
}
