package test.javafx.scene.web;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.util.Util;
import static javafx.concurrent.Worker.State.SUCCEEDED;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class CSSFilterTest {
private static final CountDownLatch launchLatch = new CountDownLatch(1);
static CSSFilterTestApp cssFilterTestApp;
private WebView webView;
public static class CSSFilterTestApp extends Application {
Stage primaryStage = null;
@Override
public void init() {
CSSFilterTest.cssFilterTestApp = this;
}
@Override
public void start(Stage primaryStage) throws Exception {
Platform.setImplicitExit(false);
this.primaryStage = primaryStage;
launchLatch.countDown();
}
}
private static String colorToString(Color c) {
int r = (int)(c.getRed() * 255.0);
int g = (int)(c.getGreen() * 255.0);
int b = (int)(c.getBlue() * 255.0);
int a = (int)(c.getOpacity() * 255.0);
return "rgba(" + r + "," + g + "," + b + "," + a + ")";
}
private void assertColorEquals(String msg, Color expected, Color actual, double delta) {
if (!testColorEquals(expected, actual, delta)) {
fail(msg + " expected:" + colorToString(expected)
+ " but was:" + colorToString(actual));
}
}
private void assertColorNotEquals(String msg, Color notExpected, Color actual, double delta) {
if (testColorEquals(notExpected, actual, delta)) {
fail(msg + " not expected:" + colorToString(notExpected)
+ " but was:" + colorToString(actual));
}
}
protected boolean testColorEquals(Color expected, Color actual, double delta) {
double deltaRed = Math.abs(expected.getRed() - actual.getRed());
double deltaGreen = Math.abs(expected.getGreen() - actual.getGreen());
double deltaBlue = Math.abs(expected.getBlue() - actual.getBlue());
double deltaOpacity = Math.abs(expected.getOpacity() - actual.getOpacity());
return (deltaRed <= delta && deltaGreen <= delta && deltaBlue <= delta && deltaOpacity <= delta);
}
@BeforeClass
public static void setupOnce() {
new Thread(() -> Application.launch(CSSFilterTestApp.class, (String[])null)).start();
assertTrue("Timeout waiting for FX runtime to start", Util.await(launchLatch));
}
@AfterClass
public static void tearDownOnce() {
Platform.exit();
}
@Before
public void setupTestObjects() {
Platform.runLater(() -> {
webView = new WebView();
cssFilterTestApp.primaryStage.setScene(new Scene(webView));
cssFilterTestApp.primaryStage.show();
});
}
@Ignore("JDK-8269067")
@Test public void testCSSFilterRendering() {
final CountDownLatch webViewStateLatch = new CountDownLatch(1);
Util.runAndWait(() -> {
assertNotNull(webView);
webView.getEngine().getLoadWorker().stateProperty().
addListener((observable, oldValue, newValue) -> {
if (newValue == SUCCEEDED) {
webView.requestFocus();
}
});
webView.focusedProperty().
addListener((observable, oldValue, newValue) -> {
if (newValue) {
webViewStateLatch.countDown();
}
});
final String urlString = CSSFilterTest.class.getResource("simpleImagewithfilter.html").toExternalForm();
webView.getEngine().load(urlString);
});
assertTrue("Timeout when waiting for focus change ", Util.await(webViewStateLatch));
Util.sleep(1000);
Util.runAndWait(() -> {
WritableImage snapshot = cssFilterTestApp.primaryStage.getScene().snapshot(null);
PixelReader pr = snapshot.getPixelReader();
final double delta = 0.07;
Color whiteColor = Color.rgb(255, 255, 255);
Color blueColor = Color.rgb(0, 0, 255);
assertColorEquals("Color should be opaque white:",
whiteColor, pr.getColor(0, 0), delta);
assertColorEquals("Color should be opaque white:",
whiteColor, pr.getColor(5, 0), delta);
assertColorEquals("Color should be opaque white:",
whiteColor, pr.getColor(0, 5), delta);
assertColorEquals("Color should be opaque blue:",
blueColor, pr.getColor(25, 25), delta);
assertColorEquals("Color should be opaque blue:",
blueColor, pr.getColor(190, 200), delta);
assertColorEquals("Color should be opaque blue:",
blueColor, pr.getColor(200, 190), delta);
assertColorEquals("Color should be opaque blue:",
blueColor, pr.getColor(200, 200), delta);
assertColorNotEquals("Color should not be opaque white:",
whiteColor, pr.getColor(220, 220), delta);
assertColorNotEquals("Color should not be opaque blue:",
blueColor, pr.getColor(220, 220), delta);
});
}
}
