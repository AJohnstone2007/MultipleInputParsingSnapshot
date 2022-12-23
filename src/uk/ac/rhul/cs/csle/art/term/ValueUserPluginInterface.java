package uk.ac.rhul.cs.csle.art.term;

public interface ValueUserPluginInterface {
  String name();

  Value user(Value... args) throws ValueException;
}
