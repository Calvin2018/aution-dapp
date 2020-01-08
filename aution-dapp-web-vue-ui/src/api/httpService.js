import http from '@/utils/http'
import { util } from '@/utils/util'
import { SessionStorage } from '@/utils/storage'
export default {
  accessAPI: function(opts) {
    const apiObj = opts.apiObj || {}
    const _method = apiObj.method
    const _noAuth = opts.noAuth || opts.noAuth
    const _body = opts.body || {}
    const _path = opts.path
    const _query = {}
    const _headers = opts.headers  || {}
    const _token = opts.token || ''

    let apiUrl = apiObj.url
    // let _token = ''
    //
    // if (SessionStorage.get('auth_token')) {
    //   _token = SessionStorage.get('auth_token')
    // }
    _headers.Authorization = !_noAuth ? 'Bearer ' + _token : _noAuth
    if (util.isObject(_path)) {
      Object.keys(_path).forEach(key => {
        apiUrl = apiUrl.replace(':' + key, _path[key])
      })
    }
    if (opts.query && !util.isEmptyObject(opts.query)) {
      Object.keys(opts.query).forEach(key => {
        if (opts.query[key] !== undefined && opts.query[key] !== null) {
          _query[key] = opts.query[key]
        }
      })
    }

    return http({
      url: apiUrl,
      method: _method,
      params: _query,
      data: _body,
      headers: _headers
    })
  }
}
