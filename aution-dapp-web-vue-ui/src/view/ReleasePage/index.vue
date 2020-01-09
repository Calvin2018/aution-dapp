<template>
    <div class="publish-page">
        <div class="top-bg">
            <span>发布拍卖</span>
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
        </div>
        <div class="publish-box">
            <div class="title-box">
                <img class="publish-title" src="@/static/images/publish-title.png"/>
            </div>

            <van-field required input-align='right' v-model="articleName" label="拍卖标题" placeholder="请输入您要拍卖的物品" @input="titleInput"/>

            <van-field required input-align='right' readonly clickable label="物品类别" :value="articleKindText" placeholder="请选择" @click="showKindPicker = true"/>
            <van-popup v-model="showKindPicker" position="bottom">
                <van-picker show-toolbar :columns="kindList" @cancel="showKindPicker = false" @confirm="onConfirm" />
            </van-popup>

            <van-field 
                readonly 
                required 
                clickable 
                input-align='right' 
                :value="startingPrice" 
                label="起拍价" 
                placeholder="请输入起拍价（Scion）"
                @click="shouNumberHandler"
                />

            <van-field required input-align='right' readonly clickable label="拍卖截止时间" :value="overTime" placeholder="请选择" @click="showTimePicker = true"/>
            <van-popup v-model="showTimePicker" position="bottom">
                <van-datetime-picker
                    v-model="currentDate"
                    type="datetime"
                    :min-date="minDate"
                    :max-date="maxDate"
                    @confirm='verificationHandler'
                    @cancel="showTimePicker = false"/>
            </van-popup>

            <van-field
                required
                label="详细信息"
                rows="4"
                disabled="true"
                maxlength='200'
                autosize/>
            <textarea class="textarea" placeholder='请输入拍卖物品更多信息
比如尺寸，性能等数据（200字以内）' :rows='4' :maxlength='200' v-model="descInfo" @input="textareInput"></textarea>


            <van-field required readonly clickable label="添加图片"/>

            <span style="position: absolute; top: 26.2875rem; left: 6.125rem; font-size: 0.625rem; color: rgb(162, 162,162)">(最多五张)</span>
            <div class="img-content-box">
                <van-uploader
                    v-model="fileList"
                    multiple
                    :max-count="5"/>
            </div>


            <van-number-keyboard
                v-model="startingPrice"
                :show="numberShow"
                close-button-text="完成"
                :maxlength="6"
                @blur="numberShow = false"
            />
        </div>

        <van-button type="primary" size="large" @click="showSuccess" v-if="isSubmit === false">确定</van-button>
        <van-button loading  size="large" type="primary" loading-type="spinner"  v-if="isSubmit === true"/>

        <van-popup v-model="publishShow">
            <div class="publish-success-box">
                <img src="@/static/images/close-icon.png" alt="" @click="closeSuccess">
            </div>
        </van-popup>


    </div>
</template>

<script>
import { util } from '@/utils/util'
import { Toast } from 'vant';
import apiUrl from '@/api/apiUrl'
import httpService from '@/api/httpService'
import axios from 'axios'

