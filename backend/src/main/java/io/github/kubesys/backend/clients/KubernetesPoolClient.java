/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.clients;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.kubesys.backend.models.auth.Role;
import io.github.kubesys.client.KubernetesClient;

/**
 * @author  wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since   2023/07/04
 * 
 */
@Component
public class KubernetesPoolClient {

	private static Map<String, KubernetesClient> kubeClients = new HashMap<>();
	
	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	
	public KubernetesClient getKubeClient(String region) throws Exception {
		if (kubeClients.containsKey(region)) {
			return kubeClients.get(region);
		}
		
		Role role = (Role) postgresClient.find(Role.class, "admin");
		String url = role.getTokens().get(region).get("url").asText();
		String token = role.getTokens().get(region).get("token").asText();
		KubernetesClient client = new KubernetesClient(url, token);
		kubeClients.put(region, client);
		return client;
	}

}
