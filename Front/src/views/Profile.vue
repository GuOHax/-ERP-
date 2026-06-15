<template>
  <div class="profile-container">
    <el-card class="profile-card" shadow="never">
      <div class="profile-header">
        <div class="avatar-wrapper">
          <el-avatar :size="90" :src="formData.emIcon" style="background: #409eff; font-size: 36px;">
            {{ formData.emName ? formData.emName.slice(0, 1) : 'U' }}
          </el-avatar>
          <el-upload
            class="avatar-upload-btn"
            :action="uploadAction"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            name="file"
            accept="image/*"
          >
            <el-button size="small" type="primary">更换头像</el-button>
          </el-upload>
        </div>
        <div class="user-info">
          <h2>{{ formData.emName }}</h2>
          <p class="user-meta">
            <el-tag size="small" type="info">{{ formData.emNo }}</el-tag>
            <el-tag v-if="formData.emStatus === 1" size="small" type="success">启用中</el-tag>
            <el-tag v-else size="small" type="danger">已禁用</el-tag>
          </p>
        </div>
      </div>

      <el-divider />

      <el-form
        :model="formData"
        :rules="rules"
        ref="profileFormRef"
        label-width="100px"
        class="profile-form"
      >
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="员工编号">
              <el-input v-model="formData.emNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="emName">
              <el-input v-model="formData.emName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="formData.emSex">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
                <el-radio :label="0">未知</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="emTel">
              <el-input v-model="formData.emTel" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="emEmail">
              <el-input v-model="formData.emEmail" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号状态">
              <el-tag :type="formData.emStatus === 1 ? 'success' : 'danger'">
                {{ formData.emStatus === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input type="textarea" v-model="formData.emRemark" :rows="3" placeholder="备注信息" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">修改密码</el-divider>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="新密码" prop="newPwd">
              <el-input v-model="formData.newPwd" type="password" placeholder="留空则不修改密码" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPwd">
              <el-input v-model="formData.confirmPwd" type="password" placeholder="再次输入新密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-footer">
          <el-button type="primary" @click="submitForm" :loading="loading">保存修改</el-button>
          <el-button @click="loadProfile">重置</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { updateEmployee } from '@/network/employee';
import { getEmployeesByPage } from '@/network/employee';
import { formatDate } from '@/common/utils';
import { getToken } from '@/common/auth';

export default {
  name: 'Profile',
  setup() {
    const profileFormRef = ref(null);
    const loading = ref(false);
    const uploadAction = '/api/employee/upload';
    const formatDateTime = formatDate;

    const formData = ref({
      emId: null,
      emNo: '',
      emName: '',
      emPwd: '',
      emTel: '',
      emEmail: '',
      emSex: 0,
      emIcon: '',
      emRemark: '',
      emStatus: 1,
      newPwd: '',
      confirmPwd: ''
    });

    const validateConfirmPwd = (rule, value, callback) => {
      if (formData.value.newPwd && value !== formData.value.newPwd) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    const rules = {
      emName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      emTel: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      emEmail: [
        { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
      ],
      confirmPwd: [{ validator: validateConfirmPwd, trigger: 'blur' }]
    };

    const loadProfile = () => {
      const token = getToken();
      if (!token) return;
      const parts = token.split('_');
      const emId = parts[parts.length - 1];
      getEmployeesByPage(1, 999, '')
        .then(res => {
          if (res.code === 0) {
            const list = res.data.content || [];
            const user = list.find(e => e.emId == emId) || list[0];
            if (user) {
              formData.value = {
                ...user,
                newPwd: '',
                confirmPwd: '',
                emPwd: ''
              };
            }
          }
        })
        .catch(() => {});
    };

    const submitForm = () => {
      profileFormRef.value.validate(valid => {
        if (!valid) return;
        loading.value = true;
        const payload = { ...formData.value };
        if (!payload.newPwd) {
          delete payload.newPwd;
          delete payload.confirmPwd;
          delete payload.emPwd;
        } else {
          payload.emPwd = payload.newPwd;
          delete payload.newPwd;
          delete payload.confirmPwd;
        }
        updateEmployee(formData.value.emId, payload)
          .then(res => {
            ElMessage.success(res.message || '保存成功');
          })
          .catch(() => {})
          .finally(() => { loading.value = false; });
      });
    };

    const handleUploadSuccess = (res) => {
      if (res.code === 0) {
        formData.value.emIcon = res.data;
        ElMessage.success('头像上传成功');
      } else {
        ElMessage.error(res.message || '上传失败');
      }
    };

    onMounted(() => { loadProfile(); });

    return { formData, profileFormRef, loading, uploadAction, rules, submitForm, handleUploadSuccess, formatDateTime, loadProfile };
  }
};
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.profile-card {
  border-radius: 12px;
  border: none;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 10px 0;
}

.avatar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.user-info h2 {
  margin: 0 0 8px;
  font-size: 22px;
  color: #303133;
}

.user-meta {
  display: flex;
  gap: 8px;
  align-items: center;
  margin: 0;
}

.profile-form {
  padding: 10px 0;
}

.form-footer {
  padding-top: 16px;
  text-align: center;
}
</style>
