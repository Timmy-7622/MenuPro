package com.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.menu.model.Menu;
import com.menu.repository.MenuRepository;
import org.springframework.web.bind.annotation.GetMapping;





@Controller
@RequestMapping("menu")
public class UserMenuController {

	@Autowired
	private MenuRepository menuRepository;
	
	@GetMapping
	public String showUserMenu(Model model) {
		List<Menu> menus = menuRepository.findByStatus("上架");
		model.addAttribute("menuItems", menus);
		return "foodmenuuser/menu_cards";
	}
	

	@GetMapping("/cart")
	public String cartPage() {
		return "foodmenuuser/cart";
	}
	
	
}
