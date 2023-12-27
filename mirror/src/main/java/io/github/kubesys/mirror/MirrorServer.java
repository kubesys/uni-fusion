/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.datas.KubeDataModel;
import io.github.kubesys.mirror.sources.AbstractKubeSource;
import io.github.kubesys.mirror.sources.KubeSourceExtractor;
import io.github.kubesys.mirror.sources.KubeSourceListener;
import io.github.kubesys.mirror.targets.PostgresTarget;
import io.github.kubesys.mirror.targets.RabbitMQTarget;
import io.github.kubesys.mirror.utils.MirrorUtil;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/17
 *
 * 本项目的主要作用是将Kubernetes的所有状态同步到Postgres数据库中
 * 
 * 具体文档见https://www.yuque.com/kubesys/backend/cbtrr8nshxowbgiy
 */
public class MirrorServer {
	
	/**
	 * @param args      系统参数
	 * @throws Exception  启动的异常
	 */
	public static void main(String[] args) throws Exception {
		
		DataTarget<KubeDataModel> dbTarget    = new PostgresTarget();
		DataTarget<KubeDataModel> msgTarget   = new RabbitMQTarget();
		dbTarget.setNext(msgTarget);
		
		AbstractKubeSource extrator = new KubeSourceExtractor(dbTarget);
		extrator.startCollect();
		
		AbstractKubeSource listener = new KubeSourceListener(dbTarget);
		listener.startCollect();
		
		// 调试的时候用
//		debug(extrator);
	}

	static void debug(AbstractKubeSource source) throws Exception {
		JsonNode json = source.getKubeClient().getKindDesc().get("Pod");
		source.startCollect("Pod", MirrorUtil.toKubeMeta("Pod", json));
//		System.out.println(source.getKubeClient().getKindDesc());
//		JsonNode json = source.getKubeClient().getKindDesc().get("apiextensions.k8s.io.CustomResourceDefinition");
//		source.startCollect("apiextensions.k8s.io.CustomResourceDefinition", MirrorUtil.toKubeMeta("apiextensions.k8s.io.CustomResourceDefinition", json));
	}
}
