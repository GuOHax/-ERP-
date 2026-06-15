<!-- Front/src/views/NotFound.vue -->
<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面未找到</p>
    <el-button type="primary" @click="goHome">返回首页</el-button>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: 'NotFound',
  setup() {
    const router = useRouter();
    const goHome = () => {
      router.push({ name: 'Dashboard' });
    };
    return { goHome };
  }
};
</script>

<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
}
.not-found h1 {
  font-size: 96px;
  margin: 0;
}
.not-found p {
  font-size: 24px;
  margin: 20px 0;
}
</style>
