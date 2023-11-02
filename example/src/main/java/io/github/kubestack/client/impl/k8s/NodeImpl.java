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
public class NodeImpl extends AbstractImpl<io.github.kubestack.client.api.models.k8s.Node, io.fabric8.kubernetes.api.model.NodeSpec> {

	public NodeImpl(KubeStackClient client, String kind) {
		super(client, kind);
	}


	@Override
	protected io.github.kubestack.client.api.models.k8s.Node getModel() {
		return new io.github.kubestack.client.api.models.k8s.Node();
	}

	@Override
	protected io.fabric8.kubernetes.api.model.NodeSpec getSpec() {
		return new io.fabric8.kubernetes.api.model.NodeSpec();
	}

	@Override
	protected Object getLifecycle() {
		return null;
	}

	@Override
	protected Class<?> getModelClass() {
		return io.github.kubestack.client.api.models.k8s.Node.class;
	}

}