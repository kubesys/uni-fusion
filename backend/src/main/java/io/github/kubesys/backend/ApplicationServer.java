/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.kubesys.backend.models.auth.AuthBaseModel;
import io.github.kubesys.backend.models.kube.KubeBaseModel;
import io.github.kubesys.devfrk.spring.HttpServer;
import io.github.kubesys.devfrk.spring.constants.BeanConstants;
import io.github.kubesys.devfrk.spring.cores.HttpAuthingInterceptor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since 2023.06.28
 * 
 *        <p>
 *        启动backend服务，可以进一步对以下进行配置
 * 
 *        <li><code>src/main/resources/application.yml<code>
 *        <li><code>src/main/resources/log4j.properties<code>
 * 
 */

@ComponentScan(basePackages = { "io.github.kubesys.backend.services", "io.github.kubesys.backend.clients" })
@EntityScan(basePackages = { "io.github.kubesys.backend.models" })
@EnableTransactionManagement
@EnableJpaRepositories
@EnableConfigurationProperties(JpaProperties.class)
public class ApplicationServer extends HttpServer {

	/**
	 * 启动Backend服务
	 * 
	 * @param args 启动参数，默认是空
	 * @throws Exception 初始化启动失败报错即退出
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationServer.class, args);

	}
	
	@Bean(name = BeanConstants.AUTHING)
	public HttpAuthingInterceptor interceptor() {
		return new DefaultHttpAuthing();
	}

	/*******************************************************
	 * 
	 * 配置两个数据源，见application.yml ChatGPT告诉我的，不要问我为啥？
	 * 
	 ********************************************************/
	/**
	 * 配置application.yml中spring.jpa
	 * 注意：本框架必须配置jpa
	 * 
	 * @return JpaProperties
	 */
	@Bean(name = "jpa")
	@ConfigurationProperties(prefix = "spring.jpa")
	public JpaProperties jpaProperties() {
		return new JpaProperties();
	}

	/**
	 * 将application.yml中spring.jpa转化为Properties
	 * 注意：本框架必须配置jpa
	 * 
	 * @param jpaProperties   org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
	 * @return java.util.Properties
	 */
	@Bean(name = "jpaProperties")
	public Properties hibernateProperties(@Qualifier("jpa") JpaProperties jpaProperties) {
		Properties properties = new Properties();
		properties.putAll(jpaProperties.getProperties());
		return properties;
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * 配置application.yml中spring.datasource.auth
	 * 
	 * @return DataSource
	 */
	@Bean(name = "authDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.auth")
	public DataSource authDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 创建相关的EntityManager，供SQL查询使用
	 * 
	 * @param dataSource
	 * @param properties
	 * @return
	 */
	@Bean(name = "authEntityManager")
	@PersistenceContext(unitName = "authEntityManager")
	@DependsOn({ "jpaProperties", "authDataSource" })
	public EntityManager authEntityManager(
			@Qualifier("authDataSource") DataSource dataSource,
			@Qualifier("jpaProperties") Properties properties) {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPersistenceUnitName("authEntityManager");
		entityManagerFactoryBean.setPackagesToScan(AuthBaseModel.class.getPackageName());
		entityManagerFactoryBean.setJpaProperties(properties);
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean.getObject().createEntityManager();

	}
	
	//-----------------------------------------------------------------------------------------
	/**
	 * 配置application.yml中spring.datasource.kube
	 * 
	 * @return DataSource
	 */
	@Bean(name = "kubeDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.kube")
	public DataSource kubeDataSource() {
		return DataSourceBuilder.create().build();
	}


	/**
	 * 创建相关的EntityManager，供SQL查询使用
	 * 
	 * @param dataSource
	 * @param properties
	 * @return
	 */
	@Bean(name = "kubeEntityManager")
	@PersistenceContext(unitName = "kubeEntityManager")
	@DependsOn({ "jpaProperties", "kubeDataSource" })
	public EntityManager kubeEntityManager(@Qualifier("kubeDataSource") DataSource dataSource,
			@Qualifier("jpaProperties") Properties properties) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPersistenceUnitName("kubeEntityManager");
		entityManagerFactoryBean.setPackagesToScan(KubeBaseModel.class.getPackageName());
		entityManagerFactoryBean.setJpaProperties(properties);
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean.getObject().createEntityManager();
	}
	
	//-----------------------------------------------------------------------------------------
	// 我认为这是一个Spring bug，不清楚为啥会创建jpaSharedEM_entityManagerFactory
	@Bean(name = "jpaSharedEM_entityManagerFactory")
	public void springCompatibility() {
		
	}

}
