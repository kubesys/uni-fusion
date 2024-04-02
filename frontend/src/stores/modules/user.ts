import { defineStore } from 'pinia'
import cache from '@/utils/cache'
import type { RouteRecordRaw } from 'vue-router'
import { getUserInfo, login, logout, getMenu } from '@/api/user'
import router, { filterAsyncRoutes } from '@/router'
import { TOKEN_KEY } from '@/enums/cacheEnums'
import { PageEnum } from '@/enums/pageEnum'
import { clearAuthInfo, getToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'
export interface UserState {
    token: string
    userInfo: Record<string, any>
    routes: RouteRecordRaw[]
    menu: any[]
    perms: string[]
    catalogs: any[],
    groups: any[],
    sideItems: any[],
    menuItem: any[],
    selectedCatalog: string
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
        perms: [],
        catalogs: [],
        groups: [],
        sideItems: [],
        menuItem: [],
        selectedCatalog: ''
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
                        if(data.data.code == 20000){
                            console.log(data.data.data)
                            ElMessage.success('登录成功！')
                            // this.token = data
                            cache.set(TOKEN_KEY, data.data.data)
                            resolve(data)
                        } else if (data.data.exId == 300){
                            ElMessage.error('账号或密码错误，请检查！')
                        } else {
                            ElMessage.error('lack of kind')
                        }
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
                        this.catalogs = data.data.data.spec.catalogs
                        this.groups = data.data.data.spec.groups
                        this.menu = data.data.data.spec.items
                        // const routepath = this.selectedCatalog === ''? this.catalogs[0].path : this.selectedCatalog
                        // this.getRoutes(data.data.data.spec.items, routepath)


                        // if (this.selectedCatalog === ''){
                        //     this.getRoutes(data.data.data.spec.items)
                        // } else {
                        //     console.log(data.data.data.spec.items)
                        //     this.getRoutes(data.data.data.spec.items, this.selectedCatalog)
                        // }
                        const mergedMenu = this.getRoutes(data.data.data.spec.items, this.selectedCatalog)
                        this.routes = filterAsyncRoutes(mergedMenu)
                        resolve(data.data.data.spec.data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            })
        },
        /**
         * https://system-iscas.yuque.com/org-wiki-system-iscas-os28is/htugy3/aitk5capwdhocdlk
         * @param items
         * @param path
         */
        getRoutes(items:[] , path = this.catalogs[0].path){
            // const routes:any[] = items.filter(item => item.paths.startsWith(path))
            const routes:any[] = items
            console.log(routes)

            const mergedMenu = this.groups.map(mainItem => {
                const matchingSubMenu = routes.filter(subItem => subItem.path.startsWith(mainItem.path));
                console.log(matchingSubMenu)
                return {
                    name: mainItem.name,
                    menuType: "M",
                    paths: mainItem.path,
                    children: matchingSubMenu.map(subItem => {
                        if(subItem.source){
                            return {
                                name: subItem.name,
                                label: subItem.label,
                                icon: subItem.icon,
                                component: subItem.component,
                                paths: subItem.path.replace(mainItem.path, '').replace(/^\//, ''),
                                menuType: "C",
                                source: subItem.source
                            }
                        } else {
                            return {
                                name: subItem.name,
                                label: subItem.label,
                                icon: subItem.icon,
                                component: subItem.component,
                                paths: subItem.path.replace(mainItem.path, '').replace(/^\//, ''),
                                menuType: "C",
                                Kind: subItem.kind,
                                kind: subItem.kind.toLowerCase(),
                                structure: subItem.structure,
                                filter: subItem.filter
                            };
                        }
                    })
                };
            });
            console.log(mergedMenu)
            this.sideItems = mergedMenu
            this.menuItem.push(mergedMenu[0])
            return mergedMenu
        },
        setSelectedCatalog(catalogPath:string) {
            this.selectedCatalog = catalogPath;
            console.log(this.selectedCatalog)
        }
    }
})

export default useUserStore
