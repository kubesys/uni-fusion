/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.specs.vms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Status;
import io.github.kubestack.client.api.specs.KubeStackSpec;
import io.github.kubestack.client.api.specs.vms.virtualmachine.Domain;
import io.github.kubestack.client.api.specs.vms.virtualmachine.Lifecycle;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 *
 * @version 1.2.0
 * @since   2019/9/4
 *
 * <p>
 * <code>ExtendedKubernetesClient<code> extends <code>DefaultKubernetesClient<code>
 * to provide the lifecycle of VirtualMachine, VirtualMachinePool, VirtualMachineDisk,
 * VirtualMachineImage, VirtualMachineSnapshot, VirtualMachineNetwork
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VirtualMachineSpec extends KubeStackSpec {


	protected Domain domain;

	protected Lifecycle lifecycle;

	protected String powerstate;

	protected Status status;

	public VirtualMachineSpec() {

	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Lifecycle getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getPowerstate() {
		return powerstate;
	}

	public void setPowerstate(String powerstate) {
		this.powerstate = powerstate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
