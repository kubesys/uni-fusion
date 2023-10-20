/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores;

import io.github.kubesys.client.annotations.Env;

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
	@Env(description = "Postgres连接地址", 
			defaultValue = "jdbc:postgresql://kube-database:kube-system/kubestack" ,
			example = "如jdbc:postgresql://localhost:5432/mydatabase")
	public static final String ENV_JDBC_URL            = "jdbcUrl";
	
	/**
	 * JDBC的Username
	 */
	@Env(description = "Postgres用户名", 
			defaultValue = "postgres" ,
			example = "如admin")
	public static final String ENV_JDBC_USER           = "jdbcUser";
	
	/**
	 * JDBC的Password
	 */
	@Env(description = "Postgres密码", 
			defaultValue = "onceas" ,
			example = "如admin")
	public static final String ENV_JDBC_PWD             = "jdbcPwd";
	
	
	/**
	 * JDBC的Password
	 */
	@Env(description = "Postgres线程池大小", 
			defaultValue = "10" ,
			example = "如20")
	public static final String ENV_JDBC_POOLSIZE         = "jdbcPoolSize";
	
	/**
	 * JDBC的Driver
	 */
	@Env(description = "Postgres驱动", 
			defaultValue = "org.postgresql.Driver" ,
			example = "如org.postgresql.Driver")
	public static final String ENV_JDBC_DRIVER          = "jdbcDriver";
	
	/**
	 * Kubernetes的Url地址
	 */
	@Env(description = "Kubernetes连接地址", 
			defaultValue = "https://10.96.0.1:443" ,
			example = "如https://127.0.0.1:6443")
	public static final String ENV_KUBE_URL             = "kubeUrl";
	
	/**
	 * Kubernetes的Token
	 */
	@Env(description = "Kubernetes的Bearer Token", 
			defaultValue = "无默认值，查看https://github.com/kubesys/client-java获取token" ,
			example = "如eyJhbGciOiJSUzI1NiIsImtpZCI6IkNhcVFxOHpmSHdRcTBpVFJvd2tacldzNzR2NElERHVzcG01eUM2ZmU0dHcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudC10b2tlbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjYyMjc5ZWFiLTBiZmQtNGU2NC1hYjU3LTA3OGZiODhkMTk4MSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWNsaWVudCJ9.TI9mZC39ixMEq4h3cGfveKqLQSSh2y7BvvqLQixJBrFlKjsu9RJwlcGqEjh32UyshKDLtF4bM1J7w9HTMy7t74uGae9No-4Nm-R4kN3mXJA04MMdWZAV5gipDAuhR1J5R5wdoIIwYNyuUJNavWh61AqtXJkwC3uCAIYnClY9-Kx25Jif-XFlXyRkfETJxA2I9ZAbKZ3g_LOJgmVNfstjxSNTLJgRImYzQ65hrM2oZFul1_rZFPXM76rsNWwObvzPtDKPCT_yaqWt3dzAxxxOuP9EaQodVPSz7YNJb1ZHsGKgqAN9_I8MjQ2wJ0gLahyT4DFaU8rb2OvDhlDV66DoOw")
	public static final String ENV_KUBE_TOKEN           = "kubeToken";
	
	/**
	 * Kubernetes的Token
	 */
	@Env(description = "Kubernetes集群标识", 
			defaultValue = "无默认值，随机生成16位" ,
			example = "如abcdedfg12345678")
	public static final String ENV_KUBE_REGION           = "kubeRegion";
	
	/**
	 * RabbitMQ的Url集群标识
	 */
	@Env(description = "RabbitMQ的Url集群标识", 
			defaultValue = "amqp://kube-message.kube-stack:5672" ,
			example = "如amqp://139.9.165.93:30304")
	public static final String ENV_MQ_URL                = "mqUrl";
	
	/**
	 * RabbitMQ的用户名
	 */
	@Env(description = "RabbitMQ用户名", 
			defaultValue = "guest" ,
			example = "如admin")
	public static final String ENV_MQ_USER               = "mqUser";

	/**
	 * RabbitMQ的密码
	 */
	@Env(description = "RabbitMQ密码", 
			defaultValue = "guest" ,
			example = "如admin")
	public static final String ENV_MQ_PWD                = "mqPwd";
	
	
	/**
	 * RabbitMQ的密码
	 */
	@Env(description = "RabbitMQ队列数据超期删除时间，单位是毫秒", 
			defaultValue = "10000" ,
			example = "100")
	public static final String ENV_MQ_DURATION          = "mqDuration";
}
