package test.javafx.scene.web;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import static javafx.concurrent.Worker.State.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
public class LoadNotificationsTest extends TestBase {
Set<State> log = new HashSet<State>();
AssertionError assertion;
String currentUrl;
@Test public void testSuccessfulLoad() {
currentUrl = "src/test/resources/test/html/ipsum.html";
testUrl(new File(currentUrl).toURI().toASCIIString());
}
@Test public void testFailedLoad() {
currentUrl = "no.such.file";
testUrl(currentUrl);
}
@Test public void testEmptyLoad() {
currentUrl = "about:blank";
testUrl(null);
testUrl("");
testUrl("about:blank");
}
private void testUrl(String url) {
log.clear();
assertion = null;
submit(() -> {
checkState(RUNNING, getEngine().getLoadWorker().getState());
});
load(url);
check();
}
@Override public void changed(ObservableValue property, Object oldValue, Object newValue) {
Worker<Void> worker = getEngine().getLoadWorker();
if (property == worker.runningProperty()) {
checkRunning((Boolean)oldValue, (Boolean)newValue);
} else if (property == worker.stateProperty()) {
checkState((State)oldValue, (State)newValue);
}
}
private void checkRunning(boolean oldValue, boolean newValue) {
State state = getEngine().getLoadWorker().getState();
if (newValue) {
assertTrue("LoadWorker.running should be false",
state == SCHEDULED || state == RUNNING);
} else {
assertTrue("LoadWorker.running should be true",
state == SUCCEEDED || state == FAILED);
}
}
private void checkState(State oldValue, State newValue) {
WebEngine web = getEngine();
Worker<Void> worker = web.getLoadWorker();
log.add(newValue);
try {
switch (newValue) {
case READY:
assertEquals("LoadWorker.totalWork", -1, worker.getTotalWork(), 0);
assertEquals("LoadWorker.workDone", -1, worker.getWorkDone(), 0);
assertEquals("LoadWorker.progress", -1, worker.getProgress(), 0);
assertNull("LoadWorker.exception should be null", worker.getException());
assertEquals("LoadWorker.message", "", worker.getMessage());
break;
case SCHEDULED:
case RUNNING:
assertEquals("LoadWorker.state",
(newValue == RUNNING ? SCHEDULED : READY),
oldValue);
assertEquals("LoadWorker.totalWork", 100.0, worker.getTotalWork(), 0);
assertEquals("LoadWorker.workDone", 0.0, worker.getWorkDone(), 0);
assertEquals("LoadWorker.progress", 0.0, worker.getProgress(), 0);
assertNull("LoadWorker.exception should be null", worker.getException());
assertTrue("LoadWorker.message should read 'Loading [url]'",
worker.getMessage().matches("Loading .*" + currentUrl));
assertNull("WebEngine.document should be null", web.getDocument());
assertTrue("WebEngine.location should be set",
web.getLocation().endsWith(currentUrl));
break;
case SUCCEEDED:
assertEquals("LoadWorker.state", RUNNING, oldValue);
assertEquals("LoadWorker.totalWork", 100.0, worker.getTotalWork(), 0);
assertEquals("LoadWorker.workDone", 100.0, worker.getWorkDone(), 0);
assertEquals("LoadWorker.progress", 1.0, worker.getProgress(), 0);
assertNull("LoadWorker.exception should be null", worker.getException());
assertTrue("LoadWorker.message should read 'Loading complete'",
worker.getMessage().startsWith("Loading complete"));
assertNotNull("WebEngine.document should be set", web.getDocument());
assertTrue("WebEngine.location should be set",
web.getLocation().endsWith(currentUrl));
break;
case FAILED:
assertEquals("LoadWorker.state", RUNNING, oldValue);
assertEquals("LoadWorker.totalWork", 100.0, worker.getTotalWork(), 0);
assertNotNull("LoadWorker.exception should be set", worker.getException());
assertTrue("LoadWorker.message should read 'Loading failed'",
worker.getMessage().startsWith("Loading failed"));
break;
default:
fail("Unexpected LoadWorker.state == " + newValue);
}
} catch (AssertionError e) {
assertion = e;
}
}
private void check() {
if (assertion != null) {
throw assertion;
}
assertTrue("State.READY was never set", log.contains(READY));
assertTrue("State.SCHEDULED was never set", log.contains(SCHEDULED));
assertTrue("State.RUNNING was never set", log.contains(RUNNING));
assertTrue("Neither State.SUCCEEDED nor State.FAILED has been set",
log.contains(SUCCEEDED) || log.contains(FAILED));
}
}
