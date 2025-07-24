//package com.techm.netconf.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.techm.netconf.model.UsersModel;
//import com.techm.netconf.service.UsersService;
//
//@Controller
//public class UsersController {
//	
//	@Autowired
//	private UsersService usersService;
//	
//	@GetMapping("/register") //register==url path &&  client (like a browser or Postman) sends a GET request to a specific URL, Spring looks for a controller method annotated with @GetMapping that matches the URL.
//	public String getRegisterPage(Model model) {
//		model.addAttribute("registerRequest", new UsersModel());
//		/*
//		 * - Adds an object to the model under the name "registerRequest" 
//		 * - Thymeleaf can then reference it using ${registerRequest} and bind it to form fields
//		 * using th:field="*{...}" that is pass data from controller to view
//		 */
//		return "register_page"; // Renders register_page.html from templates/
//	}
//	
//	@GetMapping("/login")
//	public String getLoginPage(Model model) { //Model delivers its instance
//		model.addAttribute("LoginRequest", new UsersModel());
//		return "login_page";
//	}
//	
//	@PostMapping("/register")
//	public String register(@ModelAttribute UsersModel usersModel) { // registerRequest now has login, email, password from the form
//		System.out.println("register request: " + usersModel); //debug -> is usersModel getting info from the html page
//		UsersModel registeredUser= usersService.registerUser(usersModel.getLogin(), usersModel.getPassword(),usersModel.getEmail()); //this is only the adding to database section
//		return registeredUser == null ? "error_page" : "redirect:/login";
//	}
//	
//	@PostMapping("/login")
//	public String login(@ModelAttribute UsersModel usersModel, Model model) { //In Spring MVC, @ModelAttribute is a powerful annotation used to bind form data or request parameters to a model object and make that object available to the view layer.
//		System.out.println("login request: " + usersModel);
//		UsersModel authenticated= usersService.authenticate(usersModel.getLogin(), usersModel.getPassword());
//		if (authenticated != null) {
//			model.addAttribute("userLogin", authenticated.getLogin()); //variable called userlogin with the value from getlogin -> used in view
//			return "personal_page";
//		}else {
//			return "error_page";
//		}
//	}
//
//}
