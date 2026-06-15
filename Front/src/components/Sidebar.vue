<!-- Front/src/components/Sidebar.vue -->
<template>
  <el-menu
    :default-active="activeMenu"
    class="sidebar-menu"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409eff"
    :unique-opened="true"
    :collapse="false"
    router
  >
    <!-- Logo 区域 -->
    <div class="sidebar-logo">
      <el-icon class="logo-icon"><Shop /></el-icon>
      <span class="logo-text">ERP 管理平台</span>
    </div>

    <!-- 系统管理（员工管理、部门管理、角色管理、个人信息） -->
    <el-sub-menu index="system">
      <template #title>
        <el-icon><Setting /></el-icon>
        <span>系统管理</span>
      </template>
      <el-menu-item index="/dashboard/profile">个人信息</el-menu-item>
      <el-menu-item index="/dashboard/employees">员工管理</el-menu-item>
      <el-menu-item index="/dashboard/departments">部门管理</el-menu-item>
      <el-menu-item index="/dashboard/roles">角色管理</el-menu-item>
    </el-sub-menu>

    <!-- 销售管理 -->
    <el-menu-item index="/dashboard/sales">
      <el-icon><Sell /></el-icon>
      <template #title>销售管理</template>
    </el-menu-item>

    <!-- 采购管理 -->
    <el-menu-item index="/dashboard/purchases">
      <el-icon><ShoppingCart /></el-icon>
      <template #title>采购管理</template>
    </el-menu-item>

    <!-- 库存管理 -->
    <el-menu-item index="/dashboard/inventory">
      <el-icon><Box /></el-icon>
      <template #title>库存管理</template>
    </el-menu-item>

    <!-- 退出登录 -->
    <el-menu-item index="/login" @click="handleLogout" class="logout-item">
      <el-icon><SwitchButton /></el-icon>
      <template #title>退出登录</template>
    </el-menu-item>
  </el-menu>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { removeToken } from '@/common/auth';
import { useRouter } from 'vue-router';
import {
  Shop,
  Setting,
  Sell,
  ShoppingCart,
  Box,
  SwitchButton
} from '@element-plus/icons-vue';

export default {
  name: 'Sidebar',
  components: {
    Shop,
    Setting,
    Sell,
    ShoppingCart,
    Box,
    SwitchButton
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const activeMenu = computed(() => route.path);

    const handleLogout = () => {
      removeToken();
      router.push({ name: 'Login' });
    };

    return { activeMenu, handleLogout };
  }
};
</script>

<style scoped>
.sidebar-menu {
  height: 100%;
  border-right: none;
  overflow-y: auto;
}

.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: #263445;
  border-bottom: 1px solid #1f2d3d;
  overflow: hidden;
}

.logo-icon {
  font-size: 22px;
  color: #409eff;
  flex-shrink: 0;
}

.logo-text {
  font-size: 15px;
  font-weight: bold;
  color: #ffffff;
  letter-spacing: 1px;
  white-space: nowrap;
}

.logout-item {
  margin-top: auto;
  border-top: 1px solid #1f2d3d;
}
</style>
