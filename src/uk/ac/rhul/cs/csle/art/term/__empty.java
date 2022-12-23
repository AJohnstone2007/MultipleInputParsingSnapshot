package uk.ac.rhul.cs.csle.art.term;

public class __empty extends Value {
  // Special type with no payload: use class based equality
  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof __empty;
  }

  @Override
  public String toLiteralString() {
    return "__empty";
  }
}