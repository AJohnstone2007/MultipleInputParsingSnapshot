package uk.ac.rhul.cs.csle.art.term;

import java.math.BigDecimal;
import java.math.BigInteger;

public class __intAP extends Value {
  private final BigInteger value;

  @Override
  public BigInteger value() {
    return value;
  }

  public __intAP(int termIndex) {
    value = null;
  }

  public __intAP(BigInteger value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toLiteralString() {
    return "__intAP(" + value + ")";
  }

  @Override
  public Value __gt(Value r) {
    return new __bool(value.compareTo(((__intAP) r).value()) > 0);
  }

  @Override
  public Value __ge(Value r) {
    return new __bool(value.compareTo(((__intAP) r).value()) >= 0);
  }

  @Override
  public Value __lt(Value r) {
    return new __bool(value.compareTo(((__intAP) r).value()) < 0);
  }

  @Override
  public Value __le(Value r) {
    return new __bool(value.compareTo(((__intAP) r).value()) <= 0);
  }

  @Override
  public Value __not() {
    return new __intAP(value.not());
  }

  @Override
  public Value __and(Value r) {
    return new __intAP(value.and(((__intAP) r).value));
  }

  @Override
  public Value __or(Value r) {
    return new __intAP(value.or(((__intAP) r).value));
  }

  @Override
  public Value __xor(Value r) {
    return new __intAP(value.xor(((__intAP) r).value));
  }

  @Override
  public Value __cnd(Value r) {
    return new __intAP(value.not().or(((__intAP) r).value));
  }

  @Override
  public Value __lsh(Value r) {
    return new __intAP(value.shiftLeft(((__int32) r).value()));
  }

  @Override
  public Value __ash(Value r) {
    return new __intAP(value.shiftRight(((__int32) r).value()));
  }

  @Override
  public Value __neg() {
    return new __intAP(value.negate());
  }

  @Override
  public Value __add(Value r) {
    return new __intAP(value.add(((__intAP) r).value));
  }

  @Override
  public Value __sub(Value r) {
    return new __intAP(value.subtract(((__intAP) r).value));
  }

  @Override
  public Value __mul(Value r) {
    return new __intAP(value.multiply(((__intAP) r).value));
  }

  @Override
  public Value __div(Value r) {
    return new __intAP(value.divide(((__intAP) r).value));
  }

  @Override
  public Value __mod(Value r) {
    return new __intAP(value.mod(((__intAP) r).value));
  }

  @Override
  public Value __exp(Value r) {
    return new __intAP(value.pow(((__int32) r).value()));
  }

  @Override
  public Value __cast__bool() {
    return (value.compareTo(BigInteger.ZERO) == 0) ? iTerms.valueBoolFalse : iTerms.valueBoolTrue;
  }

  @Override
  public Value __cast__intAP() {
    return new __intAP(value);
  }

  @Override
  public Value __cast__int32() {
    return new __int32(value.intValue(), 0);
  }

  @Override
  public Value __cast__realAP() {
    return new __realAP(new BigDecimal(value));
  }

  @Override
  public Value __cast__real64() {
    return new __real64(value.doubleValue());
  }
}