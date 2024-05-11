<template>
    <div class="logo">
        <div class="font-size-style">
<!--          <img src='src/assets/images/logo.png'>-->
<!--          <img :width="szie" :height="szie" src='src/assets/images/logo.png' />-->
          基于国产软硬件的云原生基础平台
        </div>
    </div>
</template>

<script setup lang="ts">
import useAppStore from '@/stores/modules/app'
import { ThemeEnum } from '@/enums/appEnums'
defineProps({
    szie: { type: Number, default: 34 },
    title: { type: String },
    theme: { type: String },
    showTitle: { type: Boolean, default: true }
})
const appStore = useAppStore()
const config = computed(() => appStore.config)
</script>
<style lang="scss" scoped>
.logo {

    height: var(--navbar-height);
    overflow: hidden;
    @apply flex items-center p-2 relative;
    .logo-title {
        width: 70%;
        position: absolute;
        @apply text-xl;
    }

    .title-width-enter-active {
        opacity: 0;
        transition: all 0.3s ease-out;
    }

    .title-width-leave-active {
        transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
    }

    .title-width-enter-from,
    .title-width-leave-to {
        width: 0;
        opacity: 0;
    }
}

.font-size-style{
    font-size: 16px;
    font-weight: bold;
    color: #eeeeee;
    font-family: serif;

}


</style>
