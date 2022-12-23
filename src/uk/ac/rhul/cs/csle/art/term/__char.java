package uk.ac.rhul.cs.csle.art.term;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;
import uk.ac.rhul.cs.csle.art.util.text.ARTText;

public class __char extends Value {
  private final char value;

  @Override
  public Character value() {
    return value;
  }

  public __char(char value) {
    this.value = value;
  }

  public __char(int termIndex) {
    int[] children = iTerms.getTermChildren(termIndex);
    String child = iTerms.getTermSymbolString(children[0]);
    if (child.charAt(0) != '`') throw new ARTUncheckedException("__char argument must have leading `");
    value = child.charAt(1);
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
    __char other = (__char) obj;
    if (value != other.value) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return "__char(`" + ARTText.toLiteralString(Character.toString(value)) + ")";
  }

  @Override
  public Value __compare(Value r) {
    if (value > ((__char) r).value) return iTerms.valueInt32One;
    if (value < ((__char) r).value) return iTerms.valueInt32MinusOne;
    return iTerms.valueInt32Zero;
  }

  @Override
  public Value __gt(Value r) {
    if (value > ((__char) r).value) return iTerms.valueBoolTrue;
    return iTerms.valueBoolFalse;
  }

  @Override
  public Value __ge(Value r) {
    if (value >= ((__char) r).value) return iTerms.valueBoolTrue;
    return iTerms.valueBoolFalse;
  }

  @Override
  public Value __lt(Value r) {
    if (value < ((__char) r).value) return iTerms.valueBoolTrue;
    return iTerms.valueBoolFalse;
  }

  @Override
  public Value __le(Value r) {
    if (value <= ((__char) r).value) return iTerms.valueBoolTrue;
    return iTerms.valueBoolFalse;
  }
}