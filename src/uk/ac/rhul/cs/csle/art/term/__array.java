package uk.ac.rhul.cs.csle.art.term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class __array extends Value {
  private final Value[] value;

  @Override
  public Value[] value() {
    return value;
  }

  public __array(int dummy, int length) {
    this.value = new Value[length];
    for (int i = 0; i < value.length; i++)
      value[i] = iTerms.valueEmpty;
  }

  public __array(Value[] value) {
    this.value = value;
  }

  public __array(int termIndex) {
    // System.out.println("__array constructor called with termIndex " + termIndex + ": " + iTerms.toString(termIndex));
    int[] children = iTerms.getTermChildren(termIndex);
    this.value = new Value[(int) iTerms.valueFromTerm(children[0]).value()];

    // Now walk the children of termIndex, adding them to the array
    for (int i = 1; i < children.length; i++)
      value[i - 1] = iTerms.valueFromTerm(children[i]);

    // Now walk the remaining new children, setting them to __empty
    for (int i = children.length - 1; i < value.length; i++)
      value[i] = iTerms.valueEmpty;
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
    StringBuilder sb = new StringBuilder("__array(__int32(" + value.length + ")");
    for (Value v : value)
      sb.append(", " + v.toLiteralString());
    sb.append(")");
    return sb.toString();
  }

  @Override
  public Value __size() {
    return new __int32(value.length, 0);
  }

  @Override
  public Value __put(Value r, Value rr) {
    __int32 ri = (__int32) r;
    if (ri.value() >= value.length) throw new ARTUncheckedException("__array access out of bounds: __put at index " + ri + " with bounds 0.." + value.length);
    value[((__int32) r).value()] = rr;
    return this;
  }

  @Override
  public Value __get(Value r) {
    return value[((__int32) r).value()];
  }

  private Value slice(int lo, int hi) {
    if (hi < lo) throw new ARTUncheckedException("__slice operation on __array type has hi bound lower than lo bound");
    Value[] retValue = new Value[hi - lo];

    for (int i = lo; i < hi; i++)
      retValue[i - lo] = value[i];

    return new __array(retValue);
  }

  @Override
  public Value __slice(Value r, Value rr) {
    return slice(((__int32) r).value(), ((__int32) rr).value());
  }

  @Override
  public Value __cast__array() {
    return new __array(value);
  }

  @Override
  public Value __cast__list() {
    return new __list((LinkedList<Value>) Arrays.asList(value));
  }

  @Override
  public Value __cast__flexArray() {
    return new __flexArray(new ArrayList<Value>(Arrays.asList(value)));
  }

  @Override
  public Value __cast__set() {
    return new __set(new HashSet<Value>(Arrays.asList(value)));
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