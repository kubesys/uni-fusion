import {
  getResource,
  listResources,
  updateResource,
  createResource,
  deleteResource,
} from "@/api/kubernetes"
import { ElMessage } from 'element-plus'
import { message } from 'ant-design-vue';

export function frontendData(ListName:string, TableName:string, pageSite:object, tableColumns:[], tableData:[], allLabels:object, actions:[] = [],  region='local', retryCount = 10 ){
  const getResourceData = (retry:any) => {
    listResources({
      fullkind: ListName,
      page: pageSite.value.page,
      limit: pageSite.value.limit,
      labels: allLabels,
      region: region
    }).then((resp) => {
      resp.data.data.items
      tableData.value = resp.data.data;

      /***********************
       *
       * Echarts data
       *
       ***********************/
      var resultRun = 3
      var resultPen = 0
      tableData.value.items.forEach((item: any) => {
        if (item.status !== undefined && item.status.phase === 'Running') {
          resultRun++
        } else if (item.status !== undefined && item.status.phase !== 'Running') {
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
        tableColumns.value = resp.data.data.spec.data;

      }).catch((error) => {
        if (retry < retryCount) {
          getResourceData(retry + 1);
        } else {
          ElMessage.error('Request failed' + TableName + '-table');
        }
      });
    }).catch((error) => {
      if (retry < retryCount) {
        getResourceData(retry + 1);
      } else {
        getResource({
          fullkind: "doslab.io.Frontend",
          name: TableName + '-table',
          namespace: "default",
          region: region
        }).then((resp) => {
          tableColumns.value = resp.data.data.spec.data;
        })
      }
    });
  }

  getResourceData(1);
}

export function frontendMeta(TableName:string, descItem: [], region = 'local', retryCount = 10) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-desc',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          descItem.value = resp.data.data.spec;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else{
            ElMessage.error('Request failed' + TableName + '-desc');
          }
        });
  };

  getResourceData(1);
}

export function frontendFormSearch(TableName:string, formItem: [], buttonItem: [], region = 'local', retryCount = 10) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-formsearch',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          formItem.value = resp.data.data.spec.data.items;
          buttonItem.value = resp.data.data.spec.data.buttons;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1); // 重新发送
          } else {
            ElMessage.error('Request failed,' + TableName + 'formsearch');
          }
        });
  };

  getResourceData(1);
}

export function frontendInfo(TableName:string, infoItem: [], region = 'local', retryCount = 10) {
  const getResourceData = (retry:any) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: TableName + '-info',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          infoItem.value = resp.data.data.spec;
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else{
            ElMessage.error('Request failed' + TableName + '-info');
          }
        });
  };

  getResourceData(1);
}

// export function frontendAction(TableName:string, step: [], region = 'local', retryCount = 3) {
//   const getResourceData = (retry:any) => {
//     getResource({
//       fullkind: "doslab.io.Frontend",
//       name: TableName + '-action-scale',
//       namespace: "default",
//       region: region
//     })
//         .then((resp) => {
//           console.log(resp.data.data.spec);
//           step.value = resp.data.data.spec;
//         })
//         .catch((error) => {
//           console.error(error);
//           if (retry < retryCount) {
//             getResourceData(retry + 1);
//           } else {
//             console.error('Request failed.');
//           }
//         });
//   };
//
//   getResourceData(1);
// }

