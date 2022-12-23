package uk.ac.rhul.cs.csle.art.term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class __list extends Value {
  private final LinkedList<Value> value;

  @Override
  public LinkedList<Value> value() {
    return value;
  }

  public __list() {
    this.value = new LinkedList<>();
  }

  public __list(LinkedList<Value> value) {
    this.value = value;
  }

  public __list(int termIndex) {
    value = new LinkedList<>();
    // Now walk the children of termIndex, adding them to the list
    int[] children = iTerms.getTermChildren(termIndex);
    for (int i = 0; i < children.length; i++)
      value.add(iTerms.valueFromTerm(children[i]));
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
    StringBuilder sb = new StringBuilder("__list");
    if (!value.isEmpty()) {
      sb.append("(");

      boolean notFirst = false;
      for (Value v : value) {
        if (notFirst)
          sb.append(",");
        else
          notFirst = true;
        sb.append(v.toLiteralString());
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
  public Value __cat(Value r) {
    if (r instanceof __list)
      for (Value v : ((__list) r).value())
        value.addLast(v);
    else
      value.addLast(r);
    return this;
  }

  @Override
  public Value __put(Value r) {
    value.addLast(r);
    return this;
  }

  @Override
  public Value __get() {
    if (value.isEmpty()) return iTerms.valueEmpty;
    return value.get(0);
  }

  @Override
  public Value __slice() {
    if (value.size() < 2) return iTerms.valueEmpty;

    __list ret = new __list(value);
    ret.value.remove();
    return ret;
  }

  @Override
  public Value __cast__array() {
    return new __array(value.toArray(new Value[0]));
  }

  @Override
  public Value __cast__list() {
    return new __list(new LinkedList<Value>(value));
  }

  @Override
  public Value __cast__flexArray() {
    return new __flexArray(new ArrayList<Value>(value));
  }

  @Override
  public Value __cast__set() {
    return new __set(new HashSet<Value>(value));
  }

  @Override
  public Value __cast__map() {
    Value ret = new __map();
    for (Value v : value)
      ret.__put(v, iTerms.valueEmpty);
    return ret;
  }

  @Override
  public Value __cast__mapChain() {
    Value ret = new __mapChain();
    for (Value v : value)
      ret.__put(v, iTerms.valueEmpty);
    return ret;
  }
}