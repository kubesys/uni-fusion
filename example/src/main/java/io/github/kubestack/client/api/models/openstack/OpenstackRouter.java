/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.api.models.openstack;

import io.github.kubestack.client.api.specs.openstack.OpenstackRouterSpec;

/**
 * @Description Openstack Ojbect definition
 * @Date 2023/February/27 15:28
 * @Author guohao
 **/
public class OpenstackRouter extends OpenstackModel {
    protected OpenstackRouterSpec spec;

    public OpenstackRouter() {}

    public OpenstackRouterSpec getSpec() {
        return spec;
    }

    public void setSpec(OpenstackRouterSpec spec) {
        this.spec = spec;
    }
}
