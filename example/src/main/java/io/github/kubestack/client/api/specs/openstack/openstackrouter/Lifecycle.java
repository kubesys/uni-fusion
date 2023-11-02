/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstackrouter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * @Description OpenstackRouter Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("Lifecycle")
public class Lifecycle {

    @JsonProperty("CreateNetworkingV2ExtensionsLayer3Routers")
    protected Create create_;

    @JsonProperty("ListL3AgentsNetworkingV2ExtensionsLayer3Routers")
    protected ListL3Agents listL3Agents_;

    @JsonProperty("DeleteNetworkingV2ExtensionsLayer3Routers")
    protected Delete delete_;

    @JsonProperty("RemoveInterfaceNetworkingV2ExtensionsLayer3Routers")
    protected RemoveInterface removeInterface_;

    @JsonProperty("AddInterfaceNetworkingV2ExtensionsLayer3Routers")
    protected AddInterface addInterface_;

    @JsonProperty("GetNetworkingV2ExtensionsLayer3Routers")
    protected Get get_;

    @JsonProperty("UpdateNetworkingV2ExtensionsLayer3Routers")
    protected Update update_;


    public Lifecycle(){

    }
    public Create getCreate(){
        return create_;
    }

    public void setCreate_(Create create_){
        this.create_ = create_;
    }

    public ListL3Agents getListL3Agents(){
        return listL3Agents_;
    }

    public void setListL3Agents_(ListL3Agents listL3Agents_){
        this.listL3Agents_ = listL3Agents_;
    }

    public Delete getDelete(){
        return delete_;
    }

    public void setDelete_(Delete delete_){
        this.delete_ = delete_;
    }

    public RemoveInterface getRemoveInterface(){
        return removeInterface_;
    }

    public void setRemoveInterface_(RemoveInterface removeInterface_){
        this.removeInterface_ = removeInterface_;
    }

    public AddInterface getAddInterface(){
        return addInterface_;
    }

    public void setAddInterface_(AddInterface addInterface_){
        this.addInterface_ = addInterface_;
    }

    public Get getGet(){
        return get_;
    }

    public void setGet_(Get get_){
        this.get_ = get_;
    }

    public Update getUpdate(){
        return update_;
    }

    public void setUpdate_(Update update_){
        this.update_ = update_;
    }




    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("CreateNetworkingV2ExtensionsLayer3Routers")
    public class Create {

        @JsonProperty("Opts")
        protected CreateOpts opts_;


        public Create(){

        }
        public CreateOpts getOpts(){
            return opts_;
        }

