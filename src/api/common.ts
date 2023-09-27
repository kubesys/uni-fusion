import { getResource, listResources, updateResource, createResource, deleteResource } from "@/api/kubernetes"
import { ElMessage } from 'element-plus'

export function frontendData(ListName:string, TableName:string, pageSite:object, tableColumns:[], tableData:[], allLabels:object, actions:[] = [],  region='test'){
  const MAX_RETRIES = 5;
  const fetchData = (retryCount = 0) => {
    if (retryCount >= MAX_RETRIES) {
      ElMessage.error('lack of kind.')
      return;
    }

    listResources({
      fullkind: ListName,
      page: pageSite.value.page,
      limit: pageSite.value.limit,
      labels: allLabels,
      region: region
    }).then((resp)=>{
      console.log(resp.data.data.items);
      tableData.value = resp.data.data;

      /***********************
       *
       * Echarts data
       *
       ***********************/
      let resultRun = 0
      let resultPen = 0
      tableData.value.items.forEach((item:any)=>{
        if (item.status.phase === 'Running'){
          resultRun++
        } else if(item.status.phase !== 'Running'){
          resultPen++
        }
      })
      tableData.value.resultRun = resultRun
      tableData.value.resultPen = resultPen

      getResource({
        fullkind: "doslab.io.Frontend",
        name: TableName + '-table',
        namespace: "default",
        region: region
      }).then((resp) => {
        console.log(resp.data.data.spec.data);
        tableColumns.value = resp.data.data.spec.data;

        getResource({
          fullkind: "doslab.io.Frontend",
          name: TableName + '-action',
          namespace: "default",
          region: region
        }).then((resp)=>{
          console.log(resp.data.data.spec.data)
          actions.value = resp.data.data.spec.data
        }).catch((error)=>{
          fetchData(retryCount + 1);
        })

      }).catch((error) => {
        console.error("Inner request failed.");
        fetchData(retryCount + 1); // 递归调用 fetchData 函数，并增加 retryCount
      });
    }).catch((error) => {
      console.error("Outer request failed.");
      fetchData(retryCount + 1);
    });
  };

  fetchData();
  return [tableData, tableColumns];
}

export function frontendMeta(TableName:string, descItem: [], region = 'test', retryCount = 5) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-desc',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          console.log(resp.data.data.spec);
          descItem.value = resp.data.data.spec;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else {
            console.error('Request failed.');
          }
        });
  };

  getResourceData(1); // 初始化时发送请求
}

export function frontendFormSearch(TableName:string, formItem: [], region = 'test', retryCount = 5) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-formsearch',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          console.log(resp.data.data.spec.data.items);
          formItem.value = resp.data.data.spec.data.items;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1); // 重新发送
          } else {
            console.error('Request failed.');
          }
        });
  };

  getResourceData(1);
}

export function frontendAction(TableName:string, step: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-action-scale',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          console.log(resp.data.data.spec);
          step.value = resp.data.data.spec;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else {
            console.error('Request failed.');
          }
        });
  };

  getResourceData(1);
}

export function frontendCreateTemplate(TableName:string, templateSpec: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-create',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          console.log(resp.data.data.spec);
          templateSpec.value = resp.data.data.spec;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else {
            console.error('Request failed.');
          }
        });
  };

  getResourceData(1);
}

export function frontendUpdate(rowData:object, region = 'test', retryCount = 3) {
  const updateResourceData = (retry:any) => {
    updateResource({
      region: region,
      data: rowData
    })
        .then((resp) => {
          console.log(resp);
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            updateResourceData(retry + 1);
          } else {
            console.error('Request failed.');
          }
        });
  };

  updateResourceData(1);
}

export function validResponse(response:any) {
  return response != null && response.hasOwnProperty('code') && response.code === 20000
}
export function frontendCreate(jsonData:any, region = 'test'){
  createResource({
    region: region,
    data: jsonData
  }).then((resp)=>{
    if(resp.data.code == 20000){
      console.log(resp.data.code)
      ElMessage.success('创建成功.')
    }
  })
}

export function frontendDelete(Listname:string, name:string, region = 'test'){
  deleteResource({
    fullkind: Listname,
    name: name,
    namespace: "default",
    region: region
  }).then((resp)=>{
    if(resp.data.code == 20000){
      ElMessage.success('删除成功.')
    }
  })
}

