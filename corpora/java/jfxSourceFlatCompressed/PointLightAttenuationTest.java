package test.javafx.scene.lighting3D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import test.util.Util;
public class PointLightAttenuationTest extends LightingTest {
private static final int[] LAMBERT_SAMPLE_DISTS = new int[] {0, 30, 60};
private static final int ATTN_SAMPLE_DIST = LIGHT_DIST;
private static final PointLight LIGHT = new PointLight(Color.BLUE);
public static void main(String[] args) throws Exception {
initFX();
}
@BeforeClass
public static void initFX() throws Exception {
startupLatch = new CountDownLatch(1);
LightingTest.light = LIGHT;
new Thread(() -> Application.launch(TestApp.class, (String[]) null)).start();
assertTrue("Timeout waiting for FX runtime to start", startupLatch.await(15, TimeUnit.SECONDS));
}
@Before
public void setupEach() {
assumeTrue(Platform.isSupported(ConditionalFeature.SCENE3D));
LIGHT.setLinearAttenuation(0);
LIGHT.setQuadraticAttenuation(0);
LIGHT.setMaxRange(Double.POSITIVE_INFINITY);
}
@Test
public void testLambert() {
Util.runAndWait(() -> {
var snapshot = snapshot();
for (int x : LAMBERT_SAMPLE_DISTS) {
double sampledBlue = snapshot.getPixelReader().getColor(x, 0).getBlue();
assertEquals(FAIL_MESSAGE + " for " + x, calculateLambertTerm(x), sampledBlue, DELTA);
}
});
}
@Test
public void testAttenuation() {
Util.runAndWait(() -> {
double diagDist = Math.sqrt(LIGHT_DIST * LIGHT_DIST + ATTN_SAMPLE_DIST * ATTN_SAMPLE_DIST);
double lambertCenter = calculateLambertTerm(0);
double lambertSample = calculateLambertTerm(ATTN_SAMPLE_DIST);
LIGHT.setLinearAttenuation(0.01);
doAttenuationTest(diagDist, lambertCenter, lambertSample);
LIGHT.setLinearAttenuation(0);
LIGHT.setQuadraticAttenuation(0.01);
doAttenuationTest(diagDist, lambertCenter, lambertSample);
});
}
private void doAttenuationTest(double diagDist, double lambertCenter, double lambertSample) {
var snapshot = snapshot();
var attn = calculateAttenuationFactor(LIGHT_DIST);
var sampledBlue = snapshot.getPixelReader().getColor(0, 0).getBlue();
assertEquals(FAIL_MESSAGE, lambertCenter * attn, sampledBlue, DELTA);
attn = calculateAttenuationFactor(diagDist);
sampledBlue = snapshot.getPixelReader().getColor(ATTN_SAMPLE_DIST, 0).getBlue();
assertEquals(FAIL_MESSAGE, lambertSample * attn, sampledBlue, DELTA);
}
@Test
public void testRange() {
Util.runAndWait(() -> {
double diagDist = Math.sqrt(LIGHT_DIST * LIGHT_DIST + ATTN_SAMPLE_DIST * ATTN_SAMPLE_DIST);
LIGHT.setMaxRange((LIGHT_DIST + diagDist) / 2);
var snapshot = snapshot();
double sampledBlue = snapshot.getPixelReader().getColor(0, 0).getBlue();
assertEquals(FAIL_MESSAGE + ", should be in range", 1, sampledBlue, DELTA);
sampledBlue = snapshot.getPixelReader().getColor(ATTN_SAMPLE_DIST, 0).getBlue();
assertEquals(FAIL_MESSAGE + ", should be out of range", 0, sampledBlue, DELTA);
});
}
private double calculateAttenuationFactor(double dist) {
return 1 / (LIGHT.getConstantAttenuation() + LIGHT.getLinearAttenuation() * dist
+ LIGHT.getQuadraticAttenuation() * dist * dist);
}
}
