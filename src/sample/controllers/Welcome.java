package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import java.io.IOException;

public class Welcome {

    private AccountingSystem sys;
    @FXML
    private Button signin;
    @FXML
    private Button exitBtn;

    public void login(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/LoginPage.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            LoginPage loginpage = fxmlLoader.getController();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) signin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/SignupPage.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            SignupPage signuppage = fxmlLoader.getController();
            signuppage.setSys(sys);
            stage.setTitle("SignUp");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) signin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent){
        Stage currentStage = (Stage) exitBtn.getScene().getWindow();
        currentStage.close();
    }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
    }
}
