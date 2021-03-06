import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import 'normalize.css/normalize.css'
import 'element-ui/lib/theme-chalk/index.css'


Vue.config.productionTip = false

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')
