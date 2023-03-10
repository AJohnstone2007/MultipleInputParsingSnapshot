package com.sun.javafx.scene;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
public class ParentHelper extends NodeHelper {
private static final ParentHelper theInstance;
private static ParentAccessor parentAccessor;
static {
theInstance = new ParentHelper();
Utils.forceInit(Parent.class);
}
private static ParentHelper getInstance() {
return theInstance;
}
public static void initHelper(Parent parent) {
setHelper(parent, getInstance());
}
public static void superProcessCSS(Node node) {
((ParentHelper) getHelper(node)).superProcessCSSImpl(node);
}
public static List<String> getAllParentStylesheets(Parent parent) {
return ((ParentHelper) getHelper(parent)).getAllParentStylesheetsImpl(parent);
}
@Override
protected NGNode createPeerImpl(Node node) {
return parentAccessor.doCreatePeer(node);
}
@Override
protected void updatePeerImpl(Node node) {
super.updatePeerImpl(node);
parentAccessor.doUpdatePeer(node);
}
@Override
protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds bounds,
BaseTransform tx) {
return parentAccessor.doComputeGeomBounds(node, bounds, tx);
}
@Override
protected boolean computeContainsImpl(Node node, double localX, double localY) {
return parentAccessor.doComputeContains(node, localX, localY);
}
void superProcessCSSImpl(Node node) {
super.processCSSImpl(node);
}
@Override
protected void processCSSImpl(Node node) {
parentAccessor.doProcessCSS(node);
}
protected List<String> getAllParentStylesheetsImpl(Parent parent) {
return parentAccessor.doGetAllParentStylesheets(parent);
}
@Override
protected void pickNodeLocalImpl(Node node, PickRay localPickRay,
PickResultChooser result) {
parentAccessor.doPickNodeLocal(node, localPickRay, result);
}
public static boolean pickChildrenNode(Parent parent, PickRay pickRay,
PickResultChooser result) {
return parentAccessor.pickChildrenNode(parent, pickRay, result);
}
public static void setTraversalEngine(Parent parent, ParentTraversalEngine value) {
parentAccessor.setTraversalEngine(parent, value);
}
public static ParentTraversalEngine getTraversalEngine(Parent parent) {
return parentAccessor.getTraversalEngine(parent);
}
public static void setParentAccessor(final ParentAccessor newAccessor) {
if (parentAccessor != null) {
throw new IllegalStateException();
}
parentAccessor = newAccessor;
}
public interface ParentAccessor {
NGNode doCreatePeer(Node node);
void doUpdatePeer(Node node);
boolean doComputeContains(Node node, double localX, double localY);
BaseBounds doComputeGeomBounds(Node node, BaseBounds bounds, BaseTransform tx);
void doProcessCSS(Node node);
void doPickNodeLocal(Node node, PickRay localPickRay, PickResultChooser result);
boolean pickChildrenNode(Parent parent, PickRay pickRay, PickResultChooser result);
void setTraversalEngine(Parent parent, ParentTraversalEngine value);
ParentTraversalEngine getTraversalEngine(Parent parent);
List<String> doGetAllParentStylesheets(Parent parent);
}
}
