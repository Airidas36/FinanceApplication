package webControllers;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.Classes.AccountingSystem;
import sample.databaseUtils.UserUtils;
import sample.utils.HashUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

@Controller
public class WebUserController {
    @RequestMapping(value="user/userList", method= RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String userlist() throws SQLException, ClassNotFoundException {
        AccountingSystem accsys = new AccountingSystem("AiridasInc", "1.0.0", LocalDate.now()); // Instantiate system
        accsys.loadDatabase();
        Gson gson = new Gson();
        return gson.toJson(accsys.getUsers());
    }

    @RequestMapping(value = "/user/userLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody String request) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String password = HashUtils.hash(data.getProperty("password"));
        return UserUtils.empLogin(username,password);
    }

    @RequestMapping(value = "/user/legalLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String legalLogin(@RequestBody String request) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String password = HashUtils.hash(data.getProperty("password"));
        return UserUtils.legalLogin(username, password);
    }

    @RequestMapping(value = "/user/registerEmp", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String registerEmp(@RequestBody String request) throws NoSuchAlgorithmException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String password = HashUtils.hash(data.getProperty("password"));
        String permissions = data.getProperty("permissions");
        String email = data.getProperty("email");
        String phoneNum = data.getProperty("phoneNum");
        String position = data.getProperty("position");
        try{
            UserUtils.createEmpUser(username, password, permissions, email, phoneNum, position);
            return "User created.";
        }catch (Exception e){
            return "Failed to create user";
        }
    }

    @RequestMapping(value = "/user/registerLegal", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String registerLegal(@RequestBody String request) throws NoSuchAlgorithmException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String password = HashUtils.hash(data.getProperty("password"));
        String permissions = data.getProperty("permissions");
        String email = data.getProperty("email");
        String companyName = data.getProperty("companyName");
        String website = data.getProperty("website");
        try{
            UserUtils.createLegalUser(username, password, permissions, email, companyName, website);
            return "User created.";
        }catch (Exception e){
            return "Failed to create user";
        }
    }

    @RequestMapping(value = "/user/deleteUser", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteUser(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        try{
            if(UserUtils.findUser(username) == null)
                return "User does not exist.";
        }catch (Exception e){
            return "Failed to check up if user exists.";
        }
        try{
            UserUtils.delete(username);
            return "User deleted.";
        } catch (Exception e){
            return "Failed to delete user.";
        }
    }
}
