/*
*
*/
import http from '@/util/http'

export default {
  getData() {
    return http.get('/data')
  }
}
