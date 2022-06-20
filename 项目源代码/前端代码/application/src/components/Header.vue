<template>
  <div id="header">
    <div id="leftBox">
      <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><rect width="48" height="48" fill="white" fill-opacity="0.01"/><path d="M41 13.9997L24 4L7 13.9997L7 33.9998L24 44L41 33.9998V13.9997Z" fill="#4a90e2" stroke="#4a90e2" stroke-width="4" stroke-linejoin="round"/><path d="M16 18.9974L23.9932 24L31.9951 18.9974" stroke="#FFF" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><path d="M24 24V33" stroke="#FFF" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg>
      <div>员工信息管理系统</div>
    </div>
    <div id="rightBox">
      <div id="nameBox">欢迎您：{{employeeInformation.name}}</div>
      <el-dropdown trigger="click" @command="handleCommand">
        <el-avatar :src="employeeInformation.avatar" fit="fill"></el-avatar>
        <el-dropdown-menu>
          <el-dropdown-item class="informationBox">
            <div>
              <el-avatar :src="employeeInformation.avatar" fit="fill"></el-avatar>
              <div>
                <el-tag size="mini" effect="dark">{{employeeInformation.departmentName}}</el-tag>
                <div>{{employeeInformation.name}}</div>
              </div>
            </div>
          </el-dropdown-item>
          <el-dropdown-item :divided="true" command="center">个人中心</el-dropdown-item>
          <el-dropdown-item command="logout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { RESET_ALL, RESET_MENU_STATE, SET_EMPLOYEE, SET_ROUTE_STATUS, SET_TAB } from '@/store/mutation_name'
import employeeService from '@/api/employeeService'
import roleService from '@/api/roleService'
import roleMenuService from '@/api/roleMenuService'

export default {
  name: "Header",
  data() {
    return {

    }
  },
  computed: {
    employeeInformation: {
      get () {
        const departmentId = this.$store.state.employee.departmentId
        const department = this.$store.state.department.find(item => item.id === departmentId)
        return {
          name: this.$store.state.employee.name,
          avatar: this.$store.state.employee.avatar,
          departmentName: this.$store.state.employee.departmentName,
        }
      },
      set (val) {
        this.$store.commit(SET_EMPLOYEE, val)
      }
    }
  },

  methods: {
    setTab() {
      let tab = {
        title: '个人中心',
        name: '/home/center'
      }
      this.$store.commit({
        type: SET_TAB,
        tab: tab
      })
      this.$router.push('/home/center')
    },

    handleCommand(command) {
      if (command === 'logout') {
        this.logout()
      } else if (command === 'center') {
        this.setTab()
      }
    },

    logout() {
      employeeService.logout().then(res => {
        localStorage.clear()
        sessionStorage.clear()
        this.$store.commit({type: RESET_ALL})
        this.$store.commit({type: RESET_MENU_STATE})
        this.$store.commit({
          type: SET_ROUTE_STATUS,
          routeStatus: false
        })
        this.$router.push('/login')
      })
    }
  }
}
</script>

<style scoped lang="scss">
#header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  #leftBox {
    display: flex;
    justify-content: center;
    align-items: center;
    padding-left: 10px;
  }

  #rightBox {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 10px;
    padding: 5px;

    .el-avatar {
      width: 48px;
      height: 48px;
      display: block;
    }

    #nameBox {
      padding: 0 25px;
    }
  }
}
</style>

<style lang="scss">
  .el-dropdown-menu__item {
    width: 150px;
  }

  .informationBox {
    div {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .el-avatar {
        width: 64px;
        height: 64px;
      }

      div {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
      }
    }
  }
</style>
