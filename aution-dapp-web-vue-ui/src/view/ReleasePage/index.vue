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
                placeholder="请输入起拍价（STC）"
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
                    :after-read="onRead"
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
// import {Exif} from './exif-js-master/exif'

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
            files:{
                name: "",
                type: ""
            },
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
        iosOrAndroid() {
            var u = navigator.userAgent;
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

            if (isAndroid) {
                return 'and';
            } else {
                return 'ios';
            }
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
            if(value.indexOf(" ") > -1){
                // this.articleName ='';
                Toast('标题不能包含空格');
            }
            if(value.length > 15){
                Toast('标题长度不能超过十五位');
            }
            //不能有特殊字符及表情
            if(value !=="" && !value.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")){
                Toast('标题不允许输入特殊字符');
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
            if(this.articleName.indexOf(" ") > -1){
                Toast('标题不能包含空格');
                return;
            }
            if(this.articleName !=="" && !this.articleName.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")){
                Toast('不允许输入特殊字符');
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
                if(res.data.code === '100004') {
                    Toast.fail(res.data.msg);
                    this.isSubmit = false
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
        },





        async onRead(file) {
            this.files.name = file.file.name; // 获取文件名
            this.files.type = file.file.type; // 获取类型
            this.picValue = file.file; // 文件流
            this.imgPreview(this.picValue);
        },
        imgPreview(file) {
            let self = this;
            let Orientation;
            //去获取拍照时的信息，解决拍出来的照片旋转问题
            Exif.getData(file, function () {
                Orientation = Exif.getTag(this, "Orientation");
            });
            // 看支持不支持FileReader
            if (!file || !window.FileReader) return;
            if (/^image/.test(file.type)) {
                // 创建一个reader
                let reader = new FileReader();
                // 将图片2将转成 base64 格式
                reader.readAsDataURL(file);
                // 读取成功后的回调
                reader.onloadend = function () {
                // console.log(this.result);
                let result = this.result;
                let img = new Image();
                img.src = result;
                //判断图片是否大于500K,是就直接上传，反之压缩图片
                if (this.result.length <= 500 * 1024) {
                    self.headerImage = this.result;
                    self.postImg();
                } else {
                    img.onload = function () {
                    let data = self.compress(img, Orientation);
                    self.headerImage = data;
                    self.postImg();
                    };
                }
                };
            }
        },
        // 压缩图片
        compress(img, Orientation) {
            let canvas = document.createElement("canvas");
            let ctx = canvas.getContext("2d");
            //瓦片canvas
            let tCanvas = document.createElement("canvas");
            let tctx = tCanvas.getContext("2d");
            // let initSize = img.src.length;
            let width = img.width;
            let height = img.height;
            //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
            let ratio;
            if ((ratio = (width * height) / 4000000) > 1) {
                // console.log("大于400万像素");
                ratio = Math.sqrt(ratio);
                width /= ratio;
                height /= ratio;
            } else {
                ratio = 1;
            }
            canvas.width = width;
            canvas.height = height;
            //    铺底色
            ctx.fillStyle = "#fff";
            ctx.fillRect(0, 0, canvas.width, canvas.height);
            //如果图片像素大于100万则使用瓦片绘制
            let count;
            if ((count = (width * height) / 1000000) > 1) {
                // console.log("超过100W像素");
                count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
                //      计算每块瓦片的宽和高
                let nw = ~~(width / count);
                let nh = ~~(height / count);
                tCanvas.width = nw;
                tCanvas.height = nh;
                for (let i = 0; i < count; i++) {
                for (let j = 0; j < count; j++) {
                    tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
                    ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
                }
                }
            } else {
                ctx.drawImage(img, 0, 0, width, height);
            }
            //修复ios上传图片的时候 被旋转的问题
            if (Orientation != "" && Orientation != 1) {
                switch (Orientation) {
                case 6: //需要顺时针（向左）90度旋转
                    this.rotateImg(img, "left", canvas);
                    break;
                case 8: //需要逆时针（向右）90度旋转
                    this.rotateImg(img, "right", canvas);
                    break;
                case 3: //需要180度旋转
                    this.rotateImg(img, "right", canvas); //转两次
                    this.rotateImg(img, "right", canvas);
                    break;
                }
            }
            //进行最小压缩
            let ndata = canvas.toDataURL("image/jpeg", 0.1);
            tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
            return ndata;
        },
        // 旋转图片
        rotateImg(img, direction, canvas) {
            //最小与最大旋转方向，图片旋转4次后回到原方向
            const min_step = 0;
            const max_step = 3;
            if (img == null) return;
            //img的高度和宽度不能在img元素隐藏后获取，否则会出错
            let height = img.height;
            let width = img.width;
            let step = 2;
            if (step == null) {
                step = min_step;
            }
            if (direction == "right") {
                step++;
                //旋转到原位置，即超过最大值
                step > max_step && (step = min_step);
            } else {
                step--;
                step < min_step && (step = max_step);
            }
            //旋转角度以弧度值为参数
            let degree = (step * 90 * Math.PI) / 180;
            let ctx = canvas.getContext("2d");
            switch (step) {
                case 0:
                canvas.width = width;
                canvas.height = height;
                ctx.drawImage(img, 0, 0);
                break;
                case 1:
                canvas.width = height;
                canvas.height = width;
                ctx.rotate(degree);
                ctx.drawImage(img, 0, -height);
                break;
                case 2:
                canvas.width = width;
                canvas.height = height;
                ctx.rotate(degree);
                ctx.drawImage(img, -width, -height);
                break;
                case 3:
                canvas.width = height;
                canvas.height = width;
                ctx.rotate(degree);
                ctx.drawImage(img, -width, 0);
                break;
            }
        },
        //将base64转换为文件
        dataURLtoFile(dataurl) {
            var arr = dataurl.split(","),
                bstr = atob(arr[1]),
                n = bstr.length,
                u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new File([u8arr], this.files.name, {
                type: this.files.type
            });
            },
            //这里写接口
            async postImg() {
            let file = this.dataURLtoFile(this.headerImage);
            let formData = new window.FormData();
            formData.append("file", file);
            toast_loding(this, "图片上传中···");
            try {
                let res = await util.ajax.post(this.upImgUrl, formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
                });
            } catch (e) {
                console.log(e);
            }
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
