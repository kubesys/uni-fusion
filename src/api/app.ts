import request from '@/utils/request'
import axios from "axios";

// 配置
export function getConfig() {
    return request.get({ url: 'http://localhost:5173/common/index/config' })
}

// 工作台主页
export function getWorkbench() {
    return request.get({ url: '/common/index/console' })
}

export function getTableData() {
    return axios.get( 'http://localhost:5173/cloudHostings')
}
