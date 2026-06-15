// Front/src/network/role.js
import request from './index';

export const getRoles = () => {
  return request.get('/role/list');
};

export const getRole = (id) => {
  return request.get(`/role/get/${id}`);
};

export const addRole = (data) => {
  return request.post('/role/add', data);
};

export const updateRole = (id, data) => {
  return request.put(`/role/update/${id}`, data);
};

export const deleteRole = (id) => {
  return request.delete(`/role/delete/${id}`);
};
