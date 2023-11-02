/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstacksnapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;

/**
 * @Description OpenstackSnapshot Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("Lifecycle")
public class Lifecycle {

    @JsonProperty("UpdateMetadataBlockstorageV3Snapshots")
    protected UpdateMetadata updateMetadata_;

    @JsonProperty("DeleteBlockstorageV3Snapshots")
    protected Delete delete_;

    @JsonProperty("GetBlockstorageV3Snapshots")
    protected Get get_;

    @JsonProperty("CreateBlockstorageV3Snapshots")
    protected Create create_;

    @JsonProperty("UpdateBlockstorageV3Snapshots")
    protected Update update_;


    public Lifecycle(){

    }
    public UpdateMetadata getUpdateMetadata(){
        return updateMetadata_;
    }

    public void setUpdateMetadata_(UpdateMetadata updateMetadata_){
        this.updateMetadata_ = updateMetadata_;
    }

    public Delete getDelete(){
        return delete_;
    }

    public void setDelete_(Delete delete_){
        this.delete_ = delete_;
    }

    public Get getGet(){
        return get_;
    }

    public void setGet_(Get get_){
        this.get_ = get_;
    }

    public Create getCreate(){
        return create_;
    }

    public void setCreate_(Create create_){
        this.create_ = create_;
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
    @JsonRootName("UpdateMetadataBlockstorageV3Snapshots")
    public class UpdateMetadata {

        @JsonProperty("Id")
        protected String id_;

        @JsonProperty("Opts")
        protected UpdateMetadataOpts opts_;


        public UpdateMetadata(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }

        public UpdateMetadataOpts getOpts(){
            return opts_;
        }

        public void setOpts_(UpdateMetadataOpts opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("UpdateMetadataOpts")
        public class UpdateMetadataOpts {

            @JsonProperty("metadata")
            protected HashMap<String, Object> metadata_;


            public UpdateMetadataOpts(){

            }
            public HashMap<String, Object> getMetadata(){
                return metadata_;
            }

            public void setMetadata_(HashMap<String, Object> metadata_){
                this.metadata_ = metadata_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("DeleteBlockstorageV3Snapshots")
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
    @JsonRootName("GetBlockstorageV3Snapshots")
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
    @JsonRootName("CreateBlockstorageV3Snapshots")
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

            @JsonProperty("volume_id")
            protected String volumeID_;

            @JsonProperty("force")
            protected Boolean force_;

            @JsonProperty("name")
            protected String name_;

            @JsonProperty("description")
            protected String description_;

            @JsonProperty("metadata")
            protected HashMap<String, String> metadata_;


            public CreateOpts(){

            }
            public String getVolumeID(){
                return volumeID_;
            }

            public void setVolumeID_(String volumeID_){
                this.volumeID_ = volumeID_;
            }

            public Boolean getForce(){
                return force_;
            }

            public void setForce_(Boolean force_){
                this.force_ = force_;
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

            public HashMap<String, String> getMetadata(){
                return metadata_;
            }

            public void setMetadata_(HashMap<String, String> metadata_){
                this.metadata_ = metadata_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("UpdateBlockstorageV3Snapshots")
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



        }


    }


}
