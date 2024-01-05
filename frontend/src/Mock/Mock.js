import Mock from 'mockjs';

Mock.mock('http://localhost:5173/allMenu', {
    code:200,
    data:{
        gutter:20,
        rows:[
            {
                index: 10,
                title: "资源中心",
                items:[
                    {index: 2,type:"span1",span: 6,name: "云主机", src: "docker.png", classify:["云主机", "云盘", "镜像", "计算规格", "云盘规格","快照"]},
                    {index: 2,type:"span2",span: 6,name: "硬件设施", src: "docker.png", classify:["区域", "集群","物理机", "主存储", "镜像服务器"]},
                    {index: 2,type:"span2",span: 6,name: "网络资源", src: "docker.png", classify:["二层网络","VXLAN Pool", "公有网络", "扁平网络", "VPC网络","VPC路由器","路由器镜像", "路由器规格"]},
                    {index: 2,type:"span2",span: 6,name: "网络服务", src: "docker.png", classify:["安全组", "虚拟IP", "弹性IP","端口转发","负载均衡"]},
                ]
            },
            {
                index: 10,
                title: "平台运维",
                items: [
                    {index: 2,type:"span1",span: 6,name: "消息日志", src: "docker.png", classify:["操作日志"]}
                ]
            },
            {
                index: 10,
                title: "设置",
                items: [
                    {index: 2,type:"span1",span: 6,name: "设置", src: "docker.png", classify:["全局设置"]}
                ]
            }
        ]
    }
});

Mock.mock('http://localhost:5173/cloudHostings',{
    code:200,
    data:{
        rows:[
            {
                index:10,
                title: "虚拟机",
                detail: "运行在物理机上的虚拟机实例，具有独立的IP地址，可以访问公共网络，运行应用服务",

                items:[
                    {index:1,
                     label:"可用资源",
                     name:"first",
                     buttonName:"创建云主机",
                     tableheaderItems:[
                         {prop:"console", label:"控制台", width:120}, {prop:"startStatus", label:"启用状态", width:150}, {prop:"CPU", label:"CPU", width:100}, {prop:"memory", label:"内存", width:100}, {prop:"Ipv4", label:"Ipv4地址", width:200}, {prop:"CPUFrame", label:"CPU架构", width:100}, {prop:"platform", label:"平台", width:100}, {prop:"owner", label:"拥有者", width:200}, {prop:"date", label:"创建时间", width:200}
                     ],
                     tableItems:[
                         {name:"ubuntu-3",  console:"□", startStatus:"🟢启动", CPU:"4", memory:"8GB" , Ipv4:"172.20.100.193", CPUFrame:"x86_64", platform:"🐧Linux", owner:"admin", date:"2022-09-23 15:11:49"},
                        {name:"ubuntu-2",  console:"□", startStatus:"🔴停止", CPU:"4", memory:"8GB" , Ipv4:"172.20.100.193", CPUFrame:"x86_64", platform:"🐧Linux", owner:"admin", date:"2022-09-23 15:11:49"},
                        {name:"ubuntu-1",  console:"□", startStatus:"🔴停止", CPU:"4", memory:"8GB" , Ipv4:"172.20.100.145", CPUFrame:"x86_64", platform:"🐧Linux", owner:"admin", date:"2022-09-23 14:27:23"}
                     ]
                    },
                    {label: "回收站",
                     name:"second",
                     tableheaderItems:[{prop:"startStatus", label:"启用状态", width:150},{prop:"CPU", label:"CPU", width:100},{prop:"memory", label:"内存", width:100}, {prop:"Ipv4", label:"Ipv4地址", width:200}, {prop:"CPUFrame", label:"CPU架构", width:100}, {prop:"platform", label:"平台", width:100}, {prop:"owner", label:"拥有者", width:200}, {prop:"date", label:"创建时间", width:200}]}
                ]
            }
        ]
    }
});

Mock.mock('http://localhost:5173/Hostings',{
    code:200,
    data:{
        gutter: 20,
        rows:[
            {
                index:10,
                title: "镜像",
                detail: "云主机或云盘使用的镜像模板文件，包括两种类型：系统镜像、云盘镜像。",

                items:[
                    {   index:1,
                        "label": "可用资源",
                        name:"first",
                        buttonName:"添加镜像",
                        tableheaderItems:[{prop:"startStatus", label:"启用状态", width:150},{prop:"CPU", label:"CPU", width:100},{prop:"memory", label:"内存", width:100}, {prop:"Ipv4", label:"Ipv4地址", width:200}, {prop:"CPUFrame", label:"CPU架构", width:100}, {prop:"platform", label:"平台", width:100}, {prop:"owner", label:"拥有者", width:200}, {prop:"date", label:"创建时间", width:200}]}
                ]
            }
        ]
    }
});

