/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.sources;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;
import io.github.kubesys.client.cores.KubernetesRuleBase;
import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.datas.KubeDataModel;

/**
 * @author wuheng@iscas.ac.cn
 * @version 0.0.1
 * @since 2023/06/18
 *
 */
public class KubeSourceListener extends AbstractKubeSource {

	/**
	 * @param dataTarget dataTarget
	 * @throws Exception Exception
	 */
	public KubeSourceListener(DataTarget<KubeDataModel> dataTarget) throws Exception {
		super(dataTarget);
	}

	@Override
	public void startCollect() throws Exception {
		kubeClient.watchResources("apiextensions.k8s.io.CustomResourceDefinition", 
				new NewResourceWacther(kubeClient, this));
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
		
		
		protected final KubeSourceListener listener;
		
		public NewResourceWacther(KubernetesClient client, KubeSourceListener listener) {
			super(client);
			this.listener = listener;
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
				listener.doStartCollect(apiGroup + "." + kind, toValue(node));
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
							new NewResourceWacther(client, listener));
					break;
				} catch (Exception e) {
					m_logger.info("fail to restart watcher apiextensions.k8s.io.CustomResourceDefinition: " + e.toString());
					Thread.currentThread().interrupt();
				}
				
			}
		}

	}
}
