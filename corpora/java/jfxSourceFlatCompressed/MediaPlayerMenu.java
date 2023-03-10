package fxmediaplayer.menu;
import fxmediaplayer.FXMediaPlayerInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
public class MediaPlayerMenu {
private FXMediaPlayerInterface FXMediaPlayer = null;
private ToolBar toolBar = null;
private Button buttonOpen = null;
private Button buttonDispose = null;
private ToggleButton buttonAutoPlay = null;
private Button buttonFullScreen = null;
private ToggleButton buttonSmooth = null;
private ToggleButton buttonPreserveRatio = null;
private ToggleButton buttonScrubbing = null;
private MediaPlayerMenuOpenDialog openDialog = null;
public MediaPlayerMenu(FXMediaPlayerInterface FXMediaPlayer) {
this.FXMediaPlayer = FXMediaPlayer;
}
public ToolBar getMenu() {
if (toolBar == null) {
toolBar = new ToolBar();
buttonOpen = new Button("Open");
buttonOpen.setOnAction((ActionEvent event) -> {
onButtonOpen();
});
toolBar.getItems().add(buttonOpen);
buttonDispose = new Button("Dispose");
buttonDispose.setOnAction((ActionEvent event) -> {
onButtonDispose();
});
toolBar.getItems().add(buttonDispose);
buttonAutoPlay = new ToggleButton("Auto Play");
buttonAutoPlay.setOnAction((ActionEvent event) -> {
onButtonAutoPlay();
});
toolBar.getItems().add(buttonAutoPlay);
buttonFullScreen = new Button("Full Screen");
buttonFullScreen.setOnAction((ActionEvent event) -> {
onButtonFullScreen();
});
toolBar.getItems().add(buttonFullScreen);
buttonSmooth = new ToggleButton("Smooth");
buttonSmooth.setOnAction((ActionEvent event) -> {
onButtonSmooth();
});
if (FXMediaPlayer.getMediaView().isSmooth()) {
buttonSmooth.setSelected(true);
}
toolBar.getItems().add(buttonSmooth);
buttonPreserveRatio = new ToggleButton("Ratio");
buttonPreserveRatio.setOnAction((ActionEvent event) -> {
onButtonPreserveRatio();
});
if (FXMediaPlayer.getMediaView().isPreserveRatio()) {
buttonPreserveRatio.setSelected(true);
}
toolBar.getItems().add(buttonPreserveRatio);
buttonScrubbing = new ToggleButton("Scrubbing");
buttonScrubbing.setOnAction((ActionEvent event) -> {
onButtonScrubbing();
});
toolBar.getItems().add(buttonScrubbing);
}
return toolBar;
}
private void onButtonOpen() {
if (openDialog == null) {
openDialog = new MediaPlayerMenuOpenDialog(FXMediaPlayer);
}
openDialog.open();
}
private void onButtonDispose() {
FXMediaPlayer.onSourceChanged(null);
}
private void onButtonAutoPlay() {
FXMediaPlayer.setAutoPlay(buttonAutoPlay.isSelected());
}
private void onButtonFullScreen() {
FXMediaPlayer.setFullScreen(true);
}
private void onButtonSmooth() {
FXMediaPlayer.getMediaView().setSmooth(buttonSmooth.isSelected());
}
private void onButtonPreserveRatio() {
FXMediaPlayer.getMediaView()
.setPreserveRatio(buttonPreserveRatio.isSelected());
}
private void onButtonScrubbing() {
FXMediaPlayer.setScrubbing(buttonScrubbing.isSelected());
}
}
