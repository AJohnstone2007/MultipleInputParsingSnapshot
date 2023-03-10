package fx83dfeatures;
import fx83dfeatures.utils3d.CameraController;
import fx83dfeatures.utils3d.Camera3D;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
public class SpecularColorTestApp extends Application {
private ScrollBar specularPowerScroll;
private ColorPicker specularColorPicker;
private CheckBox specularColorCheckBox, specularMapCheckBox;
Image diffuseMap = new Image("resources/cup_diffuseMap_1024.png");
Image specularGrayMap = new Image("resources/spec.png");
Image specularColorMap = new Image("resources/spec_color.png");
private List<PhongMaterial> materials;
private void addSphere(Group g, int x, int y, PhongMaterial material) {
Sphere s = new Sphere(10);
s.setMaterial(material);
s.setTranslateX(x);
s.setTranslateY(y);
g.getChildren().add(s);
}
interface MaterialModifier {
public void modify(PhongMaterial mat);
}
private void addMaterials(MaterialModifier modifier) {
PhongMaterial mat = new PhongMaterial(Color.ANTIQUEWHITE);
modifier.modify(mat);
materials.add(mat);
mat = new PhongMaterial();
mat.setDiffuseMap(diffuseMap);
modifier.modify(mat);
materials.add(mat);
mat = new PhongMaterial(Color.RED);
mat.setDiffuseMap(diffuseMap);
modifier.modify(mat);
materials.add(mat);
}
private void bindMaterial(PhongMaterial m, ObservableValue color, ObservableValue map) {
if (color != null) {
m.specularColorProperty().bind(color);
}
if (map != null) {
m.specularMapProperty().bind(map);
}
m.specularPowerProperty().bind(specularPowerScroll.valueProperty());
}
private void addSpheres(Group g) {
materials = new ArrayList();
addMaterials(m -> {});
addMaterials(m -> m.setSpecularPower(100));
addMaterials(m -> m.setSpecularColor(Color.WHITE));
ObjectBinding specularColorBind = Bindings.createObjectBinding(
() -> specularColorCheckBox.isSelected() ?
specularColorPicker.getValue() : null,
specularColorPicker.valueProperty(),
specularColorCheckBox.selectedProperty()
);
ObjectBinding specularGrayMapBind = Bindings.createObjectBinding(
() -> specularMapCheckBox.isSelected() ?
specularGrayMap : null,
specularMapCheckBox.selectedProperty()
);
ObjectBinding specularColorMapBind = Bindings.createObjectBinding(
() -> specularMapCheckBox.isSelected() ?
specularColorMap : null,
specularMapCheckBox.selectedProperty()
);
addMaterials(m -> bindMaterial(m, specularColorBind, null));
addMaterials(m -> m.specularMapProperty().bind(specularGrayMapBind));
addMaterials(m -> m.specularMapProperty().bind(specularColorMapBind));
addMaterials(m -> bindMaterial(m, null, specularGrayMapBind));
addMaterials(m -> bindMaterial(m, null, specularColorMapBind));
addMaterials(m -> bindMaterial(m, specularColorBind, specularGrayMapBind));
addMaterials(m -> bindMaterial(m, specularColorBind, specularColorMapBind));
int numX = 6, numY = ( materials.size() + numX - 1 ) / numX;
for (int y = 0; y < numY; y++) {
for (int x = 0; x < numX; x++) {
int idx = y * numX + x;
if (idx < materials.size()) {
int xPos = -50 + x * 100 / (numX - 1);
int yPos = -40 + y * 80 / (numY - 1);
addSphere(g, xPos, yPos, materials.get(idx));
}
}
}
}
private Node createControls() {
specularPowerScroll = new ScrollBar();
specularPowerScroll.setValue(new PhongMaterial().getSpecularPower());
specularPowerScroll.setMin(1);
specularPowerScroll.setMax(100);
specularColorCheckBox = new CheckBox("Specular Color");
specularColorCheckBox.setSelected(true);
specularColorPicker = new ColorPicker(Color.color(0.5, 1, 0));
ImageView diffView = createImageView(diffuseMap);
specularMapCheckBox = new CheckBox("Specular Map");
specularMapCheckBox.setSelected(true);
ImageView specGrayView = createImageView(specularGrayMap);
ImageView specColorView = createImageView(specularColorMap);
VBox controls = new VBox(15);
controls.setPadding(new Insets(15));
GridPane grid = new GridPane();
grid.setHgap(5);
grid.setVgap(5);
grid.addRow(0, new Label("Diffuse"), new Label("Specuar power only"));
grid.addRow(1, new Label("White specular color"), new Label("Specular color and power"));
grid.addRow(3, new Label("Gray map"), new Label("Color map"));
grid.addRow(4, new Label("Gray map and power"), new Label("Color map and power"));
grid.addRow(5, new Label("Gray map with color"), new Label("Color map with color"));
controls.getChildren().addAll(specularColorCheckBox, specularColorPicker, specularPowerScroll,
grid, diffView, specularMapCheckBox, new HBox(15, specGrayView, specColorView));
return controls;
}
private ImageView createImageView(Image image) {
ImageView diffView = new ImageView(image);
diffView.setFitWidth(100);
diffView.setPreserveRatio(true);
diffView.setSmooth(true);
diffView.setCache(true);
return diffView;
}
@Override
public void start(Stage stage) throws Exception {
Node controls = createControls();
PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
pointLight.setTranslateZ(-200);
PointLight pointLight2 = new PointLight(Color.ANTIQUEWHITE);
pointLight2.setTranslateX(-250);
pointLight2.setTranslateY(300);
pointLight2.setTranslateZ(-1500);
PointLight pointLight3 = new PointLight(Color.ANTIQUEWHITE);
pointLight3.setTranslateX(250);
pointLight3.setTranslateY(100);
pointLight3.setTranslateZ(500);
Group root1 = new Group(pointLight );
addSpheres(root1);
SubScene scene3D = new SubScene(root1, 1024, 768, true, null);
final Camera3D cam3d = new Camera3D();
cam3d.setPosZ(-200);
cam3d.setFarClip(10000);
scene3D.setCamera(cam3d);
control = new CameraController(cam3d, scene3D);
scene3D.setFill(Color.DARKGRAY);
HBox root = new HBox(scene3D, controls);
Scene scene = new Scene(root, 1324, 768, true);
scene3D.requestFocus();
scene.setCamera(new PerspectiveCamera());
stage.setScene(scene);
stage.show();
}
private CameraController control;
public static void main(String[] args) {
launch(args);
}
}
