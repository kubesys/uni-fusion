/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.api.models.openstack;

import io.github.kubestack.client.api.specs.openstack.OpenstackImageSpec;

/**
 * @Description Openstack Ojbect definition
 * @Date 2023/February/27 15:28
 * @Author guohao
 **/
public class OpenstackImage extends OpenstackModel {
    protected OpenstackImageSpec spec;

    public OpenstackImage() {}

    public OpenstackImageSpec getSpec() {
        return spec;
    }

    public void setSpec(OpenstackImageSpec spec) {
        this.spec = spec;
    }
}
