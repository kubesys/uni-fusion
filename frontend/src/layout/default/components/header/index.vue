<template>
  <header class="header" >
    <div class="axu" >
      <div class="navbar" :style="{ color: menuProp.textColor }">
        <div class="flex-1 flex" style="margin-left: 15px">
          <div class="navbar-item">
            <allMenu />
          </div>
          <div>

          </div>
          <div class="navbar-item">
            <refresh />
          </div>
          <div class="flex px-2" v-if="!isMobile" style="margin-top: 5px" >
            <!--                        <breadcrumb />-->
            <a-menu v-model:selectedKeys="current" mode="horizontal" @select="handleTopMenuClick">
              <template v-for="menu in items">
                <a-sub-menu v-if="menu.children" :key="menu.title" :title="menu.title" >
                  <a-menu-item v-for="item in menu.children" :key="item.title">
                    <router-link v-if="item.path" :to="item.path">{{item.title}}</router-link>
                  </a-menu-item>
                </a-sub-menu>
                <a-menu-item v-else :key="menu.title">
                  <router-link v-if="menu.path" :to="menu.path">{{menu.title}}</router-link>
                </a-menu-item>
              </template>
            </a-menu>
          </div>

        </div>
        <div class="flex">
          <!--                    <div class="navbar-item" v-if="!isMobile">-->
          <!--                        <full-screen />-->
          <!--                    </div>-->
          <!--                    <div class="navbar-item">-->
          <!--                        <user-drop-down />-->
          <!--                    </div>-->
          <div class="navbar-item">
            <setting />
          </div>
        </div>
      </div>
      <!--            <multiple-tabs v-if="settingStore.openMultipleTabs" />-->
    </div>
  </header>

</template>

<script setup lang="ts">
import useAppStore from '@/stores/modules/app'
import Fold from './fold.vue'
import AllMenu from './allMenu.vue'
import Refresh from './refresh.vue'
import Breadcrumb from './breadcrumb.vue'
import FullScreen from './full-screen.vue'
import UserDropDown from './user-drop-down.vue'
import Setting from '../setting/index.vue'
import MultipleTabs from './multiple-tabs.vue'

import useSettingStore from '@/stores/modules/setting'

import useUserStore from '@/stores/modules/user'

const userStore = useUserStore()
const appStore = useAppStore()
const isMobile = computed(() => appStore.isMobile)

const settingStore = useSettingStore()
const sideTheme = computed(() => settingStore.sideTheme)

const router = useRouter();
const current = ref<string[]>(['setting:1']);

// const items = ref([
//   {
//     title: '首页',
//     path: '/home/dashboard'
//   },
//   {
//     key: 'sub1',
//     label: '资源中心',
//     title: '资源中心',
//     children: [
//       {
//         title: '云资源池',
//         path: '/envInfo/basicInfo/vm',
//       },
//       {
//         title: '硬件设施',
//         path: '/envInfo/hardware/zone',
//       },
//       {
//         title: '网络资源',
//         path: '/envInfo/resource/layer2Net',
//       },
//       {
//         title: '平台管理',
//         path: '/envInfo/platform/workload',
//       }
//     ]
//   },
//   {
//     title: '平台运维',
//     children: [
//       {
//         title: '消息日志',
//         path: 'setting:1',
//       }
//     ]
//   },
//   {
//     title: '设置',
//     path: '/config'
//   },
// ]);

const items = ref([
  {
    title: '首页',
    path: '/home/dashboard'
  },
  {
    key: 'sub1',
    label: '资源中心',
    title: '资源中心',
    children: [
      {
        title: '平台管理',
        path: '/envInfo/platform/workload',
      }
    ]
  }
]);

// function handleclick(){
//   router.push('/home/dashboard');
// }
function handleTopMenuClick(info:any){
  console.log(info.key)
  for (const item of userStore.sideItems) {
    if(item.name == info.key){
      userStore.menuItem = []
      userStore.menuItem.push(item)
      console.log(userStore.menuItem)
      appStore.refreshView()
    }
  }
}

const menuProp = computed(() => {
  return {
    backgroundColor: sideTheme.value == 'dark' ? settingStore.sideDarkColor : '',
    textColor: sideTheme.value == 'dark' ? 'var(--el-color-white)' : '',
    activeTextColor: sideTheme.value == 'dark' ? 'var(--el-color-white)' : ''
  }
})
</script>

<style lang="scss">
.header{
  z-index: 9;
}

.axu {
  position: relative;
}

.navbar {
  height: var(--navbar-height);
  @apply flex px-2 bg-body;
  .navbar-item {
    @apply h-full  flex justify-center items-center  hover:bg-page;
    line-height: 90px;
  }
  background-color: var(--side-dark-color, var(--el-bg-color));
  box-shadow: 0 0px 2px rgba(0,0,0,0.5);
}
</style>
