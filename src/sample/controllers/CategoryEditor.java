package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sample.Classes.AccountingSystem;
import sample.Classes.Category;
import sample.databaseUtils.CategoryUtils;

import java.sql.*;
import java.time.LocalDate;

public class CategoryEditor{
    private AccountingSystem sys= new AccountingSystem();
    private String mode;
    private Category category;
    @FXML
    private TreeItem<Category> selectedCategory;
    @FXML
    private TreeView<Category> categoryTree;
    @FXML
    private TextField catName;
    @FXML
    private TextField catDesc;

    public void setCategoryTree(TreeView<Category> categoryTree) {
        this.categoryTree = categoryTree;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSelectedCategory(TreeItem<Category> selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /*public void setSys(AccountingSystem sys) {
        this.sys = sys;
    }*/

    public void fillFields(){
        if(mode.equals("UPDATE")) {
            catName.setText(this.category.getName());
            catDesc.setText(this.category.getDescription());
        }
    }

    public void complete() throws SQLException, ClassNotFoundException {
        if(mode.equals("ADD"))
        {
            sys.loadDatabase();
            String categoryName = catName.getText();
            String categoryDescription = catDesc.getText();
            Category newCategory = new Category(categoryName, categoryDescription, sys.getActiveUser(), category);
            TreeItem<Category> newCat = new TreeItem<>(newCategory);
            newCat.setExpanded(true);
            if(selectedCategory.getValue() != null) {
                CategoryUtils.create(categoryName, categoryDescription, String.valueOf(category.getId()));
                sys.addCategory(sys.getCategories(),selectedCategory.getValue(), newCategory);
                selectedCategory.getChildren().add(newCat);
            }
            else{
                CategoryUtils.create(categoryName, categoryDescription, null);
                sys.addCategory(sys.getCategories(), null, newCategory);
                categoryTree.getRoot().getChildren().add(newCat);
            }
            sys.loadDatabase();
            System.out.println("Added " + newCategory.getName());
        }
        else if(mode.equals("UPDATE"))
        {
            String categoryName = catName.getText();
            String categoryDescription = catDesc.getText();
            LocalDate modified = LocalDate.now();
            CategoryUtils.update(categoryName, categoryDescription, selectedCategory.getValue().getId(), modified);
            selectedCategory.valueProperty().set(new Category(categoryName, categoryDescription, sys.getActiveUser(), selectedCategory.getValue().getParentCategory(), modified));
            sys.updateCategory(sys.getCategories(), selectedCategory.getValue(), categoryName, categoryDescription);
            sys.loadDatabase();
        }
    }

    public void setSys(AccountingSystem sys) {
        this.sys = sys;
    }
}
