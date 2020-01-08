const user = {
    namespaced: true,
    state: {

        userInfo :{},

        // 竞拍还是拍卖
        status: 0,
        active: 0,
        id:'',
        // 0-6表示不同的状态。
        mySelfListStatus:0,
        goodsStatus:0
    },
    mutations: {
        setgoodsStatus(state,str){
            state.goodsStatus = str;
        },
        setmySelfListStatus(state,str){
            state.mySelfListStatus = str;
        },
        setStatusA(state, params) {
            state.status = params.status
            state.active = params.active
        },
        setUserInfo(state, params) {
            state.userInfo = params
        },
        setid(state,str){
            state.id=str
        }
    }
}
export default user