package test.javafx.scene.web;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.util.Util;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import static javafx.concurrent.Worker.State.SUCCEEDED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
public class WebIObserverTest {
private static final CountDownLatch launchLatch = new CountDownLatch(1);
private static WebIObserverTestApp WebIObserverTestApp;
private WebView webView;
public static class WebIObserverTestApp extends Application {
private Stage primaryStage = null;
@Override
public void init() {
WebIObserverTest.WebIObserverTestApp = this;
}
@Override
public void start(Stage primaryStage) throws Exception {
Platform.setImplicitExit(false);
this.primaryStage = primaryStage;
launchLatch.countDown();
}
}
@BeforeClass
public static void setupOnce() {
new Thread(() -> Application.launch(WebIObserverTestApp.class, (String[])null)).start();
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
WebIObserverTestApp.primaryStage.setScene(new Scene(webView));
WebIObserverTestApp.primaryStage.show();
});
}
@Test public void testIO() {
final CountDownLatch webViewStateLatch = new CountDownLatch(1);
URL resource = WebIObserverTest.class.getResource("testIObserver.html");
assertNotNull("Resource was null", resource);
Util.runAndWait(() -> {
assertNotNull(webView);
webView.getEngine().getLoadWorker().stateProperty().
addListener((observable, oldValue, newValue) -> {
if (newValue == SUCCEEDED) {
webViewStateLatch.countDown();
}
});
webView.getEngine().load(resource.toExternalForm());
});
assertTrue("Timeout waiting for succeeded state", Util.await(webViewStateLatch));
Util.sleep(500);
Util.runAndWait(() ->
assertEquals("Unknown intersection ratio", "?", getIntersectionRatio()));
Util.runAndWait(() -> webView.getEngine().executeScript("testIO()"));
Util.sleep(100);
Util.runAndWait(() ->
assertEquals("Intersection ratio", "0.5", getIntersectionRatio()));
}
private String getIntersectionRatio() {
Object object = webView.getEngine().executeScript("document.querySelector('#output pre').innerText");
assertNotNull("InnerText was null", object);
return String.valueOf(object);
}
}
