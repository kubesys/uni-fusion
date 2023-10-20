/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.utils;

import java.sql.Timestamp;
import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.mirror.cores.MirrorConstants;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/23
 *
 */
public class SQLUtil {

	private SQLUtil() {
		super();
	}

	/**
	 * 不考虑plural为null情况，如果存在则直接抛出异常
	 * 
	 * @param plural    plural名称, plural可能出现-或者/情况，但实际数据库表不支持，需求替换成""
	 * @return 得到table名
	 */
	public static String table(String plural) {
		return plural.replaceAll(MirrorConstants.TABLE_REPLACED_SOURCE, 
								MirrorConstants.TABLE_REPLACED_TARGET);
	}
	
	/**
	 * 不考虑value为null情况，如果存在则直接抛出异常
	 * 
	 * @param value    JSON中需要查询的字段
	 * @return 得到满足SQL语法的标识，比如metadata##name，变成data -> 'metadata' ->> 'name'
	 */
	public static String jsonKey(String value) {

		StringBuilder sb = new StringBuilder();
		sb.append(MirrorConstants.JSON_DATABASE_ITEM);
		
		String[] parts = value.split(MirrorConstants.JSON_INPUT_SPLIT);
		
		for (int i = 0; i < parts.length - 1; i++) {
			sb.append(MirrorConstants.JSON_MIDDLE_CONNECTOR)
				.append(MirrorConstants.JSON_SINGLE_QUOTES).append(parts[i])
				.append(MirrorConstants.JSON_SINGLE_QUOTES);
		}
		
		sb.append(MirrorConstants.JSON_LAST_CONNECTOR)
				.append(MirrorConstants.JSON_SINGLE_QUOTES)
				.append(parts[parts.length - 1])
				.append(MirrorConstants.JSON_SINGLE_QUOTES);
		
		return sb.toString();
	}
	
	/**
	 * @param json   json
	 * @return      获得kubernetes的创建时间
	 */
	public static Timestamp createdTime(JsonNode json) {
		String dateTimeStr = json.get(KubernetesConstants.KUBE_METADATA)
					.get(KubernetesConstants.KUBE_METADATA_CREATED).asText();
		// 使用DateTimeFormatter解析字符串为Instant对象
        Instant instant = Instant.parse(dateTimeStr);
        // 将Instant对象转换为java.sql.Timestamp对象
        return Timestamp.from(instant);
	}
	
	/**
	 * @return      获得kubernetes的更新时间
	 */
	public static Timestamp updatedTime() {
        return Timestamp.valueOf(java.time.LocalDateTime.now());
	}
}
