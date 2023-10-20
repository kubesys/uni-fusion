/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.targets;

import java.util.logging.Logger;

import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.cores.clients.PostgresClient;
import io.github.kubesys.mirror.datas.KubeDataModel;
import io.github.kubesys.mirror.targets.postgres.PostgresDataMgr;
import io.github.kubesys.mirror.targets.postgres.PostgresTableMgr;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/21
 *
 */
public class PostgresTarget extends DataTarget<KubeDataModel> {

	/**
	 * 日志
	 */
	static final Logger m_logger = Logger.getLogger(PostgresTarget.class.getName());
	
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
	 * Table管理
	 */
	protected final PostgresTableMgr tableMgr;
	
	/**
	 * 数据管理
	 */
	protected final PostgresDataMgr dataMgr;
	
	public PostgresTarget() {
		super();
		PostgresClient pgClient = new PostgresClient();
		this.tableMgr = new PostgresTableMgr(pgClient);
		this.dataMgr  = new PostgresDataMgr(pgClient);
	}

	@Override
	public synchronized void doHandleAdded(KubeDataModel data) throws Exception {
		tableMgr.createTableIfNeed(data);
		dataMgr.saveData(data);
	}

	@Override
	public void doHandleModified(KubeDataModel data) throws Exception {
		dataMgr.updateData(data);
	}

	@Override
	public void doHandleDeleted(KubeDataModel data) throws Exception {
		dataMgr.deleteData(data);
	}
}
