<template>
    <div class="container">
        <el-row class="tac" style="height: 100%;">
            <el-col :span="4">
                <el-menu active-text-color="#ffd04b" background-color="#545c64" class="el-menu-vertical-demo"
                    text-color="#fff" @open="handleOpen" @close="handleClose" @select="handleSelect">
                    <el-menu-item index="1">
                        <template #title>
                            <el-icon><Operation /></el-icon>
                            <span>控制面板</span>
                        </template>
                    </el-menu-item>

                    <el-sub-menu index="2">
                        <template #title>
                            <el-icon><Place /></el-icon>
                            <span>渔排点管理</span>
                        </template>

                        <el-menu-item index="2-1">添加渔排</el-menu-item>
                        <el-menu-item index="2-2">管理渔排</el-menu-item>
                    </el-sub-menu>

                    <el-menu-item index="3">
                        <el-icon><PartlyCloudy /></el-icon>
                        <span>天气监测</span>
                    </el-menu-item>

                    <el-menu-item index="4">
                        <el-icon><Coin /></el-icon>
                        <span>算法监测</span>
                    </el-menu-item>

                    <el-menu-item index="5">
                        <el-icon><Warning /></el-icon>
                        <span>告警信息</span>
                    </el-menu-item>

                    <el-sub-menu index="6">
                        <template #title>
                            <el-icon><User /></el-icon>
                            <span>系统设置</span>
                        </template>

                        <el-menu-item index="6-1">身份证绑定</el-menu-item>
                        <el-menu-item index="6-2">邮箱换绑</el-menu-item>
                        <el-menu-item index="6-3">个人信息修改</el-menu-item>
                    </el-sub-menu>

                </el-menu>
            </el-col>

            <el-col :span="1">
                <div class="divider"></div> <!-- 分界线 -->
            </el-col>
            
            <el-col :span="19">
                <!-- 右侧内容 -->
                <router-view></router-view>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { User, Warning, Coin, PartlyCloudy, Place, Operation } from '@element-plus/icons-vue';

export default {
    name: "HomeFishermanPage",
    components: {
        User,
        Warning,
        Coin,
        PartlyCloudy,
        Place,
        Operation
    },
    data() {
        return {
            isCollapse: true
        };
    },
    methods: {
        handleOpen(key, keyPath) {
            // console.log(key, keyPath)
        },
        handleClose(key, keyPath) {
            // console.log(key, keyPath)
        },
        handleSelect(index) {
            let route = '';
            switch (index) {
                case '1':
                route = '/dashboard';
                break;
                case '2-1':
                route = '/fish-point-add';
                break;
                case '2-2':
                route = '/fish-point-manage';
                break;
                case '3':
                route = '/weather-monitor';
                break;
                case '4':
                route = '/algorithm-monitor';
                break;
                case '6-1':
                route = '/id-binding';
                break;
                case '6-2':
                route = '/email-binding';
                break;
                case '6-3':
                route = '/personal-info-edit';
                break;
                default:
                route = '/dashboard';
            }
            this.$router.push("/home-fisherman" + route);
        }
    }
};
</script>

<style scoped>
.container {
    background-color: rgba(255, 255, 255, 0.5);
    /* 透明背景，白色背景和50%透明度 */
    border: 1px solid #ccc;
    /* 边框颜色 */
    border-radius: 8px;
    /* 圆角边框 */
    padding: 20px;
    /* 内边距 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 轻微的阴影效果 */
    position: fixed;
    /* 或者使用 fixed */
    top: 50%;
    left: 50%;
    width: 98%;
    height: 95%;
    transform: translate(-50%, -50%);
    z-index: 2;
}

.el-menu-vertical-demo {
    border-radius: 8px;
    /* 圆角边框 */
}

.divider {
  width: 2px;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2); /* 分界线颜色 */
  margin: 0 auto; /* 水平居中 */
}
</style>