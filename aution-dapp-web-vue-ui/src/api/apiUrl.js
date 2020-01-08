/**
 * @author chengxuan
 * @description api url setting
 */
//
// const cbaasHostName = process.env.BASE_API // const HostName = '' 真实请求所以接口
// const gatewayHostName = process.env.BASE_API2 // const HostName = '' 真实请求所以接口
// const exploreHostName = process.env.BASE_API3 // const HostName = '' 真实请求所以接口
// const etfHostName = process.env.BASE_API4 // const HostName = '' 真实请求所以接口
// const coinHostName = process.env.BASE_API5



const dAppHostName = process.env.BASE_API // const HostName = '' 真实请求所以接口

console.log(dAppHostName);

var urlStr = window.location.host
console.log('urlStr '+urlStr)

const apiUrl = {

    // getUserInfo
    getUserInfo: { url: dAppHostName + 'api/getUserInfo', method: 'POST' },

  // 1查询列表
  findAllGoods: { url: dAppHostName + 'api/goods/findGoodsByParam', method: 'GET' },

  // 2查询用户拍卖列表
  findGoodsBySellerId: { url: dAppHostName + 'api/goods/findGoodsBySellerId', method: 'GET' },

  // 3卖家根据状态查列表
  findGoodsBySellerIdAndStatus: { url: dAppHostName + 'api/goods/findGoodsBySellerIdAndStatus', method: 'GET' },

  // 4买家根据状态查列表
  findGoodsByBuyerIdAndStatus: { url: dAppHostName + 'api/goods/findGoodsByBuyerIdAndStatus', method: 'GET' },

  // 5查询所有商品按照时间排序
  findGoodsByEtimeAndSort: { url: dAppHostName + 'api/goods/findGoodsByEtimeAndSort', method: 'GET' },

  // 6查询所有商品按照商品类型
  findGoodsByType: { url: dAppHostName + 'api/goods/findGoodsByType', method: 'GET' },

  // 7竞拍
  orderBid: { url: dAppHostName + 'api/order/bid', method: 'POST' },

  // 8查询所有商品按照起拍价排序
  findGoodsBySpriceAndSort: { url: dAppHostName + 'api/goods/findGoodsBySpriceAndSort', method: 'GET' },

  // 9模糊查询所有商品按照商品标题
  findGoodsByTitle: { url: dAppHostName + 'api/goods/findGoodsByTitle', method: 'GET' },

  // 10通过商品id精确查询用户信息
  findGoodsByGid: { url: dAppHostName + 'api/goods/findGoodsByGid', method: 'GET' },

  // 11创建商品
  create: { url: dAppHostName + 'api/goods/create', method: 'POST' },

  // 12更新商品信息
  updateGoods: { url: dAppHostName + 'api/goods/updateGoods', method: 'POST' },

  // 13通过用户id获取新的交易消息
  getNewMessage: { url: dAppHostName + 'api/goods/getNewMessage', method: 'GET' },

  // 14查询竞拍历史按照用户id和商品id
  findHistoryByUserIdAndGoodsId: { url: dAppHostName + 'api/history/findHistoryByUserIdAndGoodsId', method: 'GET' },

  // 15查询竞拍历史按照用户id
  findHistoryByUserId: { url: dAppHostName + 'api/history/findHistoryByUserId', method: 'GET' },

  // 16查询竞拍历史按照商品id并且按照时间排序
  findHistoryByGoodsIdAndTimeSort: { url: dAppHostName + 'api/history/findHistoryByGoodsIdAndTimeSort', method: 'GET' },

  // 17查询商品竞拍历史按照每个用户最高价排序
  findHistoryByGoodsIdAndPriceSortAndGroupByUserId: { url: dAppHostName + 'api/history/findHistoryByGoodsIdAndPriceSortAndGroupByUserId', method: 'GET' },

  // 18通过id等参数提交竞价
  postPrice: { url: dAppHostName + 'api/order/bid', method: 'POST' },
  // 19添加评价
  addGoodsContent: { url: dAppHostName + 'api/goods/updateGoods', method: 'POST' },

  login: { url: dAppHostName + 'login', method: 'POST' },

}




export default apiUrl
