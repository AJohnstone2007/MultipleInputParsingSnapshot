package com.sun.scenario.animation.shared;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.util.Duration;
public class InfiniteClipEnvelope extends MultiLoopClipEnvelope {
protected InfiniteClipEnvelope(Animation animation) {
super(animation);
if (animation != null) {
autoReverse = animation.isAutoReverse();
}
}
@Override
public ClipEnvelope setCycleDuration(Duration cycleDuration) {
if (cycleDuration.isIndefinite()) {
return create(animation);
}
updateCycleTicks(cycleDuration);
return this;
}
@Override
public ClipEnvelope setCycleCount(int cycleCount) {
return (cycleCount != Animation.INDEFINITE) ? create(animation) : this;
}
@Override
public void setRate(double newRate) {
final Status status = animation.getStatus();
if (status != Status.STOPPED) {
setInternalCurrentRate((Math.abs(currentRate - rate) < EPSILON) ? newRate : -newRate);
deltaTicks = ticks - ticksRateChange(newRate);
if (isDirectionChanged(newRate)) {
final long delta = 2 * cycleTicks - cyclePos;
deltaTicks += delta;
ticks += delta;
}
abortCurrentPulse();
}
rate = newRate;
}
@Override
protected double calculateCurrentRate() {
return !autoReverse ? rate
: isDuringEvenCycle() ? rate : -rate;
}
@Override
public void timePulse(long currentTick) {
if (cycleTicks == 0L) {
return;
}
aborted = false;
inTimePulse = true;
try {
final long oldTicks = ticks;
long ticksChange = Math.round(currentTick * Math.abs(rate));
ticks = Math.max(0, deltaTicks + ticksChange);
long overallDelta = ticks - oldTicks;
if (overallDelta == 0) {
return;
}
long cycleDelta = (currentRate > 0) ? cycleTicks - cyclePos : cyclePos;
while (overallDelta >= cycleDelta) {
if (cycleDelta > 0) {
cyclePos = (currentRate > 0) ? cycleTicks : 0;
overallDelta -= cycleDelta;
AnimationAccessor.getDefault().playTo(animation, cyclePos, cycleTicks);
if (aborted) {
return;
}
}
if (autoReverse) {
setCurrentRate(-currentRate);
} else {
cyclePos = (currentRate > 0) ? 0 : cycleTicks;
AnimationAccessor.getDefault().jumpTo(animation, cyclePos, cycleTicks, false);
}
cycleDelta = cycleTicks;
}
if (overallDelta > 0) {
cyclePos += (currentRate > 0) ? overallDelta : -overallDelta;
AnimationAccessor.getDefault().playTo(animation, cyclePos, cycleTicks);
}
} finally {
inTimePulse = false;
}
}
@Override
public void jumpTo(long newTicks) {
if (cycleTicks == 0L) {
return;
}
final long oldTicks = ticks;
ticks = Math.max(0, newTicks) % (2 * cycleTicks);
final long delta = ticks - oldTicks;
if (delta != 0) {
deltaTicks += delta;
if (autoReverse) {
if (ticks > cycleTicks) {
cyclePos = 2 * cycleTicks - ticks;
if (animation.getStatus() == Status.RUNNING) {
setCurrentRate(-rate);
}
} else {
cyclePos = ticks;
if (animation.getStatus() == Status.RUNNING) {
setCurrentRate(rate);
}
}
} else {
cyclePos = ticks % cycleTicks;
if (cyclePos == 0) {
cyclePos = ticks;
}
}
AnimationAccessor.getDefault().jumpTo(animation, cyclePos, cycleTicks, false);
abortCurrentPulse();
}
}
}
