/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models.vms;

import io.github.kubestack.client.api.specs.vms.VirtualMachineDiskImageSpec;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since   2022.10.12
 *
 **/
public class VirtualMachineDiskImage extends VirtualMachineModel {

	protected VirtualMachineDiskImageSpec spec;


	public VirtualMachineDiskImageSpec getSpec() {
		return spec;
	}

	public void setSpec(VirtualMachineDiskImageSpec spec) {
		this.spec = spec;
	}


}
