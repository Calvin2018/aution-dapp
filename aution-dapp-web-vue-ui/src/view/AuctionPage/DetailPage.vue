<template>
    <div class="detail-page">
        <div class="detail-title">
         <!-- /*** TODO 商品详情的图片
          **/-->
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
            {{title}}
        </div>
         <van-swipe :autoplay="3000" @change="onChange">
            <van-swipe-item v-for="(image, index) in images" :key="index" @click="previewHandler(index)">
                <img v-lazy="image" />
            </van-swipe-item>
        </van-swipe>
        <div class="cutoff-time-box">
            <span v-if='callNumber === 1 || callNumber === 4' class="time-icon">截止时间：{{endTime}}</span>
            <!-- <span v-if='status === 1' class="time-icon">已成交,请收到货后评价</span> -->
            <span v-if='callNumber === 6' class="sad-icon">已成交,竞拍失败</span>
            <span v-if='callNumber === 5' class="assess-icon">已成交,待评价</span>
            <span v-if='callNumber === 7' class="assess-icon">已评价</span>
            <span v-if='callNumber === 3' class="sad-icon">拍卖失败</span>
        </div>

        <div class="publish-box">
            <div class="title-box">
                <img class="publish-title" :src="icon"/>
                <span class="head-tip" >拍卖人</span>
                <span class="head-tip-name" :model='tipname'>{{tipname}}</span>
                <span class="head-tip-phone" :model='tellNumber'>{{tellNumber}}</span>
            </div>

            <van-field required input-align='right' :value="articleName" label="拍卖标题" :readonly='true'/>
            <van-field required input-align='right' :value="articlePrice" label="当前出价" class="red-text stc-text" :readonly='true' v-if="status !== 5"/>
            <van-field required input-align='right' :value="articleStartPrice" label="起拍价" class="stc-text" :readonly='true'/>
            <van-field required input-align='right' :value="articleKind" label="物品类别" :readonly='true'/>

            <div class="winner-box" v-if="status === 3 || status === 4">
                <van-field required input-align='right' label="竞拍得主" :readonly='true'/>
                <span class="winner-name">{{biddingwinner}}</span>
                <span class="winner-phone">{{phone}}</span>
            </div>


            <van-field required input-align='right' label="竞价历史" :is-link='true' :readonly='true' @click='goHistory' v-if="status !== 5"/>
            <van-field required input-align='right' label="评价详情" :is-link='true' :readonly='true' @click='showAssessDetail' v-if="callNumber === 7"/>

            <van-field
                required
                label="拍卖详情"
                rows="4"
                disabled="true"
                maxlength='200'
                autosize/>
            <textarea class="textarea1" placeholder='请输入拍卖物品更多信息
比如尺寸，性能等数据（200字以内）' :rows='4' :maxlength='200' :value="descInfo" :readonly='true'></textarea>
        </div>

        <van-button type="primary" size="large" v-if='callNumber === 2' @click="auctionHandler">参与竞拍</van-button>
        <van-button type="primary" size="large" v-if='callNumber === 5' @click='showAssessWrite'>成交评价</van-button>
        <van-button type="primary" size="large" v-if='callNumber === 3' @click="backrelease">重新发布</van-button>

        <van-popup v-model="auctionShow" :close-on-click-overlay='false'>
            <div class="auction-box">
                <div class="auction-title">参与竞拍</div>
                <div class="price-tips">请出价</div>
                <van-field readonly :value="priceValue" placeholder="输入的价格，必须必当前出价高" class="price-input" @click.stop="numberShow = true"/>
                <van-button type="default" class="upload-btn" @click="uploadHandler">提交</van-button>
                <van-button type="default" class="cancel-btn" @click="cancelHandler">取消</van-button>
            </div>
        </van-popup>

        <van-popup v-model="assessDetailShow">
            <div class="assess-box">
                <div class="assess-title" >成交评价</div>
                <div class="assess-tips" >{{goodsContent}}</div>
                <van-rate v-model="starNum" size='30px' gutter='8px' readonly/>
                <van-button type="default" class="assess-btn" @click="assessHandler">确认</van-button>
            </div>
        </van-popup>

        <van-popup v-model="assessWriteShow">
            <div class="assess-write-box">
                <div class="assess-title" >成交评价</div>
                <textarea class="textarea1" placeholder='请输入您的评价，200字以内' :rows='4' :maxlength='200' v-model="assessInfo"></textarea>
                <van-rate v-model="starNumWrite" size='30px' gutter='8px'/>
                <van-button type="default" class="assess-btn" @click="assessWriteHandler">确认</van-button>
            </div>
        </van-popup>

        <van-number-keyboard
            v-model="priceValue"
            :show="numberShow"
            close-button-text="完成"
            :maxlength="6"
            extra-key="."
            @blur="numberShow = false"
        />

    </div>

