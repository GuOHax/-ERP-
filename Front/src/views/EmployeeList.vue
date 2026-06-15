<template>
  <div class="employee-list-container">
    <!-- 查询过滤栏 -->
    <div class="filter-bar">
      <el-input
        v-model="keyword"
        placeholder="输入编号 / 姓名 / 电话搜索"
        style="width: 280px"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="handleSearch">
        <el-icon><Search /></el-icon> 查询
      </el-button>
      <el-button @click="handleReset">
        <el-icon><RefreshLeft /></el-icon> 重置
      </el-button>
      <div class="filter-bar-right">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon> 新增员工
        </el-button>
        <el-button type="success" @click="handleExport">
          <el-icon><Download /></el-icon> 导出 Excel
        </el-button>
      </div>
    </div>

    <!-- 员工表格 -->
    <el-table
      :data="employees"
      style="width: 100%"
      border
      stripe
      v-loading="tableLoading"
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column prop="emNo" label="员工编号" width="120" />
      <el-table-column prop="emName" label="姓名" width="100" />
      <el-table-column prop="emEmail" label="邮箱" min-width="160" />
      <el-table-column prop="deptId" label="部门" width="120" align="center">
        <template #default="scope">{{ getDeptName(scope.row.deptId) }}</template>
      </el-table-column>
      <el-table-column prop="emTel" label="电话" width="130" />
      <el-table-column prop="emSex" label="性别" width="70" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.emSex === 1" type="primary" size="small">男</el-tag>
          <el-tag v-else-if="scope.row.emSex === 2" type="danger" size="small">女</el-tag>
          <el-tag v-else type="info" size="small">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="emStatus" label="状态" width="80" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.emStatus === 1 ? 'success' : 'info'" size="small">
            {{ scope.row.emStatus === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="emCreateTime" label="创建时间" width="170">
        <template #default="scope">{{ formatDate(scope.row.emCreateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="300" fixed="right">
        <template #default="scope">
          <div class="btn-group">
            <el-button size="small" type="primary" @click="openEditDialog(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button
              size="small"
              :type="scope.row.emStatus === 1 ? 'warning' : 'success'"
              @click="handleToggle(scope.row)"
            >
              <el-icon><Switch /></el-icon>
              {{ scope.row.emStatus === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button size="small" type="info" @click="handleCopy(scope.row)">
              <el-icon><DocumentCopy /></el-icon> 复制
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="620px"
      destroy-on-close
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="empFormRef"
        label-width="100px"
      >
        <el-form-item label="员工编号" prop="emNo">
          <el-input v-model="formData.emNo" :disabled="isEdit" placeholder="登录账号" />
        </el-form-item>
        <el-form-item label="姓名" prop="emName">
          <el-input v-model="formData.emName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="emPwd" v-if="!isEdit">
          <el-input v-model="formData.emPwd" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="电话" prop="emTel">
          <el-input v-model="formData.emTel" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="emEmail">
          <el-input v-model="formData.emEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-select v-model="formData.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option label="无" :value="null" />
            <el-option
              v-for="dept in departments"
              :key="dept.deptID"
              :label="dept.deptName"
              :value="dept.deptID"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="emSex">
          <el-radio-group v-model="formData.emSex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-upload
              class="avatar-uploader"
              :action="uploadAction"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :data="{ id: formData.emId }"
              name="file"
              accept="image/*"
            >
              <img v-if="formData.emIcon" :src="formData.emIcon" class="avatar-img" />
              <el-icon v-else class="avatar-icon"><Plus /></el-icon>
            </el-upload>
            <div class="avatar-tip">点击上传员工头像</div>
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="emRemark">
          <el-input type="textarea" v-model="formData.emRemark" :rows="3" placeholder="备注信息" />
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
  addEmployee,
  updateEmployee,
  deleteEmployee,
  toggleEmployeeStatus,
  copyEmployee,
  exportEmployees
} from '@/network/employee';
import { getEmployeesByPage } from '@/network/employee';
import { getAllDepartments } from '@/network/department';
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { formatDate } from '@/common/utils';
import axios from 'axios';
import {
  Search,
  RefreshLeft,
  Plus,
  Download,
  Edit,
  Switch,
  DocumentCopy,
  Delete
} from '@element-plus/icons-vue';

export default {
  name: 'EmployeeList',
  components: {
    Search,
    RefreshLeft,
    Plus,
    Download,
    Edit,
    Switch,
    DocumentCopy,
    Delete
  },
  setup() {
    const keyword = ref('');
    const employees = ref([]);
    const total = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const tableLoading = ref(false);

    const dialogVisible = ref(false);
    const dialogTitle = ref('新增员工');
    const isEdit = ref(false);
    const formData = ref({
      emId: null,
      emNo: '',
      emName: '',
      emPwd: '',
      emTel: '',
      emEmail: '',
      emSex: 0,
      emIcon: '',
      deptId: null,
      emRemark: '',
      emStatus: 1
    });
    const empFormRef = ref(null);

    const departments = ref([]);
    const uploadAction = '/api/employee/upload';

    const getDeptName = (deptId) => {
      if (!deptId) return '—';
      const dept = departments.value.find(d => d.deptID === deptId);
      return dept ? dept.deptName : '—';
    };

    const fetchDepartments = () => {
      getAllDepartments()
        .then(res => { if (res.code === 0) departments.value = res.data || []; })
        .catch(() => {});
    };

    const formRules = {
      emNo: [{ required: true, message: '请输入员工编号', trigger: 'blur' }],
      emName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      emPwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    };

    const fetchEmployees = () => {
      tableLoading.value = true;
      getEmployeesByPage(currentPage.value, pageSize.value, keyword.value)
        .then(res => {
          if (res.code === 0) {
            employees.value = res.data.content || [];
            total.value = res.data.totalElements || 0;
          }
        })
        .catch(() => {})
        .finally(() => {
          tableLoading.value = false;
        });
    };

    const handleSearch = () => {
      currentPage.value = 1;
      fetchEmployees();
    };

    const handleReset = () => {
      keyword.value = '';
      currentPage.value = 1;
      fetchEmployees();
    };

    const handlePageChange = (page) => {
      currentPage.value = page;
      fetchEmployees();
    };

    const handleSizeChange = (size) => {
      pageSize.value = size;
      currentPage.value = 1;
      fetchEmployees();
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增员工';
      isEdit.value = false;
      formData.value = {
        emId: null,
        emNo: '',
        emName: '',
        emPwd: '',
        emTel: '',
        emEmail: '',
        emSex: 0,
        emIcon: '',
        deptId: null,
        emRemark: '',
        emStatus: 1
      };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = '编辑员工';
      isEdit.value = true;
      formData.value = { ...row, emPwd: '' };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      empFormRef.value.validate((valid) => {
        if (!valid) return;
        const payload = { ...formData.value };
        if (isEdit.value) {
          delete payload.emPwd;
          delete payload.emCreateTime;
          updateEmployee(formData.value.emId, payload)
            .then(res => {
              ElMessage.success(res.message || '更新成功');
              fetchEmployees();
              dialogVisible.value = false;
            })
            .catch(() => {});
        } else {
          addEmployee(payload)
            .then(res => {
              ElMessage.success(res.message || '添加成功');
              fetchEmployees();
              dialogVisible.value = false;
            })
            .catch(() => {});
        }
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除员工「${row.emName}」（${row.emNo}）吗？`, '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteEmployee(row.emId)
            .then(res => {
              ElMessage.success(res.message || '删除成功');
              fetchEmployees();
            })
            .catch(() => {});
        })
        .catch(() => {});
    };

    const handleToggle = (row) => {
      const action = row.emStatus === 1 ? '停用' : '启用';
      ElMessageBox.confirm(`确定${action}员工「${row.emName}」吗？`, `确认${action}`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          toggleEmployeeStatus(row.emId)
            .then(res => {
              ElMessage.success(res.message || action + '成功');
              fetchEmployees();
            })
            .catch(() => {});
        })
        .catch(() => {});
    };

    const handleCopy = (row) => {
      ElMessageBox.confirm(`确定复制员工「${row.emName}」吗？`, '确认复制', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      })
        .then(() => {
          copyEmployee(row.emId)
            .then(res => {
              ElMessage.success(res.message || '复制成功');
              fetchEmployees();
            })
            .catch(() => {});
        })
        .catch(() => {});
    };

    const handleExport = () => {
      ElMessage.info('正在导出...');
      axios({
        url: '/api/employee/export',
        method: 'get',
        responseType: 'blob'
      })
        .then(response => {
          const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `员工列表_${new Date().toISOString().slice(0, 10)}.xlsx`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          ElMessage.success('导出成功');
        })
        .catch(() => {
          ElMessage.error('导出失败');
        });
    };

    const handleUploadSuccess = (res) => {
      if (res.code === 0) {
        formData.value.emIcon = res.data;
        ElMessage.success('上传成功');
      } else {
        ElMessage.error(res.message || '上传失败');
      }
    };

    const handleUploadError = () => {
      ElMessage.error('上传失败');
    };

    onMounted(() => {
      fetchDepartments();
      fetchEmployees();
    });

    return {
      keyword,
      employees,
      total,
      currentPage,
      pageSize,
      tableLoading,
      dialogVisible,
      dialogTitle,
      isEdit,
      formData,
      empFormRef,
      departments,
      uploadAction,
      formRules,
      handleSearch,
      handleReset,
      handlePageChange,
      handleSizeChange,
      openAddDialog,
      openEditDialog,
      submitForm,
      handleDelete,
      handleToggle,
      handleCopy,
      handleExport,
      handleUploadSuccess,
      handleUploadError,
      formatDate,
      getDeptName,
      fetchDepartments
    };
  }
};
</script>

<style scoped>
.employee-list-container {
  padding: 20px;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.filter-bar-right {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.pagination-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-uploader {
  width: 80px;
  height: 80px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-icon {
  font-size: 24px;
  color: #8c939d;
}

.avatar-tip {
  font-size: 12px;
  color: #909399;
}

.btn-group {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
}
</style>
