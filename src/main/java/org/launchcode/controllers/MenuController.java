package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by KyleLaptop on 4/11/2017.
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {

        Iterable menus = menuDao.findAll();

        model.addAttribute("menus", menus);
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value="add", method= RequestMethod.GET)
    public String addMenu(Model model) {

        model.addAttribute("title", "Add a Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value="add", method= RequestMethod.POST)
    public String processAddMenu(Model model,  @ModelAttribute @Valid Menu newMenu, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a Menu");
            return "menu/index";
        }

        menuDao.save(newMenu);

        model.addAttribute("title", "Added a Menu");

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value="view/{id}")
    public String menuview(Model model, @PathVariable() int id) {


        Menu menu = menuDao.findOne(id);

        model.addAttribute("title", menu.getName());
        model.addAttribute("menu", menu);

        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method = RequestMethod.GET)
    public String additem(Model model, @PathVariable() int id) {

        Menu ourMenu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = cheeseDao.findAll();

        AddMenuItemForm addItemForm = new AddMenuItemForm(cheeses, ourMenu);

        model.addAttribute("title", "Add item to menu: " + ourMenu.getName());
        model.addAttribute("form", addItemForm);

        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{id}", method = RequestMethod.POST)
    public String processAddItem(Model model, @ModelAttribute @Valid AddMenuItemForm returnedForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("");

            return "menu/add-item";
        }

        Menu ourMenu = menuDao.findOne(returnedForm.getMenuId());
        Cheese ourCheese = cheeseDao.findOne(returnedForm.getCheeseId());

        ourMenu.addItem(ourCheese);
        menuDao.save(ourMenu);

        return "menu/view/" + ourMenu.getId();
    }
}
