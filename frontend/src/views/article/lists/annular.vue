<template>
    <div id="myChart" :style="{ width: '70px', height: '52px' ,flex: 1}"></div>
    <div style="margin-right: 10px; flex: 1">
        <div style="float:left; margin-top: 10px; font-size: 12px">
            总数
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; font-weight: bolder; font-size: 12px">
            {{tableData.metadata.totalCount}}
        </div>
    </div>
    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #57D344; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
            运行中
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          {{ tableData.resultRun }}
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: red; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
            已停止
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          {{ tableData.resultPen }}
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #dbdde0; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
            其他
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
            0
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #96989b; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
            回收站
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
            0
        </div>
    </div>
</template>

<script setup lang="ts">
import {onMounted, onBeforeUnmount, ref, watch} from 'vue'
import { useRouter } from 'vue-router'
import { frontendData } from "@/api/common";

const route = useRoute()
const ListName = route.meta?.listname
const TableName = route.meta?.tablename
const filter = route.meta?.filter || {}
const allLabels = ref(filter)

const pageSite = ref({limit:5,page:1})
const tableColumns = ref([])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[],
  resultRun: 0,
  resultPen: 0
})

onMounted(()=>{ frontendData(ListName, TableName, pageSite,tableColumns, tableData, allLabels.value,[])})


const router = useRouter()
const { proxy } = getCurrentInstance() as any
const echartsInstance = ref(null)
watch(
    [() => tableData.value.resultRun, () => tableData.value.resultPen],
    ([newResultRun, newResultPen]) => {
      const option = {
        color: ['#57D344', '#F93940'],
        series: [
          {
            type: 'pie',
            radius: ['40%', '80%'],
            data: [{ value: newResultRun }, { value: newResultPen }],
            label: {
              show: false, // 设置为 false 隐藏指示线
            },
          },
        ],
      }

      const echarts = proxy.$ECharts

        // 初始化挂载
      echartsInstance.value = echarts.init(document.getElementById('myChart')!)
        // 添加配置
      echartsInstance.value.setOption(option)
        // 自适应
      window.onresize = function () {
          echartsInstance.value.resize()
      }

      onBeforeUnmount(() => {
        // 在组件销毁前销毁 ECharts 图表
        if (echartsInstance.value) {
          echartsInstance.value.dispose()
        }
      })

      // 监听路由变化，当路由切换时重新初始化 ECharts 图表
      router.beforeEach(() => {
        if (echartsInstance.value) {
          // 销毁当前的 ECharts 图表
          echartsInstance.value.dispose()
        }
      })
    }
)


// watch([tableData.value.resultRun, tableData.value.resultPen], ([newResultRun, newResultPen], [oldResultRun, oldResultPen]) => {
//   console.log(`resultRun 变化了，新值为 ${newResultRun}，旧值为 ${oldResultRun}`);
//   console.log(`resultPen 变化了，新值为 ${newResultPen}，旧值为 ${oldResultPen}`);
//   // 在这里可以执行你想要的操作
//
//   const option = {
//     color: ['#57D344', '#F93940'],
//     series: [
//       {
//         type: 'pie',
//         radius: ['40%', '80%'],
//         data: [{ value: newResultRun }, { value: newResultPen }],
//         label: {
//           show: false, // 设置为 false 隐藏指示线
//         },
//       },
//     ],
//   }
//
//   const router = useRouter()
//   const { proxy } = getCurrentInstance() as any
//   const echarts = proxy.$ECharts
//   const echartsInstance = ref(null)
//
//   onMounted(() => {
//     // 初始化挂载
//     echartsInstance.value = echarts.init(document.getElementById('myChart')!)
//     // 添加配置
//     echartsInstance.value.setOption(option)
//     // 自适应
//     window.onresize = function () {
//       echartsInstance.value.resize()
//     }
//   })
//
//   onBeforeUnmount(() => {
//     // 在组件销毁前销毁 ECharts 图表
//     if (echartsInstance.value) {
//       echartsInstance.value.dispose()
//     }
//   })
//
//   // 监听路由变化，当路由切换时重新初始化 ECharts 图表
//   router.beforeEach(() => {
//     if (echartsInstance.value) {
//       // 销毁当前的 ECharts 图表
//       echartsInstance.value.dispose()
//     }
//   })
// })

// const option = {
//   color: ['#57D344','#F93940'],
//   series: [
//     {
//       type: 'pie',
//       radius: ['40%', '80%'],
//       data: [{ value: tableData.value.resultRun },{value: tableData.value.resultPen}],
//       label: {
//         show: false, // 设置为 false 隐藏指示线
//       },
//     },
//   ],
// }
//
// const router = useRouter()
// const { proxy } = getCurrentInstance() as any
// const echarts = proxy.$ECharts
// const echartsInstance = ref(null)
//
// onMounted(() => {
//   // 初始化挂载
//   echartsInstance.value = echarts.init(document.getElementById('myChart')!)
//   // 添加配置
//   echartsInstance.value.setOption(option)
//   // 自适应
//   window.onresize = function () {
//     echartsInstance.value.resize()
//   }
// })
//
// onBeforeUnmount(() => {
//   // 在组件销毁前销毁 ECharts 图表
//   echartsInstance.value.dispose()
// })
//
// // 监听路由变化，当路由切换时重新初始化 ECharts 图表
// router.beforeEach(() => {
//   if (echartsInstance.value) {
//     // 销毁当前的 ECharts 图表
//     echartsInstance.value.dispose()
//   }
// })
</script>
