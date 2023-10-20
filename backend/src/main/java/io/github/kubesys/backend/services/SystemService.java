/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.kubesys.backend.clients.PostgresPoolClient;
import io.github.kubesys.backend.models.auth.User;
import io.github.kubesys.devfrk.spring.constants.ExceptionConstants;
import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.spring.exs.DataBaseWrongUserOrPasswordException;
import io.github.kubesys.devfrk.spring.utils.JSONUtils;
import io.github.kubesys.devfrk.spring.utils.StringUtils;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;

/**
 * 权限这块后续慢慢改
 * 
 * @author wuheng@iscas.ac.cn
 *
 */
@ServiceDefinition
public class SystemService extends AbstractHttpHandler {

	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	public String login(User data) throws Exception {
		try {
			
			// 根据主键name查询该用户完整信息
			User info = (User) postgresClient.find(User.class, data.getName());
			
			if (!info.getPassword().equals(data.getPassword())) {
				throw new DataBaseWrongUserOrPasswordException(
						ExceptionConstants.DB_WRONG_USERNAME_OR_PASSWORD);
			}
			
			// 如果没有token，则直接生成
			String token = info.getToken();
			if (token == null || token.length() == 0) {
				token = StringUtils.getRandomString(256);
				info.setToken(token);
				postgresClient.updateObject(token, JSONUtils.from(info));
			}
			// 返回给用户token
			return token;
		} catch (Exception ex) {
			// 查询不到报错，提示用户名密码错误
			throw new DataBaseWrongUserOrPasswordException(
					ExceptionConstants.DB_WRONG_USERNAME_OR_PASSWORD);
		}
	}
	
	public void logout(User user) throws Exception {
		postgresClient.removeObject(User.class.getName(), JSONUtils.from(user));
	}

}
