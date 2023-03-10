package test.javafx.scene.chart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.ValueAxisShim;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChartShim;
import javafx.scene.shape.Path;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
public class StackedAreaChartTest extends XYChartTestBase {
StackedAreaChart<Number,Number> ac;
final XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
boolean useCategoryAxis = false;
final String[] countries = {"USA", "Italy", "France", "China", "India"};
protected Chart createChart() {
final NumberAxis yAxis = new NumberAxis();
ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
Axis xAxis;
if (useCategoryAxis) {
xAxis = new CategoryAxis();
((CategoryAxis)xAxis).setCategories(FXCollections.observableArrayList(countries));
series1.getData().add(new XYChart.Data(countries[0], 10d));
series1.getData().add(new XYChart.Data(countries[1], 20d));
series1.getData().add(new XYChart.Data(countries[2], 15d));
series1.getData().add(new XYChart.Data(countries[3], 15d));
series1.getData().add(new XYChart.Data(countries[4], 10d));
} else {
xAxis = new NumberAxis();
ac = new StackedAreaChart<Number,Number>(xAxis,yAxis);
series1.getData().add(new XYChart.Data(10d, 10d));
series1.getData().add(new XYChart.Data(25d, 20d));
series1.getData().add(new XYChart.Data(30d, 15d));
series1.getData().add(new XYChart.Data(50d, 15d));
series1.getData().add(new XYChart.Data(80d, 10d));
}
xAxis.setLabel("X Axis");
yAxis.setLabel("Y Axis");
ac.setTitle("HelloStackedAreaChart");
return ac;
}
private String getSeriesLineFromPlot() {
for (Node n : XYChartShim.getPlotChildren(ac)) {
if (n instanceof Group) {
for (Node gn : ((Group)n).getChildren()) {
if (gn instanceof Path && "chart-series-area-line".equals(gn.getStyleClass().get(0))) {
Path line = (Path)gn;
return computeSVGPath(line);
}
}
}
}
return "";
}
@Test @Ignore("pending RT-28373")
public void testSeriesAdd() {
startApp();
ac.getData().addAll(series1);
pulse();
assertEquals("L219.0 58.0 L263.0 173.0 L438.0 173.0 L700.0 289.0 ", getSeriesLineFromPlot());
}
@Test @Ignore
public void testDataItemRemove() {
startApp();
ac.getData().addAll(series1);
pulse();
if (!ac.getData().isEmpty()) {
series1.getData().remove(0);
pulse();
assertEquals("L247.0 171.0 L412.0 171.0 L658.0 284.0 ", getSeriesLineFromPlot());
}
}
@Test @Ignore
public void testDataItemAdd() {
startApp();
ac.getData().addAll(series1);
pulse();
if (!ac.getData().isEmpty()) {
series1.getData().add(new XYChart.Data(40d,10d));
pulse();
assertEquals("L206.0 57.0 L247.0 171.0 L329.0 284.0 L412.0 171.0 L658.0 284.0 ", getSeriesLineFromPlot());
}
}
@Test @Ignore
public void testDataItemInsert() {
startApp();
ac.getData().addAll(series1);
pulse();
if (!ac.getData().isEmpty()) {
series1.getData().add(2, new XYChart.Data(40d,10d));
pulse();
assertEquals("L206.0 57.0 L247.0 171.0 L329.0 284.0 L412.0 171.0 L658.0 284.0 ", getSeriesLineFromPlot());
}
}
@Test @Ignore
public void testDataItemChange() {
startApp();
ac.getData().addAll(series1);
pulse();
if (!ac.getData().isEmpty()) {
XYChart.Data<Number,Number> d = series1.getData().get(2);
d.setXValue(40d);
d.setYValue(30d);
pulse();
assertEquals("L206.0 197.0 L329.0 40.0 L412.0 276.0 L658.0 354.0 ", getSeriesLineFromPlot());
}
}
@Test
public void testStackedAreaChartWithCategoryAxis() {
useCategoryAxis = true;
startApp();
useCategoryAxis = false;
}
@Test public void testCreateSymbols() {
startApp();
ac.setCreateSymbols(false);
pulse();
ac.getData().addAll(series1);
pulse();
assertEquals(0, countSymbols(ac, "chart-area-symbol"));
ac.getData().clear();
pulse();
ac.setCreateSymbols(true);
pulse();
ac.getData().addAll(series1);
assertEquals(5, countSymbols(ac, "chart-area-symbol"));
}
@Test
public void testAutoRange_AdditionalPointInSeries1() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(7, 12),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(18, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_AdditionalPointInSeries1AtTheEnd() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8),
new StackedAreaChart.Data(11, 12)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(19, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_AdditionalPointInSeries1AtTheBeginning() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(-1, 12),
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(20, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_AdditionalPointInSeries2() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(7, 12),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(16, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_AdditionalPointInSeries2AtTheEnd() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7),
new StackedAreaChart.Data(12, 12)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(20, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_AdditionalPointInSeries2AtTheBeginning() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(-1, 15),
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(19, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_EmptySeries1() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
)));
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(1, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(9, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Test
public void testAutoRange_EmptySeries2() {
ac.getData().clear();
final NumberAxis yAxis = (NumberAxis) ac.getYAxis();
yAxis.setAutoRanging(true);
yAxis.setForceZeroInRange(false);
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0, 4),
new StackedAreaChart.Data(2, 5),
new StackedAreaChart.Data(4, 4),
new StackedAreaChart.Data(6, 2),
new StackedAreaChart.Data(8, 6),
new StackedAreaChart.Data(10, 8)
)));
ac.getData().add(new StackedAreaChart.Series<>());
ac.getData().add(new StackedAreaChart.Series<Number, Number>(FXCollections.observableArrayList(
new StackedAreaChart.Data(0,8),
new StackedAreaChart.Data(2,1),
new StackedAreaChart.Data(4,9),
new StackedAreaChart.Data(6,7),
new StackedAreaChart.Data(8,5),
new StackedAreaChart.Data(10,7)
)));
XYChartShim.updateAxisRange(ac);
assertEquals(2, ValueAxisShim.get_dataMinValue(yAxis), 1e-100);
assertEquals(15, ValueAxisShim.get_dataMaxValue(yAxis), 1e-100);
}
@Override
void checkSeriesStyleClasses(XYChart.Series<?, ?> series,
int seriesIndex, int colorIndex) {
Group group = (Group) series.getNode();
Node fillPath = group.getChildren().get(0);
Node seriesLine = group.getChildren().get(1);
checkStyleClass(fillPath, "series"+seriesIndex, "default-color"+colorIndex);
checkStyleClass(seriesLine, "series"+seriesIndex, "default-color"+colorIndex);
}
@Override
void checkDataStyleClasses(XYChart.Data<?, ?> data,
int seriesIndex, int dataIndex, int colorIndex) {
Node symbol = data.getNode();
checkStyleClass(symbol, "series"+seriesIndex, "data"+dataIndex, "default-color"+colorIndex);
}
@Test
public void testSeriesRemoveAnimatedStyleClasses() {
startApp();
int nodesPerSeries = 4;
checkSeriesRemoveAnimatedStyleClasses(ac, nodesPerSeries, 400);
}
}
