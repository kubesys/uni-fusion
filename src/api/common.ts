import { getResource, listResources, updateResource } from "@/api/kubernetes"

export function frontendData(listname, tablename, pageSite, tableColumns:[], tableData:[], actions:[], props='', region='test'){
  const MAX_RETRIES = 10;
  const fetchData = (retryCount = 0) => {
    if (retryCount >= MAX_RETRIES) {
      console.error("Maximum retry count reached.");
      return;
    }

    listResources({
      fullkind: listname,
      page: pageSite.value.page,
      limit: pageSite.value.limit,
      labels: {
        "metadata##name": props
      },
      region: region
    }).then((resp)=>{
      console.log(resp.data.data.items);
      tableData.value = resp.data.data;

      getResource({
        fullkind: "doslab.io.Frontend",
        name: tablename + '-table',
        namespace: "default",
        region: region
      }).then((resp) => {
        console.log(resp.data.data.spec.data);
        tableColumns.value = resp.data.data.spec.data;

        getResource({
          fullkind: "doslab.io.Frontend",
          name: tablename + '-action',
          namespace: "default",
          region: region
        }).then((resp)=>{
          console.log(resp.data.data.spec.data)
          actions.value = resp.data.data.spec.data
        }).catch((error)=>{
          fetchData(retryCount+1);
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

export function frontendMeta(tablename, descItem: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: tablename + '-desc',
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

export function frontendFormSearch(tablename, descItem: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: tablename + '-formsearch',
      namespace: "default",
      region: region
    })
        .then((resp) => {
          console.log(resp.data.data.spec.data.items);
          descItem.value = resp.data.data.spec.data.items;
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

export function frontendAction(tablename, step: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: tablename + '-action-scale',
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

export function frontendCreate(tablename, step: [], region = 'test', retryCount = 3) {
  const getResourceData = (retry) => {
    getResource({
      fullkind: "doslab.io.Frontend",
      name: tablename + '-create',
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

export function frontendUpdate(rowData, region = 'test', retryCount = 3) {
  const updateResourceData = (retry) => {
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
