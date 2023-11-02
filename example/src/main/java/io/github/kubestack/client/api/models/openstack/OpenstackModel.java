package io.github.kubestack.client.api.models.openstack;

import io.fabric8.kubernetes.api.model.Status;
import io.github.kubestack.client.api.models.KubeStackModel;

/**
 * @Description openstack function support
 * @Date 2023/2/8 14:17
 * @Author guohao
 **/
public class OpenstackModel extends KubeStackModel {
    protected Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
