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
                    "name": "VirtualMachine",
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
                    "name": "VirtualMachineDisk",
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
                "label": "Pod名",
                "row": "metadata.name"
            },
                {
                    "label": "IP地址ַ",
                    "row": "status.podIP"
                },
                {
                    "kind": "internalLink",
                    "label": "命名空间",
                    "row": "metadata.namespace",
                    "internalLink": {
                        "kind": "Namespace"
                    }
                },
                {
                    "kind": "internalLink",
                    "label": "父类资源名",
                    "link": "@metadata.ownerReferences[0].apiVersion;.;metadata.ownerReferences[0].kind",
                    "row": "metadata.ownerReferences[0].name",
                    "internalLink": {
                        "kind": "@metadata.ownerReferences[0].apiVersion+metadata.ownerReferences[0].kind",
                        "item": "@metadata.ownerReferences[0].name"
                    }
                },
                {
                    "label": "所在主机",
                    "kind": "internalLink",
                    "tag": "metadata##name",
                    "row": "spec.nodeName",
                    "internalLink": {
                        "kind": "Node"
                    }
                },
                {
                    "label": "创建时间",
                    "row": "metadata.creationTimestamp"
                },
                {
                    "kind": "terminalLink",
                    "label": "远程连接",
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
                    "label": "容器日志",
                    "terminalLink": {
                        "icon": "Cellphone",
                        "target": "http://133.133.135.134:30201/e/{containerID}?follow=1&tail=10",
                        "values": [
                            "status.containerStatuses[0]"
                        ]
                    }
                },
                {
                    "label": "运行状态",
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
                    "kind": "action",
                    "label": "更多操作",
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
            "title": "Pod",
            "desc": "Pod是Kubernetes原生概念，是容器的基本运行单元。",
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


