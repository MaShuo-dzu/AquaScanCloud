<template>
    <div class="container">
        <div class="show-tables">
            <el-table-v2
                :columns="columns"
                :background="isBackground"
                :data="data"
                :width="1050"
                :height="600"
                fixed
            />
        </div>

        <br>

        <div class="pagination-container">
            <el-pagination layout="prev, pager, next" :total="total * 10" :background="isBackground"/>
        </div>
    </div>
</template>

<script>
import { createVNode } from 'vue';
import { Timer } from '@element-plus/icons-vue';
import { getRaft } from '@/api/raftMG.js';
import dayjs from 'dayjs';
import {
  ElButton,
  ElIcon,
  ElTag,
  ElTooltip,
  TableV2FixedDir,
} from 'element-plus';

export default {
    name: 'FishPointManageFishermenPage',
    computed: {
        // 计算属性，total总是等于data数组的长度
        total() {
            return this.data.length;
        }
    },
    data() {
        return {
            columns: [
                { key: 'name', dataKey: 'name', title: '名称', fixed: 'left', width: 100},
                { key: 'longitude', dataKey: 'longitude', title: '经度', fixed: 'left', width: 100},
                { key: 'latitude', dataKey: 'latitude', title: '纬度', fixed: 'left', width: 100},
                { key: 'zoom', dataKey: 'zoom', title: '区域大小', width: 150 },
                { key: 'size', dataKey: 'size', title: '大小', width: 150},
                { key: 'message', dataKey: 'message', title: '备注', width: 150},
                { key: 'mask', dataKey: 'mask', title: '渔排掩码', width: 150},
                { key: 'createTime', dataKey: 'createTime', title: '创建时间', width: 200,
                    cellRenderer({ cellData }) {
                        const formattedDate = dayjs(cellData).format('YYYY/MM/DD HH:mm:ss');
                        return createVNode(
                            ElTooltip,
                            { content: formattedDate },
                            {
                            default: () => createVNode(
                                'span',
                                { class: 'flex items-center' },
                                [
                                createVNode(
                                    ElIcon,
                                    { class: 'mr-3' },
                                    { default: () => createVNode(Timer) }
                                ),
                                formattedDate
                                ]
                            )
                            }
                        );
                    },
                },
                { key: 'updateTime', dataKey: 'updateTime', title: '更新时间', width: 200,
                    cellRenderer({ cellData }) {
                        const formattedDate = dayjs(cellData).format('YYYY/MM/DD HH:mm:ss');
                        return createVNode(
                            ElTooltip,
                            { content: formattedDate },
                            {
                            default: () => createVNode(
                                'span',
                                { class: 'flex items-center' },
                                [
                                createVNode(
                                    ElIcon,
                                    { class: 'mr-3' },
                                    { default: () => createVNode(Timer) }
                                ),
                                formattedDate
                                ]
                            )
                            }
                        );
                    },
                },
                { key: 'operations', dataKey: 'operations', title: '操作', 
                    cellRenderer: this.cellOperations,
                    fixed: 'right', 
                    width: 150,
                    align: 'center',
                },
            ],
            data: [],
            isBackground: true
        };
    },
    mounted() {
        this.fetchData();
    },
    methods: {
        async fetchData() {
            getRaft({
            }).then( (response) => {
                if (response.data.code === 200){
                    this.data = response.data.data
                } else {
                    ElMessage.error(response.data.msg)
                }
            })
        },
        cellOperations(row) {
            return createVNode(
                'div',
                null,
                [
                createVNode(
                    ElButton,
                    { 
                        size: 'small',
                        onClick: () => this.handleEdit(row)
                        },
                    { default: () => '编辑' }
                ),
                createVNode(
                    ElButton,
                    { size: 'small', onClick: () => this.handleDelete(row), type: 'danger' },
                    { default: () => '删除' }
                )
                ]
            );
        },
        handleEdit(row) {
            // 处理编辑逻辑，例如打开编辑对话框
            console.log('编辑', row);
        },
        handleDelete(row) {
            // 处理删除逻辑，例如确认删除并调用删除接口
            console.log('删除', row);
        }
    }
}
</script>

<style scoped>
.container {
    position: relative;
    border-radius: 10px;
    border: 2px solid #ccc; 
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.8);
    background-color: rgba(255, 255, 255, 0.5);
    padding: 10px;
    margin: 5px;
    z-index: 3;
}

.show-tables :deep() .el-table-v2 .el-table-v2__table {
    border-radius: 10px;
}

.show-tables :deep() .el-table-v2 .el-table-v2__empty{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, 50%); /* 使用CSS3的transform属性进行微调 */
}

.pagination-container {
    display: flex;
    justify-content: center; /* 水平居中 */
    background: transparent; /* 背景透明 */
    padding: 10px 0; /* 根据需要添加上下边距 */
}


</style>