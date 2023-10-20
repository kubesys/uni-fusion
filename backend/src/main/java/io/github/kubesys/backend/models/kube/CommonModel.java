/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models.kube;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

/**
 * @author  wuheng@iscas.ac.cn
 * @version 0.1.0
 * @since   2023/06/21
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//@Description(type = "数据表模型", value = "#NAME#")
public class CommonModel   {

	@Id
	@Column(name = "name", length = 256)
	//@Description(value = "在Kubernetes中，metadata.name的最大长度是253个字符，约束条件只能包含小写字母、数字、连字符（-）和句点（.），必须以字母或数字开头和结尾，连续的句点（.）和连字符（-）不允许出现在名称的开头或结尾，名称中的连字符（-）不允许相邻")
    private String name;
	
	@Id
	@Column(name = "namespace", length = 64)
	//@Description(value = "在Kubernetes中，metadata.name的最大长度是63个字符，约束条件只能包含小写字母、数字、连字符（-）和句点（.），必须以字母或数字开头和结尾，连续的句点（.）和连字符（-）不允许出现在名称的开头或结尾，名称中的连字符（-）不允许相邻")
	private String namespace;
	
	@Id
	@Column(name = "apiGroup", length = 128)
	//@Description(value = "在Kubernetes中，metadata.name的最大长度是128个字符，约束条件只能包含小写字母、数字、连字符（-）和句点（.），必须以字母或数字开头和结尾，连续的句点（.）和连字符（-）不允许出现在名称的开头或结尾，名称中的连字符（-）不允许相邻")
	private String apiGroup;
	
	@Id
	@Column(name = "region", length = 128)
	//@Description(value = "自定义字段，任意字符，也可以是中文")
	private String region;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "data", columnDefinition = "json")
	//@Description(value = "对应kubectl get [kind] [name] -n [namespace] -o json的具体数据")
    private JsonNode data;
	
	@CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
	//@Description(value = "数据创建时间，如2023-06-20 08:49:50.090407")
    private LocalDateTime createdAt;

	@UpdateTimestamp
    @Column(name = "updatedAt", nullable = false)
	//@Description(value = "数据更新时间，如2023-06-20 08:49:50.090407")
    private LocalDateTime updatedAt;


	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return apigroup
	 */
	public String getApiGroup() {
		return apiGroup;
	}

	/**
	 * @param apiGroup apigroup
	 */
	public void setApiGroup(String apiGroup) {
		this.apiGroup = apiGroup;
	}

	/**
	 * @return region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return data
	 */
	public JsonNode getData() {
		return data;
	}

	/**
	 * @param data data
	 */
	public void setData(JsonNode data) {
		this.data = data;
	}

	/**
	 * @return createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt createAt
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return updateAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt updateAt
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
