package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, Model model, @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "category/{id}", method = RequestMethod.GET)
    public String displayCheeseByCategory(Model model, @PathVariable long id) {

        int catId = (int) id;
        Category cat = categoryDao.findOne(catId);
        List cheeseObjs = cat.getCheeses();
        //Iterable cheeses = cheeseDao.findAll(cheeseids);
        model.addAttribute("title", cat.getName() + " Cheeses");
        model.addAttribute("cheeses", cheeseObjs);

        return "cheese/index";
    }

    @RequestMapping(value="edit/{id}", method= RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable long id) {

        Cheese editedCheese = cheeseDao.findOne( (int) id);

        model.addAttribute("title", "Edit " + editedCheese.getName() + "(id=" + id + ")");
        model.addAttribute("cheese", editedCheese);

        return "cheese/edit";
    }

    @RequestMapping(value="edit", method= RequestMethod.POST)
    public String processEditForm(int cheeseId, String name, String description) {

        Cheese editedCheese = cheeseDao.findOne(cheeseId);

        editedCheese.setName(name);
        editedCheese.setDescription(description);

        cheeseDao.save(editedCheese);

        return "cheese/index";
    }

}
