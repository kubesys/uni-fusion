/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores;

import io.github.kubesys.client.KubernetesConstants;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/17
 *
 * 本项目的主要作用是确定谁如何处理数据
 */
public abstract class DataTarget<T> {
	
	/**
	 * 元描述信息，可以为空
	 * 比如对于Postgres，meta表示创建表
	 * 对于RabbitMQ，meta表示创建队列
	 */
	protected DataTarget<T> meta;
	
	/**
	 * 下一个目标处理器
	 */
	protected DataTarget<T> next;
	
	
	/**
	 * 设置下一个目标处理器
	 * 
	 * @param next        下一个目标处理器
	 */
	public void setNext(DataTarget<T> next) {
		this.next = next;
	}
	
	
	/**
	 * 具体处理数据的逻辑
	 * 
	 * @param type          处理类型
	 * @param data          收集到的数据
	 * @throws Exception    数据处理中产生的错误
	 */
	public void handle(String type, T data) throws Exception {
		
		if (KubernetesConstants.JSON_TYPE_ADDED.equals(type)) {
			doHandleAdded(data);
		} else if (KubernetesConstants.JSON_TYPE_MODIFIED.equals(type)) {
			doHandleModified(data);
		} else if (KubernetesConstants.JSON_TYPE_DELETED.equals(type)) {
			doHandleDeleted(data);
		} else {
			throw new IllegalArgumentException("不支持的类型" + type + ",只支持ADDED, MODIFIED和DELETED");
		}
		
		if (next != null) {
            next.handle(type, data);
        }
    }

	/**
	 * @param data          收集到的数据  
	 * @throws Exception    数据处理中产生的错误
	 */
	public abstract void doHandleAdded(T data) throws Exception;
	
	
	/**
	 * @param data          收集到的数据  
	 * @throws Exception    数据处理中产生的错误
	 */
	public abstract void doHandleModified(T data) throws Exception;
	
	/**
	 * @param data          收集到的数据  
	 * @throws Exception    数据处理中产生的错误
	 */
	public abstract void doHandleDeleted(T data) throws Exception;
}
