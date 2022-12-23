package uk.ac.rhul.cs.csle.art.core;
public class ARTVersion {
  public static int major() {return 4;}
  public static int minor() {return 0;}
  public static int build() {return 314;}
  public static String status() {return "AMBER";}
  public static String version() { return major()+"."+minor()+"."+build()+"."+status(); };
}
