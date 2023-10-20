/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.utils;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.datas.KubeDataModel.Meta;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/23
 *
 */
public class MirrorUtil {

	private MirrorUtil() {
		super();
	}

	
	/**
	 * 如果Client为空，后面业务都无法开展，则直接退出
	 * 
	 * @param client     客户端
	 */
	public static void checkNull(Object client) {
		if (client == null) {
			System.exit(1);
		}
	}
	
	
	/**
	 * 得到KubeData的元数据meta，不考虑raw为空或者不符合Kuberneres资源定义的的情况
	 * 否则抛出异常
	 * https://kubernetes.io/docs/concepts/overview/working-with-objects/
	 * 
	 * @param crd     原始数据
	 * @return 得到KubeData的元数据meta
	 */
	public static Meta toKubeMeta(JsonNode crd) {
		Meta meta = new Meta();
		meta.setFullkind(KubeUtil.getCRDFullkind(crd));
		meta.setGroup(KubeUtil.getCrdGroup(crd));
		meta.setKind(KubeUtil.getCrdKind(crd));
		meta.setPlural(KubeUtil.getCrdPlural(crd));
		return meta;
	}
	
	/**
	 * 得到KubeData的元数据meta，不考虑raw为空或者不符合Kuberneres资源定义的的情况
	 * 否则抛出异常
	 * https://kubernetes.io/docs/concepts/overview/working-with-objects/
	 * 
	 * @param fullkind    名称
	 * @param raw     原始数据
	 * @return 得到KubeData的元数据meta
	 */
	public static Meta toKubeMeta(String fullkind, JsonNode raw) {
		Meta meta = new Meta();
		meta.setFullkind(fullkind);
		String apiVersion = raw.get(KubernetesConstants.KUBE_APIVERSION).asText();
		int idx = apiVersion.indexOf(KubernetesConstants.KUBE_APIVERSION_SPLIT);
		meta.setGroup(idx == -1 ? KubernetesConstants.KUBE_DEFAULT_GROUP : apiVersion.substring(0, idx));
		meta.setKind(raw.get(KubernetesConstants.KUBE_KIND).asText());
		meta.setPlural(raw.get(KubernetesConstants.KUBE_PLURAL).asText());
		// "verbs":["create","delete","deletecollection","get","list","patch","update","watch"]
		return meta;
	}
	
	/**
	 * @param key    关键字
	 * @param def    默认值
	 * @return       环境变量
	 */
	public static String getEnv(String key, String def) {
		String val = System.getenv(key);
		return val == null ? def : val;
	}
}
