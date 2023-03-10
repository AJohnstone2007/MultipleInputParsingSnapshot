package test.javafx.animation;
import com.sun.javafx.animation.KeyValueHelper;
import com.sun.javafx.animation.KeyValueType;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import static org.junit.Assert.assertEquals;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableValue;
import org.junit.Test;
public class KeyValueTest {
private static final double EPSILON_DOUBLE = 1e-12;
private static final float EPSILON_FLOAT = 1e-6f;
private void assertKeyValue(KeyValueType type, WritableValue<?> target, Object endValue, Interpolator interpolator, KeyValue kv) {
assertEquals(type, KeyValueHelper.getType(kv));
assertEquals(target, kv.getTarget());
assertEquals(endValue, kv.getEndValue());
assertEquals(interpolator, kv.getInterpolator());
}
private void assertKeyValue(KeyValueType type, WritableValue<?> target, Interpolator interpolator, KeyValue kv) {
assertEquals(type, KeyValueHelper.getType(kv));
assertEquals(target, kv.getTarget());
assertEquals(interpolator, kv.getInterpolator());
}
@Test
public void testBooleanFactory_Interpolator() {
final BooleanProperty v = new SimpleBooleanProperty();
final KeyValue kv = new KeyValue(v, true, Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.BOOLEAN, v, Boolean.TRUE, Interpolator.EASE_BOTH, kv);
}
@Test(expected=NullPointerException.class)
public void testBooleanFactory_Interpolator_NullTarget() {
new KeyValue(null, true, Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testBooleanFactory_Interpolator_NullInterpolator() {
final BooleanProperty v = new SimpleBooleanProperty();
new KeyValue(v, true, null);
}
@Test
public void testBooleanFactory() {
final BooleanProperty v = new SimpleBooleanProperty();
final KeyValue kv = new KeyValue(v, true);
assertKeyValue(KeyValueType.BOOLEAN, v, Boolean.TRUE, Interpolator.LINEAR, kv);
}
@Test(expected=NullPointerException.class)
public void testBooleanFactory_NullTarget() {
new KeyValue(null, true);
}
@Test
public void testDoubleFactory_Interpolator() {
final DoubleProperty v = new SimpleDoubleProperty();
final KeyValue kv = new KeyValue(v, Math.PI, Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.DOUBLE, v, Interpolator.EASE_BOTH, kv);
assertEquals(Math.PI, ((Number)kv.getEndValue()).doubleValue(), EPSILON_DOUBLE);
}
@Test(expected=NullPointerException.class)
public void testDoubleFactory_Interpolator_NullTarget() {
new KeyValue(null, Math.PI, Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testDoubleFactory_Interpolator_NullInterpolator() {
final DoubleProperty v = new SimpleDoubleProperty();
new KeyValue(v, Math.PI, null);
}
@Test
public void testDoubleFactory() {
final DoubleProperty v = new SimpleDoubleProperty();
final KeyValue kv = new KeyValue(v, Math.E);
assertKeyValue(KeyValueType.DOUBLE, v, Interpolator.LINEAR, kv);
assertEquals(Math.E, ((Number)kv.getEndValue()).doubleValue(), EPSILON_DOUBLE);
}
@Test(expected=NullPointerException.class)
public void testDoubleFactory_NullTarget() {
new KeyValue(null, Math.E);
}
@Test
public void testFloatFactory_Interpolator() {
final FloatProperty v = new SimpleFloatProperty();
final KeyValue kv = new KeyValue(v, (float)Math.E, Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.FLOAT, v, Interpolator.EASE_BOTH, kv);
assertEquals((float)Math.E, ((Number)kv.getEndValue()).floatValue(), EPSILON_FLOAT);
}
@Test(expected=NullPointerException.class)
public void testFloatFactory_Interpolator_NullTarget() {
new KeyValue((WritableFloatValue)null, (float)Math.E, Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testFloatFactory_Interpolator_NullInterpolator() {
final FloatProperty v = new SimpleFloatProperty();
new KeyValue(v, (float)Math.E, null);
}
@Test
public void testFloatFactory() {
final FloatProperty v = new SimpleFloatProperty();
final KeyValue kv = new KeyValue(v, (float)Math.PI);
assertKeyValue(KeyValueType.FLOAT, v, Interpolator.LINEAR, kv);
assertEquals((float)Math.PI, ((Number)kv.getEndValue()).floatValue(), EPSILON_FLOAT);
}
@Test(expected=NullPointerException.class)
public void testFloatFactory_NullTarget() {
new KeyValue((WritableFloatValue)null, (float)Math.PI);
}
@Test
public void testIntegerFactory_Interpolator() {
final IntegerProperty v = new SimpleIntegerProperty();
final KeyValue kv = new KeyValue(v, Integer.MAX_VALUE, Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.INTEGER, v, Integer.MAX_VALUE, Interpolator.EASE_BOTH, kv);
}
@Test(expected=NullPointerException.class)
public void testIntegerFactory_Interpolator_NullTarget() {
new KeyValue((WritableIntegerValue)null, 1, Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testIntegerFactory_Interpolator_NullInterpolator() {
final IntegerProperty v = new SimpleIntegerProperty();
new KeyValue(v, 1, null);
}
@Test
public void testIntegerFactory() {
final IntegerProperty v = new SimpleIntegerProperty();
final KeyValue kv = new KeyValue(v, Integer.MIN_VALUE);
assertKeyValue(KeyValueType.INTEGER, v, Integer.MIN_VALUE, Interpolator.LINEAR, kv);
}
@Test(expected=NullPointerException.class)
public void testIntegerFactory_NullTarget() {
new KeyValue((WritableIntegerValue)null, Integer.MIN_VALUE);
}
@Test
public void testLongFactory_Interpolator() {
final LongProperty v = new SimpleLongProperty();
final KeyValue kv = new KeyValue(v, Long.MAX_VALUE, Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.LONG, v, Long.MAX_VALUE, Interpolator.EASE_BOTH, kv);
}
@Test(expected=NullPointerException.class)
public void testLongFactory_Interpolator_NullTarget() {
new KeyValue((WritableLongValue)null, 1L, Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testLongFactory_Interpolator_NullInterpolator() {
final LongProperty v = new SimpleLongProperty();
new KeyValue(v, 1L, null);
}
@Test
public void testLongFactory() {
final LongProperty v = new SimpleLongProperty();
final KeyValue kv = new KeyValue(v, Long.MIN_VALUE);
assertKeyValue(KeyValueType.LONG, v, Long.MIN_VALUE, Interpolator.LINEAR, kv);
}
@Test(expected=NullPointerException.class)
public void testLongFactory_NullTarget() {
new KeyValue((WritableLongValue)null, Long.MIN_VALUE);
}
@Test
public void testObjectFactory_Interpolator() {
final StringProperty v = new SimpleStringProperty();
final KeyValue kv = new KeyValue(v, "Hello World", Interpolator.EASE_BOTH);
assertKeyValue(KeyValueType.OBJECT, v, "Hello World", Interpolator.EASE_BOTH, kv);
}
@Test(expected=NullPointerException.class)
public void testObjectFactory_Interpolator_NullTarget() {
new KeyValue(null, "Hello World", Interpolator.EASE_BOTH);
}
@Test(expected=NullPointerException.class)
public void testObjectFactory_Interpolator_NullInterpolator() {
final StringProperty v = new SimpleStringProperty();
new KeyValue(v, "Hello World", null);
}
@Test
public void testObjectFactory() {
final StringProperty v = new SimpleStringProperty();
final KeyValue kv = new KeyValue(v, "Goodbye World");
assertKeyValue(KeyValueType.OBJECT, v, "Goodbye World", Interpolator.LINEAR, kv);
}
@Test(expected=NullPointerException.class)
public void testObjectFactory_NullTarget() {
new KeyValue(null, "Goodbye World");
}
}
