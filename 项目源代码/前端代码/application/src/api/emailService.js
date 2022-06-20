import http from '@/util/http'

export default {
  // 发送验证码到用户邮箱
  sendResetPasswordVerificationCode(username) {
    return http.post(`/email/reset_password/${username}`)
  }
}
