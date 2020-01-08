/**
 * @desc 请求接口文件
 */

/**
 * 登录模块
 */
// 实现类
 import BaseApi from './base'
const coreBase = 'explorer'
// const coreBase = '/api';

class LoginReq extends BaseApi{
    constructor(){
        super(coreBase)
    }

// 登录方法
 loginIn (data){
     return this.fetch.get(`${this.url}/api/blockDetail?blockNumber=36271`,data).then(res => res) 
  // return this.fetch.post('/api/cbaas/login.html',data).then(res => res) 
}

}

export const loginReq = new LoginReq()