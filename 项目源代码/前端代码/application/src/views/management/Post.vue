<template>
  <div id="post">
    <!--    顶部操作栏-->
    <div class="pagination">
      <div>
        <el-button size="mini" type="primary" @click="openPostDialogForm()" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:POST_SINGLE')">添加岗位</el-button>
        <el-button size="mini" type="danger" @click="deletePostMultiple(postId)" :disabled="this.postId.length === 0" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:DELETE_MULTIPLE')">批量删除岗位</el-button>
        <el-button size="mini" type="success" @click="output" v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:EXPORT')">导出岗位信息</el-button>
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
    <!--    岗位信息-->
    <div id="table">
      <el-table :data="post"  @selection-change="handleSelectionChange" v-loading="loadingStatus">
        <el-table-column
          :selectable="selectable"
          v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:DELETE_MULTIPLE')"
          prop="id" type="selection" width="55">
        </el-table-column>
        <el-table-column type="index" width="80" label="序号">
        </el-table-column>
        <el-table-column prop="name" label="名称" width="180">
          <template slot-scope="scope">
            <el-tag type="warning" effect="dark" v-if="scope.row.name === '默认岗位'">
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
              @input="getPostMultipleByName(pagination.current, pagination.size, name)"
              v-model="name"
              size="mini"
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:GET_MULTIPLE')"
              placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:UPDATE_SINGLE')"
              size="mini"
              @click="openPostDialogForm(scope.row.id)">编辑</el-button>
            <el-button
              v-if="$store.state.menu.permissionList.find(item => item === 'MANAGEMENT:POST:DELETE_SINGLE') && scope.row.name !== '默认岗位'"
              size="mini"
              type="danger"
              @click="deletePostSingle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--    岗位表单信息-->
    <el-dialog title="岗位信息"
               :close-on-click-modal="false"
               @close="closePostDialogForm('departmenForm')"
               width="25%"
               :visible.sync="postDialogFormVisiable"
               top="25vh">
      <el-form :rules="postFormRules" :model="postForm" label-position="right" label-width="100px" ref="postForm">
        <el-form-item label="岗位名称" prop="name" >
          <el-input v-model="postForm.name" autocomplete="off" :disabled="postForm.name === '默认岗位' && postForm.id == '1'"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="postForm.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closePostDialogForm('postForm')">取 消</el-button>
        <el-button type="primary" @click="postForm.id?putPostSingle(postForm):postPostSingle(postForm)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import postService from '@/api/postService'
import http from '@/util/http'
import departmentService from '@/api/departmentService'
import store from '@/store'
import { SET_DEPARTMENT } from '@/store/mutation_name'

