/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.cores;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.1.1
 * @since    2023/07/17
 *
 */
public final class MirrorConstants {
	
	private MirrorConstants() {
		super();
	}

	
	/**
	 * APIVERSION
	 */
	public static final String KUBE_APIGROUP           = "apiextensions.k8s.io";
	
	
	/**
	 * CRD
	 */
	public static final String KUBE_RESDEF             = "CustomResourceDefinition";
	
	/**
	 * CA 
	 */
	public static final String KUBE_CA_PATH            = "/root/.kube/config";
	
	/**
	 * 取值为-/
	 */
	public static final String TABLE_REPLACED_SOURCE    = "[-/]";
	
	/**
	 * 取值为
	 */
	public static final String TABLE_REPLACED_TARGET    = "";
	
	/**
	 * 取值为data
	 */
	public static final String JSON_DATABASE_ITEM       = "data";
	
	/**
	 * 取值为##
	 */
	public static final String JSON_INPUT_SPLIT         = "##";
	
	/**
	 * 取值为 ->
	 */
	public static final String JSON_MIDDLE_CONNECTOR    = " -> ";
	
	/**
	 * 取值为 ->>
	 */
	public static final String JSON_LAST_CONNECTOR      = " ->> ";
	
	/**
	 * 取值为'
	 */
	public static final String JSON_SINGLE_QUOTES       = "'";
}
