package uk.ac.rhul.cs.csle.art.v3.alg.cnp.linkedapi;

import uk.ac.rhul.cs.csle.art.v3.manager.grammar.instance.ARTGrammarInstanceSlot;

// This descriptor contains both grammar slot and integer slot fields for use with the CNP interpreter (which uses grammar slots) or the CNP generated parsers (which use integer slots

public class ARTCNPDescriptor {
  @Override
  public String toString() {
    return "(" + slot.toGrammarString(".") + ", " + i + ", " + k + ")";
  }

  private final ARTGrammarInstanceSlot slot;
  private final int k;
  private final int i;

  public ARTGrammarInstanceSlot getSlot() {
    return slot;
  }

  public int getI() {
    return i;
  }

  public int getK() {
    return k;
  }

  public ARTCNPDescriptor(ARTGrammarInstanceSlot slot, int k, int i) {
    this.slot = slot;
    this.k = k;
    this.i = i;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + i;
    result = prime * result + k;
    result = prime * result + ((slot == null) ? 0 : slot.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof ARTCNPDescriptor)) return false;
    ARTCNPDescriptor other = (ARTCNPDescriptor) obj;
    if (i != other.i) return false;
    if (k != other.k) return false;
    if (slot == null) {
      if (other.slot != null) return false;
    } else if (!slot.equals(other.slot)) return false;
    return true;
  }

}