export default {
  created () {
    this.getPostMultiple(this.pagination.current, this.pagination.size)
  },
  name: 'Post',
  data () {
    // 关于岗位表单名称的校验规则
    const checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('岗位名称不能为空'))
      }
      const postWithSameName = this.post.filter(item => item.name === value) // 判定是否存在相同名称的岗位

      if (this.postForm.id) { // 若当前为编辑表单状态
        if (postWithSameName.length > 1 && value != this.currentPostFormName) {
          return callback(new Error('已存在相同的岗位名称，请修改岗位名称'))
        }
      } else { // 若当前非为编辑表单状态
        if (postWithSameName.length > 0) { //若当前表单大于0，则证明已存在此岗位
          return callback(new Error('已存在相同的岗位名称，请修改岗位名称'))
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
      // 关于岗位的数据
      post:[],
      // 关于岗位表单的数据
      postForm: {
        id: '',
        name: '',
        description: ''
      },
      // 关于岗位表单的检测规则
      postFormRules: {
        name: [
          { trigger: 'blur', validator: checkName }
        ]
      },
      // 关于岗位表单的检测状态
      postFormStatus: false,
      // 关于首次打开编辑表单的名称
      currentPostFormName: '',
      // 关于搜索关键字
      name: '',
      // 关于岗位进行多选时所需要的岗位ID
      postId: [],
      // 关于岗位数据加载时候的状态
      loadingStatus: false,

      checkAll: false,
      isIndeterminate: true,
      postDialogFormVisiable: false,
      deletePostBatchList: []
    }
  },
  methods: {
    output() {
      const data = this.$store.state.post
      let str = `岗位名称,岗位描述\n`;
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
      link.download =  "岗位数据表.csv";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    selectable(row) {
      return row.name !== '默认岗位'
    },

    // 全局刷新post
    refreshPost() {
      postService.getPostList().then(res => {
        store.commit({ type: SET_POST, post: res.data.post })
      })
    },
    // 关于分页的方法
    handleSizeChange (val) {
      this.pagination.size = val
      this.getPostMultiple(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getPostMultiple(this.pagination.current, this.pagination.size)
    },
    // 关于岗位的方法
    postPostSingle (post) {
      this.$refs.postForm.validate((valid) => {
        if (valid) {
          postService.postPostSingle(post).then(res => {
            this.postDialogFormVisiable = false
            this.getPostMultiple(this.pagination.current, this.pagination.size)
            this.refreshPost()
          })
        } else {
          return false
        }
      })
    },
    deletePostSingle (id) {
      this.$confirm('此操作将永久删除该岗位信息且此岗位下的员工将会移动到待分配岗位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        postService.deletePostSingle(id).then(res => {
          this.getPostMultiple(this.pagination.current, this.pagination.size)
          this.refreshPost()
        })
      })
    },
    deletePostMultiple (postId) {
      this.$confirm('此操作将永久删除该岗位信息且此岗位下的员工将会移动到待分配岗位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        postService.deletePostMultiple(postId).then(res => {
          this.getPostMultiple(this.pagination.current, this. pagination.size)
          this.refreshPost()
        })
      })
    },
    getPostMultiple (current, size) {
      this.loadingStatus = true
      postService.getPostMultiple(current, size).then(res => {
        this.post = res.data.postPage.records
        this.pagination.size = res.data.postPage.size
        this.pagination.current = res.data.postPage.current
        this.pagination.total = res.data.postPage.total
        this.loadingStatus = false
      })
    },
    getPostMultipleByName (current, size, name) {
      postService.getPostMultipleByName(current, size, name).then(res => {
        this.post = res.data.postPage.records
        this.pagination.size = res.data.postPage.size
        this.pagination.current = res.data.postPage.current
        this.pagination.total = res.data.postPage.total
      })
    },
    putPostSingle (post) {
      this.$refs.postForm.validate((valid) => {
        if (valid) {
          this.$confirm('此操作将修改此岗位信息, 是否继续?', '提示', {
            confirmButtonText: '修改',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(res => {
            postService.putPostSingle(post).then(res => {
              this.getPostMultiple(this.pagination.current, this.pagination.size)
              this.postDialogFormVisiable = false
            })
          }).catch(res => {
            this.$message.info("已取消对此岗位信息的修改")
          })
        }
      })
    },
    // 关于岗位表单窗口方法
    openPostDialogForm (id) {
      this.postDialogFormVisiable = true
      if (id) { //若存在ID属性，则表明为编辑操作，否则则为添加操作
        postService.getPostSingle(id).then(res => {
          this.postForm = {
            id: res.data.post.id,
            name: res.data.post.name,
            description: res.data.post.description
          }
          this.currentPostFormName = this.postForm.name
        })
      } else {
        this.postForm = {}
      }
    },
    closePostDialogForm (formName) {
      this.postDialogFormVisiable = false
      setTimeout(() => {
        this.$refs.postForm.resetFields()
      }, 500)
    },
    // 关于点击岗位数据的多选框方法
    handleSelectionChange (post) {
      this.postId = post.map(item => item.id)
    },
  }
}
</script>

<style scoped lang="scss">
  #post {
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
