import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.rhul.cs.csle.art.util.graphics.ARTSTLASCIIParser;

public class MeshViewerDemo extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    String filename = "stl/02.stl";
    float offset = 00;
    float scale = 20;
    // float scale = 1;
    new MeshViewer(new ARTSTLASCIIParser(filename), 0, 0, 0, 1);
    // new MeshViewer(new ARTSTLASCIIParser(filename), 0, 0, 0, scale);
  }
}