        public void setOpts_(CreateOpts opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("CreateOpts")
        public class CreateOpts {

            @JsonProperty("name")
            protected String name_;

            @JsonProperty("description")
            protected String description_;

            @JsonProperty("admin_state_up")
            protected Boolean adminStateUp_;

            @JsonProperty("distributed")
            protected Boolean distributed_;

            @JsonProperty("tenant_id")
            protected String tenantID_;

            @JsonProperty("project_id")
            protected String projectID_;

            @JsonProperty("external_gateway_info")
            protected GatewayInfo gatewayInfo_;

            @JsonProperty("availability_zone_hints")
            protected List<String> availabilityZoneHints_;


            public CreateOpts(){

            }
            public String getName(){
                return name_;
            }

            public void setName_(String name_){
                this.name_ = name_;
            }

            public String getDescription(){
                return description_;
            }

            public void setDescription_(String description_){
                this.description_ = description_;
            }

            public Boolean getAdminStateUp(){
                return adminStateUp_;
            }

            public void setAdminStateUp_(Boolean adminStateUp_){
                this.adminStateUp_ = adminStateUp_;
            }

            public Boolean getDistributed(){
                return distributed_;
            }

            public void setDistributed_(Boolean distributed_){
                this.distributed_ = distributed_;
            }

            public String getTenantID(){
                return tenantID_;
            }

            public void setTenantID_(String tenantID_){
                this.tenantID_ = tenantID_;
            }

            public String getProjectID(){
                return projectID_;
            }

            public void setProjectID_(String projectID_){
                this.projectID_ = projectID_;
            }

            public GatewayInfo getGatewayInfo(){
                return gatewayInfo_;
            }

            public void setGatewayInfo_(GatewayInfo gatewayInfo_){
                this.gatewayInfo_ = gatewayInfo_;
            }

            public List<String> getAvailabilityZoneHints(){
                return availabilityZoneHints_;
            }

            public void setAvailabilityZoneHints_(List<String> availabilityZoneHints_){
                this.availabilityZoneHints_ = availabilityZoneHints_;
            }




            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonRootName("external_gateway_info,omitempty")
            public class GatewayInfo {

                @JsonProperty("network_id")
                protected String networkID_;

                @JsonProperty("enable_snat")
                protected Boolean enableSNAT_;

                @JsonProperty("external_fixed_ips")
                protected List<ExternalFixedIP> externalFixedIPs_;


                public GatewayInfo(){

                }
                public String getNetworkID(){
                    return networkID_;
                }

                public void setNetworkID_(String networkID_){
                    this.networkID_ = networkID_;
                }

                public Boolean getEnableSNAT(){
                    return enableSNAT_;
                }

                public void setEnableSNAT_(Boolean enableSNAT_){
                    this.enableSNAT_ = enableSNAT_;
                }

                public List<ExternalFixedIP> getExternalFixedIPs(){
                    return externalFixedIPs_;
                }

                public void setExternalFixedIPs_(List<ExternalFixedIP> externalFixedIPs_){
                    this.externalFixedIPs_ = externalFixedIPs_;
                }




                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
                @JsonIgnoreProperties(ignoreUnknown = true)
                @JsonRootName("ExternalFixedIP")
                public class ExternalFixedIP {

                    @JsonProperty("ip_address")
                    protected String iPAddress_;

                    @JsonProperty("subnet_id")
                    protected String subnetID_;


                    public ExternalFixedIP(){

                    }
                    public String getIPAddress(){
                        return iPAddress_;
                    }

                    public void setIPAddress_(String iPAddress_){
                        this.iPAddress_ = iPAddress_;
                    }

                    public String getSubnetID(){
                        return subnetID_;
                    }

                    public void setSubnetID_(String subnetID_){
                        this.subnetID_ = subnetID_;
                    }



                }


            }


        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("ListL3AgentsNetworkingV2ExtensionsLayer3Routers")
    public class ListL3Agents {

        @JsonProperty("Id")
        protected String id_;


        public ListL3Agents(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }



    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("DeleteNetworkingV2ExtensionsLayer3Routers")
    public class Delete {

        @JsonProperty("Id")
        protected String id_;


        public Delete(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }



    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("RemoveInterfaceNetworkingV2ExtensionsLayer3Routers")
    public class RemoveInterface {

        @JsonProperty("Id")
        protected String id_;

        @JsonProperty("Opts")
        protected RemoveInterfaceOpts opts_;


        public RemoveInterface(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }

        public RemoveInterfaceOpts getOpts(){
            return opts_;
        }

        public void setOpts_(RemoveInterfaceOpts opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("RemoveInterfaceOpts")
        public class RemoveInterfaceOpts {

            @JsonProperty("subnet_id")
            protected String subnetID_;

            @JsonProperty("port_id")
            protected String portID_;


            public RemoveInterfaceOpts(){

            }
            public String getSubnetID(){
                return subnetID_;
            }

            public void setSubnetID_(String subnetID_){
                this.subnetID_ = subnetID_;
            }

            public String getPortID(){
                return portID_;
            }

            public void setPortID_(String portID_){
                this.portID_ = portID_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("AddInterfaceNetworkingV2ExtensionsLayer3Routers")
    public class AddInterface {

        @JsonProperty("Id")
        protected String id_;

        @JsonProperty("Opts")
        protected AddInterfaceOpts opts_;


        public AddInterface(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }

        public AddInterfaceOpts getOpts(){
            return opts_;
        }

        public void setOpts_(AddInterfaceOpts opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("AddInterfaceOpts")
        public class AddInterfaceOpts {

            @JsonProperty("subnet_id")
            protected String subnetID_;

            @JsonProperty("port_id")
            protected String portID_;


            public AddInterfaceOpts(){

            }
            public String getSubnetID(){
                return subnetID_;
            }

            public void setSubnetID_(String subnetID_){
                this.subnetID_ = subnetID_;
            }

            public String getPortID(){
                return portID_;
            }

            public void setPortID_(String portID_){
                this.portID_ = portID_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("GetNetworkingV2ExtensionsLayer3Routers")
    public class Get {

        @JsonProperty("Id")
        protected String id_;


        public Get(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }



    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("UpdateNetworkingV2ExtensionsLayer3Routers")
    public class Update {

        @JsonProperty("Id")
        protected String id_;

        @JsonProperty("Opts")
        protected UpdateOpts opts_;


        public Update(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }

        public UpdateOpts getOpts(){
            return opts_;
        }

        public void setOpts_(UpdateOpts opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("UpdateOpts")
        public class UpdateOpts {

            @JsonProperty("name")
            protected String name_;

            @JsonProperty("description")
            protected String description_;

            @JsonProperty("admin_state_up")
            protected Boolean adminStateUp_;

            @JsonProperty("distributed")
            protected Boolean distributed_;

            @JsonProperty("external_gateway_info")
            protected GatewayInfo gatewayInfo_;

            @JsonProperty("routes")
            protected List<Route> routes_;


            public UpdateOpts(){

            }
            public String getName(){
                return name_;
            }

            public void setName_(String name_){
                this.name_ = name_;
            }

            public String getDescription(){
                return description_;
            }

            public void setDescription_(String description_){
                this.description_ = description_;
            }

            public Boolean getAdminStateUp(){
                return adminStateUp_;
            }

            public void setAdminStateUp_(Boolean adminStateUp_){
                this.adminStateUp_ = adminStateUp_;
            }

            public Boolean getDistributed(){
                return distributed_;
            }

            public void setDistributed_(Boolean distributed_){
                this.distributed_ = distributed_;
            }

            public GatewayInfo getGatewayInfo(){
                return gatewayInfo_;
            }

            public void setGatewayInfo_(GatewayInfo gatewayInfo_){
                this.gatewayInfo_ = gatewayInfo_;
            }

            public List<Route> getRoutes(){
                return routes_;
            }

            public void setRoutes_(List<Route> routes_){
                this.routes_ = routes_;
            }




            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonRootName("external_gateway_info,omitempty")
            public class GatewayInfo {

                @JsonProperty("network_id")
                protected String networkID_;

                @JsonProperty("enable_snat")
                protected Boolean enableSNAT_;

                @JsonProperty("external_fixed_ips")
                protected List<ExternalFixedIP> externalFixedIPs_;


                public GatewayInfo(){

                }
                public String getNetworkID(){
                    return networkID_;
                }

                public void setNetworkID_(String networkID_){
                    this.networkID_ = networkID_;
                }

                public Boolean getEnableSNAT(){
                    return enableSNAT_;
                }

                public void setEnableSNAT_(Boolean enableSNAT_){
                    this.enableSNAT_ = enableSNAT_;
                }

                public List<ExternalFixedIP> getExternalFixedIPs(){
                    return externalFixedIPs_;
                }

                public void setExternalFixedIPs_(List<ExternalFixedIP> externalFixedIPs_){
                    this.externalFixedIPs_ = externalFixedIPs_;
                }




                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
                @JsonIgnoreProperties(ignoreUnknown = true)
                @JsonRootName("ExternalFixedIP")
                public class ExternalFixedIP {

                    @JsonProperty("ip_address")
                    protected String iPAddress_;

                    @JsonProperty("subnet_id")
                    protected String subnetID_;


                    public ExternalFixedIP(){

                    }
                    public String getIPAddress(){
                        return iPAddress_;
                    }

                    public void setIPAddress_(String iPAddress_){
                        this.iPAddress_ = iPAddress_;
                    }

                    public String getSubnetID(){
                        return subnetID_;
                    }

                    public void setSubnetID_(String subnetID_){
                        this.subnetID_ = subnetID_;
                    }



                }


            }



            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonRootName("Route")
            public class Route {

                @JsonProperty("nexthop")
                protected String nextHop_;

                @JsonProperty("destination")
                protected String destinationCIDR_;


                public Route(){

                }
                public String getNextHop(){
                    return nextHop_;
                }

                public void setNextHop_(String nextHop_){
                    this.nextHop_ = nextHop_;
                }

                public String getDestinationCIDR(){
                    return destinationCIDR_;
                }

                public void setDestinationCIDR_(String destinationCIDR_){
                    this.destinationCIDR_ = destinationCIDR_;
                }



            }


        }


    }


}
