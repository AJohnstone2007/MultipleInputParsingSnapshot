package uk.ac.rhul.cs.csle.art.v3.alg.earleytable.support;

public class ARTBSRSPPFLabelPair {
  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  ARTBSRSPPFLabel x, y;

  public ARTBSRSPPFLabel getX() {
    return x;
  }

  public ARTBSRSPPFLabel getY() {
    return y;
  }

  public ARTBSRSPPFLabelPair(ARTBSRSPPFLabel x, ARTBSRSPPFLabel y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((x == null) ? 0 : x.hashCode());
    result = prime * result + ((y == null) ? 0 : y.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof ARTBSRSPPFLabelPair)) return false;
    ARTBSRSPPFLabelPair other = (ARTBSRSPPFLabelPair) obj;
    if (x == null) {
      if (other.x != null) return false;
    } else if (!x.equals(other.x)) return false;
    if (y == null) {
      if (other.y != null) return false;
    } else if (!y.equals(other.y)) return false;
    return true;
  }

}
