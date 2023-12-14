/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.targets.postgres;

import java.sql.Timestamp;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.cores.Environment;
import io.github.kubesys.mirror.cores.clients.PostgresClient;
import io.github.kubesys.mirror.datas.KubeDataModel;
import io.github.kubesys.mirror.utils.SQLUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.2.0
 * @since    2023/07/25
 *
 */
public class PostgresDataMgr {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(PostgresDataMgr.class.getName());
	
	/**
	 * 创建对象的SQL语法
	 */
	public static final String INSERT = "INSERT INTO #NAME# (\"name\", \"namespace\", \"apigroup\", \"region\", \"data\", \"created\", \"updated\") " +
            "VALUES (?, ?, ?, ?, CAST(? AS json), ?, ?)";
	
	/**
	 * 更新对象的SQL语法
	 */
	public static final String UPDATE = "UPDATE #NAME# SET data = CAST(? AS json), updated = ? WHERE name = ? AND namespace = ? AND apigroup = ? AND region = ?";
	
	/**
	 * 删除对象的SQL语法
	 */
	public static final String DELETE = "DELETE FROM #NAME# WHERE name = ? AND namespace = ? AND apigroup = ? AND region = ?";

	
	/**
	 * SQL中关键字Name
	 */
	public static final String TABLE_NAME = "#NAME#";
	
	/**
	 * PG的客户端
	 */
	private final PostgresClient pgClient;
	
	
	/**
	 * @param pgClient   pgClient
	 */
	public PostgresDataMgr(PostgresClient pgClient) {
		this.pgClient = pgClient;
	}

	/**
	 * @param data    data
	 * @throws Exception Exception
	 */
	public synchronized void saveData(KubeDataModel data) throws Exception {
		
		EntityManager entityManager = pgClient.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		try {
			JsonNode value = data.getData();
			Timestamp updatedTime = SQLUtil.updatedTime();
			entityManager.createNativeQuery(INSERT.replace(TABLE_NAME, 
							SQLUtil.table(data.getMeta().getPlural())))
		        .setParameter(1, KubeUtil.getName(value))
		        .setParameter(2, KubeUtil.getNamespace(value))
		        .setParameter(3, KubeUtil.getGroup(value))
		        .setParameter(4, System.getenv(Environment.ENV_KUBE_REGION))
		        .setParameter(5, value.toPrettyString())
		        .setParameter(6, SQLUtil.createdTime(value))
		        .setParameter(7, updatedTime)
		        .executeUpdate();
			transaction.commit();
			m_logger.info("insert data sucessfully at " + updatedTime + ": (" + data.getMeta().getKind() + "," 
								+ KubeUtil.getNamespace(value) + "," + KubeUtil.getName(value) + ")");
		} catch (Exception ex) {
			m_logger.warning("unable to insert data: " + ex + ":" + data.getData().toPrettyString());
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	/**
	 * @param data  data
	 * @throws Exception Exception
	 */
	public synchronized void updateData(KubeDataModel data) throws Exception {
		EntityManager entityManager = pgClient.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		try {
			JsonNode value = data.getData();
			entityManager.createNativeQuery(UPDATE.replace(TABLE_NAME, 
							SQLUtil.table(data.getMeta().getPlural())))
				.setParameter(1, value.toPrettyString())
				.setParameter(2, SQLUtil.updatedTime())
		        .setParameter(3, KubeUtil.getName(value))
		        .setParameter(4, KubeUtil.getNamespace(value))
		        .setParameter(5, KubeUtil.getGroup(value))
		        .setParameter(6, System.getenv(Environment.ENV_KUBE_REGION))
		        .executeUpdate();
			transaction.commit();
			m_logger.info("update data sucessfully at " + SQLUtil.updatedTime() + ": (" + data.getMeta().getKind() + "," 
					+ KubeUtil.getNamespace(value) + "," + KubeUtil.getName(value) + ")");
		} catch (Exception ex) {
			m_logger.warning("unable to update data: " + ex + ":" + data.getData().toPrettyString());
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	/**
	 * @param data data
	 * @throws Exception Exception
	 */
	public synchronized void deleteData(KubeDataModel data) throws Exception {
		EntityManager entityManager = pgClient.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		try {
			JsonNode value = data.getData();
			entityManager.createNativeQuery(DELETE.replace(TABLE_NAME, 
							SQLUtil.table(data.getMeta().getPlural())))
		        .setParameter(1, KubeUtil.getName(value))
		        .setParameter(2, KubeUtil.getNamespace(value))
		        .setParameter(3, KubeUtil.getGroup(value))
		        .setParameter(4, System.getenv(Environment.ENV_KUBE_REGION))
		        .executeUpdate();
			transaction.commit();
			m_logger.info("delete data sucessfully at " + SQLUtil.updatedTime() + ": (" + data.getMeta().getKind() + "," 
					+ KubeUtil.getNamespace(value) + "," + KubeUtil.getName(value) + ")");
		} catch (Exception ex) {
			m_logger.warning("unable to delete data: " + ex + ":" + data.getData().toPrettyString());
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}
}
