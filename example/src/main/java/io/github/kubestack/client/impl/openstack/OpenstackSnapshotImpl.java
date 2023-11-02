/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.impl.openstack;

import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.api.models.openstack.OpenstackSnapshot;
import io.github.kubestack.client.api.specs.openstack.OpenstackSnapshotSpec;
import io.github.kubestack.client.api.specs.openstack.SecretRef;
import io.github.kubestack.client.api.specs.openstack.openstacksnapshot.Lifecycle;
import io.github.kubestack.client.impl.AbstractImpl;
import io.github.kubestack.core.utils.RegExpUtils;

import java.util.regex.Pattern;

/**
 * @Description OpenstackSnapshot operation impl
 * @Date 2023/2/8 16:45
 * @Author guohao
 **/
@SuppressWarnings("deprecation")
public class OpenstackSnapshotImpl extends AbstractImpl<OpenstackSnapshot, OpenstackSnapshotSpec> {

    public OpenstackSnapshotImpl(KubeStackClient client, String kind) {
        super(client, kind);
    }

    public boolean updateMetadata(String name, Lifecycle.UpdateMetadata updateMetadata, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), updateMetadata);

    }

    public boolean delete(String name, Lifecycle.Delete delete, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), delete);

    }

    public boolean get(String name, Lifecycle.Get get, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), get);

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

    public boolean update(String name, Lifecycle.Update update, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), update);

    }



    @Override
    protected Class<?> getModelClass() {
        return OpenstackSnapshot.class;
    }

    @Override
    protected OpenstackSnapshot getModel() {
        return new OpenstackSnapshot();
    }

    @Override
    protected OpenstackSnapshotSpec getSpec() {
        return new OpenstackSnapshotSpec();
    }

    @Override
    protected Object getLifecycle() {
        return new Lifecycle();
    }
}
