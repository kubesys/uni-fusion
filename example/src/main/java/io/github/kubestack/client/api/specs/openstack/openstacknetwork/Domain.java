/**
* Code Generator by multicloud_service
*/

package io.github.kubestack.client.api.specs.openstack.openstacknetwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * @Description OpenstackNetwork Json
 * @Date 2023/February/27 15:28
 * @Author guohao
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("NetworkingV2Networks")
public class Domain {

    @JsonProperty("id")
    protected String iD_;

    @JsonProperty("name")
    protected String name_;

    @JsonProperty("description")
    protected String description_;

    @JsonProperty("admin_state_up")
    protected Boolean adminStateUp_;

    @JsonProperty("status")
    protected String status_;

    @JsonProperty("subnets")
    protected List<String> subnets_;

    @JsonProperty("tenant_id")
    protected String tenantID_;

    @JsonProperty("UpdatedAt")
    protected Date updatedAt_;

    @JsonProperty("CreatedAt")
    protected Date createdAt_;

    @JsonProperty("project_id")
    protected String projectID_;

    @JsonProperty("shared")
    protected Boolean shared_;

    @JsonProperty("availability_zone_hints")
    protected List<String> availabilityZoneHints_;

    @JsonProperty("tags")
    protected List<String> tags_;

    @JsonProperty("revision_number")
    protected Integer revisionNumber_;


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

    public String getStatus(){
        return status_;
    }

    public void setStatus_(String status_){
        this.status_ = status_;
    }

    public List<String> getSubnets(){
        return subnets_;
    }

    public void setSubnets_(List<String> subnets_){
        this.subnets_ = subnets_;
    }

    public String getTenantID(){
        return tenantID_;
    }

    public void setTenantID_(String tenantID_){
        this.tenantID_ = tenantID_;
    }

    public Date getUpdatedAt(){
        return updatedAt_;
    }

    public void setUpdatedAt_(Date updatedAt_){
        this.updatedAt_ = updatedAt_;
    }

    public Date getCreatedAt(){
        return createdAt_;
    }

    public void setCreatedAt_(Date createdAt_){
        this.createdAt_ = createdAt_;
    }

    public String getProjectID(){
        return projectID_;
    }

    public void setProjectID_(String projectID_){
        this.projectID_ = projectID_;
    }

    public Boolean getShared(){
        return shared_;
    }

    public void setShared_(Boolean shared_){
        this.shared_ = shared_;
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

    public Integer getRevisionNumber(){
        return revisionNumber_;
    }

    public void setRevisionNumber_(Integer revisionNumber_){
        this.revisionNumber_ = revisionNumber_;
    }



}
