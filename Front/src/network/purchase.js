// Front/src/network/purchase.js
import request from './index';

export const getPurchases = () => {
  return request.get('/purchase/list');
};

export const getPurchase = (id) => {
  return request.get(`/purchase/get/${id}`);
};

export const addPurchase = (data) => {
  return request.post('/purchase/add', data);
};

export const updatePurchase = (id, data) => {
  return request.put(`/purchase/update/${id}`, data);
};

export const deletePurchase = (id) => {
  return request.delete(`/purchase/delete/${id}`);
};
