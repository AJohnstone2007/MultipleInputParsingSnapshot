package fxmediaplayer.control;
import fxmediaplayer.FXMediaPlayerInterface;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.media.MediaPlayer;
public class MediaPlayerToolBar {
private FXMediaPlayerInterface FXMediaPlayer = null;
private ToolBar toolBar = null;
private Button buttonPlay = null;
private Button buttonPause = null;
private Button buttonStop = null;
private ToggleButton buttonMute = null;
private ToggleButton buttonLoop = null;
private InvalidationListener statusPropertyListener = null;
public MediaPlayerToolBar(FXMediaPlayerInterface FXMediaPlayer) {
this.FXMediaPlayer = FXMediaPlayer;
}
public ToolBar getToolBar() {
if (toolBar == null) {
toolBar = new ToolBar();
buttonPlay = new Button("Play");
buttonPlay.setOnAction((ActionEvent event) -> {
onButtonPlay();
});
toolBar.getItems().add(buttonPlay);
buttonPause = new Button("Pause");
buttonPause.setOnAction((ActionEvent event) -> {
onButtonPause();
});
toolBar.getItems().add(buttonPause);
buttonStop = new Button("Stop");
buttonStop.setOnAction((ActionEvent event) -> {
onButtonStop();
});
toolBar.getItems().add(buttonStop);
buttonMute = new ToggleButton("Mute");
buttonMute.setOnAction((ActionEvent event) -> {
onButtonMute();
});
toolBar.getItems().add(buttonMute);
buttonLoop = new ToggleButton("Loop");
buttonLoop.setOnAction((ActionEvent event) -> {
onButtonLoop();
});
toolBar.getItems().add(buttonLoop);
toolBar.setDisable(true);
}
return toolBar;
}
public void onMediaPlayerChanged(MediaPlayer oldMediaPlayer) {
removeListeners(oldMediaPlayer);
addListeners();
onButtonMute();
onButtonLoop();
}
private void addListeners() {
if (FXMediaPlayer.getMediaPlayer() == null) {
return;
}
statusPropertyListener = (Observable o) -> {
onStatus(o);
};
FXMediaPlayer.getMediaPlayer()
.statusProperty().addListener(statusPropertyListener);
}
private void removeListeners(MediaPlayer mediaPlayer) {
if (mediaPlayer == null) {
return;
}
mediaPlayer.statusProperty()
.removeListener(statusPropertyListener);
}
@SuppressWarnings("unchecked")
private void onStatus(Observable o) {
try {
ReadOnlyObjectProperty<MediaPlayer.Status> prop =
(ReadOnlyObjectProperty<MediaPlayer.Status>) o;
MediaPlayer.Status status = prop.getValue();
if (status == MediaPlayer.Status.READY) {
toolBar.setDisable(false);
} else if (status == MediaPlayer.Status.DISPOSED ||
status == MediaPlayer.Status.HALTED) {
toolBar.setDisable(true);
}
} catch (Exception e) {
System.err.println(e.toString());
}
}
private void onButtonPlay() {
if (FXMediaPlayer.getMediaPlayer() != null) {
FXMediaPlayer.getMediaPlayer().play();
}
}
private void onButtonPause() {
if (FXMediaPlayer.getMediaPlayer() != null) {
FXMediaPlayer.getMediaPlayer().pause();
}
}
private void onButtonStop() {
if (FXMediaPlayer.getMediaPlayer() != null) {
FXMediaPlayer.getMediaPlayer().stop();
}
}
private void onButtonMute() {
if (FXMediaPlayer.getMediaPlayer() != null) {
FXMediaPlayer.getMediaPlayer().setMute(buttonMute.isSelected());
}
}
private void onButtonLoop() {
if (FXMediaPlayer.getMediaPlayer() != null) {
if (buttonLoop.isSelected()) {
FXMediaPlayer.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
} else {
FXMediaPlayer.getMediaPlayer().setCycleCount(1);
}
}
}
}
