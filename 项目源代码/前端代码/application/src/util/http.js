import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'
import emailService from '@/api/emailService'
import employeeService from '@/api/employeeService'
import { RESET_ALL, RESET_MENU_STATE, SET_ROUTE_STATUS } from '@/store/mutation_name'
import store from '@/store/index'

// 自定义axios的基本信息
const http = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 配置请求拦截器
http.interceptors.request.use(
  config => {
    config.headers.token = localStorage.getItem('token')
    return config
  },
  error => {
    Message.error({
      duration: 1500,
      message: error
    })
    return Promise.reject(error)
  }
)

// 配置响应拦截器
http.interceptors.response.use(
  response => {
    if (response.data !== undefined) {
      const display = response.data
      if ((display.message!= undefined) && (display.message != "获取验证码成功")) {
        if (display.message.search('获取')) {
          Message.success(response.data)
        }
      }
    }
    response.data = response.data.data
    return response
  },
  error => {
    const data = error.response.data
    Message.error(data.message)
    if (data.code == 401) {
      localStorage.clear()
      sessionStorage.clear()
      store.commit({type: RESET_ALL})
      store.commit({type: RESET_MENU_STATE})
      router.push('/login')
      store.commit({
        type: SET_ROUTE_STATUS,
        routeStatus: false
      })
    }
    return Promise.reject(data)
  }
)

export default http
