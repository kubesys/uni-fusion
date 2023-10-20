/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.backend.clients.PostgresPoolClient;
import io.github.kubesys.backend.models.auth.Role;
import io.github.kubesys.backend.models.auth.User;
import io.github.kubesys.devfrk.spring.auth.AuthingModel;
import io.github.kubesys.devfrk.spring.cores.HttpAuthingInterceptor;

/**
 * @author wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since 2023/07/07
 * 
 * 
 */
public class DefaultHttpAuthing implements HttpAuthingInterceptor  {

	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	@Override
	public boolean check(AuthingModel auth, String type, String kind) {
		// 说明注册的方法不需要kind或者fullkind，直接返回true
		if (kind == null) {
			return true;
		}
		
		try {
			User user = (User) postgresClient.find(User.class, auth.getUser());
			if (!user.getToken().equals(auth.getToken())) {
				throw new Exception();
			}
			Role role = (Role) postgresClient.find(Role.class, user.getRole());
			JsonNode allows = role.getAllows();
			if (allows.has("all")) {
				return true;
			}
			return allows.get(type).get(kind).asBoolean();
		} catch (Exception e) {
		}
		return false;
	}


}
