<template>
  <div class="main-page">
    <router-view style='padding-bottom: 3.3125rem'/>
    <div class="tabbar-box">
      <van-tabbar v-model="active"
                  active-color="#f93202">
        <van-tabbar-item to="/">
          <span>首页</span>
          <img
            slot="icon"
            v-show="active === 0"
            src="@/static/images/main_icon2.png">
          <img
            slot="icon"
            v-show="active !== 0" 
            src="@/static/images/main_icon1.png">
        </van-tabbar-item>
        <van-tabbar-item to="/release">发布
          <img
            slot="icon"
            v-if="active === 1"
            src="@/static/images/public_icon2.png">
          <img
            slot="icon"
            v-if="active !== 1"
            src="@/static/images/public_icon1.png">
        </van-tabbar-item>
        <van-tabbar-item to="/userInfo">我的
          <img
            slot="icon"
            v-if="active === 2"
            src="@/static/images/my_icon2.png">
          <img
            slot="icon"
            v-if="active !== 2"
            src="@/static/images/my_icon1.png">
        </van-tabbar-item>
      </van-tabbar>
    </div>
  </div>
</template>

<script>

import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import qs from 'qs'
import { Toast } from 'vant';

export default {
  components: {
  },
  async created() {
    
    let token = this.getQueryVariable('userId')
    this.urlGoogsid = this.getQueryVariable('goodsId');
             console.log(this.$store)
            const opts = {  
                apiObj: apiUrl.getUserInfo,
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: qs.stringify()
            }
            
            // e94bb9e93b424bad864180f966e6b7c9
            if(JSON.stringify(this.$store.state.user.userInfo) == "{}" && this.urlGoogsid === false){
              let res = await httpService.accessAPI(opts)
              if(res.code === '0'){
                this.$store.commit('user/setUserInfo', res.data)
                this.$store.commit('user/setid', res.data.job_number);
              }else{
                // Toast(res.msg);
              }
            }


            if(this.urlGoogsid !== false){
              // 表示需要显示商品详情
            this.$store.commit('user/setStatusA', {status: 0, active: 0})
            this.$router.push({
                path: '/detail',
                name: 'Detail',
                query: {
                    goodsId: this.urlGoogsid,
                }
            })
            }
    if(this.$route.path === '/') {
        this.active = 0
      }else if(this.$route.path === '/release') {
        this.active = 1
      }else if(this.$route.path === '/userInfo') {
        this.active = 2
      }
  },
  data() {
    return {
      urlGoogsid:false,
      active: 0,
      icon1: {
        normal: '@/static/images/a.png',
        active: '@/static/images/b.png'
      },
      icon2: {
        normal: '@/static/images/a.png',
        active: '@/static/images/b.png'
      },
      icon3: {
        normal: '@/static/images/a.png',
        active: '@/static/images/b.png'
      }
    }
  },
  methods: {
    getQueryVariable(variable){
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
    }
  },
  watch: {
    '$route'() {

      console.log(this.$route.path)
      if(this.$route.path === '/') {
        this.active = 0
      }else if(this.$route.path === '/release') {
        this.active = 1
      }else if(this.$route.path === '/userInfo') {
        this.active = 2
      }
    }   
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang='less'>
@import '~vux/src/styles/1px.less';
.main-page{
  width: 100%;
  // background-color: #f8f8f8;
  .tabbar-box{
    text-align: center;
    .weui-tabbar{
      position: fixed;
    }
  }
}


</style>
