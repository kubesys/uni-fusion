<template>
    <header class="header">
        <div class="axu">
            <div class="navbar">
                <div class="flex-1 flex">
                    <div class="navbar-item">
                        <fold />
                    </div>
                    <div class="navbar-item">
                        <allMenu />
                    </div>
                    <div class="navbar-item">
                        <refresh />
                    </div>
                    <div class="flex items-center px-2" v-if="!isMobile">
                        <breadcrumb />
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
    box-shadow: 0 0px 2px rgba(0,0,0,0.5);
}
</style>
