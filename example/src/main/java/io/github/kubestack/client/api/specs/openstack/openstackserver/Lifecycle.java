package io.github.kubestack.client.api.specs.openstack.openstackserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.kubestack.core.annotations.ClassDescriber;
import io.github.kubestack.core.annotations.FunctionDescriber;
import io.github.kubestack.core.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description openstack function support
 * @Date 2023/2/8 14:17
 * @Author guohao
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@ClassDescriber(value = "OpenstackServer", desc = "Openstack 虚拟机支持操作")
public class Lifecycle {
    @FunctionDescriber(shortName = "创建Server",
        description = "指定image和flavor，创建Server" + AnnotationUtils.DESC_FUNCTION_DESC, prerequisite = "",
        exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("CreateComputeV2Servers")
    protected CreateServer createServer;
    @FunctionDescriber(shortName = "更新Server", description = "更新Server中可修改的参数" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("UpdateComputeV2Servers")
    protected UpdateServer updateServer;
    @FunctionDescriber(shortName = "删除Server", description = "删除Server" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("DeleteComputeV2Servers")
    protected DeleteServer deleteServer;
    @FunctionDescriber(shortName = "强制删除Server", description = "强制删除Server" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("ForceDeleteComputeV2Servers")
    protected ForceDeleteServer forceDeleteServer;
    @FunctionDescriber(shortName = "重启Server",
        description = "包含HardReboot和SortReboot两种重启方式" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("RebootComputeV2Servers")
    protected RebootServer rebootServer;
    @FunctionDescriber(shortName = "重新创建虚拟机",
        description = "基于image, 网络等，重新创建Server" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("RebuildComputeV2Servers")
    protected RebuildServer rebuildServer;
    @FunctionDescriber(shortName = "创建虚拟机",
        description = "指定image和flavor，创建Server" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("ResizeComputeV2Servers")
    protected ResizeServer resizeServer;
    @FunctionDescriber(shortName = "修改虚拟机flavor",
        description = "修改Server使用flavor,过程中能够需要Rebuild" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("GetComputeV2Servers")
    protected GetServer getServer;
    @FunctionDescriber(shortName = "修改Server元数据", description = "修改虚拟机Metadata" + AnnotationUtils.DESC_FUNCTION_DESC,
        prerequisite = AnnotationUtils.DESC_FUNCTION_OPENSTACK_SERVER, exception = AnnotationUtils.DESC_FUNCTION_EXEC)
    @JsonProperty("UpdateMetadataComputeV2Servers")
    protected UpdateMetadataServer updateMetadataServer;

    public UpdateMetadataServer getUpdateMetadataServer() {
        return updateMetadataServer;
    }

    public void setUpdateMetadataServer(UpdateMetadataServer updateMetadataServer) {
        this.updateMetadataServer = updateMetadataServer;
    }

    public DeleteServer getDeleteServer() {
        return deleteServer;
    }

    public void setDeleteServer(DeleteServer deleteServer) {
        this.deleteServer = deleteServer;
    }

    public ForceDeleteServer getForceDeleteServer() {
        return forceDeleteServer;
    }

    public void setForceDeleteServer(ForceDeleteServer forceDeleteServer) {
        this.forceDeleteServer = forceDeleteServer;
    }

    public RebootServer getRebootServer() {
        return rebootServer;
    }

    public void setRebootServer(RebootServer rebootServer) {
        this.rebootServer = rebootServer;
    }

    public RebuildServer getRebuildServer() {
        return rebuildServer;
    }

    public void setRebuildServer(RebuildServer rebuildServer) {
        this.rebuildServer = rebuildServer;
    }

    public ResizeServer getResizeServer() {
        return resizeServer;
    }

    public void setResizeServer(ResizeServer resizeServer) {
        this.resizeServer = resizeServer;
    }

    public GetServer getGetServer() {
        return getServer;
    }

    public void setGetServer(GetServer getServer) {
        this.getServer = getServer;
    }

    public UpdateServer getUpdateServer() {
        return updateServer;
    }

    public void setUpdateServer(UpdateServer updateServer) {
        this.updateServer = updateServer;
    }

    public CreateServer getCreateServer() {
        return createServer;
    }

    public void setCreateServer(CreateServer createServer) {
        this.createServer = createServer;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("CreateComputeV2Servers")
    public static class CreateServer {
        @JsonProperty("opts")
        protected Opts opts;

        public CreateServer() {
            opts = new Opts();
        }

        public Opts getOpts() {
            return opts;
        }

        public void setOpts(Opts opts) {
            this.opts = opts;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        public static class Opts {
            @JsonProperty("name")
            protected String name;
            @JsonProperty("imageRef")
            protected String imageRef;
            @JsonProperty("flavorRef")
            protected String flavorRef;
            @JsonProperty("security_groups")
            protected List<String> securityGroups;
            @JsonProperty("user_data")
            protected String userData;
            @JsonProperty("availability_zone")
            protected String availabilityZone;
            @JsonProperty("networks")
            protected List<NovaNetworkCreate> networks;
            @JsonProperty("metadata")
            protected Map<String, String> metadata;
            protected List<Personality> personality;
            @JsonProperty("config_drive")
            protected Boolean configDrive;
            protected String adminPass;
            protected String accessIPv4;
            protected String accessIPv6;
            protected Integer min;
            protected Integer max;
            protected List<String> tags;

            public Opts() {}

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImageRef() {
                return imageRef;
            }

            public void setImageRef(String imageRef) {
                this.imageRef = imageRef;
            }

            public String getFlavorRef() {
                return flavorRef;
            }

            public void setFlavorRef(String flavorRef) {
                this.flavorRef = flavorRef;
            }

            public List<String> getSecurityGroups() {
                return securityGroups;
            }

            public void setSecurityGroups(List<String> securityGroups) {
                this.securityGroups = securityGroups;
            }

            public String getUserData() {
                return userData;
            }

            public void setUserData(String userData) {
                this.userData = userData;
            }

            public String getAvailabilityZone() {
                return availabilityZone;
            }

            public void setAvailabilityZone(String availabilityZone) {
                this.availabilityZone = availabilityZone;
            }

            public List<NovaNetworkCreate> getNetworks() {
                return networks;
            }

            public void setNetworks(List<NovaNetworkCreate> networks) {
                this.networks = networks;
            }

            public Map<String, String> getMetadata() {
                return metadata;
            }

            public void setMetadata(Map<String, String> metadata) {
                this.metadata = metadata;
            }

            public List<Personality> getPersonality() {
                return personality;
            }

            public void setPersonality(List<Personality> personality) {
                this.personality = personality;
            }

            public Boolean getConfigDrive() {
                return configDrive;
            }

            public void setConfigDrive(Boolean configDrive) {
                this.configDrive = configDrive;
            }

            public String getAdminPass() {
                return adminPass;
            }

            public void setAdminPass(String adminPass) {
                this.adminPass = adminPass;
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

            public Integer getMin() {
                return min;
            }

            public void setMin(Integer min) {
                this.min = min;
            }

            public Integer getMax() {
                return max;
            }

            public void setMax(Integer max) {
                this.max = max;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public NovaNetworkCreate NewNovaNetworkCreate() {
                return new NovaNetworkCreate();
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
            public static class NovaNetworkCreate {
                protected String UUID;
                protected String Port;
                protected String FixedIP;
                protected String Tag;

                public NovaNetworkCreate() {}

                public String getUUID() {
                    return UUID;
                }

                public void setUUID(String UUID) {
                    this.UUID = UUID;
                }

                public String getPort() {
                    return Port;
                }

                public void setPort(String port) {
                    Port = port;
                }

                public String getFixedIP() {
                    return FixedIP;
                }

                public void setFixedIP(String fixedIP) {
                    FixedIP = fixedIP;
                }

                public String getTag() {
                    return Tag;
                }

                public void setTag(String tag) {
                    Tag = tag;
                }
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
            public static class Personality {
                protected String path;
                protected String contents;

                public Personality(String path) {
                    this.path = path;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public String getContents() {
                    return contents;
                }

                public void setContents(String contents) {
                    this.contents = contents;
                }
            }
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("UpdateComputeV2Servers")
    public static class UpdateServer {
        @JsonProperty("id")
        protected String id;
        @JsonProperty("opts")
        protected Opts opts;

        public UpdateServer() {
            this.opts = new Opts();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Opts getOpts() {
            return opts;
        }

        public void setOpts(Opts opts) {
            this.opts = opts;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        public static class Opts {
            @JsonProperty("name")
            String name;
            @JsonProperty("accessIPv4")
            String accessIPv4;
            @JsonProperty("accessIPv6")
            String accessIPv6;

            public Opts() {}

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("DeleteComputeV2Servers")
    public static class DeleteServer {
        @JsonProperty("id")
        protected String id;

        public DeleteServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("ForceDeleteComputeV2Servers")
    public static class ForceDeleteServer {
        @JsonProperty("id")
        protected String id;

        public ForceDeleteServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("GetComputeV2Servers")
    public static class GetServer {
        @JsonProperty("id")
        protected String id;

        public GetServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("ChangeAdminPasswordComputeV2Servers")
    public static class ChangeAdminPassword {
        @JsonProperty("id")
        protected String id;

        @JsonProperty("newPassword")
        protected String newPassword;

        public ChangeAdminPassword() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("RebootComputeV2Servers")
    public static class RebootServer {
        @JsonProperty("id")
        protected String id;

        @JsonProperty("opts")
        protected Opts opts;

        public RebootServer() {}

        public Opts getOpts() {
            return opts;
        }

        public void setOpts(Opts opts) {
            this.opts = opts;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        public static class Opts {
            @JsonProperty("type")
            protected String type;// "SOFT" or "HARD"

            public Opts() {}

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
    @JsonRootName("RebuildComputeV2Servers")
    public static class RebuildServer {
        @JsonProperty("id")
        protected String id;

        protected Opts opts;

        public RebuildServer() {
            this.opts = new Opts();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Opts getOpts() {
            return opts;
        }

        public void setOpts(Opts opts) {
            this.opts = opts;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        public static class Opts {
            @JsonProperty("adminPass")
            protected String adminPass;
            @JsonProperty("imageRef")
            protected String imageRef;
            @JsonProperty("name")
            protected String name;
            @JsonProperty("accessIPv4")
            protected String accessIPv4;
            @JsonProperty("accessIPv6")
            protected String accessIPv6;
            @JsonProperty("metadata")
            protected HashMap<String, String> metadata;
            @JsonProperty("personality")
            protected CreateServer.Opts.Personality personality;

            public Opts() {}

            public String getAdminPass() {
                return adminPass;
            }

            public void setAdminPass(String adminPass) {
                this.adminPass = adminPass;
            }

            public String getImageRef() {
                return imageRef;
            }

            public void setImageRef(String imageRef) {
                this.imageRef = imageRef;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public HashMap<String, String> getMetadata() {
                return metadata;
            }

            public void setMetadata(HashMap<String, String> metadata) {
                this.metadata = metadata;
            }

            public CreateServer.Opts.Personality getPersonality() {
                return personality;
            }

            public void setPersonality(CreateServer.Opts.Personality personality) {
                this.personality = personality;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("ResizeComputeV2Servers")
    public static class ResizeServer {
        @JsonProperty("id")
        protected String id;
        @JsonProperty("opts")
        protected Opts opts;

        public ResizeServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Opts getOpts() {
            return opts;
        }

        public void setOpts(Opts opts) {
            this.opts = opts;
        }

        public static class Opts {
            @JsonProperty("flavorRef")
            protected String flavorRef;

            public Opts() {}

            public String getFlavorRef() {
                return flavorRef;
            }

            public void setFlavorRef(String flavorRef) {
                this.flavorRef = flavorRef;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("ConfirmResizeComputeV2Servers")
    public static class ConfirmResizeServer {
        @JsonProperty("id")
        protected String id;

        public ConfirmResizeServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("RevertResizeComputeV2Servers")
    public static class RevertResizeServer {
        @JsonProperty("id")
        protected String id;

        public RevertResizeServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("MetadataComputeV2Servers")
    public static class MetadataServer {
        @JsonProperty("id")
        protected String id;

        public MetadataServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("ResetMetadataComputeV2Servers")
    public static class ResetMetadataServer {
        @JsonProperty("id")
        protected String id;

        @JsonProperty("opts")
        protected HashMap<String, String> opts;

        public ResetMetadataServer() {}

        public HashMap<String, String> getOpts() {
            return opts;
        }

        public void setOpts(HashMap<String, String> opts) {
            this.opts = opts;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("UpdateMetadataComputeV2Servers")
    public static class UpdateMetadataServer {
        @JsonProperty("id")
        protected String id;

        @JsonProperty("opts")
        protected HashMap<String, String> opts;

        public UpdateMetadataServer() {}

        public HashMap<String, String> getOpts() {
            return opts;
        }

        public void setOpts(HashMap<String, String> opts) {
            this.opts = opts;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonRootName("CreateImageComputeV2Servers")
    public static class CreateImageFromServer {
        @JsonProperty("id")
        protected String id;

        @JsonProperty("opts")
        protected CreateImageFromServerOpts opts;

        public CreateImageFromServer() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public CreateImageFromServerOpts getOpts() {
            return opts;
        }

        public void setOpts(CreateImageFromServerOpts opts) {
            this.opts = opts;
        }

        public static class CreateImageFromServerOpts {
            @JsonProperty("name")
            protected String name;

            @JsonProperty("metadata")
            protected HashMap<String, String> metadata;

            public CreateImageFromServerOpts() {}

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public HashMap<String, String> getMetadata() {
                return metadata;
            }

            public void setMetadata(HashMap<String, String> metadata) {
                this.metadata = metadata;
            }
        }
    }
}
