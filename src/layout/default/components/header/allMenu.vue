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
          <div class="line" />
<!--          <div class="dataItems">-->
<!--            <el-row >-->
<!--              <el-col v-for="data in row.items" :key="data.index" :span=data.span>-->
<!--                {{data.name}}<div class="dataItems-main" v-for="items in data.classify">{{items}}</div>-->
<!--              </el-col>-->
<!--            </el-row>-->
<!--          </div>-->
          <div class="dataItems" >
            <!--                        <a :href="`#${catalog.path}`" @click="handleCatalogClick(catalog)">{{ catalog.name }}</a>-->
            <router-link :to=routes.path @click="handleCatalogClick(catalog)">{{ catalog.name }}</router-link>
<!--            <div @click="handleCatalogClick(catalog)">{{ catalog.name }}</div>-->
<!--            <div class="dataItems-main" v-for="menu in userStore.catalogs">{{ menu.routes }}</div>-->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, provide, ref} from 'vue';
import {getMenu} from '@/api/user';
import useUserStore from "@/stores/modules/user";

const userStore = useUserStore()

const routes = useRoute()

const handleCatalogClick = (catalog) => {
  // 更新 Store 中选中的catalog
  console.log(catalog)
  userStore.setSelectedCatalog(catalog.path);
  userStore.getMenu()
};

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

.showData .title {
  color: #a8abb2;
  font-size: 10px;
  float: left;
  margin-top: 15px;;
}

.line {
  float: left;
  margin-top: 24px;
  margin-left: 10px;
  width: 600px;
  height: 1px;
  background: #c0c4cc;
}

.dataItems {
  width: 100%;
  float: left;
  margin-top: 20px;
  margin-left: 20px;
  font-weight: 700;
}

.dataItems-main {
  margin-top: 10px;
  font-weight: normal;
  color: #58585b;
}

</style>
