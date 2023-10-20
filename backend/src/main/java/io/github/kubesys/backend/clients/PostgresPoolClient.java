/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.clients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.kubesys.backend.clients.PostgresPoolClient.SQLObject.CompareCondition;
import io.github.kubesys.backend.models.auth.AuthBaseModel;
import io.github.kubesys.devfrk.spring.utils.ClassUtils;
import io.github.kubesys.devfrk.spring.utils.JSONUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

/**
 * @author wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since 2023/07/24
 * 
 */
@Component
public class PostgresPoolClient {

	/**
	 * 日志
	 */
	private static Logger m_logger = Logger.getLogger(PostgresPoolClient.class.getName());

	/**
	 * 数据表
	 */
	public static final Map<String, String> tables = new HashMap<>();

	static {
		Set<Class<?>> classes = ClassUtils.scan(new String[] { AuthBaseModel.class.getPackageName() });
		for (Class<?> clazz : classes) {
			Table table = clazz.getAnnotation(Table.class);
			if (table != null) {
				tables.put(clazz.getName(), table.name());
			}
		}
	}

	private static final Map<Integer, String> compares = new HashMap<>();

	static {
		compares.put(0, "=");
		compares.put(1, ">");
		compares.put(2, ">=");
		compares.put(3, "<");
		compares.put(4, "<=");
	}

	@Autowired
	private EntityManager authEntityManager;

	@Autowired
	private EntityManager kubeEntityManager;

	EntityManager getEntityManager(String cls) {
		if (tables.containsKey(cls)) {
			return authEntityManager;
		}
		return kubeEntityManager;
	}

	public void createObject(String cls, JsonNode data) throws Exception {
		
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			Object treeToValue = objectMapper.treeToValue(data, Class.forName(cls));
			entityManager.persist(treeToValue);
			transaction.commit();
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		
	}

