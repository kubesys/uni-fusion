<template>
    <header class="header">
        <div class="axu">
            <div class="navbar ">
                <side-logo :show-title="!isCollapsed" :theme="sideTheme" class="flex-1"/>
                <div class="flex">
                    <div class="navbar-item">
                        <region />
                    </div>
                    <div class="navbar-item" v-if="!isMobile">
                        <full-screen />
                    </div>
                    <div class="navbar-item">
                        <user-drop-down />
                    </div>
                </div>

            </div>
        </div>
    </header>
</template>

<script setup lang="ts" >
import useAppStore from '@/stores/modules/app'
import Region from './region.vue'
import FullScreen from './full-screen.vue'
import UserDropDown from './user-drop-down.vue'
import Setting from '../setting/index.vue'
import SideLogo from './logo.vue'

import useSettingStore from '@/stores/modules/setting'
const appStore = useAppStore()
const isMobile = computed(() => appStore.isMobile)
const settingStore = useSettingStore()

const sideTheme = computed(() => settingStore.sideTheme)
const isCollapsed = computed(() => {
    if (appStore.isMobile) {
        return false
    } else {
        return appStore.isCollapsed
    }
})
</script>

<style lang="scss" scoped >
.axu {
    position: relative;
}

.navbar{
    background-color: var(--side-dark-color, #0043B1);
    height: 50px;
}

.all-search{

    float: left;
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */

}

.all-search input{
    width: 400px;
    height: 30px;
    float: left;
    border:1px solid #052E82;
    outline: none;
    background: #052E82;
}

.button-container{
    margin-top: 10px;
}
</style>
