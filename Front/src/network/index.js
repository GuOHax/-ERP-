// Front/src/network/index.js
import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';
import { removeToken } from '@/common/auth';

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
});

service.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          removeToken();
          router.push({ name: 'Login' });
          ElMessage.error('登录已过期，请重新登录');
          break;
        case 403:
          ElMessage.error('无权限访问');
          break;
        case 404:
          ElMessage.error('接口不存在');
          break;
        default:
          ElMessage.error(error.response.data?.message || '请求失败');
      }
    } else {
      ElMessage.error('网络连接异常');
    }
    return Promise.reject(error);
  }
);

export default service;
