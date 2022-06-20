<template>
  <div id="index">
    <h1 style="padding-bottom: 40px; text-align: center">欢迎使用员工信息管理系统，可通过浏览以下提示图片进行操作</h1>
    <div class="block">
      <el-carousel height="750px" :interval="10000">
        <el-carousel-item v-for="item in images" :key="item">
          <el-image :src="item">
          </el-image>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
import employeeService from '@/api/employeeService'
import departmentService from '@/api/departmentService'
import postService from '@/api/postService'
import roleService from '@/api/roleService'

export default {
  name: "Index",
  data() {
    return {
      images: [
        require('../img/1.png'),
        require('../img/2.png')
      ]
    }
  }
}
</script>

<style scoped>
  #index {
    padding: 0 30px;
  }
</style>
