/*
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.core.generators.k8s;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.Node;
import io.github.kubestack.core.utils.ClassUtils;

import java.io.File;
import java.io.FileWriter;

/**
 * @author wuheng@iscas.ac.cn
 *
 * @version 2.0.0
 * @since   202/10/18
 *
 */
public class CodeGenerator {


	static String CORE_MODEL = Node.class.getPackage().getName();


	public static void main(String[] args) throws Exception {
		for (Class<?> c : ClassUtils.scan(CORE_MODEL)) {
			if (isModel(c)) {
//				writeModel(c);
//				writeImpl(c);
				writeClient(c);
			}
		}
	}

	//---------------------------------------------------------------------------

	static String CLIENT_TEMP = "	/**\r\n"
			+ "	 * @return        XXX\r\n"
			+ "	 */\r\n"
			+ "	public XXXImpl YYYs() {\r\n"
			+ "		return new XXXImpl(this, XXX.class.getSimpleName());\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	/**\r\n"
			+ "	 * @return        watch YYYs\r\n"
			+ "	 * @throws Exception \r\n"
			+ "	 */\r\n"
			+ "	public void watchXXXs(KubernetesWatcher watcher) throws Exception {\r\n"
			+ "		this.watchResources(XXX.class.getSimpleName(), watcher);\r\n"
			+ "	}\n";

	public static void writeClient(Class<?> c) throws Exception {
		System.out.println(CLIENT_TEMP
				.replaceAll("XXX", c.getSimpleName())
				.replaceAll("YYY", c.getSimpleName().toLowerCase()));
	}

	//---------------------------------------------------------------------------

	static String MODEL_PATH = "src/main/java/io/github/kubestack/client/api/models/k8s";

	static String MODEL_TEMP = "/**\r\n"
			+ " * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences\r\n"
			+ " */\r\n"
			+ "package io.github.kubestack.client.api.models.k8s;\r\n"
			+ "\r\n"
			+ "import io.github.kubestack.client.api.models.KubeStackModel;\r\n"
			+ "\r\n"
			+ "/**\r\n"
			+ " * @author wuheng@iscas.ac.cn\r\n"
			+ " * \r\n"
			+ " * @version 2.0.0\r\n"
			+ " * @since   2022.10.12\r\n"
			+ " * \r\n"
			+ " **/\r\n"
			+ "public class XXX extends KubeStackModel {\r\n"
			+ "\r\n"
			+ "	XXXSPEC\r\n"
			+ "	\r\n"
			+ "	XXXSTATUS\r\n"
			+ "	\r\n"
			+ "}";

	static String MODEL_TEMP_SPEC = "protected io.fabric8.kubernetes.api.model.XXXSpec spec;\r\n"
			+ "\r\n"
			+ "	public io.fabric8.kubernetes.api.model.XXXSpec getSpec() {\r\n"
			+ "		return spec;\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	public void setSpec(io.fabric8.kubernetes.api.model.XXXSpec spec) {\r\n"
			+ "		this.spec = spec;\r\n"
			+ "	}";

	static String MODEL_TEMP_STATUS = "protected io.fabric8.kubernetes.api.model.XXXStatus status;\r\n"
			+ "	\r\n"
			+ "	public io.fabric8.kubernetes.api.model.XXXStatus getStatus() {\r\n"
			+ "		return status;\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	public void setStatus(io.fabric8.kubernetes.api.model.XXXStatus status) {\r\n"
			+ "		this.status = status;\r\n"
			+ "	}";

	public static boolean isModel(Class<?> c) {
		for (Class<?> item : c.getInterfaces()) {
			if (item.getName().equals(HasMetadata.class.getName())) {
				return true;
			}
		}
		return false;
	}

	public static void writeModel(Class<?> c) throws Exception {
		String name = c.getSimpleName();
		File java = new File(MODEL_PATH, name + ".java");
		System.out.println("generating " + java);

		String str = MODEL_TEMP.replace("XXX", name);

		try {
			Class.forName(c.getPackage() + "." + name + "Spec");
			String specStr = MODEL_TEMP_SPEC.replaceAll("XXX", name);
			str = str.replace(name + "SPEC", specStr);
		} catch (Exception ex) {
			str = str.replace(name + "SPEC", "");
		}

		try {
			Class.forName(c.getPackage() + "." + name + "Status");
			String statusStr = MODEL_TEMP_STATUS.replaceAll("XXX", name);
			str = str.replace(name + "STATUS", statusStr);
		} catch (Exception ex) {
			str = str.replace(name + "STATUS", "");
		}

		FileWriter fw = new FileWriter(java);
		fw.write(str);
		fw.close();
	}

	//---------------------------------------------------------------------------
	static String IMPL_PATH = "src/main/java/io/github/kubestack/client/impl/k8s";

	static String IMPL_TEMP = "/**\r\n"
			+ " * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences\r\n"
			+ " */\r\n"
			+ "package io.github.kubestack.client.impl.k8s;\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "import io.github.kubestack.client.KubeStackClient;\r\n"
			+ "import io.github.kubestack.client.impl.AbstractImpl;\r\n"
			+ "\r\n"
			+ "/**\r\n"
			+ " * @author  wuheng@otcaix.iscas.ac.cn\r\n"
			+ " * \r\n"
			+ " * @version 2.0.0\r\n"
			+ " * @since   2022/10/24\r\n"
			+ " **/\r\n"
			+ "public class XXXImpl extends AbstractImpl<YYY, ZZZ> {\r\n"
			+ "\r\n"
			+ "	public XXXImpl(KubeStackClient client, String kind) {\r\n"
			+ "		super(client, kind);\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "	@Override\r\n"
			+ "	protected YYY getModel() {\r\n"
			+ "		return new YYY();\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	@Override\r\n"
			+ "	protected ZZZ getSpec() {\r\n"
			+ "		return AAA;\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	@Override\r\n"
			+ "	protected Object getLifecycle() {\r\n"
			+ "		return null;\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "	@Override\r\n"
			+ "	protected Class<?> getModelClass() {\r\n"
			+ "		return YYY.class;\r\n"
			+ "	}\r\n"
			+ "\r\n"
			+ "}";

	public static void writeImpl(Class<?> c) throws Exception {
		String name = c.getSimpleName();
		File java = new File(IMPL_PATH, name + "Impl.java");
		System.out.println("generating " + java);

		String str = IMPL_TEMP.replace("XXX", name);

		str = str.replaceAll("YYY", "io.github.kubestack.client.api.models.k8s." + name);

		try {
			Class.forName(c.getPackage() + "." + name + "Spec");
			str = str.replaceAll("ZZZ", c.getPackage() + "." + name + "Spec");
			str = str.replaceAll("AAA", "new " + c.getName() + "Spec()");
		} catch (Exception ex) {
			str = str.replaceAll("ZZZ", KubernetesResource.class.getName());
			str = str.replaceAll("AAA", "null");
		}

		FileWriter fw = new FileWriter(java);
		fw.write(str);
		fw.close();
	}

}
