package com.techm.netconf.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity //creating a database entity
@Table(name="users_table")
public class UsersModel {
	
	@Id //to denote primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //don’t need to set the ID manually-auto increments (unique)
	Integer id;

	String login;
	
	String password;
	
	String email;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, login, password); //hashCode() method returns an int (integer) value, which serves as a numerical representation of the object. 
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsersModel other = (UsersModel) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(login, other.login)
				&& Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return "UsersModel [id=" + id + ", login=" + login + ", email=" + email + "]";
	}
	
	/*
	 * There is a crucial contract between the hashCode() and equals() methods: If
	 * two objects are considered equal according to their equals() method, their
	 * hashCode() methods must return the same hash code. If two objects are not
	 * equal, their hash codes are not required to be different; they can be
	 * different, but they may also be the same (a "hash collision").
	 */
	
	

}