export function frontendCreateTemplate(TableName:string, templateSpec: any, obj: any, region = 'local', retryCount = 3) {
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

          const jsontemplate = {}
          obj.value = { ...templateSpec.value.template, ...jsontemplate }

          for (const stepKey in templateSpec.value.data) {
            if (Object.hasOwnProperty.call(templateSpec.value.data, stepKey)) {
              const step = templateSpec.value.data[stepKey];

              // 遍历每个分组
              for (const groupKey in step) {
                if (Object.hasOwnProperty.call(step, groupKey)) {
                  const group = step[groupKey];

                  // 检查是否存在 variables 属性
                  if (group.variables) {
                    // 遍历每个变量
                    for (const variableKey in group.variables) {
                      if (Object.hasOwnProperty.call(group.variables, variableKey)) {
                        let newObj = {};  // 每次迭代创建一个新的对象
                        const arr1 = variableKey.split(".");
                        for (let index = arr1.length - 1; index >= 0; index--) {
                          if (index === arr1.length - 1) {
                            newObj = {
                              [arr1[index]]: ''
                            };
                          } else {
                            newObj = {
                              [arr1[index]]: newObj
                            };
                          }
                        }

                        obj.value = assiginObj(newObj, obj.value);
                      }
                    }
                  }
                }
              }
            }
          }
          console.log(obj)
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            getResourceData(retry + 1);
          } else {
            ElMessage.error('lack of ' + TableName + '-kind!');
          }
        });
  };

  getResourceData(1);
}

export function frontendUpdate(rowData:object, region = 'local', retryCount = 3) {
  const updateResourceData = (retry:any) => {
    updateResource({
      region: region,
      data: rowData
    })
        .then((resp) => {
          if (resp.data.code == '50000') {
            ElMessage({
              duration: 6000,
              message: resp.data,
              type: 'error',
            })
          }
        })
        .catch((error) => {
          console.error(error);
          if (retry < retryCount) {
            updateResourceData(retry + 1);
          } else {
            ElMessage.error('Request failed.');
          }
        });
  };
  updateResourceData(1);
}

export function validResponse(response:any) {
  return response != null && response.hasOwnProperty('code') && response.code === 20000
}

export function frontendCreate(jsonData:any, region = 'local'){
  createResource({
    region: region,
    data: jsonData
  }).then((resp)=>{
    if(resp.data.code == 20000){
      ElMessage.success('创建成功.')
    }
  })
}

export function frontendDelete(Listname:string, name:string, region = 'local'){
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

/******************************************************************************************
 *
 * Tabel various values
 * Url: https://system-iscas.yuque.com/org-wiki-system-iscas-os28is/htugy3/gw06v8ohezh16l3u
 *
 ******************************************************************************************/
export function getComplexDataDispose(scope, rowKey){
  if (rowKey.includes(';/;') && rowKey.indexOf('#') == -1){
    const value = getIncludesValue(scope, rowKey)
    return value
  } else {
    const value = getComplexValue(scope, rowKey)
    return value
  }
}

export function getIncludesValue(scope, key){
  let result = scope
  var arr = []
  key.split(';/;').forEach((item)=>{
    const x = getComplexValue(scope, item)
    arr.push(x)
  })
  arr.join('/')
  return arr.join('/')
}

export function getComplexValue(scope, key) {
  if (JSON.stringify(scope) === '{}' || !key) {
    return ''
  }
  let value = ''
  const strAry = key.split(';')

  if (strAry.length === 1) {

    // convert 1516703495241 to 2018-01-23 18:31:35
    // function time()
    if (key.includes('creationTimestamp')) {
      const value = getTextValue(scope, key)
      const dateObject = new Date(value);
      return  dateObject.toLocaleString();
    } else {
      value = getTextValue(scope, key)
    }

    value = '.' + value
  } else {
    for (let i = 0; i < strAry.length; i++) {
      const longKey = strAry[i]
      if (i%2 === 0) {
        const v = getTextValue(scope, longKey)
        if (strAry[1] === '|') {
          value = v.substring(0) !== '-' ? "." + v : value
        } else {
          value = value + strAry[1] + v.substring(0)
        }
      }
    }
  }
  return value.substring(1)
}

export function getTextValue(scope, key){
  if (JSON.stringify(scope) === '{}' || !key) {
    return '-'
  }

  let result = scope

  if (key.startsWith('@')) {
    let newkey = '';
    key.substring(1).split('+').forEach((item) => {
      if (item.includes('apiVersion')) {
        const apiVersion = result[item];
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
          // console.log(2);
          // 处理 value 为 undefined 或 null 的情况
        }
      }
    });
    // console.log(newkey);
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
        result = '-'
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
    } else if (result === 'Shutdown') {
      result = '🔴'
    } else if (result === 'Active') {
      result = '🟢'
    } else if (result === 'Exception') {
      result = '🔴'
    } else if (result === '400') {
      result = '🔴'
    } else if (result === 'Paused') {
      result = '🟡'
    }
    else if (key.includes('Memory')) {
      result = result/1024/1024 + 'GB'
    }
    else if (result === 'local') {
      result = '本地服务器'
    } else if (result === 'cloud') {
      result = '公有云资源'
    } else if (result === 'edge') {
      result = '边缘端设备'
    } else if (result === 'master') {
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

export function getTerminalAddr(scope, item) {
  let str = item.target
  let n = ''
  if (str.includes('/e/')) {
    item.values.forEach((item)=>{
      const id = getComplexValue(scope, item + '.lastState.terminated.containerID')
      if (typeof(id) == "string") {
        n = id.substring('docker://'.length);
      }
    })
  } else if (str.includes('{port}')) {
    console.log(scope)
    n = getComplexValue(scope, item.row)
    console.log(n)
    // return 'http://133.133.135.134:8081/VmInstance/viewNoVnc?record=' + n
    return 'http://localhost:30301/VmInstance/viewNoVnc?record=' + n
  } else {
    item.values.forEach((item)=>{
      n = getComplexValue(scope, item)
    })
  }
  let nstr = str.replace(/\{[^\}]+\}/,n);
  return nstr
}

export function getPlatformValue(scope, key){
  if (JSON.stringify(scope) === '{}' || !key) {
    return '-'
  }

  let result = scope

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
    if (result.includes('centos')) {
      result = 'Linux'
    } else  {
      result = '⊘'
    }
    return result
  }
}