	public void updateObject(String cls, JsonNode data) throws Exception {
		
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
			entityManager.merge(treeToValue);
			transaction.commit();
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	@Transactional
	public void removeObject(String cls, JsonNode data) throws Exception {
		
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
			entityManager.remove(treeToValue);
			transaction.commit();
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		
	}

	
	public Object find(Class<?> cls, Object obj) throws Exception {
		EntityManager entityManager = getEntityManager(cls.getName());
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Object find = entityManager.find(cls, obj);
			transaction.commit();
			return find;
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return null;
	}
	
	public JsonNode getObject(String cls, SQLObject data) throws Exception {
		return listObjects(cls, data, 1, 10).get("data").get(0);
	}

	@Transactional
	public long countObjects(String cls, SQLObject data) throws Exception {

		SQLBuilder builder = new SQLBuilder();

		// SELECT and Conditions
		builder.buildSelect(cls).buildConditions(data);
		return (long) execCountSql(cls, builder.getValue());

	}

	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public long countObjects(String cls, String sql) {
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Query query = entityManager.createNativeQuery(sql);
			long singleResult = (long) query.getSingleResult();
			transaction.commit();
			return singleResult;
		} catch (NoResultException ex) {
			m_logger.warning("没有查询到任何结果");
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return -1;
	}
	
	public JsonNode listObjects(String cls, SQLObject data, int page, int number) throws Exception {

		SQLBuilder builder = new SQLBuilder();

		// SELECT and Conditions
		builder.buildSelect(cls).buildConditions(data);

		// COUNT
		long total = (long) execCountSql(cls, builder.getValue());

		// LIMIT
		builder.addLimit(page, number);

		List<Object> list = execQuerySql(cls, builder.getValue(), buildItems(data));

		// RESULT
		ObjectNode result = new ObjectMapper().createObjectNode();
		result.put("count", total);
		result.set("data", JSONUtils.fromList(list));

		return result;
	}

	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public JsonNode listObjects(String cls, String sql) {
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Query query = entityManager.createNativeQuery(sql);
			List<?> mutipleResults = query.getResultList();
			transaction.commit();
			return new ObjectMapper().readTree(mutipleResults.toString());
		} catch (JsonProcessingException e) {
			m_logger.warning("结果转JSON失败" + e);
		} catch (NoResultException ex) {
			m_logger.warning("没有查询到任何结果" + ex);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return null;
	}
	private StringBuilder buildItems(SQLObject data) {
		StringBuilder sb = new StringBuilder();
		sb.append(data.distinct ? " DISTINCT " : "").append(data.getTargets());
		return sb;
	}

	@SuppressWarnings("unchecked")
	private List<Object> execQuerySql(String cls, String sql, StringBuilder sb) {
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			String query = sql.replace("#TEMP#", sb.toString());
			m_logger.info("Query:" + query);
			transaction.commit();
			return entityManager.createNativeQuery(query).getResultList();
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		
		return null;
	}

	private long execCountSql(String cls, String sql) {
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			String count = sql.replace("#TEMP#", "COUNT(*)");
			m_logger.info("Count:" + count);
			transaction.commit();
			return (long) entityManager.createNativeQuery(count).getSingleResult();
		} catch (Exception e) {
			m_logger.warning("SQL执行失败" + e);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		
		return -1;
	}
	
	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public JsonNode getObject(String cls, String sql) {
		EntityManager entityManager = getEntityManager(cls);
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Query query = entityManager.createNativeQuery(sql);
			Object singleResult = query.getSingleResult();
			transaction.commit();
			return new ObjectMapper().readTree(singleResult.toString());
		} catch (JsonProcessingException e) {
			m_logger.warning("结果转JSON失败" + e);
		} catch (NoResultException ex) {
			m_logger.warning("没有查询到任何结果" + ex);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return null;
	}

	public static class SQLBuilder {

		protected StringBuilder sqlBuilder = new StringBuilder();

		public SQLBuilder buildSelect(String cls) {
			sqlBuilder.append("SELECT ").append("#TEMP#").append(" FROM ").append(tables.get(cls));
			return this;
		}

		public SQLBuilder addLimit(int page, int number) {
			sqlBuilder.append(" LIMIT ").append(number).append(" OFFSET ").append((page - 1) * number);
			return this;
		}

		public SQLBuilder buildConditions(SQLObject data) {

			boolean first = true;

			if (data.getEqualConds() != null) {
				
				Iterator<Map.Entry<String, JsonNode>> fieldsIterator = data.getEqualConds().fields();
				while (fieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> entry = fieldsIterator.next();
					String fieldName = entry.getKey();
					String fieldValue = entry.getValue().asText();
					sqlBuilder.append(first ? " WHERE " : " AND ")
									.append(fieldName).append(" = '")
									.append(fieldValue).append("'");
					first = false;
				}
				
			}

			if (data.getLikeConds() != null) {
				Iterator<Map.Entry<String, JsonNode>> fieldsIterator = data.getLikeConds().fields();
				while (fieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> entry = fieldsIterator.next();
					String fieldName = entry.getKey();
					String fieldValue = entry.getValue().asText();
					sqlBuilder.append(first ? " WHERE " : " AND ")
							.append(fieldName).append(" LIKE '%")
							.append(fieldValue).append("%'");
					first = false;
				}

			}

			return this;
		}

		public String getValue() {
			return sqlBuilder.toString();
		}

	}

	public static class SQLObjectBuilder {

		protected SQLObject sqlObj = new SQLObject();

		public SQLObjectBuilder addTarget(String targets) {
			sqlObj.setTargets(targets);
			return this;
		}

		public SQLObjectBuilder addEqualCondition(JsonNode json) {
			sqlObj.setEqualConds(json);
			return this;
		}

		public SQLObjectBuilder addLikeCondition(JsonNode json) {
			sqlObj.setLikeConds(json);
			return this;
		}

		public SQLObjectBuilder addEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>()
					: sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 0));
			sqlObj.setCompareConds(ccs);
			return this;
		}

		public SQLObjectBuilder addGreatThanCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>()
					: sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 1));
			sqlObj.setCompareConds(ccs);
			return this;
		}

		public SQLObjectBuilder addGreatThanAndEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>()
					: sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 2));
			sqlObj.setCompareConds(ccs);
			return this;
		}

		public SQLObjectBuilder addLessThanCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>()
					: sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 3));
			sqlObj.setCompareConds(ccs);
			return this;
		}

		public SQLObjectBuilder addLessThanAndEqualCondition(String key, Object val) {
			List<CompareCondition> ccs = sqlObj.getCompareConds() == null ? new ArrayList<>()
					: sqlObj.getCompareConds();
			ccs.add(new CompareCondition(key, val, 4));
			sqlObj.setCompareConds(ccs);
			return this;
		}

		public SQLObject build() {
			return sqlObj;
		}
	}

	/**
	 * internal use
	 *
	 */
	public static class SQLObject {

		/**
		 * 对于 select a,b,c from table，targets表示a,b,c 如果为null，则表示*
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private String targets = "*";

		/**
		 * SQL语法的where条件，只对String类型有效 如 select * from table where a = '%Value%'
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private JsonNode equalConds;

		/**
		 * SQL语法的where条件，只对String类型有效 如 select * from table where a = '%Value%'
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private JsonNode likeConds;

		/**
		 * SQL语法的where条件，只对String、long、int和boolan类型有效 比大小，如0是等于，1是大于，2是大于等于，3是小于，4是小于等于
		 * https://www.w3school.com.cn/sql/sql_syntax.asp
		 */
		private List<CompareCondition> compareConds;

		/**
		 * TODO 对结果升序和降序排列
		 */
		private SortCondition sortConds;

		public SQLObject() {
			super();
		}
		
		public SQLObject(String targets, boolean equals, JsonNode json) {
			super();
			this.targets = targets;
			if (equals) {
				this.equalConds = json;
			} else {
				this.likeConds = json;
			}
		}
		
		public SQLObject(boolean equals, JsonNode json) {
			super();
			if (equals) {
				this.equalConds = json;
			} else {
				this.likeConds = json;
			}
		}
		
		public SQLObject(String targets, JsonNode equalConds, JsonNode likeConds, List<CompareCondition> compareConds,
				SortCondition sortConds, boolean distinct) {
			super();
			this.targets = targets;
			this.equalConds = equalConds;
			this.likeConds = likeConds;
			this.compareConds = compareConds;
			this.sortConds = sortConds;
			this.distinct = distinct;
		}

		private boolean distinct = false;

		public boolean isDistinct() {
			return distinct;
		}

		public void setDistinct(boolean distinct) {
			this.distinct = distinct;
		}

		public SortCondition getSortConds() {
			return sortConds;
		}

		public void setSortConds(SortCondition sortConds) {
			this.sortConds = sortConds;
		}

		public String getTargets() {
			return targets;
		}

		public void setTargets(String targets) {
			this.targets = targets;
		}

		public JsonNode getEqualConds() {
			return equalConds;
		}

		public void setEqualConds(JsonNode equalConds) {
			this.equalConds = equalConds;
		}

		public JsonNode getLikeConds() {
			return likeConds;
		}

		public void setLikeConds(JsonNode likeConds) {
			this.likeConds = likeConds;
		}

		public List<CompareCondition> getCompareConds() {
			return compareConds;
		}

		public void setCompareConds(List<CompareCondition> compareConds) {
			this.compareConds = compareConds;
		}

		static class CompareCondition {

			protected String name;

			protected Object value;

			/**
			 * 0, equal, 1 great, 2 great and equal, 3. less 4 less equal
			 */
			protected int type = 0;

			public CompareCondition() {
				super();
			}

			public CompareCondition(String name, Object value, int type) {
				super();
				this.name = name;
				this.value = value;
				this.type = type;
			}

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

		}

		static class SortCondition {

			protected String name;

			protected boolean asc;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public boolean isAsc() {
				return asc;
			}

			public void setAsc(boolean asc) {
				this.asc = asc;
			}
		}
	}
}
