import java.awt.Color;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
public class DragDropFromSwingComponentInSwingNodeTest extends Application {
private DragSource dragSource;
private DropTarget dropTarget;
private DropTargetListener dropListener;
private DragGestureListener dgListener;
private DragGestureRecognizer dragRecognizer;
public static void main(String[] args) {
Application.launch(args);
}
@Override
public void start(Stage stage) {
final SwingNode swingNode = new SwingNode();
createSwingContent(swingNode);
BorderPane pane = new BorderPane();
pane.setCenter(swingNode);
stage.setTitle("Swing in JavaFX");
Button passButton = new Button("Pass");
Button failButton = new Button("Fail");
passButton.setOnAction(e -> this.quit());
failButton.setOnAction(e -> {
this.quit();
throw new AssertionError("Drag / drop from a Swing component in a SwingNode not working");
});
VBox rootNode = new VBox(6,
new Label("1. This is a test for drag / drop from a Swing component in a SwingNode."),
new Label("2. Drag JLabel \"Drag Me!\" text and drop into console."),
new Label("3. When the content is dropped into console, if it prints \"some string data\", click on Pass or else click on Fail"),
new Label(""),
new HBox(10, passButton, failButton), pane);
stage.setScene(new Scene(rootNode, 600, 250));
stage.onCloseRequestProperty().addListener(new InvalidationListener(){
@Override public void invalidated(Observable observable) {
System.exit(0);
}
});
stage.show();
}
private void quit() {
Platform.exit();
}
private void createSwingContent(final SwingNode swingNode) {
SwingUtilities.invokeLater(new Runnable() {
@Override
public void run() {
JPanel panel = new JPanel();
panel.setBackground(Color.RED);
panel.add(new JLabel("Drag me!"));
swingNode.setContent(panel);
dgListener = new DragGestureListener() {
@Override public void dragGestureRecognized(DragGestureEvent dge) {
System.out.println("drag recognized...");
dge.startDrag(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR), new StringSelection("some string data"));
}
};
dropListener = new DropTargetAdapter() {
@Override public void drop(DropTargetDropEvent dtde) {
System.out.println("drop... " + dtde.getTransferable());
}
};
dragSource = new DragSource();
dragRecognizer = dragSource.createDefaultDragGestureRecognizer(panel, DnDConstants.ACTION_COPY_OR_MOVE, dgListener);
dropTarget = new DropTarget(panel, dropListener);
System.out.println(dragRecognizer);
System.out.println(dropTarget);
}
});
}
}
