<template>
  <div id="sider" style="height: 100%">
    <el-menu
      :default-active="this.$store.state.menu.currentTab"
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">
      <router-link to="/home/index">
        <el-menu-item index="/home/index" @click="setTab({name: '/home/index', title: '首页'})">
          <template slot="title">
            <span slot="title">首页</span>
          </template>
        </el-menu-item>
      </router-link>
<!--      根据角色的不同获取不同的菜单-->
      <el-submenu :index="item.path" v-for="item in menu" v-bind:key="menu.title">
        <template slot="title">
          <span>{{item.title}}</span>
        </template>
        <router-link :to="'/home' + subItem.path" v-for="subItem in item.children" v-bind:key="subItem.title">
          <el-menu-item :index="'/home' + subItem.path" @click="setTab(subItem)">
            <template slot="title">
              <span slot="title">{{subItem.title}}</span>
            </template>
          </el-menu-item>
        </router-link>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import store from '@/store'
import { SET_TAB } from '@/store/mutation_name'

export default {
  name: 'Sider',
  computed: {
    menu: {
      get () {
        return this.$store.state.menu.menuList
      }
    }
  },
  created () {
  },
  methods : {
    // 点击侧边栏菜单时，添加标签页，具体逻辑交由SET_TAB方法操作
    setTab(item) {
      let tab = {
        title: null,
        name: null
      }

      if (item.title === '首页') {
        tab.title = item.title
        tab.name = item.name
        this.$store.commit({
          type: SET_TAB,
          tab: tab
        })
      } else {
        tab.title = item.title
        tab.name = '/home' + item.path
        this.$store.commit({
          type: SET_TAB,
          tab: tab
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
  #sider {
    .el-menu-item {
      background: #70a1ff;
    }

    /deep/ .el-menu-item {
      text-align: left;
    }

    a {
      text-decoration: none;
    }

    /deep/ .el-menu {
      border-right: 0;
    }
  }
</style>

<style>
  .el-aside {
    background: #545c64;
  }
</style>
