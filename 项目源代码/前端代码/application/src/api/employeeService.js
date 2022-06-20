import http from '@/util/http'

// 员工服务
export default {
  getEmployeefixed() {
    return http.get('/employee/fixed/bug')
  },

  // 员工登录
  login(loginForm) {
    return http.post('/employee/login', loginForm,
      {
        headers: {
            "verification-code-key": loginForm.verificationCodeKey
        }
        }
      )
  },

  //员工退出
  logout() {
    return http.post('/logout')
  },

  postEmployeeSingle (employee) {
    return http.post(`/employee`, employee)
  },

  deleteEmployeeSingle (id) {
    return http.delete(`/employee/${id}`)
  },

  deleteEmployeeMultiple (ids) {
    return http.delete(`/employee`, {data: ids})
  },

  getEmployee() {
    return http.get('/employee')
  },

  getEmployeeByUsername(username) {
    return http.get(`/employee/${username}/fix`)
  },

  getEmployeeByToken() {
    return http.get('/employee/token')
  },

  getEmployeeMultiple (current,size) {
    return http.get(`/employee/${current}/${size}`)
  },

  getEmployeeMultipleByName (current,size,name) {
    return http.get(`/employee/${current}/${size}/${name}`)
  },

  getEmployeeSingle (id) {
    return http.get(`/employee/${id}`)
  },

  putEmployeeSingle (employee) {
    return http.put(`/employee`, employee)
  },

  resetEmployeePassword(username, verification_code, password) {
    return http.put(`/employee/reset_password/${username}/${verification_code}/${password}`)
  },

  resetEmployeePasswordSelf(username, old_password, new_password) {
    return http.put(`/employee/reset_password_self/${username}/${old_password}/${new_password}`)
  },

  download() {
    return http.get('/api/employee/download')
  },

  uploadAvatar(formData, token) {
    return http.post('/employee/avatar', formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        "token": token
      }
    })
  }
}