Mock.mock('http://localhost:5173/CloudStorage',{
    code:200,
    data:{
        gutter: 20,
        rows:[
            {
                index:10,
                title: "云盘",
                detail: "云主机的数据云盘，用于云主机扩展的存储使用。",

                items:[
                    {   index:1,
                        "label": "规格1",
                        name:"first",
                        buttonName:"创建云盘",
                        tableheaderItems:[{prop:"startStatus", label:"真实容量", width:150},{prop:"CPU", label:"容量", width:100},{prop:"memory", label:"主存储", width:100}, {prop:"Ipv4", label:"启用状态", width:200}, {prop:"CPUFrame", label:"就绪状态", width:100}, {prop:"platform", label:"实例", width:100}, {prop:"owner", label:"所有者", width:200}, {prop:"date", label:"创建时间", width:200}]},
                    {label: "规格2",
                        name:"second",
                        tableheaderItems:[{prop:"startStatus", label:"启用状态", width:150},{prop:"CPU", label:"CPU", width:100},{prop:"memory", label:"内存", width:100}, {prop:"Ipv4", label:"Ipv4地址", width:200}, {prop:"CPUFrame", label:"CPU架构", width:100}, {prop:"platform", label:"平台", width:100}, {prop:"owner", label:"拥有者", width:200}, {prop:"date", label:"创建时间", width:200}]}
                ]
            }
        ]
    }
});

Mock.mock('http://localhost:5173/regions',{
    code:200,
    data:[{
        "size": 16,
        "name": "el-icon-location-information",
        "area": "ZONE-1"
    },{
        "size": 16,
        "name": "el-icon-location-information",
        "area": "ZONE-2"
    }]
});

Mock.mock('http://localhost:5173/common/index/config',{
    code: 200,
    msg: "成功",
    data: {
        "webName": "云计算平台",
        "webLogo": "src/assets/images/logo.png",
        "webFavicon": "",
        "webBackdrop": "",
        "ossDomain": "http://127.0.0.1:8082/",
        "copyright": [
            {
                "name": "云计算平台",
                "link": "http://www.beian.gov.cn"
            }
        ]
    }
});

Mock.mock('http://localhost:5173/kubesys/kube/route',{
    code: 20000,
    msg: "成功",
    data: {
        "apiVersion": "doslab.io/v1",
        "kind": "Frontend",
        "metadata": {
            "name": "all-routes-test"
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
            "groups": [{
                "name": "平台信息",
                "path": "/envInfo/basicInfo"
            }
            ],
            "items": [
                {
                    "component": "article/lists/index",
                    "name": "Node",
                    "kind": "Node",
                    "path": "/envInfo/basicInfo/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "Pod",
                    "kind": "Pod",
                    "path": "/envInfo/basicInfo/pod"
                },
                {
                    "component": "article/lists/index",
                    "name": "Namespace",
                    "kind": "Namespace",
                    "path": "/envInfo/basicInfo/namespace"
                },
                {
                    "component": "article/lists/index",
                    "name": "Deployment",
                    "kind": "apps.Deployment",
                    "path": "/envInfo/basicInfo/deployment"
                },
                {
                    "component": "article/lists/index",
                    "name": "Frontend",
                    "kind": "doslab.io.Frontend",
                    "path": "/envInfo/basicInfo/frontend"
                },
                {
                    "component": "article/lists/index",
                    "name": "云主机",
                    "kind": "doslab.io.VirtualMachine",
                    "path": "/envInfo/basicInfo/vm"
                },
                {
                    "component": "article/lists/index",
                    "name": "VirtualMachinePool",
                    "kind": "doslab.io.VirtualMachinePool",
                    "path": "/envInfo/basicInfo/vmpool"
                },
                {
                    "component": "article/lists/index",
                    "name": "云盘",
                    "kind": "doslab.io.VirtualMachineDisk",
                    "path": "/envInfo/basicInfo/disk"
                },
                {
                    "component": "article/lists/index",
                    "name": "VirtualMachineImages",
                    "kind": "doslab.io.VirtualMachineImages",
                    "path": "/envInfo/basicInfo/vmimage"
                },
                {
                    "component": "article/dashboard/index",
                    "source": "http://133.133.137.74:5173/",
                    "name": "远程测试",
                    "path": "/envInfo/basicInfo/index"
                }
            ]
        }
    }
})

