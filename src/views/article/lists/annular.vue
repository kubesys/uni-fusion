
<template>
    <div id="myChart" :style="{ width: '70px', height: '100%' ,flex: 1}"></div>
    <div style="margin-right: 10px; flex: 1">
        <div style="float:left; margin-top: 10px; font-size: 10px">
            总数
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; font-weight: bolder; font-size: 10px">
            2
        </div>
    </div>
    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #57D344; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 10px">
            运行中
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 10px">
            0
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: red; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 10px">
            已停止
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 10px">
            2
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #dbdde0; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 10px">
            其他
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 10px">
            0
        </div>
    </div>

    <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #96989b; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 10px">
            回收站
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 10px">
            0
        </div>
    </div>
</template>

<script lang="ts">
    import { defineComponent, onMounted, getCurrentInstance } from 'vue'
    export default defineComponent({
        setup() {
            const { proxy } = getCurrentInstance() as any
            // 配置建议写在 onMount 的外面
            const option = {

                color: ['#F93940'],
                series: [
                    {
                        type: 'pie',
                        radius: ['40%', '80%'],
                        data: [{ value: 1048},],
                        label: {
                            show: false, // 设置为false隐藏指示线
                        }

                    }
                ]
            }
            onMounted(() => {
                // 获取挂载的组件实例
                const echarts = proxy.$ECharts
                //初始化挂载
                const echarts1 = echarts.init(document.getElementById('myChart'))
                //添加配置
                echarts1.setOption(option)
                // 自适应
                window.onresize = function () {
                    echarts1.resize()
                }
            })
            return {}
        }
    })
</script>
