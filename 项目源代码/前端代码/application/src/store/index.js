import Vue from 'vue'
import Vuex from 'vuex'
import {
  RESET_ALL,
  RESET_LEVEL2_MENU,
  SET_DEPARTMENT,
  SET_EMPLOYEE, SET_LEVEL2_MENU,
  SET_POST,
  SET_ROLE,
  SET_TOKEN
} from '@/store/mutation_name'
import menu from '@/store/menu'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: '',
    department: [],
    post: [],
    role: [],
    employee: '',
    component: [

    ],
  },
  mutations: {
    [SET_LEVEL2_MENU]: (state, payload) => {
      state.component = payload.data
    },

    [SET_TOKEN]: (state, payload) => {
      state.token = payload.token
      localStorage.setItem('token', payload.token)
    },
    [SET_DEPARTMENT]: (state, payload) => {
      state.department = payload.department
    },
    [SET_POST]: (state, payload) => {
      state.post = payload.post
    },
    [SET_ROLE]: (state, payload) => {
      state.role = payload.role
    },
    [SET_EMPLOYEE]: (state, payload) => {
      state.employee = payload.employee
    },
    [RESET_ALL]: (state) => {
      state.token= ''
      state.department= []
      state.post= []
      state.role= []
      state.employee= ''
      state.tabs = [
        {
          title: '首页',
          name: '/home/index' //当前标签页的标记，采用路径
        },
      ]
      state.currentTab = '/home/index'
    }
  }
  ,
  actions: {
  },
  modules: {
    menu
  }
})
