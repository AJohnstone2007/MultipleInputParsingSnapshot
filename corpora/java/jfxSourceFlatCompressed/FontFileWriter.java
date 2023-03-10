package com.sun.javafx.font;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
class FontFileWriter implements FontConstants {
byte[] header;
int pos;
int headerPos;
int writtenBytes;
FontTracker tracker;
File file;
RandomAccessFile raFile;
public FontFileWriter() {
if (!hasTempPermission()) {
tracker = FontTracker.getTracker();
}
}
protected void setLength(int size) throws IOException {
if (raFile == null) {
throw new IOException("File not open");
}
checkTracker(size);
raFile.setLength(size);
}
public void seek(int pos) throws IOException {
if (raFile == null) {
throw new IOException("File not open");
}
if (pos != this.pos) {
raFile.seek(pos);
this.pos = pos;
}
}
public File getFile() {
return file;
}
@SuppressWarnings("removal")
public File openFile() throws PrivilegedActionException {
pos = 0;
writtenBytes = 0;
file = AccessController.doPrivileged(
(PrivilegedExceptionAction<File>) () -> {
try {
return Files.createTempFile("+JXF", ".tmp").toFile();
} catch (IOException e) {
throw new IOException("Unable to create temporary file");
}
}
);
if (tracker != null) {
tracker.add(file);
}
raFile = AccessController.doPrivileged(
(PrivilegedExceptionAction<RandomAccessFile>) () -> new RandomAccessFile(file, "rw")
);
if (tracker != null) {
tracker.set(file, raFile);
}
if (PrismFontFactory.debugFonts) {
System.err.println("Temp file created: " + file.getPath());
}
return file;
}
public void closeFile() throws IOException {
if (header != null) {
raFile.seek(0);
raFile.write(header);
header = null;
}
if (raFile != null) {
raFile.close();
raFile = null;
}
if (tracker != null) {
tracker.remove(file);
}
}
@SuppressWarnings("removal")
public void deleteFile() {
if (file != null) {
if (tracker != null) {
tracker.subBytes(writtenBytes);
}
try {
closeFile();
} catch (Exception e) {
}
try {
AccessController.doPrivileged(
(PrivilegedExceptionAction<Void>) () -> {
file.delete();
return null;
}
);
if (PrismFontFactory.debugFonts) {
System.err.println("Temp file delete: " + file.getPath());
}
} catch (Exception e) {
}
file = null;
raFile = null;
}
}
public boolean isTracking() {
return tracker != null;
}
private void checkTracker(int size) throws IOException {
if (tracker != null) {
if (size < 0 || pos > FontTracker.MAX_FILE_SIZE - size) {
throw new IOException("File too big.");
}
if (tracker.getNumBytes() > FontTracker.MAX_TOTAL_BYTES - size) {
throw new IOException("Total files too big.");
}
}
}
private void checkSize(int size) throws IOException {
if (tracker != null) {
checkTracker(size);
tracker.addBytes(size);
writtenBytes += size;
}
}
private void setHeaderPos(int pos) {
headerPos = pos;
}
public void writeHeader(int format, short numTables) throws IOException {
int size = TTCHEADERSIZE + (DIRECTORYENTRYSIZE * numTables);
checkSize(size);
header = new byte[size];
short maxPower2 = numTables;
maxPower2 |= (maxPower2 >> 1);
maxPower2 |= (maxPower2 >> 2);
maxPower2 |= (maxPower2 >> 4);
maxPower2 |= (maxPower2 >> 8);
maxPower2 &= ~(maxPower2 >> 1);
short searchRange = (short)(maxPower2 * 16);
short entrySelector = 0;
while (maxPower2 > 1) {
entrySelector++;
maxPower2 >>= 1;
}
short rangeShift = (short)(numTables * 16 - searchRange);
setHeaderPos(0);
writeInt(format);
writeShort(numTables);
writeShort(searchRange);
writeShort(entrySelector);
writeShort(rangeShift);
}
public void writeDirectoryEntry(int index, int tag,
int checksum, int offset, int length) throws IOException
{
setHeaderPos(TTCHEADERSIZE + DIRECTORYENTRYSIZE * index);
writeInt(tag);
writeInt(checksum);
writeInt(offset);
writeInt(length);
}
private void writeInt(int value) throws IOException {
header[headerPos++] = (byte)((value & 0xFF000000) >> 24);
header[headerPos++] = (byte)((value & 0x00FF0000) >> 16);
header[headerPos++] = (byte)((value & 0x0000FF00) >> 8);
header[headerPos++] = (byte) (value & 0x000000FF);
}
private void writeShort(short value) throws IOException {
header[headerPos++] = (byte)((value & 0xFF00) >> 8);
header[headerPos++] = (byte)(value & 0xFF);
}
public void writeBytes(byte[] buffer) throws IOException {
writeBytes(buffer, 0, buffer.length);
}
public void writeBytes(byte[] buffer, int startPos, int length)
throws IOException
{
checkSize(length);
raFile.write(buffer, startPos, length);
pos += length;
}
@SuppressWarnings("removal")
static boolean hasTempPermission() {
if (System.getSecurityManager() == null) {
return true;
}
File f = null;
boolean hasPerm = false;
try {
f = Files.createTempFile("+JXF", ".tmp").toFile();
f.delete();
f = null;
hasPerm = true;
} catch (Throwable t) {
}
return hasPerm;
}
static class FontTracker {
public static final int MAX_FILE_SIZE = 32 * 1024 * 1024;
public static final int MAX_TOTAL_BYTES = 10 * MAX_FILE_SIZE;
static int numBytes;
static FontTracker tracker;
public static synchronized FontTracker getTracker() {
if (tracker == null) {
tracker = new FontTracker();
}
return tracker;
}
public synchronized int getNumBytes() {
return numBytes;
}
public synchronized void addBytes(int sz) {
numBytes += sz;
}
public synchronized void subBytes(int sz) {
numBytes -= sz;
}
private static Semaphore cs = null;
private static synchronized Semaphore getCS() {
if (cs == null) {
cs = new Semaphore(5, true);
}
return cs;
}
public boolean acquirePermit() throws InterruptedException {
return getCS().tryAcquire(120, TimeUnit.SECONDS);
}
public void releasePermit() {
getCS().release();
}
public void add(File file) {
TempFileDeletionHook.add(file);
}
public void set(File file, RandomAccessFile raf) {
TempFileDeletionHook.set(file, raf);
}
public void remove(File file) {
TempFileDeletionHook.remove(file);
}
private static class TempFileDeletionHook {
private static HashMap<File, RandomAccessFile> files =
new HashMap<File, RandomAccessFile>();
private static Thread t = null;
@SuppressWarnings("removal")
static void init() {
if (t == null) {
java.security.AccessController.doPrivileged(
(java.security.PrivilegedAction) () -> {
t = new Thread(() -> {
runHooks();
});
Runtime.getRuntime().addShutdownHook(t);
return null;
}
);
}
}
private TempFileDeletionHook() {}
static synchronized void add(File file) {
init();
files.put(file, null);
}
static synchronized void set(File file, RandomAccessFile raf) {
files.put(file, raf);
}
static synchronized void remove(File file) {
files.remove(file);
}
static synchronized void runHooks() {
if (files.isEmpty()) {
return;
}
for (Map.Entry<File, RandomAccessFile> entry : files.entrySet())
{
try {
if (entry.getValue() != null) {
entry.getValue().close();
}
} catch (Exception e) {}
entry.getKey().delete();
}
}
}
}
}
