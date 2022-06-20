/* eslint-disable prefer-const */
import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import store from '../store/index'
import axios from 'axios'
import {
  SET_DEPARTMENT, SET_EMPLOYEE,
  SET_MENU_LIST,
  SET_PERMISSION_LIST,
  SET_POST,
  SET_ROLE,
  SET_ROUTE_STATUS
} from '@/store/mutation_name'
import menuService from '@/api/menuService'
import departmentService from '@/api/departmentService'
import postService from '@/api/postService'
import roleService from '@/api/roleService'
import employeeService from '@/api/employeeService'
import http from '@/util/http'

Vue.use(axios)
Vue.use(VueRouter)

// 基本路由
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login'),
    meta: {
      title: '登录页面'
    },
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: {
      title: '主页'
    },
    children: [
      {
        path: '/home/index',
        name: 'Index',
        component: () => import('@/components/Index'),
        meta: {
          title: '首页'
        }
      },
      {
        path: '/home/center',
        name: 'Center',
        component: () => import('@/views/Center'),
        meta: {
          title: '个人中心'
        }
      },
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// 进入路由之前的处理
router.beforeEach((to, from, next) => {
  const routeStatus = store.state.menu.routeStatus
  const token = localStorage.getItem('token')

  if (to.path === '/login') { // 若为登录页面请求，直接放行
    next()
  } else if (!token) { // 若无携带token，则表明token已过期，重定向登录页
    next({
      path: '/login'
    })
  } else if (token && !routeStatus) { // 二若有携带token，但无其路由信息，则表明其为第一次登录，则对其进行路由的添加
    const allInformation = axios.all(
      [
        menuService.getMenuByToken(),
        departmentService.getDepartmentList(),
        postService.getPostList(),
        roleService.getRoleList(),
        employeeService.getEmployeeByToken(),
      ]
    ).then(res => {
      const menu = res[0].data.menu
      const permission = res[0].data.permission
      console.log(permission)
      store.commit({ type: SET_MENU_LIST, menuList: menu })
      store.commit({ type: SET_PERMISSION_LIST, permissionList: permission})
      store.commit({ type: SET_DEPARTMENT, department: res[1].data.department })
      store.commit({ type: SET_POST, post: res[2].data.post })
      store.commit({ type: SET_ROLE, role: res[3].data.role })

      const employee = res[4].data.employee
      employee.departmentName = res[4].data.departmentName
      employee.postName = res[4].data.postName
      employee.roleName = res[4].data.roleName
      store.commit({ type: SET_EMPLOYEE, employee: employee })

      menu.forEach(level1Menu => {
        const currentRoute = {
          path   : level1Menu.path,
          component : level1Menu.component,
          meta : {
            title: level1Menu.title,
          },
          children: []
        }
        router.addRoute(currentRoute)

        if (level1Menu.children) {
          level1Menu.children.forEach(level2Menu => {
            const currentChildRoute = {
              path: level2Menu.path,
              component: () => import('@/views' + level2Menu.component + '.vue'),
              meta: {
                title: level2Menu.title,
              },
              alias: '/home' + level2Menu.path,
            }
            routes[1].children.push(currentChildRoute)
          })
        }
        router.addRoutes(routes)
        store.commit({ type: SET_ROUTE_STATUS, routeStatus: true })
      })
    })
  }


  next()
})

export default router
