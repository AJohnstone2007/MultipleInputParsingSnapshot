package javafx.css.converter;
import com.sun.javafx.util.Logging;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import com.sun.javafx.logging.PlatformLogger;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public final class EnumConverter<E extends Enum<E>> extends StyleConverter<String, E> {
final Class<E> enumClass;
public EnumConverter(Class<E> enumClass) {
this.enumClass = enumClass;
}
@Override
public E convert(ParsedValue<String, E> value, Font not_used) {
if (enumClass == null) {
return null;
}
String string = value.getValue();
final int dotPos = string.lastIndexOf('.');
if (dotPos > -1) {
string = string.substring(dotPos + 1);
}
try {
string = string.replace('-', '_');
return Enum.valueOf(enumClass, string.toUpperCase(Locale.ROOT));
} catch (IllegalArgumentException e) {
return Enum.valueOf(enumClass, string);
}
}
@Override
public void writeBinary(DataOutputStream os, StringStore sstore) throws IOException {
super.writeBinary(os,sstore);
String ename = enumClass.getName();
int index = sstore.addString(ename);
os.writeShort(index);
}
public static StyleConverter<?,?> readBinary(DataInputStream is, String[] strings)
throws IOException {
short index = is.readShort();
String ename = 0 <= index && index <= strings.length ? strings[index] : null;
if (ename == null || ename.isEmpty()) return null;
if (converters == null || converters.containsKey(ename) == false) {
StyleConverter<?,?> converter = getInstance(ename);
if (converter == null) {
final PlatformLogger logger = Logging.getCSSLogger();
if (logger.isLoggable(Level.SEVERE)) {
logger.severe("could not deserialize EnumConverter for " + ename);
}
}
if (converters == null) converters = new HashMap<String,StyleConverter<?,?>>();
converters.put(ename, converter);
return converter;
}
return converters.get(ename);
}
private static Map<String,StyleConverter<?,?>> converters;
static public StyleConverter<?,?> getInstance(final String ename) {
StyleConverter<?,?> converter = null;
switch (ename) {
case "com.sun.javafx.cursor.CursorType" :
converter = new EnumConverter<com.sun.javafx.cursor.CursorType>(com.sun.javafx.cursor.CursorType.class);
break;
case "javafx.scene.layout.BackgroundRepeat" :
case "com.sun.javafx.scene.layout.region.Repeat" :
converter = new EnumConverter<javafx.scene.layout.BackgroundRepeat>(javafx.scene.layout.BackgroundRepeat.class);
break;
case "javafx.geometry.HPos" :
converter = new EnumConverter<javafx.geometry.HPos>(javafx.geometry.HPos.class);
break;
case "javafx.geometry.Orientation" :
converter = new EnumConverter<javafx.geometry.Orientation>(javafx.geometry.Orientation.class);
break;
case "javafx.geometry.Pos" :
converter = new EnumConverter<javafx.geometry.Pos>(javafx.geometry.Pos.class);
break;
case "javafx.geometry.Side" :
converter = new EnumConverter<javafx.geometry.Side>(javafx.geometry.Side.class);
break;
case "javafx.geometry.VPos" :
converter = new EnumConverter<javafx.geometry.VPos>(javafx.geometry.VPos.class);
break;
case "javafx.scene.effect.BlendMode" :
converter = new EnumConverter<javafx.scene.effect.BlendMode>(javafx.scene.effect.BlendMode.class);
break;
case "javafx.scene.effect.BlurType" :
converter = new EnumConverter<javafx.scene.effect.BlurType>(javafx.scene.effect.BlurType.class);
break;
case "javafx.scene.paint.CycleMethod" :
converter = new EnumConverter<javafx.scene.paint.CycleMethod>(javafx.scene.paint.CycleMethod.class);
break;
case "javafx.scene.shape.ArcType" :
converter = new EnumConverter<javafx.scene.shape.ArcType>(javafx.scene.shape.ArcType.class);
break;
case "javafx.scene.shape.StrokeLineCap" :
converter = new EnumConverter<javafx.scene.shape.StrokeLineCap>(javafx.scene.shape.StrokeLineCap.class);
break;
case "javafx.scene.shape.StrokeLineJoin" :
converter = new EnumConverter<javafx.scene.shape.StrokeLineJoin>(javafx.scene.shape.StrokeLineJoin.class);
break;
case "javafx.scene.shape.StrokeType" :
converter = new EnumConverter<javafx.scene.shape.StrokeType>(javafx.scene.shape.StrokeType.class);
break;
case "javafx.scene.text.FontPosture" :
converter = new EnumConverter<javafx.scene.text.FontPosture>(javafx.scene.text.FontPosture.class);
break;
case "javafx.scene.text.FontSmoothingType" :
converter = new EnumConverter<javafx.scene.text.FontSmoothingType>(javafx.scene.text.FontSmoothingType.class);
break;
case "javafx.scene.text.FontWeight" :
converter = new EnumConverter<javafx.scene.text.FontWeight>(javafx.scene.text.FontWeight.class);
break;
case "javafx.scene.text.TextAlignment" :
converter = new EnumConverter<javafx.scene.text.TextAlignment>(javafx.scene.text.TextAlignment.class);
break;
default :
assert false : "EnumConverter<"+ ename + "> not expected";
final PlatformLogger logger = Logging.getCSSLogger();
if (logger.isLoggable(Level.SEVERE)) {
logger.severe("EnumConverter : converter Class is null for : "+ename);
}
break;
}
return converter;
}
@Override
public boolean equals(Object other) {
if (other == this) return true;
if (other == null || !(other instanceof EnumConverter)) return false;
return (enumClass.equals(((EnumConverter)other).enumClass));
}
@Override
public int hashCode() {
return enumClass.hashCode();
}
@Override
public String toString() {
return "EnumConverter[" + enumClass.getName() + "]";
}
}
