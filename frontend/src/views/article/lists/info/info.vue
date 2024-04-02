<template>
  <div>
    <div class="info_title">
      <span style="font-size: 12px; color: #96989b"></span>
    </div>
    <div class="info_intro">
      <div class="image">
        <el-avatar :size="40" :src="userInfo.avatar" />
        <span style="flex: 0.5; padding: 10px; font-size: 20px; font-weight: bolder">{{jsonValue.metadata.name}}</span>
      </div>
    </div>



    <a-card
        style="width: 100%"
        :tab-list="tabList"
        :active-tab-key="key"
    >

      <a-row :gutter="16" >
        <a-col :span="12" v-for="col in infoItems" :key="col.name">
          <a-collapse v-model:activeKey="activeKey" style="width: 100%; border-radius: 0px; margin-top: 10px">
            <a-collapse-panel key="1" :header="col.name" style="margin-button: 10px">
              <a-form
                  v-for="info in col.data"
                  :label-col="labelCol"
                  :wrapper-col="wrapperCol"
                  :key="info.index">
                <a-form-item class="custom-form-item" :label="info.label" labelAlign="left" >
                  <div >
                    <!--                    {{ jsonValue.metadata.name }}-->
                    {{ getComplexDataDispose(jsonValue, info.row) }}
                  </div>
                </a-form-item>
                <!--                <a-form-item label="启用状态" labelAlign="left">-->
                <!--                  &lt;!&ndash;                <div v-if="jsonValue.spec?.status?.conditions?.state?.waiting?.reason">&ndash;&gt;-->
                <!--                  &lt;!&ndash;                  {{ jsonValue.spec.status.conditions.state.waiting.reason }}&ndash;&gt;-->
                <!--                  &lt;!&ndash;                </div>&ndash;&gt;-->

                <!--                  <span class="icon-text-container" v-if="jsonValue.spec.status.conditions.state.waiting.reason == 'Running'" >-->
                <!--                <RightCircleTwoTone two-tone-color="#57d344" />运行中-->

                <!--               </span>-->
                <!--                  <span class="icon-text-container"v-else-if="jsonValue.spec.status.conditions.state.waiting.reason == 'Shutdown'">-->
                <!--               <StopTwoTone two-tone-color="red"/>已停止-->
                <!--               </span>-->
                <!--                  <div v-else> - </div>-->
                <!--                </a-form-item>-->
                <!--                <a-form-item class="custom-form-item" label="平台" labelAlign="left">-->
                <!--                  <div >-->
                <!--                    <span><i class="iconfont icon-linux" />Liunx</span>-->
                <!--                  </div>-->
                <!--                </a-form-item>-->
              </a-form>
            </a-collapse-panel>

          </a-collapse>
        </a-col>
        <!--        <a-col :span="12" >-->
        <!--          <a-collapse v-model:activeKey="activeKey" style=" width: 100%; border-radius: 0px;">-->
        <!--                    <a-collapse-panel key="1" header="平台信息">-->
        <!--                      <a-form-->
        <!--                          :label-col="labelCol"-->
        <!--                          :wrapper-col="wrapperCol" >-->
        <!--                        <a-form-item label="集群" labelAlign="left">-->
        <!--                          <div v-if="jsonValue.metadata.name" >-->
        <!--                            - -->
        <!--                          </div>-->
        <!--                          <div v-else> - </div>-->
        <!--                        </a-form-item>-->
        <!--                        <a-form-item label="当前物理机" labelAlign="left">-->
        <!--                          <div v-if="jsonValue.metadata.name" >-->
        <!--                            - -->
        <!--                          </div>-->
        <!--                          <div v-else> - </div>-->
        <!--                        </a-form-item>-->
        <!--                        <a-form-item label="主存储" labelAlign="left">-->
        <!--                          <div v-if="jsonValue.metadata.name" >-->
        <!--                            - -->
        <!--                          </div>-->
        <!--                          <div v-else> - </div>-->
        <!--                        </a-form-item>-->

        <!--                      </a-form>-->
        <!--                    </a-collapse-panel>-->
        <!--                  </a-collapse>-->
        <!--        </a-col>-->
      </a-row>

    </a-card>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import useUserStore from "@/stores/modules/user";
import { defineProps } from 'vue';
import { frontendInfo, getComplexDataDispose} from "@/api/common";
import {RightCircleTwoTone, StopTwoTone} from "@ant-design/icons-vue";

const userStore = useUserStore()

const labelCol = { span: 4 };
const wrapperCol = { span: 9 };

const infoItems = ref([])

const key = ref('总览');
const tabList = [
  {
    key: '总览',
    tab: '总览',
  }
];

const activeKey = ref(['1']);


const userInfo = computed(() => userStore.userInfo)

const jsonValue = ref()
const  myObject  = defineProps(['myObject']);
jsonValue.value = myObject.myObject.obj

console.log(myObject.myObject.tablename)
onMounted(()=>{
  frontendInfo(myObject.myObject.tablename, infoItems)
})

</script>

<style scoped lang="scss">
.info_title{
  padding: 10px;
  margin-left: 10px;
}

.info_intro{
  flex: 1;
  width: 100%;
  display: flex;
  margin-top: 10px;
  padding-left: 24px;
  padding-right: 24px;
  margin-bottom: 20px;
  justify-content: space-between;
}

.image{
  flex: 1;
  display: flex;
}

.intro{
  flex: 0.2;
  text-align: center;
}

.space{
  flex: 0.1;
  text-align: right;
}

.custom-form-item {
  margin-bottom: 0 !important;
}
</style>
