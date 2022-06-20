<template>
  <div class="flexBox">
<!--    底部svg图片-->
    <div class="bottomBox">
      <svg width="100%" height="100%" id="svg" viewBox="0 0 1440 450" xmlns="http://www.w3.org/2000/svg" class="transition duration-300 ease-in-out delay-150"><defs><linearGradient id="gradient" x1="0%" y1="50%" x2="100%" y2="50%"><stop offset="5%" stop-color="#002bdc88"></stop><stop offset="95%" stop-color="#32ded488"></stop></linearGradient></defs><path d="M 0,600 C 0,600 0,200 0,200 C 88.39234449760764,171.38755980861245 176.78468899521528,142.7751196172249 279,158 C 381.2153110047847,173.2248803827751 497.2535885167465,232.28708133971287 596,255 C 694.7464114832535,277.71291866028713 776.2009569377989,264.0765550239235 868,247 C 959.7990430622011,229.9234449760765 1061.9425837320575,209.40669856459328 1159,201 C 1256.0574162679425,192.59330143540672 1348.0287081339711,196.29665071770336 1440,200 C 1440,200 1440,600 1440,600 Z" stroke="none" stroke-width="0" fill="url(#gradient)" class="transition-all duration-300 ease-in-out delay-150 path-0"></path><defs><linearGradient id="gradient" x1="0%" y1="50%" x2="100%" y2="50%"><stop offset="5%" stop-color="#002bdcff"></stop><stop offset="95%" stop-color="#32ded4ff"></stop></linearGradient></defs><path d="M 0,600 C 0,600 0,400 0,400 C 82.48803827751198,374.60287081339715 164.97607655502395,349.2057416267943 276,357 C 387.02392344497605,364.7942583732057 526.5837320574162,405.77990430622003 616,434 C 705.4162679425838,462.22009569377997 744.688995215311,477.67464114832535 828,464 C 911.311004784689,450.32535885167465 1038.6602870813397,407.52153110047846 1148,392 C 1257.3397129186603,376.47846889952154 1348.6698564593303,388.2392344497608 1440,400 C 1440,400 1440,600 1440,600 Z" stroke="none" stroke-width="0" fill="url(#gradient)" class="transition-all duration-300 ease-in-out delay-150 path-1"></path></svg>
    </div>
<!--    标题-->
    <div class="titleBox">
      员工信息管理系统
    </div>
<!--    登录表单-->
    <div class="formBox">
      <el-form :label-position="labelPosition" label-width="80px" :model="loginForm" ref="loginForm" :rules="loginFormRules">
        <el-form-item label="用户名" prop="username" status-icon>
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" status-icon>
          <el-input v-model="loginForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode" status-icon>
          <div class="verificationCodeBox">
            <el-input v-model="loginForm.verificationCode"></el-input>
            <el-tooltip class="item" effect="dark" content="点击刷新验证码" placement="right">
              <el-image :src="loginForm.verificationCodeImage" @click="getVerificationCode"></el-image>
            </el-tooltip>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
          <el-button @click="openResetPasswordForm">忘记密码</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
<!--    重设密码表单对话框-->
    <el-dialog :close-on-click-modal="false" title="重置密码" :visible.sync="resetPasswordFormVisible" width="410px" @close="closeForm('resetPasswordForm')">
      <el-form :rules="resetPasswordFormRules" :label-position="labelPosition" label-width="80px" :model="resetPasswordForm" ref="resetPasswordForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="resetPasswordForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <el-input v-model="resetPasswordForm.verificationCode" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetPasswordForm.password" autocomplete="off" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetEmployeePassword">修改</el-button>
          <el-button type="warning" v-if="hasSendVerificationCode === false" @click="sendVerificationCode">发送验证码</el-button>
          <el-button loading type="warning" disabled v-else>剩余{{resendVerificationCodeLeftTime}}秒</el-button>
          <el-button @click="resetForm('resetPasswordForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import verificationCodeService from '@/api/verificationCodeService'
import http from '@/util/http'
import employeeService from '@/api/employeeService'
import emailService from '@/api/emailService'

