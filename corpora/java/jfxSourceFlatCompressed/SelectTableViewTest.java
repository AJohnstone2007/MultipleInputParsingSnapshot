import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class SelectTableViewTest extends Application {
final int ROW_COUNT = 70_000;
final int COL_COUNT = 3;
@Override
public void start(Stage stage) {
TableView<String[]> tableView = new TableView<>();
tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
final ObservableList<TableColumn<String[], ?>> columns = tableView.getColumns();
for(int i = 0; i < COL_COUNT; i++) {
TableColumn<String[], String> column = new TableColumn<>("Col"+i);
final int colIndex=i;
column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[colIndex]));
column.setPrefWidth(150);
columns.add(column);
}
ObservableList<String[]> items = tableView.getItems();
for(int i = 0; i < ROW_COUNT; i++) {
String[] rec = new String[COL_COUNT];
for(int j = 0; j < rec.length; j++) {
rec[j] = i + ":" + j;
}
items.add(rec);
}
BorderPane root = new BorderPane(tableView);
Button selectAll = new Button("selectAll");
Button clearSelection = new Button("clearSelection");
Button selectToStart = new Button("selectToStart");
Button selectToEnd = new Button("selectToEnd");
Button selectPrevious = new Button("selectPrevious");
Button selectNext= new Button("selectNext");
selectAll.setFocusTraversable(true);
clearSelection.setFocusTraversable(true);
selectToStart.setFocusTraversable(true);
selectToEnd.setFocusTraversable(true);
selectPrevious.setFocusTraversable(true);
selectNext.setFocusTraversable(true);
root.setRight(new VBox(6, selectAll, selectToStart, selectToEnd, selectPrevious, selectNext, clearSelection));
stage.setScene(new Scene(root, 600, 600));
selectAll.setOnAction(e -> selectAll(tableView));
clearSelection.setOnAction(e -> clearSelection(tableView));
selectToStart.setOnAction(e -> selectToStart(tableView));
selectToEnd.setOnAction(e -> selectToLast(tableView));
selectPrevious.setOnAction(e -> selectPrevious(tableView));
selectNext.setOnAction(e -> selectNext(tableView));
stage.show();
}
private void selectAll(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().selectAll();
System.out.println("time:" + (System.currentTimeMillis() - t));
}
private void clearSelection(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().clearSelection();
System.out.println("time:" + (System.currentTimeMillis() - t));
}
private void selectToStart(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().selectRange(0, tableView.getSelectionModel().getFocusedIndex());
System.out.println("time:" + (System.currentTimeMillis() - t));
}
private void selectToLast(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().selectRange(tableView.getSelectionModel().getFocusedIndex(), tableView.getItems().size());
System.out.println("time:" + (System.currentTimeMillis() - t));
}
private void selectPrevious(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().selectPrevious();
System.out.println("time:" + (System.currentTimeMillis() - t));
}
private void selectNext(TableView tableView) {
long t = System.currentTimeMillis();
tableView.getSelectionModel().selectNext();
System.out.println("time:" + (System.currentTimeMillis() - t));
}
public static void main(String[] args) {
Application.launch(args);
}
}
