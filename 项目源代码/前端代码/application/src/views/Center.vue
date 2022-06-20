<template>
  <div id="index">
    <el-descriptions title="个人信息" :column="3" size="mini" border>
      <template slot="extra">
        <el-button type="primary" size="mini" @click="openEmployeeDialogForm(employee.id)">修改个人信息</el-button>
      </template>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{employee.name}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        {{employee.phone}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          邮箱
        </template>
        {{employee.email}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          部门
        </template>
        {{employee.departmentName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          岗位
        </template>
        {{employee.postName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          角色
        </template>
        {{employee.roleName}}
      </el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <div class="el-descriptions__title" style="padding-top: 10px">重置个人密码</div>
    <div style="width: 400px;">
      <el-form :model="passwordForm" :rules="passwordFormRules" style="padding-top: 15px" label-position="right" label-width="120px" ref="passwordForm">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="再次确认密码" prop="againPassword">
          <el-input v-model="passwordForm.againPassword" type="password"></el-input>
        </el-form-item>
      </el-form>
      <div style="width:100%; display: inline-flex; justify-content: center; align-items: center">
        <el-button type="primary" size="mini" @click="resetEmployeePasswordSelf">确定</el-button>
        <el-button size="mini" @click="resetPasswordForm">重置</el-button>
      </div>
    </div>
    <el-divider></el-divider>

    <!--    员工表单信息-->
    <el-dialog
      :close-on-click-modal="false"
      title="员工信息"
      @close="closeEmployeeDialogForm('employeeForm')"
      width="25%"
      top="10vh"
      :visible.sync="employeeDialogFormVisiable"
    >
<!--      <el-form :rules="employeeFormRules" :model="employeeForm" label-position="right" label-width="100px" ref="employeeForm">-->
      <el-form :model="employeeForm" label-position="right" label-width="100px" ref="employeeForm">
        <el-upload
          style="display: flex; justify-content: center; align-items: center; padding-bottom: 15px"
          :http-request="httpRequest"
          action=""
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <el-avatar style="width: 72px; height: 72px" fit="scale-down" :src="employeeForm.avatar" :key="employeeForm.avatar"></el-avatar>
        </el-upload>
        <el-form-item label="账户" prop="username" >
          <el-input v-model="employeeForm.username" autocomplete="off" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name"  >
          <el-input v-model="employeeForm.name" autocomplete="off" :disabled="employeeForm.roleId != 2"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="employeeForm.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleId" >
          <el-select v-model="employeeForm.roleId" :disabled="true">
            <el-option v-for="role in $store.state.role" :key="role.id" :value="role.id" :label="role.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId"  >
          <el-select v-model="employeeForm.departmentId" :disabled="true">
            <el-option v-for="department in $store.state.department" :key="department.id" :value="department.id" :label="department.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="postId" >
          <el-select v-model="employeeForm.postId" :disabled="true">
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
    <el-descriptions title="请假条" :column="3" size="mini" border>
      <template slot="extra">
        <el-button type="info" size="mini" @click="openHasSend">查看已发起的申请</el-button>
        <el-button type="primary" size="mini" @click="committedToDo()">发起请假流程</el-button>
      </template>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          申请人名称
        </template>
        {{employee.name}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-timer"></i>
          起止时间
        </template>
        <el-date-picker
          v-model="petition.date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          请假类型
        </template>
        <el-radio-group v-model="petition.type">
          <el-radio :label="1">事假</el-radio>
          <el-radio :label="0">病假</el-radio>
        </el-radio-group>
      </el-descriptions-item>
      <el-descriptions-item span="3">
        <template slot="label">
          <i class="el-icon-edit-outline"></i>
          内容
        </template>
        <div class="content">
          <el-input
            id="input1"
            type="textarea"
            placeholder="请输入内容"
            v-model="petition.content">
          </el-input>
        </div>
      </el-descriptions-item>
    </el-descriptions>
    <div>
      <el-dialog title="请假记录" :visible.sync="dialogTableVisible">
        <el-table :data="petitionData" border>
          <el-table-column label="申请日期">
            <template slot-scope="scope">
              {{formatter2(scope.row.createTime)}}
            </template>
          </el-table-column>
          <el-table-column property="handlerName" label="处理人" width="200"></el-table-column>
          <el-table-column property="isHandled" label="状态" width="200">
            <template slot-scope="scope">
              <el-tag  type="danger" effect="dark" v-if="scope.row.isHandled==0">未审核</el-tag>
              <el-tag  type="success" effect="dark" v-else>已处理</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="处理结果">
            <template  slot-scope="scope">
              <el-tag effect="dark" type="success" v-if="scope.row.reply && scope.row.reply.charAt(0) == 1">已批准</el-tag>
              <el-tag effect="dark" type="danger" v-else-if="scope.row.reply && scope.row.reply.charAt(0) == 0">未批准</el-tag>
              <el-tag  effect="dark" type="info" v-else>无结果</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import employeeService from '@/api/employeeService'
import departmentService from '@/api/departmentService'
import postService from '@/api/postService'
import roleService from '@/api/roleService'
import petitionService from '@/api/petitionService'
import store from '@/store'
import { SET_EMPLOYEE, SET_TAB } from '@/store/mutation_name'

export default {
  name: "Center",

  data() {
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

    const isNewPassowrdEqualsAgainPassword = (rule, value, callback) => {
      if (this.passwordForm.newPassword !== this.passwordForm.againPassword) {
        return callback(new Error('两次密码输入不一致，请重新输入'))
      }
      callback()
    }
    return {
      petitionData:[],
      dialogTableVisible: false,
      // 关于请假条的数据
      petition: {
        type: 1,
        date:[],
        content: ''
      },
      // 关于分页的数据
      pagination: {
        current: 1,
        size: 15,
        sizes: [15, 30, 50]
      },
      // 关于密码表单的数据
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        againPassword: ''
      },

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
      deleteEmployeeBatchList: [],

      passwordFormRules: {
        oldPassword: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
        ],
        newPassword: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          },
        ],
        againPassword: [
          {
            required: true,
            message: '请再次输入新密码',
            trigger: 'blur'
          },
          {
            validator: isNewPassowrdEqualsAgainPassword, trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    employee: {
      get () {
        return this.$store.state.employee
      }
    },
    trueDate() {
      return [
        this.afterDate(this.petition.date[0]),
        this.afterDate(this.petition.date[1])
      ]
    },
    currentEmployeeAvatar: {
      get() {
        return this.employee.avatar
      },
      set(val) {
        this.employee.avatar = val
      }
    }
  },

  methods: {

    getPetitionMultiple () {
      petitionService.getPetitionMultiple(1, 10000).then(res => {
        console.log(res.data)
        this.petitionData = res.data.petition.filter(item => (item.proposerAccount).toString() == this.$store.state.employee.username)
        // this.petitionData = res.data.petition
      })
    },

    // 打开已发送的申请
    openHasSend() {
      this.dialogTableVisible = true
      this.getPetitionMultiple()
    },
    // 格式化传输后台数据的时间
    afterDate(dateData) {
      let date = new Date(dateData)
      let y = date.getFullYear()
      let m = date.getMonth() + 1
      m = m < 10 ? ('0' + m) : m
      let d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      const time =  y + '-' + m + '-' + d;
      return time
    },

    formatter2(val) {
      return val.substring(0, 19).replace('T',' ')
    },
    // 发起请假流程
    committedToDo() {
      if (this.petition.date.length ==2 && this.petition.content != "") {
        const petition2 = {
          type: this.petition.type,
          content: this.petition.content,
          startDate: this.trueDate[0] + "T00:00:00",
          endDate: this.trueDate[1] + "T23:59:59",
          isHandled: 0,
          proposerAccount: this.$store.state.employee.username,
        }
        petitionService.postPetitionSingle(petition2).then(res => {
          this.petition.content = '';
          this.petition.date = [];
          this.petition.type = 0
        })
      } else {
        this.$message.error("请填写必要的请假内容再进行提交");
      }
    },
    // 重置密码
    resetEmployeePasswordSelf() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          employeeService.resetEmployeePasswordSelf(this.employee.username, this.passwordForm.oldPassword, this.passwordForm.newPassword).then(res => {
            this.$router.push('/login')
          })
        } else {
          return false
        }
      })
    },
    // 关于员工表单窗口方法
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
    // 初始化基本的岗位信息、角色信息、部门信息
    initalizeBaseInformation() {
      departmentService.getDepartmentList().then(res => {
        this.department = res.data.department
      })
      postService.getPostList().then(res => {
        this.post = res.data.post
      })
      roleService.getRoleList().then(res => {
        this.role = res.data.role
      })
    },
    // 格式化表格属性，针对员工ID、岗位ID、角色ID，返回对应的具体名称
    formatter(row, column, cellValue, index) {
      if (column.property === "postId") {
        const post = this.post.find(item => item.id === cellValue)
        return post.name
      } else if (column.property === "roleId") {
        const role = this.role.find(item => item.id === cellValue)
        return role.name
      } else if (column.property === "departmentId") {
        const department = this.department.find(item => item.id === cellValue)
        return department.name
      }
    },
    // 头像处理失败方法
    errorHandler() {
      return true
    },
    // 上传头像成功方法
    handleAvatarSuccess (res, file) {
      // this.employeeForm.avatar = URL.createObjectURL(file.raw);
      this.employeeForm.avatar = res.data.avatar;
      this.$forceUpdate()
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
    async httpRequest(config) {
      let formdata = new FormData()
      formdata.append('multipartFile', config.file)
      formdata.append('fileName', Date.now())
      console.log(config)
      await employeeService.uploadAvatar(formdata, this.$store.state.token).then(res => {
        this.employeeForm.avatar = res.data.avatar
      })
    },

    alertPassword() {
      this.$refs.passwordForm.validate((valid) => {
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

    resetPasswordForm() {
      this.$refs.passwordForm.resetFields()
    },

    /**
     * 两个都修改，修改完之后store到对面
     * @returns {Promise<void>}
     */
    async method(employee) {
      await employeeService.putEmployeeSingle(employee).then(res => {
        this.employeeDialogFormVisiable = false
      })
      await employeeService.getEmployeeByUsername(employee.username).then(res => {
        const employee = res.data.employee
        employee.departmentName = res.data.departmentName
        employee.roleName = res.data.roleName
        employee.postName = res.data.postName
        this.$store.commit({
          type: SET_EMPLOYEE,
          employee: employee
        })
      })
    },

    putEmployeeSingle (employee) {
      this.$refs.employeeForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此员工信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            this.employeeDialogFormVisiable = false
            this.method(employee)
          }).catch(res => {
            this.$message.info("已取消对此员工信息的修改")
          })
        }
      })
    },
  }
}
</script>

<style scoped>
#index {
  padding: 0 30px;
}
</style>
