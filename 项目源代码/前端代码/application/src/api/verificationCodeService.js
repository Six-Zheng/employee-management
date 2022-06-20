import http from '@/util/http'

// 验证码服务
export default {
  // 获取验证码
  getVerificationCode () {
    return http.get('/verification_code')
  }

  // 向邮箱发送验证码
  
}
