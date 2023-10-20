/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models.auth;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  1.2.0
 * @since    2023/07/04
 * 
 */
@Entity
@Table(name = "basic_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AuthBaseModel   {

	@Id
	@Column(name = "name", length = 32)
    private String name;
	
	@Column(name = "password", length = 32)
    private String password;

	@Column(name = "role", length = 32)
    private String role;
	
	@Column(name = "token", length = 256)
    private String token;
	
	public User() {
		super();
	}
	public User(String token) {
		super();
		this.token = token;
	}

	public User(String role, String name, String password) {
		super();
		this.role = role;
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
