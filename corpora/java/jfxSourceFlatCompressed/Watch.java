package ensemble.samples.graphics2d.stopwatch;
import java.text.DecimalFormat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
public class Watch extends Parent {
private final Dial mainDial;
private final Dial minutesDial;
private final Dial tenthsDial;
private final Group background = new Group();
private final DigitalClock digitalClock = new DigitalClock();
private final StopWatchButton startButton;
private final StopWatchButton stopButton;
private int elapsedMillis = 0;
private int lastClockTime = 0;
private DecimalFormat twoPlaces = new DecimalFormat("00");
private Timeline time = new Timeline();
public Watch() {
startButton = new StopWatchButton(Color.web("#8cc700"), Color.web("#71a000"));
stopButton = new StopWatchButton(Color.web("#AA0000"), Color.web("#660000"));
mainDial = new Dial(117, true, 12, 60, Color.RED, true);
minutesDial = new Dial(30, false, 12, 60, "minutes", Color.BLACK, false);
tenthsDial = new Dial(30, false, 12, 60, "10ths", Color.BLACK, false);
configureBackground();
myLayout();
configureListeners();
configureTimeline();
getChildren().addAll(background, minutesDial, tenthsDial, digitalClock, mainDial, startButton, stopButton);
}
private void configureTimeline() {
time.setCycleCount(Timeline.INDEFINITE);
KeyFrame keyFrame = new KeyFrame(Duration.millis(47), (ActionEvent event) -> {
calculate();
});
time.getKeyFrames().add(keyFrame);
}
private void configureBackground() {
ImageView imageView = new ImageView();
Image image = loadImage();
imageView.setImage(image);
Circle circle1 = new Circle();
circle1.setCenterX(140);
circle1.setCenterY(140);
circle1.setRadius(120);
circle1.setFill(Color.TRANSPARENT);
circle1.setStroke(Color.web("#0A0A0A"));
circle1.setStrokeWidth(0.3);
Circle circle2 = new Circle();
circle2.setCenterX(140);
circle2.setCenterY(140);
circle2.setRadius(118);
circle2.setFill(Color.TRANSPARENT);
circle2.setStroke(Color.web("#0A0A0A"));
circle2.setStrokeWidth(0.3);
Circle circle3 = new Circle();
circle3.setCenterX(140);
circle3.setCenterY(140);
circle3.setRadius(140);
circle3.setFill(Color.TRANSPARENT);
circle3.setStroke(Color.web("#818a89"));
circle3.setStrokeWidth(1);
Ellipse ellipse = new Ellipse(140, 95, 180, 95);
Circle ellipseClip = new Circle(140, 140, 140);
ellipse.setFill(Color.web("#535450"));
ellipse.setStrokeWidth(0);
GaussianBlur ellipseEffect = new GaussianBlur();
ellipseEffect.setRadius(10);
ellipse.setEffect(ellipseEffect);
ellipse.setOpacity(0.1);
ellipse.setClip(ellipseClip);
background.getChildren().addAll(imageView, circle1, circle2, circle3, ellipse);
}
private void myLayout() {
mainDial.setLayoutX(140);
mainDial.setLayoutY(140);
minutesDial.setLayoutX(100);
minutesDial.setLayoutY(100);
tenthsDial.setLayoutX(180);
tenthsDial.setLayoutY(100);
digitalClock.setLayoutX(79);
digitalClock.setLayoutY(195);
startButton.setLayoutX(223);
startButton.setLayoutY(1);
Rotate rotateRight = new Rotate(360 / 12);
startButton.getTransforms().add(rotateRight);
stopButton.setLayoutX(59.5);
stopButton.setLayoutY(0);
Rotate rotateLeft = new Rotate(-360 / 12);
stopButton.getTransforms().add(rotateLeft);
}
private void configureListeners() {
startButton.setOnMousePressed((MouseEvent me) -> {
startButton.moveDown();
me.consume();
});
stopButton.setOnMousePressed((MouseEvent me) -> {
stopButton.moveDown();
me.consume();
});
startButton.setOnMouseReleased((MouseEvent me) -> {
startButton.moveUp();
startStop();
me.consume();
});
stopButton.setOnMouseReleased((MouseEvent me) -> {
stopButton.moveUp();
stopReset();
me.consume();
});
startButton.setOnMouseDragged((MouseEvent me) -> {
me.consume();
});
stopButton.setOnMouseDragged((MouseEvent me) -> {
me.consume();
});
}
private void calculate() {
if (lastClockTime == 0) {
lastClockTime = (int) System.currentTimeMillis();
}
int now = (int) System.currentTimeMillis();
int delta = now - lastClockTime;
elapsedMillis += delta;
int tenths = (elapsedMillis / 10) % 100;
int seconds = (elapsedMillis / 1000) % 60;
int mins = (elapsedMillis / 60000) % 60;
refreshTimeDisplay(mins, seconds, tenths);
lastClockTime = now;
}
public void startStop() {
if (time.getStatus() != Animation.Status.STOPPED) {
time.stop();
lastClockTime = 0;
} else {
time.play();
}
}
public void stopReset() {
if (time.getStatus() != Animation.Status.STOPPED) {
time.stop();
lastClockTime = 0;
} else {
lastClockTime = 0;
elapsedMillis = 0;
refreshTimeDisplay(0, 0, 0);
}
}
private void refreshTimeDisplay(int mins, int seconds, int tenths) {
double handAngle = ((360 / 60) * seconds);
mainDial.setAngle(handAngle);
double tenthsHandAngle = ((360 / 100.0) * tenths);
tenthsDial.setAngle(tenthsHandAngle);
double minutesHandAngle = ((360 / 60.0) * mins);
minutesDial.setAngle(minutesHandAngle);
String timeString = twoPlaces.format(mins) + ":" +
twoPlaces.format(seconds) + "." +
twoPlaces.format(tenths);
digitalClock.refreshDigits(timeString);
}
public Image loadImage() {
String URL = "/ensemble/samples/shared-resources/stopwatch.png";
return new Image(getClass().getResourceAsStream(URL));
}
}
