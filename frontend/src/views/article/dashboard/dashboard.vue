<template>

  <div style="height: 100vh;overflow: scroll;background: #f5f7fa">
    <div >

    </div>

    <div class="layout-box">
<!--      <a-button style="margin-bottom: 10px"  @click="addGridLayout">-->
<!--        添加组件-->
<!--      </a-button>-->
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
            <div style="display: flex">
              <img src="/admin3.png" style=" height: 50px; width: 50px; margin-left: 40px; margin-top: 10px"/>
              <span style="margin-left: 20px; margin-top: 20px; font-family:SimHei; font-size: 25px; font-weight: bold "> 欢迎，admin</span>
            </div>
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
                  <p class="layout-name">{{ item.count }}</p>
                  <p class="layout-info">总数量</p>
                </div>

<!--                <div class="albumcover"><icon class="albumcover-icon" name="el-icon-RefreshRight" :size="30" /></div>-->

                <icon class="albumcover" :name=item.icon :size="30" />

              </div>
              <div style="flex: 0.5">
                <div >
                  <div style="display: flex; margin-top: 5px">
                    <div class="icon-point-green"></div>
                    <span>已运行</span>
                  </div>
                  <div style="display: flex; margin-top: 20px">
                    <div class="icon-point-red"></div>
                    <span >未运行</span>
                  </div>
                  <div style="display: flex; margin-top: 20px">
                    <div class="icon-point-other"></div>
                    <span>其他</span>
                  </div>
                </div>
              </div>

            </div>
            <div v-else-if="item.type == 'echart'">
              <a-progress class="dashboard-paogress" type="circle" :percent="item.percent" size="small" />
              <div class="echart-text" >已用</div>
              <div class="echart-text" >总量</div>
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
import { CarryOutOutlined } from '@ant-design/icons-vue'

const layout = ref(
    {
      data: [
        {"id": "微服务应用","type": "symmetry","icon": "el-icon-coin","count":"56", "x": 1, "y": 1, "h": 3, "w": 4},
        {"id": "CPU已分配率","type": "echart","percent": 0, "x": 5, "y": 1, "h": 3, "w": 2},
        {"id": "内存已分配率","type": "echart","percent": 20, "x": 7, "y": 1, "h": 3, "w": 2},
        {"id": "main-info", "x": 9, "y": 1, "h": 3, "w": 4, static: true},
        {"id": "服务","type": "symmetry","icon": "el-icon-files","count":"43", "x": 1, "y": 4, "h": 3, "w": 4},
        {"id": "主存储分配率","type": "echart","percent": 50, "x": 5, "y": 4, "h": 3, "w": 2},
        {"id": "镜像服务器使用率","type": "echart","percent": 10, "x": 7, "y": 4, "h": 3, "w": 2},
        {"id": "任务","type": "symmetry","icon": "el-icon-tickets","count":"9", "x": 9, "y": 4, "h": 3, "w": 4},
        {"id": "容器组","type": "symmetry","icon": "el-icon-copy-document","count":"226", "x": 1, "y": 7, "h": 3, "w": 4},
        {"id": "保密字典","type": "symmetry","icon": "el-icon-key","count":"49", "x": 5, "y": 7, "h": 3, "w": 4},
        {"id": "持久卷","type": "symmetry","icon": "el-icon-guide","count":"19", "x": 1, "y": 10, "h": 3, "w": 4}
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
  transform: rotate(180deg);
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

.icon-point-green {
  margin-top: 4px;
  margin-right: 5px;
  width: 7px;
  height: 7px;
  background-color: #5dd44b;
  border-radius: 50%;
}

.icon-point-red {
  margin-top: 4px;
  margin-right: 5px;
  width: 7px;
  height: 7px;
  background-color: red;
  border-radius: 50%;
}

.icon-point-other {
  margin-top: 4px;
  margin-right: 5px;
  width: 7px;
  height: 7px;
  background-color: #96989b;
  border-radius: 50%;
}

.dashboard-paogress{
  width: 100%;
  margin-left: 110px;
}

.echart-text{
  width: 100%;
  margin-left: 20px;
  margin-top: 10px
}
</style>