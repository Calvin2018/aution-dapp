// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

// import './view/ReleasePage/exif-js-master/exif'

import '@/static/css/bass.css'

import store from '@/store'


// import Vant from 'vant';
// import 'vant/lib/index.css';
// Vue.use(Vant);

import { Lazyload } from 'vant';
import 'vant/lib/Lazyload/style';
Vue.use(Lazyload);

import { ImagePreview } from 'vant';
Vue.use(ImagePreview);
import { Uploader } from 'vant';
import 'vant/lib/Uploader/style';
import { Grid, GridItem } from 'vant';
import 'vant/lib/Grid/style';
import 'vant/lib/Grid-item/style';
import { DropdownMenu, DropdownItem } from 'vant';
import 'vant/lib/Dropdown-menu/style';
import 'vant/lib/Dropdown-item/style';
import { Tabbar, TabbarItem } from 'vant';
import 'vant/lib/Tabbar-item/style';
import 'vant/lib/Tabbar/style';
import { List } from 'vant';
import 'vant/lib/List/style';
import { Field } from 'vant';
import 'vant/lib/Field/style';
import { Picker, Popup } from 'vant';
import 'vant/lib/Picker/style';
import { DatetimePicker } from 'vant';
import { PullRefresh } from 'vant';
import 'vant/lib/Pull-refresh/style';
import { Button } from 'vant';
import 'vant/lib/Button/style';
import { Image } from 'vant';
import { Tab, Tabs } from 'vant';
import 'vant/lib/Tab/style';
import 'vant/lib/Tabs/style';
import { Swipe, SwipeItem } from 'vant';
import 'vant/lib/swipe/style';
import 'vant/lib/swipe-item/style';
import { Rate } from 'vant';
import 'vant/lib/Rate/style';
import { Toast } from 'vant';
import 'vant/lib/Toast/style';
import { NumberKeyboard } from 'vant';
import 'vant/lib/Number-keyboard/style';
import { Overlay } from 'vant';
import 'vant/lib/Overlay/style';

Vue.use(Overlay);
Vue.use(NumberKeyboard);
Vue.use(Toast);
Vue.use(Rate);
Vue.use(Swipe).use(SwipeItem);
Vue.use(Tab).use(Tabs);
Vue.use(Image);
Vue.use(Button);
Vue.use(PullRefresh);
Vue.use(DatetimePicker);
Vue.use(Picker);
Vue.use(Popup);
Vue.use(Field);
Vue.use(List);
Vue.use(Tabbar).use(TabbarItem);
Vue.use(DropdownMenu).use(DropdownItem);
Vue.use(Grid).use(GridItem);
Vue.use(Uploader);


// 点击放大图片组件
import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
Vue.use(preview)


Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})
