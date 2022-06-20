<template>
  <div id="home">
    <el-container>
      <el-header>
        <Header></Header>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <Sider></Sider>
        </el-aside>
        <el-main>
          <Tabs></Tabs>
          <transition name="fade-transform" mode="out-in">
            <router-view :key="$route.fullPath"/>
          </transition>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
  .el-main {
    padding: 0;
    height: calc(100vh - 60px);
  }

  .fade-transform-leave-active,

  .fade-transform-enter-active {
    transition: all 0.35s;
  }

  .fade-transform-enter {
    opacity: 0;
    transform: translateX(-30px);
  }

  .fade-transform-leave-to {
    opacity: 0;
    transform: translateX(30px);
  }
  .el-aside {
    background-color: whitesmoke;
    color: #333;
    transition: width 0.25s;
    -webkit-transition: width 0.25s;
    -moz-transition: width 0.25s;
    -webkit-transition: width 0.25s;
    -o-transition: width 0.25s;
  }

</style>

<style>
.horizontal-collapse-transition {
  transition: 0s width ease-in-out, 0s padding-left ease-in-out, 0s padding-right ease-in-out;
}
</style>

<script>
import Header from '@/components/Header'
import Sider from '@/components/Sider'
import Tabs from '@/components/Tabs'
import SideMenu from '@/components/SideMenu'
export default {
  components: {Sider, Header, Tabs },
  data () {
    return {

    }
  }
}
</script>
