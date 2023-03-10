package com.sun.javafx.iio;
import java.io.IOException;
import java.io.InputStream;
public interface ImageLoaderFactory {
ImageFormatDescription getFormatDescription();
ImageLoader createImageLoader(InputStream input) throws IOException;
}
