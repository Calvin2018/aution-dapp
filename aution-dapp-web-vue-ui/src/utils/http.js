import axios from 'axios'
// import { Message } from 'element-ui'
import store from '@/store'

axios.defaults.withCredentials = true
// 创建一个axios实例
const service = axios.create({
  // baseURL: "https://connect.zto.com/session", 存在一些场景，借口一部分调这个地址一部分调另外一个地址
  // timeout: 5000, // 超时时间
  withCredentials: true // 允许携带cookie
})

// 请求发送处理
service.interceptors.request.use(
  config => {
    // console.log(config)
    // 可以对用户权限进行请求进行拦截 Promise.reject(error)
    // 在发送请求做一些事情
    if(store.state.user.token){
      config.headers.common['token']=store.state.user.token
    }
    return config
  },
  error => {
    // 发送请求失败报错
    // console.log('err' + error) // for debug
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 1 * 1000
    // })
    Promise.reject(error)
  }
)

// respone拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (!response.status) {
      // 请求异常
      console.log(error.response.data.message == 'null')
      if(error.response.data.message == 'null') {
        // Message({
        //   message: error.message,
        //   type: 'error',
        //   duration: 5 * 1000
        // })
      }else {
        // Message({
        //   message: error.response.data.message || error.message,
        //   type: 'error',
        //   duration: 5 * 1000
        // })
      }
      
      return Promise.reject('error')
    } else {
      // 请求成功
      return res
    }
  },
  error => {
    // console.log('err' + error) // for debug
    if(error.response.data.message == 'null') {
      // Message({
      //   message: error.message,
      //   type: 'error',
      //   duration: 5 * 1000
      // })
    }else {
      // Message({
      //   message: error.response.data.message || error.message,
      //   type: 'error',
      //   duration: 5 * 1000
      // })
    }

    return Promise.reject(error)
  }
)

export default service