export function nameChange(name: string) {
  if (name == 'doslab.io.VirtualMachine') {
    return '创建云主机'
  } else if (name == 'doslab.io.VirtualMachineDisk') {
    return '创建云盘'
  } else if (name == 'doslab.io.VirtualMachineDiskImage') {
    return '创建云盘镜像'
  } else if (name == 'doslab.io.VirtualMachineSpec') {
    return '创建计算规格'
  } else if (name == 'doslab.io.VirtualMachineDiskSpec') {
    return '创建云盘规格'
  } else if (name == 'doslab.io.Zone') {
    return '创建区域'
  } else if (name == 'doslab.io.Cluster') {
    return '创建集群'
  } else if (name == 'Node') {
    return '添加物理机'
  } else if (name == 'doslab.io.VirtualMachinePool') {
    return '创建主存储'
  } else if (name == 'doslab.io.VirtualMachineSnapshot') {
    return '创建云主机快照'
  } else if (name == 'doslab.io.VirtualMachineDiskSnapshot') {
    return '创建云盘快照'
  }

}


/******************************************************************************************
 *
 * FormSearch various values
 * Url: https://system-iscas.yuque.com/org-wiki-system-iscas-os28is/htugy3/ndh85sggwxtfw93v
 *
 ******************************************************************************************/
export function ConfigMapValue(data:any,ConfigArray:any){
  getResource({
    fullkind: 'ConfigMap',
    name: data.name,
    namespace: data.namespace,
    region: "test"
  }).then((resp) => {
    const result = resp.data.data.data
    const newArrays = Object.keys(result).map(key => {
      return { value: key, label: result[key] };
    })
    const Arr = []
    for (const key in newArrays) {
      // Vue.set(mapper, key, result[key])
      Arr.push(newArrays[key]);
    }
    ConfigArray.value = Arr
  })
}

