/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.addons.KubernetesWriter;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since 2.0.3
 *
 */
public class FrontendUtils {

	public final static Map<String, String> DEF_FRONTEND_MAPPER = new HashMap<>();

	public final static String DEFAULT_YAML_DIR = System.getProperty("user.dir") + "/yamls";

	protected final static KubernetesWriter kubeWriter = new KubernetesWriter();
	
	static {
		DEF_FRONTEND_MAPPER.put("desc",
				"{\r\n" + "        \"metadata\":{\r\n" + "                \"name\": \"$NAME$\",\r\n"
						+ "                \"namespace\": \"default\"\r\n" + "        },\r\n"
						+ "        \"apiVersion\":\"doslab.io/v1\",\r\n" + "        \"kind\":\"Frontend\",\r\n"
						+ "        \"spec\":{\r\n" + "                \"desc\": \"这是一个用户自定义资源。\",\r\n"
						+ "                \"type\": \"description\"\r\n" + "        }\r\n" + "}");
		DEF_FRONTEND_MAPPER.put("action",
				"{\r\n" + "        \"metadata\":{\r\n" + "                \"name\": \"$NAME$\",\r\n"
						+ "                \"namespace\": \"default\"\r\n" + "        },\r\n"
						+ "        \"apiVersion\":\"doslab.io/v1\",\r\n" + "        \"kind\":\"Frontend\",\r\n"
						+ "        \"spec\":{\r\n" + "                \"data\":[\r\n" + "                        {\r\n"
						+ "                                \"type\":\"delete\",\r\n"
						+ "                                \"key\":\"删除\"\r\n" + "                        },\r\n"
						+ "                        {\r\n" + "                                \"type\":\"update\",\r\n"
						+ "                                \"key\":\"更新\"\r\n" + "                        }\r\n"
						+ "                ],\r\n" + "                \"type\":\"action\"\r\n" + "        }\r\n" + "}");
		DEF_FRONTEND_MAPPER.put("formsearch", "{\r\n" + "        \"metadata\": {\r\n"
				+ "                \"name\": \"$NAME$\",\r\n" + "                \"namespace\": \"default\"\r\n"
				+ "        },\r\n" + "        \"apiVersion\": \"doslab.io/v1\",\r\n"
				+ "        \"kind\": \"Frontend\",\r\n" + "        \"spec\": {\r\n" + "                \"data\": {\r\n"
				+ "                        \"items\": [{\r\n"
				+ "                                \"label\": \"名称:\",\r\n"
				+ "                                \"path\": \"metadata##name\",\r\n"
				+ "                                \"type\": \"textbox\"\r\n" + "                        }]\r\n"
				+ "                },\r\n" + "                \"type\": \"formsearch\"\r\n" + "        }\r\n" + "}");
		DEF_FRONTEND_MAPPER.put("table", "{\r\n" + "\r\n" + "	\"apiVersion\": \"doslab.io/v1\",\r\n"
				+ "	\"kind\": \"Frontend\",\r\n" + "	\"metadata\": {\r\n"
				+ "                \"name\": \"$NAME$\",\r\n" + "                \"namespace\": \"default\"\r\n"
				+ "	},\r\n" + "	\"spec\": {\r\n" + "		\"data\": [{\r\n" + "				\"label\": \"资源名\",\r\n"
				+ "				\"row\": \"metadata.name\"\r\n" + "			},\r\n" + "			{\r\n"
				+ "				\"label\": \"创建时间\",\r\n" + "				\"row\": \"metadata.creationTimestamp\"\r\n"
				+ "			},\r\n" + "			{\r\n" + "				\"kind\": \"action\",\r\n"
				+ "				\"label\": \"更多操作\"\r\n" + "			}\r\n" + "		],\r\n"
				+ "		\"type\": \"table\"\r\n" + "	}\r\n" + "}");
	}
	
	static {
		if (!new File(FrontendUtils.DEFAULT_YAML_DIR).exists()) {
			new File(FrontendUtils.DEFAULT_YAML_DIR).mkdirs();
		}
	}
	
	public static void writeAsYaml(String name, JsonNode result) throws Exception {
		kubeWriter.writeAsYaml(FrontendUtils.DEFAULT_YAML_DIR + "/" + name, result);
	}
	
	public static String getJson(String key, String name) {
		return DEF_FRONTEND_MAPPER.get(key).replace("$NAME$", name.toLowerCase());
	}
}
