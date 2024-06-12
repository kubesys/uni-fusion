/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.sources;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.client.KubernetesWatcher;
import io.github.kubesys.client.exceptions.KubernetesConnectionException;
import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.cores.DataSource;
import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.cores.Environment;
import io.github.kubesys.mirror.cores.MirrorConstants;
import io.github.kubesys.mirror.datas.KubeDataModel;
import io.github.kubesys.mirror.datas.KubeDataModel.Meta;
import io.github.kubesys.mirror.utils.MirrorUtil;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/18
 *
 */
public abstract class AbstractKubeSource extends DataSource<KubeDataModel> {

	protected static final Logger m_logger = Logger.getLogger(AbstractKubeSource.class.getName());
	
	/**
	 * 忽略得Kinds
	 */
	static final Set<String> ignoredFullkinds = new HashSet<>();
	
	/**
	 * fullKind与元数据描述映射关系
	 * fullKind = group + "." + kind
	 */
	static final Map<String, Meta> fullkindToMeta = new HashMap<>();
	
	/**
	 * fullKind与元数据描述映射关系
	 * fullKind = group + "." + kind
	 */
	static final Map<String, Thread> fullkindToWatcher = new HashMap<>();
	
	/**
	 * 默认Kubernetes的URL
	 */
	public static final String DEFAULT_URL = "https://kubernetes.default:443";
	
	/**
	 * Kubernetes的客户端
	 */
	protected final KubernetesClient kubeClient;
	
	
	/**
	 * @param metaTarget          处理器，如创建数据库表
	 * @param dataTarget          处理器，数据处理
	 * @throws Exception 
	 */
	AbstractKubeSource(DataTarget<KubeDataModel> dataTarget) throws Exception {
		super(dataTarget);
		this.kubeClient = initKubeClient();
		// 缺少环境变量，直接异常退出
		MirrorUtil.checkNull(kubeClient);
	}

	/**
	 * 读取环境变量'ENV_KUBE_URL'和'ENV_KUBE_TOKEN'，实例化Kubernetes客户端实例.
	 * 
	 * @return Kubernetes客户端实例
	 * @throws Exception 无法连接Kubernetes
	 */
	public static KubernetesClient initKubeClient() throws Exception {
		
		File file = new File(MirrorConstants.KUBE_CA_PATH);
		if (file.exists()) {
			return new KubernetesClient(file);
		} else {
			try {
				return new KubernetesClient(
					MirrorUtil.getEnv(Environment.ENV_KUBE_URL, DEFAULT_URL),
					System.getenv(Environment.ENV_KUBE_TOKEN));
			} catch (KubernetesConnectionException ex) {
				m_logger.severe(ex.toString());
				System.exit(1);
			}
		}
		
		return null;
	}

	/**
	 * @return Kubernetes的客户端
	 */
	public KubernetesClient getKubeClient() {
		return kubeClient;
	}
	
	
	@Override
	public void startCollect(String fullkind, Meta meta) throws Exception {
		//开始监听数据
		fullkindToMeta.put(fullkind, meta);
	    Thread thread = kubeClient.watchResources(fullkind, 
	    		new KubeCollector(kubeClient,fullkind, this, dataTarget));
	    fullkindToWatcher.put(fullkind, thread);
	    
	}
	
	protected synchronized void doStartCollect(String fullkind, JsonNode value) throws Exception {
		
		// 已经监测过了，不再监测
	    if (fullkindToWatcher.containsKey(fullkind)) {
	    	return;
	    }
	    
	    // 不支持监听就忽略退出
	    if (!KubeUtil.supportWatch(value)) {
	    	ignoredFullkinds.add(fullkind);
	    	return;
	    }
	    
	    // 添加元数据描述信息
		Meta kubeData = MirrorUtil.toKubeMeta(fullkind, value);
		fullkindToMeta.put(fullkind, kubeData);
	    
    	//开始监听数据
		kubeClient.watchResources(fullkind, new KubeCollector(kubeClient, fullkind, this, dataTarget));
	}

	/**
	 * @author wuheng@iscas.ac.cn
	 * @version 0.1.0
	 * @since   2023/06/22
	 *
	 */
	static class KubeCollector extends KubernetesWatcher {
		
		protected final AbstractKubeSource source;
		
		/**
		 * 全称=group+kind
		 */
		protected final String fullKind;
		
		/**
		 * 目标处理器
		 */
		protected final DataTarget<KubeDataModel> dataTarget;
		
		protected KubeCollector(KubernetesClient client, String fullKind, AbstractKubeSource source, DataTarget<KubeDataModel> target) {
			super(client);
			this.fullKind = fullKind;
			this.source = source;
			this.dataTarget = target;
		}


		@Override
		public void doAdded(JsonNode node) {
			try {
				dataTarget.handle(KubernetesConstants.JSON_TYPE_ADDED, 
						new KubeDataModel(fullkindToMeta.get(fullKind), node));
				
				if (MirrorConstants.KUBE_APIGROUP.equals(KubeUtil.getGroup(node)) &&
						MirrorConstants.KUBE_RESDEF.equals(KubeUtil.getKind(node))) {
					
					String fullkind = KubeUtil.getCRDFullkind(node);
					Meta meta = MirrorUtil.toKubeMeta(node);
					
					source.startCollect(fullkind, meta);
				}
				
			} catch (Exception e) {
				m_logger.warning("unknown error: " + e + ":" + node.toPrettyString());
			}
		}

		@Override
		public void doModified(JsonNode node) {
			try {
				dataTarget.handle(KubernetesConstants.JSON_TYPE_MODIFIED, 
						new KubeDataModel(fullkindToMeta.get(fullKind), node));
			} catch (Exception e) {
				m_logger.warning("unknown error: " + e  + ":" + node.toPrettyString());
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public void doDeleted(JsonNode node) {
			try {
				dataTarget.handle(KubernetesConstants.JSON_TYPE_DELETED, 
						new KubeDataModel(fullkindToMeta.get(fullKind), node));
				
				if (MirrorConstants.KUBE_APIGROUP.equals(KubeUtil.getGroup(node)) &&
						MirrorConstants.KUBE_RESDEF.equals(KubeUtil.getKind(node))) {
					
					String fullkind = KubeUtil.getCRDFullkind(node);
					
					//开始停止监听
					Thread thread = fullkindToWatcher.remove(fullkind);
					thread.stop();
					fullkindToMeta.remove(fullkind);
				}
				
			} catch (Exception e) {
				m_logger.warning("unknown error: " + e  + ":" + node.toPrettyString());
			}
		}

		@Override
		public void doClose() {
			m_logger.severe("connection is close, wait for reconnect " + fullKind);
			try {
				Thread.sleep(3000);
				 client.watchResources(fullKind, new KubeCollector(
						 	client, fullKind, source, dataTarget));
			} catch (Exception e) {
				doClose();
			}
		}
	}

}
