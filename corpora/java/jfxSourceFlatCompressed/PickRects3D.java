package picktest;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
public class PickRects3D extends Application {
@Override public void start(Stage stage) {
stage.setTitle("3D Picking Test");
stage.setWidth(WIDTH);
stage.setHeight(HEIGHT);
Scene scene = new Scene(new Group());
scene.setFill(Color.BLACK);
scene.setCamera(new PerspectiveCamera());
Group sceneContent = new Group();
sceneContent.setTranslateX(50);
sceneContent.setTranslateZ(400);
Group rotationGroup = new Group();
rotationGroup.setRotate(20);
rotationGroup.setRotationAxis(Rotate.Y_AXIS);
Group yGroup = new Group();
ObservableList<Group> yGroups = javafx.collections.FXCollections.<Group>observableArrayList();
for (int y = 0; y <= 4; y++) {
Group yGroupElem = new Group();
yGroupElem.setTranslateY(10 + y * 80);
ObservableList<Group> xGroups = javafx.collections.FXCollections.<Group>observableArrayList();
for (int x = 0; x <= 9; x++) {
float angle = 20 * x;
Group xRotateGroup = new Group();
Rotate rot = new Rotate(angle, 40, 15, 0, Rotate.Y_AXIS);
xRotateGroup.getTransforms().add(rot);
Group rectGroup = new Group();
rectGroup.setTranslateZ(300);
final int xx = x;
final int yy = y;
final Rectangle rectangle = new Rectangle(10, 10, 80, 30);
rectangle.setFill(Color.YELLOW);
rectangle.setStroke(Color.RED);
rectangle.setStrokeWidth(5);
rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
@Override
public void handle(MouseEvent e) {
System.out.println("col:" + xx + " row:" + yy + " Mouse Moved:" + e);
}
});
rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
@Override
public void handle(MouseEvent e) {
System.out.println("col:" + xx + " row:" + yy + " Mouse Pressed:" + e);
}
});
rectGroup.getChildren().addAll(rectangle);
xRotateGroup.getChildren().addAll(rectGroup);
xGroups.add(xRotateGroup);
}
yGroupElem.getChildren().addAll(xGroups);
yGroups.add(yGroupElem);
}
yGroup.getChildren().addAll(yGroups);
rotationGroup.getChildren().addAll(yGroup);
sceneContent.getChildren().addAll(rotationGroup);
((Group)scene.getRoot()).getChildren().addAll(sceneContent);
stage.setScene(scene);
stage.show();
}
public static void main(String[] args) {
Application.launch(args);
}
private static int WIDTH = 640;
private static int HEIGHT = 480;
}
