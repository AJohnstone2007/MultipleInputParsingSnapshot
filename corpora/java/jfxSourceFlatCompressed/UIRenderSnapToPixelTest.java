package test.javafx.scene;
import com.sun.javafx.PlatformUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
public class UIRenderSnapToPixelTest {
private static final double scale = 1.25;
private static CountDownLatch startupLatch;
private static volatile Stage stage;
private static final double epsilon = 0.00001;
@BeforeClass
public static void setupOnce() throws Exception {
System.setProperty("glass.win.uiScale", String.valueOf(scale));
System.setProperty("glass.gtk.uiScale", String.valueOf(scale));
startupLatch = new CountDownLatch(1);
new Thread(() -> Application.launch(TestApp.class, (String[]) null)).start();
assertTrue("Timeout waiting for FX runtime to start", startupLatch.await(15, TimeUnit.SECONDS));
}
@AfterClass
public static void teardown() {
Platform.runLater(stage::hide);
Platform.exit();
}
@Test
public void testScrollPaneSnapChildrenToPixels() {
assumeTrue(PlatformUtil.isLinux() || PlatformUtil.isWindows());
Assert.assertEquals("Wrong render scale", scale, stage.getRenderScaleY(), 0.0001);
for (Node node : stage.getScene().getRoot().getChildrenUnmodifiable()) {
if (node instanceof ScrollPane) {
var sp = (ScrollPane) node;
Assert.assertEquals("Top inset not snapped to pixel", 0, ((sp.snappedTopInset() * scale) + epsilon) % 1, 0.0001);
Assert.assertEquals("Bottom inset not snapped to pixel", 0, ((sp.snappedBottomInset() * scale) + epsilon) % 1, 0.0001);
Assert.assertEquals("Left inset not snapped to pixel", 0, ((sp.snappedLeftInset() * scale) + epsilon) % 1, 0.0001);
Assert.assertEquals("Right inset not snapped to pixel", 0, ((sp.snappedRightInset() * scale) + epsilon) % 1, 0.0001);
}
}
}
public static class TestApp extends Application {
private static void run() {
startupLatch.countDown();
}
@Override
public void start(Stage primaryStage) throws Exception {
final Label label = new Label("This text may appear blurry at some screen scale without the fix for JDK-8211294");
final ScrollPane scrollpane = new ScrollPane(label);
scrollpane.setSnapToPixel(true);
final VBox root = new VBox();
root.getChildren().add(new Label("This text should be sharp at all screen scale"));
root.getChildren().add(scrollpane);
final Scene scene = new Scene(root);
primaryStage.setScene(scene);
stage = primaryStage;
stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> Platform.runLater(TestApp::run));
stage.show();
}
}
}
