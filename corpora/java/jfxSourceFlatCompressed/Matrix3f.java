package com.sun.javafx.geom;
public class Matrix3f {
public float m00;
public float m01;
public float m02;
public float m10;
public float m11;
public float m12;
public float m20;
public float m21;
public float m22;
public Matrix3f(float m00, float m01, float m02,
float m10, float m11, float m12,
float m20, float m21, float m22) {
this.m00 = m00;
this.m01 = m01;
this.m02 = m02;
this.m10 = m10;
this.m11 = m11;
this.m12 = m12;
this.m20 = m20;
this.m21 = m21;
this.m22 = m22;
}
public Matrix3f(float[] v) {
this.m00 = v[0];
this.m01 = v[1];
this.m02 = v[2];
this.m10 = v[3];
this.m11 = v[4];
this.m12 = v[5];
this.m20 = v[6];
this.m21 = v[7];
this.m22 = v[8];
}
public Matrix3f(Vec3f[] v) {
this.m00 = v[0].x;
this.m01 = v[0].y;
this.m02 = v[0].z;
this.m10 = v[1].x;
this.m11 = v[1].x;
this.m12 = v[1].x;
this.m20 = v[2].x;
this.m21 = v[2].x;
this.m22 = v[2].x;
}
public Matrix3f(Matrix3f m1) {
this.m00 = m1.m00;
this.m01 = m1.m01;
this.m02 = m1.m02;
this.m10 = m1.m10;
this.m11 = m1.m11;
this.m12 = m1.m12;
this.m20 = m1.m20;
this.m21 = m1.m21;
this.m22 = m1.m22;
}
public Matrix3f() {
this.m00 = (float) 1.0;
this.m01 = (float) 0.0;
this.m02 = (float) 0.0;
this.m10 = (float) 0.0;
this.m11 = (float) 1.0;
this.m12 = (float) 0.0;
this.m20 = (float) 0.0;
this.m21 = (float) 0.0;
this.m22 = (float) 1.0;
}
@Override
public String toString() {
return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n"
+ this.m10 + ", " + this.m11 + ", " + this.m12 + "\n"
+ this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
}
public final void setIdentity() {
this.m00 = (float) 1.0;
this.m01 = (float) 0.0;
this.m02 = (float) 0.0;
this.m10 = (float) 0.0;
this.m11 = (float) 1.0;
this.m12 = (float) 0.0;
this.m20 = (float) 0.0;
this.m21 = (float) 0.0;
this.m22 = (float) 1.0;
}
public final void setRow(int row, float[] v) {
switch (row) {
case 0:
this.m00 = v[0];
this.m01 = v[1];
this.m02 = v[2];
break;
case 1:
this.m10 = v[0];
this.m11 = v[1];
this.m12 = v[2];
break;
case 2:
this.m20 = v[0];
this.m21 = v[1];
this.m22 = v[2];
break;
default:
throw new ArrayIndexOutOfBoundsException("Matrix3f");
}
}
public final void setRow(int row, Vec3f v) {
switch (row) {
case 0:
this.m00 = v.x;
this.m01 = v.y;
this.m02 = v.z;
break;
case 1:
this.m10 = v.x;
this.m11 = v.y;
this.m12 = v.z;
break;
case 2:
this.m20 = v.x;
this.m21 = v.y;
this.m22 = v.z;
break;
default:
throw new ArrayIndexOutOfBoundsException("Matrix3f");
}
}
public final void getRow(int row, Vec3f v) {
if (row == 0) {
v.x = m00;
v.y = m01;
v.z = m02;
} else if (row == 1) {
v.x = m10;
v.y = m11;
v.z = m12;
} else if (row == 2) {
v.x = m20;
v.y = m21;
v.z = m22;
} else {
throw new ArrayIndexOutOfBoundsException("Matrix3f");
}
}
public final void getRow(int row, float[] v) {
if (row == 0) {
v[0] = m00;
v[1] = m01;
v[2] = m02;
} else if (row == 1) {
v[0] = m10;
v[1] = m11;
v[2] = m12;
} else if (row == 2) {
v[0] = m20;
v[1] = m21;
v[2] = m22;
} else {
throw new ArrayIndexOutOfBoundsException("Matrix3f");
}
}
}
