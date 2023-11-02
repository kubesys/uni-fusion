/*
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubestack.core.generators.vms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kubestack.client.api.models.vms.*;
import io.github.kubestack.client.api.specs.KubeStackSpec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @version 1.0.0
 * @since   2019/9/3
 *
 */
public class JSONGenerator {

	public final static List<String> list = new ArrayList<String>();

	static {
		list.add(toPackage(VirtualMachine.class));
		list.add(toPackage(VirtualMachineImage.class));
		list.add(toPackage(VirtualMachineDisk.class));
		list.add(toPackage(VirtualMachineDiskImage.class));
		list.add(toPackage(VirtualMachineDiskSnapshot.class));
		list.add(toPackage(VirtualMachineSnapshot.class));
		list.add(toPackage(VirtualMachinePool.class));
		list.add(toPackage(VirtualMachineNetwork.class));
	}

	static String toPackage(Class<?> clz) {
		return KubeStackSpec.class.getPackage().getName() + ".vms." + clz.getSimpleName().toLowerCase() + ".Lifecycle";
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	protected static void instance(Object obj) throws Exception {
		Class<? extends Object> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getModifiers() == 26) {
				continue;
			}
			String typename = field.getType().getName();
			Method m = clazz.getMethod(
					setMethod(field.getName()),
					field.getType());
			if (typename.equals(String.class.getName())) {
				m.invoke(obj, "String");
			} else if (typename.equals("boolean")
					|| typename.equals(Boolean.class.getName()))  {
				m.invoke(obj, true);
			} else if (typename.equals("int")
					|| typename.equals(Integer.class.getName()))  {
				m.invoke(obj, 1);
			} else if (typename.equals(ArrayList.class.getName())
					|| typename.equals(List.class.getName())
					|| typename.equals(Set.class.getName())) {
				String generictype = field.getGenericType().getTypeName();
				int start = generictype.indexOf("<");
				int end   = generictype.indexOf(">");
				String realtype = generictype.substring(start + 1, end);
				List list = new ArrayList();
				if (realtype.equals(String.class.getName())) {
					list.add("String");
					list.add("String");
				} else if (realtype.equals("boolean")
						|| realtype.equals(Boolean.class.getName()))  {
					list.add(true);
					list.add(true);
				} else if (realtype.equals("int")
						|| realtype.equals(Integer.class.getName()))  {
					list.add(1);
					list.add(1);
				} else {
					Object ins1 = Class.forName(realtype).newInstance();
					list.add(ins1);
					instance(ins1);

					Object ins2 = Class.forName(realtype).newInstance();
					list.add(ins2);
					instance(ins2);
				}
				m.invoke(obj, list);
			} else {
				try {
					if (typename.equals(obj.getClass().getTypeName())) {
						System.out.println("Warning: infinite loop for" + typename);
						continue;
					}

					Object param = Class.forName(typename).newInstance();
					m.invoke(obj, param);
					instance(param);
				} catch (Exception ex) {

				}
			}
		}
	}

	protected static String setMethod(String name) {
		return "set" + name.substring(0, 1).toUpperCase()
							+ name.substring(1);
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		for (String name : list) {
			Object obj = Class.forName(name).newInstance();
			instance(obj);
			System.out.println(new ObjectMapper().writeValueAsString(obj));
		}
	}

}
