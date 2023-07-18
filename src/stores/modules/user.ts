import { defineStore } from 'pinia'
import cache from '@/utils/cache'
import type { RouteRecordRaw } from 'vue-router'
import { getUserInfo, login, logout, getMenu } from '@/api/user'
import router, { filterAsyncRoutes } from '@/router'
import { TOKEN_KEY } from '@/enums/cacheEnums'
import { PageEnum } from '@/enums/pageEnum'
import { clearAuthInfo, getToken } from '@/utils/auth'
export interface UserState {
    token: string
    userInfo: Record<string, any>
    routes: RouteRecordRaw[]
    menu: any[]
    perms: string[]
}

const useUserStore = defineStore({
    id: 'user',
    state: (): UserState => ({
        token: getToken() || '',
        // 用户信息
        userInfo: {},
        // 路由
        routes: [],
        menu: [],
        // 权限
        perms: []
    }),
    getters: {},
    actions: {
        resetState() {
            this.token = ''
            this.userInfo = {}
            this.perms = []
        },
        login(playload: any) {
            const { account, password } = playload
            const encodedPassword = btoa(password);
            return new Promise((resolve, reject) => {
                login({
                    name: account,
                    password: encodedPassword
                    // password: encodedPassword
                })
                    .then((data) => {
                        console.log(data.data.data)
                        // this.token = data
                        cache.set(TOKEN_KEY, data.data.data)
                        localStorage.setItem('token', data.data.data)
                        resolve(data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            })
        },
        logout() {
            return new Promise((resolve, reject) => {
                logout()
                    .then(async (data) => {
                        this.token = ''
                        await router.push(PageEnum.LOGIN)
                        clearAuthInfo()
                        resolve(data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            })
        },
        getUserInfo() {
            return new Promise((resolve, reject) => {
                getUserInfo()
                    .then((data) => {
                        this.userInfo = data.user
                        this.perms = data.permissions
                        resolve(data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            })
        },
        getMenu() {
            return new Promise((resolve, reject) => {
                getMenu()
                    .then((data) => {
                        console.log(data.data.data.spec.data)
                        this.menu = data.data.data.spec.data
                        this.routes = filterAsyncRoutes(data.data.data.spec.data)
                        resolve(data.data.data.spec.data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            })
        }
    }
})

export default useUserStore
