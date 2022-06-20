import http from '@/util/http'

export default {
  postRoleMenuSingle (RoleMenu) {
    return http.post(`/role_menu`, RoleMenu)
  },

  deleteRoleMenuSingle (id) {
    return http.delete(`/role_menu/${id}`)
  },

  deleteRoleMenuMultiple (ids) {
    return http.delete(`/role_menu`, {data: ids})
  },

  // 通过token获取用户所对应的角色的菜单
  getRoleMenuByToken() {
    return http.get('/role_menu/token')
  },

  // 不分页获取所有数据信息
  getRoleList () {
    return http.get(`/role`)
  },

  getRoleMenuMultiple () {
    return http.get(`/role_menu`)
  },

  getRoleMenuMultipleByName (name) {
    return http.get(`/role_menu/${name}`)
  },

  getRoleMenuMultipleByRoleId (roleId) {
    return http.get(`/role_menu/role_id/${roleId}`)
  },

  getRoleMenuSingle (id) {
    return http.get(`/role_menu/${id}`)
  },

  putRoleMenuSingle (RoleMenu) {
    return http.put(`/role_menu`, RoleMenu)
  },

  putRoleMenuMultiple (roleId, ids) {
    return http.put(`/role_menu/role_id/${roleId}`, ids)
  }
}

