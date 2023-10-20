/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kubesys.client.utils.KubeUtil;
import io.github.kubesys.mirror.datas.KubeDataModel.Meta;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.1.1
 * @since    2023/07/17
 *
 */
class KubeUtilTest {

	final static String meta_json = "{\r\n"
			+ "	\"apiVersion\": \"v1\",\r\n"
			+ "	\"kind\": \"Pod\",\r\n"
			+ "	\"plural\": \"pods\",\r\n"
			+ "	\"verbs\": [\r\n"
			+ "		\"create\",\r\n"
			+ "		\"delete\",\r\n"
			+ "		\"deletecollection\",\r\n"
			+ "		\"get\",\r\n"
			+ "		\"list\",\r\n"
			+ "		\"patch\",\r\n"
			+ "		\"update\",\r\n"
			+ "		\"watch\"\r\n"
			+ "	]\r\n"
			+ "}";
	
	final static String pod_json = "{\r\n"
			+ "    \"apiVersion\": \"v1\",\r\n"
			+ "    \"kind\": \"Pod\",\r\n"
			+ "    \"metadata\": {\r\n"
			+ "        \"annotations\": {\r\n"
			+ "            \"kubectl.kubernetes.io/last-applied-configuration\": \"{\\\"apiVersion\\\":\\\"v1\\\",\\\"kind\\\":\\\"Pod\\\",\\\"metadata\\\":{\\\"annotations\\\":{},\\\"labels\\\":{\\\"app\\\":\\\"busybox\\\"},\\\"name\\\":\\\"busybox-pod\\\",\\\"namespace\\\":\\\"default\\\"},\\\"spec\\\":{\\\"containers\\\":[{\\\"command\\\":[\\\"sleep\\\",\\\"3600\\\"],\\\"image\\\":\\\"busybox\\\",\\\"name\\\":\\\"busybox-container\\\",\\\"resources\\\":{\\\"limits\\\":{\\\"cpu\\\":\\\"500m\\\",\\\"memory\\\":\\\"256Mi\\\"}}}]}}\\n\",\r\n"
			+ "            \"ovn.kubernetes.io/allocated\": \"true\",\r\n"
			+ "            \"ovn.kubernetes.io/cidr\": \"10.16.0.0/16\",\r\n"
			+ "            \"ovn.kubernetes.io/gateway\": \"10.16.0.1\",\r\n"
			+ "            \"ovn.kubernetes.io/ip_address\": \"10.16.0.52\",\r\n"
			+ "            \"ovn.kubernetes.io/logical_router\": \"ovn-cluster\",\r\n"
			+ "            \"ovn.kubernetes.io/logical_switch\": \"ovn-default\",\r\n"
			+ "            \"ovn.kubernetes.io/mac_address\": \"00:00:00:2E:11:4F\",\r\n"
			+ "            \"ovn.kubernetes.io/pod_nic_type\": \"veth-pair\",\r\n"
			+ "            \"ovn.kubernetes.io/routed\": \"true\"\r\n"
			+ "        },\r\n"
			+ "        \"creationTimestamp\": \"2023-07-17T13:43:46Z\",\r\n"
			+ "        \"labels\": {\r\n"
			+ "            \"app\": \"busybox\"\r\n"
			+ "        },\r\n"
			+ "        \"name\": \"busybox-pod\",\r\n"
			+ "        \"namespace\": \"default\",\r\n"
			+ "        \"resourceVersion\": \"5894098\",\r\n"
			+ "        \"uid\": \"093cf042-5369-4a50-a034-4f3d0e1bc9aa\"\r\n"
			+ "    },\r\n"
			+ "    \"spec\": {\r\n"
			+ "        \"containers\": [\r\n"
			+ "            {\r\n"
			+ "                \"command\": [\r\n"
			+ "                    \"sleep\",\r\n"
			+ "                    \"3600\"\r\n"
			+ "                ],\r\n"
			+ "                \"image\": \"busybox\",\r\n"
			+ "                \"imagePullPolicy\": \"Always\",\r\n"
			+ "                \"name\": \"busybox-container\",\r\n"
			+ "                \"resources\": {\r\n"
			+ "                    \"limits\": {\r\n"
			+ "                        \"cpu\": \"500m\",\r\n"
			+ "                        \"memory\": \"256Mi\"\r\n"
			+ "                    },\r\n"
			+ "                    \"requests\": {\r\n"
			+ "                        \"cpu\": \"500m\",\r\n"
			+ "                        \"memory\": \"256Mi\"\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                \"terminationMessagePath\": \"/dev/termination-log\",\r\n"
			+ "                \"terminationMessagePolicy\": \"File\",\r\n"
			+ "                \"volumeMounts\": [\r\n"
			+ "                    {\r\n"
			+ "                        \"mountPath\": \"/var/run/secrets/kubernetes.io/serviceaccount\",\r\n"
			+ "                        \"name\": \"kube-api-access-tthpt\",\r\n"
			+ "                        \"readOnly\": true\r\n"
			+ "                    }\r\n"
			+ "                ]\r\n"
			+ "            }\r\n"
			+ "        ],\r\n"
			+ "        \"dnsPolicy\": \"ClusterFirst\",\r\n"
			+ "        \"enableServiceLinks\": true,\r\n"
			+ "        \"nodeName\": \"ecs-2503\",\r\n"
			+ "        \"preemptionPolicy\": \"PreemptLowerPriority\",\r\n"
			+ "        \"priority\": 0,\r\n"
			+ "        \"restartPolicy\": \"Always\",\r\n"
			+ "        \"schedulerName\": \"default-scheduler\",\r\n"
			+ "        \"securityContext\": {},\r\n"
			+ "        \"serviceAccount\": \"default\",\r\n"
			+ "        \"serviceAccountName\": \"default\",\r\n"
			+ "        \"terminationGracePeriodSeconds\": 30,\r\n"
			+ "        \"tolerations\": [\r\n"
			+ "            {\r\n"
			+ "                \"effect\": \"NoExecute\",\r\n"
			+ "                \"key\": \"node.kubernetes.io/not-ready\",\r\n"
			+ "                \"operator\": \"Exists\",\r\n"
			+ "                \"tolerationSeconds\": 300\r\n"
			+ "            },\r\n"
			+ "            {\r\n"
			+ "                \"effect\": \"NoExecute\",\r\n"
			+ "                \"key\": \"node.kubernetes.io/unreachable\",\r\n"
			+ "                \"operator\": \"Exists\",\r\n"
			+ "                \"tolerationSeconds\": 300\r\n"
			+ "            }\r\n"
			+ "        ],\r\n"
			+ "        \"volumes\": [\r\n"
			+ "            {\r\n"
			+ "                \"name\": \"kube-api-access-tthpt\",\r\n"
			+ "                \"projected\": {\r\n"
			+ "                    \"defaultMode\": 420,\r\n"
			+ "                    \"sources\": [\r\n"
			+ "                        {\r\n"
			+ "                            \"serviceAccountToken\": {\r\n"
			+ "                                \"expirationSeconds\": 3607,\r\n"
			+ "                                \"path\": \"token\"\r\n"
			+ "                            }\r\n"
			+ "                        },\r\n"
			+ "                        {\r\n"
			+ "                            \"configMap\": {\r\n"
			+ "                                \"items\": [\r\n"
			+ "                                    {\r\n"
			+ "                                        \"key\": \"ca.crt\",\r\n"
			+ "                                        \"path\": \"ca.crt\"\r\n"
			+ "                                    }\r\n"
			+ "                                ],\r\n"
			+ "                                \"name\": \"kube-root-ca.crt\"\r\n"
			+ "                            }\r\n"
			+ "                        },\r\n"
			+ "                        {\r\n"
			+ "                            \"downwardAPI\": {\r\n"
			+ "                                \"items\": [\r\n"
			+ "                                    {\r\n"
			+ "                                        \"fieldRef\": {\r\n"
			+ "                                            \"apiVersion\": \"v1\",\r\n"
			+ "                                            \"fieldPath\": \"metadata.namespace\"\r\n"
			+ "                                        },\r\n"
			+ "                                        \"path\": \"namespace\"\r\n"
			+ "                                    }\r\n"
			+ "                                ]\r\n"
			+ "                            }\r\n"
			+ "                        }\r\n"
			+ "                    ]\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        ]\r\n"
			+ "    },\r\n"
			+ "    \"status\": {\r\n"
			+ "        \"conditions\": [\r\n"
			+ "            {\r\n"
			+ "                \"lastProbeTime\": null,\r\n"
			+ "                \"lastTransitionTime\": \"2023-07-17T13:43:46Z\",\r\n"
			+ "                \"status\": \"True\",\r\n"
			+ "                \"type\": \"Initialized\"\r\n"
			+ "            },\r\n"
			+ "            {\r\n"
			+ "                \"lastProbeTime\": null,\r\n"
			+ "                \"lastTransitionTime\": \"2023-07-17T13:43:49Z\",\r\n"
			+ "                \"status\": \"True\",\r\n"
			+ "                \"type\": \"Ready\"\r\n"
			+ "            },\r\n"
			+ "            {\r\n"
			+ "                \"lastProbeTime\": null,\r\n"
			+ "                \"lastTransitionTime\": \"2023-07-17T13:43:49Z\",\r\n"
			+ "                \"status\": \"True\",\r\n"
			+ "                \"type\": \"ContainersReady\"\r\n"
			+ "            },\r\n"
			+ "            {\r\n"
			+ "                \"lastProbeTime\": null,\r\n"
			+ "                \"lastTransitionTime\": \"2023-07-17T13:43:46Z\",\r\n"
			+ "                \"status\": \"True\",\r\n"
			+ "                \"type\": \"PodScheduled\"\r\n"
			+ "            }\r\n"
			+ "        ],\r\n"
			+ "        \"containerStatuses\": [\r\n"
			+ "            {\r\n"
			+ "                \"containerID\": \"containerd://32e98fd6ae532f997b9cadb0250566ed4a6dcd0690397a8576efdc5fccd1c6cf\",\r\n"
			+ "                \"image\": \"docker.io/library/busybox:latest\",\r\n"
			+ "                \"imageID\": \"docker.io/library/busybox@sha256:2376a0c12759aa1214ba83e771ff252c7b1663216b192fbe5e0fb364e952f85c\",\r\n"
			+ "                \"lastState\": {},\r\n"
			+ "                \"name\": \"busybox-container\",\r\n"
			+ "                \"ready\": true,\r\n"
			+ "                \"restartCount\": 0,\r\n"
			+ "                \"started\": true,\r\n"
			+ "                \"state\": {\r\n"
			+ "                    \"running\": {\r\n"
			+ "                        \"startedAt\": \"2023-07-17T13:43:48Z\"\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        ],\r\n"
			+ "        \"hostIP\": \"192.168.0.189\",\r\n"
			+ "        \"phase\": \"Running\",\r\n"
			+ "        \"podIP\": \"10.16.0.52\",\r\n"
			+ "        \"podIPs\": [\r\n"
			+ "            {\r\n"
			+ "                \"ip\": \"10.16.0.52\"\r\n"
			+ "            }\r\n"
			+ "        ],\r\n"
			+ "        \"qosClass\": \"Guaranteed\",\r\n"
			+ "        \"startTime\": \"2023-07-17T13:43:46Z\"\r\n"
			+ "    }\r\n"
			+ "}";
	
