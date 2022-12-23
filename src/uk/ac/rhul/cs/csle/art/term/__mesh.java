package uk.ac.rhul.cs.csle.art.term;

import uk.ac.rhul.cs.csle.art.util.graphics.ARTSTLMesh;

public class __mesh extends Value {
  ARTSTLMesh value;

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
  public ARTSTLMesh value() {
    return value;
  }

  @Override
  public String toLiteralString() {
    return ("__mesh(???)");
  }
}
