<template>
  <div id="employee">
    <!--    顶部操作栏-->
    <div class="pagination">
      <div>
        <el-button size="mini" type="primary" @click="openEmployeeDialogForm()" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:POST_SINGLE')">添加员工</el-button>
        <el-button size="mini" type="danger" @click="deleteEmployeeMultiple(employeeId)" :disabled="this.employeeId.length === 0" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:DELETE_MULTIPLE')">批量删除员工</el-button>
        <el-button size="mini" type="success" @click="output">导出员工信息</el-button>
      </div>
      <!--    分页信息-->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="pagination.sizes"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </div>
    <!--    员工信息-->
    <div id="table">
      <el-table :data="employee"  @selection-change="handleSelectionChange" v-loading="loadingStatus">
        <el-table-column
          v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:DELETE_MULTIPLE')"
          prop="id" type="selection" width="55">
        </el-table-column>
        <el-table-column type="index" width="80" label="序号">
        </el-table-column>
        <el-table-column
          width="120"
          prop="username"
          label="账户">
        </el-table-column>
        <el-table-column prop="avatar" label="头像" width="120">
          <template slot-scope="scope">
            <el-avatar fit="full" :size="48" :src="scope.row.avatar" :key="scope.row.avatar">
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="160">
        </el-table-column>
        <el-table-column
          prop="sex"
          width="120"
          label="性别">
          <template slot-scope="scope">
            <el-tag effect="dark" v-if="scope.row.sex === 1">
              男
            </el-tag>
            <el-tag effect="dark" type="danger" v-else>
              女
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="roleId"
          :formatter="formatter"
          width="120"
          label="角色"
        >
        </el-table-column>
        <el-table-column
          prop="departmentId"
          width="160"
          :formatter="formatter"
          label="部门">
        </el-table-column>
        <el-table-column
          width="160"
          prop="postId"
          :formatter="formatter"
          label="岗位">
        </el-table-column>
        <el-table-column
          width="200"
          prop="email"
          label="邮箱">
        </el-table-column>
        <el-table-column
          width="200"
          prop="phone"
          label="电话">
        </el-table-column>
        <el-table-column
          align="right">
          <template slot="header" slot-scope="scope">
            <el-input
              @input="getEmployeeMultipleByName(pagination.current, pagination.size, name)"
              v-model="name"
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:GET_MULTIPLE')"
              placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:UPDATE_SINGLE')"
              @click="openEmployeeDialogForm(scope.row.id)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:EMPLOYEE:DELETE_SINGLE')"
              @click="deleteEmployeeSingle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--    员工表单信息-->
    <el-dialog
      :close-on-click-modal="false"
      title="员工信息"
      @close="closeEmployeeDialogForm('departmenForm')"
      width="25%"
      top="10vh"
      :visible.sync="employeeDialogFormVisiable"
      >
      <el-form :rules="employeeFormRules" :model="employeeForm" label-position="right" label-width="100px" ref="employeeForm">
          <el-upload
            style="display: flex; justify-content: center; align-items: center; padding-bottom: 15px"
            :http-request="httpRequest"
            action=""
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <div style="width: 78px;height: 78px;">

              <el-avatar style="width: 72px; height: 72px" fit="scale-down" :src="employeeForm.avatar" :key="employeeForm.avatar"></el-avatar>
            </div>
          </el-upload>
        <el-form-item label="账户" prop="username" >
          <el-input v-model="employeeForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name" >
          <el-input v-model="employeeForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="employeeForm.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleId" >
          <el-select v-model="employeeForm.roleId">
            <el-option v-for="role in $store.state.role" :key="role.id" :value="role.id" :label="role.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId" >
          <el-select v-model="employeeForm.departmentId">
            <el-option v-for="department in $store.state.department" :key="department.id" :value="department.id" :label="department.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="postId" >
          <el-select v-model="employeeForm.postId">
            <el-option v-for="post in $store.state.post" :key="post.id" :value="post.id" :label="post.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" >
          <el-input v-model="employeeForm.email" autocomplete="off" :disabled="employeeForm.name === '待分配员工' && employeeForm.id === 1 "></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone" >
          <el-input v-model="employeeForm.phone" autocomplete="off" :disabled="employeeForm.name === '待分配员工' && employeeForm.id === 1 "></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEmployeeDialogForm('employeeForm')">取 消</el-button>
        <el-button type="primary" @click="employeeForm.id?putEmployeeSingle(employeeForm):postEmployeeSingle(employeeForm)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import employeeService from '@/api/employeeService'
