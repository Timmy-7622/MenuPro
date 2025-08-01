package com.menu.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.menu.model.Menu;
import com.menu.repository.MenuRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;






@Controller
@RequestMapping("/admin/menu")
public class MenuController {
	@Autowired
	private MenuRepository menuRepository;
	
	
	
	@GetMapping("/list")
	public String showMenuList(Model model) {
		List<Menu> menus = menuRepository.findByStatus("上架");
		for (Menu m : menus) {
	        System.out.println("圖片路徑：" + m.getImageUrl());
	    }
		model.addAttribute("menuItems", menus);
		return "menu_list";
	}
	
	@GetMapping("/new")
	public String showCreatMenu(Model model) {
		model.addAttribute("menu", new Menu());
		return "menu_creat";
	}
	
	@PostMapping("/save")
	public String saveMenu(@ModelAttribute Menu menu,
						   @RequestParam("imageFile") MultipartFile imageFile) {
		if(!imageFile.isEmpty()) {
			try {
				String projectRoot = System.getProperty("user.dir");
				Path uploadPath = Paths.get(projectRoot, "uploads");
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				String filename = imageFile.getOriginalFilename();
				Path filePath = uploadPath.resolve(filename);
				Files.copy(imageFile.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);
				menu.setImageUrl("/uploads/" + filename);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		menuRepository.save(menu);		
		return "redirect:/admin/menu/list";
	}
	
	@GetMapping("/delete/{id}")
	public String softDelete(@PathVariable("id") Long id) {
		Menu menu = menuRepository.findById(id).orElse(null);
		if(menu != null) {
			menu.setStatus("下架");
			menuRepository.save(menu);
		}
		return "redirect:/admin/menu/list";
	}
	
	@GetMapping("/edit/{id}")
	public String EditForm(@PathVariable("id") Long id, Model model) {
		Menu menu = menuRepository.findById(id).orElse(null);
		model.addAttribute("menu", menu);
		return "menu_edit";
	}
	
	@PostMapping("/update")
	public String UpdateMenu(@ModelAttribute Menu menu,
			                 @RequestParam("imageFile") MultipartFile imageFile) {
		try {
			if(!imageFile.isEmpty()) {
				String projectRoot = System.getProperty("user.dir");
				Path uploadPath = Paths.get(projectRoot, "uploads");
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				String filename = imageFile.getOriginalFilename();
				Path filePath = uploadPath.resolve(filename);
				Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				menu.setImageUrl("/uploads/" + filename);
			}else {
				Menu original = menuRepository.findById(menu.getMenuId()).orElse(null);
				if(original != null) {
					menu.setImageUrl(original.getImageUrl());
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		menuRepository.save(menu);
		return "redirect:/admin/menu/list";
	}
	
	
	
	

}
