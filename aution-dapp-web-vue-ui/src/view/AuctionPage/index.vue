<template>
    <div class="auction-group">
        <Swiper loop auto height='227px' :list="demo01_list" v-model="demo01_index" @on-index-change="demo01_onIndexChange"></Swiper>

        <!-- 排序筛选器 -->
        <sticky-slot class="stickyTop">
            <van-dropdown-menu class="my-check-box-all" active-color='#ff7200'>
                <van-dropdown-item v-model="priceSort" :options="priceSortList" />
                <van-dropdown-item v-model="timeSort" :options="timeSortList" />
                <van-dropdown-item v-model="kindSort" :options="kindSortList" />
            </van-dropdown-menu>
        </sticky-slot>

        <!-- 商品列表 -->
        <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
            <van-list
                  :finished="finished"
                  finished-text="没有更多了"
                  @load="onLoad">
                    <div class="commodity-item" v-for="(item, index) in commodityList"
                        @click="goDetailHandler(index)"
                        :key="item.goodsId"
                        :title="item.title">
                        <div class="img-box">
                            <img :src="'http://aution.cclcloud.net/image/'+item.imgs[0]" alt="">
                        </div>
                        <div class="desc-box">
                                <div class="desc-title">{{item.title}}</div>
                                <div class="desc-time">截止时间：{{item.endTime}}</div>
                                <div class="desc-price">起拍价：{{item.startPrice}} STC</div>
                        </div>
                        <div class="quiz-btn">
                            竞拍
                        </div>
                    </div>
            </van-list>
        </van-pull-refresh>

        <img src="../../static/images/topping-icon.png" alt="" class="top-icon" @click='toTop'>

    </div>
</template>

<script>
import { Swiper } from 'vux'
import stickySlot from '@/view/AuctionPage/stickySlot.vue'
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import { util } from '@/utils/util'
import { Toast } from 'vant';

