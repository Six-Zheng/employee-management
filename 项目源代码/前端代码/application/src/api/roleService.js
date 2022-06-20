import http from '@/util/http'

export default {
  postRoleSingle (role) {
    return http.post(`/role`, role)
  },

  deleteRoleSingle (id) {
    return http.delete(`/role/${id}`)
  },

  deleteRoleMultiple (ids) {
    return http.delete(`/role`, {data: ids})
  },

  getRoleList () {
    return http.get(`/role`)
  },

  getRoleMultiple (current,size) {
    return http.get(`/role/${current}/${size}`)
  },

  getRoleMultipleByName (current,size,name) {
    return http.get(`/role/${current}/${size}/${name}`)
  },

  getRoleSingle (id) {
    return http.get(`/role/${id}`)
  },

  putRoleSingle (role) {
    return http.put(`/role`, role)
  }
}
