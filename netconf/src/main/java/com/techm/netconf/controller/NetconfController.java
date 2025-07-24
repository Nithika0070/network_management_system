package com.techm.netconf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techm.netconf.model.NetconfModel;
import com.techm.netconf.model.UsersModel;
import com.techm.netconf.repository.NetconfRepository;
import com.techm.netconf.service.NetconfDeletionService;
import com.techm.netconf.service.NetconfProvisionService;
import com.techm.netconf.service.NetconfService;
import com.techm.netconf.service.UsersService;

@Controller
@RequestMapping("/netconf")
public class NetconfController {

    @Autowired
    private NetconfService service;
    
    @Autowired
    private NetconfProvisionService provision;
    
    @Autowired
    private NetconfDeletionService deletion;
    
    @Autowired
    private NetconfRepository repository;
    
    @Autowired
	private UsersService usersService;
	
	@GetMapping("/register") //register==url path &&  client (like a browser or Postman) sends a GET request to a specific URL, Spring looks for a controller method annotated with @GetMapping that matches the URL.
	public String getRegisterPage(Model model) {
		model.addAttribute("registerRequest", new UsersModel());
		/*
		 * - Adds an object to the model under the name "registerRequest" 
		 * - Thymeleaf can then reference it using ${registerRequest} and bind it to form fields
		 * using th:field="*{...}" that is pass data from controller to view
		 */
		return "register_page"; // Renders register_page.html from templates/
	}
	
	@GetMapping("/login")
	public String getLoginPage(Model model) { //Model delivers its instance
		model.addAttribute("loginRequest", new UsersModel());
		return "login_page";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute UsersModel usersModel) { // registerRequest now has login, email, password from the form
		System.out.println("register request: " + usersModel); //debug -> is usersModel getting info from the html page
		UsersModel registeredUser= usersService.registerUser(usersModel.getLogin(), usersModel.getPassword(),usersModel.getEmail()); //this is only the adding to database section
		return registeredUser == null ? "error_page" : "redirect:/netconf/login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute UsersModel usersModel, Model model) { //In Spring MVC, @ModelAttribute is a powerful annotation used to bind form data or request parameters to a model object and make that object available to the view layer.
		System.out.println("login request: " + usersModel);
		UsersModel authenticated= usersService.authenticate(usersModel.getLogin(), usersModel.getPassword());
		if (authenticated != null) {
			model.addAttribute("userLogin", authenticated.getLogin()); //variable called userlogin with the value from getlogin -> used in view
			return "redirect:/netconf/sync";
		}else {
			return "error_page";
		}
	}

    @GetMapping("/sync")
    public String syncNodes(Model model) {
//        service.fetchAndStoreNodes();
//        return ResponseEntity.ok("Nodes synced to DB");
//    	return service.fetchAndStoreNodes();
//    	List<NetconfModel> nodes = service.fetchAndStoreNodes();
    	service.fetchAndStoreNodes();
    	List<NetconfModel> allNodes = repository.findAll();
        model.addAttribute("nodes", allNodes);
        return "netconf_nodes";
    }
    
    @GetMapping("/provision")
    public String showProvisionPage(Model model) {
        model.addAttribute("netconfRequest", new NetconfModel());
        return "provision_page";
    }

    @PostMapping("/provision")
    public String provisionDevice(@ModelAttribute NetconfModel model, Model responseModel) {
        NetconfModel provisioned = provision.createDevice(model);
        responseModel.addAttribute("message", "Device provisioned with ID: " + provisioned.getNodeId());
        return "netconf_nodes";
    }
    
    @PostMapping("/node/{nodeId}")
    public String deleteSingleNode(@PathVariable String nodeId) {
    	deletion.deleteNodeEverywhere(nodeId);
        //RedirectAttributes.addFlashAttribute("message", success ? "Deleted node: " + nodeId : "Failed to delete node: " + nodeId);
        return "redirect:/netconf/sync";
    }

    @PostMapping("/node")
    public String deleteAllNodes() {
        deletion.deleteAllNodes();
        return "redirect:/netconf/sync";
    }
    
//works only on postman as springboot doesn't directly support put and delete operation
    @DeleteMapping("/node/{nodeId}")
    public ResponseEntity<String> removeDevice(@PathVariable String nodeId) {
        boolean result = deletion.deleteNodeEverywhere(nodeId);
        if (result) {
            return ResponseEntity.ok("Deleted node: " + nodeId);
        } else {
            return ResponseEntity.status(404).body("Node not found: " + nodeId);
        }
    }
    
    @DeleteMapping("/node")
    public ResponseEntity<String> removeAllDevices() {
        deletion.deleteAllNodes();
        return ResponseEntity.ok("All nodes deleted from database and simulator.");
    }


}