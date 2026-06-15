// Front/src/common/auth.js
const TokenKey = 'erp_token';

export function setToken(token) {
  localStorage.setItem(TokenKey, token);
}

export function getToken() {
  return localStorage.getItem(TokenKey);
}

export function removeToken() {
  localStorage.removeItem(TokenKey);
}