import departmentService from '@/api/departmentService'
import postService from '@/api/postService'
import roleService from '@/api/roleService'
import axios from 'axios'

export default {
  created () {
    this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
  },
  name: 'Employee',
  data () {
    // 关于员工表单名称的校验规则
    const checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('员工名称不能为空'))
      }
      const employeeWithSameName = this.employee.filter(item => item.name === value) // 判定是否存在相同名称的员工

      if (this.employeeForm.id) { // 若当前为编辑表单状态
        if (employeeWithSameName.length > 1 && value != this.currentEmployeeFormName) {
          return callback(new Error('已存在相同的员工名称，请修改员工名称'))
        }
      } else { // 若当前非为编辑表单状态
        if (employeeWithSameName.length > 0) { //若当前表单大于0，则证明已存在此员工
          return callback(new Error('已存在相同的员工名称，请修改员工名称'))
        }
      }
      callback()
    }
    return {
      // 关于分页的数据
      pagination: {
        current: 1,
        size: 15,
        sizes: [15, 30, 50]
      },
      // 关于员工的数据
      employee:[],
      // 关于员工表单的数据
      employeeForm: {
        id: '',
        avatar: '',
        departmentId: '',
        postId: '',
        roleId: '',
        email: '',
        phone: '',
        username: '',
        name: '',
        sex: '',
      },
      // 关于员工表单的检测规则
      employeeFormRules: {
        name: [
          { trigger: 'blur', validator: checkName }
        ]
      },
      // 关于员工表单的检测状态
      employeeFormStatus: false,
      // 关于首次打开编辑表单的名称
      currentEmployeeFormName: '',
      // 关于搜索关键字
      name: '',
      // 关于员工进行多选时所需要的员工ID
      employeeId: [],
      // 关于员工数据加载时候的状态
      loadingStatus: false,
      // 关于部门信息
      department: [],
      // 关于岗位信息
      post: [],
      // 关于角色信息
      role: [],
      checkAll: false,
      isIndeterminate: true,
      employeeDialogFormVisiable: false,
      deleteEmployeeBatchList: []
    }
  },
  methods: {
    output() {
      employeeService.getEmployeefixed().then(res => {
        const data = res.data.employees
        let str = `姓名,账户,邮箱,电话,性别,部门,岗位,系统角色\n`;
        for(let i = 0 ; i < data.length ; i++ ){
          for(let item in data[i]){
            str+=`${data[i][item] + '\t'},`;
          }
          str+='\n';
        }
        let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
        //通过创建a标签实现
        var link = document.createElement("a");
        link.href = uri;
        //对下载的文件命名
        link.download =  "员工数据表.csv";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
    },

    // 关于分页的方法
    handleSizeChange (val) {
      this.pagination.size = val
      this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
    },
    // 关于部门的方法
    postEmployeeSingle (employee) {
      this.$refs.employeeForm.validate((valid) => {
        if (valid) {
          employeeService.postEmployeeSingle(employee).then(res => {
            this.employeeDialogFormVisiable = false
            this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
          })
        } else {
          return false
        }
      })
    },
    deleteEmployeeSingle (id) {
      this.$confirm('此操作将永久删除该部门信息且此部门下的员工将会移动到待分配部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        employeeService.deleteEmployeeSingle(id).then(res => {
          this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
        })
      })
    },
    deleteEmployeeMultiple (employeeId) {
      this.$confirm('此操作将永久删除该部门信息且此部门下的员工将会移动到待分配部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        employeeService.deleteEmployeeMultiple(employeeId).then(res => {
          this.getEmployeeMultiple(this.pagination.current, this. pagination.size)
        })
      })
    },
    getEmployeeMultiple (current, size) {
      this.loadingStatus = true
      employeeService.getEmployeeMultiple(current, size).then(res => {
        const currentEmployee = this.$store.state.employee
        this.employee = res.data.employeePage.records
        this.pagination.size = res.data.employeePage.size
        this.pagination.current = res.data.employeePage.current
        this.pagination.total = res.data.employeePage.total
          this.loadingStatus = false
      })
    },
    async getEmployeeMultipleByName (current, size, name) {
      await employeeService.getEmployeeMultipleByName(current, size, name).then(res => {
        this.employee = res.data.employeePage.records
        this.pagination.size = res.data.employeePage.size
        this.pagination.current = res.data.employeePage.current
        this.pagination.total = res.data.employeePage.total
      })
    },
    putEmployeeSingle (employee) {
      this.$refs.employeeForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此部门信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            console.log('here')
            console.log(employee)
            employeeService.putEmployeeSingle(employee).then(res => {
              this.getEmployeeMultiple(this.pagination.current, this.pagination.size)
              this.employeeDialogFormVisiable = false
            })
          }).catch(res => {
            this.$message.info("已取消对此部门信息的修改")
          })
        }
      })
    },
    // 关于部门表单窗口方法
    openEmployeeDialogForm (id) {
      this.employeeDialogFormVisiable = true
      if (id) { //若存在ID属性，则表明为编辑操作，否则则为添加操作
        employeeService.getEmployeeSingle(id).then(res => {
          this.employeeForm = {
            id: res.data.employee.id,
            avatar: res.data.employee.avatar,
            departmentId: res.data.employee.departmentId,
            postId: res.data.employee.postId,
            roleId: res.data.employee.roleId,
            email: res.data.employee.email,
            phone: res.data.employee.phone,
            username: res.data.employee.username,
            name: res.data.employee.name,
            sex: res.data.employee.sex,
          }

          this.currentEmployeeFormName = this.employeeForm.name
        })
      } else {
        this.employeeForm = {}
      }
    },
    closeEmployeeDialogForm (formName) {
      this.employeeDialogFormVisiable = false
      setTimeout(() => {
        this.$refs.employeeForm.resetFields()
      }, 500)
    },
    // 关于点击部门数据的多选框方法
    handleSelectionChange (employee) {
      this.employeeId = employee.map(item => item.id)
    },
    // 格式化表格属性，针对员工ID、岗位ID、角色ID，返回对应的具体名称
    formatter(row, column, cellValue, index) {
      if (column.property === "postId") {
        const post = this.$store.state.post.find(item => item.id === cellValue)
        return post.name
      } else if (column.property === "roleId") {
        const role = this.$store.state.role.find(item => item.id === cellValue)
        return role.name
      } else if (column.property === "departmentId") {
        const department = this.$store.state.department.find(item => item.id === cellValue)
        return department.name
      }
    },
    // 头像处理失败方法
    errorHandler() {
      return true
    },
    // 上传头像成功方法
    handleAvatarSuccess (res, file) {
      alert('上传成功')
      this.employeeForm.avatar = URL.createObjectURL(file.raw);
    },
    // 上传头像之前的检测
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },

    // 自定义上传方法
     httpRequest(config) {
      let formdata = new FormData()
      formdata.append('multipartFile', config.file)
      formdata.append('fileName', Date.now())
      console.log(config)
      employeeService.uploadAvatar(formdata, this.$store.state.token).then(res => {
        this.employeeForm.avatar = res.data.avatar
      })
    },
  }
}
</script>

<style scoped lang="scss">
  #employee {
    /deep/ .el-table {
      padding: 0px 5px;
    }

    .pagination {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;
    }
  }
</style>
