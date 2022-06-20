<template>
  <div id="department">
<!--    顶部操作栏-->
    <div class="pagination">
      <div>
        <el-button size="mini" type="primary" @click="openDepartmentDialogForm()" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:POST_SINGLE')">添加部门</el-button>
        <el-button size="mini" type="danger" @click="deleteDepartmentMultiple(departmentId)" :disabled="this.departmentId.length === 0" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:DELETE_MULTIPLE')">批量删除部门</el-button>
        <el-button size="mini" type="success" @click="output" v-if="permissionList2.find(item => item === 'MANAGEMENT:DEPARTMENT:EXPORT')">导出部门信息</el-button>
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
<!--    部门信息-->
    <div id="table">
      <el-table
        :data="department"  @selection-change="handleSelectionChange" v-loading="loadingStatus">
        <el-table-column
          :selectable="selectable"
          prop="id" type="selection"
          width="55"
          v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:DELETE_MULTIPLE')">
        </el-table-column>
        <el-table-column type="index" width="80" label="序号">
        </el-table-column>
        <el-table-column  label="名称" width="180">
          <template slot-scope="scope">
            <el-tag type="warning" effect="dark" v-if="scope.row.name === '默认部门'">
              {{scope.row.name}}
            </el-tag>
            <el-tag type="info" effect="dark" v-else>
              {{scope.row.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column
          width="250"
          align="right">
          <template slot="header" slot-scope="scope">
            <el-input
              @input="getDepartmentMultipleByName(pagination.current, pagination.size, name)"
              v-model="name"
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:GET_MULTIPLE')"
              placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:UPDATE_SINGLE')"
              @click="openDepartmentDialogForm(scope.row.id)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:DEPARTMENT:DELETE_SINGLE') && scope.row.name !== '默认部门'"
              @click="deleteDepartmentSingle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
<!--    部门表单信息-->
    <el-dialog
      :close-on-click-modal="false"
              title="部门信息"
               @close="closeDepartmentDialogForm('departmenForm')"
               width="30%"
               :visible.sync="departmentDialogFormVisiable"
               top="25vh">
      <el-form :rules="departmentFormRules" :model="departmentForm" label-position="right" label-width="100px" ref="departmentForm">
        <el-form-item label="部门名称" prop="name" >
          <el-input v-model="departmentForm.name" autocomplete="off" :disabled="departmentForm.name === '默认部门' && departmentForm.id == '1'"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="departmentForm.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDepartmentDialogForm('departmentForm')">取 消</el-button>
        <el-button type="primary" @click="departmentForm.id?putDepartmentSingle(departmentForm):postDepartmentSingle(departmentForm)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import departmentService from '@/api/departmentService'
import http from '@/util/http'
import store from '@/store'
import { SET_DEPARTMENT } from '@/store/mutation_name'

export default {
  created () {
    this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
  },
  name: 'Department',
  data () {
    // 关于部门表单名称的校验规则
    const checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('部门名称不能为空'))
      }
      const departmentWithSameName = this.department.filter(item => item.name === value) // 判定是否存在相同名称的部门

      if (this.departmentForm.id) { // 若当前为编辑表单状态
        if (departmentWithSameName.length > 1 && value != this.currentDepartmentFormName) {
          return callback(new Error('已存在相同的部门名称，请修改部门名称'))
        }
      } else { // 若当前非为编辑表单状态
        if (departmentWithSameName.length > 0) { //若当前表单大于0，则证明已存在此部门
          return callback(new Error('已存在相同的部门名称，请修改部门名称'))
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
      // 关于部门的数据
      department:[],
      // 关于部门表单的数据
      departmentForm: {
        id: '',
        name: '',
        description: ''
      },
      // 关于部门表单的检测规则
      departmentFormRules: {
        name: [
          { trigger: 'blur', validator: checkName }
        ]
      },
      // 关于部门表单的检测状态
      departmentFormStatus: false,
      // 关于首次打开编辑表单的名称
      currentDepartmentFormName: '',
      // 关于搜索关键字
      name: '',
      // 关于部门进行多选时所需要的部门ID
      departmentId: [],
      // 关于部门数据加载时候的状态
      loadingStatus: false,

      checkAll: false,
      isIndeterminate: true,
      departmentDialogFormVisiable: false,
      deleteDepartmentBatchList: []
    }
  },
  computed: {
    permissionList2: {
      get () {
        return this.$store.state.menu.permissionList
      }
    }
  },
  methods: {
    output() {
      const data = this.$store.state.department
      let str = `部门名称,部门描述\n`;
      for(let i = 0 ; i < data.length ; i++ ){
        let index = 0
        for(let item in data[i]){
          index = index + 1;
          if (index > 5)
            str+=`${data[i][item] + '\t'},`;
        }
        // for (let item = 5; item < data[i].length; i++) {
        //   str+=`${data[i][item] + '\t'},`;
        // }
        str+='\n';
      }
      let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
      //通过创建a标签实现
      var link = document.createElement("a");
      link.href = uri;
      //对下载的文件命名
      link.download =  "部门数据表.csv";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    selectable(row) {
      return row.name !== '默认部门'
    },

    // 全局刷新department
    refreshDepartment() {
      departmentService.getDepartmentList().then(res => {
        store.commit({ type: SET_DEPARTMENT, department: res.data.department })
      })
    },

    // 关于分页的方法
    handleSizeChange (val) {
      this.pagination.size = val
      this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
    },
    // 关于部门的方法
    postDepartmentSingle (department) {
      this.$refs.departmentForm.validate((valid) => {
        if (valid) {
          departmentService.postDepartmentSingle(department).then(res => {
            this.departmentDialogFormVisiable = false
            this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
            this.refreshDepartment()
          })
        } else {
          return false
        }
      })
    },
    deleteDepartmentSingle (id) {
      this.$confirm('此操作将永久删除该部门信息且此部门下的员工将会移动到默认部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        departmentService.deleteDepartmentSingle(id).then(res => {
          this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
          this.refreshDepartment()
        })
      })
    },
    deleteDepartmentMultiple (departmentId) {
      this.$confirm('此操作将永久删除该部门信息且此部门下的员工将会移动到默认部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        departmentService.deleteDepartmentMultiple(departmentId).then(res => {
          this.getDepartmentMultiple(this.pagination.current, this. pagination.size)
          this.refreshDepartment()
        })
      })
    },
    getDepartmentMultiple (current, size) {
      this.loadingStatus = true
      departmentService.getDepartmentMultiple(current, size).then(res => {
        this.department = res.data.departmentPage.records
        this.pagination.size = res.data.departmentPage.size
        this.pagination.current = res.data.departmentPage.current
        this.pagination.total = res.data.departmentPage.total
        this.loadingStatus = false
      })
    },
    getDepartmentMultipleByName (current, size, name) {
      departmentService.getDepartmentMultipleByName(current, size, name).then(res => {
        this.department = res.data.departmentPage.records
        this.pagination.size = res.data.departmentPage.size
        this.pagination.current = res.data.departmentPage.current
        this.pagination.total = res.data.departmentPage.total
      })
    },
    putDepartmentSingle (department) {
      this.$refs.departmentForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此部门信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            departmentService.putDepartmentSingle(department).then(res => {
              this.getDepartmentMultiple(this.pagination.current, this.pagination.size)
              this.departmentDialogFormVisiable = false
            })
          }).catch(res => {
            this.$message.info("已取消对此部门信息的修改")
          })
        }
      })
    },
    // 关于部门表单窗口方法
    openDepartmentDialogForm (id) {
      this.departmentDialogFormVisiable = true
      if (id) { //若存在ID属性，则表明为编辑操作，否则则为添加操作
        departmentService.getDepartmentSingle(id).then(res => {
          this.departmentForm = {
            id: res.data.department.id,
            name: res.data.department.name,
            description: res.data.department.description
          }
          this.currentDepartmentFormName = this.departmentForm.name
        })
      } else {
        this.departmentForm = {}
      }
    },
    closeDepartmentDialogForm (formName) {
      this.departmentDialogFormVisiable = false
      setTimeout(() => {
        this.$refs.departmentForm.resetFields()
      }, 500)
    },
    // 关于点击部门数据的多选框方法
    handleSelectionChange (department) {
      this.departmentId = department.map(item => item.id)
    },
  }
}
</script>

<style scoped lang="scss">
  #department {
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