/************************
 *
 * Tabel various values
 *
 ************************/
export function getComplexDataDispose(scope, rowKey){
  const value = getComplexValue(scope, rowKey)
  return value
}

export function getComplexValue(scope, key){
  if (JSON.stringify(scope) === '{}' || !key) {
    return '-'
  }

  let result = scope

  if (key.startsWith('@')) {
    let newkey = '';
    key.substring(1).split('+').forEach((item) => {
      console.log(item)
      if (item.includes('apiVersion')) {
        const apiVersion = result[item];
        console.log(apiVersion)
        if (apiVersion) {
          newkey += apiVersion.split('/')[0];
        } else {
          console.log(1);
          // 处理 apiVersion 为 undefined 或 null 的情况
        }
      } else {
        const value = result[item];
        if (value !== undefined && value !== null) {
          newkey += '.' + value;
        } else {
          console.log(2);
          // 处理 value 为 undefined 或 null 的情况
        }
      }
    });
    console.log(newkey);
  }


  key.split('.').every((item) => {
    item = item.replaceAll('#', '.')
    if (item.indexOf('[') > 0) {
      result = result[item.substring(0, item.indexOf('['))]
      if (result === undefined || result.length === 0) {
        result = '-'
        return false
      } else {
        result =
            result[
                parseInt(
                    item.substring(item.indexOf('[') + 1, item.indexOf(']'))
                )
                ]
        return true
      }
    } else {
      if (result && result[item] !== undefined) {
        result = result[item]
        return true
      } else {
        result = '⊘'
        return false
      }
    }
  })

  if (result instanceof Object || result instanceof Array) {
    const objResult = new Set()
    for (const key in result) {
      if (result[key] === '') {
        continue
      }
      objResult.add(result[key])
    }
    return objResult
  } else {
    if (result === 'Running') {
      result = '🟢'
    } else if (result === 'Terminating') {
      result = '🔴'
    } else if (result === 'Pending') {
      result = '🔴'
    } else if (result === 'Succeeded') {
      result = '🟢'
    } else if (result === 'Completed') {
      result = '🟢'
    } else if (result === 'Failed') {
      result = '🔴'
    } else if (result === 'Unknown') {
      result = '🔴'
    } else if (result === 'Ready') {
      result = '🟢'
    }
    else if ((result + '').endsWith('Ki')) {
      result = (Number(result.substring(0, result.length - 2).trim())/1024/1024).toFixed(2) + 'GB'
    } else if ((result + '').endsWith('Mi')) {
      result = (Number(result.substring(0, result.length - 2).trim())/1024).toFixed(2) + 'GB'
    } else if ((result + '').endsWith('Ti')) {
      result = (Number(result.substring(0, result.length - 2).trim())*1024).toFixed(2) + 'GB'
    }
    // Resource classification:  https://www.yuque.com/kubesys/kube-frontend/ipnl6c
    else if (result === 'local') {
      result = '本地服务器'
    } else if (result === 'cloud') {
      result = '公有云资源'
    } else if (result === 'edge') {
      result = '边缘端设备'
    } else if (result === 'leader') {
      result = '主控节点'
    } else if (result === 'worker') {
      result = '工作节点'
    } else if (result === 'no-schedule') {
      result = '正在维护'
    } else if (result === 'schedule') {
      result = '正在工作'
    }
    return result
  }
}

export function getFormDataValue(data:any, newArray:any){
  if(data.kind === 'ConfigMap'){
    const newArrays = newArray
    getResource({
      fullkind: "ConfigMap",
      name: data.name,
      namespace: data.namespace,
      region: "test"
    }).then((resp)=>{
      const cfg = resp.data.data.data
      newArray = Object.keys(cfg).map(key => {
        return { value: key, label: cfg[key] };
      });
      console.log(newArray)
      return newArray
    })
    console.log(newArray)
    return [
      {
        "value": "Completed",
        "label": "执行完成"
      },
      {
        "value": "Failed",
        "label": "执行失败"
      },
      {
        "value": "Pending",
        "label": "挂起中"
      },
      {
        "value": "Running",
        "label": "运行中"
      },
      {
        "value": "Terminating",
        "label": "销毁中"
      },
      {
        "value": "Unknown",
        "label": "未知状态"
      }
    ]
  }
}