</template>

<script>
import store from '@/store/index'
import { ImagePreview } from 'vant';
import { Toast } from 'vant';
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import { util } from '@/utils/util'
import qs from 'qs'
import { allianceReq } from '@/view/allpool/deteil/pool.js'

export default {
    data() {
        return {
            phone:'',
            biddingwinner:'',
            goodsContent:'',
            callNumber:0,
            userid:'',
            // 0进入参与竞拍页面
            // 1进入竞拍成功页面
            // 2进入竞拍失败页面
            // 3进入拍卖成功-待评价页面
            // 4进入拍卖成功-已评价页面
            // 5进入拍卖失败页面
            // status:0,
            icon:'',
            tipname:'',
            tellNumber:'',
            /***
             * TODO 轮播图片
             */
            images: [
                // 'https://img.yzcdn.cn/vant/apple-1.jpg',
                // 'https://img.yzcdn.cn/vant/apple-2.jpg',
                // 'https://img.yzcdn.cn/vant/apple-3.jpg',
                // 'https://img.yzcdn.cn/vant/apple-4.jpg',
            ],
            startPosition: 0,
            articleName: '',
            articlePrice: '',
            articleStartPrice: '',
            articleKind: '',
            descInfo: '',
            priceValue: '',
            assessInfo: '',
            endTime: '',

            auctionShow: false,
            assessDetailShow: false,
            assessWriteShow: false,
            starNum: 0,
            starNumWrite: 0,
            numberShow: false,
        }
    },
    created() {
        if(typeof this.$route.query.goodsstatus === 'string'){
            this.$route.query.goodsstatus=parseInt(this.$route.query.goodsstatus);
        }
        if(store.state.user.id === null || store.state.user.id === undefined || store.state.user.id === ''){
            this.$router.push({
                path: '/',
            })
            return ;
        }

        console.log(typeof this.$route.query.goodsstatus);
        // this.$store.commit('user/setgoodsStatus', 1)
        this.getUserInfo();

    },
    computed: {
        title() {
            switch(this.status) {
                case 0:
                    return '竞拍'
                    break
                case 1:
                    return '竞拍成功'
                    break
                case 2:
                    return '竞拍失败'
                    break
                case 3:
                    return '拍卖成功'
                    break
                case 4:
                    return '竞拍成功'
                    break
                case 5:
                    return '拍卖失败'
                    break
            }
        },
        status() {
            if(this.$store.state.user.status === 0) {
                if(this.$store.state.user.active === 0) {
                    return 0
                }else if(this.$store.state.user.active === 1) {
                    return 3
                }else if(this.$store.state.user.active === 2) {
                    return 5
                }
            }

            if(this.$store.state.user.status === 1) {
                if(this.$store.state.user.active === 0) {
                    return 0
                }else if(this.$store.state.user.active === 1) {
                    return 1
                }else if(this.$store.state.user.active === 2) {
                    return 2
                }
            }
        }
    },
    methods: {
        backrelease(){
            this.$router.push({
                path: '/release',
                name: 'Release',
                query: {
                    goodsId: this.$route.query.goodsId,
                }
            })
        },
        async getUserInfo(){
            const opts = {
                apiObj: apiUrl.getUserInfo,
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: qs.stringify({
                    userId: store.state.user.id
                })
            }
            let res = await httpService.accessAPI(opts)

            if(res.code === '0'){
                this.userid = res.data.job_number;
                this.getDetial();
            }
        },
        getContent(){
            const opts = {
                apiObj: apiUrl.findGoodsByGid,
                query: {
                    page: 0,
                    size: 1,
                    gId: this.$route.query.goodsId,
                }
            }
            httpService.accessAPI(opts)
                .then(res => {
                    if(res.code !== '0'){
                    Toast(res.msg);
                    return;
                    }
                    this.goodsContent = res.data.content;
                    this.starNum = parseInt(res.data.temp);
                    this.callNumber = 7;
                })
        },
        getDetial(){
        const opts = {
                apiObj: apiUrl.findGoodsByGid,
                query: {
                    page: 0,
                    size: 1,
                    gId: this.$route.query.goodsId,
                }
            }
        httpService.accessAPI(opts)
            .then(res => {
                if(res.code !== '0'){
                    Toast(res.msg);
                    return;
                }
                this.biddingwinner = res.data.buyName;//竞拍得主
                this.phone = res.data.buyPhone;//拍卖得主电话
                // 先获取当前的评论值
                this.goodsContent = res.data.content;
                this.starNum = parseInt(res.data.temp);
                console.log(this.goodsContent);
                console.log(this.starNum);
                console.log(this.goodsContent === null);
                console.log(this.starNum === '-1');
                if(this.$route.query.goodsstatus !== null || this.$route.query.goodsstatus !== undefined){
                    if(this.$route.query.goodsstatus === 1 || this.$route.query.goodsstatus === '1'){
                    // 表示正在拍卖中
                        if( this.userid === res.data.sellerId){
                            this.callNumber = 1 ;
                        }else{
                            this.callNumber = 2 ;//表示成功
                        }
                    }
                    if(this.$route.query.goodsstatus === 2 || this.$route.query.goodsstatus === '2'){
                        // 表示拍卖成功
                        //表示成功
                        if( this.goodsContent === null && this.starNum === -1  ){
                            this.callNumber = 4;
                        }else{
                            this.callNumber = 7 ;//竞拍成功
                        }
                    }
                    if( this.$route.query.goodsstatus === 3 || this.$route.query.goodsstatus === '3'){
                        // 表示流拍
                        this.callNumber = 3;//表示流拍重新发布
                    }
                    if( this.$route.query.goodsstatus === 4 || this.$route.query.goodsstatus === '4'){
                        // 表示竞拍中
                        this.callNumber = 2;//表示竞拍中
                    }
                    if(this.$route.query.goodsstatus === 5 || this.$route.query.goodsstatus === '5'){
                        // 竞拍成功

                        if( this.goodsContent === null && this.starNum === -1 ){
                            this.callNumber = 5
                        }else{
                            this.callNumber = 7 ;//竞拍成功
                        }

                        // this.assessWriteShow = true;
                    }
                    if(this.$route.query.goodsstatus === 6 || this.$route.query.goodsstatus === '5'){
                        // 竞拍失败
                        this.callNumber = 6 ;//表示成功
                    }
                }


                this.endTime = util.dateToStr(new Date(res.data.endTime), 5)

                this.articleName = res.data.title
                this.articlePrice = res.data.currentPrice === null?0 :res.data.currentPrice;
                this.articleStartPrice = res.data.startPrice
                this.descInfo = res.data.details || ''
                this.tipname = res.data.userName;
                this.tellNumber = res.data.userPhone;
                this.icon = res.data.avatar;
                this.images.splice(0);
                this.images = res.data.imgs.split(";");
                this.images.splice(this.images.length-1,1);

                for(let i=0;i<this.images.length;i++){
                    this.images[i]='http://aution.cclcloud.net/image/'+this.images[i];
                }
                        // console.log(item.imgs[0]);

                console.log(res.data.imgs);
                console.log(this.images);
                switch (res.data.type) {
                    case 1:
                        this.articleKind = '手机数码'
                        break
                    case 2:
                        this.articleKind = '生活电器'
                        break
                    case 3:
                        this.articleKind = '护肤/化妆品'
                        break
                    case 4:
                        this.articleKind = '影音图书'
                        break
                    case 5:
                        this.articleKind = '零食'
                        break
                    case 6:
                        this.articleKind = '珠宝首饰'
                        break
                    case 7:
                        this.articleKind = '其他'
                        break
                }
            })
        },
        previewHandler(n) {
            ImagePreview({
                images: this.images,
                startPosition: n,
                onClose() {
                }
            })
        },

        onChange(index) {
            this.startPosition = index
        },
        backHandler() {
            this.$router.go(-1)
        },
        goHistory() {
            this.$router.push({
                path: '/history',
                name: 'History',
                query: {
                    goodsId: this.$route.query.goodsId
                }
            })
        },
        showAssessDetail() {
            this.assessDetailShow = true;

        },
        showAssessWrite() {
            this.assessWriteShow = true;
        },
        auctionHandler() {
            this.auctionShow = true;
        },
        uploadHandler() {
            this.auctionShow = false;
            // 提交竞拍价格接口
            // this.postPrice();
            this.postPrice();

        },
        /***
         * TODO 参与竞猜的点击事件
         * @returns {Promise<void>}
         */
        async postPrice(){
            const opts = {
                apiObj: apiUrl.postPrice,
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: qs.stringify({
                    gId:this.$route.query.goodsId,
                    userId:this.$store.state.user.id,
                    price:this.priceValue
                })
            }
            let res = await httpService.accessAPI(opts)
            if(res.code === '0' ){
                if(res.data.flag === true){
                    window.location.href=res.data.pay_url;
                }else{
                    Toast.fail('服务异常，请联系管理员');
                }
            }else{
                if(res.code === '100003'){
                    Toast.fail('出价不能低于起拍价或竞拍价');
                    /**
                     * TODO 这里是当竞拍价变高的时候，需要再调用一次接口
                     */

                    this.getDetial()

                }else{
                    Toast.fail(res.msg);
                }
            }
            this.priceValue = ''
            console.log(res);
         },
        cancelHandler() {
            this.priceValue = ''
            this.auctionShow = false;
        },
        assessHandler() {
            this.assessDetailShow = false;
        },
        assessWriteHandler() {

            this.assessWriteShow = false;
            console.log(this.starNumWrite);
            console.log(this.assessInfo);
            this.addGoodsContent();
        },
        async addGoodsContent(){
            const opts = {
                apiObj: apiUrl.addGoodsContent,
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: qs.stringify({
                    goodsId:this.$route.query.goodsId,
                    temp:this.starNumWrite,
                    content:this.assessInfo
                })
            }
            let res = await httpService.accessAPI(opts)
            if(res.code === '0' ){
                if(res.data === true){
                    // this.getDetial()
                    this.getContent();

                }

            }else{
                if(res.code === '100003'){
                    Toast.fail('出价不能低于起拍价或竞拍价');
                }else{
                    Toast.fail(res.msg);
                }
            }
            console.log(res);
         },
    }
}
</script>

