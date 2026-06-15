// Front/src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

// 从 @element-plus/icons-vue 导入所有图标，并全局注册
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

const app = createApp(App);

app.use(router);
app.use(ElementPlus);

// 注册所有图标组件，之后在任何地方都可以直接用 <el-icon><Plus /></el-icon> 之类的写法
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.mount('#app');
