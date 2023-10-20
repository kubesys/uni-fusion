/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models.auth;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author wuheng@iscas.ac.cn
 * @version  1.2.0
 * @since    2023/07/04
 * 
 */
@Entity
@Table(name = "basic_role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends AuthBaseModel   {

	@Id
	@Column(name = "role", length = 32)
    private String role;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tokens", columnDefinition = "json")
    private JsonNode tokens;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "allows", columnDefinition = "json")
    private JsonNode allows;

	public Role() {
		super();
	}

	public Role(String role) {
		super();
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public JsonNode getTokens() {
		return tokens;
	}

	public void setTokens(JsonNode tokens) {
		this.tokens = tokens;
	}

	public JsonNode getAllows() {
		return allows;
	}

	public void setAllows(JsonNode allows) {
		this.allows = allows;
	}
	
}
