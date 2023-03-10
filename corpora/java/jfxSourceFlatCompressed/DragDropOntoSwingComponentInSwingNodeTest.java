import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
public class DragDropOntoSwingComponentInSwingNodeTest extends Application
{
public static void main(String[] args)
{
Application.launch(args);
}
@Override
public void start(Stage primaryStage) throws Exception
{
Button passButton = new Button("Pass");
Button failButton = new Button("Fail");
passButton.setOnAction(e -> this.quit());
failButton.setOnAction(e -> {
this.quit();
throw new AssertionError("Drag / drop onto a Swing component in a SwingNode not working");
});
SwingNode swingNode = new SwingNode();
StackPane pane = new StackPane(swingNode);
SwingUtilities.invokeLater( () -> {
swingNode.setContent(Content.createPanel());
});
VBox rootNode = new VBox(6,
new Label("1. This is a test for drag / drop onto a Swing component in a SwingNode."),
new Label("2. Select and drag some text/image from a browser or document and drop into red JPanel."),
new Label("3. When the content is dropped into JPanel, if it prints the mime dataflavor on console, click on Pass or else click on Fail"),
new Label(""),
new HBox(10, passButton, failButton), pane);
primaryStage.setScene(new Scene(rootNode));
primaryStage.setWidth(800);
primaryStage.setHeight(600);
primaryStage.show();
}
private void quit() {
Platform.exit();
}
static class Content {
public static JPanel createPanel()
{
JPanel panel = new JPanel();
panel.setPreferredSize(new Dimension(400, 400));
panel.setBackground(Color.RED);
panel.setDropTarget(new DropTarget(
panel, DnDConstants.ACTION_COPY, new DropTargetAdapter()
{
@Override
public void dragEnter(DropTargetDragEvent dtde)
{
dtde.acceptDrag(dtde.getDropAction());
}
@Override
public void drop(DropTargetDropEvent dtde)
{
for( DataFlavor dataFlavor : dtde.getCurrentDataFlavors() )
{
System.out.println(dataFlavor);
}
}
}));
return panel;
}
}
}
