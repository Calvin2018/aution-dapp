webpackJsonp([8],{"3Sum":function(t,s){},ATzu:function(t,s,e){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var i=e("y7k7"),a=e("SWAV"),n=e("oFuF"),o=e("Fd2+"),r={created:function(){},data:function(){return{goodsstatus:0,number:0,barList1:["进行中拍卖","拍卖成功","拍卖失败"],barList2:["进行中竞拍","竞拍成功","竞拍失败"],status:this.$store.state.user.status,active:this.$store.state.user.active,finished:!1,refreshLoading:!1,loading:!1,commodityList:[],pageSize:10,currentPage:1,loadingArr:[!1,!1,!1]}},computed:{title:function(){return this.number<=3?"参与的拍卖":"参与的竞拍"}},methods:{onLoad:function(){this.updateList()},onRefresh:function(t){this.currentPage,this.updateList()},backHandler:function(){this.$router.go(-1)},goDetailHandler:function(t){this.goodsstatus=this.commodityList[t].status,this.$router.push({path:"/detail",name:"Detail",query:{goodsId:this.commodityList[t].goodsId,shwoBtn:!0,goodsstatus:this.goodsstatus}})},updateList:function(){var t=this,s=void 0;0===this.status?0===this.active?s={apiObj:i.a.findGoodsBySellerIdAndStatus,query:{sellerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:1}}:1===this.active?s={apiObj:i.a.findGoodsBySellerIdAndStatus,query:{sellerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:2}}:2===this.active&&(s={apiObj:i.a.findGoodsBySellerIdAndStatus,query:{sellerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:3}}):1===this.status&&(0===this.active?s={apiObj:i.a.findGoodsByBuyerIdAndStatus,query:{buyerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:1}}:1===this.active?s={apiObj:i.a.findGoodsByBuyerIdAndStatus,query:{buyerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:2}}:2===this.active&&(s={apiObj:i.a.findGoodsByBuyerIdAndStatus,query:{buyerId:this.$store.state.user.id,size:this.pageSize,page:this.currentPage-1,status:3}})),a.a.accessAPI(s).then(function(s){"0"===s.code?(console.log(t.loading),t.commodityList.splice(0),t.commodityList=s.data,t.commodityList.forEach(function(t){t.endTime=n.a.dateToStr(new Date(t.endTime),5)}),t.commodityList.forEach(function(t){t.imgs=t.imgs.split(";"),console.log(t.imgs[0])}),t.loading=!1,t.refreshLoading=!1,t.commodityList.length<=t.currentPage*t.pageSize?t.finished=!0:t.currentPage++):Object(o.x)(s.msg)},function(s){t.commodityList=[],t.loading=!1,t.finished=!0})}},watch:{active:function(){this.$store.commit("user/setStatusA",{status:this.status,active:this.active}),this.updateList()}}},c={render:function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",{staticClass:"list-page"},[i("div",{staticClass:"list-title"},[i("img",{staticClass:"back-btn",attrs:{src:e("s50C")},on:{click:t.backHandler}}),t._v("\n        "+t._s(t.title)+"\n    ")]),t._v(" "),i("van-tabs",{attrs:{animated:""},model:{value:t.active,callback:function(s){t.active=s},expression:"active"}},t._l(3,function(s){return i("van-tab",{key:s,attrs:{title:0===t.status?t.barList1[s-1]:t.barList2[s-1]}},[i("van-pull-refresh",{on:{refresh:t.onRefresh},model:{value:t.refreshLoading,callback:function(s){t.refreshLoading=s},expression:"refreshLoading"}},[i("van-list",{attrs:{finished:t.finished,"finished-text":"没有更多了"},on:{load:t.onLoad},model:{value:t.loading,callback:function(s){t.loading=s},expression:"loading"}},t._l(t.commodityList,function(s,e){return i("div",{key:s.goodsId,staticClass:"commodity-item",attrs:{title:s.goodsId},on:{click:function(s){return t.goDetailHandler(e)}}},[i("div",{staticClass:"img-box"},[i("img",{attrs:{src:"img/"+s.imgs[0],alt:""}})]),t._v(" "),i("div",{staticClass:"desc-box"},[i("div",{staticClass:"desc-title"},[t._v(t._s(s.title))]),t._v(" "),1===t.active||1===t.status&&2===t.active?t._e():i("div",{staticClass:"desc-time"},[t._v("截止时间："+t._s(s.endTime))]),t._v(" "),1===t.active||1===t.status&&2===t.active?i("div",{staticClass:"desc-time"},[t._v("成交时间："+t._s(s.endTime))]):t._e(),t._v(" "),0===t.active?i("div",{staticClass:"desc-price"},[t._v("当前价格："+t._s(s.currentPrice)+" Scoin")]):t._e(),t._v(" "),1===t.active||1===t.status&&2===t.active?i("div",{staticClass:"desc-price"},[t._v("成交价："+t._s(s.currentPrice)+" Scoin")]):t._e(),t._v(" "),0===t.status&&2===t.active?i("div",{staticClass:"desc-price"},[t._v("起拍价："+t._s(s.startPrice)+" Scoin")]):t._e()]),t._v(" "),i("div",{staticClass:"quiz-btn"})])}),0)],1)],1)}),1)],1)},staticRenderFns:[]};var d=e("VU/8")(r,c,!1,function(t){e("3Sum")},null,null);s.default=d.exports}});
//# sourceMappingURL=8.f0f835b47519ae34bb53.js.map