package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.controllers.Welcome;

import java.time.LocalDate;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AccountingSystem accsys = new AccountingSystem("AiridasInc", "1.0.0", LocalDate.now()); // Instantiate system
        accsys.loadDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/Welcome.fxml"));
        Parent root = loader.load();
        Welcome welcome = loader.getController();
        welcome.setSys(accsys);
        primaryStage.setTitle("Accounting System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
