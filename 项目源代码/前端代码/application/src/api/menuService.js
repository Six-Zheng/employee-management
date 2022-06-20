import http from '@/util/http'

export default {
  postMenuSingle (Menu) {
    return http.post(`/menu`, Menu)
  },

  deleteMenuSingle (id) {
    return http.delete(`/menu/${id}`)
  },

  deleteMenuMultiple (ids) {
    return http.delete(`/menu`, {data: ids})
  },

  // 通过token获取用户所对应的角色的菜单
  getMenuByToken() {
    return http.get('/menu/token')
  },

  getMenuMultiple () {
    return http.get(`/menu`)
  },

  getMenuMultipleByName (name) {
    return http.get(`/menu/${name}`)
  },

  getMenuSingle (id) {
    return http.get(`/menu/${id}`)
  },

  putMenuSingle (Menu) {
    return http.put(`/menu`, Menu)
  },

  getMenuByRoleId (roleId) {
    return http.get(`/menu/test/${roleId}`)
  },

  getLevel2Component () {
    return http.get('/menu/level2')
  },

  getLevel2ComponentD () {
    return http.get('/menu/level12')
  }
}

