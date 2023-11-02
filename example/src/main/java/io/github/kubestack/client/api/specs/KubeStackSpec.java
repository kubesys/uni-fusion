/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.specs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Affinity;

import java.util.Map;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since 2022.9.28
 *
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
public abstract class KubeStackSpec {

	/**
	 * serialVersionUID
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1171174592223281364L;

	/**
	 * advanced scheduling policy based on node name
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected String nodeName;

	/**
	 * advanced scheduling policy based on node selector
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected Map<String, String> nodeSelector;

	/**
	 * affinity and anti-affinity
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	protected Affinity affinity;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Map<String, String> getNodeSelector() {
		return nodeSelector;
	}

	public void setNodeSelector(Map<String, String> nodeSelector) {
		this.nodeSelector = nodeSelector;
	}

	public Affinity getAffinity() {
		return affinity;
	}

	public void setAffinity(Affinity affinity) {
		this.affinity = affinity;
	}

}
