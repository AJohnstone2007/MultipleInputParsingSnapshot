package ensemble.samples.media.streamingmediaplayer;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
public class PlayerPane extends BorderPane {
private MediaPlayer mp;
private MediaView mediaView;
private final boolean repeat = false;
private boolean stopRequested = false;
private boolean atEndOfMedia = false;
private Duration duration;
private Slider timeSlider;
private Label playTime;
private Slider volumeSlider;
private HBox mediaTopBar;
private HBox mediaBottomBar;
private ParallelTransition transition = null;
@Override
protected void layoutChildren() {
if (mediaView != null && getBottom() != null) {
mediaView.setFitWidth(getWidth());
mediaView.setFitHeight(getHeight() - getBottom().prefHeight(-1));
}
super.layoutChildren();
if (mediaView != null) {
mediaView.setTranslateX((((Pane)getCenter()).getWidth() -
mediaView.prefWidth(-1)) / 2);
mediaView.setTranslateY((((Pane)getCenter()).getHeight() -
mediaView.prefHeight(-1)) / 2);
}
}
@Override
protected double computeMinWidth(double height) {
return mediaBottomBar.prefWidth(-1);
}
@Override
protected double computeMinHeight(double width) {
return 200;
}
@Override
protected double computePrefWidth(double height) {
return Math.max(mp.getMedia().getWidth(),
mediaBottomBar.prefWidth(height));
}
@Override
protected double computePrefHeight(double width) {
return mp.getMedia().getHeight() + mediaBottomBar.prefHeight(width);
}
@Override
protected double computeMaxWidth(double height) {
return Double.MAX_VALUE;
}
@Override
protected double computeMaxHeight(double width) {
return Double.MAX_VALUE;
}
public PlayerPane(final MediaPlayer mp) {
this.mp = mp;
setId("player-pane");
mediaView = new MediaView(mp);
Pane mvPane = new Pane() {
};
mvPane.setId("media-pane");
mvPane.getChildren().add(mediaView);
setCenter(mvPane);
mediaTopBar = new HBox();
mediaTopBar.setPadding(new Insets(5, 10, 5, 10));
mediaTopBar.setAlignment(Pos.CENTER);
mediaTopBar.setOpacity(1);
BorderPane.setAlignment(mediaTopBar, Pos.CENTER);
ReadOnlyObjectProperty<Duration> time = mp.currentTimeProperty();
time.addListener((ObservableValue<? extends Duration> observable,
Duration oldValue, Duration newValue) -> {
updateValues();
});
mp.setOnPlaying(() -> {
if (stopRequested) {
mp.pause();
stopRequested = false;
}
});
mp.setOnReady(() -> {
duration = mp.getMedia().getDuration();
updateValues();
});
mp.setOnEndOfMedia(() -> {
if (!repeat) {
stopRequested = true;
atEndOfMedia = true;
}
});
mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
Label timeLabel = new Label("Time");
timeLabel.setMinWidth(Control.USE_PREF_SIZE);
timeLabel.setTextFill(Color.WHITE);
mediaTopBar.getChildren().add(timeLabel);
timeSlider = new Slider();
timeSlider.setId("media-slider");
timeSlider.setMinWidth(240);
timeSlider.setMaxWidth(Double.MAX_VALUE);
final DoubleProperty value = timeSlider.valueProperty();
value.addListener((ObservableValue<? extends Number> observable,
Number old, Number now) -> {
if (timeSlider.isValueChanging()) {
if (duration != null) {
mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
}
updateValues();
} else if (Math.abs(now.doubleValue() - old.doubleValue()) > 1.5) {
if (duration != null) {
mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
}
}
});
HBox.setHgrow(timeSlider, Priority.ALWAYS);
mediaTopBar.getChildren().add(timeSlider);
playTime = new Label();
playTime.setMinWidth(Control.USE_PREF_SIZE);
playTime.setTextFill(Color.WHITE);
mediaTopBar.getChildren().add(playTime);
Label volumeLabel = new Label("Vol");
volumeLabel.setMinWidth(Control.USE_PREF_SIZE);
volumeLabel.setTextFill(Color.WHITE);
mediaTopBar.getChildren().add(volumeLabel);
volumeSlider = new Slider();
volumeSlider.setId("media-slider");
volumeSlider.setPrefWidth(120);
volumeSlider.setMinWidth(30);
volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
volumeSlider.valueProperty().addListener((Observable ov) -> {
});
volumeSlider.valueProperty().addListener((ObservableValue<? extends Number> observable,
Number old, Number now) -> {
mp.setVolume(volumeSlider.getValue() / 100.0);
});
HBox.setHgrow(volumeSlider, Priority.ALWAYS);
mediaTopBar.getChildren().add(volumeSlider);
setTop(mediaTopBar);
final EventHandler<ActionEvent> backAction = (ActionEvent e) -> {
mp.seek(Duration.ZERO);
};
final EventHandler<ActionEvent> stopAction = (ActionEvent e) -> {
mp.stop();
};
final EventHandler<ActionEvent> playAction = (ActionEvent e) -> {
mp.play();
};
final EventHandler<ActionEvent> pauseAction = (ActionEvent e) -> {
mp.pause();
};
final EventHandler<ActionEvent> forwardAction = (ActionEvent e) -> {
Duration currentTime = mp.getCurrentTime();
mp.seek(Duration.seconds(currentTime.toSeconds() + 5.0));
};
Button backButton = new Button("Back");
backButton.setId("back-button");
backButton.setOnAction(backAction);
Button stopButton = new Button("Stop");
stopButton.setId("stop-button");
stopButton.setOnAction(stopAction);
Button playButton = new Button("Play");
playButton.setId("play-button");
playButton.setOnAction(playAction);
Button pauseButton = new Button("Pause");
pauseButton.setId("pause-button");
pauseButton.setOnAction(pauseAction);
Button forwardButton = new Button("Forward");
forwardButton.setId("forward-button");
forwardButton.setOnAction(forwardAction);
mediaBottomBar = new HBox();
mediaBottomBar.setId("bottom");
mediaBottomBar.setSpacing(0);
mediaBottomBar.setAlignment(Pos.CENTER);
mediaBottomBar.getChildren().addAll(backButton, stopButton, playButton,
pauseButton, forwardButton);
BorderPane.setAlignment(mediaBottomBar, Pos.CENTER);
setBottom(mediaBottomBar);
}
protected void updateValues() {
if (playTime != null && timeSlider != null &&
volumeSlider != null && duration != null) {
Platform.runLater(() -> {
Duration currentTime = mp.getCurrentTime();
playTime.setText(formatTime(currentTime, duration));
timeSlider.setDisable(duration.isUnknown());
if (!timeSlider.isDisabled() &&
duration.greaterThan(Duration.ZERO) &&
!timeSlider.isValueChanging()) {
double relativeTime =
currentTime.divide(duration).toMillis() * 100.0;
timeSlider.setValue(relativeTime);
}
if (!volumeSlider.isValueChanging()) {
int relativeVolume = (int)Math.round(mp.getVolume() * 100);
volumeSlider.setValue(relativeVolume);
}
});
}
}
private static String formatTime(Duration elapsed, Duration duration) {
int intElapsed = (int) Math.floor(elapsed.toSeconds());
int elapsedHours = intElapsed / (60 * 60);
if (elapsedHours > 0) {
intElapsed -= elapsedHours * 60 * 60;
}
int elapsedMinutes = intElapsed / 60;
int elapsedSeconds = intElapsed -
elapsedHours * 60 * 60 - elapsedMinutes * 60;
if (duration.greaterThan(Duration.ZERO)) {
int intDuration = (int) Math.floor(duration.toSeconds());
int durationHours = intDuration / (60 * 60);
if (durationHours > 0) {
intDuration -= durationHours * 60 * 60;
}
int durationMinutes = intDuration / 60;
int durationSeconds = intDuration -
durationHours * 60 * 60 - durationMinutes * 60;
if (durationHours > 0) {
return String.format("%d:%02d:%02d",
elapsedHours, elapsedMinutes, elapsedSeconds);
} else {
return String.format("%02d:%02d",
elapsedMinutes, elapsedSeconds);
}
} else {
if (elapsedHours > 0) {
return String.format("%d:%02d:%02d",
elapsedHours, elapsedMinutes, elapsedSeconds);
} else {
return String.format("%02d:%02d",
elapsedMinutes, elapsedSeconds);
}
}
}
}
