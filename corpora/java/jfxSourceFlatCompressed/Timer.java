package com.sun.webkit;
import java.security.AccessController;
import java.security.PrivilegedAction;
public class Timer {
private static Timer instance;
private static Mode mode;
long fireTime;
Timer() {
}
public static enum Mode {
PLATFORM_TICKS,
SEPARATE_THREAD
}
@SuppressWarnings("removal")
public synchronized static Mode getMode() {
if (mode == null) {
mode = Boolean.valueOf(AccessController.doPrivileged(
(PrivilegedAction<String>) () -> System.getProperty(
"com.sun.webkit.platformticks", "true"))) ? Mode.PLATFORM_TICKS : Mode.SEPARATE_THREAD;
}
return mode;
}
public synchronized static Timer getTimer() {
if (instance == null) {
instance = (getMode() == Mode.PLATFORM_TICKS) ?
new Timer() : new SeparateThreadTimer();
}
return instance;
}
public synchronized void notifyTick() {
if (fireTime > 0 && fireTime <= System.currentTimeMillis()) {
fireTimerEvent(fireTime);
}
}
void fireTimerEvent(long time) {
boolean needFire = false;
synchronized (this) {
if (time == fireTime) {
needFire = true;
fireTime = 0;
}
}
if (needFire) {
WebPage.lockPage();
try {
twkFireTimerEvent();
} finally {
WebPage.unlockPage();
}
}
}
synchronized void setFireTime(long time) {
fireTime = time;
}
private static void fwkSetFireTime(double fireTime) {
getTimer().setFireTime((long)Math.ceil(fireTime * 1000));
}
private static void fwkStopTimer() {
getTimer().setFireTime(0);
}
private static native void twkFireTimerEvent();
}
final class SeparateThreadTimer extends Timer implements Runnable {
private final Invoker invoker;
private final FireRunner fireRunner;
private final Thread thread;
SeparateThreadTimer() {
invoker = Invoker.getInvoker();
fireRunner = new FireRunner();
thread = new Thread(this, "WebPane-Timer");
thread.setDaemon(true);
}
private final class FireRunner implements Runnable {
private volatile long time;
private Runnable forTime(long time) {
this.time = time;
return this;
}
@Override
public void run() {
fireTimerEvent(time);
}
}
@Override
synchronized void setFireTime(long time) {
super.setFireTime(time);
if (thread.getState() == Thread.State.NEW) {
thread.start();
}
notifyAll();
}
@Override
public synchronized void run() {
while (true) {
try {
if (fireTime > 0) {
long curTime = System.currentTimeMillis();
while (fireTime > curTime) {
wait(fireTime - curTime);
curTime = System.currentTimeMillis();
}
if (fireTime > 0) {
invoker.invokeOnEventThread(fireRunner.forTime(fireTime));
}
}
wait();
} catch (InterruptedException e) {
break;
}
}
}
@Override
public void notifyTick() {
assert false;
}
}
