package test.robot.com.sun.glass.ui.monocle;
import com.sun.glass.ui.monocle.TestLogShim;
import test.robot.com.sun.glass.ui.monocle.TestApplication;
import test.robot.com.sun.glass.ui.monocle.input.devices.TestTouchDevice;
import test.robot.com.sun.glass.ui.monocle.input.devices.TestTouchDevices;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.TouchEvent;
import org.junit.*;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class SingleTouchNonFullScreenTest extends ParameterizedTestBase {
private static final TestCase[] TEST_CASES = {
new TestCase(200, 100, 400, 300, 200, 100, 599, 399),
new TestCase(100, 200, 400, 300, 100, 200, 499, 499),
};
private TestCase testCase;
static class TestCase {
Rectangle2D stageBounds;
int x1, y1, x2, y2;
TestCase(double winX, double winY, double width,double height, int x1, int y1, int x2, int y2) {
this.stageBounds = new Rectangle2D(winX, winY, width, height);
this.x1 = x1;
this.y1 = y1;
this.x2 = x2;
this.y2 = y2;
}
public String toString() {
return "TestCase[stage bounds=("
+ stageBounds.getMinX()
+ "," + stageBounds.getMinY()
+ "," + stageBounds.getWidth()
+ "," + stageBounds.getHeight() + ")"
+ ", x1=" + x1
+ ", y1=" + y1
+ ", x2=" + x2
+ ", y2=" + y2 + "]";
}
}
public SingleTouchNonFullScreenTest(TestTouchDevice device, TestCase testCase)
{
super(device, testCase.stageBounds);
this.testCase = testCase;
TestLogShim.format("Starting test with %s, %s", device, testCase);
}
@Parameterized.Parameters
public static Collection<Object[]> data() {
List<Object[]> params = new ArrayList<>();
List<TestTouchDevice> devices = TestTouchDevices.getTouchDevices();
for (TestTouchDevice device : devices) {
for (TestCase testCase : TEST_CASES) {
params.add(new Object[]{device, testCase});
}
}
return params;
}
@Before
public void addListener() throws Exception {
TestApplication.getStage().getScene().addEventHandler(
TouchEvent.TOUCH_PRESSED,
e -> TestLogShim.format("Touch pressed [relative]: %.0f, %.0f",
e.getTouchPoint().getX(),
e.getTouchPoint().getY())
);
TestApplication.getStage().getScene().addEventHandler(
TouchEvent.TOUCH_RELEASED,
e -> TestLogShim.format("Touch released [relative]: %.0f, %.0f",
e.getTouchPoint().getX(),
e.getTouchPoint().getY()));
TestApplication.getStage().getScene().addEventHandler(
TouchEvent.TOUCH_MOVED,
e -> TestLogShim.format("Touch moved [relative]: %.0f, %.0f",
e.getTouchPoint().getX(),
e.getTouchPoint().getY()));
}
@Test
public void tap() throws Exception {
final int x1 = testCase.x1;
final int y1 = testCase.y1;
final int relX1 = x1 - (int) stageBounds.getMinX();
final int relY1 = y1 - (int) stageBounds.getMinY();
int p = device.addPoint(x1, y1);
device.sync();
device.removePoint(p);
device.sync();
TestLogShim.waitForLog("Mouse pressed: %d, %d", x1, y1);
TestLogShim.waitForLog("Mouse released: %d, %d", x1, y1);
TestLogShim.waitForLog("Mouse clicked: %d, %d", x1, y1);
TestLogShim.waitForLog("Touch pressed [relative]: %d, %d", relX1, relY1);
TestLogShim.waitForLog("Touch pressed: %d, %d", x1, y1);
TestLogShim.waitForLog("Touch released [relative]: %d, %d", relX1, relY1);
TestLogShim.waitForLog("Touch released: %d, %d", x1, y1);
Assert.assertEquals("Expected only one touch point", 0,
TestLogShim.getLog().stream()
.filter(s -> s.startsWith("Touch points count"))
.filter(s -> !s.startsWith("Touch points count: [1]")).count());
}
@Test
public void tapAndDrag() throws Exception {
final int x1 = testCase.x1;
final int y1 = testCase.y1;
final int x2 = testCase.x2;
final int y2 = testCase.y2;
final int relX1 = x1 - (int) stageBounds.getMinX();
final int relY1 = y1 - (int) stageBounds.getMinY();
final int relX2 = x2 - (int) stageBounds.getMinX();
final int relY2 = y2 - (int) stageBounds.getMinY();
int p = device.addPoint(x1, y1);
device.sync();
device.setPoint(p, x2, y2);
device.sync();
device.removePoint(p);
device.sync();
TestLogShim.waitForLog("Mouse pressed: %d, %d", x1, y1);
TestLogShim.waitForLog("Mouse dragged: %d, %d", x2, y2);
TestLogShim.waitForLog("Mouse released: %d, %d", x2, y2);
TestLogShim.waitForLog("Mouse clicked: %d, %d", x2, y2);
TestLogShim.waitForLog("Touch pressed [relative]: %d, %d", relX1, relY1);
TestLogShim.waitForLog("Touch pressed: %d, %d", x1, y1);
TestLogShim.waitForLog("Touch moved [relative]: %d, %d", relX2, relY2);
TestLogShim.waitForLog("Touch moved: %d, %d", x2, y2);
TestLogShim.waitForLog("Touch released [relative]: %d, %d", relX2, relY2);
TestLogShim.waitForLog("Touch released: %d, %d", x2, y2);
Assert.assertEquals("Expected only one touch point", 0,
TestLogShim.getLog().stream()
.filter(s -> s.startsWith("Touch points count"))
.filter(s -> !s.startsWith("Touch points count: [1]")).count());
}
}
