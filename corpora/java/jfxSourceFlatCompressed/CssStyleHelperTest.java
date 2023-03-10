package test.javafx.scene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import test.util.Util;
import static org.junit.Assert.assertTrue;
public class CssStyleHelperTest {
private static CountDownLatch startupLatch;
private static StackPane root;
private static Stage stage;
private static Label label1;
private static Button button;
private static CheckBox checkBox;
private static Label label2;
public static class TestApp extends Application {
@Override
public void start(Stage primaryStage) throws Exception {
stage = primaryStage;
label1 = new Label("Label1");
button = new Button("aButton");
checkBox = new CheckBox("aCheckBox");
label2 = new Label("Label2");
root = new StackPane();
root.getChildren().addAll(label1, button, checkBox, label2);
Scene scene = new Scene(root, 200, 200);
scene.getStylesheets().add(getClass().getResource("RootFont.css").toExternalForm());
primaryStage.setScene(scene);
primaryStage.setOnShown(l -> {
Platform.runLater(() -> startupLatch.countDown());
});
primaryStage.show();
}
}
@BeforeClass
public static void initFX() throws Exception {
startupLatch = new CountDownLatch(1);
new Thread(() -> Application.launch(TestApp.class, (String[]) null)).start();
assertTrue("Timeout waiting for FX runtime to start",
startupLatch.await(15, TimeUnit.SECONDS));
}
@Test
public void testCssIsCorrectlyAppliedToLabelOnStageHideAndShow() throws Exception {
Assert.assertNull("Label1 should have no background", label1.getBackground());
Assert.assertNull("Label2 should have no background", label2.getBackground());
startupLatch = new CountDownLatch(1);
Util.runAndWait(() -> {
stage.hide();
stage.show();
});
assertTrue("Timeout waiting for Stage to show after hide",
startupLatch.await(15, TimeUnit.SECONDS));
Assert.assertNull("Label1 should have no background", label1.getBackground());
Assert.assertNull("Label2 should have no background", label2.getBackground());
}
@AfterClass
public static void teardownOnce() {
Platform.runLater(() -> {
stage.hide();
Platform.exit();
});
}
}
