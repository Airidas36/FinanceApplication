package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.Classes.User;
import sample.databaseUtils.DatabaseUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.utils.HashUtils.hash;

public class SignupPage{
    private AccountingSystem sys= new AccountingSystem();
    private boolean taken = false;
    @FXML
    private  ChoiceBox<String> permission_box;
    @FXML
    private  ChoiceBox<String> user_box;
    @FXML
    private TextField username, email, phoneNum, position, company, website;
    @FXML
    private PasswordField password;
    @FXML
    private Label companyName, websiteField, usernameError;
    @FXML
    private Button backBtn;
    @FXML
    private Label phoneLabel, positionLabel;

    @FXML
    private void inputFormChange() {
        if (user_box.getValue().equals("legal")) {
            companyName.setVisible(true);
            websiteField.setVisible(true);
            company.setVisible(true);
            website.setVisible(true);
            phoneLabel.setVisible(false);
            positionLabel.setVisible(false);
            position.setVisible(false);
            phoneNum.setVisible(false);
        } else {
            companyName.setVisible(false);
            websiteField.setVisible(false);
            company.setVisible(false);
            website.setVisible(false);
            phoneLabel.setVisible(true);
            positionLabel.setVisible(true);
            position.setVisible(true);
            phoneNum.setVisible(true);
        }
    }

    @FXML
    public void adduser(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException, SQLException, ClassNotFoundException {
        String username = this.username.getText();
        String password = this.password.getText();
        password = hash(password);
        String email = this.email.getText();
        String phoneNum = this.phoneNum.getText();
        String position = this.position.getText();
        if (username.length() > 0 && password.length() > 0 && !taken) {
            if (user_box.getValue().equals("legal")) {
                String company = this.company.getText();
                String website = this.website.getText();
                Connection connection = connectToDb();
                Statement stmt = connection.createStatement();
                String insert = "INSERT INTO legaluser (USERNAME, PASSWORD, PERMISSIONS, EMAIL, COMPANYNAME, WEBSITE) VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, permission_box.getValue());
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, company);
                preparedStatement.setString(6, website);
                preparedStatement.executeUpdate();
                DatabaseUtils.disconnectFromDb(connection, stmt);
                sys.loadDatabase();
                //sys.addUser(username, password, phoneNum, position, email, permission_box.getValue(), user_box.getValue(), company, website);
            } else {
                String phoneNumber = this.phoneNum.getText();
                String work_position = this.position.getText();
                Connection connection = connectToDb();
                Statement stmt = connection.createStatement();
                String insert = "INSERT INTO user (USERNAME, PASSWORD, PERMISSIONS, EMAIL, PHONENUM, POSITION) VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, permission_box.getValue());
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setString(6, work_position);
                preparedStatement.executeUpdate();
                DatabaseUtils.disconnectFromDb(connection, stmt);
                sys.loadDatabase();
                //sys.addUser(username, password, phoneNum, position, email, permission_box.getValue(), user_box.getValue(), null, null);
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/LoginPage.fxml"));
            Stage stage = new Stage();
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            LoginPage loginpage = fxmlLoader.getController();
            stage.setTitle("Sign In");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            currentStage.close();
        }
    }

    @FXML
    private void checkUsername() {
        String username = this.username.getText();
        this.taken = false;
        try {
            for (User user : sys.getUsers())
                if (user.getUsername().equals(username)) {
                    taken = true;
                    break;
                }
            usernameError.setVisible(taken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/Welcome.fxml"));
        Stage stage = new Stage();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Welcome welcome = fxmlLoader.getController();
        welcome.setSys(sys);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) backBtn.getScene().getWindow();
        currentStage.close();
    }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
        ObservableList<String> permissions = FXCollections.observableArrayList("admin", "standard");
        ObservableList<String> user_type = FXCollections.observableArrayList("employee", "legal");
        permission_box.setItems(permissions);
        user_box.setItems(user_type);
        user_box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
                if (oldFruit != null) {
                    inputFormChange();
                }
            }
        });
        user_box.setValue("legal");
        user_box.setValue("employee");
    }
}
