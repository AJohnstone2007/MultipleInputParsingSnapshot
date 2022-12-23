package uk.ac.rhul.cs.csle.art;

import java.io.FileNotFoundException;

import uk.ac.rhul.cs.csle.art.core.ARTCore;
import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class ART {
  public static void main(final String[] args) throws FileNotFoundException {
    try {
      new ARTCore(args);
    } catch (ARTUncheckedException e) {
      System.err.println("Fatal error: " + e.getMessage());
    }
  }
}