export default {
    name: 'ReleasePage',
    components: {
    },
    created(){
        console.log(this.$route.query.goodsId);
        // this.$route.query.goodsId= '156464966761886cd3bcc0da5fa08';

        // 测试语句
        if(this.$route.query.goodsId !== undefined ){
            // 表示重新发布，需要调用接口;
            this.getDetial();
        }

    },
    computed: {
        overTime() {
            if(this.currentDate === null) {
                return
            }else {
                return util.dateToStr(new Date(this.currentDate), 3)
            }
        },
        maxDate() {
            var m = this.minDate.getDay()
            switch(m){
                case 1:
                    return new Date(new Date().getTime() + 3600 * 24 * 3 * 1000)
                    break
                case 2:
                    return new Date(new Date().getTime() + 3600 * 24 * 3 * 1000)
                    break
                case 3:
                    return new Date(new Date().getTime() + 3600 * 24 * 5 * 1000)
                    break
                case 4:
                    return new Date(new Date().getTime() + 3600 * 24 * 5 * 1000)
                    break
                case 5:
                    return new Date(new Date().getTime() + 3600 * 24 * 5 * 1000)
                    break
                case 6:
                    return new Date(new Date().getTime() + 3600 * 24 * 4 * 1000)
                    break
                case 0:
                    return new Date(new Date().getTime() + 3600 * 24 * 3 * 1000)
                    break
            }
        }
    },
    data() {
        return {
            articleName: '',
            articleKind: '',
            articleKindText: '',
            kindList: ['手机数码', '生活电器', '护肤/化妆品', '影音图书', '零食', '珠宝首饰', '其他'],
            startingPrice: '',
            limit:5,
            showText: '恭喜你，成功发布了一件拍卖商品',
            fileList: [],
            descInfo: '',
            showKindPicker: false,
            showTimePicker: false,
            year: 0,
            mouth: 0,
            day: 0,
            minDate: new Date(),
            currentDate: null,
            publishShow: false,
            numberShow: false,
            isSubmit: false,
        }
    },
    methods: {
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
                this.articleName = res.data.title //名称
                this.startingPrice = res.data.startPrice.toString();//起拍价
                this.currentDate = util.dateToStr(new Date(res.data.endTime), 3)//截止时间
                this.descInfo = res.data.details;//拍卖详情
                let imgArr = [];
                imgArr = res.data.imgs.split(";");
                imgArr.splice(imgArr.length-1,1);
                for(let i=0;i<imgArr.length;i++){
                    this.fileList.push({
                        url:imgArr[i]
                    })
                }
                console.log(this.fileList);
                switch (res.data.type) { // 物品类别
                    case 1:
                        this.articleKindText = '手机数码'
                        break
                    case 2:
                        this.articleKindText = '生活电器'
                        break
                    case 3:
                        this.articleKindText = '护肤/化妆品'
                        break
                    case 4:
                        this.articleKindText = '影音图书'
                        break
                    case 5:
                        this.articleKindText = '零食'
                        break
                    case 6:
                        this.articleKindText = '珠宝首饰'
                        break
                    case 7:
                        this.articleKindText = '其他'
                        break
                }
            })
        },
        titleInput(value){
            if(this.filterInput(value) === false){
                this.articleName ='';
            }

            // this.filterInput(value);
            if(value.length > 15){
                Toast('标题长度不能超过十五位');
            }
        },
        textareInput(value){
            // this.descInfo = this.filterInput(value);
            if(this.filterInput(value) === false){
                this.descInfo ='';
            }
        },
        filterInput(val) {
            // 这里过滤的是除了英文字母和数字的其他字符
            // if((/^[A-Za-z0-9\u4e00-\u9fa5]+$/.test(val))){
            //     return false;
            // }
            // return val.replace(, '')

            // var reg = new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5]+$");
            // if(!reg.test(val)) {
            //     Toast('不允许输入特殊字符');
            //     return false;
            // }


            // var regRule = /\uD83C[\uDF00-\uDFFF]|\uD83D[\uDC00-\uDE4F]/g;
            // if(val.match(regRule)) {
            //     Toast.fail('不支持非法字符');
            //     return false
            // }


            // if(!val.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")){
            //      Toast('不允许输入特殊字符');
            //     return false;
	        // }

        },
        onShow () {
            console.log('on show')
        },
        onHide (type) {
            console.log('on hide', type)
        },
        onChange (val) {
            console.log('val change', val)
        },
        change (value) {
            console.log('change', value)
        },
        showSuccess() {
            if(this.articleName.length > 15){
                Toast('标题长度不能超过十五位');
                return;
            }
            if(this.articleName === '' || this.articleKind === '' || this.startingPrice === '' || this.currentDate === null || this.descInfo === '' || this.fileList.length === 0 ) {
                Toast.fail('请将信息填写完整');
                return
            }

            if(this.currentDate.getTime() <= new Date().getTime()) {
                Toast('截止时间需大于当前时间');
                return
            }



            this.isSubmit = true
            let fd = new FormData()

            let newFileList = []
            this.fileList.forEach(item => {
                newFileList.push(item.file)
                fd.append('files', item.file)
            })

            console.log(newFileList)

            let url = apiUrl.create.url
            fd.append('sellerId', this.$store.state.user.userInfo.job_number)
            fd.append('title', this.articleName)
            fd.append('type', this.articleKind + 1)
            fd.append('startPrice', Number(this.startingPrice))
            // fd.append('sPrice', this.startingPrice)
            fd.append('startTime', this.minDate.getTime())
            fd.append('endTime', this.currentDate.getTime())
            fd.append('details', this.descInfo)
            axios.post(url, fd, {headers: {
                'Content-Type': 'multipart/form-data'
            }}).then(res => {
                if(res.data.code === '0'){
                    this.isSubmit = false
                this.publishShow = true
                }
            }, rej => {
                this.isSubmit = false
                Toast.fail(res.msg);
            })








            // console.log(this.fileList)

            // var form = new FormData();
            // form.append("files", this.fileList)

            // console.log(form)

            // const opts = {
            //     apiObj: apiUrl.create,
            //     headers: {
            //         'Content-Type': 'multipart/form-data'
            //     },
            //     body: {
            //         sellerId: '123',
            //         title: this.articleName,
            //         type: this.articleKind + 1,
            //         sPrice: this.startingPrice,
            //         sTime: this.minDate.getTime(),
            //         eTime: this.currentDate.getTime(),
            //         details: this.descInfo,
            //         // files: this.fileList,
            //         files: form
            //     }
            // }
            // httpService.accessAPI(opts)
            //     .then(res => {
            //         this.publishShow = true
            //     }, rej => {

            //     })

        },
        closeSuccess() {
            this.publishShow = false
        },
        onConfirm(value) {
            this.articleKindText = value
            this.articleKind = this.kindList.findIndex(item => {
                return item === value
            })
            this.showKindPicker = false;
        },
        backHandler() {
            this.$router.go(-1)
        },
        verificationHandler(value) {
            if(value.getDay() === 6 || value.getDay() === 0) {
                Toast.fail('不可以选择节假日哦');
                return
            }
            this.showTimePicker = false
        },
        shouNumberHandler() {
            console.log('shouNumberHandlershouNumberHandler')
            this.numberShow = true
        }
    },
    watch: {
        fileList() {
            console.log(this.fileList)
        },
        publishShow() {
            if(this.publishShow === false) {
                this.$router.push('/')
            }
        }
    }
}
</script>

