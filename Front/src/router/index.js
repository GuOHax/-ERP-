import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Dashboard from '@/views/Dashboard.vue'
import EmployeeList from '@/views/EmployeeList.vue'
import DepartmentList from '@/views/DepartmentList.vue'
import RoleList from '@/views/RoleList.vue'
import Profile from '@/views/Profile.vue'
import SalesList from '@/views/SalesList.vue'
import PurchaseList from '@/views/PurchaseList.vue'
import InventoryList from '@/views/InventoryList.vue'
import NotFound from '@/views/NotFound.vue'
import { getToken } from '@/common/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true },
    redirect: { name: 'Profile' },
    children: [
      // 系统管理
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { requiresAuth: true, title: '个人信息' }
      },
      {
        path: 'employees',
        name: 'EmployeeList',
        component: EmployeeList,
        meta: { requiresAuth: true, title: '员工管理' }
      },
      {
        path: 'departments',
        name: 'DepartmentList',
        component: DepartmentList,
        meta: { requiresAuth: true, title: '部门管理' }
      },
      {
        path: 'roles',
        name: 'RoleList',
        component: RoleList,
        meta: { requiresAuth: true, title: '角色管理' }
      },
      // 销售管理
      {
        path: 'sales',
        name: 'SalesList',
        component: SalesList,
        meta: { requiresAuth: true, title: '销售管理' }
      },
      // 采购管理
      {
        path: 'purchases',
        name: 'PurchaseList',
        component: PurchaseList,
        meta: { requiresAuth: true, title: '采购管理' }
      },
      // 库存管理
      {
        path: 'inventory',
        name: 'InventoryList',
        component: InventoryList,
        meta: { requiresAuth: true, title: '库存管理' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    if (getToken()) {
      next()
    } else {
      next({ name: 'Login' })
    }
  } else {
    next()
  }
})

export default router
