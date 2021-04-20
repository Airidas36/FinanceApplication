package webControllers;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.Classes.AccountingSystem;
import sample.Classes.Category;
import sample.databaseUtils.CategoryUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
public class WebCategoryController {
    @RequestMapping(value = "category/categoryList", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCats() throws SQLException, ClassNotFoundException {
        AccountingSystem accsys = new AccountingSystem("AiridasInc", "1.0.0", LocalDate.now()); // Instantiate system
        accsys.loadDatabase();
        Gson gson = new Gson();
        String categories = gson.toJson(accsys.getCategories());
        return categories;
    }

    @RequestMapping(value = "category/onlyCats", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String onlyCats() throws SQLException, ClassNotFoundException {
        List<Category> allCats = CategoryUtils.getAllCategories();
        Gson gson = new Gson();
        String categories = gson.toJson(allCats);
        return categories;
    }

    @RequestMapping(value = "category/updateCategory", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCat(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int categoryId = Integer.parseInt(data.getProperty("categoryId"));
        String newName = data.getProperty("newName");
        String newDesc = data.getProperty("newDesc");

        try {
            CategoryUtils.update(newName, newDesc, categoryId, LocalDate.now());
        } catch (Exception e) {
            return "Error while updating.";
        }
        return "Category updated.";
    }

    @RequestMapping(value = "category/createCategory", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        String parentId = data.getProperty("parentId");
        if(parentId != null){
            if(CategoryUtils.getCategory(Integer.parseInt(parentId)) == null)
                return "No such parent category.";
        }
        try{
            CategoryUtils.create(name, description, parentId);
            return "Category created.";
        }
        catch (Exception e){
            return "Error creating category.";
        }

    }

    @RequestMapping(value = "category/deleteCategory", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCategory(@RequestBody String request){
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int id = Integer.parseInt(data.getProperty("categoryId"));
        try{
            CategoryUtils.delete(id);
            return "Category deleted.";
        }
        catch (Exception e){
            return "Failed to delete category.";
        }
    }
}
