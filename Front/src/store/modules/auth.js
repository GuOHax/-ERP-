import { login, register } from '@/network/auth';
import { setToken, removeToken } from '@/common/auth';

const state = {
  currentUser: null
};

const mutations = {
  SET_USER(state, user) {
    state.currentUser = user;
  },
  CLEAR_USER(state) {
    state.currentUser = null;
  }
};

const actions = {
  async login({ commit }, payload) {
    const res = await login(payload);
    if (res.code === 0) {
      const user = res.data;
      commit('SET_USER', user);
      setToken(user.emNo + '_' + user.emId);
      return user;
    } else {
      throw new Error(res.message || '登录失败');
    }
  },
  async register({ commit }, payload) {
    const res = await register(payload);
    if (res.code === 0) {
      return res.data;
    } else {
      throw new Error(res.message || '注册失败');
    }
  },
  logout({ commit }) {
    commit('CLEAR_USER');
    removeToken();
  }
};

const getters = {
  currentUser: state => state.currentUser
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
