<template>
    <div class="history-page">
        <div class="history-title">
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
            竞拍历史
        </div>

        <div class="title-box">
            <van-grid :column-num="7" :border="false">
                <van-grid-item text=" "/>
                <van-grid-item text="买主" :center='false'/>
                <van-grid-item text=" "/>
                <van-grid-item text="出价"/>
                <van-grid-item text=" "/>
                <van-grid-item text="时间"/>
                <van-grid-item text=" "/>
            </van-grid>
        </div>

        <!-- 商品列表 -->
        <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
            <van-list v-model="loading"
                  :finished="finished"
                  finished-text="没有更多了"
                  @load="onLoad">
                    <div class="commodity-item" v-for="item in historyList"
                        :key="item"
                        :title="item">
                        <div class="head-img-box">
                            <img :src="item.avatar" alt="">
                        </div>
                        <div class="name">{{item.userName}}</div>
                        <div class="price">{{item.bidPrice}} <span>STC</span></div>
                        <div class="time1">{{item.bidTime[0]}}</div>
                        <div class="time2">{{item.bidTime[1]}}</div>
                    </div>
            </van-list>
        </van-pull-refresh>
         
    </div>
   
</template>

<script>
import { ImagePreview, Toast } from 'vant';
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import { util } from '@/utils/util'
import qs from 'qs'


export default {
    data() {
        return {
            images: [
                'https://img.yzcdn.cn/vant/apple-1.jpg',
                'https://img.yzcdn.cn/vant/apple-2.jpg',
                'https://img.yzcdn.cn/vant/apple-3.jpg',
                'https://img.yzcdn.cn/vant/apple-4.jpg',
            ],
            historyList: [],
            loading: false,
            finished: false,
            refreshLoading: false,
            pageSize: 10,
            currentPage: 1,
        }
    },
    created() {
    },
    methods: {
        backHandler() {
            this.$router.go(-1)
        },
        updateList() {
            const opts = {  
                apiObj: apiUrl.findHistoryByGoodsIdAndTimeSort,
                query: {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    gId: this.$route.query.goodsId,
                    sort: 'DESC',
                }
            }
            httpService.accessAPI(opts)
                .then(res => {
                        if(res.code !== '0'){
                            Toast(res.msg);
                            return;
                        }
                        
                        this.historyList = res.data
                        this.loading = false;
                        this.historyList.forEach(item => {
                            item.bidTime = util.dateToStr(new Date(item.bidTime), 5).split(' ');
                            console.log(item.bidTime);
                        })
                        // this.historyList.forEach(item => {
                        //     item.imgs = item.imgs.split(";");
                        //     console.log(item.imgs[0]);
                        // })
                        console.log(this.historyList);
                        if(this.historyList.length <= this.currentPage * this.pageSize) {
                            this.finished = true;
                        }else {
                            this.currentPage ++
                        }
                    })
        },
        onLoad() {
            this.updateList()
        },
        onRefresh() {
            this.currentPage === 1
            this.updateList()
        },
    }
}
</script>

<style lang="less">
    .history-page {
        .history-title{
            height: 2.8125rem;
            background: linear-gradient(#f64507, #ff7516);
            line-height: 3.1125rem;
            font-size: 1.125rem;
            font-weight: 500;
            color: #fff;
            text-align: center;
            background-size: cover;
            .back-btn {
                position: absolute;
                top: .75rem;
                left: .5625rem;
                width: .875rem;
                height: 1.5625rem;
            }
        }
        .title-box{
            .van-grid-item__text{
                font-size: 1rem;
            }
        }
        .commodity-item{
            height: 3.875rem;
            width: 22.3125rem;
            margin-left: 50%;
            transform: translate(-50%);
            background-color: rgb(255,255,255);
            margin-top: .9375rem;
            position: relative;
            box-shadow: 0 1px 2px rgba(0, 0, 0, .12), 0 0 3px rgba(0, 0, 0, .04);
            border-radius: 3.875rem 1rem 1rem 3.875rem;
            .head-img-box{
                position: relative;
                width: 3.875rem;
                height: 100%;
                display: inline-block;
                img{
                    width: 3.875rem;
                    height: 3.875rem;
                    margin-bottom: 1rem;
                    border-radius: 50%;

                }
            }
            .name {
                width:5.25rem;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                position: absolute;
                left: 4.325rem;
                top: 0;
                line-height: 3.875rem;
                font-size: .875rem;
            }
            .price{
                position: absolute;
                top: 0;
                line-height: 3.875rem;
                width: 6.875rem;
                left: 50%;
                transform: translate(-50%);
                text-align: center;
                color: #ff6900;
                font-size: .875rem;
                font-weight: 550;
                span{
                    font-size: .625rem;
                    font-weight: 500;
                }
            }
            .time1{
                position: absolute;
                top: 0.925rem;
                width: 6.875rem;
                right: 0;
                text-align: center;
                font-size: .875rem;
            }
            .time2{
                position: absolute;
                top: 1.8875rem;
                width: 6.875rem;
                right: 0;                
                text-align: center;
                font-size: .75rem;
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
                width: 4.5rem;
                height: 2rem;
                background-color: rgb(255, 114, 0);
                border-radius: 1.25rem;
                position: absolute;
                top: 3.875rem;
                right: 1.0625rem;
                text-align: center;
                line-height: 2rem;
                font-size: 1rem;
                color: #fff;
            }
            .top-icon{
                width: 1.875rem;
                height: 1.875rem;
                position: absolute;
                top: .5625rem;
                right: .9375rem;
            }
        }
    }
</style>