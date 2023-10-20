- all-routes.json路由语法

  ```
  {
  	"apiVersion": "doslab.io/v1",
  	"kind": "Frontend",
  	"metadata": {
  		"name": "all-routes"
  	},
  	"spec": {
  		"catalogs": [{
  				"name": "环境信息",
  				"path": "/envInfo"
  			},
  			{
  				"name": "应用管理",
  				"path": "/appMgr"
  			}
  		],
  		"items": [{
  				"menuIcon": "el-icon-Monitor",
  				"kind": "平台信息",
  				"isShow": 1,
  				"paths": "/envInfo/basicInfo",
  				"menuType": "M",
  				"children": [{
  						"menuIcon": "el-icon-Monitor",
  						"kind": "路测节点",
  						"isShow": 1,
  						"component": "article/lists/index",
  						"paths": "node",
  						"menuType": "C",
  						"listname": "Node",
  						"tablename": "node"
  					},
  					{
  						"menuIcon": "el-icon-Monitor",
  						"kind": "系统概述",
  						"isShow": 1,
  						"paths": "index",
  						"menuType": "C",
  						"listname": "Node",
  						"tablename": "node"
  					},
  					{
  						"menuIcon": "el-icon-Monitor",
  						"kind": "云盘",
  						"isShow": 1,
  						"component": "article/lists/index",
  						"paths": "replicaset",
  						"menuType": "C",
  						"listname": "apps.ReplicaSet",
  						"tablename": "apps.replicaset"
  					}
  				]
  			},
  			{
  				"menuIcon": "el-icon-CollectionTag",
  				"kind": "Pod",
  				"isShow": 1,
  				"component": "article/lists/index",
  				"paths": "column",
  				"menuType": "C",
  				"listname": "Pod",
  				"tablename": "pod"
  			},
  			{
  				"menuIcon": "el-icon-Monitor",
  				"kind": "工作负载",
  				"isShow": 1,
  				"paths": "/appMgr/workload",
  				"menuType": "M",
  				"children": [{
  					"menuIcon": "el-icon-Monitor",
  					"kind": "Pod",
  					"isShow": 1,
  					"component": "article/lists/index",
  					"paths": "pod",
  					"menuType": "C",
  					"listname": "Pod",
  					"tablename": "pod"
  				}]
  			}
  		]
  	}
  }
  
  ```

  

- all-regions.json区域语法

  ```
  {
  	"apiVersion": "doslab.io/v1",
  	"kind": "Frontend",
  	"metadata": {
  		"name": "all-regions"
  	},
  	"spec": {
  		"data": [{
  			"size": 16,
  			"name": "el-icon-location-information",
  			"area": "ZONE-1",
  			"value": "test"
  		}, {
  			"size": 16,
  			"name": "el-icon-location-information",
  			"area": "ZONE-2",
  			"value": "test1"
  		}]
  	}
  }
  ```

  

- [kind]-[partition].json页面布局语法

  - 以Pod页面布局为例

    - pod-desc.json kind描述

      ```
      {
      	"metadata": {
      		"name": "pod-desc"
      	},
      	"apiVersion": "doslab.io/v1",
      	"kind": "Frontend",
      	"spec": {
      		"title": "Pod",
      		"desc": "Pod是Kubernetes原生概念，是容器的基本运行单元。",
      		"type": "description"
      	}
      }
      ```

    - pod-formsearch.json kind对应表单项

      ```
      {
      	"metadata": {
      		"name": "pod-formsearch"
      	},
      	"apiVersion": "doslab.io/v1",
      	"kind": "Frontend",
      	"spec": {
      		"data": {
      			"items": [{
      				"label": "资源名称:",
      				"path": "metadata##name",
      				"type": "textbox"
      			}]
      		},
      		"type": "formsearch"
      	}
      }
      ```

    - pod-table.json kind对应表格

      ```
      {
      	"apiVersion": "doslab.io/v1",
      	"kind": "Frontend",
      	"metadata": {
      		"name": "pod-table"
      	},
      	"spec": {
      		"data": [{
      				"label": "资源名",
      				"row": "metadata.name"
      			},
      			{
      				"label": "IP地址ַ",
      				"row": "status.podIP"
      			},
      			{
      				"kind": "internalLink",
      				"label": "命名空间",
      				"link": "Namespace",
      				"row": "metadata.namespace",
      				"tag": "metadata##name"
      			},
      			{
      				"kind": "internalLink",
      				"label": "父类资源名",
      				"link": "@metadata.ownerReferences[0].apiVersion;.;metadata.ownerReferences[0].kind",
      				"row": "metadata.ownerReferences[0].name",
      				"tag": "metadata##name"
      			},
      			{
      				"label": "所在主机",
      				"kind": "internalLink",
      				"link": "Node",
      				"tag": "metadata##name",
      				"row": "spec.nodeName"
      			},
      			{
      				"label": "创建时间",
      				"row": "metadata.creationTimestamp"
      			},
      			{
      				"kind": "externalLink",
      				"label": "运行状态",
      				"row": "status.phase",
      				"link": "http://39.100.91.95:31002/d/AejWkmInz/analysis-by-pod?orgId=1&refresh=10s&var-namespace={1}&var-pod={2}&kiosk",
      				"tag": "metadata.namespace,metadata.name"
      			},
      			{
      				"kind": "action",
      				"label": "更多操作"
      			}
      		],
      		"type": "table"
      	}
      }
      ```

    - pod-action.yaml kind对应表格操作项

      ```
      {
      	"metadata": {
      		"name": "pod-action"
      	},
      	"apiVersion": "doslab.io/v1",
      	"kind": "Frontend",
      	"spec": {
      		"data": [{
      			"name": "更新pod",
      			"type": "Update",
      			"json": "pod-action-scale"
      		}, {
      			"name": "删除pod",
      			"type": "Delete",
      			"params": ["metdata##name", "metdata##namespace"]
      		}]
      	}
      }
      ```

    - pod-action-scale.yaml kind操作对话框

      ```
      {
      	"metadata": {
      		"name": "pod-action-scale"
      	},
      	"apiVersion": "doslab.io/v1",
      	"kind": "Frontend",
      	"spec": {
      		"data": {
      			"group1": {
      				"spec.replicas": {
      					"value": "资源名称",
      					"required": "false",
      					"type": "text",
      					"regexp": "1<x<10"
      				},
      				"spec.introduce": {
      					"value": "命名空间",
      					"required": "false",
      					"type": "text"
      				}
      			}
      		}
      	}
      }
      ```