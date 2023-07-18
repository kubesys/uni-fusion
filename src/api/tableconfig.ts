import axios from "./request";

// 页面功能描述
export function getTableDesc() {
    return axios.post( '/kubesys/kube/getResource',{
        fullkind:"doslab.io.Frontend",
        name:"apps.replicaset-desc",
        namespace:"default",
        region:"test"
    })
}

export function getTableColumns() {
    return axios.post( '/kubesys/kube/getResource',{
        fullkind:"doslab.io.Frontend",
        name:"apps.replicaset-table",
        namespace:"default",
        region:"test"
    })
}

export function getTableData() {
    return axios.post( '/kubesys/kube/listResources', {
        fullkind: "apps.ReplicaSet",
        page: 1,
        limit: 10,
        labels: {},
        region: "test"
    })
}




