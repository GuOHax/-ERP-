const state = {
  list: []
};

const mutations = {
  SET_EMPLOYEES(state, employees) {
    state.list = employees;
  }
};

const actions = {
  setEmployees({ commit }, employees) {
    commit('SET_EMPLOYEES', employees);
  }
};

const getters = {
  employees: state => state.list
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
