/**
* Code Generator by multicloud_service
*/
package io.github.kubestack.client.impl.openstack;

import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.api.models.openstack.OpenstackRouter;
import io.github.kubestack.client.api.specs.openstack.OpenstackRouterSpec;
import io.github.kubestack.client.api.specs.openstack.SecretRef;
import io.github.kubestack.client.api.specs.openstack.openstackrouter.Lifecycle;
import io.github.kubestack.client.impl.AbstractImpl;
import io.github.kubestack.core.utils.RegExpUtils;

import java.util.regex.Pattern;

/**
 * @Description OpenstackRouter operation impl
 * @Date 2023/2/8 16:45
 * @Author guohao
 **/
@SuppressWarnings("deprecation")
public class OpenstackRouterImpl extends AbstractImpl<OpenstackRouter, OpenstackRouterSpec> {

    public OpenstackRouterImpl(KubeStackClient client, String kind) {
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

    public boolean listL3Agents(String name, Lifecycle.ListL3Agents listL3Agents, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), listL3Agents);

    }

    public boolean delete(String name, Lifecycle.Delete delete, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), delete);

    }

    public boolean removeInterface(String name, Lifecycle.RemoveInterface removeInterface, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), removeInterface);

    }

    public boolean addInterface(String name, Lifecycle.AddInterface addInterface, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), addInterface);

    }

    public boolean get(String name, Lifecycle.Get get, SecretRef secretRef) throws Exception{
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }

        return update(name, updateMetadata(name, null), get);

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
        return OpenstackRouter.class;
    }

    @Override
    protected OpenstackRouter getModel() {
        return new OpenstackRouter();
    }

    @Override
    protected OpenstackRouterSpec getSpec() {
        return new OpenstackRouterSpec();
    }

    @Override
    protected Object getLifecycle() {
        return new Lifecycle();
    }
}
