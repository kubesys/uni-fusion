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
public class PodTemplate extends KubeStackModel {

	protected io.fabric8.kubernetes.api.model.PodTemplateSpec spec;

	public io.fabric8.kubernetes.api.model.PodTemplateSpec getSpec() {
		return spec;
	}

	public void setSpec(io.fabric8.kubernetes.api.model.PodTemplateSpec spec) {
		this.spec = spec;
	}
	
	
	
}