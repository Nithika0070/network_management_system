package com.techm.netconf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techm.netconf.model.UsersModel;
import com.techm.netconf.repository.UsersRepository;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	public UsersService(UsersRepository usersRepository) {
		super();
		this.usersRepository = usersRepository;
	}
	
	public UsersModel registerUser(String login, String password, String email) {
		if (login ==null && password==null) {
			return null;
		} else {
			UsersModel usersModel=new UsersModel(); //we dont use @autowired because we use'new' here
			usersModel.setLogin(login);
			usersModel.setPassword(password);
			usersModel.setEmail(email);
			return usersRepository.save(usersModel); //returns the saved entity—which is of type UsersModel.
		}
	}
	
	public UsersModel authenticate(String login, String password) {
		return usersRepository.findByLoginAndPassword(login, password).orElse(null);
	}

}
