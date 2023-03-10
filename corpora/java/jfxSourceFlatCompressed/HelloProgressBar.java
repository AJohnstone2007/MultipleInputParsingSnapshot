package hello;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
public class HelloProgressBar extends Application {
public static void main(String[] args) {
Application.launch(args);
}
@Override public void start(Stage stage) {
stage.setTitle("Hello ProgressBar");
Scene scene = new Scene(new Group(), 600, 450);
scene.setFill(Color.CHOCOLATE);
Group root = (Group)scene.getRoot();
double y = 15;
final double SPACING = 25;
ProgressBar pbar = new ProgressBar();
pbar.setLayoutX(15);
pbar.setLayoutY(y);
pbar.setVisible(true);
root.getChildren().add(pbar);
y += SPACING;
pbar = new ProgressBar();
pbar.setPrefWidth(150);
pbar.setLayoutX(15);
pbar.setLayoutY(y);
pbar.setVisible(true);
root.getChildren().add(pbar);
y += SPACING;
pbar = new ProgressBar();
pbar.setPrefWidth(200);
pbar.setLayoutX(15);
pbar.setLayoutY(y);
pbar.setVisible(true);
root.getChildren().add(pbar);
y = 15;
pbar = new ProgressBar();
pbar.setLayoutX(230);
pbar.setLayoutY(y);
pbar.setProgress(0.25);
pbar.setVisible(true);
root.getChildren().add(pbar);
y += SPACING;
pbar = new ProgressBar();
pbar.setPrefWidth(150);
pbar.setLayoutX(230);
pbar.setLayoutY(y);
pbar.setProgress(0.50);
pbar.setVisible(true);
root.getChildren().add(pbar);
y += SPACING;
pbar = new ProgressBar();
pbar.setPrefWidth(200);
pbar.setLayoutX(230);
pbar.setLayoutY(y);
pbar.setProgress(0);
root.getChildren().add(pbar);
final Timeline timeline = new Timeline();
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.setAutoReverse(true);
final KeyValue kv = new KeyValue(pbar.progressProperty(), 1);
final KeyFrame kf1 = new KeyFrame(Duration.millis(3000), kv);
timeline.getKeyFrames().add(kf1);
timeline.play();
stage.setScene(scene);
stage.show();
}
}
