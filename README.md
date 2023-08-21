# frontend

#####技术架构:
#######Vue3 + node.js + elementPlus + vite + typescript + yarn

#####部署流程:
#####确保安装了yarn，如果没有全局安装yarn：
#######npm install -g yarn

#####安装依赖
#######yarn install

#####运行
#######yarn dev

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

