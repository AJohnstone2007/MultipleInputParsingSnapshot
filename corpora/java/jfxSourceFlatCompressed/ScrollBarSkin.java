package javafx.scene.control.skin;
import com.sun.javafx.scene.control.Properties;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import com.sun.javafx.util.Utils;
import com.sun.javafx.scene.control.behavior.ScrollBarBehavior;
import java.util.function.Consumer;
public class ScrollBarSkin extends SkinBase<ScrollBar> {
private final ScrollBarBehavior behavior;
private StackPane thumb;
private StackPane trackBackground;
private StackPane track;
private EndButton incButton;
private EndButton decButton;
private double trackLength;
private double thumbLength;
private double preDragThumbPos;
private Point2D dragStart;
private double trackPos;
public ScrollBarSkin(ScrollBar control) {
super(control);
this.behavior = new ScrollBarBehavior(control);
initialize();
getSkinnable().requestLayout();
final Consumer<ObservableValue<?>> consumer = e -> {
positionThumb();
getSkinnable().requestLayout();
};
registerChangeListener(control.minProperty(), consumer);
registerChangeListener(control.maxProperty(), consumer);
registerChangeListener(control.visibleAmountProperty(), consumer);
registerChangeListener(control.valueProperty(), e -> positionThumb());
registerChangeListener(control.orientationProperty(), e -> getSkinnable().requestLayout());
}
@Override public void dispose() {
super.dispose();
if (behavior != null) {
behavior.dispose();
}
}
@Override protected void layoutChildren(final double x, final double y,
final double w, final double h) {
final ScrollBar s = getSkinnable();
double visiblePortion;
if (s.getMax() > s.getMin()) {
visiblePortion = s.getVisibleAmount()/(s.getMax() - s.getMin());
}
else {
visiblePortion = 1.0;
}
if (s.getOrientation() == Orientation.VERTICAL) {
if (!Properties.IS_TOUCH_SUPPORTED) {
double decHeight = snapSizeY(decButton.prefHeight(-1));
double incHeight = snapSizeY(incButton.prefHeight(-1));
decButton.resize(w, decHeight);
incButton.resize(w, incHeight);
trackLength = snapSizeY(h - (decHeight + incHeight));
thumbLength = snapSizeY(Utils.clamp(minThumbLength(), (trackLength * visiblePortion), trackLength));
trackBackground.resizeRelocate(snapPositionX(x), snapPositionY(y), w, trackLength+decHeight+incHeight);
decButton.relocate(snapPositionX(x), snapPositionY(y));
incButton.relocate(snapPositionX(x), snapPositionY(y + h - incHeight));
track.resizeRelocate(snapPositionX(x), snapPositionY(y + decHeight), w, trackLength);
thumb.resize(snapSizeX(x >= 0 ? w : w + x), thumbLength);
positionThumb();
}
else {
trackLength = snapSizeY(h);
thumbLength = snapSizeY(Utils.clamp(minThumbLength(), (trackLength * visiblePortion), trackLength));
track.resizeRelocate(snapPositionX(x), snapPositionY(y), w, trackLength);
thumb.resize(snapSizeX(x >= 0 ? w : w + x), thumbLength);
positionThumb();
}
} else {
if (!Properties.IS_TOUCH_SUPPORTED) {
double decWidth = snapSizeX(decButton.prefWidth(-1));
double incWidth = snapSizeX(incButton.prefWidth(-1));
decButton.resize(decWidth, h);
incButton.resize(incWidth, h);
trackLength = snapSizeX(w - (decWidth + incWidth));
thumbLength = snapSizeX(Utils.clamp(minThumbLength(), (trackLength * visiblePortion), trackLength));
trackBackground.resizeRelocate(snapPositionX(x), snapPositionY(y), trackLength+decWidth+incWidth, h);
decButton.relocate(snapPositionX(x), snapPositionY(y));
incButton.relocate(snapPositionX(x + w - incWidth), snapPositionY(y));
track.resizeRelocate(snapPositionX(x + decWidth), snapPositionY(y), trackLength, h);
thumb.resize(thumbLength, snapSizeY(y >= 0 ? h : h + y));
positionThumb();
}
else {
trackLength = snapSizeX(w);
thumbLength = snapSizeX(Utils.clamp(minThumbLength(), (trackLength * visiblePortion), trackLength));
track.resizeRelocate(snapPositionX(x), snapPositionY(y), trackLength, h);
thumb.resize(thumbLength, snapSizeY(y >= 0 ? h : h + y));
positionThumb();
}
s.resize(snapSizeX(s.getWidth()), snapSizeY(s.getHeight()));
}
if (s.getOrientation() == Orientation.VERTICAL && h >= (computeMinHeight(-1, (int)y , snappedRightInset(), snappedBottomInset(), (int)x) - (y+snappedBottomInset())) ||
s.getOrientation() == Orientation.HORIZONTAL && w >= (computeMinWidth(-1, (int)y , snappedRightInset(), snappedBottomInset(), (int)x) - (x+snappedRightInset()))) {
trackBackground.setVisible(true);
track.setVisible(true);
thumb.setVisible(true);
if (!Properties.IS_TOUCH_SUPPORTED) {
incButton.setVisible(true);
decButton.setVisible(true);
}
}
else {
trackBackground.setVisible(false);
track.setVisible(false);
thumb.setVisible(false);
if (!Properties.IS_TOUCH_SUPPORTED) {
if (h >= decButton.computeMinWidth(-1)) {
decButton.setVisible(true);
}
else {
decButton.setVisible(false);
}
if (h >= incButton.computeMinWidth(-1)) {
incButton.setVisible(true);
}
else {
incButton.setVisible(false);
}
}
}
}
@Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
return getBreadth();
} else {
if (!Properties.IS_TOUCH_SUPPORTED) {
return decButton.minWidth(-1) + incButton.minWidth(-1) + minTrackLength()+leftInset+rightInset;
} else {
return minTrackLength()+leftInset+rightInset;
}
}
}
@Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
if (!Properties.IS_TOUCH_SUPPORTED) {
return decButton.minHeight(-1) + incButton.minHeight(-1) + minTrackLength()+topInset+bottomInset;
} else {
return minTrackLength()+topInset+bottomInset;
}
} else {
return getBreadth();
}
}
@Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
final ScrollBar s = getSkinnable();
return s.getOrientation() == Orientation.VERTICAL ? getBreadth() : Properties.DEFAULT_LENGTH+leftInset+rightInset;
}
@Override protected double computePrefHeight(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
final ScrollBar s = getSkinnable();
return s.getOrientation() == Orientation.VERTICAL ? Properties.DEFAULT_LENGTH+topInset+bottomInset : getBreadth();
}
@Override protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
final ScrollBar s = getSkinnable();
return s.getOrientation() == Orientation.VERTICAL ? s.prefWidth(-1) : Double.MAX_VALUE;
}
@Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
final ScrollBar s = getSkinnable();
return s.getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : s.prefHeight(-1);
}
private void initialize() {
track = new StackPane();
track.getStyleClass().setAll("track");
trackBackground = new StackPane();
trackBackground.getStyleClass().setAll("track-background");
thumb = new StackPane() {
@Override
public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
switch (attribute) {
case VALUE: return getSkinnable().getValue();
default: return super.queryAccessibleAttribute(attribute, parameters);
}
}
};
thumb.getStyleClass().setAll("thumb");
thumb.setAccessibleRole(AccessibleRole.THUMB);
if (!Properties.IS_TOUCH_SUPPORTED) {
incButton = new EndButton("increment-button", "increment-arrow") {
@Override
public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
switch (action) {
case FIRE:
getSkinnable().increment();
break;
default: super.executeAccessibleAction(action, parameters);
}
}
};
incButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
incButton.setOnMousePressed(me -> {
if (!thumb.isVisible() || trackLength > thumbLength) {
behavior.incButtonPressed();
}
me.consume();
});
incButton.setOnMouseReleased(me -> {
if (!thumb.isVisible() || trackLength > thumbLength) {
behavior.incButtonReleased();
}
me.consume();
});
decButton = new EndButton("decrement-button", "decrement-arrow") {
@Override
public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
switch (action) {
case FIRE:
getSkinnable().decrement();
break;
default: super.executeAccessibleAction(action, parameters);
}
}
};
decButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
decButton.setOnMousePressed(me -> {
if (!thumb.isVisible() || trackLength > thumbLength) {
behavior.decButtonPressed();
}
me.consume();
});
decButton.setOnMouseReleased(me -> {
if (!thumb.isVisible() || trackLength > thumbLength) {
behavior.decButtonReleased();
}
me.consume();
});
}
track.setOnMousePressed(me -> {
if (!thumb.isPressed() && me.getButton() == MouseButton.PRIMARY) {
if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
if (trackLength != 0) {
behavior.trackPress(me.getY() / trackLength);
me.consume();
}
} else {
if (trackLength != 0) {
behavior.trackPress(me.getX() / trackLength);
me.consume();
}
}
}
});
track.setOnMouseReleased(me -> {
behavior.trackRelease();
me.consume();
});
thumb.setOnMousePressed(me -> {
if (me.isSynthesized()) {
me.consume();
return;
}
if (getSkinnable().getMax() > getSkinnable().getMin()) {
dragStart = thumb.localToParent(me.getX(), me.getY());
double clampedValue = Utils.clamp(getSkinnable().getMin(), getSkinnable().getValue(), getSkinnable().getMax());
preDragThumbPos = (clampedValue - getSkinnable().getMin()) / (getSkinnable().getMax() - getSkinnable().getMin());
me.consume();
}
});
thumb.setOnMouseDragged(me -> {
if (me.isSynthesized()) {
me.consume();
return;
}
if (getSkinnable().getMax() > getSkinnable().getMin()) {
if (trackLength > thumbLength) {
Point2D cur = thumb.localToParent(me.getX(), me.getY());
if (dragStart == null) {
dragStart = thumb.localToParent(me.getX(), me.getY());
}
double dragPos = getSkinnable().getOrientation() == Orientation.VERTICAL ? cur.getY() - dragStart.getY(): cur.getX() - dragStart.getX();
behavior.thumbDragged(preDragThumbPos + dragPos / (trackLength - thumbLength));
}
me.consume();
}
});
thumb.setOnScrollStarted(se -> {
if (se.isDirect()) {
if (getSkinnable().getMax() > getSkinnable().getMin()) {
dragStart = thumb.localToParent(se.getX(), se.getY());
double clampedValue = Utils.clamp(getSkinnable().getMin(), getSkinnable().getValue(), getSkinnable().getMax());
preDragThumbPos = (clampedValue - getSkinnable().getMin()) / (getSkinnable().getMax() - getSkinnable().getMin());
se.consume();
}
}
});
thumb.setOnScroll(event -> {
if (event.isDirect()) {
if (getSkinnable().getMax() > getSkinnable().getMin()) {
if (trackLength > thumbLength) {
Point2D cur = thumb.localToParent(event.getX(), event.getY());
if (dragStart == null) {
dragStart = thumb.localToParent(event.getX(), event.getY());
}
double dragPos = getSkinnable().getOrientation() == Orientation.VERTICAL ? cur.getY() - dragStart.getY(): cur.getX() - dragStart.getX();
behavior.thumbDragged( preDragThumbPos + dragPos / (trackLength - thumbLength));
}
event.consume();
return;
}
}
});
getSkinnable().addEventHandler(ScrollEvent.SCROLL, event -> {
if (trackLength > thumbLength) {
double dx = event.getDeltaX();
double dy = event.getDeltaY();
dx = (Math.abs(dx) < Math.abs(dy) ? dy : dx);
ScrollBar sb = (ScrollBar) getSkinnable();
double delta = (getSkinnable().getOrientation() == Orientation.VERTICAL ? dy : dx);
if (event.isDirect()) {
if (trackLength > thumbLength) {
behavior.thumbDragged((getSkinnable().getOrientation() == Orientation.VERTICAL ? event.getY() : event.getX()) / trackLength);
event.consume();
}
}
else {
if (delta > 0.0 && sb.getValue() > sb.getMin()) {
sb.decrement();
event.consume();
} else if (delta < 0.0 && sb.getValue() < sb.getMax()) {
sb.increment();
event.consume();
}
}
}
});
getChildren().clear();
if (!Properties.IS_TOUCH_SUPPORTED) {
getChildren().addAll(trackBackground, incButton, decButton, track, thumb);
}
else {
getChildren().addAll(track, thumb);
}
}
double getBreadth() {
if (!Properties.IS_TOUCH_SUPPORTED) {
if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
return Math.max(decButton.prefWidth(-1), incButton.prefWidth(-1)) +snappedLeftInset()+snappedRightInset();
} else {
return Math.max(decButton.prefHeight(-1), incButton.prefHeight(-1)) +snappedTopInset()+snappedBottomInset();
}
}
else {
if (getSkinnable().getOrientation() == Orientation.VERTICAL) {
return Math.max(Properties.DEFAULT_EMBEDDED_SB_BREADTH, Properties.DEFAULT_EMBEDDED_SB_BREADTH)+snappedLeftInset()+snappedRightInset();
} else {
return Math.max(Properties.DEFAULT_EMBEDDED_SB_BREADTH, Properties.DEFAULT_EMBEDDED_SB_BREADTH)+snappedTopInset()+snappedBottomInset();
}
}
}
double minThumbLength() {
return 1.5f * getBreadth();
}
double minTrackLength() {
return 2.0f * getBreadth();
}
void positionThumb() {
ScrollBar s = getSkinnable();
double clampedValue = Utils.clamp(s.getMin(), s.getValue(), s.getMax());
trackPos = (s.getMax() - s.getMin() > 0) ? ((trackLength - thumbLength) * (clampedValue - s.getMin()) / (s.getMax() - s.getMin())) : (0.0F);
if (!Properties.IS_TOUCH_SUPPORTED) {
if (s.getOrientation() == Orientation.VERTICAL) {
trackPos += decButton.prefHeight(-1);
} else {
trackPos += decButton.prefWidth(-1);
}
}
thumb.setTranslateX( snapPositionX(s.getOrientation() == Orientation.VERTICAL ? snappedLeftInset() : trackPos + snappedLeftInset()));
thumb.setTranslateY( snapPositionY(s.getOrientation() == Orientation.VERTICAL ? trackPos + snappedTopInset() : snappedTopInset()));
}
private Node getThumb() {
return thumb;
}
private Node getTrack() {
return track;
}
private Node getIncrementButton() {
return incButton;
}
private Node getDecrementButton() {
return decButton;
}
private static class EndButton extends Region {
private Region arrow;
private EndButton(String styleClass, String arrowStyleClass) {
getStyleClass().setAll(styleClass);
arrow = new Region();
arrow.getStyleClass().setAll(arrowStyleClass);
getChildren().setAll(arrow);
requestLayout();
}
@Override protected void layoutChildren() {
final double top = snappedTopInset();
final double left = snappedLeftInset();
final double bottom = snappedBottomInset();
final double right = snappedRightInset();
final double aw = snapSizeX(arrow.prefWidth(-1));
final double ah = snapSizeY(arrow.prefHeight(-1));
final double yPos = snapPositionY((getHeight() - (top + bottom + ah)) / 2.0);
final double xPos = snapPositionX((getWidth() - (left + right + aw)) / 2.0);
arrow.resizeRelocate(xPos + left, yPos + top, aw, ah);
}
@Override protected double computeMinHeight(double width) {
return prefHeight(-1);
}
@Override protected double computeMinWidth(double height) {
return prefWidth(-1);
}
@Override protected double computePrefWidth(double height) {
final double left = snappedLeftInset();
final double right = snappedRightInset();
final double aw = snapSizeX(arrow.prefWidth(-1));
return left + aw + right;
}
@Override protected double computePrefHeight(double width) {
final double top = snappedTopInset();
final double bottom = snappedBottomInset();
final double ah = snapSizeY(arrow.prefHeight(-1));
return top + ah + bottom;
}
}
}
