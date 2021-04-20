package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginPage implements Initializable{

    private AccountingSystem sys = new AccountingSystem();

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginError;

    public void login(ActionEvent actionEvent) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        sys.loginto(username,password);
        if(sys.getActiveUser() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/empAdminMenu.fxml"));
                Stage stage = new Stage();
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                EmpAdminMenu empAdminMenu= fxmlLoader.getController();
                empAdminMenu.setSys(sys);
                stage.setTitle("Sign In");
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) loginError.getScene().getWindow();
                currentStage.close();
        }
        else{
            loginError.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            sys.loadDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
