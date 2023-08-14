<template>
  <div class="navbar-item allMenu cursor-pointer h-full flex items-center px-2">
    <div>
      <i class="iconfont icon-iconfont2"></i>
      <div class="nav-bar-list">
        <div class="search">
          <button class="iconfont icon-icon_sousuo"></button>
          <input type="text" placeholder="  搜索">
        </div>
        <div class="showData">
          <div class="title" v-for="catalog in allMenuItems" :key="catalog.name">
            <!--                        <a :href="`#${catalog.path}`" @click="handleCatalogClick(catalog)">{{ catalog.name }}</a>-->
            <router-link :to=catalog.path @click="handleCatalogClick(catalog)">{{ catalog.name }}</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, provide, ref} from 'vue';
import {getMenu} from '@/api/user';
import useMenuStore from '@/stores/modules/store';

const menuStore = useMenuStore();

const allMenuItems = ref([]);

const fetchAllMenuItems = async () => {
  try {
    const resp = await getMenu();
    allMenuItems.value = resp.data.data.spec.catalogs;
  } catch (error) {
    console.error('Error fetching all menu items:', error);
  }
};

const handleCatalogClick = (catalog) => {
  // 更新 Store 中选中的目录
  console.log(catalog)
  menuStore.setSelectedCatalog(catalog.path);
};

onMounted(fetchAllMenuItems);
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
