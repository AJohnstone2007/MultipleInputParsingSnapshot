package test.com.sun.javafx.event;
import com.sun.javafx.event.CompositeEventHandler;
import com.sun.javafx.event.CompositeEventHandlerShim;
import static org.junit.Assert.*;
import test.com.sun.javafx.event.EventCountingHandler;
import test.com.sun.javafx.event.EmptyEvent;
import javafx.event.Event;
import javafx.event.WeakEventHandler;
import javafx.event.WeakEventHandlerUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class CompositeEventHandlerTest {
private CompositeEventHandler<Event> compositeEventHandler;
@Before
public void setUp() {
compositeEventHandler = new CompositeEventHandler<Event>();
}
@Test
public void testHasFilterWeakCleared() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventHandler =
new WeakEventHandler<Event>(eventCountingHandler);
compositeEventHandler.addEventFilter(weakEventHandler);
assertFalse("must not have handler after adding filter", compositeEventHandler.hasHandler());
assertTrue("must have filter", compositeEventHandler.hasFilter());
WeakEventHandlerUtil.clear(weakEventHandler);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void testHasHandlerAddWeakClear() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventHandler =
new WeakEventHandler<Event>(eventCountingHandler);
compositeEventHandler.addEventHandler(weakEventHandler);
assertTrue("sanity: really added?", CompositeEventHandlerShim.containsHandler(
compositeEventHandler, weakEventHandler));
assertFalse("must not have filter after adding handler", compositeEventHandler.hasFilter());
assertTrue("must have handler", compositeEventHandler.hasHandler());
WeakEventHandlerUtil.clear(weakEventHandler);
assertFalse("must not have handler", compositeEventHandler.hasHandler());
assertFalse("must not have filter", compositeEventHandler.hasFilter());
}
@Test
public void testHasFilterWeak() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventHandler =
new WeakEventHandler<Event>(eventCountingHandler);
compositeEventHandler.addEventFilter(weakEventHandler);
assertFalse("must not have handler after adding filter", compositeEventHandler.hasHandler());
assertTrue("must have filter", compositeEventHandler.hasFilter());
compositeEventHandler.removeEventFilter(weakEventHandler);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void testHasHandlerAddWeak() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventHandler =
new WeakEventHandler<Event>(eventCountingHandler);
compositeEventHandler.addEventHandler(weakEventHandler);
assertTrue("sanity: really added?", CompositeEventHandlerShim.containsHandler(
compositeEventHandler, weakEventHandler));
assertFalse("must not have filter after adding handler", compositeEventHandler.hasFilter());
assertTrue("must have handler", compositeEventHandler.hasHandler());
compositeEventHandler.removeEventHandler(weakEventHandler);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void testHasFilter() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
compositeEventHandler.addEventFilter(eventCountingHandler);
assertFalse("must not have handler after adding filter", compositeEventHandler.hasHandler());
assertTrue("must have filter", compositeEventHandler.hasFilter());
compositeEventHandler.removeEventFilter(eventCountingHandler);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void testHasHandlerAdd() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
compositeEventHandler.addEventHandler(eventCountingHandler);
assertTrue("sanity: really added?", CompositeEventHandlerShim.containsHandler(
compositeEventHandler, eventCountingHandler));
assertFalse("must not have filter after adding handler", compositeEventHandler.hasFilter());
assertTrue("must have handler", compositeEventHandler.hasHandler());
compositeEventHandler.removeEventHandler(eventCountingHandler);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void testHasHandlerSingleton() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
compositeEventHandler.setEventHandler(eventCountingHandler);
assertFalse("must not have filter after set handler", compositeEventHandler.hasFilter());
assertTrue("must have handler", compositeEventHandler.hasHandler());
compositeEventHandler.setEventHandler(null);
assertFalse("must not have filter", compositeEventHandler.hasFilter());
assertFalse("must not have handler", compositeEventHandler.hasHandler());
}
@Test
public void weakEventHandlerTest() {
final EventCountingHandler<Event> eventCountingHandler =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventHandler =
new WeakEventHandler<Event>(eventCountingHandler);
compositeEventHandler.addEventHandler(weakEventHandler);
Assert.assertTrue(
CompositeEventHandlerShim.containsHandler(compositeEventHandler, weakEventHandler));
compositeEventHandler.dispatchCapturingEvent(new EmptyEvent());
Assert.assertEquals(0, eventCountingHandler.getEventCount());
compositeEventHandler.dispatchBubblingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingHandler.getEventCount());
WeakEventHandlerUtil.clear(weakEventHandler);
Assert.assertFalse(
CompositeEventHandlerShim.containsHandler(compositeEventHandler, weakEventHandler));
compositeEventHandler.dispatchCapturingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingHandler.getEventCount());
compositeEventHandler.dispatchBubblingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingHandler.getEventCount());
}
@Test
public void weakEventFilterTest() {
final EventCountingHandler<Event> eventCountingFilter =
new EventCountingHandler<Event>();
final WeakEventHandler<Event> weakEventFilter =
new WeakEventHandler<Event>(eventCountingFilter);
compositeEventHandler.addEventFilter(weakEventFilter);
Assert.assertTrue(
CompositeEventHandlerShim.containsFilter(compositeEventHandler, weakEventFilter));
compositeEventHandler.dispatchCapturingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingFilter.getEventCount());
compositeEventHandler.dispatchBubblingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingFilter.getEventCount());
WeakEventHandlerUtil.clear(weakEventFilter);
Assert.assertFalse(
CompositeEventHandlerShim.containsFilter(compositeEventHandler, weakEventFilter));
compositeEventHandler.dispatchCapturingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingFilter.getEventCount());
compositeEventHandler.dispatchBubblingEvent(new EmptyEvent());
Assert.assertEquals(1, eventCountingFilter.getEventCount());
}
}
