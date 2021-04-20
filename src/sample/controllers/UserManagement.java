package sample.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.Classes.User;
import sample.databaseUtils.UserUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserManagement implements Initializable {
    private AccountingSystem sys= new AccountingSystem();
    @FXML
    private ListView<User> userList;
    @FXML
    private Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    User currentItemSelected = userList.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("User removal");
                    alert.setHeaderText("Are you sure you want to remove " + currentItemSelected.getUsername() + " ?");
                    alert.setContentText("This action cannot be reverted.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        try {
                            UserUtils.delete(currentItemSelected);
                        } catch (SQLException | ClassNotFoundException throwables) {
                            throwables.printStackTrace();
                        }
                        sys.deleteUser(currentItemSelected);
                        userList.getItems().clear();
                        try {
                            sys.loadDatabase();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        sys.getUsers().forEach(user -> userList.getItems().addAll(user));
                    } else {
                        alert.close();
                    }
                }
            }
        });
    }

    public void backToMenu() throws IOException {
        Stage currentStage = (Stage) back.getScene().getWindow();
        currentStage.close();
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
    }

    public void setSys(AccountingSystem sys) throws SQLException, ClassNotFoundException {
        this.sys = sys;
        this.sys.loadDatabase();
        System.out.println(sys.getUsers().toString());
        sys.getUsers().forEach(user -> userList.getItems().addAll(user));
    }
}
