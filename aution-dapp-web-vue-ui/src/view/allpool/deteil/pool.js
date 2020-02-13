import BaseApi from '../../../api/http/base'
// import { BASE_URL,RESPONSE_CODE } from '@/api/http/config.js'
// const cbaasHostName = API_CONFIG.BASE_API // const HostName = '' 真实请求所以接口
const gatewayHostName = process.env.BASE_API

class AllianceReq extends BaseApi{
    constructor(){
        super(gatewayHostName)
        // super(local)
    }

    创建联盟
    postPriceto (data1){
        return this.fetch.post(`${this.url}dapp/api/order/bid`,data1).then(res => res.data)
    }
    
}
export const allianceReq = new AllianceReq()
