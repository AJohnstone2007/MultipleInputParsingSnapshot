package uk.ac.rhul.cs.csle.art.term;

import java.io.PrintStream;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class __output extends Value {
  private final PrintStream value;

  public __output(PrintStream stream) {
    this.value = stream;
  }

  @Override
  public Object value() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    __output other = (__output) obj;
    if (value == null) {
      if (other.value != null) return false;
    } else if (!value.equals(other.value)) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return ("__output(" + value + ")");
  }

  @Override
  public Value __put(Value r) {
    if (!(value instanceof PrintStream)) throw new ARTUncheckedException("Value type __channel invalid operation __put(_,_) - wrong stream kind ");
    value.print(r.toLiteralString());
    return this;
  }
}
