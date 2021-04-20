package sample.databaseUtils;

import com.google.gson.Gson;
import sample.Classes.Category;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.databaseUtils.DatabaseUtils.disconnectFromDb;

public class CategoryUtils {

    public static void update(String name, String description, int id, LocalDate modified) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        String query = "UPDATE category Set name = ?, description = ?, dateModified = ? WHERE categoryId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setDate(3, Date.valueOf(modified));
        preparedStatement.setInt(4, id);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static void create(Category newCat, Category parentCat) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        PreparedStatement preparedStatement = null;
        if (parentCat != null) {
            String query = "INSERT INTO category (name, description, dateCreated, dateModified, parentId) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newCat.getName());
            preparedStatement.setString(2, newCat.getDescription());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, null);
            preparedStatement.setInt(5, newCat.getParentId());
            preparedStatement.executeUpdate();
        } else {
            String query = "INSERT INTO category (name, description, dateCreated, dateModified) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newCat.getName());
            preparedStatement.setString(2, newCat.getDescription());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, null);
            preparedStatement.executeUpdate();
        }
        disconnectFromDb(connection, preparedStatement);
    }

    public static void create(String name, String description, String parentId) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        PreparedStatement preparedStatement = null;
        if (parentId != null) {
            String query = "INSERT INTO category (name, description, dateCreated, dateModified, parentId) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, null);
            preparedStatement.setInt(5, Integer.parseInt(parentId));
            preparedStatement.executeUpdate();
        } else {
            String query = "INSERT INTO category (name, description, dateCreated, dateModified) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, null);
            preparedStatement.executeUpdate();
        }
        disconnectFromDb(connection, preparedStatement);
    }

    public static void delete(int id) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        String query = "DELETE FROM category WHERE categoryId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static Category getCategory(int id) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM category WHERE categoryId=" + id;
        ResultSet cat = stmt.executeQuery(query);
        if (cat.next()) {
            Gson gson = new Gson();
            LocalDate dateModified = null;
            String name = cat.getString("name");
            String description = cat.getString("description");
            LocalDate dateCreated = cat.getDate("dateCreated").toLocalDate();
            try {
                dateModified = cat.getDate("dateModified").toLocalDate();
            } catch (Exception e) {
            }

            int parentId = cat.getInt("parentId");
            String income = cat.getString("income");
            String expense = cat.getString("expense");
            int categoryId = cat.getInt("categoryId");
            disconnectFromDb(connection, stmt);
            return new Category(name, description, dateCreated, dateModified, categoryId, parentId);
        }
        disconnectFromDb(connection, stmt);
        return null;
    }

    public static List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        List<Category> all = new ArrayList<>();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM category";
        ResultSet cat = stmt.executeQuery(query);
        while (cat.next()) {
            LocalDate dateModified = null;
            String name = cat.getString("name");
            String description = cat.getString("description");
            LocalDate dateCreated = cat.getDate("dateCreated").toLocalDate();
            if (cat.getDate("dateModified") != null)
                dateModified = cat.getDate("dateModified").toLocalDate();

            int parentId = cat.getInt("parentId");
            int categoryId = cat.getInt("categoryId");
            all.add(new Category(name, description, dateCreated, dateModified, categoryId, parentId, buildPath(parentId, "", connection)));
        }
        disconnectFromDb(connection, stmt);
        return all;
    }

    private static String buildPath(int parentId, String path, Connection connection) throws SQLException, ClassNotFoundException {
        if (parentId != 0) {
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM category WHERE categoryId=" + parentId;
            ResultSet cat = stmt.executeQuery(query);
            while (cat.next()) {
                String name = cat.getString("name");
                int parent = cat.getInt("parentId");
                path = path + "<" + name;
                buildPath(parent, path, connection);
            }
        }
        return path;
    }
}
