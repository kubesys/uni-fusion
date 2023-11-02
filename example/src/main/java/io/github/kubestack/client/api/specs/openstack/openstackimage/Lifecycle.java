/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstackimage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;

/**
 * @Description OpenstackImage Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("Lifecycle")
public class Lifecycle {

    @JsonProperty("CreateImageserviceV2Images")
    protected Create create_;

    @JsonProperty("UpdateImageserviceV2Images")
    protected Update update_;

    @JsonProperty("DeleteImageserviceV2Images")
    protected Delete delete_;

    @JsonProperty("GetImageserviceV2Images")
    protected Get get_;


    public Lifecycle(){

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




    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("CreateImageserviceV2Images")
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

            @JsonProperty("id")
            protected String iD_;

            @JsonProperty("visibility")
            protected String visibility_;

            @JsonProperty("os_hidden")
            protected Boolean hidden_;

            @JsonProperty("tags")
            protected List<String> tags_;

            @JsonProperty("container_format")
            protected String containerFormat_;

            @JsonProperty("disk_format")
            protected String diskFormat_;

            @JsonProperty("min_disk")
            protected Integer minDisk_;

            @JsonProperty("min_ram")
            protected Integer minRAM_;

            @JsonProperty("protected")
            protected Boolean protected_;

            @JsonProperty("Properties")
            protected HashMap<String, String> properties_;


            public CreateOpts(){

            }
            public String getName(){
                return name_;
            }

            public void setName_(String name_){
                this.name_ = name_;
            }

            public String getID(){
                return iD_;
            }

            public void setID_(String iD_){
                this.iD_ = iD_;
            }

            public String getVisibility(){
                return visibility_;
            }

            public void setVisibility_(String visibility_){
                this.visibility_ = visibility_;
            }

            public Boolean getHidden(){
                return hidden_;
            }

            public void setHidden_(Boolean hidden_){
                this.hidden_ = hidden_;
            }

            public List<String> getTags(){
                return tags_;
            }

            public void setTags_(List<String> tags_){
                this.tags_ = tags_;
            }

            public String getContainerFormat(){
                return containerFormat_;
            }

            public void setContainerFormat_(String containerFormat_){
                this.containerFormat_ = containerFormat_;
            }

            public String getDiskFormat(){
                return diskFormat_;
            }

            public void setDiskFormat_(String diskFormat_){
                this.diskFormat_ = diskFormat_;
            }

            public Integer getMinDisk(){
                return minDisk_;
            }

            public void setMinDisk_(Integer minDisk_){
                this.minDisk_ = minDisk_;
            }

            public Integer getMinRAM(){
                return minRAM_;
            }

            public void setMinRAM_(Integer minRAM_){
                this.minRAM_ = minRAM_;
            }

            public Boolean getProtected(){
                return protected_;
            }

            public void setProtected_(Boolean protected_){
                this.protected_ = protected_;
            }

            public HashMap<String, String> getProperties(){
                return properties_;
            }

            public void setProperties_(HashMap<String, String> properties_){
                this.properties_ = properties_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("UpdateImageserviceV2Images")
    public class Update {

        @JsonProperty("Id")
        protected String id_;

        @JsonProperty("Opts")
        protected List<UpdateImageserviceV2ImagesProperty> opts_;


        public Update(){

        }
        public String getId(){
            return id_;
        }

        public void setId_(String id_){
            this.id_ = id_;
        }

        public List<UpdateImageserviceV2ImagesProperty> getOpts(){
            return opts_;
        }

        public void setOpts_(List<UpdateImageserviceV2ImagesProperty> opts_){
            this.opts_ = opts_;
        }




        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonRootName("UpdateImageserviceV2ImagesProperty")
        public class UpdateImageserviceV2ImagesProperty {

            @JsonProperty("Op")
            protected String op_;

            @JsonProperty("Name")
            protected String name_;

            @JsonProperty("Value")
            protected String value_;


            public UpdateImageserviceV2ImagesProperty(){

            }
            public String getOp(){
                return op_;
            }

            public void setOp_(String op_){
                this.op_ = op_;
            }

            public String getName(){
                return name_;
            }

            public void setName_(String name_){
                this.name_ = name_;
            }

            public String getValue(){
                return value_;
            }

            public void setValue_(String value_){
                this.value_ = value_;
            }



        }


    }



    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("DeleteImageserviceV2Images")
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
    @JsonRootName("GetImageserviceV2Images")
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
