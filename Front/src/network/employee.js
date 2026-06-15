// Front/src/network/employee.js
import request from './index';

/**
 * 分页/模糊查询员工
 * @param {Number} page 当前页（从 1 开始）
 * @param {Number} size 每页大小
 * @param {String} keyword 模糊查询关键字
 */
export function getEmployeesByPage(page, size, keyword) {
  return request({
    url: '/employee/page',
    method: 'get',
    params: {
      page,
      size,
      keyword
    }
  });
}

/**
 * 获取所有员工（不分页场景）
 */
export function getAllEmployees() {
  return request({
    url: '/employee/list',
    method: 'get'
  });
}

/**
 * 根据 ID 查询员工
 * @param {Number} id
 */
export function getEmployeeById(id) {
  return request({
    url: `/employee/get/${id}`,
    method: 'get'
  });
}

/**
 * 新增员工
 * @param {Object} data Employee 对象
 */
export function addEmployee(data) {
  return request({
    url: '/employee/add',
    method: 'post',
    data: data
  });
}

/**
 * 更新员工
 * @param {Number} id
 * @param {Object} data 更新字段
 */
export function updateEmployee(id, data) {
  return request({
    url: `/employee/update/${id}`,
    method: 'put',
    data: data
  });
}

/**
 * 删除员工
 * @param {Number} id
 */
export function deleteEmployee(id) {
  return request({
    url: `/employee/delete/${id}`,
    method: 'delete'
  });
}

/**
 * 切换员工状态（启用/禁用）
 * @param {Number} id
 */
export function toggleEmployeeStatus(id) {
  return request({
    url: `/employee/toggle/${id}`,
    method: 'put'
  });
}

/**
 * 复制员工
 * @param {Number} id
 */
export function copyEmployee(id) {
  return request({
    url: `/employee/copy/${id}`,
    method: 'post'
  });
}

/**
 * 导出员工列表为 CSV/Excel
 */
export function exportEmployees() {
  return request({
    url: '/employee/export',
    method: 'get',
    responseType: 'blob'
  });
}

/**
 * 上传员工头像
 * @param {Number} id
 * @param {FormData} formData 包含文件对象
 */
export function uploadAvatar(id, formData) {
  return request({
    url: `/employee/upload/${id}`,
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  });
}