<style lang="less">
    .publish-page{
        position: relative;
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
        }
        .publish-box{
            width: 20.1875rem;
            margin-left: 1.625rem;
            margin-top: -4.5rem;
            padding-bottom: 1rem;
            background: #fff;
            border-radius: .9375rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
            .title-box{
                text-align: center;
            }
            .publish-title{
                width: 7rem;
                height: 1.125rem;
                margin-top: 1.1rem;
            }
            .textarea{
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
        }
        .publish-btn{
            margin-top: 2.5rem
        }
        .upload-title{
            margin-top: 1.25rem;
            margin-bottom: .625rem;
        }
        .van-button--large{
            width: 20.1875rem;
            margin-left: 1.625rem;
            margin-top: 0.75rem;
            height: 2.5rem;
            line-height: 2.5rem;
        }
        .van-button--primary {
            color: #fff;
            background-color: #ff6900;
            border: 1px solid #ff6900;
        }
        .van-popup--center{
            border-radius: .9375rem;
        }
        .publish-success-box{
            width: 18.75rem;
            height: 18.75rem;
            background: url('../../static/images/publish-success-bg.png');
            background-size: cover;
            position: relative;
            img {
                width: 1.5rem;
                height: 1.5rem;
                position: absolute;
                right: .625rem;
                top: .625rem;
            }
        }
    }
</style>
