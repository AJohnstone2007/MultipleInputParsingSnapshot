package uk.ac.rhul.cs.csle.art.term;

public class __bool extends Value {
  private final boolean value;

  @Override
  public Boolean value() {
    return value;
  }

  public __bool(Boolean b) {
    value = b;
  }

  public __bool(int termIndex) {
    int[] children = iTerms.getTermChildren(termIndex);
    String child = iTerms.getTermSymbolString(children[0]);
    if (child.equals("True"))
      value = true;
    else
      value = false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (value ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    __bool other = (__bool) obj;
    if (value != other.value) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return value ? "__bool(True)" : "__bool(False)";
  }

  @Override
  public Value __not() {
    return new __bool(!value);
  }

  @Override
  public Value __or(Value r) {
    return new __bool(value | ((__bool) r).value);
  }

  @Override
  public Value __and(Value r) {
    return new __bool(value & ((__bool) r).value);
  }

  @Override
  public Value __xor(Value r) {
    return new __bool(value ^ ((__bool) r).value);
  }

  @Override
  public Value __cnd(Value r) {
    return new __bool(!value | ((__bool) r).value);
  }
}