export function getFormDataValue(data:any, optionArray:any){
  if(data.kind === 'ConfigMap'){
    ConfigMapValue(data, optionArray)
  } else {
    optionArray.value = [{value: 'ecs-253', label:'ecs-253'}]
  }
}

/******************************************************************************************
 *
 * Action various values
 * Url: https://system-iscas.yuque.com/org-wiki-system-iscas-os28is/htugy3/ndh85sggwxtfw93v
 *
 ******************************************************************************************/
export function actionDataValue(TableName:string, ListName:string, dialogVisible, selectedItemName, rowItemData, dialogname:string, action:string, rowData:any){
  if (action === TableName + '-action-scale') {
    dialogVisible.value = true;
    selectedItemName.value = dialogname
    rowItemData.value = rowData.metadata
  } else if (action === 'DELETE') {
    frontendDelete(ListName, rowData.metadata.name)
  } else if (action === 'UPDATE') {
    dialogVisible.value = true;
    selectedItemName.value = dialogname
    rowItemData.value = rowData.metadata
  } else if (action === 'start') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      startVM: {}
    };
    frontendUpdate(obj)
    message
        .loading( '正在启动', 5)
        .then(
            () => {
              if (rowData.spec.status.conditions.state.waiting.reason == 'Running') {
                message.success(rowData.spec.status.conditions.state.waiting.message, 6)
              } else {
                message.error('启动失败', 6)
              }
            },
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )
  } else if (action === 'stop') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      stopVMForce: {}
    };
    frontendUpdate(obj)
    // frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
    message
        .loading('正在停止', 5)
        .then(
            () => message.success('已关闭。', 6),
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )
  } else if (action === 'suspend') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      suspendVM: {}
    };
    frontendUpdate(obj)
    message
        .loading('正在暂停', 5)
        .then(
            () => message.success('已暂停。', 2.5),
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )
  } else if (action === 'resume') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      resumeVM: {}
    };
    frontendUpdate(obj)
    message
        .loading('正在恢复', 5)
        .then(
            () => message.success('已恢复。', 2.5),
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )
  } else if (action === 'shutdown') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      stopVMForce: {}
    };
    frontendUpdate(obj)
    message
        .loading('正在强制关闭', 5)
        .then(
            () => message.success('已关闭。', 2.5),
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )

  } else if (action === 'UnplugDisk') {
    let obj = {
      "apiVersion": "doslab.io/v1",
      "kind": rowData.kind,
      "metadata": rowData.metadata,
      "spec": rowData.spec
    }
    obj.spec.lifecycle = {
      unplugDisk: {
        target: "Linux: vdb"
      }
    };
    frontendUpdate(obj)
    message
        .loading('正在卸载', 5)
        .then(
            () => message.success('已卸载成功。', 2.5),
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
        )

  }
}

/******************************************************************************************
 *
 * Create various values
 * Url: https://system-iscas.yuque.com/org-wiki-system-iscas-os28is/htugy3/ccxy0kdbtsn023wl
 *
 ******************************************************************************************/
function assiginObj(target = {},sources= {}){
  const obj = target;
  if(typeof target != 'object' || typeof sources != 'object'){
    return sources; // 如果其中一个不是对象 就返回sources
  }
  for(const key in sources){
    // 如果target也存在 那就再次合并
    if(target.hasOwnProperty(key)){
      obj[key] = assiginObj(target[key],sources[key]);
    } else {
      // 不存在就直接添加
      obj[key] = sources[key];
    }
  }
  return obj;
}

export function assignValues(obj1:any, obj2:any) {
  for (const key in obj1) {
    if (Object.prototype.hasOwnProperty.call(obj1, key)) {
      const value = obj1[key];
      assignValue(obj2, value);
    }
  }
}

export function assignValue(obj:any, value:any) {
  obj.metadata.labels.name = value
  console.log(obj)
  frontendUpdate(obj)
  ElMessage.success('更新成功.')
}

