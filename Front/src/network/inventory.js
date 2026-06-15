// Front/src/network/inventory.js
import request from './index';

export const getInventories = () => {
  return request.get('/inventory/list');
};

export const getInventory = (id) => {
  return request.get(`/inventory/get/${id}`);
};

export const addInventory = (data) => {
  return request.post('/inventory/add', data);
};

export const updateInventory = (id, data) => {
  return request.put(`/inventory/update/${id}`, data);
};

export const deleteInventory = (id) => {
  return request.delete(`/inventory/delete/${id}`);
};
