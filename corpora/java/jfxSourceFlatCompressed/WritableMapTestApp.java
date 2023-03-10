package fx83dfeatures;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
public class WritableMapTestApp extends Application {
private ColorPicker diffuseMapPicker;
private CheckBox diffuseMapCheckBox;
private ColorPicker specularMapPicker;
private CheckBox specularMapCheckBox;
private ColorPicker selfIllumMapPicker;
private CheckBox selfIllumMapCheckBox;
private Image generateMap(WritableImage writableImage, Image map) {
PixelReader pixelReader = map.getPixelReader();
PixelWriter pixelWriter = writableImage.getPixelWriter();
for (int y = 0; y < writableImage.getHeight(); y++) {
for (int x = 0; x < writableImage.getWidth(); x++) {
Color color = pixelReader.getColor(x, y);
pixelWriter.setColor(x, y, color);
}
}
return writableImage;
}
private Image generateMap(WritableImage writableImage, Color color) {
PixelWriter pixelWriter = writableImage.getPixelWriter();
for (int y = 0; y < writableImage.getHeight(); y++) {
for (int x = 0; x < writableImage.getWidth(); x++) {
pixelWriter.setColor(x, y, color);
}
}
return writableImage;
}
@Override
public void start(Stage stage) throws Exception {
int width = 100;
int height = 100;
final WritableImage diffuseMap = new WritableImage(width, height);
final WritableImage specularMap = new WritableImage(width, height);
final WritableImage selfIllumMap = new WritableImage(width, height);
generateMap(diffuseMap, Color.RED);
generateMap(specularMap, Color.ANTIQUEWHITE);
final PhongMaterial sharedMaterial = new PhongMaterial();
final PhongMaterial sharedMapMaterial = new PhongMaterial();
sharedMapMaterial.setDiffuseMap(diffuseMap);
sharedMapMaterial.setSpecularMap(specularMap);
final Sphere sharedMatSphere = new Sphere();
sharedMatSphere.setScaleX(100);
sharedMatSphere.setScaleY(100);
sharedMatSphere.setScaleZ(100);
sharedMatSphere.setMaterial(sharedMaterial);
sharedMatSphere.setTranslateX(150);
sharedMatSphere.setTranslateY(400);
final Sphere sharedMatSphere1 = new Sphere();
sharedMatSphere1.setScaleX(100);
sharedMatSphere1.setScaleY(100);
sharedMatSphere1.setScaleZ(100);
sharedMatSphere1.setMaterial(sharedMaterial);
sharedMatSphere1.setTranslateX(400);
sharedMatSphere1.setTranslateY(400);
final Sphere sharedMapSphere = new Sphere(2);
sharedMapSphere.setScaleX(100);
sharedMapSphere.setScaleY(100);
sharedMapSphere.setScaleZ(100);
sharedMapSphere.setMaterial(sharedMapMaterial);
sharedMapSphere.setTranslateX(750);
sharedMapSphere.setTranslateY(400);
Group root1 = new Group(sharedMatSphere, sharedMatSphere1, sharedMapSphere);
diffuseMapPicker = new ColorPicker(Color.RED);
diffuseMapPicker.valueProperty().addListener((ov, t, t1) -> generateMap(diffuseMap, t1));
diffuseMapCheckBox = new CheckBox("Set Diffuse Map for left two spheres");
diffuseMapCheckBox.setSelected(false);
diffuseMapCheckBox.selectedProperty().addListener((ov, t, t1) -> sharedMaterial.setDiffuseMap(t1 ? diffuseMap : null));
specularMapPicker = new ColorPicker(Color.ANTIQUEWHITE);
specularMapPicker.valueProperty().addListener((ov, t, t1) -> generateMap(specularMap, t1));
specularMapCheckBox = new CheckBox("Set Specular Map for left two spheres");
specularMapCheckBox.setSelected(false);
specularMapCheckBox.selectedProperty().addListener((ov, t, t1) -> sharedMaterial.setSpecularMap(t1 ? specularMap : null));
selfIllumMapPicker = new ColorPicker();
selfIllumMapPicker.valueProperty().addListener((ov, t, t1) -> generateMap(selfIllumMap, t1));
selfIllumMapCheckBox = new CheckBox("Set SelfIllumination Map for left two spheres");
selfIllumMapCheckBox.setSelected(false);
selfIllumMapCheckBox.selectedProperty().addListener((ov, t, t1) -> {
sharedMapMaterial.setSelfIlluminationMap(selfIllumMap);
sharedMaterial.setSelfIlluminationMap(t1 ? selfIllumMap : null);
});
HBox labelBox = new HBox(20);
labelBox.getChildren().add(new Label("NOTE: Writable image will be generated based on selected color. In other words, Texture Map is used instead of Color."));
HBox diffuseBox = new HBox(20);
diffuseBox.getChildren().addAll(diffuseMapPicker, diffuseMapCheckBox);
HBox specularBox = new HBox(20);
specularBox.getChildren().addAll(specularMapPicker, specularMapCheckBox);
HBox selfIllumBox = new HBox(20);
selfIllumBox.getChildren().addAll(selfIllumMapPicker, selfIllumMapCheckBox);
VBox controls = new VBox(20);
controls.getChildren().addAll(labelBox, diffuseBox, specularBox, selfIllumBox);
Group root = new Group(root1, controls);
Scene scene = new Scene(root, 1000, 650, true);
scene.setCamera(new PerspectiveCamera());
stage.setScene(scene);
stage.show();
}
public static void main(String[] args) {
launch(args);
}
}
