// Front/src/network/department.js
import request from './index';

/**
 * 获取所有部门
 */
export function getAllDepartments() {
  return request({
    url: '/department/list',
    method: 'get'
  });
}

/**
 * 根据 ID 查询部门
 * @param {Number} id
 */
export function getDepartmentById(id) {
  return request({
    url: `/department/get/${id}`,
    method: 'get'
  });
}

/**
 * 新增根部门
 * @param {Object} data { deptName, deptOrder, isShow }
 */
export function addDepartment(data) {
  return request({
    url: '/department/add',
    method: 'post',
    data: data
  });
}

/**
 * 更新部门
 * @param {Number} id
 * @param {Object} data 更新字段
 */
export function updateDepartment(id, data) {
  return request({
    url: `/department/update/${id}`,
    method: 'put',
    data: data
  });
}

/**
 * 删除部门
 * @param {Number} id
 */
export function deleteDepartment(id) {
  return request({
    url: `/department/delete/${id}`,
    method: 'delete'
  });
}

/**
 * 切换部门显示状态
 * @param {Number} id
 */
export function toggleDepartmentShow(id) {
  return request({
    url: `/department/toggle/${id}`,
    method: 'put'
  });
}

/**
 * 添加子部门
 * @param {Number} parentId
 * @param {Object} data 子部门信息 { deptName, deptOrder, isShow }
 */
export function addDepartmentChild(parentId, data) {
  return request({
    url: `/department/addChild/${parentId}`,
    method: 'post',
    data: data
  });
}
