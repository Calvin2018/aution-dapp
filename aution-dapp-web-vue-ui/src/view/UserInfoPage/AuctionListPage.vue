<template>
    <div class="list-page">
        <div class="list-title">
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
            {{title}}
        </div>
        <van-tabs v-model="active" animated>
            <van-tab v-for="index in 3" :title="status === 0? barList1[index - 1]:barList2[index - 1]" :key="index">
                <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
                    <van-list
                        v-model="loading"
                        :finished="finished"
                        finished-text="没有更多了"
                        @load="onLoad">
                            <div class="commodity-item" v-for="(item, index) in commodityList"
                                @click="goDetailHandler(index)"
                                :key="item.goodsId"
                                :title="item.goodsId">
                                <div class="img-box">
                                  <!--测试环境-->
                                    <img :src="'http://10.250.218.104:8089/image/'+item.imgs[0]" alt="">
                                  <!--正式环境-->
<!--                                    <img :src="'http://aution.cclcloud.net/image/'+item.imgs[0]" alt="">-->
                                </div>
                                <div class="desc-box">
                                        <div class="desc-title">{{item.title}}</div>
                                        <div class="desc-time" v-if="!(active === 1 || (status === 1 && active === 2))">截止时间：{{item.endTime}}</div>
                                        <div class="desc-time" v-if="active === 1 || (status === 1 && active === 2)">成交时间：{{item.endTime}}</div>
                                        <div class="desc-price" v-if="active === 0">当前价格：{{item.currentPrice ===null ? 0:item.currentPrice}} STC</div>
                                        <div class="desc-price" v-if="active === 1 || (status === 1 && active === 2)">成交价：{{item.currentPrice}} STC</div>
                                        <div class="desc-price" v-if="status === 0 && active === 2">起拍价：{{item.startPrice}} STC</div>
                                </div>
                                <div class="quiz-btn">
                                </div>
                            </div>
                    </van-list>
                </van-pull-refresh>
            </van-tab>
        </van-tabs>
    </div>
</template>

<script>
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import { util } from '@/utils/util'
import { Toast } from 'vant';

