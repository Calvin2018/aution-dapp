import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: () => import('@/view/MainPage'),
      children: [
        {
          path: '/',
          name: 'Auction',
          component: () => import('@/view/AuctionPage/index') 
        },
        {
          path: '/release',
          name: 'Release',
          component: () => import('@/view/ReleasePage/index') 
        },
        {
          path: '/userInfo',
          name: 'UserInfo',
          component: () => import('@/view/UserInfoPage/index') 
        },
        {
          path: '/auctionList',
          name: 'AuctionList',
          component: () => import('@/view/UserInfoPage/AuctionListPage') 
        },
      ]
    },
    {
      path: '/introduction',
      name: 'Introduction',
      component: () => import('@/view/AuctionPage/IntroductionPage') 
    },
    {
      path: '/detail',
      name: 'Detail',
      component: () => import('@/view/AuctionPage/DetailPage') 
    },
    {
      path: '/history',
      name: 'History',
      component: () => import('@/view/AuctionPage/AuctionHistoryPage') 
    },
  ]
})
