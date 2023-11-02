/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.specs.vms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Status;
import io.github.kubestack.client.api.specs.KubeStackSpec;
import io.github.kubestack.client.api.specs.vms.virtualmachinedisksnapshot.Lifecycle;
import io.github.kubestack.client.api.specs.vms.virtualmachinedisksnapshot.Volume;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since   2022.9.28
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VirtualMachineDiskSnapshotSpec extends KubeStackSpec {


	protected Volume volume;

	protected Lifecycle lifecycle;

	protected Status status;

	public VirtualMachineDiskSnapshotSpec() {

	}

	public Volume getVolume() {
		return volume;
	}

	public void setVolume(Volume volume) {
		this.volume = volume;
	}

	public Lifecycle getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
