package com.sun.javafx.sg.prism;
public class NGBox extends NGShape3D {
public void updateMesh(NGTriangleMesh mesh) {
this.mesh = mesh;
invalidate();
}
}
