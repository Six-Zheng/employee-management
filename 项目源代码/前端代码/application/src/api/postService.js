import http from '@/util/http'

export default {
  postPostSingle (post) {
    return http.post(`/post`, post)
  },

  deletePostSingle (id) {
    return http.delete(`/post/${id}`)
  },

  deletePostMultiple (ids) {
    return http.delete(`/post`, {data: ids})
  },

  // 不分页获取所有数据信息
  getPostList () {
    return http.get(`/post`)
  },

  getPostMultiple (current,size) {
    return http.get(`/post/${current}/${size}`)
  },

  getPostMultipleByName (current,size,name) {
    return http.get(`/post/${current}/${size}/${name}`)
  },

  getPostSingle (id) {
    return http.get(`/post/${id}`)
  },

  putPostSingle (post) {
    return http.put(`/post`, post)
  }
}
