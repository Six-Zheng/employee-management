<template>
  <div>
    <div v-if="petition == undefined">
      <el-empty description="暂无任何无请假信息"></el-empty>
    </div>
    <div v-else style="height: 85vh;">
      <div class="pagination">
        <!--    分页信息-->
        <el-pagination
          style="padding-left: 25%"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="pagination.sizes"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
      <div style=" display:flex;justify-content: center;align-items: center; padding: 10px;flex-wrap: wrap;">
        <div style="padding: 10px" v-for="item in petition" :key="item.id">
          <div class="petition-box">
            <el-card>
              <div style="display: flex; justify-content: space-between; align-items: center">
                <div>
                  <el-tag type="info" effect="dark">申请人</el-tag>&nbsp;
                  <h3 style="display: inline">{{ item.proposerName}}</h3>
                </div>
                <div>
                  <el-icon class="el-icon-timer" style="color: #a59399"></el-icon>{{formatter(item.createTime)}}
                </div>
              </div>
              <el-collapse style="margin: 10px" accordion>
                <el-collapse-item>
                  <div style="padding-top: 10px" class="a">
                    <el-steps :active="2" simple>
                      <el-step icon="el-icon-timer" :title=formatter2(item.startDate)></el-step>
                      <el-step icon="el-icon-timer" :title=formatter2(item.endDate)></el-step>
                    </el-steps>
                  </div>

                  <template slot="title">
                    <el-avatar v-if="item.type==0">病</el-avatar>
                    <el-avatar v-else>事</el-avatar>
                    <span style="padding-left: 80%">请假详情</span>
                  </template>
                  <el-card  style="padding-top: 10px">{{item.content}}</el-card>
                </el-collapse-item>
              </el-collapse>
              <div v-if="item.isHandled==0">
                <div style="display: flex; justify-content: space-between">
                  <el-tag  type="danger" effect="dark">未审核</el-tag>
                  <el-button  type="primary" size="small" effect="dark" @click="oepnDialog(item.id)">点击进行审核</el-button>
                </div>
              </div>
              <div v-else style="display: flex; flex-direction: column">
                <div style="display: flex; justify-content: space-between; align-items: center">
                  <div>
                    <el-tag type="warning" effect="dark">处理人</el-tag>&nbsp;
                    <h3 style="display: inline">{{ item.handlerName }}</h3>
                  </div>
                  <div>
                    <el-icon class="el-icon-timer" style="color: #a59399"></el-icon>{{formatter(item.updateTime)}}
                  </div>
                </div>
                <div>
                  <el-collapse style="margin: 10px; margin-left: 0px" accordion>
                    <el-collapse-item>
                      <template slot="title">
                        <el-tag effect="dark" type="success" v-if="item.reply.charAt(0) == 1">已批准</el-tag>
                        <el-tag v-else effect="dark" type="danger">未批准</el-tag>
                        <span style="padding-left: 77%">处理详情</span>
                      </template>
                      <el-card  style="padding-top: 10px">{{item.reply.substring(1)}}</el-card>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </div>
            </el-card>
          </div>
          <el-dialog
            @close="closeDialog"
            title="审核"
            :visible.sync="dialog.dialogVisible"
            width="30%">
            <span></span>
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入审核意见"
              v-model="dialog.currentContent">
            </el-input>
            <div style="padding-top: 10px">
              <el-radio-group v-model="dialog.currentValue" style="display: flex; justify-content: flex-end;">
               <div>
                 <el-radio :label="1" style="color: #67C23A">批准</el-radio>
                 <el-radio :label="0" style="color:#F56C6C">禁止</el-radio>
               </div>
              </el-radio-group>
            </div>
            <span slot="footer" class="dialog-footer">
              <el-button @click="closeDialog">取 消</el-button>
              <el-button type="primary" @click="commitPetition">确 定</el-button>
             </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import petitionService from '@/api/petitionService'
export default {

  name: "Petition.Vue",
  created () {
    this.getPetitionMultiple(this.pagination.current, this.pagination.size)
    console.log(this.petition)
  },
  data() {
    return {
      dialog: {
        dialogVisible: false,
        currentContent: '',
        currentValue: 3,
        currentPetitionId: '',
      },
      pagination: {
        current: 1,
        size: 3,
        sizes: [3, 10, 15]
      },
      petition:[],
    }
  },
  methods: {
    // 关闭审核框
    closeDialog() {
      this.dialog.dialogVisible = false
      setTimeout(() => {
        this.dialog.currentValue= 1;
        this.dialog.currentContent = '';
        this.dialog.currentPetitionId = ''
      },500)
    },
    // 进行审核
    oepnDialog(id) {
      this.dialog.dialogVisible=true
      this.dialog.currentPetitionId = id;
    },
    // 提交审核意见
    async commitPetition() {
      const petition = await petitionService.getPetitionSingle(this.dialog.currentPetitionId).then(res => res.data.petition)
      console.log(petition)
      petition.reply = this.dialog.currentValue + this.dialog.currentContent
      petition.handlerAccount = this.$store.state.employee.username
      petition.isHandled = 1
      petitionService.putPetitionSingle(petition).then(res => {
        this.getPetitionMultiple(this.pagination.current, this.pagination.size)
        this.closeDialog()
      })
    },

    // 格式化时间1
    formatter(val) {
      return val.substring(0, 19).replace('T',' ')
    },

    // 格式化时间2
    formatter2(val) {
      return val.slice(0, 10)
    },
    getPetitionMultiple (current, size) {
      petitionService.getPetitionMultiple(current, size).then(res => {
        console.log(res.data)
        this.petition = res.data.petition
        this.pagination.size = res.data.petitionPage.size
        this.pagination.current = res.data.petitionPage.current
        this.pagination.total = res.data.petitionPage.total
      })
    },
    handleSizeChange (val) {
      this.pagination.size = val
      this.getPetitionMultiple(this.pagination.current, this.pagination.size)
    },
    handleCurrentChange (val) {
      this.pagination.current = val
      this.getPetitionMultiple(this.pagination.current, this.pagination.size)
    },
  }
}
</script>

<style>
  .el-step.is-simple:not(:last-of-type) .el-step__title {
    max-width: 100%;
  }

  .el-step__title.is-finish {
    color: black;
  }
</style>

<style scoped>
  .petition-box {
    display: flex;
    flex-direction: column;
    margin-left: 30px;
    width: 100vh;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    border-radius: 3px;
  }

  .el-step__head.is-finish {
    color: red;
  }

  .el-tag {
    border-radius: 3px
  }
</style>
