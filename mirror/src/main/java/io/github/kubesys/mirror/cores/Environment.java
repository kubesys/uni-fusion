/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores;


/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/17
 *
 */
public final class Environment {
	
	private Environment() {
		super();
	}

	/**
	 * JDBC的Url地址
	 */
	public static final String ENV_JDBC_URL            = "jdbcUrl";
	
	/**
	 * JDBC的Username
	 */
	public static final String ENV_JDBC_USER           = "jdbcUser";
	
	/**
	 * JDBC的Password
	 */
	public static final String ENV_JDBC_PWD             = "jdbcPwd";
	
	
	/**
	 * JDBC的Password
	 */
	public static final String ENV_JDBC_POOLSIZE         = "jdbcPoolSize";
	
	/**
	 * JDBC的Driver
	 */
	public static final String ENV_JDBC_DRIVER          = "jdbcDriver";
	
	/**
	 * Kubernetes的Url地址
	 */
	public static final String ENV_KUBE_URL             = "kubeUrl";
	
	/**
	 * Kubernetes的Token
	 */
	public static final String ENV_KUBE_TOKEN           = "kubeToken";
	
	/**
	 * Kubernetes的Token
	 */
	public static final String ENV_KUBE_REGION           = "kubeRegion";
	
	/**
	 * RabbitMQ的Url集群标识
	 */
	public static final String ENV_MQ_URL                = "mqUrl";
	
	/**
	 * RabbitMQ的用户名
	 */
	public static final String ENV_MQ_USER               = "mqUser";

	/**
	 * RabbitMQ的密码
	 */
	public static final String ENV_MQ_PWD                = "mqPwd";
	
	
	/**
	 * RabbitMQ的密码
	 */
	public static final String ENV_MQ_DURATION          = "mqDuration";
}
