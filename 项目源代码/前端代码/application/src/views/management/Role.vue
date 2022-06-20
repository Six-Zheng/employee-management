<template>
  <div id="role">
    <!--    顶部操作栏-->
    <div class="pagination">
      <div>
        <el-button size="mini" type="primary" @click="openRoleDialogForm()" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:POST_SINGLE')">添加角色</el-button>
        <el-button size="mini" type="danger" @click="deleteRoleMultiple(roleId)" :disabled="this.roleId.length === 0" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:DELETE_MULTIPLE')">批量删除角色</el-button>
        <el-button size="mini" type="success" @click="output" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:EXPORT')">导出角色信息</el-button>
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
    <!--    角色信息-->
    <div id="table">
      <el-table :data="role"  @selection-change="handleSelectionChange" v-loading="loadingStatus">
        <el-table-column prop="id"
                         :selectable="selectable"
                         v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:DELETE_MULTIPLE')"
        type="selection" width="55">
        </el-table-column>
        <el-table-column type="index" width="80" label="序号">
        </el-table-column>
        <el-table-column prop="name" label="名称" width="180">
          <template slot-scope="scope">
            <el-tag effect="dark" >{{scope.row.name}}</el-tag>
<!--            <el-tag dark type="info">{{// scope.row.name}}</el-tag>-->
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column prop="tag" label="标记"></el-table-column>
        <el-table-column
          width="350"
          align="right">
          <template slot="header" slot-scope="scope">
            <el-input
              @input="getRoleMultipleByName(pagination.current, pagination.size, name)"
              v-model="name"
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:GET_MULTIPLE')"
              placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-if="scope.row.name != '管理员' && scope.row.name != '默认角色' && $store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:UPDATE_SINGLE')"
              @click="openRoleDialogForm(scope.row.id)">编辑</el-button>
            <el-button
              type="warning"
              size="mini"
              v-if="scope.row.name != '管理员' && scope.row.name != '默认角色' && $store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:UPDATE_SINGLE')"
              @click="openRoleMenuDialogTree(scope.row.id)">分配权限</el-button>
            <el-button
              size="mini"
              type="danger"
              v-if="scope.row.name != '管理员' && scope.row.name != '默认角色' && $store.state.menu.permissionList.find(item => item === 'MANAGEMENT:ROLE:DELETE_SINGLE')"
              @click="deleteRoleSingle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--    角色表单信息-->
    <el-dialog title="角色信息"
               :close-on-click-modal="false"
               @close="closeRoleDialogForm('roleForm')"
               width="25%"
               :visible.sync="roleDialogFormVisiable"
               >
      <el-form :rules="roleFormRules" :model="roleForm" label-position="right" label-width="100px" ref="roleForm">
        <el-form-item label="角色名称" prop="name" >
          <el-input v-model="roleForm.name" autocomplete="off" :disabled="(roleForm.name === '默认角色' && roleForm.id == '1') || roleForm.name === '管理员' && roleForm.id == '2'"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="roleForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="标记" prop="tag">
          <el-input type="textarea" v-model="roleForm.tag" autocomplete="off" :disabled="roleForm.tag === 'ROLE_DEFAULT' || roleForm.tag === 'ROLE_ADMIN'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeRoleDialogForm('roleForm')">取 消</el-button>
        <el-button type="primary" @click="roleForm.id?putRoleSingle(roleForm):postRoleSingle(roleForm)">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="角色权限信息"
      @close="closeRoleMenuDialogTree"
      width="25%"
      :visible.sync="roleMenuDialogTreeVisiable"
      >
      <el-tree
        :check-strictly="true"
        ref="tree"
        highlight-current
        node-key="id"
        show-checkbox
        :props= defaultProps
        :data="roleMenuData"
        >
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeRoleMenuDialogTree">取 消</el-button>
        <el-button type="primary" @click="putRoleMenuMultiple(currentRoleId)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleService from '@/api/roleService'
import http from '@/util/http'
import menuService from '@/api/menuService'
import roleMenuService from '@/api/roleMenuService'
import departmentService from '@/api/departmentService'
import store from '@/store'
import {
  RESET_TAB,
  SET_DEPARTMENT,
  SET_MENU_LIST,
  SET_PERMISSION_LIST,
  SET_ROLE,
  SET_ROUTE_STATUS,
  SET_TAB
} from '@/store/mutation_name'
import employeeService from '@/api/employeeService'
import Home from '@/views/Home'
import router from '@/router'

export default {
  created () {
    this.getRoleMultiple(this.pagination.current, this.pagination.size)
  },
  name: 'Role',
  data () {
    // 关于角色表单名称的校验规则
    const checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('角色名称不能为空'))
      }
      const roleWithSameName = this.role.filter(item => item.name === value) // 判定是否存在相同名称的角色

      if (this.roleForm.id) { // 若当前为编辑表单状态
        if (roleWithSameName.length > 1 && value != this.currentRoleFormName) {
          return callback(new Error('已存在相同的角色名称，请修改角色名称'))
        }
      } else { // 若当前非为编辑表单状态
        if (roleWithSameName.length > 0) { //若当前表单大于0，则证明已存在此角色
          return callback(new Error('已存在相同的角色名称，请修改角色名称'))
        }
      }
      callback()
    }
    // 关于角色表单权限标记的校验规则
    const checkTag = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('标记名称不能为空'))
      }
      const roleWithSameTag = this.role.filter(item => item.tag === value) // 判定是否存在相同名称的角色

      if (this.roleForm.id) { // 若当前为编辑表单状态
        if (roleWithSameTag.length > 1 && value != this.currentRoleFormTag) {
          return callback(new Error('已存在相同的标记名称，请修改标记名称'))
        }
      } else { // 若当前非为编辑表单状态
        if (roleWithSameTag.length > 0) { //若当前表单大于0，则证明已存在此角色
          return callback(new Error('已存在相同的标记名称，请修改标记名称'))
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
      // 关于角色的数据
      role:[],
      // 关于角色表单的数据
      roleForm: {
        id: '',
        name: '',
        description: '',
        tag: '',
      },
      // 关于角色表单的检测规则
      roleFormRules: {
        name: [
          { trigger: 'blur', validator: checkName }
        ],
        tag: [
          { trigger: 'blur', validator: checkTag }
        ]
      },
      // 关于角色表单的检测状态
      roleFormStatus: false,
      // 关于首次打开编辑表单的名称
      currentRoleFormName: '',
      // 关于首次打开编辑表单的标记名称
      currentRoleFormTag: '',
      // 关于搜索关键字
      name: '',
      // 关于角色进行多选时所需要的角色ID
      roleId: [],
      // 关于角色数据加载时候的状态
      loadingStatus: false,
      // 关于角色菜单的状态
      roleMenuDialogTreeVisiable: false,
      // 关于角色菜单的数据
      roleMenuData: [],
      // 关于角色菜单属性的重命名
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      // 关于当前角色菜单所选中的ID
      roleMenuDataId: [],
      // 关于当前的角色ID
      currentRoleId: '',

      checkAll: false,
      isIndeterminate: true,
      roleDialogFormVisiable: false,
      deleteRoleBatchList: []
    }
  },
  methods: {
    output() {
      const data = this.$store.state.role
      let str = `角色名称,角色描述, 角色标记\n`;
      for (let i = 0; i < data.length; i++) {
        let index = 0
        for (let item in data[i]) {
          index = index + 1;
          if (index > 5)
            str += `${data[i][item] + '\t'},`;
        }
        str += '\n';
      }
      let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
      //通过创建a标签实现
      var link = document.createElement("a");
      link.href = uri;
      //对下载的文件命名
      link.download = "角色数据表.csv";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    selectable(row) {
      const a =  row.name !== '默认角色' && row.name !== '管理员'
      return a
    },

    // 全局刷新department
    refreshRole() {
      roleService.getRoleList().then(res => {
        console.log(res.data)
        store.commit({ type: SET_ROLE, role: res.data.role })
      })
    },
    // 关于分页的方法
    handleSizeChange (val) {
      this.pagination.size = val
      this.getRoleMultiple(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getRoleMultiple(this.pagination.current, this.pagination.size)
    },
    // 关于角色的方法
    postRoleSingle (role) {
      this.$refs.roleForm.validate((valid) => {
        if (valid) {
          roleService.postRoleSingle(role).then(res => {
            this.roleDialogFormVisiable = false
            this.getRoleMultiple(this.pagination.current, this.pagination.size)
            this.refreshRole()
          })
        } else {
          return false
        }
      })
    },
    deleteRoleSingle (id) {
      this.$confirm('此操作将永久删除该角色信息且此角色下的员工将会移动到待分配角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        roleService.deleteRoleSingle(id).then(res => {
          this.getRoleMultiple(this.pagination.current, this.pagination.size)
          this.refreshRole()
        })
      })
    },
    deleteRoleMultiple (roleId) {
      this.$confirm('此操作将永久删除该岗位信息且此岗位下的员工将会移动到待分配岗位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        roleService.deleteRoleMultiple(roleId).then(res => {
          this.getRoleMultiple(this.pagination.current, this.pagination.size)
          this.refreshRole()
        })
      })
    },
    getRoleMultiple (current, size) {
      this.loadingStatus = true
      roleService.getRoleMultiple(current, size).then(res => {
        this.role = res.data.rolePage.records
        this.pagination.size = res.data.rolePage.size
        this.pagination.current = res.data.rolePage.current
        this.pagination.total = res.data.rolePage.total
        this.loadingStatus = false
      })
    },
    getRoleMultipleByName (current, size, name) {
      roleService.getRoleMultipleByName(current, size, name).then(res => {
        this.role = res.data.rolePage.records
        this.pagination.size = res.data.rolePage.size
        this.pagination.current = res.data.rolePage.current
        this.pagination.total = res.data.rolePage.total
      })
    },
    putRoleSingle (role) {
      this.$refs.roleForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此角色信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            roleService.putRoleSingle(role).then(res => {
              this.getRoleMultiple(this.pagination.current, this.pagination.size)
              this.roleDialogFormVisiable = false
            })
          }).catch(res => {
            this.$message.info("已取消对此角色信息的修改")
          })
        }
      })
    },
    putRoleMenuMultiple(roleId) {
      const ids = this.$refs.tree.getCheckedKeys()
      roleMenuService.putRoleMenuMultiple(roleId, ids).then( res => {
        this.roleMenuDialogTreeVisiable = false
        this.fix()
      })
    },

    async fix() {
      await menuService.getMenuByRoleId(this.$store.state.employee.roleId).then(res => {
        const permission = res.data.permission

        console.log('tabs')
        console.log(this.$store.state.menu.tabs)

        this.$store.commit({
          type: RESET_TAB,
          data: res.data.menu
        })

        this.$store.commit({
          type: SET_MENU_LIST,
          menuList: res.data.menu
        })
        this.$store.commit({
          type: SET_PERMISSION_LIST,
          permissionList: permission
        })
      })
    },

    // 关于角色表单窗口方法
    openRoleDialogForm (id) {
      this.roleDialogFormVisiable = true
      if (id) { //若存在ID属性，则表明为编辑操作，否则则为添加操作
        roleService.getRoleSingle(id).then(res => {
          this.roleForm = {
            id: res.data.role.id,
            name: res.data.role.name,
            tag: res.data.role.tag,
            description: res.data.role.description
          }
          this.currentRoleFormName = this.roleForm.name
          this.currentRoleFormTag = this.roleForm.tag
        })
      } else {
        this.roleForm = {}
      }
    },
    closeRoleDialogForm (formName) {
      this.roleDialogFormVisiable = false
      setTimeout(() => {
        this.$refs.roleForm.resetFields()
      }, 500)
    },
    // 关于点击角色数据的多选框方法
    handleSelectionChange (role) {
      this.roleId = role.map(item => item.id)
    },
    // 关于角色菜单对话框窗口方法
    openRoleMenuDialogTree (id) {
      this.roleMenuDialogTreeVisiable = true
      this.currentRoleId = id
      //获取当前角色所拥有的角色菜单信息
      roleMenuService.getRoleMenuMultipleByRoleId(id).then(res => {
        res.data.roleMenu.forEach(item => {
          this.roleMenuDataId.push(item.menuId)
        })
        //获取所有的角色菜单信息
        menuService.getMenuMultiple().then(res => {
          this.roleMenuData = res.data.menu
          this.$refs.tree.setCheckedKeys(this.roleMenuDataId);
        })
      })
    },
    closeRoleMenuDialogTree () {
      this.roleMenuDialogTreeVisiable = false
      this.roleMenuDataId = []
      this.$refs.tree.setCheckedKeys([])
      this.currentRoleId = ''

    }
  }
}
</script>

<style scoped lang="scss">
  #role {
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
