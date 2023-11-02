/*
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client;

import io.github.kubestack.client.api.models.k8s.*;
import io.github.kubestack.client.api.models.openstack.OpenstackServer;
import io.github.kubestack.client.api.models.vms.*;
import io.github.kubestack.client.impl.k8s.*;
import io.github.kubestack.client.impl.openstack.OpenstackServerImpl;
import io.github.kubestack.client.impl.vms.*;
import io.github.kubesys.client.KubernetesAnalyzer;
import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesWatcher;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since 2022/9/28
 *
 *        <code>ExtendedKubernetesClient<code> extends <code>DefaultKubernetesClient<code> to provide the lifecycle of
 *        VirtualMachine VirtualMachinePool, VirtualMachineDisk, VirtualMachineImage, VirtualMachineSnapshot,
 *        VirtualMachineNetwork
 *
 */
public class KubeStackClient extends KubernetesClient {

    /**
     * m_logger
     */
    public final static Logger m_logger = Logger.getLogger(KubeStackClient.class.getName());
    public static String GROUP = "doslab.io";
    public static String KIND_VIRTUALMACHINE = GROUP + ".VirtualMachine";
    public static String KIND_VIRTUALMACHINE_DISK = GROUP + ".VirtualMachineDisk";
    public static String KIND_VIRTUALMACHINE_DISKIMAGE = GROUP + ".VirtualMachineDiskImage";
    public static String KIND_VIRTUALMACHINE_DISKSNAPSHOT = GROUP + ".VirtualMachineDiskSnapshot";
    public static String KIND_VIRTUALMACHINE_IMAGE = GROUP + ".VirtualMachineImage";
    public static String KIND_VIRTUALMACHINE_NETWORK = GROUP + ".VirtualMachineNetwork";
    public static String KIND_VIRTUALMACHINE_POOL = GROUP + ".VirtualMachinePool";
    public static String KIND_VIRTUALMACHINE_SNAPSHOT = GROUP + ".VirtualMachineSnapshot";
    public static String KIND_OPENSTACK_SERVER = GROUP + ".OpenstackServer";

    public KubeStackClient() throws Exception {
        super();
    }

    public KubeStackClient(File file, KubernetesAnalyzer analyzer) throws Exception {
        super(file, analyzer);
    }

    public KubeStackClient(File file) throws Exception {
        super(file);
    }

    public KubeStackClient(String config) throws Exception {
        super(config);
    }

    public KubeStackClient(String url, String token, KubernetesAnalyzer analyzer) throws Exception {
        super(url, token, analyzer);
    }

    public KubeStackClient(String url, String token) throws Exception {
        super(url, token);
    }

    /**
     * @return VirtualMachines
     */
    public VirtualMachineImpl virtualMachines() {
        return new VirtualMachineImpl(this, VirtualMachine.class.getSimpleName());
    }

