package fx83dfeatures;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
public class PNTMeshViewer extends Application {
Group root;
PointLight pointLight;
MeshView meshView;
TriangleMesh triMesh;
PhongMaterial material;
int divX = 100;
int divY = 100;
boolean smooth = true;
int smoothGroups[];
float rotateAngle = 0.0f;
float funcValue = -10.0f;
float resolution = 0.1f;
boolean texture = false;
boolean textureSwitch = false;
final Image diffuseMap = new Image("resources/cup_diffuseMap_1024.png");
final Image bumpMap = new Image("resources/cup_normalMap_1024.png");
final static ToysVec3f v1 = new ToysVec3f();
final static ToysVec3f v2 = new ToysVec3f();
static void computeNormal(ToysVec3f pa, ToysVec3f pb, ToysVec3f pc, ToysVec3f normal) {
v1.sub(pb, pa);
v2.sub(pc, pa);
normal.cross(v1, v2);
normal.normalize();
}
final static float meshScale = 20;
final static float minX = -10;
final static float minY = -10;
final static float maxX = 10;
final static float maxY = 10;
double getSinDivX(double x, double y) {
double r = Math.sqrt(x*x + y*y);
return funcValue * (r == 0 ? 1 : Math.sin(r) / r);
}
void computeMesh(int subDivX, int subDivY, float scale) {
final int pointSize = 3;
int numDivX = subDivX + 1;
float[] points = triMesh.getPoints().toArray(null);
float[] normals = triMesh.getNormals().toArray(null);
int[] faces = triMesh.getFaces().toArray(null);
for (int y = 0; y <= subDivY; y++) {
float dy = (float) y / subDivY;
double fy = (1 - dy) * minY + dy * maxY;
for (int x = 0; x <= subDivX; x++) {
float dx = (float) x / subDivX;
double fx = (1 - dx) * minX + dx * maxX;
int index = y * numDivX * pointSize + (x * pointSize);
points[index] = (float) fx * scale;
points[index + 1] = (float) fy * scale;
points[index + 2] = (float) getSinDivX(fx, fy) * scale;
}
}
triMesh.getPoints().set(0, points, 0, points.length);
ToysVec3f[] triPoints = new ToysVec3f[3];
triPoints[0] = new ToysVec3f();
triPoints[1] = new ToysVec3f();
triPoints[2] = new ToysVec3f();
ToysVec3f normal = new ToysVec3f();
for (int index = 0; index < faces.length; index += 9) {
int pIndex = faces[index] * 3;
triPoints[0].x = points[pIndex];
triPoints[0].y = points[pIndex + 1];
triPoints[0].z = points[pIndex + 2];
pIndex = faces[index + 3] * 3;
triPoints[1].x = points[pIndex];
triPoints[1].y = points[pIndex + 1];
triPoints[1].z = points[pIndex + 2];
pIndex = faces[index + 6] * 3;
triPoints[2].x = points[pIndex];
triPoints[2].y = points[pIndex + 1];
triPoints[2].z = points[pIndex + 2];
computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);
assert (faces[index + 1] == faces[index + 4]) && (faces[index + 1] == faces[index + 7]);
int nIndex = faces[index + 1] * 3;
normals[nIndex] = normal.x;
normals[nIndex + 1] = normal.y;
normals[nIndex + 2] = normal.z;
}
triMesh.getNormals().set(0, normals, 0, normals.length);
}
TriangleMesh buildTriangleMesh(int subDivX, int subDivY,
float scale) {
final int pointSize = 3;
final int normalSize = 3;
final int texCoordSize = 2;
final int faceSize = 9;
int numDivX = subDivX + 1;
int numVerts = (subDivY + 1) * numDivX;
float points[] = new float[numVerts * pointSize];
float texCoords[] = new float[numVerts * texCoordSize];
int faceCount = subDivX * subDivY * 2;
float normals[] = new float[faceCount * normalSize];
int faces[] = new int[ faceCount * faceSize];
for (int y = 0; y <= subDivY; y++) {
float dy = (float) y / subDivY;
double fy = (1 - dy) * minY + dy * maxY;
for (int x = 0; x <= subDivX; x++) {
float dx = (float) x / subDivX;
double fx = (1 - dx) * minX + dx * maxX;
int index = y * numDivX * pointSize + (x * pointSize);
points[index] = (float) fx * scale;
points[index + 1] = (float) fy * scale;
points[index + 2] = (float) getSinDivX(fx, fy) * scale;
index = y * numDivX * texCoordSize + (x * texCoordSize);
texCoords[index] = dx;
texCoords[index + 1] = dy;
}
}
int normalCount = 0;
ToysVec3f[] triPoints = new ToysVec3f[3];
triPoints[0] = new ToysVec3f();
triPoints[1] = new ToysVec3f();
triPoints[2] = new ToysVec3f();
ToysVec3f normal = new ToysVec3f();
for (int y = 0; y < subDivY; y++) {
for (int x = 0; x < subDivX; x++) {
int p00 = y * numDivX + x;
int p01 = p00 + 1;
int p10 = p00 + numDivX;
int p11 = p10 + 1;
int tc00 = y * numDivX + x;
int tc01 = tc00 + 1;
int tc10 = tc00 + numDivX;
int tc11 = tc10 + 1;
int ii = p00 * 3;
triPoints[0].x = points[ii];
triPoints[0].y = points[ii + 1];
triPoints[0].z = points[ii + 2];
ii = p10 * 3;
triPoints[1].x = points[ii];
triPoints[1].y = points[ii + 1];
triPoints[1].z = points[ii + 2];
ii = p11 * 3;
triPoints[2].x = points[ii];
triPoints[2].y = points[ii + 1];
triPoints[2].z = points[ii + 2];
computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);
int normalIndex = normalCount * normalSize;
normals[normalIndex] = normal.x;
normals[normalIndex + 1] = normal.y;
normals[normalIndex + 2] = normal.z;
int index = (y * subDivX * faceSize + (x * faceSize)) * 2;
faces[index + 0] = p00;
faces[index + 1] = normalCount;
faces[index + 2] = tc00;
faces[index + 3] = p10;
faces[index + 4] = normalCount;
faces[index + 5] = tc10;
faces[index + 6] = p11;
faces[index + 7] = normalCount++;
faces[index + 8] = tc11;
index += faceSize;
ii = p11 * 3;
triPoints[0].x = points[ii];
triPoints[0].y = points[ii + 1];
triPoints[0].z = points[ii + 2];
ii = p01 * 3;
triPoints[1].x = points[ii];
triPoints[1].y = points[ii + 1];
triPoints[1].z = points[ii + 2];
ii = p00 * 3;
triPoints[2].x = points[ii];
triPoints[2].y = points[ii + 1];
triPoints[2].z = points[ii + 2];
computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);
normalIndex = normalCount * normalSize;
normals[normalIndex] = normal.x;
normals[normalIndex + 1] = normal.y;
normals[normalIndex + 2] = normal.z;
faces[index + 0] = p11;
faces[index + 1] = normalCount;
faces[index + 2] = tc11;
faces[index + 3] = p01;
faces[index + 4] = normalCount;
faces[index + 5] = tc01;
faces[index + 6] = p00;
faces[index + 7] = normalCount++;
faces[index + 8] = tc00;
}
}
TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
triangleMesh.getPoints().setAll(points);
triangleMesh.getNormals().setAll(normals);
triangleMesh.getTexCoords().setAll(texCoords);
triangleMesh.getFaces().setAll(faces);
return triangleMesh;
}
private Scene buildScene(int width, int height, boolean depthBuffer) {
triMesh = buildTriangleMesh(divX, divY, meshScale);
smoothGroups = new int[divX * divY * 2];
material = new PhongMaterial();
material.setDiffuseColor(Color.LIGHTGRAY);
material.setSpecularColor(Color.WHITE);
material.setSpecularPower(64);
meshView = new MeshView(triMesh);
meshView.setMaterial(material);
meshView.setDrawMode(DrawMode.FILL);
meshView.setCullFace(CullFace.BACK);
final Group grp1 = new Group(meshView);
grp1.setRotate(0);
grp1.setRotationAxis(Rotate.X_AXIS);
Group grp2 = new Group(grp1);
grp2.setRotate(-30);
grp2.setRotationAxis(Rotate.X_AXIS);
Group grp3 = new Group(grp2);
grp3.setTranslateX(400);
grp3.setTranslateY(400);
grp3.setTranslateZ(10);
pointLight = new PointLight(Color.ANTIQUEWHITE);
pointLight.setTranslateX(300);
pointLight.setTranslateY(-50);
pointLight.setTranslateZ(-1000);
root = new Group(grp3, pointLight);
Scene scene = new Scene(root, width, height, depthBuffer);
scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
@Override
public void handle(KeyEvent e) {
switch (e.getCharacter()) {
case "[":
funcValue -= resolution;
if (funcValue < -20.0f) {
funcValue = -20.0f;
}
computeMesh(divX, divY, meshScale);
break;
case "]":
funcValue += resolution;
if (funcValue > 20.0f) {
funcValue = 20.0f;
}
computeMesh(divX, divY, meshScale);
break;
case "p":
funcValue = -10;
computeMesh(divX, divY, meshScale);
break;
case "i":
System.err.print("i ");
if (!textureSwitch) {
texture = texture ? false : true;
} else {
textureSwitch = false;
}
if (texture) {
material.setDiffuseMap(diffuseMap);
material.setBumpMap(bumpMap);
material.setDiffuseColor(Color.WHITE);
} else {
material.setDiffuseMap(null);
material.setBumpMap(null);
material.setDiffuseColor(Color.LIGHTGRAY);
}
break;
case "s":
smooth = !smooth;
if (smooth) {
Arrays.fill(smoothGroups, 0);
} else {
for (int i = 0; i < smoothGroups.length; i++) {
smoothGroups[i] = i % 32;
}
}
triMesh.getFaceSmoothingGroups().setAll(smoothGroups);
break;
case "k":
System.err.print("k ");
if ((texture) || (!textureSwitch)) {
material.setDiffuseMap(diffuseMap);
material.setBumpMap(null);
material.setDiffuseColor(Color.WHITE);
texture = true;
textureSwitch = true;
} else {
material.setDiffuseMap(null);
material.setBumpMap(null);
material.setDiffuseColor(Color.LIGHTGRAY);
}
break;
case "u":
System.err.print("u ");
if (texture) {
material.setDiffuseMap(null);
material.setBumpMap(bumpMap);
material.setDiffuseColor(Color.LIGHTGRAY);
textureSwitch = true;
} else {
material.setDiffuseMap(null);
material.setBumpMap(null);
material.setDiffuseColor(Color.LIGHTGRAY);
}
break;
case "l":
System.err.print("l ");
boolean wireframe = meshView.getDrawMode() == DrawMode.LINE;
meshView.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
break;
case "<":
grp1.setRotate(rotateAngle -= (resolution * 5));
break;
case ">":
grp1.setRotate(rotateAngle += (resolution * 5));
break;
case "X":
grp1.setRotationAxis(Rotate.X_AXIS);
break;
case "Y":
grp1.setRotationAxis(Rotate.Y_AXIS);
break;
case "Z":
grp1.setRotationAxis(Rotate.Z_AXIS);
break;
case "P":
rotateAngle = 0;
grp1.setRotate(rotateAngle);
case "1":
System.err.print("1 ");
divX = 5;
divY = 5;
smooth = true;
smoothGroups = new int[divX * divY * 2];
rotateAngle = 0.0f;
funcValue = 0.0f;
triMesh = buildTriangleMesh(divX, divY, meshScale);
meshView.setMesh(triMesh);
break;
case "2":
System.err.print("2 ");
divX = 70;
divY = 70;
smooth = true;
smoothGroups = new int[divX * divY * 2];
rotateAngle = 0.0f;
funcValue = 0.0f;
triMesh = buildTriangleMesh(divX, divY, meshScale);
meshView.setMesh(triMesh);
break;
case " ":
root.getChildren().add(new Button("Button"));
break;
}
}
});
return scene;
}
private PerspectiveCamera addCamera(Scene scene) {
PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
scene.setCamera(perspectiveCamera);
return perspectiveCamera;
}
@Override
public void start(Stage primaryStage) {
Scene scene = buildScene(800, 800, true);
scene.setFill(Color.rgb(10, 10, 40));
addCamera(scene);
primaryStage.setTitle("PNT MeshViewer");
primaryStage.setScene(scene);
primaryStage.show();
}
public static void main(String[] args) {
launch(args);
}
}
