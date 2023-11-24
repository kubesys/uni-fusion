import axios from "@/api/request";

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
