/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models.vms;

import io.github.kubestack.client.api.specs.vms.VirtualMachineSpec;

/**
 * @author wuheng@iscas.ac.cn
 * 
 * @version 2.0.0
 * @since   2022.10.12
 * 
 **/
public class VirtualMachine extends VirtualMachineModel {

	protected VirtualMachineSpec spec;

	public VirtualMachineSpec getSpec() {
		return spec;
	}

	public void setSpec(VirtualMachineSpec spec) {
		this.spec = spec;
	}
	
}
