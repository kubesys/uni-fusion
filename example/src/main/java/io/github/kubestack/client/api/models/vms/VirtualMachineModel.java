/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models.vms;

import io.fabric8.kubernetes.api.model.Status;
import io.github.kubestack.client.api.models.KubeStackModel;

/**
 * @author wuheng@iscas.ac.cn
 * 
 * @version 2.0.0
 * @since   2022.10.12
 * 
 **/
public class VirtualMachineModel extends KubeStackModel {

	protected Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