export default {
    name: 'AuctionPage',
    components: {
        Swiper,
        stickySlot
    },
    created() {
        // this.updateList()
        console.log(this.$store)
    },
    data() {
        return {
            /*** TODO
             * 下面是轮播图的图片路径
             */
            demo01_list:[{
                url: '/introduction',
                img: '../../static/img/index_banner.png',
                title: ''
                },
            ],
            demo01_index: 0,

            priceSortList: [{text: "起拍价默认", value: ''},
                    {text: "起拍价从低到高", value: 'ASC'},
                    {text: "起拍价从高到低", value: 'DESC'}],
            priceSort: '',
            timeSortList: [{text: "截止时间默认", value: ''},
                    {text: "截止时间正序", value: 'DESC'},
                    {text: "截止时间倒序", value: 'ASC'}],
            timeSort: '',
            kindSortList: [{text: "标的类别", value: ''},
                    {text: "手机数码", value: 1},
                    {text: "生活电器", value: 2},
                    {text: "护肤/化妆品", value: 3},
                    {text: "影音图书", value: 4},
                    {text: "零食", value: 5},
                    {text: "珠宝首饰", value: 6},
                    {text: "其他", value: 7}],
            kindSort: '',

            commodityList: [],
            loading: false,
            finished: false,
            refreshLoading: false,

            pageSize: 10,
            currentPage: 1,
        }
    },
    methods: {
        demo01_onIndexChange (index) {
            this.demo01_index = index
        },

        updateList() {
            const opts = {
                apiObj: apiUrl.findAllGoods,
                query: {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    priceSort: this.priceSort,
                    timeSort: this.timeSort,
                    type: this.kindSort,
                }
            }
            httpService.accessAPI(opts)
                .then(res => {
                    this.commodityList.splice(0);
                    res.data.forEach((item, index) => {
                        this.commodityList.push(item)
                    })
                    this.commodityList.forEach(item => {
                        item.endTime = util.dateToStr(new Date(item.endTime), 5)
                    })
                    this.commodityList.forEach(item => {
                        item.imgs = item.imgs.split(";");
                        console.log(item.imgs[0]);
                    })

                    console.log(this.refreshLoading);
                    console.log(this.loading);

                    this.refreshLoading = false;
                    // this.loading = false;

                    if(this.commodityList.length <= this.currentPage * this.pageSize) {
                        this.finished = true;
                    }else {
                        this.currentPage ++
                    }
                }, rej => {
                     Toast(res.msg);
                    this.commodityList = []
                    // this.loading = false;
                    this.finished = true;
                })
        },
        onLoad() {
            this.updateList()
        },
        onRefresh() {

            console.log(this.finished);
            console.log(this.refreshLoading);
            this.currentPage = 1
            this.commodityList = []
            this.updateList();
            return;

            this.currentPage = 1
            this.commodityList = []
            const opts = {
                apiObj: apiUrl.findAllGoods,
                query: {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    priceSort: this.priceSort,
                    timeSort: this.timeSort,
                    type: this.kindSort,
                }
            }
            httpService.accessAPI(opts)
                .then(res => {
                    if(res.code !== '0'){
                        Toast(res.msg);
                        return;
                    }

                    this.refreshLoading = false;
                    this.commodityList.splice(0);
                    res.data.forEach((item, index) => {
                        this.commodityList.push(item)
                    })
                    this.commodityList.forEach(item => {
                        item.endTime = util.dateToStr(new Date(item.endTime), 5)
                    })
                    this.commodityList.forEach(item => {
                        item.imgs = item.imgs.split(";");
                        console.log(item.imgs[0]);
                    })
                    if(this.commodityList.length <= this.currentPage * this.pageSize) {
                        this.finished = true;
                    }else {
                        this.currentPage ++
                        this.finished = false
                    }

                })
        },
        goDetailHandler(index) {
            // 判断当前点击是否为自身的拍卖商品，通过id判断
            let whileShowBtn = '';
            if(this.$store.state.user.id === this.commodityList[index].sellerId){
                whileShowBtn = true;
            }else{
                whileShowBtn = false;
            }
            this.$store.commit('user/setStatusA', {status: 0, active: 0})
            this.$store.commit('user/setgoodsStatus', 1)
            this.$router.push({
                path: '/detail',
                name: 'Detail',
                query: {
                    goodsId: this.commodityList[index].goodsId,
                    shwoBtn:whileShowBtn,
                    goodsstatus:1
                }
            })
        },
        toTop() {
            window.scrollTo(0,0);
        }
    },
    watch: {
        priceSort() {
            this.timeSort = ''
            this.toTop()
            this.currentPage = 1
            this.updateList()
        },
        timeSort() {
            this.priceSort = ''
            this.toTop()
            this.currentPage = 1
            this.updateList()
        },
        kindSort() {
            this.toTop()
            this.currentPage = 1
            this.updateList()
        },
    },
}
</script>

<style lang="less">
.auction-group{

    .my-check-box-all{
        margin-top: .4375rem;
        .flex-demo {
        text-align: center;
        background-color: #fff;
        border-radius: 4px;
        background-clip: padding-box;
        height: 1.5625rem;
        position: relative;
        }
        .my-check-box-item{
            width: 100%;
            height: 100%;
            text-align: center;
        }
        .flex-first{
        margin-left: .3125rem
        }
        .flex-lase{
        margin-right: .3125rem
        }
    }

    .stickyTop {
        top: 0;
        z-index: 10;
    }

    .commodity-item{
        height: 7.1875rem;
        background-color: rgb(255,255,255);
        margin-top: .3125rem;
        position: relative;
        box-shadow: 0 1px 2px rgba(0, 0, 0, .12), 0 0 3px rgba(0, 0, 0, .04);
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
                margin-top: .5rem;
                color: rgb(255, 105, 0);
            }
            .desc-time{
                font-size: .75rem;
                margin-top: 1rem;
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
    }
    .top-icon{
        position: fixed;
        bottom: 5.625rem;
        right: 1.25rem;
        width: 1.875rem;
        height: 1.5625rem;
    }
}

</style>
