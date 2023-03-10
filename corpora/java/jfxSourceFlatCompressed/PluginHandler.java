package com.sun.webkit.plugin;
import java.net.URL;
interface PluginHandler {
String getName();
String getFileName();
String getDescription();
String[] supportedMIMETypes();
boolean isSupportedMIMEType(String mimeType);
boolean isSupportedPlatform();
Plugin createPlugin(URL url, String mimeType,
String[] pNames, String[] pValues);
}
