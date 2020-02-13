<template>
    <!-- 拍卖介绍页面 -->
    <div class="introduction-group">
        <div class="top-bg">
            <span>拍卖规则及注意事项</span>
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
        </div>
        <div class="introduction-box">
            <img src="@/static/images/introduction-bg.png" preview preview-text="拍卖流程">
        </div>
    </div>
    
</template>

<script>
export default {
    methods: {
        backHandler() {
            this.$router.go(-1)
        }
    }
}
</script>

<style lang="less">
    .introduction-group{
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
        .introduction-box{
            width: 85%;
            margin-left: 7.5%;
            margin-top: -4rem;
            img {
                width: 100%;
            }
            .content-box{
                font-size: .75rem;
            }
        }
    }
    
</style>