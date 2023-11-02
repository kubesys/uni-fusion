package io.github.kubestack.client.api.specs.openstack.openstackserver;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description OpenstackServer Json
 * @Date 2023/2/7 14:47
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonRootName(value = "server")
public class Domain {
    protected String id;
    protected String name;
    protected NovaAddresses addresses;
    protected List<GenericLink> links;
    protected Object image;
    protected NovaFlavor flavor;
    protected String accessIPv4;
    protected String accessIPv6;

    @JsonProperty("config_drive")
    protected String configDrive;

    protected Status status;
    protected Integer progress;
    protected NovaFault fault;

    @JsonProperty("tenant_id")
    protected String tenantId;

    @JsonProperty("user_id")
    protected String userId;

    @JsonProperty("key_name")
    protected String keyName;

    protected String hostId;
    protected Date updated;
    protected Date created;
    protected Map<String, String> metadata;

    @JsonProperty("security_groups")
    protected List<NovaSecurityGroup> securityGroups;

    @JsonProperty("OS-EXT-STS:task_state")
    protected String taskState;

    @JsonProperty("OS-EXT-STS:power_state")
    protected String powerState;

    @JsonProperty("OS-EXT-STS:vm_state")
    protected String vmState;

    @JsonProperty("OS-EXT-SRV-ATTR:host")
    protected String host;

    @JsonProperty("OS-EXT-SRV-ATTR:instance_name")
    protected String instanceName;

    @JsonProperty("OS-EXT-SRV-ATTR:hypervisor_hostname")
    protected String hypervisorHostname;

    @JsonProperty("OS-DCF:diskConfig")
    protected DiskConfig diskConfig;

    @JsonProperty("OS-EXT-AZ:availability_zone")
    protected String availabilityZone;

    @JsonProperty("OS-SRV-USG:launched_at")
    protected Date launchedAt;

    @JsonProperty("OS-SRV-USG:terminated_at")
    protected Date terminatedAt;

    @JsonProperty("os-extended-volumes:volumes_attached")
    protected List<IdResourceEntity> osExtendedVolumesAttached;

    protected String uuid;
    protected String adminPass;

    public Domain() {}

