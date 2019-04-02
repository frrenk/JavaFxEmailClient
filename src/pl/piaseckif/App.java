package pl.piaseckif;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.piaseckif.views.ViewFactory;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewFactory factory = ViewFactory.defaultFactory;

        Scene scene = factory.getMainScene();

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
