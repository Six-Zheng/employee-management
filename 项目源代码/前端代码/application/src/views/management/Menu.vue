k<template>
  <div id="menu">
    <div class="pagination">
      <div>
        <el-button
          size="mini"
          v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:MENU:POST_SINGLE')"
          type="primary"
          @click="openMenuDialogForm()">
          添加菜单
        </el-button>
        <el-button
          size="mini"
          v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:MENU:DELETE_MULTIPLE')"
          type="danger" @click="deleteMenuMultiple(menuId)" :disabled="this.menuId.length === 0">
          批量删除菜单
        </el-button>
      </div>
    </div>
    <div id="table">
      <el-table
        :data="menu"
        @selection-change="handleSelectionChange"
        v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:MENU:DELETE_MULTIPLE')"
        row-key="id"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        v-loading="loadingStatus">
        <el-table-column
          :selectable="selectable"
          prop="id"
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="title"
          label="名称"
          width="250">
        </el-table-column>
        <el-table-column
          prop="path"
          label="路由"
          width="210">
        </el-table-column>
        <el-table-column
          :formatter="my"
          prop="component"
          label="组件" width="210">
        </el-table-column>
        <el-table-column
          prop="level"
          label="类型"
          width="110">
          <template slot-scope="scope">
            <el-tag
              size="mini"
              style="background: #5352ed; border: solid 1px #5352ed"
              effect="dark"
              v-if="scope.row.level===1">
              一级菜单
            </el-tag>
            <el-tag
              size="mini"
              style="background: #70a1ff; border: solid 1px #70a1ff"
              type="warning"
              effect="dark"
              v-else-if="scope.row.level===2">
              二级菜单
            </el-tag>
            <el-tag
              size="mini"
              style="background: #ff6348; border: solid 1px #ff6348"
              type="info"
              effect="dark"
              v-else-if="scope.row.level===3">
              按钮
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderNum"
          label="排序号"
          width="80">
        </el-table-column>
        <el-table-column
          prop="tag"
          label="标识"
          width="400">
        </el-table-column>
        <el-table-column
          align="right">
          <template slot-scope="scope">
            <el-button
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:MENU:UPDATE_SINGLE') && scope.row.tag.indexOf('MANAGEMENT') == -1"
              size="mini"
              @click="openMenuDialogForm(scope.row.id)">
              编辑
            </el-button>
            <el-button
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:MENU:UPDATE_SINGLE') && scope.row.tag.indexOf('MANAGEMENT') == -1"
              size="mini"
              type="danger"
              @click="deleteMenuSingle(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      title="菜单信息"
      @close="closeMenuDialogForm('menuForm')"
      width="25%"
      :visible.sync="menuDialogFormVisiable"
      top="5vh">
      <el-form
        :rules="menuFormRules"
        :model="menuForm"
        label-position="right"
        label-width="100px"
        ref="menuForm">
        <el-form-item
          label="菜单名称"
          prop="title" >
          <el-input
            v-model="menuForm.title"
            autocomplete="off"
            :disabled="menuForm.name === '待分配菜单' && menuForm.id === 1 ">
          </el-input>
        </el-form-item>
        <el-form-item
          label="路由"
          prop="path">
          <el-input
            v-model="menuForm.path"
            autocomplete="off">
          </el-input>
        </el-form-item>
        <el-form-item
          v-if="menuForm.level==2"
          label="组件"
          prop="component">
          <el-select
            :key="Date.now()"
            v-model="menuForm.component">
            <el-option
              v-for="component in $store.state.component"
              :key="component.component"
              :value="component.component"
              :label="component.title">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="排序号"
          prop="orderNum">
          <el-input-number
            v-model="menuForm.orderNum"
            autocomplete="off">
          </el-input-number>
        </el-form-item>
        <el-form-item
          v-if="menuForm.level == 2"
          label="父级菜单"
          prop="parentId">
          <el-select
            :key="Date.now()"
            @change="change(val)"
            v-model="menuForm.parentId">
            <el-option
              v-for="item
              in currentFatherMenu"
              :key="item.tag"
              :value="item.id"
              :label="item.title">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="类型"
          prop="parentId">
          <template>
            <el-select
              v-model="menuForm.level"
              placeholder="请选择"
              @change="changeMenuParentId">
              <el-option
                :key="1"
                label="一级菜单"
                value="1"></el-option>
              <el-option
                :key="2"
                label="二级菜单"
                value="2"></el-option>
            </el-select>
          </template>
        </el-form-item>
        <el-form-item
          label="标识"
          prop="tag">
          <el-input
            v-model="menuForm.tag"
            autocomplete="off">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="closeMenuDialogForm('menuForm')">
          取 消
        </el-button>
        <el-button
          type="primary"
          @click="menuForm.id?putMenuSingle(menuForm):postMenuSingle(menuForm)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import menuService from '@/api/menuService'
import http from '@/util/http'
import { RESET_LEVEL2_MENU, SET_MENU_DATA, SET_MENU_LIST, SET_PERMISSION_LIST } from '@/store/mutation_name'
import store from '@/store'

