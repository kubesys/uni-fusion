/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.api.models.openstack;

import io.github.kubestack.client.api.specs.openstack.OpenstackNetworkSpec;

/**
 * @Description Openstack Ojbect definition
 * @Date 2023/February/27 15:28
 * @Author guohao
 **/
public class OpenstackNetwork extends OpenstackModel {
    protected OpenstackNetworkSpec spec;

    public OpenstackNetwork() {}

    public OpenstackNetworkSpec getSpec() {
        return spec;
    }

    public void setSpec(OpenstackNetworkSpec spec) {
        this.spec = spec;
    }
}
