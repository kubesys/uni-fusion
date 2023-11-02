/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstacknetwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * @Description OpenstackNetwork Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("Lifecycle")
public class Lifecycle {

    @JsonProperty("CreateNetworkingV2Networks")
    protected Create create_;

    @JsonProperty("DeleteNetworkingV2Networks")
    protected Delete delete_;

    @JsonProperty("UpdateNetworkingV2Networks")
    protected Update update_;

    @JsonProperty("GetNetworkingV2Networks")
    protected Get get_;


    public Lifecycle(){

    }
    public Create getCreate(){
        return create_;
    }

    public void setCreate_(Create create_){
        this.create_ = create_;
    }

    public Delete getDelete(){
        return delete_;
    }

    public void setDelete_(Delete delete_){
        this.delete_ = delete_;
    }

    public Update getUpdate(){
        return update_;
    }

    public void setUpdate_(Update update_){
        this.update_ = update_;
    }

    public Get getGet(){
        return get_;
    }

    public void setGet_(Get get_){
        this.get_ = get_;
    }




    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("CreateNetworkingV2Networks")
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

            @JsonProperty("admin_state_up")
            protected Boolean adminStateUp_;

            @JsonProperty("name")
            protected String name_;

            @JsonProperty("description")
            protected String description_;

            @JsonProperty("shared")
            protected Boolean shared_;

            @JsonProperty("tenant_id")
            protected String tenantID_;

            @JsonProperty("project_id")
            protected String projectID_;

            @JsonProperty("availability_zone_hints")
            protected List<String> availabilityZoneHints_;


            public CreateOpts(){

            }
            public Boolean getAdminStateUp(){
                return adminStateUp_;
            }

            public void setAdminStateUp_(Boolean adminStateUp_){
                this.adminStateUp_ = adminStateUp_;
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

            public Boolean getShared(){
                return shared_;
            }

            public void setShared_(Boolean shared_){
                this.shared_ = shared_;
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

            public List<String> getAvailabilityZoneHints(){
                return availabilityZoneHints_;
            }

            public void setAvailabilityZoneHints_(List<String> availabilityZoneHints_){
                this.availabilityZoneHints_ = availabilityZoneHints_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("DeleteNetworkingV2Networks")
    public class Delete {

        @JsonProperty("NetworkID")
        protected String networkID_;


        public Delete(){

        }
        public String getNetworkID(){
            return networkID_;
        }

        public void setNetworkID_(String networkID_){
            this.networkID_ = networkID_;
        }



    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("UpdateNetworkingV2Networks")
    public class Update {

        @JsonProperty("NetworkID")
        protected String networkID_;

        @JsonProperty("Opts")
        protected UpdateOpts opts_;


        public Update(){

        }
        public String getNetworkID(){
            return networkID_;
        }

        public void setNetworkID_(String networkID_){
            this.networkID_ = networkID_;
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

            @JsonProperty("admin_state_up")
            protected Boolean adminStateUp_;

            @JsonProperty("name")
            protected String name_;

            @JsonProperty("description")
            protected String description_;

            @JsonProperty("shared")
            protected Boolean shared_;

            @JsonProperty("RevisionNumber")
            protected Integer revisionNumber_;


            public UpdateOpts(){

            }
            public Boolean getAdminStateUp(){
                return adminStateUp_;
            }

            public void setAdminStateUp_(Boolean adminStateUp_){
                this.adminStateUp_ = adminStateUp_;
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

            public Boolean getShared(){
                return shared_;
            }

            public void setShared_(Boolean shared_){
                this.shared_ = shared_;
            }

            public Integer getRevisionNumber(){
                return revisionNumber_;
            }

            public void setRevisionNumber_(Integer revisionNumber_){
                this.revisionNumber_ = revisionNumber_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("GetNetworkingV2Networks")
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


}
