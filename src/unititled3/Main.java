package unititled3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {

    private FourierChartBox chartBox;
    private Button dftModeButton;
    private Button fftModeButton;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Function function = new Function();
        chartBox = new FourierChartBox(function);
        chartBox.setDftMode(true);

        dftModeButton = new Button("DFT");
        dftModeButton.setOnAction(this);
        fftModeButton = new Button("FFT");
        fftModeButton.setOnAction(this);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(dftModeButton, fftModeButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.add(chartBox, 0, 0, 3, 3);
        grid.add(hBox, 0, 4, 3, 1);

        primaryStage.setScene(new Scene(grid));
        primaryStage.show();

        chartBox.redraw();
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == dftModeButton)
            chartBox.setDftMode(true);
        else
            chartBox.setDftMode(false);

        chartBox.redraw();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
