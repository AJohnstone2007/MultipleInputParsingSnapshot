package com.sun.webkit.network.data;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
public final class Handler extends URLStreamHandler {
public Handler() {
}
@Override
protected URLConnection openConnection(URL url) throws IOException {
return new DataURLConnection(url);
}
}
