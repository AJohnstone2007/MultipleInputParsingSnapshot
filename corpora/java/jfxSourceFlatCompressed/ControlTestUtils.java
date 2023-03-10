package test.com.sun.javafx.scene.control.infrastructure;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.chart.Axis;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Tab;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import test.com.sun.javafx.binding.ExpressionHelperUtility;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public final class ControlTestUtils {
private ControlTestUtils() { }
public static void assertStyleClassContains(Tab tab, String styleClass) {
assertStyleClassContains(
"The style class " + styleClass + " was not set on the Tab " + tab,
tab, styleClass);
}
public static void assertStyleClassContains(String message, Tab tab, String styleClass) {
assertTrue(message, tab.getStyleClass().contains(styleClass));
}
public static void assertStyleClassContains(MenuItem mi, String styleClass) {
assertStyleClassContains(
"The style class " + styleClass + " was not set on the MenuItem " + mi,
mi, styleClass);
}
public static void assertStyleClassContains(String message, MenuItem mi, String styleClass) {
assertTrue(message, mi.getStyleClass().contains(styleClass));
}
public static void assertStyleClassContains(PopupControl control, String styleClass) {
assertStyleClassContains(
"The style class " + styleClass + " was not set on the Popupcontrol " + control,
control, styleClass);
}
public static void assertStyleClassContains(String message, PopupControl control, String styleClass) {
assertTrue(message, control.getStyleClass().contains(styleClass));
}
public static void assertStyleClassContains(Control control, String styleClass) {
assertStyleClassContains(
"The style class " + styleClass + " was not set on the control " + control,
control, styleClass);
}
public static void assertStyleClassContains(String message, Control control, String styleClass) {
assertTrue(message, control.getStyleClass().contains(styleClass));
}
public static void assertPseudoClassExists(Control control, String pseudoClass) {
assertPseudoClassExists(
"The pseudo class " + pseudoClass + " was not set on control " + control,
control, pseudoClass);
}
public static void assertPseudoClassExists(String message, Control control, String pseudoClass) {
Set<PseudoClass> allStates = control.getPseudoClassStates();
PseudoClass state = PseudoClass.getPseudoClass(pseudoClass);
assertTrue(message, allStates.contains(state));
}
public static void assertPseudoClassDoesNotExist(Control control, String pseudoClass) {
assertPseudoClassDoesNotExist(
"The pseudo class " + pseudoClass + " was unexpectedly set on control " + control,
control, pseudoClass);
}
public static void assertPseudoClassDoesNotExist(String message, Control control, String pseudoClass) {
Set<PseudoClass> allStates = control.getPseudoClassStates();
PseudoClass state = PseudoClass.getPseudoClass(pseudoClass);
assertFalse(message, allStates.contains(state));
}
public static void assertPseudoClassExists(Axis axis, String pseudoClass) {
assertPseudoClassExists(
"The pseudo class " + pseudoClass + " was not set on axis " + axis,
axis, pseudoClass);
}
public static void assertPseudoClassExists(String message, Axis axis, String pseudoClass) {
Set<PseudoClass> allStates = axis.getPseudoClassStates();
PseudoClass state = PseudoClass.getPseudoClass(pseudoClass);
assertTrue(message, allStates.contains(state));
}
public static void assertPseudoClassDoesNotExist(Axis axis, String pseudoClass) {
assertPseudoClassDoesNotExist(
"The pseudo class " + pseudoClass + " was unexpectedly set on axis " + axis,
axis, pseudoClass);
}
public static void assertPseudoClassDoesNotExist(String message, Axis axis, String pseudoClass) {
Set<PseudoClass> allStates = axis.getPseudoClassStates();
PseudoClass state = PseudoClass.getPseudoClass(pseudoClass);
assertFalse(message, allStates.contains(state));
}
public static void assertStyleClassContains(Pagination control, String styleClass) {
assertStyleClassContains(
"The style class " + styleClass + " was not set on the Pagination " + control,
control, styleClass);
}
public static void assertStyleClassContains(String message, Pagination control, String styleClass) {
assertTrue(message, control.getStyleClass().contains(styleClass));
}
public static void assertListenerListContains(ObservableList list, ListChangeListener listener) {
assertListenerListContains("The listener " + listener + " was not contained in " + list, list, listener);
}
public static void assertListenerListContains(String message, ObservableList list, ListChangeListener listener) {
}
public static void assertListenerListDoesNotContain(ObservableList list, ListChangeListener listener) {
assertListenerListDoesNotContain("The listener " + listener + " was contained in " + list, list, listener);
}
public static void assertListenerListDoesNotContain(String message, ObservableList list, ListChangeListener listener) {
}
public static ListChangeListener getListChangeListener(Object bean, String fieldName) {
return (ListChangeListener) getListener(bean, fieldName);
}
public static void assertValueListenersContains(ObservableValue value, InvalidationListener listener) {
assertValueListenersContains("The listener " + listener + " was not contained in " + value, value, listener);
}
public static void assertValueListenersContains(String message, ObservableValue value, InvalidationListener listener) {
List listeners = getObservableValueListeners(value);
assertTrue(message, listeners != null && listeners.contains(listener));
}
public static void assertValueListenersDoesNotContain(ObservableValue value, InvalidationListener listener) {
assertValueListenersDoesNotContain("The listener " + listener + " was contained in " + value, value, listener);
}
public static void assertValueListenersDoesNotContain(String message, ObservableValue value, InvalidationListener listener) {
List listeners = getObservableValueListeners(value);
assertTrue(message, listeners == null || !listeners.contains(listener));
}
public static int getListenerCount(ObservableValue value) {
return getObservableValueListeners(value).size();
}
public static InvalidationListener getInvalidationListener(Object bean, String fieldName) {
return (InvalidationListener) getListener(bean, fieldName);
}
private static Object getListener(Object bean, String fieldName) {
try {
Class clazz = bean.getClass();
Field field = clazz.getDeclaredField(fieldName);
field.setAccessible(true);
return field.get(bean);
} catch (Exception e) {
e.printStackTrace();
}
return null;
}
private static List getObservableValueListeners(ObservableValue value) {
ArrayList results = new ArrayList();
results.addAll(ExpressionHelperUtility.getChangeListeners(value));
results.addAll(ExpressionHelperUtility.getInvalidationListeners(value));
return results;
}
public static ExceptionHandler setHandler() {
return new ExceptionHandler();
}
public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
private Throwable cause = null;
private Thread.UncaughtExceptionHandler oldHandler;
public ExceptionHandler() {
this.oldHandler = Thread.currentThread().getUncaughtExceptionHandler();
Thread.currentThread().setUncaughtExceptionHandler(this);
}
@Override
public void uncaughtException(Thread t, Throwable e) {
e.printStackTrace();
cause = e;
}
public void checkException() {
if (cause != null) {
if (cause instanceof Error) {
throw (Error) cause;
} else if (cause instanceof RuntimeException) {
throw (RuntimeException) cause;
} else {
throw new AssertionError(cause);
}
}
}
public void resetHandler() {
Thread.currentThread().setUncaughtExceptionHandler(oldHandler);
}
}
public static void runWithExceptionHandler(Runnable r) {
ExceptionHandler myHandler = new ExceptionHandler();
try {
r.run();
} finally {
myHandler.resetHandler();
}
myHandler.checkException();
}
}
