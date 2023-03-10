package javafx.scene.control.skin;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableValue;
import javafx.collections.ListChangeListener;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import static com.sun.javafx.scene.control.skin.resources.ControlResources.getString;
public class PaginationSkin extends SkinBase<Pagination> {
private static final Duration DURATION = new Duration(125.0);
private static final double SWIPE_THRESHOLD = 0.30;
private static final double TOUCH_THRESHOLD = 15;
private static final Interpolator interpolator = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
private Pagination pagination;
private StackPane currentStackPane;
private StackPane nextStackPane;
private Timeline timeline;
private Rectangle clipRect;
private NavigationControl navigation;
private int fromIndex;
private int previousIndex;
private int currentIndex;
private int toIndex;
private int pageCount;
private int maxPageIndicatorCount;
private double startTouchPos;
private double lastTouchPos;
private long startTouchTime;
private long lastTouchTime;
private double touchVelocity;
private boolean touchThresholdBroken;
private int touchEventId = -1;
private boolean nextPageReached = false;
private boolean setInitialDirection = false;
private int direction;
private int currentAnimatedIndex;
private boolean hasPendingAnimation = false;
private boolean animate = true;
private final PaginationBehavior behavior;
private EventHandler<ActionEvent> swipeAnimationEndEventHandler = new EventHandler<ActionEvent>() {
@Override public void handle(ActionEvent t) {
swapPanes();
timeline = null;
if (hasPendingAnimation) {
animateSwitchPage();
hasPendingAnimation = false;
}
}
};
private EventHandler<ActionEvent> clampAnimationEndEventHandler = new EventHandler<ActionEvent>() {
@Override public void handle(ActionEvent t) {
currentStackPane.setTranslateX(0);
nextStackPane.setTranslateX(0);
nextStackPane.setVisible(false);
timeline = null;
}
};
public PaginationSkin(final Pagination control) {
super(control);
behavior = new PaginationBehavior(control);
clipRect = new Rectangle();
getSkinnable().setClip(clipRect);
this.pagination = control;
this.currentStackPane = new StackPane();
currentStackPane.getStyleClass().add("page");
this.nextStackPane = new StackPane();
nextStackPane.getStyleClass().add("page");
nextStackPane.setVisible(false);
resetIndexes(true);
this.navigation = new NavigationControl();
getChildren().addAll(currentStackPane, nextStackPane, navigation);
control.maxPageIndicatorCountProperty().addListener(o -> {
resetIndiciesAndNav();
});
registerChangeListener(control.widthProperty(), e -> clipRect.setWidth(getSkinnable().getWidth()));
registerChangeListener(control.heightProperty(), e -> clipRect.setHeight(getSkinnable().getHeight()));
registerChangeListener(control.pageCountProperty(), e -> resetIndiciesAndNav());
registerChangeListener(control.pageFactoryProperty(), e -> {
if (animate && timeline != null) {
timeline.setRate(8);
timeline.setOnFinished(arg0 -> {
resetIndiciesAndNav();
});
return;
}
resetIndiciesAndNav();
});
initializeSwipeAndTouchHandlers();
}
private final DoubleProperty arrowButtonGap = new StyleableDoubleProperty(60.0) {
@Override public Object getBean() {
return PaginationSkin.this;
}
@Override public String getName() {
return "arrowButtonGap";
}
@Override public CssMetaData<Pagination,Number> getCssMetaData() {
return StyleableProperties.ARROW_BUTTON_GAP;
}
};
private final DoubleProperty arrowButtonGapProperty() {
return arrowButtonGap;
}
private final double getArrowButtonGap() {
return arrowButtonGap.get();
}
private final void setArrowButtonGap(double value) {
arrowButtonGap.set(value);
}
private BooleanProperty arrowsVisible;
private final void setArrowsVisible(boolean value) { arrowsVisibleProperty().set(value); }
private final boolean isArrowsVisible() { return arrowsVisible == null ? DEFAULT_ARROW_VISIBLE : arrowsVisible.get(); }
private final BooleanProperty arrowsVisibleProperty() {
if (arrowsVisible == null) {
arrowsVisible = new StyleableBooleanProperty(DEFAULT_ARROW_VISIBLE) {
@Override
protected void invalidated() {
getSkinnable().requestLayout();
}
@Override
public CssMetaData<Pagination,Boolean> getCssMetaData() {
return StyleableProperties.ARROWS_VISIBLE;
}
@Override
public Object getBean() {
return PaginationSkin.this;
}
@Override
public String getName() {
return "arrowVisible";
}
};
}
return arrowsVisible;
}
private BooleanProperty pageInformationVisible;
private final void setPageInformationVisible(boolean value) { pageInformationVisibleProperty().set(value); }
private final boolean isPageInformationVisible() { return pageInformationVisible == null ? DEFAULT_PAGE_INFORMATION_VISIBLE : pageInformationVisible.get(); }
private final BooleanProperty pageInformationVisibleProperty() {
if (pageInformationVisible == null) {
pageInformationVisible = new StyleableBooleanProperty(DEFAULT_PAGE_INFORMATION_VISIBLE) {
@Override
protected void invalidated() {
getSkinnable().requestLayout();
}
@Override
public CssMetaData<Pagination,Boolean> getCssMetaData() {
return StyleableProperties.PAGE_INFORMATION_VISIBLE;
}
@Override
public Object getBean() {
return PaginationSkin.this;
}
@Override
public String getName() {
return "pageInformationVisible";
}
};
}
return pageInformationVisible;
}
private ObjectProperty<Side> pageInformationAlignment;
private final void setPageInformationAlignment(Side value) { pageInformationAlignmentProperty().set(value); }
private final Side getPageInformationAlignment() { return pageInformationAlignment == null ? DEFAULT_PAGE_INFORMATION_ALIGNMENT : pageInformationAlignment.get(); }
private final ObjectProperty<Side> pageInformationAlignmentProperty() {
if (pageInformationAlignment == null) {
pageInformationAlignment = new StyleableObjectProperty<Side>(Side.BOTTOM) {
@Override
protected void invalidated() {
getSkinnable().requestLayout();
}
@Override
public CssMetaData<Pagination,Side> getCssMetaData() {
return StyleableProperties.PAGE_INFORMATION_ALIGNMENT;
}
@Override
public Object getBean() {
return PaginationSkin.this;
}
@Override
public String getName() {
return "pageInformationAlignment";
}
};
}
return pageInformationAlignment;
}
private BooleanProperty tooltipVisible;
private final void setTooltipVisible(boolean value) { tooltipVisibleProperty().set(value); }
private final boolean isTooltipVisible() { return tooltipVisible == null ? DEFAULT_TOOLTIP_VISIBLE : tooltipVisible.get(); }
private final BooleanProperty tooltipVisibleProperty() {
if (tooltipVisible == null) {
tooltipVisible = new StyleableBooleanProperty(DEFAULT_TOOLTIP_VISIBLE) {
@Override
protected void invalidated() {
getSkinnable().requestLayout();
}
@Override
public CssMetaData<Pagination,Boolean> getCssMetaData() {
return StyleableProperties.TOOLTIP_VISIBLE;
}
@Override
public Object getBean() {
return PaginationSkin.this;
}
@Override
public String getName() {
return "tooltipVisible";
}
};
}
return tooltipVisible;
}
@Override public void dispose() {
super.dispose();
if (behavior != null) {
behavior.dispose();
}
}
@Override protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
double navigationWidth = navigation.isVisible() ? snapSizeX(navigation.minWidth(height)) : 0;
return leftInset + Math.max(currentStackPane.minWidth(height), navigationWidth) + rightInset;
}
@Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
double navigationHeight = navigation.isVisible() ? snapSizeY(navigation.minHeight(width)) : 0;
return topInset + currentStackPane.minHeight(width) + navigationHeight + bottomInset;
}
@Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
double navigationWidth = navigation.isVisible() ? snapSizeX(navigation.prefWidth(height)) : 0;
return leftInset + Math.max(currentStackPane.prefWidth(height), navigationWidth) + rightInset;
}
@Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
double navigationHeight = navigation.isVisible() ? snapSizeY(navigation.prefHeight(width)) : 0;
return topInset + currentStackPane.prefHeight(width) + navigationHeight + bottomInset;
}
@Override protected void layoutChildren(final double x, final double y,
final double w, final double h) {
double navigationHeight = navigation.isVisible() ? snapSizeY(navigation.prefHeight(-1)) : 0;
double stackPaneHeight = snapSizeY(h - navigationHeight);
layoutInArea(currentStackPane, x, y, w, stackPaneHeight, 0, HPos.CENTER, VPos.CENTER);
layoutInArea(nextStackPane, x, y, w, stackPaneHeight, 0, HPos.CENTER, VPos.CENTER);
layoutInArea(navigation, x, stackPaneHeight, w, navigationHeight, 0, HPos.CENTER, VPos.CENTER);
}
@Override protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
switch (attribute) {
case FOCUS_ITEM: return navigation.indicatorButtons.getSelectedToggle();
case ITEM_COUNT: return navigation.indicatorButtons.getToggles().size();
case ITEM_AT_INDEX: {
Integer index = (Integer)parameters[0];
if (index == null) return null;
return navigation.indicatorButtons.getToggles().get(index);
}
default: return super.queryAccessibleAttribute(attribute, parameters);
}
}
private void selectNext() {
if (getCurrentPageIndex() < getPageCount() - 1) {
pagination.setCurrentPageIndex(getCurrentPageIndex() + 1);
}
}
private void selectPrevious() {
if (getCurrentPageIndex() > 0) {
pagination.setCurrentPageIndex(getCurrentPageIndex() - 1);
}
}
private void resetIndiciesAndNav() {
resetIndexes(false);
navigation.initializePageIndicators();
navigation.updatePageIndicators();
}
private void initializeSwipeAndTouchHandlers() {
final Pagination control = getSkinnable();
getSkinnable().addEventHandler(TouchEvent.TOUCH_PRESSED, e -> {
if (touchEventId == -1) {
touchEventId = e.getTouchPoint().getId();
}
if (touchEventId != e.getTouchPoint().getId()) {
return;
}
lastTouchPos = startTouchPos = e.getTouchPoint().getX();
lastTouchTime = startTouchTime = System.currentTimeMillis();
touchThresholdBroken = false;
e.consume();
});
getSkinnable().addEventHandler(TouchEvent.TOUCH_MOVED, e -> {
if (touchEventId != e.getTouchPoint().getId()) {
return;
}
double drag = e.getTouchPoint().getX() - lastTouchPos;
long time = System.currentTimeMillis() - lastTouchTime;
touchVelocity = drag/time;
lastTouchPos = e.getTouchPoint().getX();
lastTouchTime = System.currentTimeMillis();
double delta = e.getTouchPoint().getX() - startTouchPos;
if (!touchThresholdBroken && Math.abs(delta) > TOUCH_THRESHOLD) {
touchThresholdBroken = true;
}
if (touchThresholdBroken) {
double width = control.getWidth() - (snappedLeftInset() + snappedRightInset());
double currentPaneX;
double nextPaneX;
if (!setInitialDirection) {
setInitialDirection = true;
direction = delta < 0 ? 1 : -1;
}
if (delta < 0) {
if (direction == -1) {
nextStackPane.getChildren().clear();
direction = 1;
}
if (Math.abs(delta) <= width) {
currentPaneX = delta;
nextPaneX = width + delta;
nextPageReached = false;
} else {
currentPaneX = -width;
nextPaneX = 0;
nextPageReached = true;
}
currentStackPane.setTranslateX(currentPaneX);
if (getCurrentPageIndex() < getPageCount() - 1) {
createPage(nextStackPane, currentIndex + 1);
nextStackPane.setVisible(true);
nextStackPane.setTranslateX(nextPaneX);
} else {
currentStackPane.setTranslateX(0);
}
} else {
if (direction == 1) {
nextStackPane.getChildren().clear();
direction = -1;
}
if (Math.abs(delta) <= width) {
currentPaneX = delta;
nextPaneX = -width + delta;
nextPageReached = false;
} else {
currentPaneX = width;
nextPaneX = 0;
nextPageReached = true;
}
currentStackPane.setTranslateX(currentPaneX);
if (getCurrentPageIndex() != 0) {
createPage(nextStackPane, currentIndex - 1);
nextStackPane.setVisible(true);
nextStackPane.setTranslateX(nextPaneX);
} else {
currentStackPane.setTranslateX(0);
}
}
}
e.consume();
});
getSkinnable().addEventHandler(TouchEvent.TOUCH_RELEASED, e -> {
if (touchEventId != e.getTouchPoint().getId()) {
return;
} else {
touchEventId = -1;
setInitialDirection = false;
}
if (touchThresholdBroken) {
final double drag = e.getTouchPoint().getX() - startTouchPos;
final long time = System.currentTimeMillis() - startTouchTime;
final boolean quick = time < 300;
final double velocity = quick ? (double)drag / time : touchVelocity;
final double distance = (velocity * 500);
final double width = control.getWidth() - (snappedLeftInset() + snappedRightInset());
final double threshold = Math.abs(distance/width);
final double delta = Math.abs(drag/width);
if (threshold > SWIPE_THRESHOLD || delta > SWIPE_THRESHOLD) {
if (startTouchPos > e.getTouchPoint().getX()) {
selectNext();
} else {
selectPrevious();
}
} else {
animateClamping(startTouchPos > e.getTouchPoint().getSceneX());
}
}
e.consume();
});
}
private void resetIndexes(boolean usePageIndex) {
maxPageIndicatorCount = getMaxPageIndicatorCount();
pageCount = getPageCount();
if (pageCount > maxPageIndicatorCount) {
pageCount = maxPageIndicatorCount;
}
fromIndex = 0;
previousIndex = 0;
currentIndex = usePageIndex ? getCurrentPageIndex() : 0;
toIndex = pageCount - 1;
if (pageCount == Pagination.INDETERMINATE && maxPageIndicatorCount == Pagination.INDETERMINATE) {
toIndex = 0;
}
boolean isAnimate = animate;
if (isAnimate) {
animate = false;
}
currentStackPane.getChildren().clear();
nextStackPane.getChildren().clear();
pagination.setCurrentPageIndex(currentIndex);
createPage(currentStackPane, currentIndex);
if (isAnimate) {
animate = true;
}
}
private boolean createPage(StackPane pane, int index) {
if (pagination.getPageFactory() != null && pane.getChildren().isEmpty()) {
Node content = pagination.getPageFactory().call(index);
if (content != null) {
pane.getChildren().setAll(content);
return true;
} else {
boolean isAnimate = animate;
if (isAnimate) {
animate = false;
}
if (pagination.getPageFactory().call(previousIndex) != null) {
pagination.setCurrentPageIndex(previousIndex);
} else {
pagination.setCurrentPageIndex(0);
}
if (isAnimate) {
animate = true;
}
return false;
}
}
return false;
}
private int getPageCount() {
if (getSkinnable().getPageCount() < 1) {
return 1;
}
return getSkinnable().getPageCount();
}
private int getMaxPageIndicatorCount() {
return getSkinnable().getMaxPageIndicatorCount();
}
private int getCurrentPageIndex() {
return getSkinnable().getCurrentPageIndex();
}
private void animateSwitchPage() {
if (timeline != null) {
timeline.setRate(8);
hasPendingAnimation = true;
return;
}
if (!nextStackPane.isVisible()) {
if (!createPage(nextStackPane, currentAnimatedIndex)) {
return;
}
}
if (nextPageReached) {
swapPanes();
nextPageReached = false;
return;
}
nextStackPane.setCache(true);
currentStackPane.setCache(true);
Platform.runLater(() -> {
boolean useTranslateX = nextStackPane.getTranslateX() != 0;
if (currentAnimatedIndex > previousIndex) {
if (!useTranslateX) {
nextStackPane.setTranslateX(currentStackPane.getWidth());
}
nextStackPane.setVisible(true);
timeline = new Timeline();
KeyFrame k1 = new KeyFrame(Duration.millis(0),
new KeyValue(currentStackPane.translateXProperty(),
useTranslateX ? currentStackPane.getTranslateX() : 0,
interpolator),
new KeyValue(nextStackPane.translateXProperty(),
useTranslateX ?
nextStackPane.getTranslateX() : currentStackPane.getWidth(), interpolator));
KeyFrame k2 = new KeyFrame(DURATION,
swipeAnimationEndEventHandler,
new KeyValue(currentStackPane.translateXProperty(), -currentStackPane.getWidth(), interpolator),
new KeyValue(nextStackPane.translateXProperty(), 0, interpolator));
timeline.getKeyFrames().setAll(k1, k2);
timeline.play();
} else {
if (!useTranslateX) {
nextStackPane.setTranslateX(-currentStackPane.getWidth());
}
nextStackPane.setVisible(true);
timeline = new Timeline();
KeyFrame k1 = new KeyFrame(Duration.millis(0),
new KeyValue(currentStackPane.translateXProperty(),
useTranslateX ? currentStackPane.getTranslateX() : 0,
interpolator),
new KeyValue(nextStackPane.translateXProperty(),
useTranslateX ? nextStackPane.getTranslateX() : -currentStackPane.getWidth(),
interpolator));
KeyFrame k2 = new KeyFrame(DURATION,
swipeAnimationEndEventHandler,
new KeyValue(currentStackPane.translateXProperty(), currentStackPane.getWidth(), interpolator),
new KeyValue(nextStackPane.translateXProperty(), 0, interpolator));
timeline.getKeyFrames().setAll(k1, k2);
timeline.play();
}
});
}
private void swapPanes() {
StackPane temp = currentStackPane;
currentStackPane = nextStackPane;
nextStackPane = temp;
currentStackPane.setTranslateX(0);
currentStackPane.setCache(false);
nextStackPane.setTranslateX(0);
nextStackPane.setCache(false);
nextStackPane.setVisible(false);
nextStackPane.getChildren().clear();
}
private void animateClamping(boolean rightToLeft) {
if (rightToLeft) {
timeline = new Timeline();
KeyFrame k1 = new KeyFrame(Duration.millis(0),
new KeyValue(currentStackPane.translateXProperty(), currentStackPane.getTranslateX(), interpolator),
new KeyValue(nextStackPane.translateXProperty(), nextStackPane.getTranslateX(), interpolator));
KeyFrame k2 = new KeyFrame(DURATION,
clampAnimationEndEventHandler,
new KeyValue(currentStackPane.translateXProperty(), 0, interpolator),
new KeyValue(nextStackPane.translateXProperty(), currentStackPane.getWidth(), interpolator));
timeline.getKeyFrames().setAll(k1, k2);
timeline.play();
} else {
timeline = new Timeline();
KeyFrame k1 = new KeyFrame(Duration.millis(0),
new KeyValue(currentStackPane.translateXProperty(), currentStackPane.getTranslateX(), interpolator),
new KeyValue(nextStackPane.translateXProperty(), nextStackPane.getTranslateX(), interpolator));
KeyFrame k2 = new KeyFrame(DURATION,
clampAnimationEndEventHandler,
new KeyValue(currentStackPane.translateXProperty(), 0, interpolator),
new KeyValue(nextStackPane.translateXProperty(), -currentStackPane.getWidth(), interpolator));
timeline.getKeyFrames().setAll(k1, k2);
timeline.play();
}
}
class NavigationControl extends StackPane {
private HBox controlBox;
private Button leftArrowButton;
private StackPane leftArrow;
private Button rightArrowButton;
private StackPane rightArrow;
private ToggleGroup indicatorButtons;
private Label pageInformation;
private double minButtonSize = -1;
public NavigationControl() {
getStyleClass().setAll("pagination-control");
addEventHandler(MouseEvent.MOUSE_PRESSED, behavior::mousePressed);
controlBox = new HBox();
controlBox.getStyleClass().add("control-box");
leftArrowButton = new Button();
leftArrowButton.setAccessibleText(getString("Accessibility.title.Pagination.PreviousButton"));
minButtonSize = leftArrowButton.getFont().getSize() * 2;
leftArrowButton.fontProperty().addListener((arg0, arg1, newFont) -> {
minButtonSize = newFont.getSize() * 2;
for(Node child: controlBox.getChildren()) {
((Control)child).setMinSize(minButtonSize, minButtonSize);
}
requestLayout();
});
leftArrowButton.setMinSize(minButtonSize, minButtonSize);
leftArrowButton.prefWidthProperty().bind(leftArrowButton.minWidthProperty());
leftArrowButton.prefHeightProperty().bind(leftArrowButton.minHeightProperty());
leftArrowButton.getStyleClass().add("left-arrow-button");
leftArrowButton.setFocusTraversable(false);
HBox.setMargin(leftArrowButton, new Insets(0, snapSizeX(arrowButtonGap.get()), 0, 0));
leftArrow = new StackPane();
leftArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
leftArrowButton.setGraphic(leftArrow);
leftArrow.getStyleClass().add("left-arrow");
rightArrowButton = new Button();
rightArrowButton.setAccessibleText(getString("Accessibility.title.Pagination.NextButton"));
rightArrowButton.setMinSize(minButtonSize, minButtonSize);
rightArrowButton.prefWidthProperty().bind(rightArrowButton.minWidthProperty());
rightArrowButton.prefHeightProperty().bind(rightArrowButton.minHeightProperty());
rightArrowButton.getStyleClass().add("right-arrow-button");
rightArrowButton.setFocusTraversable(false);
HBox.setMargin(rightArrowButton, new Insets(0, 0, 0, snapSizeX(arrowButtonGap.get())));
rightArrow = new StackPane();
rightArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
rightArrowButton.setGraphic(rightArrow);
rightArrow.getStyleClass().add("right-arrow");
indicatorButtons = new ToggleGroup();
pageInformation = new Label();
pageInformation.getStyleClass().add("page-information");
getChildren().addAll(controlBox, pageInformation);
initializeNavigationHandlers();
initializePageIndicators();
updatePageIndex();
arrowButtonGap.addListener((observable, oldValue, newValue) -> {
if (newValue.doubleValue() == 0) {
HBox.setMargin(leftArrowButton, null);
HBox.setMargin(rightArrowButton, null);
} else {
HBox.setMargin(leftArrowButton, new Insets(0, snapSizeX(newValue.doubleValue()), 0, 0));
HBox.setMargin(rightArrowButton, new Insets(0, 0, 0, snapSizeX(newValue.doubleValue())));
}
});
}
private void initializeNavigationHandlers() {
leftArrowButton.setOnAction(arg0 -> {
getNode().requestFocus();
selectPrevious();
requestLayout();
});
rightArrowButton.setOnAction(arg0 -> {
getNode().requestFocus();
selectNext();
requestLayout();
});
pagination.currentPageIndexProperty().addListener((arg0, arg1, arg2) -> {
previousIndex = arg1.intValue();
currentIndex = arg2.intValue();
updatePageIndex();
if (animate) {
currentAnimatedIndex = currentIndex;
animateSwitchPage();
} else {
createPage(currentStackPane, currentIndex);
}
});
}
private void initializePageIndicators() {
previousIndicatorCount = 0;
controlBox.getChildren().clear();
clearIndicatorButtons();
controlBox.getChildren().add(leftArrowButton);
for (int i = fromIndex; i <= toIndex; i++) {
IndicatorButton ib = new IndicatorButton(i);
ib.setMinSize(minButtonSize, minButtonSize);
ib.setToggleGroup(indicatorButtons);
controlBox.getChildren().add(ib);
}
controlBox.getChildren().add(rightArrowButton);
}
private void clearIndicatorButtons() {
for (Toggle toggle : indicatorButtons.getToggles()) {
if (toggle instanceof IndicatorButton) {
IndicatorButton indicatorButton = (IndicatorButton) toggle;
indicatorButton.release();
}
}
indicatorButtons.getToggles().clear();
}
private void updatePageIndicators() {
for (int i = 0; i < indicatorButtons.getToggles().size(); i++) {
IndicatorButton ib = (IndicatorButton)indicatorButtons.getToggles().get(i);
if (ib.getPageNumber() == currentIndex) {
ib.setSelected(true);
updatePageInformation();
break;
}
}
getSkinnable().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
}
private void updatePageIndex() {
if (pageCount == maxPageIndicatorCount) {
if (changePageSet()) {
initializePageIndicators();
}
}
updatePageIndicators();
requestLayout();
}
private void updatePageInformation() {
String currentPageNumber = Integer.toString(currentIndex + 1);
String lastPageNumber = getPageCount() == Pagination.INDETERMINATE ? "..." : Integer.toString(getPageCount());
pageInformation.setText(currentPageNumber + "/" + lastPageNumber);
}
private int previousIndicatorCount = 0;
private void layoutPageIndicators() {
final double left = snappedLeftInset();
final double right = snappedRightInset();
final double width = snapSizeX(getWidth()) - (left + right);
final double controlBoxleft = controlBox.snappedLeftInset();
final double controlBoxRight = controlBox.snappedRightInset();
final double leftArrowWidth = snapSizeX(Utils.boundedSize(leftArrowButton.prefWidth(-1), leftArrowButton.minWidth(-1), leftArrowButton.maxWidth(-1)));
final double rightArrowWidth = snapSizeX(Utils.boundedSize(rightArrowButton.prefWidth(-1), rightArrowButton.minWidth(-1), rightArrowButton.maxWidth(-1)));
final double spacing = snapSizeX(controlBox.getSpacing());
double w = width - (controlBoxleft + leftArrowWidth + 2* arrowButtonGap.get() + spacing + rightArrowWidth + controlBoxRight);
if (isPageInformationVisible() &&
(Side.LEFT.equals(getPageInformationAlignment()) ||
Side.RIGHT.equals(getPageInformationAlignment()))) {
w -= snapSizeX(pageInformation.prefWidth(-1));
}
double x = 0;
int indicatorCount = 0;
for (int i = 0; i < getMaxPageIndicatorCount(); i++) {
int index = i < indicatorButtons.getToggles().size() ? i : indicatorButtons.getToggles().size() - 1;
double iw = minButtonSize;
if (index != -1) {
IndicatorButton ib = (IndicatorButton)indicatorButtons.getToggles().get(index);
iw = snapSizeX(Utils.boundedSize(ib.prefWidth(-1), ib.minWidth(-1), ib.maxWidth(-1)));
}
x += (iw + spacing);
if (x > w) {
break;
}
indicatorCount++;
}
if (indicatorCount == 0) {
indicatorCount = 1;
}
if (indicatorCount != previousIndicatorCount) {
if (indicatorCount < getMaxPageIndicatorCount()) {
maxPageIndicatorCount = indicatorCount;
} else {
maxPageIndicatorCount = getMaxPageIndicatorCount();
}
int lastIndicatorButtonIndex;
if (pageCount > maxPageIndicatorCount) {
pageCount = maxPageIndicatorCount;
lastIndicatorButtonIndex = maxPageIndicatorCount - 1;
} else {
if (indicatorCount > getPageCount()) {
pageCount = getPageCount();
lastIndicatorButtonIndex = getPageCount() - 1;
} else {
pageCount = indicatorCount;
lastIndicatorButtonIndex = indicatorCount - 1;
}
}
if (currentIndex >= toIndex) {
toIndex = currentIndex;
fromIndex = toIndex - lastIndicatorButtonIndex;
} else if (currentIndex <= fromIndex) {
fromIndex = currentIndex;
toIndex = fromIndex + lastIndicatorButtonIndex;
} else {
toIndex = fromIndex + lastIndicatorButtonIndex;
}
if (toIndex > getPageCount() - 1) {
toIndex = getPageCount() - 1;
}
if (fromIndex < 0) {
fromIndex = 0;
toIndex = fromIndex + lastIndicatorButtonIndex;
}
initializePageIndicators();
updatePageIndicators();
previousIndicatorCount = indicatorCount;
}
}
private boolean changePageSet() {
int index = indexToIndicatorButtonsIndex(currentIndex);
int lastIndicatorButtonIndex = maxPageIndicatorCount - 1;
if (previousIndex < currentIndex &&
index == 0 &&
lastIndicatorButtonIndex != 0 &&
index % lastIndicatorButtonIndex == 0) {
fromIndex = currentIndex;
toIndex = fromIndex + lastIndicatorButtonIndex;
} else if (currentIndex < previousIndex &&
index == lastIndicatorButtonIndex &&
lastIndicatorButtonIndex != 0 &&
index % lastIndicatorButtonIndex == 0) {
toIndex = currentIndex;
fromIndex = toIndex - lastIndicatorButtonIndex;
} else {
if (currentIndex < fromIndex || currentIndex > toIndex) {
fromIndex = currentIndex - index;
toIndex = fromIndex + lastIndicatorButtonIndex;
} else {
return false;
}
}
if (toIndex > getPageCount() - 1) {
if (fromIndex > getPageCount() - 1) {
return false;
} else {
toIndex = getPageCount() - 1;
}
}
if (fromIndex < 0) {
fromIndex = 0;
toIndex = fromIndex + lastIndicatorButtonIndex;
}
return true;
}
private int indexToIndicatorButtonsIndex(int index) {
if (index >= fromIndex && index <= toIndex) {
return index - fromIndex;
}
int i = 0;
int from = fromIndex;
int to = toIndex;
if (currentIndex > previousIndex) {
while(from < getPageCount() && to < getPageCount()) {
from += i;
to += i;
if (index >= from && index <= to) {
if (index == from) {
return 0;
} else if (index == to) {
return maxPageIndicatorCount - 1;
}
return index - from;
}
i += maxPageIndicatorCount;
}
} else {
while (from > 0 && to > 0) {
from -= i;
to -= i;
if (index >= from && index <= to) {
if (index == from) {
return 0;
} else if (index == to) {
return maxPageIndicatorCount - 1;
}
return index - from;
}
i += maxPageIndicatorCount;
}
}
return maxPageIndicatorCount - 1;
}
private Pos sideToPos(Side s) {
if (Side.TOP.equals(s)) {
return Pos.TOP_CENTER;
} else if (Side.RIGHT.equals(s)) {
return Pos.CENTER_RIGHT;
} else if (Side.BOTTOM.equals(s)) {
return Pos.BOTTOM_CENTER;
}
return Pos.CENTER_LEFT;
}
@Override protected double computeMinWidth(double height) {
double left = snappedLeftInset();
double right = snappedRightInset();
double leftArrowWidth = snapSizeX(Utils.boundedSize(leftArrowButton.prefWidth(-1), leftArrowButton.minWidth(-1), leftArrowButton.maxWidth(-1)));
double rightArrowWidth = snapSizeX(Utils.boundedSize(rightArrowButton.prefWidth(-1), rightArrowButton.minWidth(-1), rightArrowButton.maxWidth(-1)));
double spacing = snapSizeX(controlBox.getSpacing());
double pageInformationWidth = 0;
Side side = getPageInformationAlignment();
if (Side.LEFT.equals(side) || Side.RIGHT.equals(side)) {
pageInformationWidth = snapSizeX(pageInformation.prefWidth(-1));
}
double arrowGap = arrowButtonGap.get();
return left + leftArrowWidth + 2 *arrowGap + minButtonSize
+ 2 * spacing + rightArrowWidth + right + pageInformationWidth;
}
@Override protected double computeMinHeight(double width) {
return computePrefHeight(width);
}
@Override protected double computePrefWidth(double height) {
final double left = snappedLeftInset();
final double right = snappedRightInset();
final double controlBoxWidth = snapSizeX(controlBox.prefWidth(height));
double pageInformationWidth = 0;
Side side = getPageInformationAlignment();
if (Side.LEFT.equals(side) || Side.RIGHT.equals(side)) {
pageInformationWidth = snapSizeX(pageInformation.prefWidth(-1));
}
return left + controlBoxWidth + right + pageInformationWidth;
}
@Override protected double computePrefHeight(double width) {
final double top = snappedTopInset();
final double bottom = snappedBottomInset();
final double boxHeight = snapSizeY(controlBox.prefHeight(width));
double pageInformationHeight = 0;
Side side = getPageInformationAlignment();
if (Side.TOP.equals(side) || Side.BOTTOM.equals(side)) {
pageInformationHeight = snapSizeY(pageInformation.prefHeight(-1));
}
return top + boxHeight + pageInformationHeight + bottom;
}
@Override protected void layoutChildren() {
final double top = snappedTopInset();
final double bottom = snappedBottomInset();
final double left = snappedLeftInset();
final double right = snappedRightInset();
final double width = snapSizeX(getWidth()) - (left + right);
final double height = snapSizeY(getHeight()) - (top + bottom);
final double controlBoxWidth = snapSizeX(controlBox.prefWidth(-1));
final double controlBoxHeight = snapSizeY(controlBox.prefHeight(-1));
final double pageInformationWidth = snapSizeX(pageInformation.prefWidth(-1));
final double pageInformationHeight = snapSizeY(pageInformation.prefHeight(-1));
leftArrowButton.setDisable(false);
rightArrowButton.setDisable(false);
if (currentIndex == 0) {
leftArrowButton.setDisable(true);
}
if (currentIndex == (getPageCount() - 1)) {
rightArrowButton.setDisable(true);
}
applyCss();
leftArrowButton.setVisible(isArrowsVisible());
rightArrowButton.setVisible(isArrowsVisible());
pageInformation.setVisible(isPageInformationVisible());
layoutPageIndicators();
HPos controlBoxHPos = controlBox.getAlignment().getHpos();
VPos controlBoxVPos = controlBox.getAlignment().getVpos();
double controlBoxX = left + Utils.computeXOffset(width, controlBoxWidth, controlBoxHPos);
double controlBoxY = top + Utils.computeYOffset(height, controlBoxHeight, controlBoxVPos);
if (isPageInformationVisible()) {
Pos p = sideToPos(getPageInformationAlignment());
HPos pageInformationHPos = p.getHpos();
VPos pageInformationVPos = p.getVpos();
double pageInformationX = left + Utils.computeXOffset(width, pageInformationWidth, pageInformationHPos);
double pageInformationY = top + Utils.computeYOffset(height, pageInformationHeight, pageInformationVPos);
if (Side.TOP.equals(getPageInformationAlignment())) {
pageInformationY = top;
controlBoxY = top + pageInformationHeight;
} else if (Side.RIGHT.equals(getPageInformationAlignment())) {
pageInformationX = width - right - pageInformationWidth;
} else if (Side.BOTTOM.equals(getPageInformationAlignment())) {
controlBoxY = top;
pageInformationY = top + controlBoxHeight;
} else if (Side.LEFT.equals(getPageInformationAlignment())) {
pageInformationX = left;
}
layoutInArea(pageInformation, pageInformationX, pageInformationY, pageInformationWidth, pageInformationHeight, 0, pageInformationHPos, pageInformationVPos);
}
layoutInArea(controlBox, controlBoxX, controlBoxY, controlBoxWidth, controlBoxHeight, 0, controlBoxHPos, controlBoxVPos);
}
}
class IndicatorButton extends ToggleButton {
private final ListChangeListener<String> updateSkinIndicatorType =
c -> setIndicatorType();
private final ChangeListener<Boolean> updateTooltipVisibility =
(ob, oldValue, newValue) -> setTooltipVisible(newValue);
private int pageNumber;
public IndicatorButton(int pageNumber) {
this.pageNumber = pageNumber;
setFocusTraversable(false);
setIndicatorType();
setTooltipVisible(isTooltipVisible());
getSkinnable().getStyleClass().addListener(updateSkinIndicatorType);
setOnAction(arg0 -> {
getNode().requestFocus();
int selected = getCurrentPageIndex();
if (selected != IndicatorButton.this.pageNumber) {
pagination.setCurrentPageIndex(IndicatorButton.this.pageNumber);
requestLayout();
}
});
tooltipVisibleProperty().addListener(updateTooltipVisibility);
prefHeightProperty().bind(minHeightProperty());
setAccessibleRole(AccessibleRole.PAGE_ITEM);
}
private void setIndicatorType() {
if (getSkinnable().getStyleClass().contains(Pagination.STYLE_CLASS_BULLET)) {
getStyleClass().remove("number-button");
getStyleClass().add("bullet-button");
setText(null);
prefWidthProperty().bind(minWidthProperty());
} else {
getStyleClass().remove("bullet-button");
getStyleClass().add("number-button");
setText(Integer.toString(this.pageNumber + 1));
prefWidthProperty().unbind();
}
}
private void setTooltipVisible(boolean b) {
if (b) {
setTooltip(new Tooltip(Integer.toString(IndicatorButton.this.pageNumber + 1)));
} else {
setTooltip(null);
}
}
public int getPageNumber() {
return this.pageNumber;
}
@Override public void fire() {
if (getToggleGroup() == null || !isSelected()) {
super.fire();
}
}
public void release() {
getSkinnable().getStyleClass().removeListener(updateSkinIndicatorType);
tooltipVisibleProperty().removeListener(updateTooltipVisibility);
}
@Override
public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
switch (attribute) {
case TEXT: return getText();
case SELECTED: return isSelected();
default: return super.queryAccessibleAttribute(attribute, parameters);
}
}
@Override
public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
switch (action) {
case REQUEST_FOCUS:
getSkinnable().setCurrentPageIndex(pageNumber);
break;
default: super.executeAccessibleAction(action);
}
}
}
private static final Boolean DEFAULT_ARROW_VISIBLE = Boolean.FALSE;
private static final Boolean DEFAULT_PAGE_INFORMATION_VISIBLE = Boolean.FALSE;
private static final Side DEFAULT_PAGE_INFORMATION_ALIGNMENT = Side.BOTTOM;
private static final Boolean DEFAULT_TOOLTIP_VISIBLE = Boolean.FALSE;
private static class StyleableProperties {
private static final CssMetaData<Pagination,Boolean> ARROWS_VISIBLE =
new CssMetaData<Pagination,Boolean>("-fx-arrows-visible",
BooleanConverter.getInstance(), DEFAULT_ARROW_VISIBLE) {
@Override
public boolean isSettable(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return skin.arrowsVisible == null || !skin.arrowsVisible.isBound();
}
@Override
public StyleableProperty<Boolean> getStyleableProperty(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return (StyleableProperty<Boolean>)(WritableValue<Boolean>)skin.arrowsVisibleProperty();
}
};
private static final CssMetaData<Pagination,Boolean> PAGE_INFORMATION_VISIBLE =
new CssMetaData<Pagination,Boolean>("-fx-page-information-visible",
BooleanConverter.getInstance(), DEFAULT_PAGE_INFORMATION_VISIBLE) {
@Override
public boolean isSettable(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return skin.pageInformationVisible == null || !skin.pageInformationVisible.isBound();
}
@Override
public StyleableProperty<Boolean> getStyleableProperty(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return (StyleableProperty<Boolean>)(WritableValue<Boolean>)skin.pageInformationVisibleProperty();
}
};
private static final CssMetaData<Pagination,Side> PAGE_INFORMATION_ALIGNMENT =
new CssMetaData<Pagination,Side>("-fx-page-information-alignment",
new EnumConverter<Side>(Side.class), DEFAULT_PAGE_INFORMATION_ALIGNMENT) {
@Override
public boolean isSettable(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return skin.pageInformationAlignment == null || !skin.pageInformationAlignment.isBound();
}
@Override
public StyleableProperty<Side> getStyleableProperty(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return (StyleableProperty<Side>)(WritableValue<Side>)skin.pageInformationAlignmentProperty();
}
};
private static final CssMetaData<Pagination,Boolean> TOOLTIP_VISIBLE =
new CssMetaData<Pagination,Boolean>("-fx-tooltip-visible",
BooleanConverter.getInstance(), DEFAULT_TOOLTIP_VISIBLE) {
@Override
public boolean isSettable(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return skin.tooltipVisible == null || !skin.tooltipVisible.isBound();
}
@Override
public StyleableProperty<Boolean> getStyleableProperty(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return (StyleableProperty<Boolean>)(WritableValue<Boolean>)skin.tooltipVisibleProperty();
}
};
private static final CssMetaData<Pagination,Number> ARROW_BUTTON_GAP =
new CssMetaData<Pagination,Number>("-fx-arrow-button-gap", SizeConverter.getInstance(), 4) {
@Override public boolean isSettable(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return skin.arrowButtonGap == null ||
!skin.arrowButtonGap.isBound();
}
@Override public StyleableProperty<Number> getStyleableProperty(Pagination n) {
final PaginationSkin skin = (PaginationSkin) n.getSkin();
return (StyleableProperty<Number>)(WritableValue<Number>)skin.arrowButtonGapProperty();
}
};
private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
static {
final List<CssMetaData<? extends Styleable, ?>> styleables =
new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
styleables.add(ARROWS_VISIBLE);
styleables.add(PAGE_INFORMATION_VISIBLE);
styleables.add(PAGE_INFORMATION_ALIGNMENT);
styleables.add(TOOLTIP_VISIBLE);
styleables.add(ARROW_BUTTON_GAP);
STYLEABLES = Collections.unmodifiableList(styleables);
}
}
public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
return StyleableProperties.STYLEABLES;
}
@Override
public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
return getClassCssMetaData();
}
}
