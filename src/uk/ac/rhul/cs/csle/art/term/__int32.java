package uk.ac.rhul.cs.csle.art.term;

import java.math.BigDecimal;
import java.math.BigInteger;

public class __int32 extends Value {

  public final int value;

  @Override
  public Integer value() {
    return value;
  }

  public __int32(int value, int dummyIgnored) {
    this.value = value;
  }

  public __int32(Integer value) {
    this.value = value;
  }

  public __int32(int termIndex) {
    String child = iTerms.getTermSymbolString(iTerms.getTermChildren(termIndex)[0]);
    value = Integer.parseInt(child);
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
    __int32 other = (__int32) obj;
    if (value != other.value) return false;
    return true;
  }

  @Override
  public String toLiteralString() {
    return "__int32(" + value + ")";
  }

  @Override
  public Value __gt(Value r) {
    return new __bool(value > ((__int32) r).value);
  }

  @Override
  public Value __ge(Value r) {
    return new __bool(value >= ((__int32) r).value);
  }

  @Override
  public Value __lt(Value r) {
    return new __bool(value < ((__int32) r).value);
  }

  @Override
  public Value __le(Value r) {
    return new __bool(value <= ((__int32) r).value);
  }

  @Override
  public Value __not() {
    return new __int32(~value, 0);
  }

  @Override
  public Value __and(Value r) {
    return new __int32(value & ((__int32) r).value, 0);
  }

  @Override
  public Value __or(Value r) {
    return new __int32(value | ((__int32) r).value, 0);
  }

  @Override
  public Value __xor(Value r) {
    return new __int32(value ^ ((__int32) r).value, 0);
  }

  @Override
  public Value __cnd(Value r) {
    return new __int32(~value | ((__int32) r).value, 0);
  }

  @Override
  public Value __lsh(Value r) {
    return new __int32(value << ((__int32) r).value, 0);
  }

  @Override
  public Value __rsh(Value r) {
    return new __int32(value >>> ((__int32) r).value, 0);
  }

  @Override
  public Value __ash(Value r) {
    return new __int32(value >> ((__int32) r).value, 0);
  }

  @Override
  public Value __rol(Value r) {
    int lsb = value < 0 ? 1 : 0;
    return new __int32((value << ((__int32) r).value) | lsb, 0);
  }

  @Override
  public Value __ror(Value r) {
    int msb = value << 31;
    return new __int32((value >> ((__int32) r).value) | msb, 0);
  }

  @Override
  public Value __neg() {
    return new __int32(-value, 0);
  }

  @Override
  public Value __add(Value r) {
    return new __int32(value + ((__int32) r).value, 0);
  }

  @Override
  public Value __sub(Value r) {
    return new __int32(value - ((__int32) r).value, 0);
  }

  @Override
  public Value __mul(Value r) {
    return new __int32(value * ((__int32) r).value, 0);
  }

  @Override
  public Value __div(Value r) {
    return new __int32(value / ((__int32) r).value, 0);
  }

  @Override
  public Value __mod(Value r) {
    return new __int32(value % ((__int32) r).value, 0);
  }

  @Override
  public Value __exp(Value r) {
    int ret = value, ri = ((__int32) r).value;
    if (ri < 0) return iTerms.valueInt32Zero;
    if (ri == 0) return iTerms.valueInt32One;
    while (ri-- > 1)
      ret *= value;
    return new __int32(ret, 0);
  }

  @Override
  public Value __cast__bool() {
    return (value == 0) ? iTerms.valueBoolFalse : iTerms.valueBoolTrue;
  }

  @Override
  public Value __cast__char() {
    return new __char((char) value);
  }

  @Override
  public Value __cast__intAP() {
    return new __intAP(BigInteger.valueOf(value));
  }

  @Override
  public Value __cast__int32() {
    return new __int32(value, 0);
  }

  @Override
  public Value __cast__realAP() {
    return new __realAP(new BigDecimal(value));
  }

  @Override
  public Value __cast__real64() {
    return new __real64((double) value);
  }
}