package uk.ac.rhul.cs.csle.art.term;

public class __binding extends Value {
  private final Value key, value; // term numbers for left and right hand side of binding

  public Value key() {
    return key;
  }

  @Override
  public Value value() {
    return value;
  }

  public __binding(int termIndex) {
    int[] children = iTerms.getTermChildren(termIndex);
    // TODO: extract binings from term tree
    key = iTerms.valueFromTerm(children[0]);
    value = iTerms.valueFromTerm(children[1]);
  }

  public __binding(Value key, Value value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    __binding other = (__binding) obj;
    if (key == null) {
      if (other.key != null) return false;
    } else if (!key.equals(other.key)) return false;
    if (value == null) {
      if (other.value != null) return false;
    } else if (!value.equals(other.value)) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return "__binding(" + key.toLiteralString() + ", " + value.toLiteralString() + ")";
  }
}