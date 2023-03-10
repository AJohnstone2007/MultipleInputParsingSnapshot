package com.sun.webkit.network;
import com.sun.javafx.logging.PlatformLogger;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
final class CookieStore {
private static final PlatformLogger logger =
PlatformLogger.getLogger(CookieStore.class.getName());
private static final int MAX_BUCKET_SIZE = 50;
private static final int TOTAL_COUNT_LOWER_THRESHOLD = 3000;
private static final int TOTAL_COUNT_UPPER_THRESHOLD = 4000;
private final Map<String,Map<Cookie,Cookie>> buckets =
new HashMap<String,Map<Cookie,Cookie>>();
private int totalCount = 0;
CookieStore() {
}
Cookie get(Cookie cookie) {
Map<Cookie,Cookie> bucket = buckets.get(cookie.getDomain());
if (bucket == null) {
return null;
}
Cookie storedCookie = bucket.get(cookie);
if (storedCookie == null) {
return null;
}
if (storedCookie.hasExpired()) {
bucket.remove(storedCookie);
totalCount--;
log("Expired cookie removed by get", storedCookie, bucket);
return null;
}
return storedCookie;
}
List<Cookie> get(String hostname, String path, boolean secureProtocol,
boolean httpApi)
{
if (logger.isLoggable(Level.FINEST)) {
logger.finest("hostname: [{0}], path: [{1}], "
+ "secureProtocol: [{2}], httpApi: [{3}]", new Object[] {
hostname, path, secureProtocol, httpApi});
}
ArrayList<Cookie> result = new ArrayList<Cookie>();
String domain = hostname;
while (domain.length() > 0) {
Map<Cookie,Cookie> bucket = buckets.get(domain);
if (bucket != null) {
find(result, bucket, hostname, path, secureProtocol, httpApi);
}
int nextPoint = domain.indexOf('.');
if (nextPoint != -1) {
domain = domain.substring(nextPoint + 1);
} else {
break;
}
}
Collections.sort(result, new GetComparator());
long currentTime = System.currentTimeMillis();
for (Cookie cookie : result) {
cookie.setLastAccessTime(currentTime);
}
logger.finest("result: {0}", result);
return result;
}
private void find(List<Cookie> list, Map<Cookie,Cookie> bucket,
String hostname, String path, boolean secureProtocol,
boolean httpApi)
{
Iterator<Cookie> it = bucket.values().iterator();
while (it.hasNext()) {
Cookie cookie = it.next();
if (cookie.hasExpired()) {
it.remove();
totalCount--;
log("Expired cookie removed by find", cookie, bucket);
continue;
}
if (cookie.getHostOnly()) {
if (!hostname.equalsIgnoreCase(cookie.getDomain())) {
continue;
}
} else {
if (!Cookie.domainMatches(hostname, cookie.getDomain())) {
continue;
}
}
if (!Cookie.pathMatches(path, cookie.getPath())) {
continue;
}
if (cookie.getSecureOnly() && !secureProtocol) {
continue;
}
if (cookie.getHttpOnly() && !httpApi) {
continue;
}
list.add(cookie);
}
}
private static final class GetComparator implements Comparator<Cookie> {
@Override
public int compare(Cookie c1, Cookie c2) {
int d = c2.getPath().length() - c1.getPath().length();
if (d != 0) {
return d;
}
return c1.getCreationTime().compareTo(c2.getCreationTime());
}
}
void put(Cookie cookie) {
Map<Cookie,Cookie> bucket = buckets.get(cookie.getDomain());
if (bucket == null) {
bucket = new LinkedHashMap<Cookie,Cookie>(20);
buckets.put(cookie.getDomain(), bucket);
}
if (cookie.hasExpired()) {
log("Cookie expired", cookie, bucket);
if (bucket.remove(cookie) != null) {
totalCount--;
log("Expired cookie removed by put", cookie, bucket);
}
} else {
if (bucket.put(cookie, cookie) == null) {
totalCount++;
log("Cookie added", cookie, bucket);
if (bucket.size() > MAX_BUCKET_SIZE) {
purge(bucket);
}
if (totalCount > TOTAL_COUNT_UPPER_THRESHOLD) {
purge();
}
} else {
log("Cookie updated", cookie, bucket);
}
}
}
private void purge(Map<Cookie,Cookie> bucket) {
logger.finest("Purging bucket: {0}", bucket.values());
Cookie earliestCookie = null;
Iterator<Cookie> it = bucket.values().iterator();
while (it.hasNext()) {
Cookie cookie = it.next();
if (cookie.hasExpired()) {
it.remove();
totalCount--;
log("Expired cookie removed", cookie, bucket);
} else {
if (earliestCookie == null || cookie.getLastAccessTime()
< earliestCookie.getLastAccessTime())
{
earliestCookie = cookie;
}
}
}
if (bucket.size() > MAX_BUCKET_SIZE) {
bucket.remove(earliestCookie);
totalCount--;
log("Excess cookie removed", earliestCookie, bucket);
}
}
private void purge() {
logger.finest("Purging store");
Queue<Cookie> removalQueue = new PriorityQueue<Cookie>(totalCount / 2,
new RemovalComparator());
for (Map.Entry<String,Map<Cookie,Cookie>> entry : buckets.entrySet()) {
Map<Cookie,Cookie> bucket = entry.getValue();
Iterator<Cookie> it = bucket.values().iterator();
while (it.hasNext()) {
Cookie cookie = it.next();
if (cookie.hasExpired()) {
it.remove();
totalCount--;
log("Expired cookie removed", cookie, bucket);
} else {
removalQueue.add(cookie);
}
}
}
while (totalCount > TOTAL_COUNT_LOWER_THRESHOLD) {
Cookie cookie = removalQueue.remove();
Map<Cookie,Cookie> bucket = buckets.get(cookie.getDomain());
if (bucket != null) {
bucket.remove(cookie);
totalCount--;
log("Excess cookie removed", cookie, bucket);
}
}
}
private static final class RemovalComparator implements Comparator<Cookie> {
@Override
public int compare(Cookie c1, Cookie c2) {
return (int) (c1.getLastAccessTime() - c2.getLastAccessTime());
}
}
private void log(String message, Cookie cookie,
Map<Cookie,Cookie> bucket)
{
if (logger.isLoggable(Level.FINEST)) {
logger.finest("{0}: {1}, bucket size: {2}, total count: {3}",
new Object[] {message, cookie, bucket.size(), totalCount});
}
}
}
