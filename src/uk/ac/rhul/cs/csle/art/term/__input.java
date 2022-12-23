package uk.ac.rhul.cs.csle.art.term;

import java.io.InputStream;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class __input extends Value {
  private final InputStream value;

  public __input(InputStream stream) {
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
    __input other = (__input) obj;
    if (value == null) {
      if (other.value != null) return false;
    } else if (!value.equals(other.value)) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return ("__input(" + value + ")");
  }

  @Override
  public Value __get(Value r) {
    if (!(value instanceof InputStream)) throw new ARTUncheckedException("Value type __channel invalid operation __get(_,_) - wrong stream kind ");
    // need a parse for Value instances here
    return this;
  }

}
