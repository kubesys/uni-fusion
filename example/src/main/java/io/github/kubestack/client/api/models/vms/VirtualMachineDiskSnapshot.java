/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models.vms;

import io.github.kubestack.client.api.specs.vms.VirtualMachineDiskSnapshotSpec;

/**
 * @author wuheng@iscas.ac.cn
 * 
 * @version 2.0.0
 * @since   2022.10.12
 * 
 **/
public class VirtualMachineDiskSnapshot extends VirtualMachineModel {

	protected VirtualMachineDiskSnapshotSpec spec;

	public VirtualMachineDiskSnapshotSpec getSpec() {
		return spec;
	}

	public void setSpec(VirtualMachineDiskSnapshotSpec spec) {
		this.spec = spec;
	}
	
}
