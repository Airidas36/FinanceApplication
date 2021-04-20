package sample.databaseUtils;

import sample.Classes.User;
import sample.subClasses.individualPerson;
import sample.subClasses.legalPerson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.databaseUtils.DatabaseUtils.disconnectFromDb;

public class UserUtils {
    public static void delete(User user) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        String query = "";
        if (user.isLegalPerson())
            query = "DELETE FROM legaluser WHERE username=?";
        else query = "DELETE FROM user WHERE username=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static void delete(String username) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        String query = "DELETE from user where username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        query = "DELETE from legaluser where username=?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static void changePass(User user, String newPass) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        String query = "";
        if (user.isLegalPerson())
            query = "UPDATE legaluser Set password=? where username=?";
        else query = "UPDATE user Set password=? where username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newPass);
        preparedStatement.setString(2, user.getUsername());
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static User findUser(String name) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        String query;
        query = "SELECT * from legaluser where username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet legalusers = preparedStatement.executeQuery();
        if (legalusers.next() != false) {
            String username = legalusers.getString("username");
            String password = legalusers.getString("password");
            String permissions = legalusers.getString("permissions");
            String email = legalusers.getString("email");
            String companyName = legalusers.getString("companyName");
            String website = legalusers.getString("website");
            legalPerson legalPerson = new legalPerson(username, password, permissions, companyName, email, website, true);
            legalusers.close();
            preparedStatement.close();
            return legalPerson;
        } else {
            query = "SELECT * from user where username=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet user = preparedStatement.executeQuery();
            if (user.next() != false) {
                String username = user.getString("username");
                String password = user.getString("password");
                String permissions = user.getString("permissions");
                String email = user.getString("email");
                String phoneNum = user.getString("phoneNum");
                String position = user.getString("position");
                individualPerson individualPerson = new individualPerson(username, password, permissions, email, phoneNum, position, false);
                user.close();
                preparedStatement.close();
                return individualPerson;
            }
        }
        disconnectFromDb(connection, preparedStatement);
        return null;
    }

    public static void createEmpUser(String username, String password, String permissions, String email, String phoneNum, String position) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        String insert = "INSERT INTO user (USERNAME, PASSWORD, PERMISSIONS, EMAIL, PHONENUM, POSITION) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, permissions);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, phoneNum);
        preparedStatement.setString(6, position);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static void createLegalUser(String username, String password, String permissions, String email, String companyName, String website) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        String insert = "INSERT INTO legaluser (USERNAME, PASSWORD, PERMISSIONS, EMAIL, COMPANYNAME, WEBSITE) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, permissions);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, companyName);
        preparedStatement.setString(6, website);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }

    public static String empLogin(String username, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet user = null;
        try {
            connection = connectToDb();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT * from user where username=? and password=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            user = preparedStatement.executeQuery();
        } catch (Exception e) {
            disconnectFromDb(connection, preparedStatement);
            e.printStackTrace();
            return null;
        }

        if (user.next())
            return "Login successful.";
        else return "Failed.";
    }

    public static String legalLogin(String username, String password) throws SQLException {
        Connection connection = null;
        ResultSet user = null;
        try {
            connection = connectToDb();
            String query = "SELECT * from legaluser where username=? and password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            user = preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (user.next())
            return "Login successful.";
        else return "Failed.";
    }
}
