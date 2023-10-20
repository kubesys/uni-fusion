/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.datas;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.mirror.cores.DataModel;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/18
 *
 */
public class KubeDataModel implements DataModel {
	
	
	/**
	 * 元数据描述，遵循以下格式
	 * {
		"name": "apps.DaemonSet",
		"group": "apps",
		"kind": "DaemonSet",
		"plural": "daemonsets"
	}
	 */
	protected final Meta meta;

	/**
	 * 对应Kubernetes的json数据，可以通过kubectl get [kind] [name] -n [ns] -o json进行查看
	 */
	protected final JsonNode data;
	
	
	/**
	 * @param meta            元数据
	 * @param data            数据
	 */
	public KubeDataModel(Meta meta, JsonNode data) {
		super();
		this.meta = meta;
		this.data = data;
	}

	/**
	 * @return    元数据
	 */
	public Meta getMeta() {
		return meta;
	}


	/**
	 * @return   数据
	 */
	public JsonNode getData() {
		return data;
	}

	
	/**
	 * @author  wuheng@iscas.ac.cn
	 * @version 0.1.0
	 * @since   2023/06/22
	 *
	 */
	public static class Meta {
		
		/**
		 * 对应metadata.name
		 */
		private String fullkind;
		
		/**
		 * 对应apiVersion中'/'前面的，如果没有则为""
		 */
		private String group;
		
		/**
		 * 对应kind
		 */
		private String kind;
		
		/**
		 * 对应kubectl api-resources里的plural
		 */
		private String plural;

		/**
		 * @return 名称
		 */
		public String getFullkind() {
			return fullkind;
		}

		/**
		 * @param fullkind  名称
		 */
		public void setFullkind(String fullkind) {
			this.fullkind = fullkind;
		}

		/**
		 * @return  分组
		 */
		public String getGroup() {
			return group;
		}

		/**
		 * @param group 分组
		 */
		public void setGroup(String group) {
			this.group = group;
		}

		/**
		 * @return kind类型
		 */
		public String getKind() {
			return kind;
		}

		/**
		 * @param kind  kind类型
		 */
		public void setKind(String kind) {
			this.kind = kind;
		}

		/**
		 * @return    kind的复数形式
		 */
		public String getPlural() {
			return plural;
		}

		/**
		 * @param plural  kind的复数形式 
		 */
		public void setPlural(String plural) {
			this.plural = plural;
		}
		
	}
}
