package com.techm.netconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techm.netconf.model.UsersModel;
import java.util.Optional;


public interface UsersRepository extends JpaRepository<UsersModel, Integer>{
	
	Optional<UsersModel> findByLoginAndPassword(String login, String password);
	
}
