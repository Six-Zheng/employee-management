import http from '@/util/http'

export default {
  postDepartmentSingle (department) {
    return http.post(`/department`, department)
  },

  deleteDepartmentSingle (id) {
    return http.delete(`/department/${id}`)
  },

  deleteDepartmentMultiple (ids) {
    return http.delete(`/department`, {data: ids})
  },

  // 不分页获取所有数据信息
  getDepartmentList () {
    return http.get(`/department`)
  },

  getDepartmentMultiple (current,size) {
    return http.get(`/department/${current}/${size}`)
  },

  getDepartmentMultipleByName (current,size,name) {
    return http.get(`/department/${current}/${size}/${name}`)
  },

  getDepartmentSingle (id) {
    return http.get(`/department/${id}`)
  },

  putDepartmentSingle (department) {
    return http.put(`/department`, department)
  }
}
