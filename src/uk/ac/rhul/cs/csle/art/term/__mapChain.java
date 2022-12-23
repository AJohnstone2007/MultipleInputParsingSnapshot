package uk.ac.rhul.cs.csle.art.term;

import java.util.HashMap;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class __mapChain extends Value {

  private final __mapChain parent;
  private HashMap<Value, Value> value;

  public __mapChain getPayload2() {
    return parent;
  }

  @Override
  public HashMap<Value, Value> value() {
    return value;
  }

  public __mapChain() {
    value = new HashMap<>();
    this.parent = null;
  }

  public __mapChain(__mapChain parent) {
    value = new HashMap<>();
    this.parent = parent;
  }

  public __mapChain(HashMap<Value, Value> value) {
    value = new HashMap<Value, Value>();
    parent = null;
  }

  public __mapChain(HashMap<Value, Value> value, __mapChain parent) {
    value = new HashMap<Value, Value>();
    this.parent = parent;
  }

  public __mapChain(int termIndex) {
    parent = null; // More work needed here - define the external and internal syntax of __mapChain
    value = new HashMap<>();
    // Now walk the children of termIndex, adding them to the list
    int[] children = iTerms.getTermChildren(termIndex);
    if (iTerms.getTermSymbolIndex(children[0]) != iTerms.__mapChainStringIndex && iTerms.getTermSymbolIndex(children[0]) != iTerms.__emptyStringIndex)
      throw new ARTUncheckedException("First argument of __mapChain muct be of type __mapChain or __empty: " + toString());

    for (int i = 1; i < children.length; i++) {
      if (!iTerms.getTermSymbolString(children[i]).equals("__binding"))
        throw new ARTUncheckedException("Argument of __map must be a __binding " + iTerms.toString(termIndex));
      int[] grandChildren = iTerms.getTermChildren(children[i]);
      if (grandChildren.length != 2) throw new ARTUncheckedException("Type __binding must have arity " + i + ": " + iTerms.toString(termIndex));
      value.put(iTerms.valueFromTerm(grandChildren[0]), iTerms.valueFromTerm(grandChildren[1]));
    }
  }

  private void toCanonicalStringRec(StringBuilder sb, boolean formatted) {
    if (parent == null)
      sb.append("__empty");
    else
      sb.append(parent.toString());
    for (Value v : value.keySet()) {
      sb.append(",");
      if (formatted) sb.append("\n");
      if (formatted)
        sb.append(v.toLiteralString() + "->" + value.get(v).toLiteralString());
      else
        sb.append("__binding(" + v.toLiteralString() + ", " + value.get(v).toLiteralString() + ")");
    }
  }

  @Override
  public String toLiteralString() {
    StringBuilder sb = new StringBuilder("__mapChain(");
    toCanonicalStringRec(sb, false);
    sb.append(")");
    return sb.toString();
  }

  @Override
  public Value __size() {
    return new __int32(value.size() + (parent != null ? ((__int32) parent.__size()).value() : 0), 0);
  }

  @Override
  public Value __put(Value r, Value rr) {
    value.put(r, rr);
    return this;
  }

  @Override
  public Value __get(Value r) {

    if (value.containsKey(r))
      return value.get(r);
    else if (parent != null) return parent.__get(r);

    return iTerms.valueEmpty;
  }

  @Override
  public Value __contains(Value r) {
    if (value.containsKey(r))
      return iTerms.valueBoolTrue;
    else if (parent != null) return parent.__contains(r);

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