/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores.clients;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kubesys.mirror.cores.Environment;
import io.github.kubesys.mirror.utils.MirrorUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

/**
 * @author wuheng@iscas.ac.cn
 * @version  0.1.0
 * @since   2023/06/21
 * 
 * 面向企业基础设施管理，用户量不大，未来支持连接池
 * 目前，为每一个Kubernetes的Kind分配一个客户端
 */
public class PostgresClient   {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(PostgresClient.class.getName());
	
	/**
	 * 默认postgres地址
	 */
	static final String DEFAULT_JDBCURL    = "jdbc:postgresql://kube-database.kube-iscas:5432/kubestack?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&&serverTimezone=Asia/Shang";
	
	/**
	 * 默认用户名
	 */
	static final String DEFAULT_USERNAME   = "postgres";
	
	/**
	 * 默认密码
	 */
	static final String DEFAULT_PASSWORD   = "onceas";
	
	/**
	 * 默认驱动
	 */
	static final String DEFAULT_JDBCDRIVER = "org.postgresql.Driver";
	
	
	/**
	 * 默认驱动
	 */
	static final String DEFAULT_POOLSIZE   = "2";
	
	/**
	 * 
	 * 默认数据库增删改查管理器
	 */
	protected EntityManager entityManager;

	/**
	 * 
	 */
	public PostgresClient() {
		this(MirrorUtil.getEnv(Environment.ENV_JDBC_URL, DEFAULT_JDBCURL),
				MirrorUtil.getEnv(Environment.ENV_JDBC_USER, DEFAULT_USERNAME),
				MirrorUtil.getEnv(Environment.ENV_JDBC_PWD, DEFAULT_PASSWORD),
				MirrorUtil.getEnv(Environment.ENV_JDBC_DRIVER, DEFAULT_JDBCDRIVER),
				MirrorUtil.getEnv(Environment.ENV_JDBC_POOLSIZE, DEFAULT_POOLSIZE));
	}
	
	
	/**
	 * @param jdbcUrl        jdbc地址
	 * @param username       用户名
	 * @param password       密码
	 * @param jdbcDriver     jdbc驱动
	 * @param poolSize       jdbc线程池大小
	 */
	public PostgresClient(String jdbcUrl, String username, String password, String jdbcDriver, String poolSize) {
		try {
			Configuration configuration = new Configuration();
	        // 设置数据库连接信息
	        configuration.setProperty("hibernate.connection.url", jdbcUrl);
	        configuration.setProperty("hibernate.connection.username", username);
	        configuration.setProperty("hibernate.connection.password", password);
	        configuration.setProperty("hibernate.connection.driver_class", jdbcDriver);
	        configuration.setProperty("hibernate.connection.pool_size", poolSize);
	        configuration.setProperty("hibernate.hikari.idleTimeout", "0");
	        configuration.setProperty("hibernate.hikari.connectionTestQuery", "SELECT 1");
	        configuration.setProperty("hibernate.hikari.maxLifetime", "0");
	        
	        // 构建SessionFactory
	        SessionFactory sessionFactory = configuration.buildSessionFactory();
	        // 创建Session对象
	        Session session = sessionFactory.openSession();
	        this.entityManager = session.getEntityManagerFactory().createEntityManager();
		} catch (Exception ex) {
			m_logger.severe("wrong database parameters，or unavailable network." + ex);
			System.exit(1);
		} 
	}
	
	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public Object execWithSingleResult(String sql) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Query query = entityManager.createNativeQuery(sql);
			Object singleResult = query.getSingleResult();
			transaction.commit();
			return singleResult;
		} catch (Exception ex) {
			m_logger.warning("执行错误" + ex);
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return null;
	}
	
	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public long count(String sql) {
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
	
	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public JsonNode get(String sql) {
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
	
	
	/**
	 * @param sql        SQL
	 * @return           查询结果
	 */
	public JsonNode list(String sql) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Query query = entityManager.createNativeQuery(sql);
			List<?> mutipleResults = query.getResultList();
			transaction.commit();
			return new ObjectMapper().readTree(mutipleResults.toString());
		} catch (JsonProcessingException e) {
		} catch (NoResultException ex) {
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
		return null;
	}
	
	/**
	 * @param sql SQL
	 * @return
	 */
	public boolean execWithoutResult(String sql) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.createNativeQuery(sql).executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}
	
	
	/**
	 * @return   数据库增删改查管理器
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	
}
