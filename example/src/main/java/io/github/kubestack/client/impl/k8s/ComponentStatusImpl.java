/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.impl.k8s;


import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.impl.AbstractImpl;

/**
 * @author  wuheng@otcaix.iscas.ac.cn
 * 
 * @version 2.0.0
 * @since   2022/10/24
 **/
public class ComponentStatusImpl extends AbstractImpl<io.github.kubestack.client.api.models.k8s.ComponentStatus, io.fabric8.kubernetes.api.model.KubernetesResource> {

	public ComponentStatusImpl(KubeStackClient client, String kind) {
		super(client, kind);
	}


	@Override
	protected io.github.kubestack.client.api.models.k8s.ComponentStatus getModel() {
		return new io.github.kubestack.client.api.models.k8s.ComponentStatus();
	}

	@Override
	protected io.fabric8.kubernetes.api.model.KubernetesResource getSpec() {
		return null;
	}

	@Override
	protected Object getLifecycle() {
		return null;
	}

	@Override
	protected Class<?> getModelClass() {
		return io.github.kubestack.client.api.models.k8s.ComponentStatus.class;
	}

}