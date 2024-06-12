<template>
  <div class="navbar-item allMenu cursor-pointer h-full flex items-center px-2">
    <div>
      <i class="iconfont icon-iconfont2"></i>
      <div class="nav-bar-list">
        <div class="search">
          <button class="iconfont icon-icon_sousuo"></button>
          <input type="text" placeholder="  搜索">
        </div>
        <div class="showData" v-for="catalog in userStore.catalogs" :key="catalog.name">

          <a-divider style="border-color: #dbdde0; font-size: 12px" orientation="left" orientation-margin="0px">
            <span style="color: #96989b">{{ catalog.name }}</span></a-divider>

          <!--          <div class="dataItems">-->
          <!--            <el-row >-->
          <!--              <el-col v-for="data in row.items" :key="data.index" :span=data.span>-->
          <!--                {{data.name}}<div class="dataItems-main" v-for="items in data.classify">{{items}}</div>-->
          <!--              </el-col>-->
          <!--            </el-row>-->
          <!--          </div>-->
          <div class="dataItems" v-for="item in gropsItems" >
            <!--                        <a :href="`#${catalog.path}`" @click="handleCatalogClick(catalog)">{{ catalog.name }}</a>-->

            <div class="showData_inner" v-for="group in item" :key="group.name">
              <span v-if="group.path.startsWith(catalog.path)">
                <div style="font-size: 14px; font-weight: bolder">
                  {{group.name}}
                </div>

                <div class="showData_inner_inner" v-for="item in userStore.menu" :key="item.name">
                  <router-link v-if="item.path.startsWith(group.path)" :to=item.path @select="handleMenuClick">
                    {{item.name}}
                  </router-link>
                </div>
              </span>
            </div>
            <!--            <div @click="handleCatalogClick(catalog)">{{ catalog.name }}</div>-->
            <!--            <div class="dataItems-main" v-for="menu in userStore.catalogs">{{ menu.routes }}</div>-->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import useUserStore from "@/stores/modules/user";

const userStore = useUserStore()
const routes = useRoute()
const title = routes.meta?.title
const gropsItems = ref([])

const groupByPath = (groups:any) => {
  const grouped = {};
  groups.forEach((group:any) => {
    const path = group.path.split('/')[1];
    if (!grouped[path]) {
      grouped[path] = [];
    }
    grouped[path].push(group);
  });
  return Object.values(grouped);
};

onMounted(() => {
  gropsItems.value = groupByPath(userStore.groups);
});



const handleCatalogClick = (catalog) => {
  // 更新 Store 中选中的catalog
  // console.log(catalog)
  userStore.setSelectedCatalog(catalog.path);
  userStore.getMenu()
};

function handleMenuClick(info:any){
  // console.log(info.key)
  for (const item of userStore.sideItems) {
    console.log(item);
    if(item.name == info.key){
      userStore.menuItem = []
      userStore.menuItem.push(item)
      // console.log(userStore.menuItem)
    }
  }
}

</script>


<style>
.nav-bar-list {
  width: 750px;
  height: auto;
  background: #ffffff;
  border: 1px;
  border-top: 1px solid #eeeeee;
  box-shadow: 0 3px 4px rgba(0, 0, 0, 0.5);
  position: absolute;
  left: 0;
  top: 55px;
  display: none;
  z-index: 10;
  line-height: normal;
  padding-left: 20px;
  padding-bottom: 30px;
}

.navbar-item div:hover > .nav-bar-list {
  display: block;
}

.search {
  width: 100%;
  height: 50px;
  float: left;
  margin-top: 20px;
}

.search input {
  width: 244px;
  height: 30px;
  float: left;
  border: 1px solid #d2d2d2;
  background-color: transparent;
  outline: none;
}

.search button {
  width: 30px;
  height: 30px;
  float: left;
  border: 1px solid #d2d2d2;
}

.showData {
  margin-right: 20px;
}

.showData_inner{
  flex: 1;
  font-size: 16px;
  font-weight: normal;
}

.showData_inner_inner{
  margin-top: 10px;
  font-size: 13px;
  color: #707275;
}


.dataItems {
  color: #0a0a0a;
  width: 100%;
  float: left;
  margin-bottom: 5px;
  margin-left: 20px;

  display: flex;
}

.dataItems-main {
  margin-top: 10px;
  font-weight: normal;
  color: #58585b;
}

</style>
