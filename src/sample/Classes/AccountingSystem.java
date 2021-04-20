package sample.Classes;

import sample.subClasses.individualPerson;
import sample.subClasses.legalPerson;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.utils.HashUtils.hash;

public class AccountingSystem implements Serializable {
    private LocalDate sysCreationDate;
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private String companyName;
    private String sysVersion;
    private User activeUser = null;
    private double systemBalance;

    public AccountingSystem(String companyName, String sysVersion, LocalDate sysCreationDate, ArrayList<Category> categories, ArrayList<User> users) {
        this.companyName = companyName;
        this.sysVersion = sysVersion;
        this.sysCreationDate = sysCreationDate;
        this.categories = categories;
        this.users = users;
    }

    public AccountingSystem(String companyName, String sysVersion, LocalDate sysCreationDate) {
        this.companyName = companyName;
        this.sysVersion = sysVersion;
        this.sysCreationDate = sysCreationDate;
        this.systemBalance = 0;
        //readUsers();
        //importCategories();
    }

    public AccountingSystem() {

    }


    public void addCategory(ArrayList<Category> categories, Category selectedCat, Category category) {
        boolean okToAdd = true;
        if (selectedCat != null) {
            for (Category cat : categories) {
                if (cat.getSubcategories().size() > 0) addCategory(cat.getSubcategories(), selectedCat, category);
                if (selectedCat.getName().equals(cat.getName()) && selectedCat.getDescription().equals(cat.getDescription())
                        && selectedCat.getParentCategory() == cat.getParentCategory()) {
                    cat.getSubcategories().add(category);
                    //exportCategories();
                    break;
                }
            }
        } else {
            for (Category cat : categories) {
                if (cat.getName().equals(category.getName()))
                    okToAdd = false;
            }
            if (okToAdd) {
                categories.add(category);
                //exportCategories();
            }
        }
    }

    public void updateCategory(ArrayList<Category> categories, Category selectedCat, String newName, String newDesc) {
        for (Category cat : categories) {
            if (cat.getSubcategories().size() > 0)
                updateCategory(cat.getSubcategories(), selectedCat, newName, newDesc);
            if (selectedCat.getName().equals(cat.getName()) && selectedCat.getDescription().equals(cat.getDescription()) && selectedCat.getParentCategory() == cat.getParentCategory()) {
                cat.setName(newName);
                cat.setDescription(newDesc);
            }
        }
    }

    public void deleteCategory(ArrayList<Category> categories, Category selectedCat) {
        for (Category cat : categories) {
            if (cat.getSubcategories().size() > 0) deleteCategory(cat.getSubcategories(), selectedCat);
            if (selectedCat.getName().equals(cat.getName()) && selectedCat.getDescription().equals(cat.getDescription()) && selectedCat.getParentCategory() == cat.getParentCategory()) {
                categories.remove(cat);
                break;
            }
        }
    }

    public void loginto(String username, String password) {
        boolean logged = false;
        String hashedPass = null;
        for (User user : users) {
            try {
                hashedPass = hash(password); // Hashing password with SHA-256
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error!");
                e.printStackTrace();
            }
            if (user.getUsername().equals(username) && user.getPassword().equals(hashedPass)) {
                logged = true;
                this.activeUser = user; // Flag new active user in the system
                System.out.println("Successfully logged in as " + user.getUsername());
                break;
            }
        }

    }

    public void logOut() {
        this.activeUser = null;
        //writeUsers();
        //exportSystem();
       // exportCategories();
    }


    public void deleteUser(User user) {
        users.remove(user);
        writeUsers();
    }

    public void addUser(User user)
    {
        users.add(user);
    }

    public String showCategories(ArrayList<Category> categories, String indice, int index, String line) {
        for (Category c : categories) {
            if (c.getParentCategory() == null)
                line += indice + index + ". " + c.getName() + " (" + c.getDescription() + ")\n";
            else if (c.getParentCategory() != null && !(indice.equals("")))
                line += indice + "-" + c.getName() + " (" + c.getDescription() + ")\n";
            else
                line += indice + index + ". " + c.getName() + " (" + c.getDescription() + ")\n";
            if (c.getSubcategories().size() != 0) {
                return showCategories(c.getSubcategories(), indice + "\t", index, line);
            }
            index++;
        }
        return line;
    }



