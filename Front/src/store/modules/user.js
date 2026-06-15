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

const getters = {
  currentUser: state => state.currentUser
};

export default {
  namespaced: true,
  state,
  mutations,
  getters
};
