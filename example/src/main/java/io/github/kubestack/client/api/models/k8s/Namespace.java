/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models.k8s;

import io.github.kubestack.client.api.models.KubeStackModel;

/**
 * @author wuheng@iscas.ac.cn
 * 
 * @version 2.0.0
 * @since   2022.10.12
 * 
 **/
public class Namespace extends KubeStackModel {

	protected io.fabric8.kubernetes.api.model.NamespaceSpec spec;

	public io.fabric8.kubernetes.api.model.NamespaceSpec getSpec() {
		return spec;
	}

	public void setSpec(io.fabric8.kubernetes.api.model.NamespaceSpec spec) {
		this.spec = spec;
	}
	
	protected io.fabric8.kubernetes.api.model.NamespaceStatus status;
	
	public io.fabric8.kubernetes.api.model.NamespaceStatus getStatus() {
		return status;
	}

	public void setStatus(io.fabric8.kubernetes.api.model.NamespaceStatus status) {
		this.status = status;
	}
	
}