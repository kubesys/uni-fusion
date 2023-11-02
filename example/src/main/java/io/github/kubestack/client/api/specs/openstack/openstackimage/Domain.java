/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstackimage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
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
@JsonRootName("ImageserviceV2Images")
public class Domain {

    @JsonProperty("id")
    protected String iD_;

    @JsonProperty("name")
    protected String name_;

    @JsonProperty("status")
    protected String status_;

    @JsonProperty("tags")
    protected List<String> tags_;

    @JsonProperty("container_format")
    protected String containerFormat_;

    @JsonProperty("disk_format")
    protected String diskFormat_;

    @JsonProperty("min_disk")
    protected Integer minDiskGigabytes_;

    @JsonProperty("min_ram")
    protected Integer minRAMMegabytes_;

    @JsonProperty("owner")
    protected String owner_;

    @JsonProperty("protected")
    protected Boolean protected_;

    @JsonProperty("visibility")
    protected String visibility_;

    @JsonProperty("os_hidden")
    protected Boolean hidden_;

    @JsonProperty("checksum")
    protected String checksum_;

    @JsonProperty("SizeBytes")
    protected Integer sizeBytes_;

    @JsonProperty("metadata")
    protected HashMap<String, String> metadata_;

    @JsonProperty("Properties")
    protected HashMap<String, Object> properties_;

    @JsonProperty("created_at")
    protected Date createdAt_;

    @JsonProperty("updated_at")
    protected Date updatedAt_;

    @JsonProperty("file")
    protected String file_;

    @JsonProperty("schema")
    protected String schema_;

    @JsonProperty("virtual_size")
    protected Integer virtualSize_;

    @JsonProperty("OpenStackImageImportMethods")
    protected List<String> openStackImageImportMethods_;

    @JsonProperty("OpenStackImageStoreIDs")
    protected List<String> openStackImageStoreIDs_;


    public Domain(){

    }
    public String getID(){
        return iD_;
    }

    public void setID_(String iD_){
        this.iD_ = iD_;
    }

    public String getName(){
        return name_;
    }

    public void setName_(String name_){
        this.name_ = name_;
    }

    public String getStatus(){
        return status_;
    }

    public void setStatus_(String status_){
        this.status_ = status_;
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

    public Integer getMinDiskGigabytes(){
        return minDiskGigabytes_;
    }

    public void setMinDiskGigabytes_(Integer minDiskGigabytes_){
        this.minDiskGigabytes_ = minDiskGigabytes_;
    }

    public Integer getMinRAMMegabytes(){
        return minRAMMegabytes_;
    }

    public void setMinRAMMegabytes_(Integer minRAMMegabytes_){
        this.minRAMMegabytes_ = minRAMMegabytes_;
    }

    public String getOwner(){
        return owner_;
    }

    public void setOwner_(String owner_){
        this.owner_ = owner_;
    }

    public Boolean getProtected(){
        return protected_;
    }

    public void setProtected_(Boolean protected_){
        this.protected_ = protected_;
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

    public String getChecksum(){
        return checksum_;
    }

    public void setChecksum_(String checksum_){
        this.checksum_ = checksum_;
    }

    public Integer getSizeBytes(){
        return sizeBytes_;
    }

    public void setSizeBytes_(Integer sizeBytes_){
        this.sizeBytes_ = sizeBytes_;
    }

    public HashMap<String, String> getMetadata(){
        return metadata_;
    }

    public void setMetadata_(HashMap<String, String> metadata_){
        this.metadata_ = metadata_;
    }

    public HashMap<String, Object> getProperties(){
        return properties_;
    }

    public void setProperties_(HashMap<String, Object> properties_){
        this.properties_ = properties_;
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

    public String getFile(){
        return file_;
    }

    public void setFile_(String file_){
        this.file_ = file_;
    }

    public String getSchema(){
        return schema_;
    }

    public void setSchema_(String schema_){
        this.schema_ = schema_;
    }

    public Integer getVirtualSize(){
        return virtualSize_;
    }

    public void setVirtualSize_(Integer virtualSize_){
        this.virtualSize_ = virtualSize_;
    }

    public List<String> getOpenStackImageImportMethods(){
        return openStackImageImportMethods_;
    }

    public void setOpenStackImageImportMethods_(List<String> openStackImageImportMethods_){
        this.openStackImageImportMethods_ = openStackImageImportMethods_;
    }

    public List<String> getOpenStackImageStoreIDs(){
        return openStackImageStoreIDs_;
    }

    public void setOpenStackImageStoreIDs_(List<String> openStackImageStoreIDs_){
        this.openStackImageStoreIDs_ = openStackImageStoreIDs_;
    }



}
