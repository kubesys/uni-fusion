/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.targets;

import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.datas.KubeDataModel;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/18
 *
 */
public class PrintTarget extends DataTarget<KubeDataModel> {

	@Override
	public void doHandleAdded(KubeDataModel data) throws Exception {
		System.out.println("ADDED: " + data.getData().toPrettyString());
	}

	@Override
	public void doHandleModified(KubeDataModel data) throws Exception {
		System.out.println("MODIFIED: " + data.getData().toPrettyString());
	}

	@Override
	public void doHandleDeleted(KubeDataModel data) throws Exception {
		System.out.println("DELETED: " + data.getData().toPrettyString());
	}

}
