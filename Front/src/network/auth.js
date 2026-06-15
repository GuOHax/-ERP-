import request from './index';

export const login = data => {
  return request.post('/auth/login', data);
};

export const register = data => {
  return request.post('/auth/register', data);
};
