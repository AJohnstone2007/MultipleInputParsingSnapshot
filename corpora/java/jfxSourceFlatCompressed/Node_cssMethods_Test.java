package test.javafx.scene;
import java.util.Arrays;
import java.util.Collection;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import test.com.sun.javafx.pgstub.StubImageLoaderFactory;
import test.com.sun.javafx.pgstub.StubPlatformImageInfo;
import test.com.sun.javafx.pgstub.StubToolkit;
import test.com.sun.javafx.test.CssMethodsTestBase;
import test.com.sun.javafx.test.ValueComparator;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
@RunWith(Parameterized.class)
public final class Node_cssMethods_Test extends CssMethodsTestBase {
private static final Node TEST_NODE = new Rectangle();
private static final String TEST_CURSOR_URL = "file:cursor.png";
@BeforeClass
public static void configureImageLoaderFactory() {
final StubImageLoaderFactory imageLoaderFactory =
((StubToolkit) Toolkit.getToolkit()).getImageLoaderFactory();
imageLoaderFactory.reset();
imageLoaderFactory.registerImage(
TEST_CURSOR_URL,
new StubPlatformImageInfo(32, 32));
}
@Parameters
public static Collection data() {
return Arrays.asList(new Object[] {
config(TEST_NODE, "cursor", null, "-fx-cursor", Cursor.cursor(TEST_CURSOR_URL),
new ValueComparator() {
@Override
public boolean equals(final Object expected,
final Object actual) {
if (actual instanceof ImageCursor) {
final Image cursorImage =
((ImageCursor) actual).getImage();
if ((cursorImage != null)
&& cursorImage.getUrl().equals(
TEST_CURSOR_URL)) {
return true;
}
}
return false;
}
}),
config("cursor", null, "-fx-cursor", Cursor.MOVE),
config("effect", null, "-fx-effect", new Shadow()),
config("focusTraversable", false, "-fx-focus-traversable", true),
config("managed", true, "-fx-managed", false, false),
config("opacity", 1.0, "-fx-opacity", 0.5),
config("opacity", 0.5, "-fx-opacity", null, 0.0),
config("viewOrder", 0.0, "-fx-view-order", 0.5),
config("viewOrder", 0.5, "-fx-view-order", null, 0.0),
config("blendMode", BlendMode.SRC_OVER, "-fx-blend-mode", BlendMode.SRC_ATOP),
config("rotate", 0.0, "-fx-rotate", 45.0),
config("rotate", 0.5, "-fx-rotate", null, 0.0),
config("scaleX", 1.0, "-fx-scale-x", 0.5),
config("scaleX", 0.5, "-fx-scale-x", null, 0.0),
config("scaleY", 1.0, "-fx-scale-y", 2.0),
config("scaleY", 0.5, "-fx-scale-y", null, 0.0),
config("scaleZ", 1.0, "-fx-scale-z", 1.5),
config("scaleZ", 0.5, "-fx-scale-z", null, 0.0),
config("translateX", 0.0, "-fx-translate-x", 10.0),
config("translateX", 1.0, "-fx-translate-x", null, 0.0),
config("translateY", 0.0, "-fx-translate-y", 20.0),
config("translateY", 1.0, "-fx-translate-y", null, 0.0),
config("translateZ", 0.0, "-fx-translate-z", 30.0),
config("translateZ", 1.0, "-fx-translate-z", null, 0.0),
});
}
public static Object[] config(
final String propertyName,
final Object initialValue,
final String cssPropertyKey,
final Object cssPropertyValue) {
return config(TEST_NODE, propertyName, initialValue,
cssPropertyKey, cssPropertyValue);
}
public static Object[] config(
final String propertyName,
final Object initialValue,
final String cssPropertyKey,
final Object cssPropertyValue,
final Object expectedFinalValue) {
return config(TEST_NODE, propertyName, initialValue,
cssPropertyKey, cssPropertyValue, expectedFinalValue);
}
public Node_cssMethods_Test(final Configuration configuration) {
super(configuration);
}
}
