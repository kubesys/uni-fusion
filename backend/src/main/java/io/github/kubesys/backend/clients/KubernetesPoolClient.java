/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.clients;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.backend.models.auth.Role;
import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;
import io.github.kubesys.client.cores.KubernetesRuleBase;
import io.github.kubesys.client.utils.KubeUtil;

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
		client.watchResources("apiextensions.k8s.io.CustomResourceDefinition", 
				new NewResourceWacther(client));
		kubeClients.put(region, client);
		return client;
	}
	
	public static class NewResourceWacther extends KubernetesWatcher {

		/**
		 * m_logger
		 */
		public static final Logger m_logger = Logger.getLogger(NewResourceWacther.class.getName());
		
		static JsonNode verbs;

		static {
			try {
				verbs = new ObjectMapper().readTree(KubernetesConstants.KUBE_DEFAULT_KIND_VERBS);
			} catch (JsonProcessingException e) {
				// ignore here
			}
		}
		
		
		public NewResourceWacther(KubernetesClient client) {
			super(client);
		}

		@Override
		public void doAdded(JsonNode node) {
			
			JsonNode spec = node.get(KubernetesConstants.KUBE_SPEC);
			String apiGroup  = spec.get(KubernetesConstants.KUBE_SPEC_GROUP).asText();
			String version = spec.get(KubernetesConstants.KUBE_SPEC_VERSIONS)
								.iterator().next().get(KubernetesConstants
										.KUBE_SPEC_VERSIONS_NAME).asText();
			String kind = spec.get(KubernetesConstants.KUBE_SPEC_NAMES).get(
							KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
			
			try {
				client.getAnalyzer().registry.registerKinds(client, KubernetesConstants.VALUE_APIS + "/" + apiGroup + "/" + version);
			} catch (Exception e) {
				m_logger.severe(e.toString());
			}
			
		}



		static JsonNode toValue(JsonNode crd) {
			ObjectNode value = new ObjectMapper().createObjectNode();
			value.put(KubernetesConstants.KUBE_APIVERSION, KubeUtil.getCrdApiversion(crd));
			value.put(KubernetesConstants.KUBE_KIND, KubeUtil.getCrdKind(crd));
			value.put(KubernetesConstants.KUBE_PLURAL, KubeUtil.getCrdPlural(crd));
			value.set(KubernetesConstants.KUBE_SPEC_NAMES_VERBS, verbs);
			return value;
		}
		
		@Override
		public void doDeleted(JsonNode node) {
			
			JsonNode spec = node.get(KubernetesConstants.KUBE_SPEC);
			JsonNode names = spec.get(KubernetesConstants.KUBE_SPEC_NAMES);
			
			String shortKind = names.get(KubernetesConstants.KUBE_SPEC_NAMES_KIND).asText();
			String apiGroup  = spec.get(KubernetesConstants.KUBE_SPEC_GROUP).asText();
			String fullKind  = apiGroup + "." + shortKind;
			
			KubernetesRuleBase ruleBase = client.getAnalyzer().convertor.getRuleBase();
			ruleBase.removeFullKind(shortKind, fullKind);
			
			ruleBase.removeKindBy(fullKind);
			ruleBase.removeNameBy(fullKind);
			ruleBase.removeGroupBy(fullKind);
			ruleBase.removeVersionBy(fullKind);
			ruleBase.removeNamespacedBy(fullKind);
			ruleBase.removeApiPrefixBy(fullKind);
			ruleBase.removeVerbsBy(fullKind);
			
			m_logger.info("unregister" + shortKind);
		}

		@Override
		public void doModified(JsonNode node) {
			// ignore here
		}

		@Override
		public void doClose() {
			while (true) {
				try {
					m_logger.info("watcher apiextensions.k8s.io.CustomResourceDefinition is crash");
					m_logger.info("wait 5 seconds to restart watcher apiextensions.k8s.io.CustomResourceDefinition ");
					Thread.sleep(5000);
					client.watchResources("apiextensions.k8s.io.CustomResourceDefinition", 
							KubernetesConstants.VALUE_ALL_NAMESPACES, 
							new NewResourceWacther(client));
					break;
				} catch (Exception e) {
					m_logger.info("fail to restart watcher apiextensions.k8s.io.CustomResourceDefinition: " + e.toString());
					Thread.currentThread().interrupt();
				}
				
			}
		}

	}

}
