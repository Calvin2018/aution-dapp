<template>
    <div class="user-page">
        <div class="top-bg">
            <span>个人中心</span>
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
            <div class="head-img">
                <img :src="this.$store.state.user.userInfo.avatar" alt="">
            </div>
            <span class="user-name">{{this.$store.state.user.userInfo.user_name}}</span>
            <span class="user-phone">{{this.$store.state.user.userInfo.user_phone}}</span>
            <div class="my-account">我的账户</div>
<!--            <div class="my-icon">{{price}}</div>-->
<!--            <div class="my-icon-r">stc</div>-->
        </div>
        <div class="grid-box">
            <van-grid clickable :column-num="3">
                <van-grid-item to="/auctionList" @click='saveStatusHandler(0)'>
                    <img src="@/static/images/auction-icon.png" alt="">
                    <img class="pending-icon" src="@/static/images/pending-icon.png" alt="">
                    <span>拍卖</span>
                </van-grid-item>
                <van-grid-item to="/auctionList" @click='saveStatusHandler(1)'>
                    <img src="@/static/images/auction-suc-icon.png" alt="">
                    <span :class="redList.some(item => item === '1')? 'smill-red' : ''">拍卖成功</span>
                </van-grid-item>
                <van-grid-item to="/auctionList" @click='saveStatusHandler(2)'>
                    <img src="@/static/images/auction-def-icon.png" alt="">
                    <span :class="redList.some(item => item === '2')? 'smill-red' : ''">拍卖失败</span>
                </van-grid-item>
            </van-grid>
        </div>

        <div class="grid-box">
            <van-grid clickable :column-num="3">
                <van-grid-item to="/auctionList" @click='saveStatusHandler(3)'>
                    <img src="@/static/images/buy-icon.png" alt="">
                    <img class="pending-icon" src="@/static/images/pending-icon.png" alt="">
                    <span>竞拍</span>
                </van-grid-item>
                <van-grid-item to="/auctionList" @click='saveStatusHandler(4)'>
                    <img src="@/static/images/buy-suc-icon.png" alt="">
                    <span :class="redList.some(item => item === '3')? 'smill-red' : ''">竞拍成功</span>
                </van-grid-item>
                <van-grid-item to="/auctionList" @click='saveStatusHandler(5)'>
                    <img src="@/static/images/buy-def-icon.png" alt="">
                    <span :class="redList.some(item => item === '4')? 'smill-red' : ''">竞拍失败</span>
                </van-grid-item>
            </van-grid>
        </div>

    </div>
</template>

<script>
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import { util } from '@/utils/util'
import { Toast } from 'vant';
export default {
    created() {
        const opts = {
                apiObj: apiUrl.getNewMessage,
                query: {
                    userId: this.$store.state.user.id,
                }
            }
            httpService.accessAPI(opts)

                .then(res => {
                    if(res.code === '0'){
                        this.redList=[];
                        this.redList = res.data.msg
                        // this.price=res.data.balance
                    }else{
                        Toast(res.msg);
                    }

                })
    },
    data() {
        return {
            redList: [],
            price:''
        }
    },
    methods: {
        saveStatusHandler(n) {

            switch(n) {
                case 0:
                    this.$store.commit('user/setStatusA', {status: 0, active: 0})
                    break
                case 1:
                    this.$store.commit('user/setStatusA', {status: 0, active: 1})
                    break
                case 2:
                    this.$store.commit('user/setStatusA', {status: 0, active: 2})
                    break
                case 3:
                    this.$store.commit('user/setStatusA', {status: 1, active: 0})
                    break
                case 4:
                    this.$store.commit('user/setStatusA', {status: 1, active: 1})
                    break
                case 5:
                    this.$store.commit('user/setStatusA', {status: 1, active: 2})
                    break
            }
        },
        backHandler() {
            this.$router.go(-1)
        }
    }
}
</script>

<style lang="less">
    .user-page{
        .top-bg{
            height: 8rem;
            text-align: center;
            background: linear-gradient(#f64507, #ff7516);
            line-height: 3rem;
            font-size: 20px;
            span{
                color: #fff;
            }
            .back-btn {
                position: absolute;
                top: .75rem;
                left: .5625rem;
                width: .875rem;
                height: 1.5625rem;
            }
            .head-img{
                width: 3.5rem;
                height: 3.5rem;
                margin-left: 2rem;
                border-radius: 50%;
                overflow: hidden;
                img {
                    width: 100%;
                    height: 100%;
                }
            }
            .user-name{
                position: absolute;
                top: 4.05rem;
                left: 6.25rem;
                font-size: 1rem;
                line-height: 1rem;
            }
            .user-phone{
                position: absolute;
                top: 5.5375rem;
                left: 6.25rem;
                font-size: .875rem;
                line-height: .875rem;
            }
            .my-account{
                width: 5.875rem;
                height: 1.925rem;
                position: absolute;
                top: 2.8125rem;
                background: url('../../static/images/my-account.png') no-repeat;
                background-size: cover;
                right: 0;
                font-size: .875rem;
                line-height: 1.925rem;
            }
            .my-icon{
                position: absolute;
                top: 4.4875rem;
                right: 3.575rem;
                font-weight: 600;
                color: #fff;
            }
            .my-icon-r{
                position: absolute;
                top: 4.5775rem;
                right: 2.375rem;
                font-weight: 600;
                color: #fff;
                font-size: .8125rem;
            }
        }
        .grid-box{
            width: 20.3125rem;
            height: 6.375rem;
            background-color: #fff;
            margin-left: 1.5625rem;
            margin-top: .625rem;
            position: relative;
            border-radius: .625rem;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
            img {
                width: 2.4375rem;
                height: 2.4375rem;
                margin-bottom: .625rem;
            }
            .pending-icon{
                width: 1rem;
                height: 2.5rem;
                position: absolute;
                top: 0;
                left: .9375rem;
            }
            .smill-red{
                position: relative;
            }
            .smill-red::before{
                content: '';
                height: .3125rem;
                width: .3125rem;
                border-radius: 50%;
                background: red;
                position: absolute;
                top: 50%;
                left: -0.5rem;
                transform: translateY(-50%);
            }
        }
    }
</style>
