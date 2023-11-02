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
public class ResourceQuota extends KubeStackModel {

	protected io.fabric8.kubernetes.api.model.ResourceQuotaSpec spec;

	public io.fabric8.kubernetes.api.model.ResourceQuotaSpec getSpec() {
		return spec;
	}

	public void setSpec(io.fabric8.kubernetes.api.model.ResourceQuotaSpec spec) {
		this.spec = spec;
	}
	
	protected io.fabric8.kubernetes.api.model.ResourceQuotaStatus status;
	
	public io.fabric8.kubernetes.api.model.ResourceQuotaStatus getStatus() {
		return status;
	}

	public void setStatus(io.fabric8.kubernetes.api.model.ResourceQuotaStatus status) {
		this.status = status;
	}
	
}