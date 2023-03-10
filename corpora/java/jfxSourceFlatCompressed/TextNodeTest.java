package test.javafx.scene.text;
import com.sun.javafx.application.PlatformImpl;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
public class TextNodeTest {
@BeforeClass
public static void initFX() {
final CountDownLatch startupLatch = new CountDownLatch(1);
PlatformImpl.startup(() -> {
startupLatch.countDown();
});
try {
if (!startupLatch.await(5, TimeUnit.SECONDS)) {
fail("Timeout waiting for FX runtime to start");
}
} catch (InterruptedException ex) {
fail("Unexpected exception: " + ex);
}
}
@Test public void testBounds() {
Text text = new Text("a");
Bounds bounds = text.getLayoutBounds();
text.setText("");
Bounds bounds2 = text.getLayoutBounds();
assertEquals(bounds.getHeight(), bounds2.getHeight(), 0.00001);
}
public void assertBoundsEquals(PathElement[] boundsShape,
double x, double y,
double w, double h) {
assertNotNull(boundsShape);
assertEquals(5, boundsShape.length);
assertNotNull(boundsShape[0]);
assertNotNull(boundsShape[1]);
assertNotNull(boundsShape[2]);
assertNotNull(boundsShape[3]);
assertNotNull(boundsShape[4]);
assertEquals(boundsShape[0].getClass(), MoveTo.class);
assertEquals(boundsShape[1].getClass(), LineTo.class);
assertEquals(boundsShape[2].getClass(), LineTo.class);
assertEquals(boundsShape[3].getClass(), LineTo.class);
assertEquals(boundsShape[4].getClass(), LineTo.class);
MoveTo m = (MoveTo)boundsShape[0];
LineTo l0 = (LineTo)boundsShape[1];
LineTo l1 = (LineTo)boundsShape[2];
LineTo l2 = (LineTo)boundsShape[3];
LineTo l3 = (LineTo)boundsShape[4];
double e = 0.00001;
assertEquals(m.getX(), x, e);
assertEquals(m.getY(), y, e);
assertEquals(l0.getX(), x+w, e);
assertEquals(l0.getY(), y, e);
assertEquals(l1.getX(), x+w, e);
assertEquals(l1.getY(), y+h, e);
assertEquals(l2.getX(), x, e);
assertEquals(l2.getY(), y+h, e);
assertEquals(l3.getX(), x, e);
assertEquals(l3.getY(), y, e);
}
public void assertCaretEquals(PathElement[] caretShape,
double x0, double y0,
double x1, double y1) {
assertNotNull(caretShape);
assertEquals(2, caretShape.length);
assertNotNull(caretShape[0]);
assertNotNull(caretShape[1]);
assertEquals(caretShape[0].getClass(), MoveTo.class);
assertEquals(caretShape[1].getClass(), LineTo.class);
MoveTo m = (MoveTo)caretShape[0];
LineTo l = (LineTo)caretShape[1];
double e = 0.00001;
assertEquals(m.getX(), x0, e);
assertEquals(m.getY(), y0, e);
assertEquals(l.getX(), x1, e);
assertEquals(l.getY(), y1, e);
}
@SuppressWarnings("deprecation")
@Test public void testCaretShape() {
Font font = new Font("Monospaced Regular", 16);
Text text = new Text("a");
text.setFont(font);
text.setTextOrigin(VPos.TOP);
Bounds bounds = text.getLayoutBounds();
float lineHeight = (float)bounds.getHeight();
float avgChar = (float)bounds.getWidth();
PathElement[] empty = {};
assertEquals(empty, text.getCaretShape());
text.setCaretPosition(0);
assertCaretEquals(text.getCaretShape(), 0, 0, 0, lineHeight);
text.setCaretPosition(-1);
assertEquals(empty, text.getCaretShape());
text.setCaretPosition(0);
assertCaretEquals(text.getCaretShape(), 0, 0, 0, lineHeight);
text.setCaretBias(false);
text.setText("abc");
assertEquals(empty, text.getCaretShape());
assertEquals(-1, text.getCaretPosition());
assertEquals(true, text.caretBiasProperty().get());
text.setCaretPosition(0);
text.setCaretBias(true);
assertCaretEquals(text.getCaretShape(), 0, 0, 0, lineHeight);
text.setCaretPosition(0);
text.setCaretBias(false);
assertCaretEquals(text.getCaretShape(), avgChar, 0, avgChar, lineHeight);
text.setCaretPosition(1);
text.setCaretBias(true);
assertCaretEquals(text.getCaretShape(), avgChar, 0, avgChar, lineHeight);
text.setCaretPosition(1);
text.setCaretBias(false);
assertCaretEquals(text.getCaretShape(), avgChar*2, 0, avgChar*2, lineHeight);
text.setCaretPosition(2);
text.setCaretBias(true);
assertCaretEquals(text.getCaretShape(), avgChar*2, 0, avgChar*2, lineHeight);
text.setCaretPosition(2);
text.setCaretBias(false);
assertCaretEquals(text.getCaretShape(), avgChar*3, 0, avgChar*3, lineHeight);
text.setCaretPosition(3);
text.setCaretBias(true);
assertCaretEquals(text.getCaretShape(), avgChar*3, 0, avgChar*3, lineHeight);
text.setCaretPosition(3);
text.setCaretBias(false);
assertCaretEquals(text.getCaretShape(), avgChar*3, 0, avgChar*3, lineHeight);
text.setCaretPosition(4);
text.setCaretBias(true);
assertEquals(empty, text.getCaretShape());
text.setCaretPosition(4);
text.setCaretBias(false);
assertEquals(empty, text.getCaretShape());
text.setText("");
text.setCaretPosition(0);
text.setCaretBias(true);
assertCaretEquals(text.getCaretShape(), 0, 0, 0, lineHeight);
}
@SuppressWarnings("deprecation")
@Test public void testSelectionShape() {
Font font = new Font("Monospaced Regular", 16);
Text text = new Text("a");
text.setFont(font);
text.setTextOrigin(VPos.TOP);
Bounds bounds = text.getLayoutBounds();
float lineHeight = (float)bounds.getHeight();
float avgChar = (float)bounds.getWidth();
PathElement[] empty = {};
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(-1);
assertEquals(empty, text.getSelectionShape());;
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionEnd(-1);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(1);
text.setSelectionEnd(0);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(0);
text.setSelectionEnd(0);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(0);
text.setSelectionEnd(3);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(3);
text.setSelectionEnd(5);
assertEquals(empty, text.getSelectionShape());
text.setText("abc");
assertEquals(empty, text.getSelectionShape());
assertEquals(-1, text.getSelectionStart());
assertEquals(-1, text.getSelectionEnd());
text.setSelectionStart(0);
text.setSelectionEnd(0);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(0);
text.setSelectionEnd(1);
assertBoundsEquals(text.getSelectionShape(), 0, 0, avgChar, lineHeight);
text.setSelectionStart(0);
text.setSelectionEnd(2);
assertBoundsEquals(text.getSelectionShape(), 0, 0, 2*avgChar, lineHeight);
text.setSelectionStart(0);
text.setSelectionEnd(3);
assertBoundsEquals(text.getSelectionShape(), 0, 0, 3*avgChar, lineHeight);
text.setSelectionStart(0);
text.setSelectionEnd(4);
assertEquals(empty, text.getSelectionShape());
text.setSelectionStart(1);
text.setSelectionEnd(2);
assertBoundsEquals(text.getSelectionShape(), avgChar, 0, avgChar, lineHeight);
text.setSelectionStart(1);
text.setSelectionEnd(3);
assertBoundsEquals(text.getSelectionShape(), avgChar, 0, 2*avgChar, lineHeight);
text.setSelectionStart(2);
text.setSelectionEnd(3);
assertBoundsEquals(text.getSelectionShape(), 2*avgChar, 0, avgChar, lineHeight);
text.setSelectionStart(3);
text.setSelectionEnd(3);
assertEquals(empty, text.getSelectionShape());
}
}