    public static void main(String[] args) {
        // language=JSON
        String jsonString = "{\n" + "  \"OS-DCF:diskConfig\":\"MANUAL\",\n"
            + "  \"OS-EXT-AZ:availability_zone\":\"nova\",\n" + "  \"OS-EXT-SRV-ATTR:host\":\"openstack\",\n"
            + "  \"OS-EXT-SRV-ATTR:hypervisor_hostname\":\"openstack\",\n"
            + "  \"OS-EXT-SRV-ATTR:instance_name\":\"instance-0000001d\",\n" + "  \"OS-EXT-STS:power_state\":1,\n"
            + "  \"OS-EXT-STS:task_state\":null,\n" + "  \"OS-EXT-STS:vm_state\":\"active\",\n"
            + "  \"OS-SRV-USG:launched_at\":\"2023-02-07T07:07:53.000000\",\n"
            + "  \"OS-SRV-USG:terminated_at\":null,\n" + "  \"accessIPv4\":\"\",\n" + "  \"accessIPv6\":\"\",\n"
            + "  \"addresses\":{\n" + "    \"public\":[\n" + "      {\n"
            + "        \"OS-EXT-IPS-MAC:mac_addr\":\"fa:16:3e:13:bc:ba\",\n"
            + "        \"OS-EXT-IPS:type\":\"fixed\",\n" + "        \"addr\":\"172.24.4.226\",\n"
            + "        \"version\":4\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"config_drive\":\"\",\n"
            + "  \"created\":\"2023-02-07T07:07:16Z\",\n" + "  \"flavor\":{\n" + "    \"id\":\"3\",\n"
            + "    \"links\":[\n" + "      {\n"
            + "        \"href\":\"http://133.133.135.136:8774/aac94320146c464ab84146e35aa61c77/flavors/3\",\n"
            + "        \"rel\":\"bookmark\"\n" + "      }\n" + "    ]\n" + "  },\n"
            + "  \"hostId\":\"ab4a15f58126be6dfe9896dc9495ba8e4f9613d9395294f982f9c500\",\n"
            + "  \"id\":\"65992d97-a29c-4b6c-b2b4-c12e5bfd475f\",\n" + "  \"image\":{\n"
            + "    \"id\":\"952b386b-6f30-46f6-b019-f522b157aa3a\",\n" + "    \"links\":[\n" + "      {\n"
            + "        \"href\":\"http://133.133.135.136:8774/aac94320146c464ab84146e35aa61c77/images/952b386b-6f30-46f6-b019-f522b157aa3a\",\n"
            + "        \"rel\":\"bookmark\"\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"key_name\":null,\n"
            + "  \"links\":[\n" + "    {\n"
            + "      \"href\":\"http://133.133.135.136:8774/v2.1/aac94320146c464ab84146e35aa61c77/servers/65992d97-a29c-4b6c-b2b4-c12e5bfd475f\",\n"
            + "      \"rel\":\"self\"\n" + "    },\n" + "    {\n"
            + "      \"href\":\"http://133.133.135.136:8774/aac94320146c464ab84146e35aa61c77/servers/65992d97-a29c-4b6c-b2b4-c12e5bfd475f\",\n"
            + "      \"rel\":\"bookmark\"\n" + "    }\n" + "  ],\n" + "  \"metadata\":{\n" + "\n" + "  },\n"
            + "  \"name\":\"test-create\",\n" + "  \"os-extended-volumes:volumes_attached\":[\n" + "\n" + "  ],\n"
            + "  \"progress\":0,\n" + "  \"security_groups\":[\n" + "    {\n" + "      \"name\":\"default\"\n"
            + "    }\n" + "  ],\n" + "  \"status\":\"ACTIVE\",\n"
            + "  \"tenant_id\":\"aac94320146c464ab84146e35aa61c77\",\n" + "  \"updated\":\"2023-02-07T07:07:53Z\",\n"
            + "  \"user_id\":\"f8db2401acfb4c3b98400dac8fa22207\"\n" + "}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Domain openstackServerDomain1 = mapper.readValue(jsonString, Domain.class);
            String newJsonString = mapper.writeValueAsString(openstackServerDomain1);
            System.out.println(newJsonString);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NovaAddresses getAddresses() {
        return addresses;
    }

    public void setAddresses(NovaAddresses addresses) {
        this.addresses = addresses;
    }

    public List<GenericLink> getLinks() {
        return links;
    }

    public void setLinks(List<GenericLink> links) {
        this.links = links;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public NovaFlavor getFlavor() {
        return flavor;
    }

    public void setFlavor(NovaFlavor flavor) {
        this.flavor = flavor;
    }

    public String getAccessIPv4() {
        return accessIPv4;
    }

    public void setAccessIPv4(String accessIPv4) {
        this.accessIPv4 = accessIPv4;
    }

    public String getAccessIPv6() {
        return accessIPv6;
    }

    public void setAccessIPv6(String accessIPv6) {
        this.accessIPv6 = accessIPv6;
    }

    public String getConfigDrive() {
        return configDrive;
    }

    public void setConfigDrive(String configDrive) {
        this.configDrive = configDrive;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public NovaFault getFault() {
        return fault;
    }

    public void setFault(NovaFault fault) {
        this.fault = fault;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public List<NovaSecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<NovaSecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getVmState() {
        return vmState;
    }

    public void setVmState(String vmState) {
        this.vmState = vmState;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getHypervisorHostname() {
        return hypervisorHostname;
    }

    public void setHypervisorHostname(String hypervisorHostname) {
        this.hypervisorHostname = hypervisorHostname;
    }

    public DiskConfig getDiskConfig() {
        return diskConfig;
    }

    public void setDiskConfig(DiskConfig diskConfig) {
        this.diskConfig = diskConfig;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public Date getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Date launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Date getTerminatedAt() {
        return terminatedAt;
    }

    public void setTerminatedAt(Date terminatedAt) {
        this.terminatedAt = terminatedAt;
    }

    public List<IdResourceEntity> getOsExtendedVolumesAttached() {
        return osExtendedVolumesAttached;
    }

    public void setOsExtendedVolumesAttached(List<IdResourceEntity> osExtendedVolumesAttached) {
        this.osExtendedVolumesAttached = osExtendedVolumesAttached;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public enum Status {
        /** The server is active */
        ACTIVE,
        /** The server has not finished the original build process */
        BUILD,
        /** The server is currently being rebuilt */
        REBUILD,
        /**
         * The server is suspended, either by request or necessity. This status appears for only the following
         * hypervisors: XenServer/XCP, KVM, and ESXi. Administrative users may suspend an instance if it is infrequently
         * used or to perform system maintenance. When you suspend an instance, its VM state is stored on disk, all
         * memory is written to disk, and the virtual machine is stopped. Suspending an instance is similar to placing a
         * device in hibernation; memory and vCPUs become available to create other instances.
         */
        SUSPENDED,
        /**
         * In a paused state, the state of the server is stored in RAM. A paused server continues to run in frozen
         * state.
         */
        PAUSED,
        /**
         * Server is performing the differential copy of data that changed during its initial copy. Server is down for
         * this stage.
         */
        RESIZE,
        /**
         * System is awaiting confirmation that the server is operational after a move or resize.
         */
        VERIFY_RESIZE,
        /**
         * The resize or migration of a server failed for some reason. The destination server is being cleaned up and
         * the original source server is restarting.
         */
        REVERT_RESIZE,
        /** The password is being reset on the server. */
        PASSWORD,
        /**
         * The server is in a soft reboot state. A reboot command was passed to the operating system.
         */
        REBOOT,
        /**
         * The server is hard rebooting. This is equivalent to pulling the power plug on a physical server, plugging it
         * back in, and rebooting it.
         */
        HARD_REBOOT,
        /** The server is permanently deleted. */
        DELETED,
        /** The state of the server is unknown. Contact your cloud provider. */
        UNKNOWN,
        /** The server is in error. */
        ERROR,
        /** The server is powered off and the disk image still persists. */
        STOPPED,
        /**
         * The virtual machine (VM) was powered down by the user, but not through the OpenStack Compute API.
         */
        SHUTOFF,
        /** The server is currently being migrated */
        MIGRATING,
        /** The server is shelved */
        SHELVED,
        /**
         * The server is shelved_offloaded, server removed from the hypervisor to minimize resource usage.
         */
        SHELVED_OFFLOADED,
        /** The server is in rescue mode */
        RESCUE,
        /**
         * OpenStack4j could not find a Status mapping for the current reported Status. File an issue indicating the
         * missing state
         */
        UNRECOGNIZED;

        @JsonCreator
        public static Status forValue(String value) {
            if (value != null) {
                for (Status s : Status.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return Status.UNRECOGNIZED;
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    enum DiskConfig {
        MANUAL, AUTO;

        @JsonCreator
        public static DiskConfig forValue(String value) {
            if (value != null && value.equalsIgnoreCase("auto"))
                return DiskConfig.AUTO;
            return DiskConfig.MANUAL;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NovaAddresses {
        @JsonProperty("addresses")
        protected Map<String, List<NovaAddress>> addresses = new HashMap<>();

        public NovaAddresses() {}

        public Map<String, List<NovaAddress>> getAddresses() {
            return addresses;
        }

        public void setAddresses(Map<String, List<NovaAddress>> addresses) {
            this.addresses = addresses;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NovaAddress {

            @JsonProperty("OS-EXT-IPS-MAC:mac_addr")
            protected String macAddr;

            protected int version;
            protected String addr;

            @JsonProperty("OS-EXT-IPS:type")
            protected String type;

            public NovaAddress() {}

            public String getMacAddr() {
                return macAddr;
            }

            public void setMacAddr(String macAddr) {
                this.macAddr = macAddr;
            }

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GenericLink {

        protected String rel;
        protected String href;
        protected String type;

        public GenericLink() {}

        public String getRel() {
            return rel;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @JsonRootName("flavor")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    public static class NovaFlavor {

        protected String id;
        protected String name;
        protected Integer ram;
        protected Integer vcpus;
        protected Integer disk;

        @JsonProperty("OS-FLV-EXT-DATA:ephemeral")
        protected int ephemeral;

        protected int swap;

        @JsonProperty("rxtx_factor")
        protected float rxtxFactor = 1.0f;

        @JsonProperty("OS-FLV-DISABLED:disabled")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        protected Boolean disabled;

        @JsonProperty("rxtx_quota")
        protected Integer rxtxQuota;

        @JsonProperty("rxtx_cap")
        protected Integer rxtxCap;

        @JsonProperty("os-flavor-access:is_public")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        protected Boolean isPublic;

        protected List<GenericLink> links;

        public NovaFlavor() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getRam() {
            return ram;
        }

        public void setRam(Integer ram) {
            this.ram = ram;
        }

        public Integer getVcpus() {
            return vcpus;
        }

        public void setVcpus(Integer vcpus) {
            this.vcpus = vcpus;
        }

        public Integer getDisk() {
            return disk;
        }

        public void setDisk(Integer disk) {
            this.disk = disk;
        }

        public int getEphemeral() {
            return ephemeral;
        }

        public void setEphemeral(int ephemeral) {
            this.ephemeral = ephemeral;
        }

        public int getSwap() {
            return swap;
        }

        public void setSwap(int swap) {
            this.swap = swap;
        }

        public float getRxtxFactor() {
            return rxtxFactor;
        }

        public void setRxtxFactor(float rxtxFactor) {
            this.rxtxFactor = rxtxFactor;
        }

        public Boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
        }

        public Integer getRxtxQuota() {
            return rxtxQuota;
        }

        public void setRxtxQuota(Integer rxtxQuota) {
            this.rxtxQuota = rxtxQuota;
        }

        public Integer getRxtxCap() {
            return rxtxCap;
        }

        public void setRxtxCap(Integer rxtxCap) {
            this.rxtxCap = rxtxCap;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }

        public List<GenericLink> getLinks() {
            return links;
        }

        public void setLinks(List<GenericLink> links) {
            this.links = links;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NovaFault {

        protected int code;
        protected String message;
        protected String details;
        protected Date created;

        public NovaFault() {}

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }
    }

    @JsonRootName("security_groups")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NovaSecurityGroup {

        protected String name;

        public NovaSecurityGroup() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IdResourceEntity {

        @JsonProperty
        protected String id;

        public IdResourceEntity() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
