<template>
  <div class="login-wrapper">
    <div class="login-left">
      <div class="brand">
        <h1>ERP</h1>
        <p>企业管理平台</p>
      </div>
    </div>
    <div class="login-right">
      <div class="login-box">
        <div class="login-header">
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>
        <el-form
          :model="form"
          :rules="rules"
          ref="loginFormRef"
          label-width="0"
          class="login-form"
        >
          <el-form-item prop="emNo">
            <el-input
              v-model="form.emNo"
              placeholder="请输入员工编号"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="emPwd">
            <el-input
              v-model="form.emPwd"
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="loading"
              size="large"
            >
              登 录
            </el-button>
          </el-form-item>
          <div class="register-tip">
            没有账号？
            <router-link to="/register">立即注册</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '@/network/auth';
import { setToken } from '@/common/auth';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';

export default {
  name: 'Login',
  components: { User, Lock },
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const loginFormRef = ref(null);

    const form = ref({
      emNo: '',
      emPwd: ''
    });

    const rules = {
      emNo: [
        { required: true, message: '请输入员工编号', trigger: 'blur' }
      ],
      emPwd: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    };

    const handleLogin = () => {
      loginFormRef.value.validate(valid => {
        if (!valid) return;

        loading.value = true;
        login(form.value)
          .then(res => {
            if (res.code === 0) {
              setToken(res.data.emNo + '_' + res.data.emId);
              localStorage.setItem('erp_user_name', res.data.emName || res.data.emNo);
              ElMessage.success('登录成功，欢迎回来！');
              router.push({ name: 'Profile' });
            } else {
              ElMessage.error(res.message || '登录失败');
            }
          })
          .catch(() => {
            // 错误由拦截器统一提示
          })
          .finally(() => {
            loading.value = false;
          });
      });
    };

    return { form, rules, loginFormRef, handleLogin, loading };
  }
};
</script>

<style scoped>
.login-wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1a5f7a 0%, #08639b 50%, #08639b 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.brand h1 {
  font-size: 72px;
  font-weight: 900;
  color: #ffffff;
  letter-spacing: 12px;
  margin: 0;
}

.brand p {
  font-size: 24px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 12px;
  letter-spacing: 4px;
}

.login-right {
  width: 480px;
  background: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 320px;
}

.login-header {
  margin-bottom: 40px;
}

.login-header h2 {
  font-size: 28px;
  color: #303133;
  margin: 0 0 8px;
}

.login-header p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.login-form {
  margin-top: 0;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 8px;
  margin-top: 8px;
  background: linear-gradient(135deg, #08639b, #1a5f7a);
  border: none;
}

.login-btn:hover {
  background: linear-gradient(135deg, #1a5f7a, #08639b);
}

.register-tip {
  text-align: center;
  font-size: 14px;
  color: #909399;
  margin-top: 16px;
}

.register-tip a {
  color: #409eff;
  text-decoration: none;
}

.register-tip a:hover {
  text-decoration: underline;
}
</style>
