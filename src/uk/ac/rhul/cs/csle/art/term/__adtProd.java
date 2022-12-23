package uk.ac.rhul.cs.csle.art.term;

public class __adtProd extends Value {
  private final int value;

  @Override
  public Integer value() {
    return value;
  }

  public __adtProd(int termIndex) {
    this.value = termIndex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + value;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    __adtProd other = (__adtProd) obj;
    if (value != other.value) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return "__adtProd(" + value + ")";
  }
}