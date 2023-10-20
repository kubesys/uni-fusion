<template>
    <div v-for="row in cloudHostingItems.rows"
         :key="row.index"
         :gutter=cloudHostingItems.gutter>
        <div class="host-header-list">
            <div class="header-introduce">
                <div class="header-introduce-name">{{row.title}}</div>
                <div class="header-introduce-detail">{{row.detail}}</div>
            </div>
            <div class="host-state">
                <annular />
                <!--                    <div class="allData ">-->
                <!--                        <span>总数</span>-->
                <!--                        <span>1</span>-->
                <!--                    </div>-->
            </div>
        </div>
        <div class="Tab-card-list">
            <el-tabs class="custom-table"
                     type="card">
                <div class="container"
                     v-for="datas in row.items"
                     :key="datas.index">
                    <el-tab-pane class="table-pane" :label=datas.label >
                        <div class="operational-context" v-if="datas.index">
                            <el-button class="mb-4">
                                <template #icon>
                                    <icon name="el-icon-refresh-right" />
                                </template>
                            </el-button>
                            <el-button type="primary" class="mb-4">
                                <template #icon>
                                    <icon name="el-icon-Plus" />
                                </template>
                                {{datas.buttonName}}
                            </el-button>
                            <el-button type="primary" class="mb-4">
                                <template #icon>
                                    <icon name="el-icon-more" />
                                </template>
                            </el-button>
                        </div>
                        <div class="table-box">
                            <el-table class="table-style"
                                      size="large"
                                      :data="datas.tableItems"
                                      style="width:100%"
                                      stripe="true">
                                <el-table-column
                                        type="selection"
                                        width="55">
                                </el-table-column>
                                <el-table-column
                                        fixed="left"
                                        prop="name"
                                        label="名称"
                                        width="250">
                                </el-table-column>
                                <el-table-column
                                        v-for="item in datas.tableheaderItems"
                                        :prop="item.prop"
                                        :label="item.label"
                                        :width="item.width">
                                </el-table-column>
                                <el-table-column
                                        fixed="right"
                                        prop="operate"
                                        label="操作"
                                        width="250">
                                    <Select />
                                </el-table-column>
                            </el-table>
                            <el-pagination :total="10"
                                           :page-size="5"

                            ></el-pagination>
                        </div>
                    </el-tab-pane>
                </div>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    import axios from "axios";
    import Annular from "../lists/annular.vue";
    import Select from '../lists/select.vue'
    import {getMenu} from "../../../api/user";

    export default {
        components:{ Annular, Select },

        data () {
            return {
                cloudHostingItems:[],
            }
        },

        mounted() {
            this.cloudHosting()
        },

        methods: {
            cloudHosting() {
                axios.get('http://localhost:5173/Hostings').then(response => {
                    if (response.data) {
                        console.log(response.data.data);
                        this.cloudHostingItems = response.data.data;
                    }
                });

            }
        }
    }
</script>

<style scoped>
    .host-header-list{
        width: 100%;
        height: 56px;
        display: flex; /* 使用 flex 布局 */
        padding-left: 24px;
        padding-right: 24px;
        margin-top: 20px;
        margin-bottom: 20px;
        justify-content: space-between;
    }

    .header-introduce{
        float: left;
        text-align: left;
    }

    .header-introduce .header-introduce-name{
        font-size: 25px;
    }

    .header-introduce .header-introduce-detail{
        font-size: 10px;
        color: #909399;
    }

    .host-state{
        display: flex; /* 使用 flex 布局 */
        text-align: right;
        flex: 0;
    }

    .showAllData{

    }

    .Tab-card-list{
        padding-top: 20px;
    }

    ::v-deep .el-tabs__item {
        box-shadow: 5px 0 5px -5px rgba(0, 0, 0, 0.5);
    }

    .custom-table{
        float: left;
        width: 100%;
    }

    .custom-table .table-pane{
        float: left;
    }

    .table-style{
        width: auto;
    }

    .table-box{
        width: 100%;
        overflow: auto;
    }
</style>
