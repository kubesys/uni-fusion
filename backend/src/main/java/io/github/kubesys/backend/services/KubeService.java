/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.backend.clients.KubernetesPoolClient;
import io.github.kubesys.backend.clients.PostgresPoolClient;
import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;
import io.github.kubesys.mirror.cores.clients.builers.PostgresSQLBuilder;
import io.github.kubesys.mirror.utils.SQLUtil;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  1.2.0
 * @since    2023/06/28
 *
 * 提供以下服务
 * 
 * - Kubernetes
 *    - CreateResource
 *    - UpdateResource
 *    - UpdateResourceStatus
 *    - DeleteResource
 * - Database
 *    - GetResource
 *    - ListResources
 * - Metadata
 *    - getMetadata
 *    
 */

@ServiceDefinition
public class KubeService extends AbstractHttpHandler {

	/**
	 * Kubernetes客户端
	 */
	@Autowired
	protected KubernetesPoolClient kubeClient;
	
	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	
	
	/*************************************************************************
	 * 
	 * 
	 *       Invoking Kubernetes
	 * 
	 * 
	 **************************************************************************/
	

	public JsonNode getMeta(String region) throws Exception {
		return kubeClient.getKubeClient(region).getKindDesc();
	}
	
	/**
	 * 创建资源
	 * 
	 * @param data                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode createResource(
			String region,
			JsonNode data)
			throws Exception {

		return kubeClient.getKubeClient(region).createResource(data);
	}

	/**
	 * see KubernetesClient.updateResource
	 * 
	 * @param data                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode updateResource(
			String region,
			JsonNode data)
			throws Exception {
		return kubeClient.getKubeClient(region).updateResource(data);
	}

	/**
	 * see KubernetesClient.createResource and KubernetesClient.updateResource
	 * 
	 * @param data                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode createOrUpdateResource(
			String region,
			JsonNode data)
			throws Exception {
		try {
			return kubeClient.getKubeClient(region).createResource(data);
		} catch (Exception e) {
			return kubeClient.getKubeClient(region).updateResource(data);
		}
	}

	/**
	 * see KubernetesClient.deleteResource
	 * 
	 * @param token                   token
	 * @param fullkind                kind
	 * @param namespace               namespace
	 * @param name                    name
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public JsonNode deleteResource(
			String fullkind,
			String name,
			String namespace,
			String region) 
			throws Exception {

		return kubeClient.getKubeClient(region).deleteResource(fullkind, namespace, name);
	}

	/**
	 * see KubernetesClient.getResource
	 * 
	 * @param fullkind                fullkind
	 * @param namespace               namespace
	 * @param name                    name
	 * @param region                  region
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public Object getResource(
			String fullkind,
			String name,
			String namespace,
			String region) throws Exception {
		
		String plural = kubeClient.getKubeClient(region).getKindDesc().get(fullkind).get("plural").asText();
		String table = SQLUtil.table(plural);
		
		String group = KubeUtil.getGroup(fullkind);
		Map<String, String> map = new HashMap<>();
		map.put("name", name);
		map.put("namespace", namespace);
		map.put("apigroup", group);
		map.put("region", region);
		
		String sql = new PostgresSQLBuilder().getSQL(table, map);
		try {
			return postgresClient.getObject(fullkind, sql);
		} catch (Exception ex) {
			if (PostgresPoolClient.tables.containsKey(fullkind)) {
				return new ObjectMapper().createObjectNode();
			}
			throw ex;
		}
	}

	
	/*************************************************************************
	 * 
	 * 
	 *       Invoking Database
	 * 
	 * 
	 **************************************************************************/
	
	/**
	 * equal to see KubernetesClient.listResources
	 * 
	 * @param token                   token
	 * @param fullkind                    kind
	 * @param namespace               namespace
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public JsonNode listResources(
			String fullkind,
			int limit,
			int page,
			Map<String, String> labels,
			String region)
			throws Exception {
		
		JsonNode kindDesc = kubeClient.getKubeClient(region).getKindDesc().get(fullkind);
		
		String plural = kindDesc.get("plural").asText();
		String table = SQLUtil.table(plural);
		
		ObjectNode json = new ObjectMapper().createObjectNode();
		json.put("apiVersion", kindDesc.get("apiVersion").asText());
		json.put("kind", kindDesc.get("kind").asText() + "List");
		ObjectNode meta = new ObjectMapper().createObjectNode();
		long totalCount = postgresClient.countObjects(fullkind,
				new PostgresSQLBuilder().countSQL(table, labels));
		meta.put("name", "get-" + plural);
		meta.put("totalCount", totalCount);
		meta.put("currentPage", page);
		meta.put("totalPage", (totalCount%limit == 0) ? totalCount/limit : totalCount/limit + 1);
		meta.put("itemsPerPage", limit);
		meta.put("conditions", new ObjectMapper().writeValueAsString(labels));
		json.set("metadata", meta);
		try {
			json.set("items",postgresClient.listObjects(fullkind,
				new PostgresSQLBuilder().listSQL(table, labels, page, limit)));
		} catch (Exception ex) {
			if (PostgresPoolClient.tables.containsKey(fullkind)) {
				return json;
			}
			throw ex;
		}
		return json;
	}

}
