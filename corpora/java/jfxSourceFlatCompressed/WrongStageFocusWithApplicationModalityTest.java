package test.robot.javafx.stage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.util.Util;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
public class WrongStageFocusWithApplicationModalityTest {
private static Robot robot;
private static Stage stage;
private static Alert alert;
private static CountDownLatch startupLatch = new CountDownLatch(4);
private static CountDownLatch alertCloseLatch = new CountDownLatch(3);
@BeforeClass
public static void initFX() throws Exception {
new Thread(() -> Application.launch(TestApp.class, (String[]) null)).start();
waitForLatch(startupLatch, 15, "FX runtime failed to start.");
}
@Test(timeout = 25000)
public void testWindowFocusByClosingAlerts() throws Exception {
Thread.sleep(3000);
mouseClick();
Thread.sleep(1000);
keyPress(KeyCode.ESCAPE);
Thread.sleep(500);
keyPress(KeyCode.ESCAPE);
Thread.sleep(500);
keyPress(KeyCode.ESCAPE);
Thread.sleep(500);
waitForLatch(alertCloseLatch, 10, "Alerts not closed, wrong focus");
}
@AfterClass
public static void exit() {
Platform.runLater(() -> {
stage.hide();
});
Platform.exit();
}
private static void waitForLatch(CountDownLatch latch, int seconds, String msg) throws Exception {
Assert.assertTrue("Timeout: " + msg, latch.await(seconds, TimeUnit.SECONDS));
}
private static void keyPress(KeyCode code) throws Exception {
Util.runAndWait(() -> {
robot.keyPress(code);
robot.keyRelease(code);
});
}
private static void mouseClick() {
Util.runAndWait(() -> {
robot.mouseMove((int) (alert.getX() + alert.getWidth() / 2),
(int) (alert.getY() + alert.getHeight() / 2));
Util.sleep(100);
robot.mousePress(MouseButton.PRIMARY);
robot.mouseRelease(MouseButton.PRIMARY);
});
}
public static class TestApp extends Application {
@Override
public void start(Stage primaryStage) {
robot = new Robot();
stage = primaryStage;
BorderPane root = new BorderPane();
stage.setScene(new Scene(root, 500, 500));
stage.setOnShown(event -> Platform.runLater(startupLatch::countDown));
stage.setAlwaysOnTop(true);
stage.show();
showAlert("Alert 1");
showAlert("Alert 2");
alert = showAlert("Alert 3");
}
private Alert showAlert(String title) {
Alert alert = new Alert(Alert.AlertType.INFORMATION);
alert.initOwner(stage);
alert.setTitle(title);
alert.setOnShown(event -> Platform.runLater(startupLatch::countDown));
alert.setOnHidden(event -> Platform.runLater(alertCloseLatch::countDown));
alert.show();
return alert;
}
}
}
