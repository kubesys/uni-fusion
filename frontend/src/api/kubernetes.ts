import axios from "@/api/request";

// export function getResourcetable(params : any) {
//     return axios.post( 'http://localhost:5173/kubesys/kube/getResource/table', params)
// }
//
// export function getResourcedesc(params : any) {
//     return axios.post( 'http://localhost:5173/kubesys/kube/getResource/desc', params)
// }
//
// export function getResourceformsearch(params : any) {
//     return axios.post( 'http://localhost:5173/kubesys/kube/getResource/formsearch', params)
// }

export function getResource(params : any) {
    return axios.post( '/kubesys/kube/getResource', params)
}

export function listResources(params : any) {
    return axios.post( '/kubesys/kube/listResources', params)
}

export function updateResource(params : any){
    return axios.post( '/kubesys/kube/updateResource', params)
}

export function createResource(params: any){
    return axios.post('/kubesys/kube/createResource', params)
}

export function deleteResource(params: any){
    return axios.post('/kubesys/kube/deleteResource', params)
}
