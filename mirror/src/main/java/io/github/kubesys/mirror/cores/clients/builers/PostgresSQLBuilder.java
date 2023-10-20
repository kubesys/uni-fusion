/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores.clients.builers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import io.github.kubesys.mirror.utils.SQLUtil;

/**
 * @author wuheng@iscas.ac.cn
 * @version  0.1.0
 * @since   2023/06/23
 * 
 */
public class PostgresSQLBuilder {

	static final String GET_SQL = "SELECT data FROM #NAME# WHERE ";
	
	static final String COUNT_SQL = "SELECT count(*) FROM #NAME# WHERE ";
	
	static final String TABLE_NAME = "#NAME#";
	
	static final String WHERE_AND = " AND ";
	
	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(PostgresSQLBuilder.class.getName());
	
	/**
	 * @param table       表名
	 * @param primaryKeys 根据我们设计，主键是name, namespace, apigroup和region
	 * @return 精准匹配
	 */
	public String getSQL(String table, Map<String, String> primaryKeys) {
		
		primaryKeys = (primaryKeys == null) ? new HashMap<>() : primaryKeys;
		
		StringBuilder sb = new StringBuilder();
		sb.append(GET_SQL.replace(TABLE_NAME, table));
		
		for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
			String key = entry.getKey();
		    String value = entry.getValue();
		    sb.append(key).append(" = '").append(value).append("'").append(WHERE_AND);
		}

		return (primaryKeys.size() == 0) ? sb.toString() : sb.substring(0, sb.length() - 4);
	}
	
	/**
	 * @param table       表名
	 * @param dataLabels  JSON中任何字符串字段
	 * @return 模糊匹配结果
	 */
	public String countSQL(String table, Map<String, String> dataLabels) {
		
		dataLabels = (dataLabels == null) ? new HashMap<>() : dataLabels;
		
		StringBuilder sb = new StringBuilder();
		sb.append(COUNT_SQL.replace(TABLE_NAME, table));
		for (Map.Entry<String, String> entry : dataLabels.entrySet()) {
			String key = entry.getKey();
		    String value = entry.getValue();
		    sb.append(SQLUtil.jsonKey(key)).append(" like '%").append(value).append("%'").append(WHERE_AND);
		}
		return (dataLabels.size() == 0) ? sb.substring(0, sb.length() - 7) : sb.substring(0, sb.length() - 4);
	}
	
	/**
	 * @param table       表名
	 * @param dataLabels  标签集合
	 * @param page        第几页
	 * @param limit       每页显示的数据数
	 * @return 模糊匹配结果
	 */
	public String listSQL(String table, Map<String, String> dataLabels, int page, int limit) {
		
		dataLabels = (dataLabels == null) ? new HashMap<>() : dataLabels;
		
		StringBuilder sb = new StringBuilder();
		sb.append(GET_SQL.replace(TABLE_NAME, table));
		for (Map.Entry<String, String> entry : dataLabels.entrySet()) {
			String key = entry.getKey();
		    String value = entry.getValue();
		    sb.append(SQLUtil.jsonKey(key)).append(" like '%").append(value).append("%'").append(WHERE_AND);
		}
		String listSQL = (dataLabels.size() == 0) ? sb.substring(0, sb.length() - 7) : sb.substring(0, sb.length() - 4);
		return listSQL + " OFFSET " + (page - 1) * limit + " LIMIT " + limit;
	}
	
}
