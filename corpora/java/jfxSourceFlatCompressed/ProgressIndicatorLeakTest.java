package test.javafx.scene.control;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static test.util.Util.TIMEOUT;
import test.util.Util;
import test.util.memory.JMemoryBuddy;
public class ProgressIndicatorLeakTest {
@BeforeClass
public static void initFX() throws Exception {
CountDownLatch startupLatch = new CountDownLatch(1);
Platform.setImplicitExit(false);
Platform.startup(startupLatch::countDown);
Assert.assertTrue("Timeout waiting for FX runtime to start",
startupLatch.await(TIMEOUT, TimeUnit.MILLISECONDS));
}
@Test
public void leakDeterminationIndicator() throws Exception {
JMemoryBuddy.memoryTest((checker) -> {
CountDownLatch showingLatch = new CountDownLatch(1);
Util.runAndWait(() -> {
Stage stage = new Stage();
ProgressIndicator indicator = new ProgressIndicator(-1);
indicator.setSkin(new ProgressIndicatorSkin(indicator));
Scene scene = new Scene(indicator);
stage.setScene(scene);
indicator.setProgress(1.0);
Assert.assertEquals("size is wrong", 1, indicator.getChildrenUnmodifiable().size());
Node detIndicator = indicator.getChildrenUnmodifiable().get(0);
indicator.setProgress(-1.0);
indicator.setProgress(1.0);
checker.assertCollectable(detIndicator);
stage.setOnShown(l -> {
Platform.runLater(() -> showingLatch.countDown());
});
stage.show();
});
try {
Assert.assertTrue("Timeout waiting for setOnShown", showingLatch.await(15, TimeUnit.SECONDS));
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
});
}
@Test
public void stageLeakWhenTreeNotShowing() throws Exception {
JMemoryBuddy.memoryTest((checker) -> {
CountDownLatch showingLatch = new CountDownLatch(1);
AtomicReference<Stage> stage = new AtomicReference<>();
Util.runAndWait(() -> {
stage.set(new Stage());
Group root = new Group();
root.setVisible(false);
root.getChildren().add(new ProgressIndicator());
stage.get().setScene(new Scene(root));
stage.get().setOnShown(l -> {
Platform.runLater(() -> showingLatch.countDown());
});
stage.get().show();
});
try {
assertTrue("Timeout waiting test stage", showingLatch.await(15, TimeUnit.SECONDS));
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
Util.runAndWait(() -> {
stage.get().close();
});
checker.assertCollectable(stage.get());
});
}
@AfterClass
public static void teardownOnce() {
Platform.runLater(() -> {
Platform.exit();
});
}
}
