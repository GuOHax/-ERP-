const state = {
  list: []
};

const mutations = {
  SET_DEPARTMENTS(state, depts) {
    state.list = depts;
  }
};

const actions = {
  setDepartments({ commit }, depts) {
    commit('SET_DEPARTMENTS', depts);
  }
};

const getters = {
  departments: state => state.list
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
