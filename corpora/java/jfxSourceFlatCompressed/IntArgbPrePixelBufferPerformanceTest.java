import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
public class IntArgbPrePixelBufferPerformanceTest extends Application {
private static final int SCENE_WIDTH = 1000;
private static final int SCENE_HEIGHT = 1000;
private static final int IMAGE_WIDTH = SCENE_WIDTH;
private static final int IMAGE_HEIGHT = SCENE_HEIGHT;
private static final int COPY_BUFFER_WIDTH = IMAGE_WIDTH;
private static final int COPY_BUFFER_HEIGHT = IMAGE_HEIGHT;
private PixelBuffer<IntBuffer> pixelBuffer;
private IntBuffer intBuffer;
private ArrayList<Color> colors = new ArrayList<>();
private int count = 0;
private List<IntBuffer> copyBuffers = new ArrayList<>();
private VBox wImgContainer = new VBox(8);
private VBox pbImgContainer = new VBox(8);
private IntBuffer createBuffer(int w, int h, Color c) {
IntBuffer bf = IntBuffer.allocate(w * h);
int red = (int) Math.round(c.getRed() * 255.0);
int green = (int) Math.round(c.getGreen() * 255.0);
int blue = (int) Math.round(c.getBlue() * 255.0);
int alpha = (int) Math.round(c.getOpacity() * 255.0);
int color = alpha << 24 | red << 16 | green << 8 | blue;
for (int y = 0; y < h; y++) {
for (int x = 0; x < w; x++) {
bf.put(color);
}
}
bf.rewind();
return bf;
}
private void createCopyBuffers() {
for (Color clr : colors) {
IntBuffer buf = createBuffer(COPY_BUFFER_WIDTH, COPY_BUFFER_HEIGHT, clr);
copyBuffers.add(buf);
}
}
private void updateIntBuffer(IntBuffer buf) {
IntBuffer src = copyBuffers.get(count++);
buf.put(src);
buf.rewind();
src.rewind();
if (count >= copyBuffers.size()) {
count = 0;
}
}
private WritableImage createWImageFromBuffer(int w, int h, Color c) {
intBuffer = createBuffer(w, h, c);
WritableImage img = new WritableImage(w, h);
PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
PixelWriter pw = img.getPixelWriter();
pw.setPixels(0, 0, w, h, pf, intBuffer, w);
return img;
}
private WritableImage createWImageFromPixelBuffer(int w, int h, Color c) {
IntBuffer intBuffer = createBuffer(w, h, c);
PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
pixelBuffer = new PixelBuffer<>(w, h, intBuffer, pf);
return new WritableImage(pixelBuffer);
}
private void loadWritableImage() {
final Image bImage = createWImageFromBuffer(IMAGE_WIDTH, IMAGE_HEIGHT, Color.BLUE);
ImageView bIv = new ImageView(bImage);
final TextField tf = new TextField("100");
final Label clr = new Label("Color");
Button updateWritableImage = new Button("Update WritableImage");
updateWritableImage.setOnAction(e -> {
int numIter = Integer.parseInt(tf.getText());
double t1 = System.nanoTime();
for (int i = 0; i < numIter; i++) {
updateIntBuffer(intBuffer);
PixelWriter pw = ((WritableImage) bImage).getPixelWriter();
PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
pw.setPixels(0, 0, COPY_BUFFER_WIDTH, COPY_BUFFER_HEIGHT, pf, intBuffer, COPY_BUFFER_WIDTH);
}
double t2 = System.nanoTime();
double t3 = t2 - t1;
clr.setText(colors.get(count).toString());
System.out.println("WI: AVERAGE time to update the Image: [" + t3 / (long) numIter + "]nano sec,  [" + (t3 / 1000000.0) / (double) numIter + "]ms");
});
wImgContainer.getChildren().addAll(updateWritableImage, tf, clr, bIv);
}
private void loadPBImage() {
Image pbImage = createWImageFromPixelBuffer(IMAGE_WIDTH, IMAGE_HEIGHT, Color.BLUE);
ImageView pbIv = new ImageView(pbImage);
final TextField tf = new TextField("100");
final Label clr = new Label("Color");
Button updatePixelBuffer = new Button("Update PixelBuffer");
updatePixelBuffer.setOnAction(e -> {
int numIter = Integer.parseInt(tf.getText());
double t1 = System.nanoTime();
for (int i = 0; i < numIter; i++) {
pixelBuffer.updateBuffer(pixBuf -> {
updateIntBuffer(pixBuf.getBuffer());
return new Rectangle2D(0, 0, COPY_BUFFER_WIDTH, COPY_BUFFER_HEIGHT);
});
}
double t2 = System.nanoTime();
double t3 = t2 - t1;
clr.setText(colors.get(count).toString());
System.out.println("PB: AVERAGE time to update the Image: [" + t3 / (long) numIter + "]nano sec,  [" + (t3 / 1000000.0) / (double) numIter + "]ms");
});
pbImgContainer.getChildren().addAll(updatePixelBuffer, tf, clr, pbIv);
}
@Override
public void start(Stage pbImageStage) {
colors.add(Color.GREEN);
colors.add(Color.BLUE);
colors.add(Color.RED);
createCopyBuffers();
VBox pbRoot = new VBox(12);
Scene pbScene = new Scene(pbRoot, SCENE_WIDTH, SCENE_HEIGHT);
loadPBImage();
pbRoot.getChildren().add(pbImgContainer);
pbImageStage.setScene(pbScene);
pbImageStage.setTitle("PixelBuffer");
pbImageStage.setX(10);
pbImageStage.setY(10);
pbImageStage.show();
VBox wImRoot = new VBox(12);
Scene wImScene = new Scene(wImRoot, SCENE_WIDTH, SCENE_HEIGHT);
loadWritableImage();
wImRoot.getChildren().add(wImgContainer);
Stage wImStage = new Stage();
wImStage.setScene(wImScene);
wImStage.setTitle("WritableImage-PixelWriter");
wImStage.setX(SCENE_WIDTH + 50);
wImStage.setY(10);
wImStage.show();
}
public static void main(String[] args) {
Application.launch(args);
}
}
