<template>
    <div class="all_header" :style="sideStyle" >
        <layout-navigation />
        <div class="app-header " >
            <layout-header />
        </div>
    </div>
    <div class="layout-default flex h-screen w-full">
        <div class="app-aside">
            <layout-sidebar />
        </div>


        <div class="flex-1  flex-col min-w-0">


            <div class="app-main flex-1 min-h-0">
                <layout-main />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import LayoutMain from './components/main.vue'
import LayoutSidebar from './components/sidebar/index.vue'
import LayoutHeader from './components/header/index.vue'
import LayoutNavigation from './components/navigation/index.vue'
import useSettingStore from "@/stores/modules/setting";

const settingStore = useSettingStore()
const sideTheme = computed(() => settingStore.sideTheme)
const sideStyle = computed(() => {
  return sideTheme.value == 'dark'
      ? {
        '--side-dark-color': settingStore.sideDarkColor
      }
      : ''
})


</script>
