package sample.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Category implements Serializable {
    private String name;
    private String description;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private final ArrayList<User> responsiblePeople = new ArrayList<>();
    private final ArrayList<Category> subcategories = new ArrayList<>();
    private final ArrayList<Finance> finances = new ArrayList<>();
    private Category parentCategory = null;
    private int id;
    private int parentId;
    private String path;

    public Category(String name, String description, User user, Category parentCategory, LocalDate dateCreated) {
        this.name = name;
        this.description = description;
        this.responsiblePeople.add(user);
        this.dateCreated = LocalDate.now();
        this.dateModified = this.dateCreated;
        this.parentCategory = parentCategory;
    }

    public Category(String name, String description, Category parentCategory){
        this.name = name;
        this.description = description;
        this.dateCreated = LocalDate.now();
        this.parentCategory = parentCategory;
    }

    public Category(String name, String description, LocalDate dateCreated, LocalDate dateModified, int id, int parentId, String path) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.id = id;
        this.parentId = parentId;
        this.path = path;
    }

    public Category(String categoryName, String categoryDescription, User user, Category category) {
        this.name = categoryName;
        this.description = categoryDescription;
        this.responsiblePeople.add(user);
        this.dateCreated = LocalDate.now();
        this.dateModified = this.dateCreated;
        this.parentCategory = category;
    }

    public Category(String name, String description, LocalDate dateCreated, LocalDate dateModified, int id, int parentId) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.id = id;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Category> getSubcategories() {
        return subcategories;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        double sum = finances.stream().mapToDouble(Finance::getAmount).sum();
        return name + " (" + description + ") ";
    }
}