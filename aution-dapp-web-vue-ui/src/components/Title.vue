<template>
        <div class="history-title">
            <img class="back-btn" src="@/static/images/back-btn.png" @click="backHandler"/>
            {{title}}
        </div>
</template>

<script>
export default {
    props: ['title']
    
}
</script>

<style lang="less">
    
</style>