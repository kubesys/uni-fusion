<template>
  <div class="wrap">
    <div class="header">header</div>
    <div class="main">
      <div style="height: 300%">

        <div class="content flex">
          <div class="left">
            left
          </div>
          <div class="center">
            <div style="height: 300%">
              <div>

                center


              </div>
            </div>
          </div>
          <div class="right">
            right
          </div>
        </div>

      </div>
    </div>
    <div class="footer">footer</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const templateDate = ref({
  "spec": {
    "template": {
      "apiVersion": "v1",
      "kind": "VirtualMachine"
    },
    "data": {
      "step1": {
        "group1": {
          "constants": {
            "introduction": {
              "label": "简介",
              "type": "textbox"
            }
          },
          "variables": {
            "metadata.labels.host": {
              "label": "host",
              "type": "select"
            },
            "metadata.name": {
              "label": "名称",
              "regexp": [
                "A-Za-z"
              ],
              "type": "text"
            }
          }
        },
        "group2": {
          "variables": {
            "spec.lifecycle.createAndStartVMFromISO.memory": {
              "label": "内存大小",
              "show": "show-input",
              "type": "slider"
            },
            "spec.lifecycle.createAndStartVMFromISO.os_variant": {
              "label": "操作系统",
              "type": "select"
            },
            "spec.lifecycle.createAndStartVMFromISO.vcpus": {
              "label": "CPU数量",
              "regexp": [
                "0-9"
              ],
              "step": 1,
              "type": "number"
            },
            "spec.lifecycle.createAndStartVMFromISO.virt_type": {
              "data": {
                "label": "kvm"
              },
              "label": "虚拟化类型",
              "type": "radio"
            },
            "spec.nodename": {
              "label": "节点名称",
              "type": "text"
            }
          }
        }
      },
      "step2": {
        "group1": {
          "variables": {
            "spec.lifecycle.createAndStartVMFromISO.DVD": {
              "kind": null,
              "label": "选择云盘",
              "name": null,
              "type": "combox"
            },
            "spec.lifecycle.createAndStartVMFromISO.cdrom": {
              "kind": null,
              "label": "选择光盘镜像",
              "name": null,
              "type": "combox"
            }
          }
        },
        "group2": {
          "variables": {
            "spec.lifecycle.createAndStartVMFromISO.graphics": {
              "label": "VNC协议",
              "type": "select"
            },
            "spec.lifecycle.createAndStartVMFromISO.network": {
              "label": "网络配置",
              "regexp": [
                "A-Za-z"
              ],
              "type": "list"
            },
            "spec.lifecycle.createAndStartVMFromISO.noautoconsole": {
              "data": {
                "label1": "true",
                "label2": "false"
              },
              "label": "自动打开控制台",
              "type": "radio"
            }
          }
        }
      },
      "step3": {

      }
    }
  }
})
</script>

<style scoped lang="scss">
.wrap {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-flex-direction: column;
  -ms-flex-direction: column;
  /*布局方向是垂直的*/
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.header, .footer {
  height: 40px;
  line-height: 40px;
  background-color: #D8D8D8;
  text-align: center;
}

.main {
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex: 1;
  width: 100%;
  overflow: auto;
}

.content{
  width: 100%;
  background: bisque;
  align-items: flex-start;
  height: 100%;
  overflow: hidden;
}
.left{
  width: 300px;
  background: yellowgreen;
}
.right{
  width: 300px;
  background: greenyellow;
}
.center{
  flex-grow: 1;
  background: aquamarine;
  height: 100%;
  overflow: auto;
}
</style>
