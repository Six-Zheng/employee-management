import {
  RESET_MENU_STATE,
  RESET_TAB, SET_MENU_DATA,
  SET_MENU_LIST,
  SET_PERMISSION_LIST,
  SET_ROUTE_STATUS,
  SET_TAB
} from '@/store/mutation_name'

export default {
  state: {
    menuList: [],
    permissionList: [],
    routeStatus: false,
    tabs: [
      {
        title: '首页',
        name: '/home/index' //当前标签页的标记，采用路径
      }
    ], //当前标签页组
    currentTab: '/home/index', //当前标签页姓名,
    menuData: []
  },
  mutations: {
    [SET_MENU_DATA] (state, payload) {
      state.menuData = payload.menuData
    },
    [SET_MENU_LIST] (state, payload) {
      state.menuList = payload.menuList
    },
    [SET_PERMISSION_LIST] (state, payload) {
      state.permissionList = payload.permissionList
    },
    [SET_ROUTE_STATUS] (state, payload) {
      state.routeStatus = payload.routeStatus
    },
    [SET_TAB] (state, payload) {
      // 判断标签页是否已经存在
      const isTabExist = state.tabs.findIndex(tab => tab.name === payload.tab.name)
      // 如果不存在，则将其加入标签页组中
      if (isTabExist === -1) {
        state.tabs.push({
          title: payload.tab.title,
          name: payload.tab.name
        })
      }
      state.currentTab = payload.tab.name
    },
    [RESET_TAB] (state, payload) {
      // 1. 获取当前的标签页
      const currentTab = state.currentTab;
      // 2. 获取当前的标签页
      const currentTabs = state.tabs
      // 3. 获取新的标签页
      const allTabs = [{
        title: '首页',
        name: '/home/index' //当前标签页的标记，采用路径
      }]
      for(let i = 0; i < payload.data.length; i++) {
        if (payload.data[i].children.length > 0) {
          for (let j = 0; j < payload.data[i].children.length; j++) {
            allTabs.push({
              name: '/home' + payload.data[i].children[j].path,
              title: payload.data[i].children[j].title,
            })
          }
        }
      }

      const newTabs = [

      ]

      let index = -1;
      let isExist = -1;

      // 查找当前标签页在标签组中的位置
      for (let i = 0; i < currentTabs.length; i++) {
        if (currentTabs[i].name == currentTab) {
          index = i;
        }
      }

      for (let i = 0; i < currentTabs.length; i++) {
        for (let j = 0; j < allTabs.length; j++) {
          const a = allTabs[j].title
          const b = currentTabs[i].title
          const currentTabName = currentTabs[i].name

          if (a == b) {
            newTabs.push({
              title: allTabs[j].title,
              name : allTabs[j].name
            })
          }
        }
      }

      for (let i = 0; i < newTabs.length; i++){
        if (newTabs[i].name == state.currentTab) {
          isExist= 1;
        }
      }

      // 4.引导移动
      state.tabs = newTabs

      if (isExist < 0) {
        for (let i = 0; i < index; i++) {
          const testName = currentTabs[i].name;

          for (let j = 0; j < newTabs.length; j++) {
            if (testName == newTabs[j].name)
              state.currentTab = newTabs[j].name
          }
        }


        // state.currentTab = newTabs[newTabs.length - 1].name
      }
      // state.currentTab = newTabs[newTabs.length - 1].name
    },
    [RESET_MENU_STATE] (state) {SET_ROUTE_STATUS
      state.menuList = []
      state.permissionList = []
      state.routeStatus = false
      state.currentTab = '/home/index'
      state.tabs = [
        {
          title: '首页',
          name: '/home/index'
        }
      ]
      state.department = []
      state.role = []
      state.post = []
      state.token = ''
    }
  }
}
