/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;
import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.datas.KubeDataModel;

/**
 * @author wuheng@iscas.ac.cn
 * @version 0.0.1
 * @since 2023/06/18
 *
 */
@Deprecated
public class KubeSourceListener extends AbstractKubeSource {

	static JsonNode verbs;

	static {
		try {
			verbs = new ObjectMapper().readTree(KubernetesConstants.KUBE_DEFAULT_KIND_VERBS);
		} catch (JsonProcessingException e) {
			// ignore here
		}
	}

	/**
	 * @param dataTarget dataTarget
	 * @throws Exception Exception
	 */
	public KubeSourceListener(DataTarget<KubeDataModel> dataTarget) throws Exception {
		super(dataTarget);
	}

	@Override
	public void startCollect() throws Exception {
		kubeClient.watchResources(KubernetesConstants.KUBD_FULLKIND_CUSTOMRESOURCEDEFINITION,
				new CrdWatcher(kubeClient, KubernetesConstants.KUBD_FULLKIND_CUSTOMRESOURCEDEFINITION, this));
	}

	/**
	 * 
	 * This is KubeSourceExtractor input
	 * 
	 * "storage.k8s.io.VolumeAttachment" : { "apiVersion" : "storage.k8s.io/v1",
	 * "kind" : "VolumeAttachment", "plural" : "volumeattachments", "verbs" : [
	 * "create", "delete", "deletecollection", "get", "list", "patch", "update",
	 * "watch" ] }
	 * 
	 * @return { "apiVersion" : "storage.k8s.io/v1", "kind" : "VolumeAttachment",
	 *         "plural" : "volumeattachments", "verbs" : [ "create", "delete",
	 *         "deletecollection", "get", "list", "patch", "update", "watch" ] }
	 */
	static JsonNode toValue(JsonNode crd) {
		ObjectNode value = new ObjectMapper().createObjectNode();
		value.put(KubernetesConstants.KUBE_APIVERSION, KubeUtil.getCrdApiversion(crd));
		value.put(KubernetesConstants.KUBE_KIND, KubeUtil.getCrdKind(crd));
		value.put(KubernetesConstants.KUBE_PLURAL, KubeUtil.getCrdPlural(crd));
		value.set(KubernetesConstants.KUBE_SPEC_NAMES_VERBS, verbs);
		return value;
	}
	
	
	/**
	 * @author wuheng@iscas.ac.cn
	 * @version 0.1.0
	 * @since   2023/06/22
	 *
	 */
	static class CrdWatcher extends KubernetesWatcher {
		
		/**
		 * 全称=group+kind
		 */
		protected final String fullKind;
		
		/**
		 * 监听器
		 */
		protected final KubeSourceListener listener;
		
		
		protected CrdWatcher(KubernetesClient client, String fullKind, KubeSourceListener listener) {
			super(client);
			this.fullKind = fullKind;
			this.listener = listener;
		}


		@Override
		public void doModified(JsonNode node) {
			// ignore here
		}

		@Override
		public void doDeleted(JsonNode node) {
			String fullkind = KubeUtil.getFullkind(node);
			fullkindToMeta.remove(fullkind);
			ignoredFullkinds.remove(fullkind);
			Thread thread = fullkindToWatcher.remove(fullkind);
			thread.stop();
		}

		@Override
		public void doAdded(JsonNode node) {
			String fullkind = KubeUtil.getCRDFullkind(node);
			JsonNode value = toValue(node);
			try {
				listener.doStartCollect(fullkind, value);
			} catch (Exception e) {
			}
		}

		@Override
		public void doClose() {
			try {
				Thread.sleep(3000);
				client.watchResources(KubernetesConstants.KUBD_FULLKIND_CUSTOMRESOURCEDEFINITION,
						new CrdWatcher(client, KubernetesConstants.KUBD_FULLKIND_CUSTOMRESOURCEDEFINITION, listener));
			} catch (Exception e) {
				doClose();
			}
		}

	}

}
