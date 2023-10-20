/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.targets;

import java.util.logging.Logger;

import com.rabbitmq.client.Channel;

import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.cores.clients.RabbitMQClient;
import io.github.kubesys.mirror.datas.KubeDataModel;
import io.github.kubesys.mirror.targets.rabbitmq.RabbitMQDataMgr;
import io.github.kubesys.mirror.targets.rabbitmq.RabbitMQQueueMgr;
import io.github.kubesys.mirror.utils.SQLUtil;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.2.0
 * @since    2023/07/21
 *
 */
public class RabbitMQTarget extends DataTarget<KubeDataModel> {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(RabbitMQTarget.class.getName());
	
	
	/**
	 * Queue管理
	 */
	protected final RabbitMQQueueMgr queueMgr;
	
	/**
	 * 数据管理
	 */
	protected final RabbitMQDataMgr dataMgr;
	
	public RabbitMQTarget() {
		super();
		RabbitMQClient mqClient = new RabbitMQClient();
		this.queueMgr = new RabbitMQQueueMgr(mqClient);
		this.dataMgr  = new RabbitMQDataMgr(mqClient);
	}

	@Override
	public synchronized void doHandleAdded(KubeDataModel data) throws Exception {
		forwarding(KubernetesConstants.JSON_TYPE_ADDED, data);
	}

	@Override
	public void doHandleModified(KubeDataModel data) throws Exception {
		forwarding(KubernetesConstants.JSON_TYPE_MODIFIED, data);
	}

	@Override
	public void doHandleDeleted(KubeDataModel data) throws Exception {
		forwarding(KubernetesConstants.JSON_TYPE_DELETED, data);
	}
	
	private void forwarding(String type, KubeDataModel data) {
		String queue = SQLUtil.table(data.getMeta().getFullkind().toLowerCase());
		Channel channel = queueMgr.createQueueIfNeed(queue);
		dataMgr.sendMsg(channel, queue, type, data);
	}
}