export default {
  created () {
    this.initMenuData()
    this.getComds()
  },
  computed: {
    menu: {
      get() {
        return this.$store.state.menu.menuData
      },
      set(menuData) {
        this.$store.commit('SET_MENU_DATA', menuData)
      }
    }
  },
  name: 'Menu',
    data () {
      /*
      * 检查标题是否重复
      * 规则：
      * */
      const checkTitle = (rule, value, callback) => {
        const level = this.menuForm.level
        const uncheckedData = []

        if (level == 1) {
          for (let i = 0; i < this.$store.state.menu.menuData.length; i++) {
            if (this.$store.state.menu.menuData[i].title == value)
              return new callback(new Error('标题重复'))
          }
        } else if (level == 2) {
          for (let i = 0; i < this.$store.state.menu.menuData.length; i++) {
            if (this.$store.state.menu.menuData[i].children) {
              for (let j = 0; j < this.$store.state.menu.menuData[i].children.length; j++) {
                if (this.$store.state.menu.menuData[i].children[j].title == value)
                  return new callback(new Error('标题重复'))
              }
            }
          }
        } else {
          return new callback(new Error('请选择菜单等级'))
        }
        callback()
      }
        /*
        * 检查路由是否重复
        * 规则：
        * */
        const checkPath = (rule, value, callback) => {
          const nowRoutes = this.$router.getRoutes()
          for(let i = 0; i < nowRoutes.length; i++) {
            if (nowRoutes[i].path === value)
              return new callback(new Error('路由重复，请更换名称'))
          }
          callback()
        }

        // const checkComponent = (rule, value, callback) => {
        //   console.log('abc')
        //   if(this.menuForm.level = 1 && value != null) {
        //     return new callback(new Error('一级菜单不允许挂载组件,请选择无组件'))
        //   }
        //   callback()
        // }

        /*
        * 检查权限标记是否重复
        * */
        const checkTag = (rule, value, callback) => {
          if (value.indexOf('SYSTEM') != -1) {
            return callback(new Error('不允许使用含有SYSTEM关键字的标记作为自定义菜单'))
          }

          if (this.$store.state.menu.permissionList.find(item => item === value)) {
            return callback(new Error('标记重复'));
          }
          callback()
        }

          //具体数据
          return {
            // 关于菜单表单的数据
            menuForm: {
              id: '',
              title: '',
              path: '',
              component: '',
              parentId: '',
              orderNum: '',
              level: '',
              tag: '',
              status: ''
            },
            // 关于菜单表单的检测规则
            menuFormRules: {
              tag: [
                {
                  trigger: 'blur',
                  validator: checkTag
                },
                {
                  required: true,
                  message: '请输入标记名称',
                  trigger: 'blur'
                },
              ],
              title: [
                {
                  trigger: 'blur',
                  validator: checkTitle
                },
                {
                  required: true,
                  message: '请输入标题名称',
                  trigger: 'blur'
                },
              ],
              // component: [
              //   {
              //     trigger: 'blur',
              //     validator: checkComponent
              //   },
              // ],
              path: [
                {
                  trigger: 'blur',
                  validator: checkPath
                },
                {
                  required: true,
                  message: '请输入路由名称',
                  trigger: 'blur'
                },
              ]
            },
            // 关于菜单表单的检测状态
            menuFormStatus: false,
            // 关于首次打开编辑表单的名称
            currentMenuFormTitle: '',
            // 关于搜索关键字
            name: '',
            // 关于菜单进行多选时所需要的菜单ID
            menuId: [],
            // 关于菜单数据加载时候的状态
            loadingStatus: false,
            // 当前父级菜单：
            currentFatherMenu: [],
            checkAll: false,
            isIndeterminate: true,
            menuDialogFormVisiable: false,
            deleteMenuBatchList: []
          }
        },
  methods: {
    change() {
      this.$forceUpdate()
    },
    my(row) {
      let a = row.component

      if (a ) {
        if (row.component.indexOf('Department') != -1) {
          return '部门组件'
        } else if (row.component.indexOf('Post')  != -1) {
          return '岗位组件'
        } else if (row.component.indexOf('Petition')  != -1) {
          return '请假组件'
        } else if (row.component.indexOf('Role')  != -1) {
          return '角色组件'
        } else if (row.component.indexOf('Menu')  != -1) {
          return '菜单组件'
        } else if (row.component.indexOf('Employee')  != -1) {
          return '员工组件'
        } else if (row.component.indexOf('Chart')  != -1) {
          return '概况组件'
        }
      }
    },

    selectable(row) {
      const a = (row.tag.indexOf('MANAGEMENT') == -1)

      return a
    },

    test() {
      alert(this.$store.state.menu.permissionList.find(item => item === 'SYSTEM:MANAGEMENT:DATA'))
    },
    /**
     * 初始化菜单信息
     */
    initMenuData() {
      menuService.getLevel2Component().then(res => {
        this.$store.commit({
          type: 'SET_LEVEL2_MENU',
          data: res.data.menu
        })
      })
      menuService.getMenuMultiple().then(res => {
        this.menu = res.data.menu
        this.$store.commit({
          type: 'SET_MENU_DATA',
          menuData: res.data.menu
        })
      })
    },

    changeMenuParentId() {
      this.menuForm.parentId = ''
    },

    // 关于分页的方法
    handleSizeChange (val) {
      this.pagination.size = val
      this.getMenuMultiple()
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getMenuMultiple()
    },
    // 关于菜单的方法
    postMenuSingle (menu) {
      this.$refs.menuForm.validate((valid) => {
        if (valid) {
          menuService.postMenuSingle(menu).then(res => {
            this.menuDialogFormVisiable = false
            this.initMenuData()
          })
        } else {
          return false
        }
      })
    },
    deleteMenuSingle (id) {
      this.$confirm('此操作将永久删除该菜单信息且此菜单下的员工将会移动到待分配菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        menuService.deleteMenuSingle(id).then(res => {
          this.fix()
          this.initMenuData()
        })
      })
    },
    deleteMenuMultiple (menuId) {
      this.$confirm('此操作将永久删除所选的菜单信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        menuService.deleteMenuMultiple(menuId).then(res => {
          // this.getMenuMultiple()
          this.initMenuData()
          this.fix()
        })
      })
    },
    async fix() {
      this.loadingStatus = true
      await menuService.getMenuByToken().then(res => {
        const permission = res.data.permission
        console.log(res.data.permission)
        this.$store.commit({
          type: SET_MENU_LIST,
          menuList: res.data.menu
        })
        this.$store.commit({
          type: SET_PERMISSION_LIST,
          permissionList: permission
        })
        this.loadingStatus = false
      })
      await menuService.getMenuByRoleId(this.$store.state.employee.roleId).then(res => {
        const permission = res.data.permission
        console.log('menu is here')
        console.log(res.data.menu)
        console.log(this.$store.state.tabs)

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
    getMenuMultiple () {
      this.loadingStatus = true
      menuService.getMenuMultiple().then(res => {
        this.menu = res.data.menu
        this.loadingStatus = false
      })
    },
    putMenuSingle (menu) {
      this.$refs.menuForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此菜单信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            menuService.putMenuSingle(menu).then(res => {
              this.getMenuMultiple()
              this.menuDialogFormVisiable = false
            })
          }).catch(res => {
            this.$message.info("已取消对此菜单信息的修改")
          })
        }
      })
    },
    // 关于菜单表单窗口方法
    openMenuDialogForm (id) {
      this.getComds()

      this.menuDialogFormVisiable = true
      if (id) { //若存在ID属性，则表明为编辑操作，否则则为添加操作
        alert('here')
        menuService.getMenuSingle(id).then(res => {
          console.log(res.data)
          this.menuForm = {
            id: res.data.menu.id,
            title: res.data.menu.title,
            path: res.data.menu.path,
            component: res.data.menu.title,
            parentId: res.data.menu.parentId.toString(),
            orderNum: res.data.menu.orderNum,
            level: res.data.menu.level.toString(),
            tag:res.data.menu.tag,
            status: res.data.menu.status.toString()
          }
          // 获取相对应的父级菜单名称
          if (this.menuForm.level === '2') {
            this.currentFatherMenu = this.menu.map(item => {
              const value = {
                id: (item.id).toString(),
                title: item.title
              }
              return value
            })
          } else if (this.menuForm.level === '3') {
              let level1Menu = this.menu.map(item => {
                if (item.level === 1 && item.children) {
                  return {
                    children: item.children
                  }
                }
              })

            const level2Menu = []

            level1Menu.forEach(item => {
              item.children.forEach(item2 => {
                level2Menu.push({
                  id: item2.id.toString(),
                  title: item2.title
                })
              })
            })
            this.currentFatherMenu = level2Menu
          }
        })
      } else {
        this.menuForm = {}
      }
    },
    getComds() {
      menuService.getLevel2ComponentD().then(res => {
        this.currentFatherMenu = res.data.menu
        console.log(this.currentFatherMenu)
      })
    },
    closeMenuDialogForm (formName) {
      this.menuDialogFormVisiable = false
      setTimeout(() => {
        this.$refs.menuForm.resetFields()
      }, 500)
    },
    // 关于点击菜单数据的多选框方法
    handleSelectionChange (menu) {
      this.menuId = menu.map(item => item.id)
    },
    // 获取对应的父级菜单名称
    getCurrentFatherMenu (level) {
      if (level === '1' ) {
        console.log(this.currentFatherMenu)
      } else if (level === '2') {
        var that = this
        this.menu.forEach(level1Menu => {
          that.currentFatherMenu.push(
            {
              id : level1Menu.id.toString(),
              title: level1Menu.title
            }
          )
        })
      } else if (level === '3') {
        var that = this
        this.menu.forEach(level1Menu => {
          if (level1Menu.children) {
            level1Menu.children.forEach(level2Menu => {
              that.currentFatherMenu.push(
                {
                  id : level2Menu.id.toString(),
                  title: level2Menu.title
                }
              )
            })
          }
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
  #menu {
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
