import config from '@/config'
import request from '@/utils/request'
import axios from "./request";

// 登录
export function login(params: Record<string, any>) {
    return axios.post( 'http://localhost:5173/kubesys/system/login',{
        kind: 'User',
        data: {
            name: params.name,
            password: params.password
        }
    })
}

// 退出登录
export function logout() {
    return request.post({ url: '/kubesys/system/logout' })
}

// 用户信息
export function getUserInfo() {
    return request.get({ url: 'http://localhost:5173//system/admin/self' })
}

// 菜单路由
export function getMenu() {
    return axios.post( 'http://localhost:5173/kubesys/kube/route-test',{
        fullkind:"doslab.io.Frontend",
        name:"all-routes-test",
        namespace:"default",
        region:"local"
    })
}

// all-regions
export function getRegions(){
    return axios.post( '/kubesys/kube/getResource',{
        fullkind:"doslab.io.Frontend",
        name:"all-regions",
        namespace:"default",
        region:"local"
    })
}

// 编辑管理员信息
export function setUserInfo(params: any) {
    return request.post({ url: '/system/admin/upInfo', params })
}

