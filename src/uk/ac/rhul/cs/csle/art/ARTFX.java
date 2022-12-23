package uk.ac.rhul.cs.csle.art;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.rhul.cs.csle.art.core.ARTCore;
import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;

public class ARTFX extends Application {
  public static void main(final String[] arguments) {
    Application.launch(arguments);
  }

  @Override
  public void start(final Stage stage) throws FileNotFoundException {
    try {
      new ARTCore(getParameters().getRaw().toArray(new String[0]));
    } catch (ARTUncheckedException e) {
      System.out.println("Fatal error: " + e.getMessage());
    }
  }
}
