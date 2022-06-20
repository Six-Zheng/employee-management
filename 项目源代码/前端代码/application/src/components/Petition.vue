<template>
  <div>
    <div class="petition-box">
      <el-card>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div>
            <el-tag type="info" effect="dark">申请人</el-tag>&nbsp;
            <h3 style="display: inline">{{ item.proposer }}</h3>
          </div>
          <div>
            <el-icon class="el-icon-timer" style="color: #a59399"></el-icon>{{item.createTime}}
          </div>
        </div>
        <el-collapse style="margin: 10px" accordion>
          <el-collapse-item>
            <div style="padding-top: 10px" class="a">
              <el-steps :active="2" simple>
                <el-step icon="el-icon-timer" :title=item.startDate></el-step>
                <el-step icon="el-icon-timer" :title=item.endDate></el-step>
              </el-steps>
            </div>

            <template slot="title">
               <el-avatar v-if="item.type==0">病</el-avatar>
               <el-avatar v-else>事</el-avatar>
               <span style="padding-left: 60%">请假详情</span>
            </template>
            <el-card  style="padding-top: 10px">在界面中一致：所有的元素和结构需保持一致，比如：设计样式、图标和文本、元素的位置等。</el-card>
          </el-collapse-item>
        </el-collapse>
        <div v-if="item.status==0">
          <div style="display: flex; justify-content: space-between">
            <el-tag  type="danger" effect="dark">未审核</el-tag>
            <el-button  type="primary" size="small" effect="dark" @click="dialog.dialogVisible=true">点击进行审核</el-button>
          </div>
        </div>
        <div v-else style="display: flex; justify-content: space-between; align-items: center">
          <div>
            <el-tag type="warning" effect="dark">处理人</el-tag>&nbsp;
            <h3 style="display: inline">{{ item.handler }}</h3>
          </div>
          <div>
            <el-icon class="el-icon-timer" style="color: #a59399"></el-icon>{{item.updateTime}}
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog
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
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialog.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialog.dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Petition',
  data() {
    return {
      dialog: {
        dialogVisible: false,
        currentContent: ''
      },
      item: {
        reply: '情况属实，已批准',
        type: 1,
        status: 0,
        proposer: '郑晓杰',
        handler: '文纯洁',
        content: '因家庭有急事，无法在这两天赶往公司',
        createTime: '2022-02-14 12:29:33',
        startDate: "2022-02-17",
        endDate: '2022-02-18',
        updateTime: '2022-06-08 12:23:32',
      }
    }
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
  .a >>> .el-step__head is-finish {
    color: red;
  }

  .petition-box {
    display: flex;
    flex-direction: column;

    margin-left: 30px;
    width: 450px;
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
