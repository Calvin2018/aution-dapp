import Vue from 'vue'
Vue.filter('dateStrDay', input => {
  if (!input && input !== 0) return
  return input.split(' ')[0]
})
