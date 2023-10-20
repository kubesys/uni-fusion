/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.sources;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.mirror.cores.DataTarget;
import io.github.kubesys.mirror.datas.KubeDataModel;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/18
 *
 */
public class KubeSourceExtractor extends AbstractKubeSource {

	
	/**
	 * @param dataTarget     dataTarget
	 * @throws Exception     Exception
	 */
	public KubeSourceExtractor(DataTarget<KubeDataModel> dataTarget) throws Exception {
		super(dataTarget);
	}

	@Override
	public void startCollect() throws Exception {
		
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = 
							kubeClient.getKindDesc().fields();
		
		while (fieldsIterator.hasNext()) {
		    Map.Entry<String, JsonNode> entry = fieldsIterator.next();
		    String fullkind = entry.getKey();
		    JsonNode value = entry.getValue();
		    doStartCollect(fullkind, value);
		}
	}
	
}