Mock.mock('http://localhost:5173/kubesys/kube/route-test',{
    code: 20000,
    msg: "成功",
    data: {
        "apiVersion": "doslab.io/v1",
        "kind": "Frontend",
        "metadata": {
            "name": "all-routes-test"
        },
        "spec": {
            "catalogs": [{
                "name": "资源中心",
                "path": "/envInfo"
            },
                {
                    "name": "平台运维",
                    "path": "/appMgr"
                },
                {
                    "name": "设置",
                    "path": "/config"
                }
            ],
            "groups": [{
                "name": "云资源池",
                "path": "/envInfo/basicInfo"
            },{
                "name": "硬件设施",
                "path": "/envInfo/hardware"
            },{
                "name": "网络资源",
                "path": "/envInfo/resource"
            },{
                "name": "网络服务",
                "path": "/envInfo/service"
            },{
                "name": "消息日志",
                "path": "/appMgr/log"
            },{
                "name": "设置",
                "path": "/config/config"
            }],
            "items": [
                {
                    "component": "article/lists/index",
                    "label": "虚拟资源",
                    "name": "云主机",
                    "kind": "doslab.io.VirtualMachine",
                    "path": "/envInfo/basicInfo/vm"
                },
                {
                    "component": "article/lists/index",
                    "name": "云盘",
                    "label": "虚拟资源",
                    "kind": "doslab.io.VirtualMachineDisk",
                    "path": "/envInfo/basicInfo/disk"
                },
                {
                    "component": "article/lists/index",
                    "name": "镜像",
                    "label": "计算配置",
                    "kind": "doslab.io.VirtualMachineImages",
                    "path": "/envInfo/basicInfo/vmimage"
                },
                {
                    "component": "article/lists/index",
                    "name": "计算规格",
                    "kind": "doslab.io.VirtualMachineImages",
                    "path": "/envInfo/basicInfo/vmimage"
                },
                {
                    "component": "article/lists/index",
                    "name": "云盘规格",
                    "kind": "doslab.io.VirtualMachineImages",
                    "path": "/envInfo/basicInfo/vmimage"
                },
                {
                    "component": "article/lists/index",
                    "name": "快照",
                    "kind": "doslab.io.VirtualMachineImages",
                    "path": "/envInfo/basicInfo/vmimage"
                },
                {
                    "component": "article/lists/index",
                    "name": "区域",
                    "kind": "Node",
                    "path": "/envInfo/hardware/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "集群",
                    "kind": "Node",
                    "path": "/envInfo/hardwareo/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "物理机",
                    "kind": "Node",
                    "path": "/envInfo/hardwareo/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "主存储",
                    "kind": "Node",
                    "path": "/envInfo/hardwareo/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "镜像服务器",
                    "kind": "Node",
                    "path": "/envInfo/hardwareo/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "二层网络",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "VXLAN Pool",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "共有网络",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "扁平网络",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "VPC网络",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "VPC路由器",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "路由器镜像",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "路由器规格",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "管理网络",
                    "kind": "Node",
                    "path": "/envInfo/resource/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "安全组",
                    "kind": "Node",
                    "path": "/envInfo/service/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "虚拟IP",
                    "kind": "Node",
                    "path": "/envInfo/service/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "弹性IP",
                    "kind": "Node",
                    "path": "/envInfo/service/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "端口转发",
                    "kind": "Node",
                    "path": "/envInfo/service/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "负载均衡",
                    "kind": "Node",
                    "path": "/envInfo/service/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "操作日志",
                    "kind": "Node",
                    "path": "/appMgr/log/node"
                },
                {
                    "component": "article/lists/index",
                    "name": "全局日志",
                    "kind": "Node",
                    "path": "/config/config/node"
                }
            ]
        }
    }
})


Mock.mock('http://localhost:5173/kubesys/system/login',{
    code: 20000,
    data: "k4m9cmqtkhtz7dsocyh7n1q7fidf3fwhusgu5ajo8y4ludvecop7xa3xusombrdrmaiidaggd55662kwlxn14kbbauqzclmxk2kynhjwzlqr0asohesufhjfb7xzldfpnqwfqjxwr2rrupzxfbjvwx0xqxr4nicoo3wvdwq9h8ef0ccz6y2vnlnmum2nc3qxrnvbqzffbljoumrc8hb9bgzijzkdwewbagqlrmmwcgcgsavtb64llknhvaxfeqsf"
})

Mock.mock('http://localhost:5173/kubesys/system/listResources',{
    code: 20000,
    data: null
})

Mock.mock('http://localhost:5173/kubesys/kube/getResource/table', {
    code: 20000,
    data: {
        "apiVersion": "doslab.io/v1",
        "kind": "Frontend",
        "metadata": {
            "name": "pod-table"
        },
        "spec": {
            "data": [{
                "title": "名称",
                "fixed": 'left',
                "dataIndex": "name",
                "width": "300px",
                "row": "metadata.name"
            },
                {
                    "title": "控制台ַ",
                    "row": "status.podIP"
                },
                {
                    "kind": "启用状态",
                    "title": "命名空间",
                    "row": "metadata.namespace",
                    "internalLink": {
                        "kind": "Namespace"
                    }
                },
                {
                    "kind": "internalLink",
                    "title": "CPU",
                    "link": "@metadata.ownerReferences[0].apiVersion;.;metadata.ownerReferences[0].kind",
                    "row": "metadata.ownerReferences[0].name",
                    "internalLink": {
                        "kind": "@metadata.ownerReferences[0].apiVersion+metadata.ownerReferences[0].kind",
                        "item": "@metadata.ownerReferences[0].name"
                    }
                },
                {
                    "title": "内存",
                    "kind": "internalLink",
                    "tag": "metadata##name",
                    "row": "spec.nodeName",
                    "internalLink": {
                        "kind": "Node"
                    }
                },
                {
                    "title": "IPv4地址",
                    "row": "metadata.creationTimestamp"
                },
                {
                    "kind": "terminalLink",
                    "title": "远程连接",
                    "terminalLink": {
                        "icon": "Monitor",
                        "target": "http://133.133.135.134:30201/e/{containerID}",
                        "values": [
                            "status.containerStatuses[0]"
                        ]
                    }
                },
                {
                    "kind": "terminalLink",
                    "title": "CPU架构",
                    "terminalLink": {
                        "icon": "Cellphone",
                        "target": "http://133.133.135.134:30201/e/{containerID}?follow=1&tail=10",
                        "values": [
                            "status.containerStatuses[0]"
                        ]
                    }
                },
                {
                    "title": "平台",
                    "row": "status.phase",
                    "iconLink": [
                        {
                            "value": "正常运行",
                            "icon": "running.icon"
                        },
                        {
                            "value": "失败停止",
                            "icon": "fail.icon"
                        }
                    ]
                },
                {
                    "title": "所有者",
                    "width": "300px",
                    "row": "metadata.creationTimestamp"
                },
                {
                    "title": "创建时间",
                    "row": "metadata.creationTimestamp"
                },
                {
                    "kind": "action",
                    "title": "操作",
                    "fixed": 'right',
                    "actionLink": [
                        {
                            "label": "更新",
                            "action": "UPDATE"
                        },
                        {
                            "label": "删除",
                            "action": "DELETE"
                        },
                        {
                            "label": "扩容",
                            "action": "pod-action-scale"
                        }
                    ]
                }
            ],
            "type": "table"
        }
    }
})

