package fx83dfeatures;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
public class PNTBoxSmoothViewer extends Application {
Group root;
PointLight pointLight;
MeshView meshView;
TriangleMesh triMesh;
PhongMaterial material;
float resolution = 0.1f;
float rotateAngle = 0.0f;
boolean texture = false;
boolean textureSwitch = false;
final Image diffuseMap = new Image("resources/cup_diffuseMap_1024.png");
final Image bumpMap = new Image("resources/cup_normalMap_1024.png");
private PerspectiveCamera addCamera(Scene scene) {
PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
scene.setCamera(perspectiveCamera);
return perspectiveCamera;
}
private Scene buildScene(int width, int height, boolean depthBuffer) {
triMesh = createBox(200, 200, 200);
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
grp2.setRotate(-90);
grp2.setRotationAxis(Rotate.X_AXIS);
Group grp3 = new Group(grp2);
grp3.setTranslateX(400);
grp3.setTranslateY(400);
grp3.setTranslateZ(10);
pointLight = new PointLight(Color.ANTIQUEWHITE);
pointLight.setTranslateX(300);
pointLight.setTranslateY(-50);
pointLight.setTranslateZ(-1000);
root = new Group(grp3 );
Scene scene = new Scene(root, width, height, depthBuffer);
scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
@Override
public void handle(KeyEvent e) {
switch (e.getCharacter()) {
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
case " ":
root.getChildren().add(new Button("Button"));
break;
}
}
});
return scene;
}
@Override
public void start(Stage primaryStage) {
Scene scene = buildScene(800, 800, true);
scene.setFill(Color.rgb(10, 10, 40));
addCamera(scene);
primaryStage.setTitle("PNT BOX Smooth Viewer");
primaryStage.setScene(scene);
primaryStage.show();
}
public static void main(String[] args) {
launch(args);
}
TriangleMesh createBox(float w, float h, float d) {
float hw = w / 2f;
float hh = h / 2f;
float hd = d / 2f;
float points[] = {
-hw, -hh, -hd,
hw, -hh, -hd,
hw, hh, -hd,
-hw, hh, -hd,
-hw, -hh, hd,
hw, -hh, hd,
hw, hh, hd,
-hw, hh, hd};
float normals[] = {
-hw, -hh, -hd,
hw, -hh, -hd,
hw, hh, -hd,
-hw, hh, -hd,
-hw, -hh, hd,
hw, -hh, hd,
hw, hh, hd,
-hw, hh, hd};
float texCoords[] = {0, 0, 1, 0, 1, 1, 0, 1};
int faces[] = {
0, 0, 0, 2, 2, 2, 1, 1, 1,
2, 2, 2, 0, 0, 0, 3, 3, 3,
1, 1, 0, 6, 6, 2, 5, 5, 1,
6, 6, 2, 1, 1, 0, 2, 2, 3,
5, 5, 0, 7, 7, 2, 4, 4, 1,
7, 7, 2, 5, 5, 0, 6, 6, 3,
4, 4, 0, 3, 3, 2, 0, 0, 1,
3, 3, 2, 4, 4, 0, 7, 7, 3,
3, 3, 0, 6, 6, 2, 2, 2, 1,
6, 6, 2, 3, 3, 0, 7, 7, 3,
4, 4, 0, 1, 1, 2, 5, 5, 1,
1, 1, 2, 4, 4, 0, 0, 0, 3,
};
ToysVec3f temp = new ToysVec3f();
for (int i = 0; i < normals.length; i += 3) {
temp.x = normals[i];
temp.y = normals[i+1];
temp.z = normals[i+2];
temp.normalize();
normals[i] = temp.x;
normals[i+1] = temp.y;
normals[i+2] = temp.z;
}
TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
triangleMesh.getPoints().setAll(points);
triangleMesh.getNormals().setAll(normals);
triangleMesh.getTexCoords().setAll(texCoords);
triangleMesh.getFaces().setAll(faces);
return triangleMesh;
}
}