export default {
  name: 'Login',

  created() {
    this.getVerificationCode()
  },
  data () {
    return {
      // 表单名称位置
      labelPosition: 'right',
      // 登陆表单数据
      loginForm: {
        username: '',
        password: '',
        verificationCode: '',
        verificationCodeKey: '',
        verificationCodeImage: ''
      },
      // 登录表单验证规则
      loginFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 4, max: 4, message: '验证码长度为4个字符', trigger: 'blur'},
        ]
      },
      // 重置密码表单可见标志
      resetPasswordFormVisible: false,
      // 重置密码表单
      resetPasswordForm: {
        username: '',
        verificationCode: '',
        password: ''
      },
      // 重置密码登录表单验证规则
      resetPasswordFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 4, max: 4, message: '验证码长度为4个字符', trigger: 'blur'},
        ],
        password: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ]
      },
      // 是否发送验证码
      hasSendVerificationCode: false,
      // 再次发送验证码的时间计数时间
      resendVerificationCodeLeftTime: 60,
      // 发送验证码的定时器函数
      resendVerificationCodeFunction: null,
    }
  },
  methods: {
    login() {
      employeeService.login(this.loginForm).then(res => {
        this.$store.commit({ type: 'SET_TOKEN', token: res.headers.token })
        this.$router.push('/home/index')
      }).catch(error => {
        this.resetForm('loginForm')
        this.getVerificationCode()
      })
    },
    // 获取验证码
    getVerificationCode() {
      verificationCodeService.getVerificationCode().then(res => {
        this.loginForm.username = '666666'
        this.loginForm.password = '123456'
        this.loginForm.verificationCode = res.data.verificationCode
        this.loginForm.verificationCodeKey = res.data.verificationCodeKey
        this.loginForm.verificationCodeImage = res.data.verificationCodeImage
      })
    },
    // 重置表单
    resetForm (formName) {
      this.$refs[formName].resetFields()

      if (formName == 'resetPasswordForm') {
        this.resendVerificationCodeLeftTime = 60
        this.hasSendVerificationCode = false
        clearInterval(this.resendVerificationCodeFunction);
      }
    },
    // 关闭表单
    closeForm (formName) {
      this.resetForm(formName)
      this.resetPasswordFormVisible = false
    },
    // 打开重置密码表单
    openResetPasswordForm () {
      this.resetForm('loginForm')
      this.resetPasswordFormVisible = true
    },
    // 发送验证码
    sendVerificationCode () {
      this.$refs.resetPasswordForm.validateField("username", error => {
        if (error) {
          return false
        } else {
          // 让用户60秒等待
          this.hasSendVerificationCode = true
          this.resendVerificationCodeFunction = setInterval(() => {
            if (this.resendVerificationCodeLeftTime > 0) {
              this.resendVerificationCodeLeftTime--
            } else {
              this.hasSendVerificationCode = false
              clearInterval(this.resendVerificationCodeFunction);
              this.resendVerificationCodeLeftTime = 60
            }
          }, 1000)
          // TODO 这部分要放在请求成功部分
          // 发送邮件
          emailService.sendResetPasswordVerificationCode(this.resetPasswordForm.username).then(res => {
          })
        }
      })
    },
    resetEmployeePassword() {
      this.$refs.resetPasswordForm.validate((valid) => {
        if (valid) {
          employeeService.resetEmployeePassword(this.resetPasswordForm.username,this.resetPasswordForm.verificationCode,this.resetPasswordForm.password).then(res => {
            this.resetPasswordFormVisible = false
          }).catch(error => {
            this.$message.error("重置密码失败")
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
  .flexBox {
    width: 100%;
    height: 100vh;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    background: white;

    .titleBox {
      padding-left: 50px;
      padding-bottom: 30px;
      font-size: 30px;
    }

    .formBox {
      padding-bottom: 10%;

      .verificationCodeBox {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: 260px;

        .el-input {
          width: 60%;
        }

        .el-image {
          width: 35%;
          height: 40px;
          margin-left: 5%;
          border-radius: 5px;
        }
      }
    }

    .bottomBox {
      position: fixed;
      left: 0;
      bottom: 0;
      width: 100%;

      svg {
        display: block;
      }
    }
  }
</style>
