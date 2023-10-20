<template>
    <el-dropdown :hide-on-click="false">
        <span class="el-dropdown-link">
           <el-button> <icon name="el-icon-more"  /></el-button>
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item>编辑</el-dropdown-item>
                <el-dropdown-item>启动</el-dropdown-item>
                <el-dropdown-item>停止</el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script lang="ts" setup>

</script>
<style scoped>
    .example-showcase .el-dropdown + .el-dropdown {
        margin-left: 15px;
    }
    .example-showcase .el-dropdown-link {
        cursor: pointer;
        color: var(--el-color-primary);
        display: flex;
        align-items: center;
    }
</style>