Mock.mock('http://localhost:5173/kubesys/kube/getResource/desc',{
    code: 20000,
    data: {
        "metadata":{
            "name":"pod-desc"
        },
        "apiVersion":"doslab.io/v1",
        "kind":"Frontend",
        "spec":{
            "title": "云主机",
            "desc": "运行在物理机上的虚拟机实例，具有独立的IP地址，可以访问公共网络，运行应用服务。",
            "type": "description"
        }
    }
})

Mock.mock('http://localhost:5173/kubesys/kube/getResource/formsearch',{
    code: 20000,
    data: {
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
                },
                    {
                        "label": "所在主机:",
                        "path": "spec##nodeName",
                        "type": "combobox",
                        "data": {
                            "kind": "Node",
                            "field": "metadata##name"
                        }
                    },
                    {
                        "label": "实例状态:",
                        "path": "status##phase",
                        "type": "combobox",
                        "data": {
                            "kind": "ConfigMap",
                            "namespace": "default",
                            "name": "pod-status"
                        }
                    }]
            },
            "type": "formsearch"
        }
    }
})

Mock.mock('http://localhost:5173/system/menu/route',{
    code:200,
    msg: "成功",
    data:[
        {
            "menuIcon": "el-icon-Monitor",
            "kind": "云主机",
            "pid": 0,
            "updateTime": "2022-09-19 17:22:25",
            "params": "",
            "isShow": 1,
            "isCache": 1,
            "isDisable": 0,
            "component": "article/lists/index",
            "createTime": "2022-04-19 12:16:05",
            "paths": "workbench",
            "menuType": "C",
            "perms": "common:index:console",
            "id": 1,
            "selected": "",
            "menuSort": 50,
            "otherURL": "http://localhost:5173/cloudHostings"
        },
        {
            "menuIcon": "el-icon-ChatDotSquare",
            "kind": "云盘",
            "pid": 703,
            "updateTime": "2022-09-20 15:17:00",
            "params": "",
            "isShow": 1,
            "isCache": 1,
            "isDisable": 0,
            "component": "article/cloud/index",
            "createTime": "2022-08-29 15:22:23",
            "paths": "lists",
            "menuType": "C",
            "perms": "article:list",
            "id": 704,
            "selected": "",
            "menuSort": 49
        },
        {
            "menuIcon": "el-icon-ChatDotSquare",
            "kind": "规格",
            "pid": 703,
            "updateTime": "2022-09-20 15:17:00",
            "params": "",
            "isShow": 1,
            "isCache": 1,
            "isDisable": 0,
            "component": "article/cloud/index",
            "createTime": "2022-08-29 15:22:23",
            "paths": "lists",
            "menuType": "C",
            "perms": "article:list",
            "id": 704,
            "selected": "",
            "menuSort": 49
        },
        {
            "menuIcon": "el-icon-CollectionTag",
            "kind": "镜像",
            "pid": 703,
            "updateTime": "2022-09-19 17:02:17",
            "params": "",
            "isShow": 1,
            "isCache": 1,
            "isDisable": 0,
            "component": "article/column/index",
            "createTime": "2022-08-29 15:46:58",
            "paths": "column",
            "menuType": "C",
            "perms": "article:cate:list",
            "id": 705,
            "selected": "",
            "menuSort": 48
        },
        {
            "menuIcon": "el-icon-Lock",
            "kind": "权限管理",
            "pid": 0,
            "updateTime": "2022-09-08 16:36:41",
            "params": "",
            "isShow": 1,
            "isCache": 0,
            "isDisable": 0,
            "component": "",
            "createTime": "2022-04-19 12:16:05",
            "children": [
                {
                    "menuIcon": "local-icon-wode",
                    "kind": "管理员",
                    "pid": 100,
                    "updateTime": "2022-09-16 12:10:04",
                    "params": "",
                    "isShow": 1,
                    "isCache": 1,
                    "isDisable": 0,
                    "component": "permission/admin/index",
                    "createTime": "2022-04-19 12:16:05",
                    "paths": "admin",
                    "menuType": "C",
                    "perms": "system:admin:list",
                    "id": 101,
                    "selected": "",
                    "menuSort": 0
                },
                {
                    "menuIcon": "el-icon-Female",
                    "kind": "角色管理",
                    "pid": 100,
                    "updateTime": "2022-09-16 12:10:51",
                    "params": "",
                    "isShow": 1,
                    "isCache": 1,
                    "isDisable": 0,
                    "component": "permission/role/index",
                    "createTime": "2022-04-19 12:16:05",
                    "paths": "role",
                    "menuType": "C",
                    "perms": "system:role:list",
                    "id": 110,
                    "selected": "",
                    "menuSort": 0
                }
            ],
            "paths": "permission",
            "menuType": "M",
            "perms": "",
            "id": 100,
            "selected": "",
            "menuSort": 44
        },
        {
            "menuIcon": "local-icon-keziyuyue",
            "kind": "用户设置",
            "pid": 500,
            "updateTime": "2022-09-16 12:12:50",
            "params": "",
            "isShow": 1,
            "isCache": 0,
            "isDisable": 0,
            "component": "",
            "createTime": "2022-09-06 17:10:07",
            "children": [
                {
                    "menuIcon": "",
                    "kind": "用户设置",
                    "pid": 724,
                    "updateTime": "2022-09-16 15:10:25",
                    "params": "",
                    "isShow": 1,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "setting/user/setup",
                    "createTime": "2022-09-06 17:12:35",
                    "paths": "setup",
                    "menuType": "C",
                    "perms": "setting:user:detail",
                    "id": 725,
                    "selected": "",
                    "menuSort": 0
                },
                {
                    "menuIcon": "",
                    "kind": "登录注册",
                    "pid": 724,
                    "updateTime": "2022-09-16 15:11:03",
                    "params": "",
                    "isShow": 1,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "setting/user/login_register",
                    "createTime": "2022-09-06 17:27:55",
                    "paths": "login_register",
                    "menuType": "C",
                    "perms": "setting:login:detail",
                    "id": 726,
                    "selected": "",
                    "menuSort": 0
                }
            ],
            "paths": "user",
            "menuType": "M",
            "perms": "",
            "id": 724,
            "selected": "",
            "menuSort": 8
        },
        {
            "menuIcon": "",
            "kind": "文章添加/编辑",
            "pid": 703,
            "updateTime": "2022-09-30 11:54:52",
            "params": "",
            "isShow": 0,
            "isCache": 0,
            "isDisable": 0,
            "component": "article/lists/edit",
            "createTime": "2022-09-08 16:31:22",
            "paths": "lists/edit",
            "menuType": "C",
            "perms": "article:add/edit",
            "id": 731,
            "selected": "/article/lists",
            "menuSort": 2
        },
        {
            "menuIcon": "el-icon-EditPen",
            "kind": "开发工具",
            "pid": 0,
            "updateTime": "2022-09-28 11:28:21",
            "params": "",
            "isShow": 1,
            "isCache": 0,
            "isDisable": 0,
            "component": "",
            "createTime": "2022-08-09 14:46:46",
            "children": [
                {
                    "menuIcon": "el-icon-Box",
                    "kind": "字典管理",
                    "pid": 600,
                    "updateTime": "2022-09-15 15:14:47",
                    "params": "",
                    "isShow": 1,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "setting/dict/type/index",
                    "createTime": "2022-08-09 16:57:16",
                    "paths": "dict",
                    "menuType": "C",
                    "perms": "setting:dict:type:list",
                    "id": 515,
                    "selected": "",
                    "menuSort": 0
                },
                {
                    "menuIcon": "",
                    "kind": "字典数据管理",
                    "pid": 600,
                    "updateTime": "2022-09-16 14:20:52",
                    "params": "",
                    "isShow": 0,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "setting/dict/data/index",
                    "createTime": "2022-08-11 15:29:08",
                    "paths": "dict/data",
                    "menuType": "C",
                    "perms": "setting:dict:data:list",
                    "id": 519,
                    "selected": "/dev_tools/dict",
                    "menuSort": 0
                },
                {
                    "menuIcon": "el-icon-DocumentAdd",
                    "kind": "代码生成器",
                    "pid": 600,
                    "updateTime": "2022-08-15 11:01:50",
                    "params": "",
                    "isShow": 1,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "dev_tools/code/index",
                    "createTime": "2022-08-09 15:09:14",
                    "paths": "code",
                    "menuType": "C",
                    "perms": "gen:list",
                    "id": 610,
                    "selected": "",
                    "menuSort": 0
                },
                {
                    "menuIcon": "",
                    "kind": "编辑数据表",
                    "pid": 600,
                    "updateTime": "2022-08-30 15:13:35",
                    "params": "",
                    "isShow": 0,
                    "isCache": 0,
                    "isDisable": 0,
                    "component": "dev_tools/code/edit",
                    "createTime": "2022-08-30 15:12:05",
                    "paths": "code/edit",
                    "menuType": "C",
                    "perms": "gen:editTable",
                    "id": 715,
                    "selected": "/dev_tools/code",
                    "menuSort": 0
                }
            ],
            "paths": "dev_tools",
            "menuType": "M",
            "perms": "",
            "id": 600,
            "selected": "",
            "menuSort": 0
        },
        {
            "menuIcon": "",
            "kind": "用户详情",
            "pid": 712,
            "updateTime": "2022-09-08 17:07:29",
            "params": "",
            "isShow": 0,
            "isCache": 0,
            "isDisable": 0,
            "component": "consumer/lists/detail",
            "createTime": "2022-09-08 17:07:29",
            "paths": "detail",
            "menuType": "C",
            "perms": "user:detail",
            "id": 739,
            "selected": "/consumer/lists",
            "menuSort": 0
        }
    ]
});

