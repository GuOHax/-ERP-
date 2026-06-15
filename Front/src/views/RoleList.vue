<template>
  <div class="role-list-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon> 新增角色
      </el-button>
      <el-button @click="fetchRoles">
        <el-icon><Refresh /></el-icon> 刷新
      </el-button>
    </div>

    <!-- 角色表格 -->
    <el-table
      :data="roles"
      style="width: 100%; margin-top: 16px"
      border
      stripe
      v-loading="tableLoading"
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column prop="roleName" label="角色名称" min-width="150" />
      <el-table-column prop="roleKey" label="权限标识" width="180" />
      <el-table-column prop="roleSort" label="排序" width="100" align="center" />
      <el-table-column prop="roleStatus" label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.roleStatus === 1 ? 'success' : 'danger'" size="small">
            {{ scope.row.roleStatus === 1 ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170">
        <template #default="scope">{{ formatDateTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column prop="roleRemark" label="备注" min-width="200" show-overflow-tooltip />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="scope">
          <div class="btn-group">
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

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="520px"
      destroy-on-close
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="roleKey">
          <el-input v-model="formData.roleKey" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="排序" prop="roleSort">
          <el-input-number v-model="formData.roleSort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="roleStatus">
          <el-radio-group v-model="formData.roleStatus">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="roleRemark">
          <el-input type="textarea" v-model="formData.roleRemark" :rows="3" placeholder="备注信息" />
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
import { getRoles, addRole, updateRole, deleteRole } from '@/network/role';
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { formatDateTime } from '@/common/utils';
import { Plus, Refresh, Edit, Delete } from '@element-plus/icons-vue';

export default {
  name: 'RoleList',
  components: { Plus, Refresh, Edit, Delete },
  setup() {
    const roles = ref([]);
    const tableLoading = ref(false);
    const dialogVisible = ref(false);
    const dialogTitle = ref('新增角色');
    const isEdit = ref(false);
    const formRef = ref(null);
    const formData = ref({
      roleId: null,
      roleName: '',
      roleKey: '',
      roleSort: 0,
      roleStatus: 1,
      roleRemark: ''
    });

    const formRules = {
      roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
    };

    const fetchRoles = () => {
      tableLoading.value = true;
      getRoles()
        .then(res => {
          if (res.code === 0) roles.value = res.data || [];
        })
        .catch(() => {})
        .finally(() => { tableLoading.value = false; });
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增角色';
      isEdit.value = false;
      formData.value = { roleId: null, roleName: '', roleKey: '', roleSort: 0, roleStatus: 1, roleRemark: '' };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = `编辑角色「${row.roleName}」`;
      isEdit.value = true;
      formData.value = { ...row };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      formRef.value.validate(valid => {
        if (!valid) return;
        if (isEdit.value) {
          updateRole(formData.value.roleId, formData.value)
            .then(res => {
              ElMessage.success(res.message || '更新成功');
              fetchRoles();
              dialogVisible.value = false;
            })
            .catch(() => {});
        } else {
          addRole(formData.value)
            .then(res => {
              ElMessage.success(res.message || '添加成功');
              fetchRoles();
              dialogVisible.value = false;
            })
            .catch(() => {});
        }
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除角色「${row.roleName}」吗？`, '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.roleId)
          .then(res => {
            ElMessage.success(res.message || '删除成功');
            fetchRoles();
          })
          .catch(() => {});
      }).catch(() => {});
    };

    onMounted(() => { fetchRoles(); });

    return {
      roles, tableLoading, dialogVisible, dialogTitle, isEdit,
      formData, formRef, formRules, fetchRoles,
      openAddDialog, openEditDialog, submitForm, handleDelete,
      formatDateTime
    };
  }
};
</script>

<style scoped>
.role-list-container { padding: 20px; }
.toolbar { display: flex; gap: 10px; align-items: center; }
.btn-group { display: flex; flex-wrap: wrap; gap: 4px; align-items: center; }
</style>
