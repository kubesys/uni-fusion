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
                      <a-menu v-model:selectedKeys="current" mode="horizontal" :items="items"  @click="handleclick">
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
const appStore = useAppStore()
const isMobile = computed(() => appStore.isMobile)

const settingStore = useSettingStore()
const sideTheme = computed(() => settingStore.sideTheme)

const router = useRouter();
const current = ref<string[]>(['setting:1']);


const items = ref([
  {
    key: 'mail',
    label: '首页',
    title: '首页',
    routes: '/home/dashboard'
  },
  {
    key: 'sub1',
    label: '资源中心',
    title: '资源中心',
    children: [
      {
        label: '云资源池',
        key: 'setting:1',
      },
      {
        label: '硬件设施',
        key: 'setting:2',
      },
      {
        label: '网络资源',
        key: 'setting:3',
      },
      {
        label: '网络服务',
        key: 'setting:4',
      }
    ]
  },
  {
    key: 'sub2',
    label: '平台运维',
    title: '平台运维',
    children: [
      {
        label: '消息日志',
        key: 'setting:1',
      }
    ]
  },
  {
    key: 'alipay',
    label: h('a', { href: 'https://antdv.com', target: '_blank' }, '设置'),
    title: '设置',
  },
]);

function handleclick(){
  router.push('/home/dashboard');
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