<style lang="less">
    .detail-page {
        .van-number-keyboard{
            z-index: 99999999!important;
        }
        .detail-title{
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
        .van-swipe {
            height: 7.8125rem;
            background: #fff;
            img {
                height: 100%;
                margin-left: 50%;
                transform: translateX(-50%);

            }
        }
        .cutoff-time-box{
            height: 1.875rem;
            background: #ffc799;
            span{
                line-height: 1.875rem;
                margin-left: 2.625rem;
                font-size: .875rem;
                position: relative;
            }
            span::after {
                content: '';
                width: .75rem;
                height: .75rem;
                position: absolute;
                left: -15px;
                top: 50%;
                transform: translateY(-55%);
            }
            .time-icon::after {
                background: url('../../static/images/clock-icon.png');
                background-size: cover;
            }
            .sad-icon::after {
                background: url('../../static/images/sad-icon.png');
                background-size: cover;
            }
            .assess-icon::after {
                background: url('../../static/images/assess-icon.png');
                background-size: cover;
            }
        }
        .publish-box{
            width: 20.1875rem;
            margin-left: 1.625rem;
            margin-top: 2.0625rem;
            padding-bottom: 0.25rem;
            background: #fff;
            border-radius: .9375rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
            .winner-box{
                position: relative;
                .winner-name {
                    position: absolute;
                    top: 0;
                    right: .9375rem;
                    font-size: .875rem;
                    font-weight: 550;
                    color: #ff6900;
                }
                .winner-phone{
                    position: absolute;
                    top: 1.25rem;
                    right: .9375rem;
                    font-size: .8125rem;
                    color: #ff6900;
                }
            }
            .red-text{
                .van-field__control--right{
                    color: #ff6900;
                    font-weight: 500;
                }
            }
            .stc-text {
                .van-field__control--right{
                    position: relative;
                    padding-right: 1.55rem;
                }
                .van-field__body{
                    position: relative;
                }
                .van-field__body::after{
                    content: 'STC';
                    position: absolute;
                    top: 0;
                    right: 0;
                    font-size: .625rem;
                    color: #323233;
                }
            }

            .title-box{
                text-align: center;
                height: 3.3125rem;
                position: relative;
                .head-tip{
                    position: absolute;
                    left: 1rem;
                    top: .625rem;
                    font-size: .875rem;
                    color: #323233;
                }
                .head-tip-name {
                    width:5.25rem;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    position: absolute;
                    right: 1rem;
                    top: .625rem;
                    font-size: .875rem;
                    color: #323233;
                }
                .head-tip-phone {
                    position: absolute;
                    right: 1rem;
                    top: 1.825rem;
                    font-size: .75rem;
                    color: #898888;
                }
            }
            .publish-title{
                width: 3.5rem;
                height: 3.5rem;
                box-shadow: 0 3px 5px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
                border-radius: 50%;
                margin-top: -1.75rem;
            }
            .textarea1{
                box-sizing: border-box;
                width: 96%;
                height: 5.25rem;
                margin-left: 2%;
                background: rgb(250, 250, 250);
                border-radius: .3125rem;
                border: 1px rgb(232, 232, 232) solid;
                font-size: .875rem;
            }
            .img-content-box{
                width: 96%;
                margin-left: 2%;
                .van-uploader {
                    width: 100%;
                }
                .van-uploader__preview{
                    margin: 0 0.625rem 0.625rem 0;
                }
            }
            .van-field{
                padding: .43875rem 0.9375rem;
            }
        }
        .van-button--large{
            width: 20.1875rem;
            margin-left: 1.625rem;
            margin-top: 0.7rem;
            height: 2.3rem;
            line-height: 2.3rem;
        }
        .van-button--primary {
            color: #fff;
            background-color: #ff6900;
            border: 1px solid #ff6900;
        }
        .van-popup--center{
            border-radius: 1.25rem;
        }
        .auction-box{
            width: 18.4375rem;
            height: 14.0625rem;
            position: relative;
            .auction-title{
                height: 3rem;
                text-align: center;
                line-height: 3rem;
                background: linear-gradient(#f64507, #ff7516);
                color: #fff;
                font-weight: 550;
            }
            .price-tips{
                position: absolute;
                left: 1.75rem;
                top: 4.4375rem;
                color: #6a6a6a;
            }
            .price-input{
                position: absolute;
                left: 1.75rem;
                top: 6.4375rem;
                color: #6a6a6a;
                background: #fafafa;
                border: #e8e8e8 solid .0625rem;
                border-radius: .625rem;
                width: 14.9375rem;
            }
            .upload-btn{
                position: absolute;
                left: 1.75rem;
                top: 10.125rem;
                width: 6rem;
                height: 2.6875rem;
                border-radius: .625rem;
                background-color: #ff7100;
                color: #fff;
                font-size: 1rem
            }
            .cancel-btn{
                position: absolute;
                left: 10.6875rem;
                top: 10.125rem;
                width: 6rem;
                height: 2.6875rem;
                border-radius: .625rem;
                background-color: #d9d9d9;
                font-size: 1rem
            }
        }
        .assess-box{
            width: 18.4375rem;
            height: 21.125rem;
            position: relative;
            background: url('../../static/images/assess-detail-bg.png');
            background-size: cover;
            .assess-title{
                height: 6rem;
                text-align: center;
                line-height: 6rem;
                font-size: 1.5rem;
                color: #fff;
                font-weight: 550;
            }
            .assess-tips{
                text-align: center;
                padding-left: .625rem;
                padding-right: .625rem;
                color: #6a6a6a;
                margin-top: 3rem;
                font-size: .875rem
            }
            .van-rate{
                margin-top: 1.25rem;
                text-align: center;
            }
            .assess-btn{
                margin-top: 1.25rem;
                width: 16.5625rem;
                height: 3rem;
                border-radius: .625rem;
                background-color: #ff7100;
                color: #fff;
                font-size: 1rem;
                margin-left: 50%;
                transform: translate(-50%)
            }
        }
        .assess-write-box{
            width: 18.4375rem;
            height: 21.125rem;
            position: relative;
            background: url('../../static/images/assess-detail-bg.png');
            background-size: cover;
            .assess-title{
                height: 6rem;
                text-align: center;
                line-height: 6rem;
                font-size: 1.5rem;
                color: #fff;
                font-weight: 550;
            }
            .assess-tips{
                width: 100%;
                color: #6a6a6a;
                margin-top: 1.5rem;
                font-size: .875rem
            }
            .van-rate{
                margin-top: 1rem;
                text-align: center;
            }
            .assess-btn{
                margin-top: 1rem;
                width: 16.5625rem;
                height: 3rem;
                border-radius: .625rem;
                background-color: #ff7100;
                color: #fff;
                font-size: 1rem;
                margin-left: 50%;
                transform: translate(-50%)
            }
            .textarea1{
                margin-top: 1.5rem;
                box-sizing: border-box;
                width: 96%;
                height: 5.25rem;
                margin-left: 2%;
                background: rgb(250, 250, 250);
                border-radius: .3125rem;
                border: 1px rgb(232, 232, 232) solid;
                font-size: .875rem;
            }
        }
    }
</style>