	@Test
	void testToKubeMeta() throws Exception {
		Meta meta = new Meta();
		meta.setGroup("");
		meta.setKind("Pod");
		meta.setFullkind("Pod");
		meta.setPlural("pods");
		assertEquals(meta.getFullkind(), MirrorUtil.toKubeMeta("Pod", 
				new ObjectMapper().readTree(meta_json)).getFullkind());
	}
	
	@Test
	void testGroup1() {
		assertEquals("", KubeUtil.toGroup("v1"));
	}
	
	@Test
	void testGroup2() {
		assertEquals("doslab.io", KubeUtil.toGroup("doslab.io/v1"));
	}
	
	@Test
	void testName() throws Exception {
		assertEquals("busybox-pod", KubeUtil.getName(new ObjectMapper().readTree(pod_json)));
	}
	
	@Test
	void testNamespace() throws Exception {
		assertEquals("default", KubeUtil.getNamespace(new ObjectMapper().readTree(pod_json)));
	}
	
	@Test
	void testGetGroupFromJson() throws Exception {
		assertEquals("", KubeUtil.getGroup(new ObjectMapper().readTree(pod_json)));
	}
	
	@Test
	void testGetGroupFromFullkind1() {
		assertEquals("", KubeUtil.getGroup("Pod"));
	}
	
	@Test
	void testGetGroupFromFullkind2() {
		assertEquals("doslab.io", KubeUtil.getGroup("doslab.io.VirtualMachine"));
	}

}
