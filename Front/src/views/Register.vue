<!-- src/views/Register.vue -->
<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="text-center">ERP 系统注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="100px">
        <el-form-item label="员工编号：" prop="emNo">
          <el-input v-model="registerForm.emNo" autocomplete="off" placeholder="请输入员工编号"></el-input>
        </el-form-item>

        <el-form-item label="姓名：" prop="emName">
          <el-input v-model="registerForm.emName" placeholder="请输入姓名"></el-input>
        </el-form-item>

        <el-form-item label="密码：" prop="emPwd">
          <el-input v-model="registerForm.emPwd" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item label="电话：" prop="emTel">
          <el-input v-model="registerForm.emTel" placeholder="请输入电话"></el-input>
        </el-form-item>

        <el-form-item label="邮箱：" prop="emEmail">
          <el-input v-model="registerForm.emEmail" placeholder="请输入邮箱"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%">注册</el-button>
        </el-form-item>

        <el-form-item>
          <span>已有账号？ <router-link to="/login">去登录</router-link></span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { register } from '@/network/auth';
import { ElMessage } from 'element-plus';

export default {
  name: 'Register',
  setup() {
    const router = useRouter();
    const registerFormRef = ref(null);
    const registerForm = ref({
      emNo: '',
      emName: '',
      emPwd: '',
      emTel: '',
      emEmail: ''
    });
    const rules = {
      emNo: [{ required: true, message: '请输入员工编号', trigger: 'blur' }],
      emName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      emPwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      emTel: [{ required: true, message: '请输入电话', trigger: 'blur' }],
      emEmail: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
    };

    const handleRegister = () => {
      registerFormRef.value.validate(async valid => {
        if (!valid) return;
        try {
          await register(registerForm.value);
          ElMessage.success('注册成功，请登录');
          router.push('/login');
        } catch (error) {
          // 错误由拦截器统一提示
        }
      });
    };

    return {
      registerForm,
      rules,
      registerFormRef,
      handleRegister
    };
  }
};
</script>

<style scoped>
.register-container {
  width: 100%;
  height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
}
.register-card {
  width: 500px;
  padding: 20px;
}
.text-center {
  text-align: center;
  margin-bottom: 20px;
}
</style>
