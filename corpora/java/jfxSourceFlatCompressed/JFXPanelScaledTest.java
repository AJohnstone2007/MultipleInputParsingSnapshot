package test.robot.javafx.embed.swing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static test.util.Util.TIMEOUT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import com.sun.javafx.PlatformUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.JFXPanelShim;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import junit.framework.AssertionFailedError;
public class JFXPanelScaledTest {
static CountDownLatch launchLatch;
private static MyApp myApp;
private static Timer t;
static int cnt;
final static int TARGET_BASE_SIZE = 101;
final static int TARGET_SCALED_SIZE = (int) Math.ceil(TARGET_BASE_SIZE *1.25);
@BeforeClass
public static void setupOnce() throws Exception {
assumeTrue(PlatformUtil.isWindows());
System.setProperty("sun.java2d.uiScale.enabled", "true");
System.setProperty("sun.java2d.uiScale", "125%");
System.setProperty("glass.win.uiScale", "1.25");
System.setProperty("glass.gtk.uiScale", "1.25");
launchLatch = new CountDownLatch(1);
SwingUtilities.invokeLater(() -> {
myApp = new MyApp();
});
assertTrue("Timeout waiting for Application to launch",
launchLatch.await(5 * TIMEOUT, TimeUnit.MILLISECONDS));
}
@AfterClass
public static void teardownOnce() {
if (myApp != null) {
SwingUtilities.invokeLater(myApp::dispose);
}
}
@Test
public void testScale() throws Exception {
BufferedImage pixelsIm = JFXPanelShim.getPixelsIm(myApp.jfxPanel);
assertEquals(TARGET_SCALED_SIZE, pixelsIm.getWidth());
assertEquals(TARGET_SCALED_SIZE, pixelsIm.getHeight());
Color c = new Color(0, 0, 0);
int colorOfDiagonal = c.getRGB();
for (int x = 10; x < 45; x++) {
for (int y = 90; y < 115; y++) {
if (colorOfDiagonal == pixelsIm.getRGB(x, y)) {
fail("image is skewed");
}
}
}
}
public static class MyApp extends JFrame {
private static final long serialVersionUID = 1L;
private final JFXPanel jfxPanel;
public MyApp() {
super("JFXPanel Scaling");
jfxPanel = new JFXPanel();
setLayout(null);
jfxPanel.setSize(new Dimension(100, 100));
add(jfxPanel);
setSize(500, 500);
setVisible(true);
Platform.runLater(() -> initFX(jfxPanel));
cnt = 0;
t = new Timer(500, (e) -> {
switch (cnt) {
case 0:
jfxPanel.setSize(new Dimension(201, 201));
break;
case 1:
jfxPanel.setSize(new Dimension(TARGET_BASE_SIZE, TARGET_BASE_SIZE));
break;
case 2:
t.stop();
launchLatch.countDown();
break;
}
cnt++;
});
t.start();
}
private static void initFX(JFXPanel fxPanel) {
Region region = new Region();
region.setStyle("-fx-background-color: #FFFFFF;" + "-fx-border-color: #000000;"
+ "-fx-border-width: 0 5px 0 0;" + "-fx-border-style: solid");
Scene scene = new Scene(region);
fxPanel.setScene(scene);
}
}
}
