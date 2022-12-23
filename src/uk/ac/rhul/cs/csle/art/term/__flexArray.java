package uk.ac.rhul.cs.csle.art.term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class __flexArray extends Value {
  private final ArrayList<Value> value;

  @Override
  public ArrayList<Value> value() {
    return value;
  }

  public __flexArray() {
    this.value = new ArrayList<>();
  }

  public __flexArray(ArrayList<Value> value) {
    this.value = value;
  }

  public __flexArray(int termIndex) {
    this.value = new ArrayList<>();
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
    StringBuilder sb = new StringBuilder("__flexArray");
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
  public Value __put(Value r, Value rr) {
    value.set(((__int32) r).value(), rr);
    return this;
  }

  @Override
  public Value __get(Value r) {
    return value.get(((__int32) r).value());
  }

  @Override
  public Value __cat(Value r) {
    value.addAll(((__flexArray) r).value);
    return this;
  }

  private Value slice(int lo, int hi) {
    ArrayList<Value> ret = new ArrayList<>();

    for (int i = lo; i < hi; i++) {
      ret.add(value.get(i));
    }

    return new __flexArray(ret);
  }

  @Override
  public Value __slice(Value r, Value rr) {
    return slice(((__int32) r).value(), ((__int32) rr).value());
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