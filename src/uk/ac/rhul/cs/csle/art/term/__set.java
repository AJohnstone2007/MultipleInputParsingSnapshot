package uk.ac.rhul.cs.csle.art.term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class __set extends Value {
  private final HashSet<Value> value;

  @Override
  public HashSet<Value> value() {
    return value;
  }

  public __set() {
    this.value = new HashSet<>();
  }

  public __set(HashSet<Value> value) {
    this.value = value;
  }

  public __set(int termIndex) {
    value = new HashSet<>();
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
    StringBuilder sb = new StringBuilder("__set");
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
  public Value __put(Value r) {
    value.add(r);
    return this;
  }

  @Override
  public Value __contains(Value r) {
    return value.contains(r) ? iTerms.valueBoolTrue : iTerms.valueBoolFalse;
  }

  @Override
  public Value __remove(Value r) {
    value.remove(r);
    return this;
  }

  @Override
  public Value __extract() {
    Value key = value.iterator().next();
    value.remove(key);
    return key;
  }

  @Override
  public Value __union(Value r) {
    value.addAll(((__set) r).value());
    return this;
  }

  @Override
  public Value __intersection(Value r) {
    value.retainAll(((__set) r).value());
    return this;
  }

  @Override
  public Value __difference(Value r) {
    value.removeAll(((__set) r).value());
    return this;
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