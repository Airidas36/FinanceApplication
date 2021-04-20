package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Classes.AccountingSystem;
import sample.Classes.Category;
import sample.databaseUtils.CategoryUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindow implements Initializable{
    private AccountingSystem sys= new AccountingSystem();
    @FXML
    private TreeView<Category> categoryTree;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Label financeLabel;
    private TreeItem<Category> selectedCategory = null;

    public void loadTree() {
        TreeItem<Category> root = new TreeItem<>();
        root.setExpanded(true);
        addNodes(sys.getCategories(), root);
        categoryTree.setRoot(root);
        categoryTree.setShowRoot(true);
        categoryTree = new TreeView<>(root);
    }

    public void addNodes(ArrayList<Category> categories, TreeItem<Category> root) {
        for (Category cat : categories) {
            TreeItem<Category> newRoot = makeBranch(cat, root);
            if (cat.getSubcategories().size() > 0) {
                addNodes(cat.getSubcategories(), newRoot);
            }
        }
    }

    public TreeItem<Category> makeBranch(Category title, TreeItem<Category> parent) {
        TreeItem<Category> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    categoryTree.getSelectionModel().selectedItemProperty().addListener((v, oldCategory, selectedCategory) -> selectedCategory = selectedCategory);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/FinanceManagement.fxml"));
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    FinanceManagement financeManagement= fxmlLoader.getController();
                    financeManagement.setSys(sys);
                    try {
                        financeManagement.setCategory(selectedCategory.getValue());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    stage.setTitle("Finance Management");
                    stage.setScene(scene);
                    stage.show();
                    try {
                        sys.loadDatabase();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    categoryTree.refresh();
                }
            }
        });
        categoryTree.getSelectionModel().selectedItemProperty().addListener((v, oldCategory, selectedCategory) -> this.selectedCategory = selectedCategory);
    }

    public void addCategory(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        openDialog("ADD");
        sys.loadDatabase();
    }

    public void updateCategory(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        if (selectedCategory != null) openDialog("UPDATE");
    }

    public void deleteCategory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (selectedCategory != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete category");
            alert.setHeaderText("Are you sure you want to delete " + selectedCategory.getValue().getName() + " ?");
            alert.setContentText("This action cannot be reverted.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                CategoryUtils.delete(selectedCategory.getValue().getId());
                sys.loadDatabase();
                sys.deleteCategory(sys.getCategories(), selectedCategory.getValue());
                selectedCategory.getParent().getChildren().remove(selectedCategory);
            }
        }
    }
    public static void deleteCategories(Connection connection, int id) throws SQLException {
        String query = "SELECT categoryId FROM category WHERE parentId = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet categories = preparedStatement.executeQuery(query);
        while(categories.next()){
            query = "SELECT categoryId FROM category WHERE parentId = " + categories.getInt("categoryId");
            PreparedStatement a1 = connection.prepareStatement(query);
            ResultSet nexcats = a1.executeQuery(query);
            if(nexcats != null) {
                deleteCategories(connection, categories.getInt("categoryId"));
                query = "DELETE FROM category WHERE parentId = " + categories.getInt("categoryId");
                a1 = connection.prepareStatement(query);
                a1.executeUpdate();
            }
            else{
                PreparedStatement stmt;
                query = "DELETE FROM category WHERE parentId = " + categories.getInt("categoryId");
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();
            }
        }
    }

    public void openDialog (String mode) throws IOException, SQLException, ClassNotFoundException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample/resources/CategoryEditor.fxml"));
            DialogPane categoryDialogPane = fxmlLoader.load();
            CategoryEditor categoryEditor = fxmlLoader.getController();
            if(selectedCategory != null) {
                categoryEditor.setCategory(selectedCategory.getValue());
                categoryEditor.setSys(sys);
                categoryEditor.setMode(mode);
                categoryEditor.setSelectedCategory(selectedCategory);
                categoryEditor.setCategoryTree(categoryTree);
                categoryEditor.fillFields();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.setDialogPane(categoryDialogPane);
                dialog.setTitle(mode.charAt(0) + mode.substring(1, mode.length()).toLowerCase() + " Category");
                ButtonType buttonTypeOne = new ButtonType("Done");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.get() == buttonTypeOne) {
                    categoryEditor.complete();
                    sys.loadDatabase();
                    loadTree();
                    categoryTree.refresh();
                    //sys.showCategories(sys.getCategories(), "", 1);
                }
            }
        }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
        if(sys.getActiveUser().isLegalPerson())
        {
            addBtn.setVisible(false);
            updateBtn.setVisible(false);
            deleteBtn.setVisible(false);
            financeLabel.setVisible(true);
        }
        loadTree();
    }
}
