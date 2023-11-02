package io.github.kubestack.client.impl.openstack;

import io.github.kubestack.client.KubeStackClient;
import io.github.kubestack.client.api.models.openstack.OpenstackServer;
import io.github.kubestack.client.api.specs.openstack.OpenstackServerSpec;
import io.github.kubestack.client.api.specs.openstack.SecretRef;
import io.github.kubestack.client.api.specs.openstack.openstackserver.Lifecycle;
import io.github.kubestack.client.impl.AbstractImpl;
import io.github.kubestack.core.utils.RegExpUtils;

import java.util.regex.Pattern;

/**
 * @Description // openstack server operation impl
 * @Date 2023/2/8 16:45
 * @Author guohao
 **/
@SuppressWarnings("deprecation")
public class OpenstackServerImpl extends AbstractImpl<OpenstackServer, OpenstackServerSpec> {

    public OpenstackServerImpl(KubeStackClient client, String kind) {
        super(client, kind);
    }

    public boolean createServer(String name, Lifecycle.CreateServer createServer, SecretRef secretRef)
        throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        // todo test createLifecycle
        return create(getModel(), createMetadata(name, null, null),
            createSpec(createLifecycle(createServer), secretRef));
    }

    public boolean getServer(String name, Lifecycle.GetServer getServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), getServer);
    }

    public boolean deleteServer(String name, Lifecycle.DeleteServer deleteServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), deleteServer);
    }

    public boolean forceDeleteServer(String name, Lifecycle.ForceDeleteServer forceDeleteServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), forceDeleteServer);
    }

    public boolean rebootServer(String name, Lifecycle.RebootServer rebootServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), rebootServer);
    }

    public boolean rebuildServer(String name, Lifecycle.RebuildServer rebuildServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), rebuildServer);
    }

    public boolean resizeServer(String name, Lifecycle.ResizeServer resizeServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), resizeServer);
    }

    public boolean updateMetadataServer(String name, Lifecycle.UpdateMetadataServer updateMetadataServer)
        throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), updateMetadataServer);

    }

    public boolean updateServer(String name, Lifecycle.UpdateServer updateServer) throws Exception {
        Pattern pattern = Pattern.compile(RegExpUtils.NAME_PATTERN);
        if (!pattern.matcher(name).matches()) {
            throw new IllegalArgumentException(
                "the length must be between 4 and 100, and it can only includes a-z, 0-9 and -.");
        }
        return update(name, updateMetadata(name, null), updateServer);
    }

    @Override
    protected Class<?> getModelClass() {
        return OpenstackServer.class;
    }

    @Override
    protected OpenstackServer getModel() {
        return new OpenstackServer();
    }

    @Override
    protected OpenstackServerSpec getSpec() {
        return new OpenstackServerSpec();
    }

    @Override
    protected Object getLifecycle() {
        return new Lifecycle();
    }
}
