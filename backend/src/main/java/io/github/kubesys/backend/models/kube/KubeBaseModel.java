/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models.kube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.MappedSuperclass;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  1.2.0
 * @since    2023/07/04
 * 
 * backend必须依赖https://github.com/kubesys/mirror项目预先启动
 * mirror同步Kubernetes的所有数据，其中name为数据表名（kubectl api-resources）
 * 
 * 这个类的目的主要就是注册一个EntityManager，见ApplicationServer的@Bean(name = "kubeEntityManager")
 */
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public final class KubeBaseModel   {


}
