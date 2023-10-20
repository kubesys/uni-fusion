## kube-backend

Providing unified API to manage mutiple Kubernetes clusters:

- RuntimeMirror: extract all objects during runtime by Kubernetes' 'watch' API, then write to a database and publish to a MQ using the JSON style.
- ApiMapper: define an unified APIs <clusterId, operator, json>, which include create, update, delete, list, get
  - create/update/delete: invoking Kubernetes apiserver directly
  - query/get: access to the database using SQL  

This project is based on the following softwares.

|               NAME            |   Website                       |      LICENSE              | 
|-------------------------------|---------------------------------|---------------------------|
|     client-java               |  https://github.com/kubesys/client-java              |  Apache License 2.0 |
|     devfrk-java               |  https://github.com/kubesys/devfrk-java              |  Apache License 2.0 |



This project should work with the following components.

- [Kubernetes](https://github.com/kubernetes/kubernetes)
- [frontend](https://github.com/kubesys/frontend)


## Authos

- wuheng@iscas.ac.cn
- xugang@iscas.ac.cn

## API

### 1. 创建 http://ip:30308/kubesys/kube/createResource

POST请求 

参数示例，region适用与多集群场景，一个集群固定为local，data下面就是具体Pod的json
```
{
    "region": "local",
    "data": {
        "apiVersion": "v1",
        "kind": "Pod",
        "metadata": {
            "name": "busybox-pod",
            "labels": {
                "app": "busybox"
            }
        },
        "spec": {
            "containers": [
                {
                    "name": "busybox-container",
                    "image": "busybox",
                    "command": [
                        "sleep",
                        "3600"
                    ],
                    "resources": {
                        "limits": {
                            "memory": "256Mi",
                            "cpu": "500m"
                        }
                    }
                }
            ]
        }
    }
}
```

### 2. 更新 http://ip:30308/kubesys/kube/updateResource

POST请求

参数示例，region适用与多集群场景，一个集群固定为local，data下面就是具体Pod的json
```
{
    "region": "local",
    "data": {
        "apiVersion": "v1",
        "kind": "Pod",
        "metadata": {
            "name": "busybox-pod",
            "labels": {
                "app": "busybox"
            }
        },
        "spec": {
            "containers": [
                {
                    "name": "busybox-container",
                    "image": "busybox",
                    "command": [
                        "sleep",
                        "3600"
                    ],
                    "resources": {
                        "limits": {
                            "memory": "256Mi",
                            "cpu": "500m"
                        }
                    }
                }
            ]
        }
    }
}
```

### 3. 删除 http://ip:30308/kubesys/kube/deleteResource

POST请求


fullkind参见getMeta方法，fullkind = group + "." + kind，对于Pod的apversion为v1，则其group为null，fullkind = kind，对于Deployment，其apiversion为apps/v1，则其
group为apps，fullkind = apps.Deployment
```
{
    "fullkind": "Pod",
    "name": "busybox-pod",
    "namespace": "default",
    "region": "local"
}
```

### 4. 获取 http://ip:30308/kubesys/kube/getResource

POST请求


```
{
    "fullkind": "Pod",
    "name": "busybox",
    "namespace": "default",
    "region": "test"
}
```


### 5. 查询 http://ip:30308/kubesys/kube/listResources

POST请求

page表示当前第几页，limit表示每页做多显示多少个元素
labels表示查询条件，由于前端.是关键字，故采用##替代，metdadata##name表示查询Pod中JSON的metadata.name字段

```
{
    "fullkind": "Pod",
    "page": 1,
    "limit": 10,
    "labels": {"metdata##name", "kube"},
    "region": "test"
}
```

### 5. 查询 http://ip:30308/kubesys/kube/getMeta

POST请求


```
{
    "kind": "Meta",
    "region": "local"
}
```

### 7. 登陆 /system/login

POST请求

```
{
    "kind": "User",
    "data": {
        "name": "admin",
        "password": "b25jZWFz"
    }
}
```

### 8. 登出 /system/logout

POST请求

Headers
```
authorization: bearer token (see response from login)
user: see name in login
```

## Roadmap

- 2.3.x: support single region

## Others

- ?useUnicode=true&characterEncoding=UTF8&connectTimeout=2000&socketTimeout=6000&autoReconnect=true&&serverTimezone=Asia/Shang
