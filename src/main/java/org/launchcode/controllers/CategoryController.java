package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by KyleLaptop on 4/10/2017.
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;
    //this is some hybernate magic, controllers are passed a Dao object

    @RequestMapping(value = "")
    private String index(Model model) {

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Categories");

        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    private String addCategory (Model model) {

        model.addAttribute("title", "Add a Category");
        model.addAttribute(new Category());

        return "category/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    private String addCategory (Model model, @ModelAttribute @Valid Category category,Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute(errors);
            model.addAttribute(new Category());
            model.addAttribute("title", "Add a Category");

            return "category/add";
        }

        categoryDao.save(category);
        return "redirect:"; //takes back to Category list at category/index or category/
    }
}
