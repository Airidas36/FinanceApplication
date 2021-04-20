package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import java.io.IOException;
import java.sql.SQLException;

public class EmpAdminMenu {
    private AccountingSystem sys;
    @FXML
    private Label loginInfo;
    @FXML
    private MenuItem passBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button userManagement;

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
        loginInfo.setText(loginInfo.getText() + "\n" + sys.getActiveUser().getUsername());
        if(sys.getActiveUser().getPermissions().equals("standard"))
            userManagement.setVisible(false);

    }

    public void changePassword(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/passwordEditor.fxml"));
        Parent parent = fxmlLoader.load();
        PasswordEditor passwordEditor =fxmlLoader.getController();
        passwordEditor.setSys(sys);
        Scene scene = new Scene(parent);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        passwordEditor.setSys(sys);
        window.showAndWait();
    }

    public void manageCategories(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/MainWindow.fxml"));
        Stage stage = new Stage();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MainWindow mainwindow = fxmlLoader.getController();
        mainwindow.setSys(sys);
        stage.setTitle("Manage Categories");
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
        currentStage.close();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
        currentStage.close();
        sys.logOut();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/Welcome.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Parent) fxmlLoader.load()));
        Welcome welcome = fxmlLoader.getController();
        welcome.setSys(sys);
        stage.setTitle("Welcome");
        stage.show();
    }

    public void manageUsers(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
        currentStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/userManagement.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Parent) fxmlLoader.load()));
        UserManagement userManagement = fxmlLoader.getController();
        userManagement.setSys(sys);
        stage.setTitle("User Management");
        stage.show();
    }
}
