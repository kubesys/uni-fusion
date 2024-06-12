<template>
  <div  >
    <div class="create-title" style="font-size: 16px; padding: 10px">
      <image-contain src='/menu1.png'  style="flex: 0.5; margin-left: 15px; margin-bottom: 15px"/>
      <span style="flex: 2; margin-left: 10px;margin-top: 10px">{{ userStore.menuItem[0].name }}</span>
    </div>
    <a-menu
        v-model:openKeys="openKeys"
        v-model:selectedKeys="selectedKeys"
        style="width: 256px"
        mode="inline"
    >
      <template v-for="menu of userStore.menuItem">

        <a-menu-item-group v-if="menu.children" :key="menu.name" :title="menu.name">
          <a-menu-item v-for="item of menu.children" :key="item.name">
            <template #icon><a-icon :icon="item.icon" /></template>
            <router-link v-if="item.paths" :to="item.paths">{{item.name}}</router-link>
          </a-menu-item>
        </a-menu-item-group>

      </template>
    </a-menu>

    <div style="padding: 15px;  position: fixed;  bottom: 0;">
      <div  style="margin-left: 80px">
        <fold />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { getNormalPath, objectToQuery } from '@/utils/util'
import { isExternal } from '@/utils/validate'
import type { RouteRecordRaw } from 'vue-router'
import Fold from "@/layout/default/components/header/fold.vue";
import { reactive, ref, watch, VueElement, h } from 'vue';
import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue';
import type { MenuProps, ItemType } from 'ant-design-vue';
import AIcon from './AIcon'
import {
  DesktopOutlined,
  PieChartOutlined,
  FolderViewOutlined,
  SaveOutlined,
  FundProjectionScreenOutlined,
  FileExcelOutlined
} from '@ant-design/icons-vue';
import useUserStore from '@/stores/modules/user'

const userStore = useUserStore()
const selectedKeys = ref<string[]>(['1']);
const openKeys = ref<string[]>(['sub1']);

function getItem(
    label: VueElement | string,
    key: string,
    icon?: any,
    children?: ItemType[],
    type?: 'group',
): ItemType {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as ItemType;
}

// interface Props {
//     route: RouteRecordRaw
//     routePath: string
//     popperClass: string
// }
//
// const props = defineProps<Props>()

// const hasShowChild = computed(() => {
//     const children: RouteRecordRaw[] = props.route.children ?? []
//     return !!children.length
// })

// const routeMeta = computed(() => {
//     return props.route.meta
// })

const items: ItemType[] = reactive([
  getItem('虚拟资源', 'grp', null, [getItem('云主机', '1', h(DesktopOutlined)), getItem('云盘', '2', h(SaveOutlined))], 'group'),
  getItem('计算配置', 'grp', null, [ getItem('云盘镜像', '4', h(PieChartOutlined)), getItem('计算规格', '5', h(FundProjectionScreenOutlined)), getItem('云盘规格', '6', h(FileExcelOutlined))], 'group'),
  getItem('资源服务', 'grp', null, [getItem('云主机快照', '7', h(FolderViewOutlined)), getItem('云盘快照', '7', h(FolderViewOutlined))], 'group')
]);

// const resolvePath = (path: string) => {
//     if (isExternal(path)) {
//         return path
//     }
//     const newPath = getNormalPath(`${props.routePath}/${path}`)
//     return newPath
// }
// const queryStr = computed<string>(() => {
//     const query = props.route.meta?.query as string
//     try {
//         const queryObj = JSON.parse(query)
//         return objectToQuery(queryObj)
//     } catch (error) {
//         // console.log(error)
//
//         return query
//     }
// })

</script>
<style lang="scss" scoped>
.el-menu-item,
.el-sub-menu__title {
  .menu-item-icon {
    margin-right: 8px;
    width: var(--el-menu-icon-width);
    text-align: center;
    vertical-align: middle;
  }
}

.create-title {
  display: flex;
}
</style>
