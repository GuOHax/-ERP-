<!-- Front/src/views/Dashboard.vue -->
<template>
  <div class="dashboard">
    <el-container style="height: 100vh">
      <el-aside :width="sidebarWidth" class="sidebar">
        <Sidebar />
      </el-aside>
      <el-container>
        <el-header class="navbar">
          <Navbar />
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';
import Navbar from '@/components/Navbar.vue';

export default {
  name: 'Dashboard',
  components: {
    Sidebar,
    Navbar
  },
  data() {
    return {
      sidebarWidth: '210px'
    };
  }
};
</script>

<style scoped>
.sidebar {
  background: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.navbar {
  background: #ffffff;
  padding: 0;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.main-content {
  padding: 0;
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
