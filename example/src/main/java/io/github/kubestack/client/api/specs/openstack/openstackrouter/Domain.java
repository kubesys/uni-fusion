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
@JsonRootName("NetworkingV2ExtensionsLayer3Routers")
public class Domain {

    @JsonProperty("status")
    protected String status_;

    @JsonProperty("external_gateway_info")
    protected GatewayInfo gatewayInfo_;

    @JsonProperty("admin_state_up")
    protected Boolean adminStateUp_;

    @JsonProperty("distributed")
    protected Boolean distributed_;

    @JsonProperty("name")
    protected String name_;

    @JsonProperty("description")
    protected String description_;

    @JsonProperty("id")
    protected String iD_;

    @JsonProperty("tenant_id")
    protected String tenantID_;

    @JsonProperty("project_id")
    protected String projectID_;

    @JsonProperty("routes")
    protected List<Route> routes_;

    @JsonProperty("availability_zone_hints")
    protected List<String> availabilityZoneHints_;

    @JsonProperty("tags")
    protected List<String> tags_;


    public Domain(){

    }
    public String getStatus(){
        return status_;
    }

    public void setStatus_(String status_){
        this.status_ = status_;
    }

    public GatewayInfo getGatewayInfo(){
        return gatewayInfo_;
    }

    public void setGatewayInfo_(GatewayInfo gatewayInfo_){
        this.gatewayInfo_ = gatewayInfo_;
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

    public String getID(){
        return iD_;
    }

    public void setID_(String iD_){
        this.iD_ = iD_;
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

    public List<Route> getRoutes(){
        return routes_;
    }

    public void setRoutes_(List<Route> routes_){
        this.routes_ = routes_;
    }

    public List<String> getAvailabilityZoneHints(){
        return availabilityZoneHints_;
    }

    public void setAvailabilityZoneHints_(List<String> availabilityZoneHints_){
        this.availabilityZoneHints_ = availabilityZoneHints_;
    }

    public List<String> getTags(){
        return tags_;
    }

    public void setTags_(List<String> tags_){
        this.tags_ = tags_;
    }




    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("external_gateway_info")
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