    /**
     * @return VirtualMachines
     * @throws Exception
     */
    public void watchVirtualMachines(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE, watcher);
    }

    /***
     * @Description OpenstackServers
     *
     * @return io.github.kubestack.client.impl.openstack.OpenstackServerImpl
     * @Date 2023/2/8 17:06
     * @Author guohao
     **/
    public OpenstackServerImpl openstackServers() {
        return new OpenstackServerImpl(this, OpenstackServer.class.getSimpleName());
    }

    /***
     * @Description OpenstackServersc
     *
     * @Param
     * @param watcher
     * @return void
     * @Date 2023/2/8 17:06
     * @Author guohao
     **/
    public void watchOpenstackServer(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_OPENSTACK_SERVER, watcher);
    }

    /**
     * @return VirtualMachineDisks
     */
    public VirtualMachineDiskImpl virtualMachineDisks() {
        return new VirtualMachineDiskImpl(this, VirtualMachineDisk.class.getSimpleName());
    }

    /**
     * @return VirtualMachineDisks
     * @throws Exception
     */
    public void watchVirtualMachineDisks(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_DISK, watcher);
    }

    /**
     * @return VirtualMachineDiskImages
     */
    public VirtualMachineDiskImageImpl virtualMachineDiskImages() {
        return new VirtualMachineDiskImageImpl(this, VirtualMachineDiskImage.class.getSimpleName());
    }

    /**
     * @return VirtualMachineDiskImages
     * @throws Exception
     */
    public void watchVirtualMachineDiskImages(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_DISKIMAGE, watcher);
    }

    /**
     * @return VirtualMachineDiskSnapshots
     */
    public VirtualMachineDiskSnapshotImpl virtualMachineDiskSnapshots() {
        return new VirtualMachineDiskSnapshotImpl(this, VirtualMachineDiskSnapshot.class.getSimpleName());
    }

    /**
     * @return VirtualMachineDiskSnapshots
     * @throws Exception
     */
    public void watchVirtualMachineDiskSnapshots(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_DISKSNAPSHOT, watcher);
    }

    /**
     * @return VirtualMachineImages
     */
    public VirtualMachineImageImpl virtualMachineImages() {
        return new VirtualMachineImageImpl(this, VirtualMachineImage.class.getSimpleName());
    }

    /**
     * @return VirtualMachineImages
     * @throws Exception
     */
    public void watchVirtualMachineImages(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_IMAGE, watcher);
    }

    /**
     * @return VirtualMachineNetworks
     */
    public VirtualMachineNetworkImpl virtualMachineNetworks() {
        return new VirtualMachineNetworkImpl(this, VirtualMachineNetwork.class.getSimpleName());
    }

    /**
     * @return VirtualMachineNetworks
     * @throws Exception
     */
    public void watchVirtualMachineNetworks(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_NETWORK, watcher);
    }

    /**
     * @return VirtualMachinePools
     */
    public VirtualMachinePoolImpl virtualMachinePools() {
        return new VirtualMachinePoolImpl(this, VirtualMachinePool.class.getSimpleName());
    }

    /**
     * @return VirtualMachinePools
     * @throws Exception
     */
    public void watchVirtualMachinePools(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_POOL, watcher);
    }

    /**
     * @return VirtualMachineSnapshots
     */
    public VirtualMachineSnapshotImpl virtualMachineSnapshots() {
        return new VirtualMachineSnapshotImpl(this, VirtualMachineSnapshot.class.getSimpleName());
    }

    /**
     * @return VirtualMachineSnapshots
     * @throws Exception
     */
    public void watchVirtualMachineSnapshots(KubernetesWatcher watcher) throws Exception {
        this.watchResources(KIND_VIRTUALMACHINE_SNAPSHOT, watcher);
    }

    /**
     * the same as 'virtualMachines'
     *
     * @return virtualMachines
     */
    public VirtualMachineImpl getVirtualMachineImpl() {
        return virtualMachines();
    }

    /***
     * @Description //the same as "openstackServers"
     *
     * @return io.github.kubestack.client.impl.openstack.OpenstackServerImpl
     * @Date 2023/2/8 17:05
     * @Author guohao
     **/
    public OpenstackServerImpl getOpenstackServerImpl() {
        return openstackServers();
    }

    /**
     * the same as 'virtualMachineDisks'
     *
     * @return virtualMachineDisks
     */
    public VirtualMachineDiskImpl getVirtualMachineDiskImpl() {
        return virtualMachineDisks();
    }

    /**
     * the same as 'virtualMachineDiskSnapshots'
     *
     * @return virtualMachineDiskSnapshots
     */
    public VirtualMachineDiskSnapshotImpl getVirtualMachineDiskSnapshotImpl() {
        return virtualMachineDiskSnapshots();
    }

    /**
     * the same as 'virtualMachineImages'
     *
     * @return virtualMachineImages
     */
    public VirtualMachineImageImpl getVirtualMachineImageImpl() {
        return virtualMachineImages();
    }

    /**
     * the same as 'virtualMachineSnapshots'
     *
     * @return virtualMachineSnapshots
     */
    public VirtualMachineSnapshotImpl getVirtualMachineSnapshotImpl() {
        return virtualMachineSnapshots();
    }

    /**
     * the same as 'virtualMachinePools'
     *
     * @return virtualMachinePools
     */
    public VirtualMachinePoolImpl getVirtualMachinePoolImpl() {
        return virtualMachinePools();
    }

    /**
     * the same as 'virtualMachineDiskImages'
     *
     * @return virtualMachineDiskImages
     */
    public VirtualMachineDiskImageImpl getVirtualMachineDiskImageImpl() {
        return virtualMachineDiskImages();
    }

    /**
     * the same as 'virtualMachineNetworks'
     *
     * @return virtualMachineNetworks
     */
    public VirtualMachineNetworkImpl getVirtualMachineNetworkImpl() {
        return virtualMachineNetworks();
    }

    // --------------------------------------------------------

    /**
     * @return APIService
     */
    public APIServiceImpl apiservices() {
        return new APIServiceImpl(this, APIService.class.getSimpleName());
    }

    /**
     * @return watch apiservices
     * @throws Exception
     */
    public void watchAPIServices(KubernetesWatcher watcher) throws Exception {
        this.watchResources(APIService.class.getSimpleName(), watcher);
    }

    /**
     * @return Binding
     */
    public BindingImpl bindings() {
        return new BindingImpl(this, Binding.class.getSimpleName());
    }

    /**
     * @return watch bindings
     * @throws Exception
     */
    public void watchBindings(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Binding.class.getSimpleName(), watcher);
    }

    /**
     * @return ComponentStatus
     */
    public ComponentStatusImpl componentstatuss() {
        return new ComponentStatusImpl(this, ComponentStatus.class.getSimpleName());
    }

    /**
     * @return watch componentstatuss
     * @throws Exception
     */
    public void watchComponentStatuss(KubernetesWatcher watcher) throws Exception {
        this.watchResources(ComponentStatus.class.getSimpleName(), watcher);
    }

    /**
     * @return ConfigMap
     */
    public ConfigMapImpl configmaps() {
        return new ConfigMapImpl(this, ConfigMap.class.getSimpleName());
    }

    /**
     * @return watch configmaps
     * @throws Exception
     */
    public void watchConfigMaps(KubernetesWatcher watcher) throws Exception {
        this.watchResources(ConfigMap.class.getSimpleName(), watcher);
    }

    /**
     * @return Endpoints
     */
    public EndpointsImpl endpointss() {
        return new EndpointsImpl(this, Endpoints.class.getSimpleName());
    }

    /**
     * @return watch endpointss
     * @throws Exception
     */
    public void watchEndpointss(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Endpoints.class.getSimpleName(), watcher);
    }

    /**
     * @return Event
     */
    public EventImpl events() {
        return new EventImpl(this, Event.class.getSimpleName());
    }

    /**
     * @return watch events
     * @throws Exception
     */
    public void watchEvents(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Event.class.getSimpleName(), watcher);
    }

    /**
     * @return GenericKubernetesResource
     */
    public GenericKubernetesResourceImpl generickubernetesresources() {
        return new GenericKubernetesResourceImpl(this, GenericKubernetesResource.class.getSimpleName());
    }

    /**
     * @return watch generickubernetesresources
     * @throws Exception
     */
    public void watchGenericKubernetesResources(KubernetesWatcher watcher) throws Exception {
        this.watchResources(GenericKubernetesResource.class.getSimpleName(), watcher);
    }

    /**
     * @return LimitRange
     */
    public LimitRangeImpl limitranges() {
        return new LimitRangeImpl(this, LimitRange.class.getSimpleName());
    }

    /**
     * @return watch limitranges
     * @throws Exception
     */
    public void watchLimitRanges(KubernetesWatcher watcher) throws Exception {
        this.watchResources(LimitRange.class.getSimpleName(), watcher);
    }

    /**
     * @return Namespace
     */
    public NamespaceImpl namespaces() {
        return new NamespaceImpl(this, Namespace.class.getSimpleName());
    }

    /**
     * @return watch namespaces
     * @throws Exception
     */
    public void watchNamespaces(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Namespace.class.getSimpleName(), watcher);
    }

    /**
     * @return Node
     */
    public NodeImpl nodes() {
        return new NodeImpl(this, Node.class.getSimpleName());
    }

    /**
     * @return watch nodes
     * @throws Exception
     */
    public void watchNodes(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Node.class.getSimpleName(), watcher);
    }

    /**
     * @return PersistentVolume
     */
    public PersistentVolumeImpl persistentvolumes() {
        return new PersistentVolumeImpl(this, PersistentVolume.class.getSimpleName());
    }

    /**
     * @return watch persistentvolumes
     * @throws Exception
     */
    public void watchPersistentVolumes(KubernetesWatcher watcher) throws Exception {
        this.watchResources(PersistentVolume.class.getSimpleName(), watcher);
    }

    /**
     * @return PersistentVolumeClaim
     */
    public PersistentVolumeClaimImpl persistentvolumeclaims() {
        return new PersistentVolumeClaimImpl(this, PersistentVolumeClaim.class.getSimpleName());
    }

    /**
     * @return watch persistentvolumeclaims
     * @throws Exception
     */
    public void watchPersistentVolumeClaims(KubernetesWatcher watcher) throws Exception {
        this.watchResources(PersistentVolumeClaim.class.getSimpleName(), watcher);
    }

    /**
     * @return Pod
     */
    public PodImpl pods() {
        return new PodImpl(this, Pod.class.getSimpleName());
    }

    /**
     * @return watch pods
     * @throws Exception
     */
    public void watchPods(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Pod.class.getSimpleName(), watcher);
    }

    /**
     * @return PodTemplate
     */
    public PodTemplateImpl podtemplates() {
        return new PodTemplateImpl(this, PodTemplate.class.getSimpleName());
    }

    /**
     * @return watch podtemplates
     * @throws Exception
     */
    public void watchPodTemplates(KubernetesWatcher watcher) throws Exception {
        this.watchResources(PodTemplate.class.getSimpleName(), watcher);
    }

    /**
     * @return ReplicationController
     */
    public ReplicationControllerImpl replicationcontrollers() {
        return new ReplicationControllerImpl(this, ReplicationController.class.getSimpleName());
    }

    /**
     * @return watch replicationcontrollers
     * @throws Exception
     */
    public void watchReplicationControllers(KubernetesWatcher watcher) throws Exception {
        this.watchResources(ReplicationController.class.getSimpleName(), watcher);
    }

    /**
     * @return ResourceQuota
     */
    public ResourceQuotaImpl resourcequotas() {
        return new ResourceQuotaImpl(this, ResourceQuota.class.getSimpleName());
    }

    /**
     * @return watch resourcequotas
     * @throws Exception
     */
    public void watchResourceQuotas(KubernetesWatcher watcher) throws Exception {
        this.watchResources(ResourceQuota.class.getSimpleName(), watcher);
    }

    /**
     * @return Secret
     */
    public SecretImpl secrets() {
        return new SecretImpl(this, Secret.class.getSimpleName());
    }

    /**
     * @return watch secrets
     * @throws Exception
     */
    public void watchSecrets(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Secret.class.getSimpleName(), watcher);
    }

    /**
     * @return Service
     */
    public ServiceImpl services() {
        return new ServiceImpl(this, Service.class.getSimpleName());
    }

    /**
     * @return watch services
     * @throws Exception
     */
    public void watchServices(KubernetesWatcher watcher) throws Exception {
        this.watchResources(Service.class.getSimpleName(), watcher);
    }

    /**
     * @return ServiceAccount
     */
    public ServiceAccountImpl serviceaccounts() {
        return new ServiceAccountImpl(this, ServiceAccount.class.getSimpleName());
    }

    /**
     * @return watch serviceaccounts
     * @throws Exception
     */
    public void watchServiceAccounts(KubernetesWatcher watcher) throws Exception {
        this.watchResources(ServiceAccount.class.getSimpleName(), watcher);
    }

}
