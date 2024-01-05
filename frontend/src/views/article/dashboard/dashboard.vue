<template>
  <div style="background: #f5f7fa; padding: 20px; width: 100%; height: 1000px;">
    <div style="margin-bottom: 20px"> 当前区域 </div>
    <a-row :gutter="[30, 24]" >
      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">物理机</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="4">
        <div class="gutter-box2">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">CPU分配率</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="4">
        <div class="gutter-box2">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">内存已分配率</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1" style="">
          <img src="/admin2.png" style="height: 30px; width: 609px"/>
          <img src="/admin3.png" style="height: 40px; width: 40px; position: absolute; margin-left: 20px"/>
        </div>
      </a-col>

      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">云主机</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="4">
        <div class="gutter-box2">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">主存储已分配率</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="4">
        <div class="gutter-box2">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">镜像服务器使用率</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">云盘</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>

      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">主存储</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="8">
        <div class="gutter-box3">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">Top10 : 云盘真实容量</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
      <a-col class="gutter-row" :span="8">
        <div class="gutter-box1">
          <div style="padding: 20px; font-size: 14px; font-weight: bolder">最近访问</div>
          <div style="display: flex">
            <div style="flex: 1"></div>
            <div style="flex: 2"></div>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>


</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.gutter-box {
  background: #ffffff;
  padding: 80px 0;
}
.gutter-box1{
  background: #ffffff;
  width: 609px;
  height: 200px;
}

.gutter-box2{
  background: #ffffff;
  width: 294px;
  height: 200px;
}

.gutter-box3{
  background: #ffffff;
  width: 609px;
  height: 420px;
}


</style>
