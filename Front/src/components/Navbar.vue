<!-- Front/src/components/Navbar.vue -->
<template>
  <div class="navbar">
    <div class="navbar-left">
      <span class="page-title">{{ pageTitle }}</span>
    </div>
    <div class="navbar-right">
      <el-dropdown @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" style="background: #409eff;">
            {{ userInitials }}
          </el-avatar>
          <span class="username">{{ userName || '用户' }}</span>
          <el-icon class="arrow"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { removeToken } from '@/common/auth';
import { useRouter } from 'vue-router';
import { ArrowDown, SwitchButton } from '@element-plus/icons-vue';

export default {
  name: 'Navbar',
  components: { ArrowDown, SwitchButton },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const userName = ref(localStorage.getItem('erp_user_name') || '');

    const pageTitle = computed(() => {
      return route.meta.title || 'ERP 管理平台';
    });

    const userInitials = computed(() => {
      const name = userName.value;
      if (!name) return 'U';
      return name.slice(0, 1).toUpperCase();
    });

    const handleCommand = (command) => {
      if (command === 'logout') {
        removeToken();
        router.push({ name: 'Login' });
      }
    };

    return { pageTitle, userName, userInitials, handleCommand };
  }
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  background: #ffffff;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #606266;
}

.arrow {
  color: #c0c4cc;
  font-size: 12px;
}
</style>
