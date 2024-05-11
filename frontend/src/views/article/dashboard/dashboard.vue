<template>

  <div style="height: 100vh;overflow: scroll;background: #f5f7fa">
    <div >

    </div>

    <div class="layout-box">
      <a-button style="margin-bottom: 10px"  @click="addGridLayout">
        添加组件
      </a-button>
      <grid-layout
          v-model:data="layout.data"
          :isDrawGridLines="false"
          :isCollision="false"
          :remove="false"
          :resize="false"
          :gutter="20"
          @draggableStart="draggableStart"
          @draggableHandle="draggableHandle"
          @draggableEnd="draggableEnd"
          @remove="remove"
      >
        <grid-item v-for="item in layout.data" :key="item.id" :id="item.id">

          <div v-if="item.id == 'main-info'" style="width: 100%; height: 100%;background-color: #ffffff">
            <img src="/admin2.png" style="height: 30px; width: 609px"/>
            <img src="/admin3.png" style="height: 40px; width: 40px; position: absolute; margin-left: 20px"/>
          </div>
          <div v-else style="width: 100%; height: 100%; background-color: #ffffff">
            <div style="padding: 20px; font-size: 14px; font-weight: bolder"> {{ item.id }} </div>
            <div style="display: flex" v-if="item.type == 'symmetry' ">
<!--              <div style="flex: 0.5;margin-top: 30px">-->
<!--                <div class="layout-icon" ></div>-->
<!--                <div style="overflow: hidden">-->
<!--                  123-->
<!--                </div>-->
<!--              </div>-->
              <div class="loader">
                <div class="dashboard-icon">
                  <p class="layout-name">2</p>
                  <p class="layout-info">总数量</p>
                </div>

                <div class="albumcover"></div>

              </div>
              <div style="flex: 0.5">21</div>

            </div>
          </div>
        </grid-item>
      </grid-layout>
    </div>
  </div>

</template>

<script setup lang="ts">
import { GridLayout, GridItem, type Layout, type LayoutItem } from 'vue3-draggable-grid'
import 'vue3-draggable-grid/dist/style.css'
import { ref, watch } from 'vue'

const layout = ref(
    {
      data: [
        {"id": "物理机","type": "symmetry", "x": 1, "y": 1, "h": 3, "w": 4},
        {"id": "CPU已分配率", "x": 5, "y": 1, "h": 3, "w": 2},
        {"id": "内存已分配率", "x": 7, "y": 1, "h": 3, "w": 2},
        {"id": "main-info", "x": 9, "y": 1, "h": 3, "w": 4, static: true},
        {"id": "云主机", "x": 1, "y": 4, "h": 3, "w": 4},
        {"id": "主存储分配率", "x": 5, "y": 4, "h": 3, "w": 2},
        {"id": "镜像服务器使用率", "x": 7, "y": 4, "h": 3, "w": 2},
        {"id": "云盘", "x": 9, "y": 4, "h": 3, "w": 4},
        {"id": "云存储", "x": 1, "y": 7, "h": 3, "w": 4},
        {"id": "Top10：云盘真实容量", "x": 5, "y": 7, "h": 6, "w": 4},
        {"id": "最近访问", "x": 9, "y": 7, "h": 3, "w": 4},
        {"id": "镜像服务器", "x": 1, "y": 10, "h": 3, "w": 4},
        {"id": "Top10：快照容量", "x": 9, "y": 10, "h": 6, "w": 4},
      ]
    }
)

const addGridLayout = ()=>{
  const uniqueId = 'id-' + Math.random().toString(36).substr(2, 9);
  const layoutx = Math.random()*(5-1)+1
  layout.value.data.push({id: uniqueId, x: layoutx, y: 1, h: 1, w: 1})
}

// 验证更新数据是否正确
watch(layout, () => {
  console.log('数据更新', layout.value)
}, {deep: true})

const draggableStart = (id: string) => {
  console.log('拖拽开始', id)
}

const draggableHandle = (id: string, data: LayoutItem) => {
  console.log('拖拽中', id, data)
}

const draggableEnd = (data: Layout) => {
  console.log('拖拽结束', data)
}

const remove = (id: string) => {
  console.log('删除', id)
}

</script>
<style lang="less" scoped>

.layout-box {
  width: auto;
  padding: 10px;
}

.layout-icon{
  float: left;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: 55px;
  height: 55px;
  border-style: solid;
  border-width: 1px;
  border-radius: 50%;
  -moz-border-radius: 50%; -webkit-border-radius : 50%;
  border-color: #1E90FF;
  -webkit-border-radius: 50%;
}


.main {
  background-color: white;
  padding: 1em;
  padding-bottom: 1.1em;
  border-radius: 15px;
  margin: 1em;
}

.loader {
  flex: 0.7;
  display: flex;
  flex-direction: row;
  height: 4em;
  padding-right: 5em;
  transform: rotate(180deg);
  margin-top: 30px;
  justify-content: right;
  border-radius: 20px;
  transition: .4s ease-in-out;
}

.albumcover {
  position: relative;
  margin-right: 1em;
  height: 55px;
  width: 55px;
  background-color: #f5f7fa;
  align-self: center;
  border-radius: 50%;
}

.dashboard-icon {
  position: relative;
  transform: rotate(180deg);
  margin-right: 1em;
  color: black;
  align-self: center;
}

.layout-name {
  text-align: center;
  font-size: 20px;
}

.layout-info {
  font-size: 1em;
}


</style>