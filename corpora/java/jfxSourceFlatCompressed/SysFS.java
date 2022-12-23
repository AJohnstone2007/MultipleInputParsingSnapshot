package com.sun.glass.ui.monocle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
class SysFS {
static final String CURSOR_BLINK =
"/sys/devices/virtual/graphics/fbcon/cursor_blink";
static Map<String, BitSet> readCapabilities(File sysPath) {
Map<String, BitSet> capsMap = new HashMap<String, BitSet>();
File[] capsFiles = new File(sysPath, "device/capabilities").listFiles();
if (capsFiles == null) {
return capsMap;
}
for (int i = 0; i < capsFiles.length; i++) {
try {
BufferedReader r = new BufferedReader(new FileReader(capsFiles[i]));
String s = r.readLine();
r.close();
if (s == null) {
continue;
}
String[] elements = s.split(" ");
if (elements == null) {
continue;
}
byte[] b = new byte[elements.length * (LinuxArch.is64Bit() ? 8 : 4)];
ByteBuffer bb = ByteBuffer.wrap(b);
bb.order(ByteOrder.LITTLE_ENDIAN);
for (int j = elements.length - 1; j >= 0; j--) {
if (LinuxArch.is64Bit()) {
bb.putLong(Long.parseUnsignedLong(elements[j], 16));
} else {
bb.putInt(Integer.parseUnsignedInt(elements[j], 16));
}
}
capsMap.put(capsFiles[i].getName(), BitSet.valueOf(b));
} catch (IOException | RuntimeException e) {
e.printStackTrace();
}
}
return capsMap;
}
static Map<String, String> readUEvent(File sysPath) {
Map<String, String> uevent = new HashMap();
File f = new File(sysPath, "device/uevent");
try {
BufferedReader r = new BufferedReader(new FileReader(f));
for (String line; (line = r.readLine()) != null;) {
int i = line.indexOf("=");
if (i >= 0) {
uevent.put(line.substring(0, i), line.substring(i + 1));
}
}
} catch (IOException e) {
}
return uevent;
}
static void triggerUdevNotification(String sysClass) {
File[] devices = new File("/sys/class/" + sysClass).listFiles();
byte[] action = "change".getBytes();
for (File device: devices) {
File uevent = new File(device, "uevent");
if (uevent.exists()) {
try {
write(uevent.getAbsolutePath(), action);
} catch (IOException e) {
System.err.println("Udev: Failed to write to " + uevent);
System.err.println("      Check that you have permission to access input devices");
if (!e.getMessage().contains("Permission denied")) {
e.printStackTrace();
}
}
}
}
}
static void write(String location, byte[] value) throws IOException {
FileOutputStream out = new FileOutputStream(location);
try {
out.write(value);
} finally {
out.close();
}
}
static void write(String location, String value) throws IOException {
write(location, value.getBytes());
}
static int[] readInts(String location, int expectedLength) throws IOException {
BufferedReader r = new BufferedReader(new FileReader(location));
String s = r.readLine();
r.close();
if (s != null && s.length() > 0) {
String[] elements = s.split(",");
try {
if (expectedLength == 0 || elements.length == expectedLength) {
int[] xs = new int[elements.length];
for (int i = 0; i < xs.length; i++) {
xs[i] = Integer.parseInt(elements[i]);
}
return xs;
}
} catch (NumberFormatException e) {
}
}
if (expectedLength != 0) {
throw new IOException("Expected to find " + expectedLength
+ " integers in " + location + " but found '"
+ s + "'");
} else {
return new int[0];
}
}
static int readInt(String location) throws IOException {
BufferedReader r = new BufferedReader(new FileReader(location));
String s = r.readLine();
r.close();
try {
if (s != null && s.length() > 0) {
return Integer.parseInt(s);
} else {
throw new IOException(location + " does not contain an integer");
}
} catch (NumberFormatException e) {
throw new IOException(
location + " does not contain an integer ('" + s + "'");
}
}
}
