package sample.Classes;

import java.io.Serializable;

public class Finance implements Serializable {
    private String name;
    private String description;
    private String financeType;
    private String source;
    private double amount;
    private int catId;
    private int id;

    public Finance(String name, String description, String source, double amount, String financeType, int catId) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.financeType = financeType;
        this.catId = catId;
        this.source = source;
    }

    public Finance(String name, String description, String financeType, String source, double amount, int catId, int id) {
        this.name = name;
        this.description = description;
        this.financeType = financeType;
        this.amount = amount;
        this.catId = catId;
        this.id = id;
        this.source = source;
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

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return name + " (" + description + ") Amount: " + amount + " Type: " + financeType;
    }

    public String getSource() {
        return source;
    }

    public int getCatId() {
        return catId;
    }

    public int getId() {
        return id;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }
}
