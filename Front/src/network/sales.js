// Front/src/network/sales.js
import request from './index';

export const getSalesList = () => {
  return request.get('/sales/list');
};

export const getSales = (id) => {
  return request.get(`/sales/get/${id}`);
};

export const addSales = (data) => {
  return request.post('/sales/add', data);
};

export const updateSales = (id, data) => {
  return request.put(`/sales/update/${id}`, data);
};

export const deleteSales = (id) => {
  return request.delete(`/sales/delete/${id}`);
};