    public void addUser(String username, String password, String phone, String position, String email, String permissions, String user_type, String company, String website) throws NoSuchAlgorithmException {
        password = hash(password);
        if (user_type.equals("legal"))
            users.add(new legalPerson(username, password, permissions, company, email, website, true));
        else users.add(new individualPerson(username, password, permissions, email, phone, position, false));
        //writeUsers();
    }


    private void readUsers() { // Reads user list from users.ser file and appends it to system userlist
        String path = "src/sample/userList.ser";
        System.out.println(path);
        ObjectInputStream input = null;
        try {
            FileInputStream readFile = new FileInputStream(path);
            input = new ObjectInputStream(readFile);
            ArrayList<User> usersIn = (ArrayList<User>) input.readObject();
            users.clear();
            users.addAll(usersIn);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("User list is empty, or there was a changed made to Class User attributes!");
            System.out.println("Fill the user list again.");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException a) {
                a.printStackTrace();
            }
        }
    }

    private void writeUsers() { // Overwrites user.ser file
        try {
            FileOutputStream out = new FileOutputStream("src/sample/userList.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(users);
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing user file!");
        }
    }

    public boolean passwordLegit(User user, String oldPassword) throws NoSuchAlgorithmException {
        return user.getPassword().equals(hash(oldPassword));
    }

    public void changePass(String newPassword) throws NoSuchAlgorithmException {
        newPassword = hash(newPassword);
        activeUser.setPass(newPassword);
        //writeUsers();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public void loadDatabase() throws SQLException, ClassNotFoundException {
        this.categories = new ArrayList<>();
        User active = null;
        active = activeUser;
        users.clear();
        if(active != null)
            users.add(active);
        Connection connection = connectToDb();
        Statement stmt = connection.createStatement();
        String query;
        query = "SELECT * from legaluser";
        ResultSet legalusers = stmt.executeQuery(query);
        String actuserName = "";
        if(activeUser != null)
            actuserName = activeUser.getUsername();
        while(legalusers.next()){
            if(!legalusers.getString("username").equals(actuserName)){
                String username = legalusers.getString("username");
                String password = legalusers.getString("password");
                String permissions = legalusers.getString("permissions");
                String email = legalusers.getString("email");
                String companyName = legalusers.getString("companyName");
                String website = legalusers.getString("website");
                legalPerson legalPerson = new legalPerson(username, password, permissions,companyName, email, website, true);
                users.add(legalPerson);
            }
        }
        legalusers.close();
        query = "SELECT * from user";
        ResultSet user = stmt.executeQuery(query);
        while(user.next()){
            if(!user.getString("username").equals(actuserName)){
                String username = user.getString("username");
                String password = user.getString("password");
                String permissions = user.getString("permissions");
                String email = user.getString("email");
                String phoneNum = user.getString("phoneNum");
                String position = user.getString("position");
                individualPerson individualPerson = new individualPerson(username, password, permissions, email, phoneNum, position, false);
                users.add(individualPerson);
            }
        }
        user.close();

        query = "SELECT * from category";
        ResultSet categories = stmt.executeQuery(query);
        while(categories.next())
        {
            String name = categories.getString("name");
            String description = categories.getString("description");
            LocalDate dateCreated = categories.getDate("dateCreated").toLocalDate();
            LocalDate dateModified = null;
            try{
                dateModified = categories.getDate("dateModified").toLocalDate();
            }
            catch (Exception e){}
            int parentId = categories.getInt("parentId");
            int categoryId = categories.getInt("categoryId");
            Category cat = new Category(name, description, dateCreated, dateModified, categoryId, parentId);
            if(parentId == 0)
                this.categories.add(cat);
            else addCat(this.categories, cat);
        }
        categories.close();
    }

    public void addCat(ArrayList<Category> categories, Category cat)
    {
        for(Category c: categories)
        {
            if(c.getId() == (cat.getParentId())){
                c.getSubcategories().add(cat);
                break;
            }
            else
            {
                if(c.getSubcategories().size() > 0)
                    addCat(c.getSubcategories(), cat);
            }
        }
    }

}