package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.databaseUtils.UserUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static sample.utils.HashUtils.hash;

public class PasswordEditor{
    private AccountingSystem sys= new AccountingSystem();
    @FXML
    private Button defaultBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField rePassword;

    public void okBtnClick() throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        if (sys.passwordLegit(sys.getActiveUser(), oldPassword.getText())) {
            if (newPassword.getText().length() > 0 && rePassword.getText().length() > 0) {
                if (newPassword.getText().equals(rePassword.getText()))
                {
                    String newPass = hash(newPassword.getText());
                    UserUtils.changePass(sys.getActiveUser(), newPass);
                    Stage stage = (Stage) defaultBtn.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    public void cancelBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
    }
}