export default {
    created() {
        this.number =  this.$store.state.user.status ;
    //    if(this.number >= 4){
    //     //    表示竞拍

    //    }else{
    //     //    表示拍卖

    //    }
    },
    data() {
        return{
            goodsstatus:0,
            number:0,
            barList1: ['进行中拍卖','拍卖成功','拍卖失败'],
            barList2: ['进行中竞拍','竞拍成功','竞拍失败'],
            status: this.$store.state.user.status,
            active: this.$store.state.user.active,
            finished: false,
            refreshLoading: false,
            loading: false,
            commodityList: [],
            // activeLoad:false,
            pageSize: 10,
            currentPage: 0,
            loadingArr:[false,false,false]
        }
    },
    computed: {
        title() {
            if(this.number == 0) {
                return '我的拍卖'
            }else {
                return '我的竞拍'
            }
        }
    },
    methods: {
        onLoad() {

            this.updateList();
            console.log("activeLoad-----"+this.$store.state.user.activeLoad)
             if(this.$store.state.user.activeLoad ===0){
                this.currentPage ++
             }

        },
        onRefresh(index) {

            this.currentPage = 0;
            this.finished =true
            this.commodityList = []
            // this.loading =true;
            this.updateList(true)

        },
        backHandler() {
            this.$router.go(-1)
        },
        goDetailHandler(index) {
            this.goodsstatus = this.commodityList[index].status
            this.$router.push({
                path: '/detail',
                name: 'Detail',
                query: {
                    goodsId: this.commodityList[index].goodsId,
                    shwoBtn:true,
                    goodsstatus:this.goodsstatus
                }
            })
        },
        updateList(fBopol) {
            // this.activeLoad=false;

            let opts;
            if(this.status === 0) {
                if(this.active === 0) {
                    opts = {
                        apiObj: apiUrl.findGoodsBySellerIdAndStatus,
                        query: {
                            sellerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 1
                        }
                    }
                }else if(this.active === 1) {
                    opts = {
                        apiObj: apiUrl.findGoodsBySellerIdAndStatus,
                        query: {
                            sellerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 2
                        }
                    }
                }else if(this.active === 2) {
                    opts = {
                        apiObj: apiUrl.findGoodsBySellerIdAndStatus,
                        query: {
                            sellerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 3
                        }
                    }
                }
            }else if(this.status === 1) {
                if(this.active === 0) {
                    opts = {
                        apiObj: apiUrl.findGoodsByBuyerIdAndStatus,
                        query: {
                            buyerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 1
                        }
                    }
                }else if(this.active === 1) {
                    opts = {
                        apiObj: apiUrl.findGoodsByBuyerIdAndStatus,
                        query: {
                            buyerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 2
                        }
                    }
                }else if(this.active === 2) {
                    opts = {
                        apiObj: apiUrl.findGoodsByBuyerIdAndStatus,
                        query: {
                            buyerId: this.$store.state.user.id,
                            size: this.pageSize,
                            page: this.currentPage,
                            status: 3
                        }
                    }
                }
            }

            httpService.accessAPI(opts)
                .then(res => {
                    if(res.code !== '0'){
                        Toast(res.msg);
                        return ;
                    }

                    this.loading = false;
                    this.refreshLoading = false;
                    if(fBopol) {
                        this.finished = false;
                    }
                    if(res.data.length === 0) {
                        this.finished = true
                        return
                    }


                    console.log(this.loading);
                    // this.commodityList.splice(0);
                    if(res.data.length < this.pageSize) {
                        this.finished = true
                    }
                    console.log("currentPage:---"+this.currentPage);
                    if(this.$store.state.user.activeLoad ===1){
                        this.currentPage = 1
                    }
                    if(this.currentPage === 1) {
                        this.commodityList = res.data
                    }else {
                        this.commodityList = this.commodityList.concat(res.data)
                    }
                    // this.commodityList = res.data;
                    this.commodityList.forEach(item => {
                        item.endTime = util.dateToStr(new Date(item.endTime), 5)
                    })
                    this.commodityList.forEach(item => {
                        if(!Array.isArray(item.imgs)){
                            item.imgs = item.imgs.split(";");
                            console.log(item.imgs[0]);
                        }
                        // item.imgs = item.imgs.split(";");
                        // console.log(item.imgs[0]);
                    })
                    this.$store.commit('user/setActiveLoad', 0);
                    // this.loading = false;
                    // this.refreshLoading = false;
                    //
                    // // this.loadingArr[index-1] =false;/
                    // if(this.commodityList.length <= this.currentPage * this.pageSize) {
                    //     this.finished = true;
                    // }else {
                    //     this.currentPage ++
                    // }
                }, rej => {
                    this.commodityList = []
                    this.loading = false;
                    this.finished = true;
                })
        }
    },
    watch: {
        active() {

            // this.activeLoad=true;
            this.$store.commit('user/setStatusA', {status: this.status, active: this.active});
            this.$store.commit('user/setActiveLoad', 1);
            this.commodityList=[];
            this.currentPage = 0
            this.updateList()
            this.finished = false
            this.loading = false
        }
    }
}
</script>

<style lang="less">
    .list-page{
        .list-title{
            height: 2.8125rem;
            background: linear-gradient(#f64507, #ff7516);
            line-height: 3.225rem;
            font-size: 1.125rem;
            font-weight: 500;
            color: #fff;
            text-align: center;
            .back-btn {
                position: absolute;
                top: .75rem;
                left: .5625rem;
                width: .875rem;
                height: 1.5625rem;
            }
        }
        .commodity-item{
            height: 7.1875rem;
            background-color: rgb(255,255,255);
            margin-top: .3125rem;
            position: relative;
            .img-box{
            position: relative;
            width: 7.5rem;
            height: 100%;
            display: inline-block;
            img{
                width: 5.9375rem;
                height: 5.9375rem;
                margin-left: .9375rem;
                margin-top: .375rem;
                margin-bottom: 1rem;
                border-radius: 10px;
            }
            }
            .desc-box{
                position: absolute;
                top: 0px;
                left: 7.5rem;
                width: 15.9375rem;
                display: inline-block;
                height: 100%;
                margin-left: .1875rem;
                .desc-title{
                    font-size: .875rem;
                    margin-top: .8125rem;
                }
                .desc-price{
                    font-size: .875rem;
                    margin-top: .25rem;
                    color: rgb(255, 105, 0);
                }
                .desc-time{
                    font-size: .75rem;
                    margin-top: 1.25rem;
                    color: rgb(157, 157, 157);
                }
            }
            .quiz-btn{
                width: .875rem;
                height: 1.5625rem;
                background: url('../../static/images/arrow-R.png');
                background-size: cover;
                position: absolute;
                top: 3.075rem;
                right: 1.5625rem;
            }
        }
        // .van-ellipsis::before{
        //         content: '';
        //         height: .3125rem;
        //         width: .3125rem;
        //         border-radius: 50%;
        //         background: red;
        //         position: absolute;
        //         top: 50%;
        //         left: 1.2rem;
        //         transform: translateY(-50%);
        // }
    }
</style>
