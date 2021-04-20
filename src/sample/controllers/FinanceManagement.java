package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.Classes.Category;
import sample.Classes.Finance;
import sample.databaseUtils.FinanceUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FinanceManagement implements Initializable{
    private AccountingSystem sys= new AccountingSystem();
    private Category category;
    @FXML
    private ListView<Finance> finList = new ListView<>();
    @FXML
    private Button addFin;
    @FXML
    private Button updateFin;
    @FXML
    private Button deleteFin;
    private Finance currentItemSelected;

    public void setCategory(Category category) throws SQLException, ClassNotFoundException {
        this.category = category;
        reloadFinance();
        finList.refresh();
    }

    public void addFinance(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/FinanceEditor.fxml"));
        Stage stage = new Stage();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        FinanceEditor financeEditor = fxmlLoader.getController();
        financeEditor.setSys(sys);
        financeEditor.setAction("ADD");
        financeEditor.setCategory(category);
        stage.setTitle("Add Finance");
        stage.setScene(scene);
        stage.show();
        reloadFinance();
    }

    public void updateFinance(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/FinanceEditor.fxml"));
        Stage stage = new Stage();
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        FinanceEditor financeEditor = fxmlLoader.getController();
        financeEditor.setSys(sys);
        financeEditor.setAction("UPDATE");
        financeEditor.setCategory(category);
        financeEditor.setFinance(currentItemSelected.getId());
        stage.setTitle("Add Finance");
        stage.setScene(scene);
        stage.show();
        reloadFinance();
    }

    public void deleteFinance(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(currentItemSelected != null) {
            FinanceUtils.delete(currentItemSelected.getId());
            reloadFinance();
        }

    }

    public void reloadFinance() throws SQLException, ClassNotFoundException {
        finList.getItems().clear();
        ArrayList<Finance> finances = FinanceUtils.getFinances(category.getId());
        finances.forEach(finance -> finList.getItems().addAll(finance));
        finList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                currentItemSelected = finList.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
