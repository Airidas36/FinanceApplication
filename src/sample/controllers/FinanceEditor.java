package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.Classes.Category;
import sample.Classes.Finance;
import sample.databaseUtils.FinanceUtils;

import java.sql.*;

public class FinanceEditor {
    private AccountingSystem sys= new AccountingSystem();
    private String action;
    private Category category;
    @FXML
    private ChoiceBox<String> finBox = new ChoiceBox<>();
    @FXML
    private Button doneBtn;
    @FXML private Button cancelBtn;
    @FXML
    private TextField finName;
    @FXML
    private TextField finDesc;
    @FXML
    private TextField finType;
    @FXML
    private TextField finSrc;
    @FXML
    private TextField finAmount = new TextField();
    private int finId;

    public void setAction(String action){
        this.action = action;
    }

    public AccountingSystem getSys() {
        return sys;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void complete(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(action.equals("ADD"))
        {
            if(finName != null && finDesc != null && finType != null && finSrc != null && finAmount != null) {
                String name = finName.getText();
                String description = finDesc.getText();
                String financeType = finType.getText();
                String IncOrExp = finBox.getValue();
                String source = finSrc.getText();
                double amount = 0;
                try {
                    amount = Double.parseDouble(finAmount.getText());
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                if(IncOrExp.equals("Expense"))
                    amount *= -1;
                FinanceUtils.add(name, description, financeType, amount, source, category.getId());
                Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
                currentStage.close();
            }
        }
        if(action.equals("UPDATE"))
        {
            if(finName != null && finDesc != null && finType != null && finSrc != null && finAmount != null) {
                String name = finName.getText();
                String description = finDesc.getText();
                String financeType = finType.getText();
                String IncOrExp = finBox.getValue();
                String source = finSrc.getText();
                double amount = 0;
                try {
                    amount = Double.parseDouble(finAmount.getText());
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                if(IncOrExp.equals("Expense"))
                    amount *= -1;
                FinanceUtils.add(name, description, financeType, amount, source, category.getId());
                Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
                currentStage.close();
            }
        }

    }

    public void cancelAction(ActionEvent actionEvent){
        Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
        currentStage.close();
    }

    public void setSys(AccountingSystem sys){
        this.sys = sys;
        ObservableList<String> fintype = FXCollections.observableArrayList("Income", "Expense");
        finBox.setItems(fintype);
    }

    public void setFinance(int id) {
        this.finId = id;
        if(action.equals("UPDATE"))
        {
            Finance fin = null;
            try {
                fin = FinanceUtils.getFin(finId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finName.setText(fin.getName());
            finDesc.setText(fin.getDescription());
            finType.setText(fin.getFinanceType());
            finSrc.setText(fin.getSource());
            finAmount.setText(String.valueOf(fin.getAmount()));
        }
    }
}
