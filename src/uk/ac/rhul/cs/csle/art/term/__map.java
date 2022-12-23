package uk.ac.rhul.cs.csle.art.term;

import java.util.HashMap;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class __map extends Value {
  private final HashMap<Value, Value> value;

  @Override
  public HashMap<Value, Value> value() {
    return value;
  }

  public __map() {
    this.value = new HashMap<>();
  }

  public __map(HashMap<Value, Value> value) {
    this.value = value;
  }

  public __map(int termIndex) {
    value = new HashMap<>();
    // Now walk the children of termIndex, adding them to the list
    int[] children = iTerms.getTermChildren(termIndex);
    for (int i = 0; i < children.length; i++) {
      if (!iTerms.getTermSymbolString(children[i]).equals("__binding"))
        throw new ARTUncheckedException("Argument of __map must be a __binding " + iTerms.toString(termIndex));
      int[] grandChildren = iTerms.getTermChildren(children[i]);
      if (grandChildren.length != 2) throw new ARTUncheckedException("Type __binding must have arity " + i + ": " + iTerms.toString(termIndex));
      value.put(iTerms.valueFromTerm(grandChildren[0]), iTerms.valueFromTerm(grandChildren[1]));
    }
  }

  // Collection type with mutable payload: each instance must be unique
  @Override
  public int hashCode() {
    return System.identityHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj;
  }

  @Override
  public String toLiteralString() {
    StringBuilder sb = new StringBuilder("__map");

    if (!value.isEmpty()) {
      sb.append("(");

      boolean notFirst = false;
      for (Value v : value.keySet()) {
        if (notFirst)
          sb.append(",");
        else
          notFirst = true;
        sb.append("__binding(" + v.toLiteralString() + ", " + value.get(v).toLiteralString() + ")");
      }
      sb.append(")");
    }
    return sb.toString();
  }

  @Override
  public Value __size() {
    return new __int32(value.size(), 0);
  }

  @Override
  public Value __put(Value r, Value rr) {
    value.put(r, rr);
    return this;
  }

  @Override
  public Value __get(Value r) {
    if (value.containsKey(r)) return value.get(r);

    return iTerms.valueEmpty;
  }

  @Override
  public Value __contains(Value r) {
    if (value.containsKey(r)) return iTerms.valueBoolTrue;

    return iTerms.valueBoolFalse;
  }

  @Override
  public Value __remove(Value r) {
    value.remove(r);
    return this;
  }

  @Override
  public Value __extract() {
    if (value.size() == 0) return iTerms.valueEmpty;
    Value key = value.keySet().iterator().next();
    Value keyValue = value.get(key);
    value.remove(key);
    return new __binding(key, keyValue);
  }

  @Override
  public Value __cast__map() {
    return new __map(new HashMap<Value, Value>(value));
  }

  @Override
  public Value __cast__mapChain() {
    return new __mapChain(new HashMap<Value, Value>(value));
  }
}