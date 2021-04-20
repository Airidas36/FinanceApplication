package sample.databaseUtils;

import sample.Classes.Finance;

import java.sql.*;
import java.util.ArrayList;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.databaseUtils.DatabaseUtils.disconnectFromDb;

public class FinanceUtils {
    public static ArrayList<Finance> getFinances(int categoryId) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finances = new ArrayList<>();
        Connection connection = connectToDb();
        String query = "SELECT * FROM finance where categoryId="+categoryId;
        Statement stmt = connection.createStatement();
        ResultSet fin = stmt.executeQuery(query);
        while(fin.next())
        {
            int finId = fin.getInt(1);
            String name = fin.getString(2);
            String description = fin.getString(3);
            String type = fin.getString(4);
            double amount = fin.getDouble(5);
            String source=  fin.getString(6);
            int catId = fin.getInt(7);
            finances.add(new Finance(name, description, type, source, amount, catId, finId));
        }
        System.out.println(finances);
        return finances;
    }
    public static void add(String name, String description, String type, double amount, String source, int categoryId) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO finance (name, description, type, amount, source, categoryId) VALUES(?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, type);
        preparedStatement.setDouble(4, amount);
        preparedStatement.setString(5, source);
        preparedStatement.setInt(6, categoryId);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }
    public static void update(String name, String description, String type, double amount, String source, int financeId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        System.out.println(name + " " + description + " " + type + " " + amount + " " + source + " " + financeId);
        connection = connectToDb();
        System.out.println("Failed to connect");
        String query = "UPDATE finance SET name = ?, description = ?, type = ?, amount = ?, source = ? where financeId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,description);
        preparedStatement.setString(3, type);
        preparedStatement.setDouble(4, amount);
        preparedStatement.setString(5,source);
        preparedStatement.setInt(6, financeId);
        preparedStatement.executeUpdate();
        disconnectFromDb(connection, preparedStatement);
    }
    public static void delete(int id) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        Statement stmt = connection.createStatement();
        String query = "DELETE from finance where financeId=" + id;
        stmt.execute(query);
        disconnectFromDb(connection, stmt);
    }

    public static ArrayList<Finance> getIncome(int categoryId) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finances = new ArrayList<>();
        Connection connection = connectToDb();
        String query = "SELECT * FROM finance where categoryId="+categoryId;
        Statement stmt = connection.createStatement();
        ResultSet fin = stmt.executeQuery(query);
        while(fin.next())
        {
            int finId = fin.getInt(1);
            String name = fin.getString(2);
            String description = fin.getString(3);
            String type = fin.getString(4);
            double amount = fin.getDouble(5);
            String source=  fin.getString(6);
            int catId = fin.getInt(7);
            if(amount > 0)
                finances.add(new Finance(name, description, type, source, amount, catId, finId));
        }
        System.out.println(finances);
        return finances;
    }

    public static ArrayList<Finance> getExpense(int categoryId) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finances = new ArrayList<>();
        Connection connection = connectToDb();
        String query = "SELECT * FROM finance where categoryId="+categoryId;
        Statement stmt = connection.createStatement();
        ResultSet fin = stmt.executeQuery(query);
        while(fin.next())
        {
            int finId = fin.getInt(1);
            String name = fin.getString(2);
            String description = fin.getString(3);
            String type = fin.getString(4);
            double amount = fin.getDouble(5);
            String source=  fin.getString(6);
            int catId = fin.getInt(7);
            if(amount < 0)
                finances.add(new Finance(name, description, type, source, amount, catId, finId));
        }
        System.out.println(finances);
        return finances;
    }

    public static Finance getFin(int finId) throws SQLException, ClassNotFoundException {
        Finance finance = null;
        Connection connection = connectToDb();
        String query = "SELECT * FROM finance where fin="+finId;
        Statement stmt = connection.createStatement();
        ResultSet fin = stmt.executeQuery(query);
        while(fin.next())
        {
            int financeId = fin.getInt(1);
            String name = fin.getString(2);
            String description = fin.getString(3);
            String type = fin.getString(4);
            double amount = fin.getDouble(5);
            String source=  fin.getString(6);
            int catId = fin.getInt(7);
            finance = new Finance(name, description, type, source, amount, catId, finId);
        }
        disconnectFromDb(connection, stmt);
        return finance;
    }
}
