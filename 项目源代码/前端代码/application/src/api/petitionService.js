import http from '@/util/http'

export default {
  /*
  * 申请请假条
  * */
  postPetitionSingle (petition) {
    return http.post(`/petition`, petition)
  },

  /*
  * 撤销请假条：针对于申请者
  * */
  deletepetitionSingle (id) {
    return http.delete(`/petition/${id}`)
  },

  /*
  * 分页获取请假条
  * */
  getPetitionMultiple (current,size) {
    return http.get(`/petition/${current}/${size}`)
  },

  /*
  * 获取单条请假条
  * */
  getPetitionSingle (id) {
    return http.get(`/petition/${id}`)
  },

  /*
  * 修改请假条 针对于处理者
  * */
  putPetitionSingle (petition) {
    return http.put(`/petition`, petition)
  }
}
