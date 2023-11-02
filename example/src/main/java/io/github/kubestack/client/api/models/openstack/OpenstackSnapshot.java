/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.api.models.openstack;

import io.github.kubestack.client.api.specs.openstack.OpenstackSnapshotSpec;

/**
 * @Description Openstack Ojbect definition
 * @Date 2023/February/27 15:28
 * @Author guohao
 **/
public class OpenstackSnapshot extends OpenstackModel {
    protected OpenstackSnapshotSpec spec;

    public OpenstackSnapshot() {}

    public OpenstackSnapshotSpec getSpec() {
        return spec;
    }

    public void setSpec(OpenstackSnapshotSpec spec) {
        this.spec = spec;
    }
}
