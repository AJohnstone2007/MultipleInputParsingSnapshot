package com.sun.webkit;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Set;
public abstract class Utilities {
private static Utilities instance;
public static synchronized void setUtilities(Utilities util) {
instance = util;
}
public static synchronized Utilities getUtilities() {
return instance;
}
protected abstract Pasteboard createPasteboard();
protected abstract PopupMenu createPopupMenu();
protected abstract ContextMenu createContextMenu();
private static final Set<String> CLASS_METHODS_ALLOW_LIST = Set.of(
"getCanonicalName",
"getEnumConstants",
"getFields",
"getMethods",
"getName",
"getPackageName",
"getSimpleName",
"getSuperclass",
"getTypeName",
"getTypeParameters",
"isAssignableFrom",
"isArray",
"isEnum",
"isInstance",
"isInterface",
"isLocalClass",
"isMemberClass",
"isPrimitive",
"isSynthetic",
"toGenericString",
"toString"
);
private static final Set<String> CLASSES_REJECT_LIST = Set.of(
"java.lang.ClassLoader",
"java.lang.Module",
"java.lang.Runtime",
"java.lang.System"
);
private static final List<String> PACKAGES_REJECT_LIST = List.of(
"java.lang.invoke",
"java.lang.module",
"java.lang.reflect",
"java.security",
"sun.misc"
);
@SuppressWarnings("removal")
private static Object fwkInvokeWithContext(final Method method,
final Object instance,
final Object[] args,
AccessControlContext acc)
throws Throwable {
final Class<?> clazz = method.getDeclaringClass();
if (clazz.equals(java.lang.Class.class)) {
if (!CLASS_METHODS_ALLOW_LIST.contains(method.getName())) {
throw new UnsupportedOperationException("invocation not supported");
}
} else {
final String className = clazz.getName();
if (CLASSES_REJECT_LIST.contains(className)) {
throw new UnsupportedOperationException("invocation not supported");
}
PACKAGES_REJECT_LIST.forEach(packageName -> {
if (className.startsWith(packageName + ".")) {
throw new UnsupportedOperationException("invocation not supported");
}
});
}
try {
return AccessController.doPrivileged((PrivilegedExceptionAction<Object>)
() -> MethodHelper.invoke(method, instance, args), acc);
} catch (PrivilegedActionException ex) {
Throwable cause = ex.getCause();
if (cause == null)
cause = ex;
else if (cause instanceof InvocationTargetException
&& cause.getCause() != null)
cause = cause.getCause();
throw cause;
}
}
}
