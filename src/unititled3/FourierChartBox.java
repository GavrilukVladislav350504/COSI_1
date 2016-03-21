package unititled3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.apache.commons.math3.complex.Complex;


import java.util.ArrayList;

public class FourierChartBox extends Pane {

    private LineChart timeChart;
    private ScatterChart freqChart;
    private LineChart restoredChart;

    private Function function;
    private Boolean dftMode;

    //INIT
    FourierChartBox (Function f) {

        function = f;

        timeChart = new LineChart(new NumberAxis(), new NumberAxis());
        freqChart = new ScatterChart(new NumberAxis(), new NumberAxis());
        restoredChart = new LineChart(new NumberAxis(), new NumberAxis());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setMaxWidth(400);
        grid.setMaxHeight(600);
        grid.setAlignment(Pos.CENTER);
        grid.add(timeChart, 0, 0);
        grid.add(freqChart, 0, 1);
        grid.add(restoredChart, 0, 2);

        this.getChildren().add(grid);
    }

    //SETTERS
    public void setDftMode(Boolean dftMode) {
        this.dftMode = dftMode;
    }

    //PUBLIC METHODS
    public void redraw() {

        ArrayList digitalValues = calculateDigitalValues();
        ArrayList cx = dftMode ? Fourier.DFT(digitalValues) : Fourier.FFT(digitalValues);
        digitalValues = dftMode ? Fourier.RDFT(cx) : Fourier.RFFT(cx);

        ObservableList points1 = timeChartPoints();
        ObservableList points2 = freqChartPoints(cx);
        ObservableList points3 = restoredChartPoints(digitalValues);

        clearCharts();
        timeChart.getData().add(new XYChart.Series<>("TIME", points1));
        freqChart.getData().add(new XYChart.Series<>("FREQ", points2));
        restoredChart.getData().add(new XYChart.Series<>("RESTORED", points3));
    }

    //PRIVATE METHODS
    private ObservableList<XYChart.Data> timeChartPoints() {

        int pointsCount = 300;
        double step = function.getPeriod() / pointsCount;

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < pointsCount; i++) {
            double x = i * step;
            double y = function.detValue(x);
            points.add(createPoint(x, y, false));
        }

        return points;
    }

    private ObservableList<XYChart.Data> freqChartPoints(ArrayList<Complex> cx) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < cx.size(); i++) {
            Complex c = cx.get(i);
            points.add(createPoint(i, c.abs(), true));
        }

        return points;
    }

    private ObservableList<XYChart.Data> restoredChartPoints(ArrayList digitalValues) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();
        double digitalStep = function.getPeriod() / Fourier.N;

        for (int i = 0; i < digitalValues.size(); i++) {
            double x = i * digitalStep;
            double y =  (double)digitalValues.get(i);
            points.add(createPoint(x, y, false));
        }

        return points;
    }

    private XYChart.Data createPoint(double x, double y, boolean isVisible) {

        XYChart.Data<Object, Object> point = new XYChart.Data<Object, Object>(x,y);

        Rectangle rect = new Rectangle(5, 5);
        rect.setVisible(isVisible);
        point.setNode(rect);

        return point;
    }

    private ArrayList calculateDigitalValues() {

        double step = function.getPeriod() / Fourier.N;
        ArrayList values = new ArrayList();

        for (int i = 0; i < Fourier.N; i ++) {
            double x = i * step;
            values.add(function.detValue(x));
        }

        return values;
    }

    private void clearCharts() {
        timeChart.getData().clear();
        freqChart.getData().clear();
        restoredChart.getData().clear();
    }
}
