package test.javafx.binding;
import static org.junit.Assert.*;
import javafx.beans.InvalidationListener;
import test.javafx.beans.InvalidationListenerMock;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import org.junit.Before;
import org.junit.Test;
public class BindingsBooleanTest {
private BooleanProperty op1;
private BooleanProperty op2;
private InvalidationListenerMock observer;
@Before
public void setUp() {
op1 = new SimpleBooleanProperty(true);
op2 = new SimpleBooleanProperty(false);
observer = new InvalidationListenerMock();
}
@SuppressWarnings("unused")
@Test
public void testAnd() {
final BooleanBinding binding = Bindings.and(op1, op2);
binding.addListener(observer);
assertEquals(true && false, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1, op2);
observer.reset();
op1.set(false);
assertEquals(false && false, binding.get());
observer.check(binding, 1);
op1.set(true);
binding.get();
observer.reset();
op2.set(true);
assertEquals(true && true, binding.get());
observer.check(binding, 1);
op1.set(false);
assertEquals(false && true, binding.get());
observer.check(binding, 1);
}
@Test
public void testAnd_Efficiency() {
final BooleanBinding binding = Bindings.and(op1, op2);
binding.addListener(observer);
binding.get();
op1.set(false);
op2.set(true);
observer.check(binding, 1);
op2.set(false);
observer.check(null, 0);
}
@SuppressWarnings("unused")
@Test
public void testAnd_Self() {
final BooleanBinding binding = Bindings.and(op1, op1);
binding.addListener(observer);
assertEquals(true && true, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1);
observer.reset();
op1.set(false);
assertEquals(false && false, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true && true, binding.get());
observer.check(binding, 1);
}
@Test
public void testAnd_WeakReference() {
final ObservableBooleanValueMock op1 = new ObservableBooleanValueMock();
final ObservableBooleanValueMock op2 = new ObservableBooleanValueMock();
BooleanBinding binding = Bindings.and(op1, op2);
assertNotNull(op1.listener);
assertNotNull(op2.listener);
binding = null;
System.gc();
op1.fireInvalidationEvent();
assertNull(op1.listener);
assertNotNull(op2.listener);
op2.fireInvalidationEvent();
assertNull(op1.listener);
assertNull(op2.listener);
}
@Test(expected=NullPointerException.class)
public void testAnd_null_x() {
Bindings.and(null, op1);
}
@Test(expected=NullPointerException.class)
public void testAnd_x_null() {
Bindings.and(op1, null);
}
@SuppressWarnings("unused")
@Test
public void testOr() {
final BooleanBinding binding = Bindings.or(op1, op2);
binding.addListener(observer);
assertEquals(true || false, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1, op2);
observer.reset();
op1.set(false);
assertEquals(false || false, binding.get());
observer.check(binding, 1);
op2.set(true);
assertEquals(false || true, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true || true, binding.get());
observer.check(binding, 1);
}
@Test
public void testOr_Efficiency() {
final BooleanBinding binding = Bindings.or(op1, op2);
binding.addListener(observer);
binding.get();
op1.set(false);
op2.set(true);
observer.check(binding, 1);
op1.set(true);
binding.get();
observer.reset();
op2.set(false);
observer.check(null, 0);
}
@SuppressWarnings("unused")
@Test
public void testOr_Self() {
final BooleanBinding binding = Bindings.or(op1, op1);
binding.addListener(observer);
assertEquals(true || true, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1);
observer.reset();
op1.set(false);
assertEquals(false || false, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true || true, binding.get());
observer.check(binding, 1);
}
@Test
public void testOr_WeakReference() {
final ObservableBooleanValueMock op1 = new ObservableBooleanValueMock();
final ObservableBooleanValueMock op2 = new ObservableBooleanValueMock();
BooleanBinding binding = Bindings.or(op1, op2);
assertNotNull(op1.listener);
assertNotNull(op2.listener);
binding = null;
System.gc();
op1.fireInvalidationEvent();
assertNull(op1.listener);
assertNotNull(op2.listener);
op2.fireInvalidationEvent();
assertNull(op1.listener);
assertNull(op2.listener);
}
@Test(expected=NullPointerException.class)
public void testOr_null_x() {
Bindings.or(null, op1);
}
@Test(expected=NullPointerException.class)
public void testOr_x_null() {
Bindings.or(op1, null);
}
@Test
public void testNot() {
final BooleanBinding binding = Bindings.not(op1);
binding.addListener(observer);
assertEquals(!true, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1);
observer.reset();
op1.set(false);
assertEquals(!false, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(!true, binding.get());
observer.check(binding, 1);
}
@Test
public void testNot_Efficiency() {
final BooleanBinding binding = Bindings.not(op1);
binding.addListener(observer);
binding.get();
op1.set(false);
op1.set(true);
observer.check(binding, 1);
}
@Test(expected=NullPointerException.class)
public void testNot_null() {
Bindings.not(null);
}
@Test
public void testEqual() {
final BooleanBinding binding = Bindings.equal(op1, op2);
binding.addListener(observer);
assertEquals(true == false, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1, op2);
observer.reset();
op1.set(false);
assertEquals(false == false, binding.get());
observer.check(binding, 1);
op2.set(true);
assertEquals(false == true, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true == true, binding.get());
observer.check(binding, 1);
}
@Test
public void testEqual_Efficiency() {
final BooleanBinding binding = Bindings.equal(op1, op2);
binding.addListener(observer);
binding.get();
op1.set(false);
op2.set(true);
observer.check(binding, 1);
}
@Test
public void testEqual_Self() {
final BooleanBinding binding = Bindings.equal(op1, op1);
binding.addListener(observer);
assertEquals(true == true, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1);
observer.reset();
op1.set(false);
assertEquals(false == false, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true == true, binding.get());
observer.check(binding, 1);
}
@Test(expected=NullPointerException.class)
public void testEqual_null_x() {
Bindings.equal((ObservableBooleanValue)null, op1);
}
@Test(expected=NullPointerException.class)
public void testEqual_x_null() {
Bindings.equal(op1, (ObservableBooleanValue)null);
}
@Test
public void testNotEqual() {
final BooleanBinding binding = Bindings.notEqual(op1, op2);
binding.addListener(observer);
assertEquals(true != false, binding.get());
DependencyUtils.checkDependencies(binding.getDependencies(), op1, op2);
observer.reset();
op1.set(false);
assertEquals(false != false, binding.get());
observer.check(binding, 1);
op2.set(true);
assertEquals(false != true, binding.get());
observer.check(binding, 1);
op1.set(true);
assertEquals(true != true, binding.get());
observer.check(binding, 1);
}
private static class ObservableBooleanValueMock implements ObservableBooleanValue {
private InvalidationListener listener;
@Override
public boolean get() {
return false;
}
@Override
public Boolean getValue() {
return Boolean.FALSE;
}
private void fireInvalidationEvent() {
if (listener == null) {
fail("Attempt to fire an event with no listener attached");
}
this.listener.invalidated(this);
}
@Override
public void addListener(ChangeListener<? super Boolean> listener) {
}
@Override
public void removeListener(ChangeListener<? super Boolean> listener) {
}
@Override
public void addListener(InvalidationListener listener) {
if ((this.listener != null) && !this.listener.equals(listener)) {
fail("More than one listener set in mock.");
}
this.listener = listener;
}
@Override
public void removeListener(InvalidationListener listener) {
if (this.listener != listener) {
fail("Attempt to remove unknown listener");
}
this.listener = null;
}
}
}
