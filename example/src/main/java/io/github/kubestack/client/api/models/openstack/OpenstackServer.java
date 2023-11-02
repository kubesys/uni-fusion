package io.github.kubestack.client.api.models.openstack;

import io.github.kubestack.client.api.specs.openstack.OpenstackServerSpec;

/**
 * @Description openstack function support
 * @Date 2023/2/8 14:17
 * @Author guohao
 **/
public class OpenstackServer extends OpenstackModel {
    protected OpenstackServerSpec spec;

    public OpenstackServer() {}

    public OpenstackServerSpec getSpec() {
        return spec;
    }

    public void setSpec(OpenstackServerSpec spec) {
        this.spec = spec;
    }
}
