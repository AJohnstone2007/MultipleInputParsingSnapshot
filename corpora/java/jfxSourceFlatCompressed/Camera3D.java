package fx83dfeatures.utils3d;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Affine;
public class Camera3D extends PerspectiveCamera {
public final Vec3d FORWARD = new Vec3d(0,0,1);
public final Vec3d UP = new Vec3d(0,-1,0);
public final Vec3d RIGHT = new Vec3d(1,0,0);
private final Affine transform = new Affine();
private final DoubleProperty upX = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getUpX() { return upX.getValue(); }
public final void setUpX(double value) { upX.setValue(value); }
public final DoubleProperty upXModel() { return upX; }
private final DoubleProperty upY = new SimpleDoubleProperty(-1){
@Override protected void invalidated() { updateLookup(); }
};
public final double getUpY() { return upY.getValue(); }
public final void setUpY(double value) { upY.setValue(value); }
public final DoubleProperty upYModel() { return upY; }
private final DoubleProperty upZ = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getUpZ() { return upZ.getValue(); }
public final void setUpZ(double value) { upZ.setValue(value); }
public final DoubleProperty upZModel() { return upZ; }
private final DoubleProperty targetX = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getTargetX() { return targetX.getValue(); }
public final void setTargetX(double value) { targetX.setValue(value); }
public final DoubleProperty targetXModel() { return targetX; }
private final DoubleProperty targetY = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getTargetY() { return targetY.getValue(); }
public final void setTargetY(double value) { targetY.setValue(value); }
public final DoubleProperty targetYModel() { return targetY; }
private final DoubleProperty targetZ = new SimpleDoubleProperty(1){
@Override protected void invalidated() { updateLookup(); }
};
public final double getTargetZ() { return targetZ.getValue(); }
public final void setTargetZ(double value) { targetZ.setValue(value); }
public final DoubleProperty targetZModel() { return targetZ; }
private final DoubleProperty posX = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getPosX() { return posX.getValue(); }
public final void setPosX(double value) { posX.setValue(value); }
public final DoubleProperty posXModel() { return posX; }
private final DoubleProperty posY = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getPosY() { return posY.getValue(); }
public final void setPosY(double value) { posY.setValue(value); }
public final DoubleProperty posYModel() { return posY; }
private final DoubleProperty posZ = new SimpleDoubleProperty(0){
@Override protected void invalidated() { updateLookup(); }
};
public final double getPosZ() { return posZ.getValue(); }
public final void setPosZ(double value) { posZ.setValue(value); }
public final DoubleProperty posZModel() { return posZ; }
public Camera3D() {
super(true);
getTransforms().add(transform);
updateLookup();
}
private final Vec3f tm[] = new Vec3f[]{new Vec3f(),new Vec3f(),new Vec3f(),};
private void updateLookup() {
Vec3f pos = new Vec3f((float)getPosX(), (float)getPosY(), (float)getPosZ());
Vec3f target = new Vec3f((float)getTargetX(), (float)getTargetY(), (float)getTargetZ());
Vec3f up = new Vec3f((float)getUpX(), (float)getUpY(), (float)getUpZ());
tm[2].sub(target, pos);
tm[1].set(-up.x, -up.y, -up.z);
tm[0].cross(tm[1], tm[2]);
tm[1].cross(tm[2], tm[0]);
for (int i=0; i!=3; ++i) {
tm[i].normalize();
}
transform.setMxx(tm[0].x); transform.setMxy(tm[1].x); transform.setMxz(tm[2].x);
transform.setMyx(tm[0].y); transform.setMyy(tm[1].y); transform.setMyz(tm[2].y);
transform.setMzx(tm[0].z); transform.setMzy(tm[1].z); transform.setMzz(tm[2].z);
transform.setTx (pos.x); transform.setTy(pos.y); transform.setTz(pos.z);
}
public Affine getTransform() {
return transform;
}
public void setPos(double x, double y, double z) {
setPosX(x);
setPosY(y);
setPosZ(z);
}
public void setTarget(double x, double y, double z) {
setTargetX(x);
setTargetY(y);
setTargetZ(z);
}
public void setUp(double x, double y, double z) {
setUpX(x);
setUpY(y);
setUpZ(z);
}
public Vec3d unProjectDirection(double sceneX, double sceneY, double sWidth, double sHeight) {
Vec3d vMouse = null;
if (isVerticalFieldOfView()) {
} else {
double tanHFov = Math.tan(Math.toRadians(getFieldOfView()) * 0.5f);
vMouse = new Vec3d(2*sceneX/sWidth-1, 2*sceneY/sWidth-sHeight/sWidth, 1);
vMouse.x *= tanHFov;
vMouse.y *= tanHFov;
}
Vec3d result = localToSceneDirection(vMouse, new Vec3d());
result.normalize();
return result;
}
public Vec3d getPosition() {
return new Vec3d(getPosX(), getPosY(), getPosZ());
}
public Vec3d getTarget() {
return new Vec3d(getTargetX(), getTargetY(), getTargetZ());
}
public Vec3d localToScene(Vec3d pt, Vec3d result) {
Point3D res = localToParentTransformProperty().get().transform(pt.x, pt.y, pt.z);
if (getParent() != null) {
res = getParent().localToSceneTransformProperty().get().transform(res);
}
result.set(res.getX(), res.getY(), res.getZ());
return result;
}
public Vec3d localToSceneDirection(Vec3d dir, Vec3d result) {
localToScene(dir, result);
result.sub(localToScene(new Vec3d(0, 0, 0), new Vec3d()));
return result;
}
public Vec3d getForward() {
Vec3d res = localToSceneDirection(FORWARD, new Vec3d());
res.normalize();
return res;
}
public Vec3d getUp() {
Vec3d res = localToSceneDirection(UP, new Vec3d());
res.normalize();
return res;
}
public Vec3d getRight() {
Vec3d res = localToSceneDirection(RIGHT, new Vec3d());
res.normalize();
return res;
}
@Override
public String toString() {
return "camera3D.setPos(" + posX.get() + ", " + posY.get() + ", "
+ posZ.get() + ");\n"
+ "camera3D.setTarget(" + targetX.get() + ", "
+ targetY.get() + ", " + targetZ.get() + ");\n"
+ "camera3D.setUp(" + upX.get() + ", " + upY.get() + ", "
+ upZ.get() + ");";
}
}
