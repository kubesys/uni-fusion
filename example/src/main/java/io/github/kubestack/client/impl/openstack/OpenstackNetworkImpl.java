/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.impl.openstack;

import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.api.models.openstack.OpenstackNetwork;
import io.github.kubestack.client.api.specs.openstack.OpenstackNetworkSpec;
import io.github.kubestack.client.api.specs.openstack.SecretRef;
import io.github.kubestack.client.api.specs.openstack.openstacknetwork.Lifecycle;
import io.github.kubestack.client.impl.AbstractImpl;
import io.github.kubestack.core.utils.RegExpUtils;

import java.util.regex.Pattern;

/**
 * @Description OpenstackNetwork operation impl
 * @Date 2023/2/8 16:45
 * @Author guohao
 **/
@SuppressWarnings("deprecation")
public class OpenstackNetworkImpl extends AbstractImpl<OpenstackNetwork, OpenstackNetworkSpec> {

    public OpenstackNetworkImpl(KubeStackClient client, String kind) {
        super(client, kind);
    }

    public boolean create(String name, Lifecycle.Create create, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return create(getModel(), createMetadata(name, null, null),
                    createSpec(createLifecycle(create), secretRef));

    }

    public boolean delete(String name, Lifecycle.Delete delete, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), delete);

    }

    public boolean update(String name, Lifecycle.Update update, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), update);

    }

    public boolean get(String name, Lifecycle.Get get, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), get);

    }



    @Override
    protected Class<?> getModelClass() {
        return OpenstackNetwork.class;
    }

    @Override
    protected OpenstackNetwork getModel() {
        return new OpenstackNetwork();
    }

    @Override
    protected OpenstackNetworkSpec getSpec() {
        return new OpenstackNetworkSpec();
    }

    @Override
    protected Object getLifecycle() {
        return new Lifecycle();
    }
}
