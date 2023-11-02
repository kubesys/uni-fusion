/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstacksnapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description OpenstackSnapshot Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("BlockstorageV3Snapshots")
public class Domain {

    @JsonProperty("id")
    protected String iD_;

    @JsonProperty("CreatedAt")
    protected Date createdAt_;

    @JsonProperty("UpdatedAt")
    protected Date updatedAt_;

    @JsonProperty("name")
    protected String name_;

    @JsonProperty("description")
    protected String description_;

    @JsonProperty("volume_id")
    protected String volumeID_;

    @JsonProperty("status")
    protected String status_;

    @JsonProperty("size")
    protected Integer size_;

    @JsonProperty("metadata")
    protected HashMap<String, String> metadata_;


    public Domain(){

    }
    public String getID(){
        return iD_;
    }

    public void setID_(String iD_){
        this.iD_ = iD_;
    }

    public Date getCreatedAt(){
        return createdAt_;
    }

    public void setCreatedAt_(Date createdAt_){
        this.createdAt_ = createdAt_;
    }

    public Date getUpdatedAt(){
        return updatedAt_;
    }

    public void setUpdatedAt_(Date updatedAt_){
        this.updatedAt_ = updatedAt_;
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

    public String getVolumeID(){
        return volumeID_;
    }

    public void setVolumeID_(String volumeID_){
        this.volumeID_ = volumeID_;
    }

    public String getStatus(){
        return status_;
    }

    public void setStatus_(String status_){
        this.status_ = status_;
    }

    public Integer getSize(){
        return size_;
    }

    public void setSize_(Integer size_){
        this.size_ = size_;
    }

    public HashMap<String, String> getMetadata(){
        return metadata_;
    }

    public void setMetadata_(HashMap<String, String> metadata_){
        this.metadata_ = metadata_;
    }



}
