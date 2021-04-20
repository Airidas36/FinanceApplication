package webControllers;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.Classes.Finance;
import sample.databaseUtils.FinanceUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import static sample.databaseUtils.DatabaseUtils.connectToDb;
import static sample.databaseUtils.DatabaseUtils.disconnectFromDb;

@Controller
public class WebFinanceController {

    @RequestMapping(value="finance/getSize", method= RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean getSize(int id) throws SQLException, ClassNotFoundException {
        Connection connection = connectToDb();
        Statement stmt = connection.createStatement();
        String query = "SELECT * from finance where categoryId=" + id;
        ResultSet finances = stmt.executeQuery(query);
        return finances.next();
    }

    @RequestMapping(value="finance/getFinances", method= RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ArrayList<Finance> getFinances(@RequestParam("id") String id) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finlist = new ArrayList<>();
        Connection connection = connectToDb();
        Statement stmt = connection.createStatement();
        String query = "SELECT * from finance where categoryId=" + id;
        ResultSet finances = stmt.executeQuery(query);
        while(finances.next())
        {
            int finId = finances.getInt(1);
            String name = finances.getString(2);
            String description = finances.getString(3);
            String type = finances.getString(4);
            double amount = finances.getDouble(5);
            String source = finances.getString(6);
            int categoryId = finances.getInt(7);
            finlist.add(new Finance(name, description, type, source, amount, categoryId, finId));
        }
        disconnectFromDb(connection,stmt);
        return finlist;
    }

    @RequestMapping(value="finance/getIncome", method= RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ArrayList<Finance> getIncome (@RequestParam("id") String id) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finlist = new ArrayList<>();
        finlist = FinanceUtils.getIncome(Integer.parseInt(id));
        return finlist;
    }

    @RequestMapping(value="finance/getExpense", method= RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ArrayList<Finance> getExpense (@RequestParam("id") String id) throws SQLException, ClassNotFoundException {
        ArrayList<Finance> finlist = new ArrayList<>();
        finlist = FinanceUtils.getExpense(Integer.parseInt(id));
        return finlist;
    }

    @RequestMapping(value="finance/updateFinances", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void updateFinances(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        String type = data.getProperty("type");
        double amount = Double.parseDouble(data.getProperty("amount"));
        String source = data.getProperty("source");
        int finId = Integer.parseInt(data.getProperty("id"));
        FinanceUtils.update(name,description,type, amount, source, finId);
    }

    @RequestMapping(value="finance/deleteFinance", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteFinance(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        FinanceUtils.delete(Integer.parseInt(id));
    }


}
