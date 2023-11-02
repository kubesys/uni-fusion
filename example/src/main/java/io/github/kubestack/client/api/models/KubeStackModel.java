/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.client.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.ObjectMeta;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since   2022.9.28
 *
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
public abstract class KubeStackModel {

	/**
	 * serialVersionUID
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1171174592223281364L;


	protected String apiVersion;

	protected String kind;

	protected ObjectMeta metadata;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public ObjectMeta getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMeta metadata) {
		this.metadata = metadata;
	}

}
