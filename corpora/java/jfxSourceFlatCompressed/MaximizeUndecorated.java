package test.javafx.stage;
import com.sun.javafx.PlatformUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.util.Util;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.fail;
import static org.junit.Assume.*;
public class MaximizeUndecorated {
static CountDownLatch startupLatch;
static Stage stage;
static final int POS = 500;
public static class TestApp extends Application {
@Override
public void start(Stage primaryStage) throws Exception {
primaryStage.setScene(new Scene(new Group()));
stage = primaryStage;
stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e ->
Platform.runLater(startupLatch::countDown));
stage.initStyle(StageStyle.UNDECORATED);
stage.setX(POS);
stage.setY(POS);
stage.setOnShown(e -> stage.setMaximized(true));
stage.show();
}
}
@BeforeClass
public static void initFX() {
startupLatch = new CountDownLatch(1);
new Thread(() -> Application.launch(TestApp.class, (String[]) null)).start();
try {
if (!startupLatch.await(15, TimeUnit.SECONDS)) {
fail("Timeout waiting for FX runtime to start");
}
} catch (InterruptedException ex) {
fail("Unexpected exception: " + ex);
}
}
@Test
public void testMaximize() throws Exception {
assumeTrue(!PlatformUtil.isMac());
Util.sleep(200);
boolean movedToTopCorner = stage.getY() != POS && stage.getX() != POS;
Assert.assertTrue("Stage has moved to desktop top corner", movedToTopCorner);
}
@AfterClass
public static void teardown() {
Platform.runLater(stage::hide);
Platform.exit();
}
}
