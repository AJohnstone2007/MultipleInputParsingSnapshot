package test.robot.painttest;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.Test;
import test.robot.testharness.VisualTestBase;
public class ImagePaintTest extends VisualTestBase {
private Stage testStage;
private Scene testScene;
private static final double TOLERANCE = 0.07;
private Image createImage(int w, int h, Color c1, Color c2) {
WritableImage img = new WritableImage(w, h);
PixelWriter pw = img.getPixelWriter();
for (int y = 0; y < h; y++) {
for (int x = 0; x < w; x++) {
Color c = x < w/2 ? c1 : c2;
pw.setColor(x, y, c);
}
}
return img;
}
private Rectangle createRect(Image image, int anchorX, int anchorY, int x, int y) {
Rectangle rect = new Rectangle(x, y, image.getWidth(), image.getHeight());
ImagePattern pattern = new ImagePattern(image, anchorX, anchorY,
image.getWidth(), image.getHeight(), false);
rect.setFill(pattern);
return rect;
}
public void doTestImagePaint(final boolean useSeparateImage) {
final int WIDTH = 400;
final int HEIGHT = 300;
final int IMAGE_WIDTH = 50;
final int IMAGE_HEIGHT = 20;
final Color COLOR1 = Color.RED;
final Color COLOR2 = Color.GREEN;
runAndWait(() -> {
Image image1;
Image image2;
image1 = createImage(IMAGE_WIDTH, IMAGE_HEIGHT, COLOR1, COLOR2);
if (useSeparateImage) {
image2 = createImage(IMAGE_WIDTH, IMAGE_HEIGHT, COLOR1, COLOR2);
} else {
image2 = image1;
}
Rectangle rect1 = createRect(image1, 0, 0, 0, 0);
Rectangle rect2 = createRect(image2, IMAGE_WIDTH / 2, 0, 0, IMAGE_HEIGHT);
Group root = new Group(rect1, rect2);
testScene = new Scene(root, WIDTH, HEIGHT);
testScene.setFill(Color.WHITE);
testStage = getStage();
testStage.setScene(testScene);
testStage.show();
});
waitFirstFrame();
runAndWait(() -> {
Color color;
color = getColor(testScene, 1, 1);
assertColorEquals(COLOR1, color, TOLERANCE);
color = getColor(testScene, 1 + IMAGE_WIDTH / 2, 1);
assertColorEquals(COLOR2, color, TOLERANCE);
color = getColor(testScene, 1, 1 + IMAGE_HEIGHT);
assertColorEquals(COLOR2, color, TOLERANCE);
color = getColor(testScene, 1 + IMAGE_WIDTH / 2, 1 + IMAGE_HEIGHT);
assertColorEquals(COLOR1, color, TOLERANCE);
});
}
@Test(timeout = 15000)
public void testSeparateImagePaint() {
doTestImagePaint(true);
}
@Test(timeout = 15000)
public void testSameImagePaint() {
doTestImagePaint(false);
}
}
