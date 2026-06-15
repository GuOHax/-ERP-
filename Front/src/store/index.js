// src/store/index.js
import { createStore } from 'vuex'
import auth from './modules/auth'
import user from './modules/user'
import employee from './modules/employee'
import department from './modules/department'

export default createStore({
  modules: {
    auth,
    user,
    employee,
    department
  }
})
