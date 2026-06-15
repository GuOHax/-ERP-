<template>
  <div class="department-list-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openAddDialog(null)">
        <el-icon><Plus /></el-icon> 新增根部门
      </el-button>
      <el-button @click="fetchDepartments">
        <el-icon><Refresh /></el-icon> 刷新
      </el-button>
    </div>

    <!-- 树形部门表格 -->
    <el-table
      :data="deptTreeData"
      style="width: 100%; margin-top: 12px"
      row-key="deptID"
      border
      stripe
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      v-loading="tableLoading"
    >
      <el-table-column prop="deptName" label="部门名称" min-width="180" />
      <el-table-column prop="empCount" label="人数" width="90" align="center" />
      <el-table-column prop="deptOrder" label="排序" width="80" align="center" />
      <el-table-column prop="isShow" label="显示" width="100" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.isShow"
            active-text="是"
            inactive-text="否"
            @change="handleToggleShow(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170">
        <template #default="scope">{{ formatDateTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="300" fixed="right">
        <template #default="scope">
          <div class="btn-group">
            <el-button size="small" type="primary" @click="openAddChildDialog(scope.row)">
              <el-icon><Plus /></el-icon> 添加子部门
            </el-button>
            <el-button size="small" type="warning" @click="openEditDialog(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加 / 编辑弹窗 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      destroy-on-close
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="deptFormRef"
        label-width="100px"
      >
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门人数" prop="empCount">
          <el-input-number v-model="formData.empCount" :min="0" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排序编号" prop="deptOrder">
          <el-input-number v-model="formData.deptOrder" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否显示" prop="isShow">
          <el-switch v-model="formData.isShow" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAllDepartments,
  addDepartment,
  updateDepartment,
  deleteDepartment,
  toggleDepartmentShow,
  addDepartmentChild
} from '@/network/department';
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { formatDateTime } from '@/common/utils';
import {
  Plus,
  Refresh,
  Edit,
  Delete
} from '@element-plus/icons-vue';

export default {
  name: 'DepartmentList',
  components: {
    Plus,
    Refresh,
    Edit,
    Delete
  },
  setup() {
    const allDepts = ref([]);
    const deptTreeData = ref([]);
    const tableLoading = ref(false);

    const dialogVisible = ref(false);
    const dialogTitle = ref('新增根部门');
    const isEdit = ref(false);
    const parentIDforAdd = ref(null);
    const formData = ref({
      deptID: null,
      deptName: '',
      empCount: 0,
      deptOrder: 1,
      isShow: true
    });
    const deptFormRef = ref(null);

    const formRules = {
      deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
      empCount: [{ required: true, message: '请输入部门人数', trigger: 'blur' }]
    };

    const buildTree = (list) => {
      const map = {};
      list.forEach(item => {
        map[item.deptID] = { ...item, children: [], hasChildren: false };
      });
      const tree = [];
      list.forEach(item => {
        const node = map[item.deptID];
        if (item.parentID == null) {
          tree.push(node);
        } else {
          const parent = map[item.parentID];
          if (parent) {
            parent.children.push(node);
            parent.hasChildren = true;
          } else {
            tree.push(node);
          }
        }
      });
      return tree;
    };

    const fetchDepartments = () => {
      tableLoading.value = true;
      getAllDepartments()
        .then(res => {
          if (res.code === 0) {
            allDepts.value = res.data || [];
            deptTreeData.value = buildTree(res.data || []);
          }
        })
        .catch(() => {})
        .finally(() => {
          tableLoading.value = false;
        });
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增根部门';
      isEdit.value = false;
      parentIDforAdd.value = null;
      formData.value = { deptID: null, deptName: '', empCount: 0, deptOrder: 1, isShow: true };
      dialogVisible.value = true;
    };

    const openAddChildDialog = (row) => {
      dialogTitle.value = `在「${row.deptName}」下添加子部门`;
      isEdit.value = false;
      parentIDforAdd.value = row.deptID;
      formData.value = { deptID: null, deptName: '', empCount: 0, deptOrder: 1, isShow: true };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = `编辑部门「${row.deptName}」`;
      isEdit.value = true;
      parentIDforAdd.value = row.parentID;
      formData.value = { ...row };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      deptFormRef.value.validate(valid => {
        if (!valid) return;
        const payload = {
          deptName: formData.value.deptName,
          empCount: formData.value.empCount,
          deptOrder: formData.value.deptOrder,
          isShow: formData.value.isShow
        };

        if (isEdit.value) {
          updateDepartment(formData.value.deptID, payload)
            .then(res => {
              ElMessage.success(res.message || '更新成功');
              fetchDepartments();
              dialogVisible.value = false;
            })
            .catch(() => {});
        } else {
          if (parentIDforAdd.value) {
            addDepartmentChild(parentIDforAdd.value, payload)
              .then(res => {
                ElMessage.success(res.message || '添加成功');
                fetchDepartments();
                dialogVisible.value = false;
              })
              .catch(() => {});
          } else {
            addDepartment(payload)
              .then(res => {
                ElMessage.success(res.message || '添加成功');
                fetchDepartments();
                dialogVisible.value = false;
              })
              .catch(() => {});
          }
        }
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除部门「${row.deptName}」吗？`, '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteDepartment(row.deptID)
            .then(res => {
              ElMessage.success(res.message || '删除成功');
              fetchDepartments();
            })
            .catch(() => {});
        })
        .catch(() => {});
    };

    const handleToggleShow = (row) => {
      toggleDepartmentShow(row.deptID)
        .then(res => {
          ElMessage.success(res.message || '切换成功');
        })
        .catch(() => {
          fetchDepartments();
        });
    };

    onMounted(() => {
      fetchDepartments();
    });

    return {
      deptTreeData,
      tableLoading,
      dialogVisible,
      dialogTitle,
      isEdit,
      formData,
      deptFormRef,
      formRules,
      fetchDepartments,
      openAddDialog,
      openEditDialog,
      submitForm,
      handleDelete,
      handleToggleShow,
      formatDateTime
    };
  }
};
</script>

<style scoped>
.department-list-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
}

.btn-group {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
}
</style>
