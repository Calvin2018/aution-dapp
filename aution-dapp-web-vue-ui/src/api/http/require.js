//http请求封装

import axios from 'axios'
// import { BASE_URL,RESPONSE_CODE } from './config'
import store from '@/store'
// import { Message } from 'element-ui'
var qs = require('qs')

// 创建axios的实例

const service = axios.create({
    // withCredentials: true // 允许携带cookie
    // baseURL = '/gateway'
    timeout: 5000000000
})
// service.defaults.baseURL = BASE_URL
// request拦截器
service.interceptors.request.use(
    config => {
        const token =  store.state.user.token ?  store.state.user.token : '' ;
        config.headers.token = token ? `${ token }` : ''; 
        // config.headers = {
        // 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        // };
        // // 在post请求发送出去之前，对其进行编码
        // if (config.method === 'post') {
        // config.data = qs.stringify(config.data);
        // }
        // config.headers[Access-Control-Allow-Credentials]=
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// respone拦截器
service.interceptors.response.use(
    (response) => {
        return response
    },
    (error) => {

        return error.response;
        // Message({
        //     message: error.message,
        //     type: 'error',
        //     duration: 5 * 1000
        //   })
    }
)

service.paramsGet = function (url,params){
    return service({
        method:'get',
        url,
        params
    })
}

export default service