Mock.mock('http://localhost:5173//system/admin/self',{
    "code": 200,
    "msg": "成功",
    "data": {
        "user": {
            "id": 1,
            "deptId": 1,
            "postId": 0,
            "username": "admin",
            "nickname": "admin",
            "avatar": "",
            "dept": "1",
            "role": "0",
            "isMultipoint": 1,
            "isDisable": 0,
            "lastLoginIp": "127.0.0.1",
            "lastLoginTime": "2023-07-10 15:41:58",
            "createTime": "2022-01-16 16:26:39",
            "updateTime": "2022-08-12 14:55:25"
        },
        "permissions": [
            "*"
        ]
    }
})

Mock.mock('http://localhost:5173/kubesys/system/listResources-text', {
    code: 20000,
    data: {
        "apiVersion": "doslab.io/v1",
        "kind": "VirtualMachineList",
        "metadata": {
            "name": "get-virtualmachines",
            "totalCount": 2,
            "currentPage": 1,
            "totalPage": 1,
            "itemsPerPage": 10,
            "conditions": "{}"
        },
        items: [{
            "apiVersion": "doslab.io/v1",
            "kind": "VirtualMachine",
            "metadata": {
                "annotations": {
                    "kubectl.kubernetes.io/last-applied-configuration": "{\"apiVersion\":\"doslab.io/v1\",\"kind\":\"VirtualMachine\",\"metadata\":{\"annotations\":{},\"labels\":{\"host\":\"133.133.135.134\"},\"name\":\"centos7\",\"namespace\":\"default\"},\"spec\":{\"lifecycle\":{\"createAndStartVMFromISO\":{\"cdrom\":\"/var/lib/libvirt/iso/centos7-minimal-1810.iso\",\"disk\":\"/var/lib/libvirt/cephfspool/centos7-disk1/centos7-disk1,format=qcow2\",\"graphics\":\"vnc,listen=0.0.0.0\",\"memory\":\"4096\",\"network\":\"type=bridge,source=virbr0\",\"noautoconsole\":true,\"os_variant\":\"centos7.0\",\"vcpus\":\"4\",\"virt_type\":\"kvm\"}},\"nodeName\":\"133.133.135.134\"}}\n"
                },
                "creationTimestamp": "2023-12-29T03:22:45Z",
                "generation": 11,
                "labels": {
                    "host": "133.133.135.134"
                },
                "managedFields": [
                    {
                        "apiVersion": "doslab.io/v1",
                        "fieldsType": "FieldsV1",
                        "fieldsV1": {
                            "f:metadata": {
                                "f:annotations": {
                                    ".": {},
                                    "f:kubectl.kubernetes.io/last-applied-configuration": {}
                                },
                                "f:labels": {
                                    ".": {},
                                    "f:host": {}
                                }
                            },
                            "f:spec": {
                                ".": {},
                                "f:nodeName": {}
                            }
                        },
                        "manager": "kubectl-client-side-apply",
                        "operation": "Update",
                        "time": "2023-12-29T03:22:45Z"
                    },
                    {
                        "apiVersion": "doslab.io/v1",
                        "fieldsType": "FieldsV1",
                        "fieldsV1": {
                            "f:spec": {
                                "f:description": {
                                    ".": {},
                                    "f:lastOperationTimeStamp": {}
                                },
                                "f:domain": {
                                    ".": {},
                                    "f:_id": {},
                                    "f:_type": {},
                                    "f:clock": {
                                        ".": {},
                                        "f:_offset": {},
                                        "f:timer": {}
                                    },
                                    "f:cpu": {
                                        ".": {},
                                        "f:_check": {},
                                        "f:_match": {},
                                        "f:_mode": {},
                                        "f:feature": {},
                                        "f:model": {
                                            ".": {},
                                            "f:_fallback": {},
                                            "f:text": {}
                                        },
                                        "f:vendor": {
                                            ".": {},
                                            "f:text": {}
                                        }
                                    },
                                    "f:currentMemory": {
                                        ".": {},
                                        "f:_unit": {},
                                        "f:text": {}
                                    },
                                    "f:devices": {
                                        ".": {},
                                        "f:_interface": {},
                                        "f:channel": {},
                                        "f:console": {},
                                        "f:controller": {},
                                        "f:disk": {},
                                        "f:emulator": {
                                            ".": {},
                                            "f:text": {}
                                        },
                                        "f:graphics": {},
                                        "f:input": {},
                                        "f:memballoon": {
                                            ".": {},
                                            "f:_model": {},
                                            "f:address": {
                                                ".": {},
                                                "f:_bus": {},
                                                "f:_domain": {},
                                                "f:_function": {},
                                                "f:_slot": {},
                                                "f:_type": {}
                                            },
                                            "f:alias": {
                                                ".": {},
                                                "f:_name": {}
                                            },
                                            "f:stats": {
                                                ".": {},
                                                "f:_period": {}
                                            }
                                        },
                                        "f:rng": {},
                                        "f:serial": {},
                                        "f:video": {}
                                    },
                                    "f:features": {
                                        ".": {},
                                        "f:acpi": {},
                                        "f:apic": {}
                                    },
                                    "f:memory": {
                                        ".": {},
                                        "f:_unit": {},
                                        "f:text": {}
                                    },
                                    "f:name": {
                                        ".": {},
                                        "f:text": {}
                                    },
                                    "f:on_crash": {
                                        ".": {},
                                        "f:text": {}
                                    },
                                    "f:on_poweroff": {
                                        ".": {},
                                        "f:text": {}
                                    },
                                    "f:on_reboot": {
                                        ".": {},
                                        "f:text": {}
                                    },
                                    "f:os": {
                                        ".": {},
                                        "f:boot": {},
                                        "f:type": {
                                            ".": {},
                                            "f:_arch": {},
                                            "f:_machine": {},
                                            "f:text": {}
                                        }
                                    },
                                    "f:pm": {
                                        ".": {},
                                        "f:suspend_to_disk": {
                                            ".": {},
                                            "f:_enabled": {}
                                        },
                                        "f:suspend_to_mem": {
                                            ".": {},
                                            "f:_enabled": {}
                                        }
                                    },
                                    "f:resource": {
                                        ".": {},
                                        "f:partition": {
                                            ".": {},
                                            "f:text": {}
                                        }
                                    },
                                    "f:seclabel": {},
                                    "f:uuid": {
                                        ".": {},
                                        "f:text": {}
                                    },
                                    "f:vcpu": {
                                        ".": {},
                                        "f:_placement": {},
                                        "f:text": {}
                                    }
                                },
                                "f:powerstate": {},
                                "f:status": {
                                    ".": {},
                                    "f:conditions": {
                                        ".": {},
                                        "f:state": {
                                            ".": {},
                                            "f:waiting": {
                                                ".": {},
                                                "f:message": {},
                                                "f:reason": {}
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        "manager": "OpenAPI-Generator",
                        "operation": "Update",
                        "time": "2024-01-02T01:12:25Z"
                    }
                ],
                "name": "centos7",
                "namespace": "default",
                "resourceVersion": "1081684",
                "uid": "3a6cefae-c2ec-4560-ad9a-42df6678d01d"
            },
            "spec": {
                "description": {
                    "lastOperationTimeStamp": 1704157945915
                },
                "domain": {
                    "_id": 2,
                    "_type": "kvm",
                    "clock": {
                        "_offset": "utc",
                        "timer": [
                            {
                                "_name": "rtc",
                                "_tickpolicy": "catchup"
                            },
                            {
                                "_name": "pit",
                                "_tickpolicy": "delay"
                            },
                            {
                                "_name": "hpet",
                                "_present": "no"
                            }
                        ]
                    },
                    "cpu": {
                        "_check": "full",
                        "_match": "exact",
                        "_mode": "custom",
                        "feature": [
                            {
                                "_name": "vme",
                                "_policy": "require"
                            },
                            {
                                "_name": "ss",
                                "_policy": "require"
                            },
                            {
                                "_name": "pcid",
                                "_policy": "require"
                            },
                            {
                                "_name": "hypervisor",
                                "_policy": "require"
                            },
                            {
                                "_name": "arat",
                                "_policy": "require"
                            },
                            {
                                "_name": "tsc_adjust",
                                "_policy": "require"
                            },
                            {
                                "_name": "md-clear",
                                "_policy": "require"
                            },
                            {
                                "_name": "stibp",
                                "_policy": "require"
                            },
                            {
                                "_name": "ssbd",
                                "_policy": "require"
                            },
                            {
                                "_name": "xsaveopt",
                                "_policy": "require"
                            },
                            {
                                "_name": "pdpe1gb",
                                "_policy": "require"
                            },
                            {
                                "_name": "ibpb",
                                "_policy": "require"
                            }
                        ],
                        "model": {
                            "_fallback": "forbid",
                            "text": "SandyBridge-IBRS"
                        },
                        "vendor": {
                            "text": "Intel"
                        }
                    },
                    "currentMemory": {
                        "_unit": "KiB",
                        "text": 4194304
                    },
                    "devices": {
                        "_interface": [
                            {
                                "_type": "bridge",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_slot": "0x08",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "net0"
                                },
                                "mac": {
                                    "_address": "52:54:00:6d:7f:bb"
                                },
                                "model": {
                                    "_type": "virtio"
                                },
                                "source": {
                                    "_bridge": "virbr0"
                                },
                                "target": {
                                    "_dev": "fe54006d7fbb"
                                }
                            }
                        ],
                        "channel": [
                            {
                                "_type": "unix",
                                "address": {
                                    "_bus": 0,
                                    "_controller": 0,
                                    "_port": 1,
                                    "_type": "virtio-serial"
                                },
                                "alias": {
                                    "_name": "channel0"
                                },
                                "source": {
                                    "_mode": "bind",
                                    "_path": "/var/lib/libvirt/qemu/channel/target/domain-2-centos7/org.qemu.guest_agent.0"
                                },
                                "target": {
                                    "_name": "org.qemu.guest_agent.0",
                                    "_state": "disconnected",
                                    "_type": "virtio"
                                }
                            }
                        ],
                        "console": [
                            {
                                "_tty": "/dev/pts/2",
                                "_type": "pty",
                                "alias": {
                                    "_name": "serial0"
                                },
                                "source": {
                                    "_path": "/dev/pts/2"
                                },
                                "target": {
                                    "_port": 0,
                                    "_type": "serial"
                                }
                            }
                        ],
                        "controller": [
                            {
                                "_index": 0,
                                "_model": "ich9-ehci1",
                                "_type": "usb",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x7",
                                    "_slot": "0x03",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "usb"
                                }
                            },
                            {
                                "_index": 0,
                                "_model": "ich9-uhci1",
                                "_type": "usb",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_multifunction": "on",
                                    "_slot": "0x03",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "usb"
                                },
                                "master": {
                                    "_startport": 0
                                }
                            },
                            {
                                "_index": 0,
                                "_model": "ich9-uhci2",
                                "_type": "usb",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x1",
                                    "_slot": "0x03",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "usb"
                                },
                                "master": {
                                    "_startport": 2
                                }
                            },
                            {
                                "_index": 0,
                                "_model": "ich9-uhci3",
                                "_type": "usb",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x2",
                                    "_slot": "0x03",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "usb"
                                },
                                "master": {
                                    "_startport": 4
                                }
                            },
                            {
                                "_index": 0,
                                "_model": "pci-root",
                                "_type": "pci",
                                "alias": {
                                    "_name": "pci.0"
                                }
                            },
                            {
                                "_index": 0,
                                "_type": "ide",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x1",
                                    "_slot": "0x01",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "ide"
                                }
                            },
                            {
                                "_index": 0,
                                "_type": "virtio-serial",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_slot": "0x04",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "virtio-serial0"
                                }
                            }
                        ],
                        "disk": [
                            {
                                "_device": "disk",
                                "_type": "file",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_slot": "0x05",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "virtio-disk0"
                                },
                                "backingStore": {},
                                "driver": {
                                    "_name": "qemu",
                                    "_type": "qcow2"
                                },
                                "source": {
                                    "_file": "/var/lib/libvirt/cephfspool/centos7-disk1/centos7-disk1"
                                },
                                "target": {
                                    "_bus": "virtio",
                                    "_dev": "vda"
                                }
                            },
                            {
                                "_device": "cdrom",
                                "_type": "file",
                                "address": {
                                    "_bus": 0,
                                    "_controller": 0,
                                    "_target": 0,
                                    "_type": "drive",
                                    "_unit": 0
                                },
                                "alias": {
                                    "_name": "ide0-0-0"
                                },
                                "driver": {
                                    "_name": "qemu"
                                },
                                "readonly": {},
                                "target": {
                                    "_bus": "ide",
                                    "_dev": "hda"
                                }
                            }
                        ],
                        "emulator": {
                            "text": "/usr/libexec/qemu-kvm"
                        },
                        "graphics": [
                            {
                                "_autoport": "yes",
                                "_listen": "0.0.0.0",
                                "_port": 5900,
                                "_type": "vnc",
                                "listen": {
                                    "_address": "0.0.0.0",
                                    "_type": "address"
                                }
                            }
                        ],
                        "input": [
                            {
                                "_bus": "usb",
                                "_type": "tablet",
                                "address": {
                                    "_bus": 0,
                                    "_port": 1,
                                    "_type": "usb"
                                },
                                "alias": {
                                    "_name": "input0"
                                }
                            },
                            {
                                "_bus": "ps2",
                                "_type": "mouse",
                                "alias": {
                                    "_name": "input1"
                                }
                            },
                            {
                                "_bus": "ps2",
                                "_type": "keyboard",
                                "alias": {
                                    "_name": "input2"
                                }
                            }
                        ],
                        "memballoon": {
                            "_model": "virtio",
                            "address": {
                                "_bus": "0x00",
                                "_domain": "0x0000",
                                "_function": "0x0",
                                "_slot": "0x06",
                                "_type": "pci"
                            },
                            "alias": {
                                "_name": "balloon0"
                            },
                            "stats": {
                                "_period": 5
                            }
                        },
                        "rng": [
                            {
                                "_model": "virtio",
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_slot": "0x07",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "rng0"
                                },
                                "backend": {
                                    "_model": "random",
                                    "text": "/dev/urandom"
                                }
                            }
                        ],
                        "serial": [
                            {
                                "_type": "pty",
                                "alias": {
                                    "_name": "serial0"
                                },
                                "source": {
                                    "_path": "/dev/pts/2"
                                },
                                "target": {
                                    "_port": 0,
                                    "_type": "isa-serial",
                                    "model": {
                                        "_name": "isa-serial"
                                    }
                                }
                            }
                        ],
                        "video": [
                            {
                                "address": {
                                    "_bus": "0x00",
                                    "_domain": "0x0000",
                                    "_function": "0x0",
                                    "_slot": "0x02",
                                    "_type": "pci"
                                },
                                "alias": {
                                    "_name": "video0"
                                },
                                "model": {
                                    "_heads": 1,
                                    "_primary": "yes",
                                    "_type": "cirrus",
                                    "_vram": 16384
                                }
                            }
                        ]
                    },
                    "features": {
                        "acpi": {},
                        "apic": {}
                    },
                    "memory": {
                        "_unit": "KiB",
                        "text": 4194304
                    },
                    "name": {
                        "text": "centos7"
                    },
                    "on_crash": {
                        "text": "destroy"
                    },
                    "on_poweroff": {
                        "text": "destroy"
                    },
                    "on_reboot": {
                        "text": "restart"
                    },
                    "os": {
                        "boot": [
                            {
                                "_dev": "hd"
                            }
                        ],
                        "type": {
                            "_arch": "x86_64",
                            "_machine": "pc-i440fx-rhel7.6.0",
                            "text": "hvm"
                        }
                    },
                    "pm": {
                        "suspend_to_disk": {
                            "_enabled": "no"
                        },
                        "suspend_to_mem": {
                            "_enabled": "no"
                        }
                    },
                    "resource": {
                        "partition": {
                            "text": "/machine"
                        }
                    },
                    "seclabel": [
                        {
                            "_model": "dac",
                            "_relabel": "yes",
                            "_type": "dynamic",
                            "imagelabel": {
                                "text": "+107:+107"
                            },
                            "label": {
                                "text": "+107:+107"
                            }
                        }
                    ],
                    "uuid": {
                        "text": "d1405afd-f675-4681-a3b9-6becaeab15ca"
                    },
                    "vcpu": {
                        "_placement": "static",
                        "text": 4
                    }
                },
                "nodeName": "133.133.135.134",
                "powerstate": "Running",
                "status": {
                    "conditions": {
                        "state": {
                            "waiting": {
                                "message": "The VM is Running",
                                "reason": "Running"
                            }
                        }
                    }
                }
            }
        }]
    }
})


