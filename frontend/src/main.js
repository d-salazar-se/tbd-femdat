import Vue from 'vue'
import VueRouter from 'vue-router'
import DashboardPlugin from './dashboard-plugin'

// Plugins
import App from './App.vue'

// router setup
import routes from './routes/routes'

import NProgress from 'nprogress';
import '../node_modules/nprogress/nprogress.css'


// plugin setup
Vue.use(VueRouter)
Vue.use(DashboardPlugin)

// configure router
const router = new VueRouter({
  routes,
  linkActiveClass: 'active'
});

router.beforeResolve((to, from, next) => {
  if (to.name) {
      NProgress.start()
  }
  next()
})

router.afterEach((to, from) => {
  NProgress.done()
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router
});

/* We import element-ui variables at the end so they can override the default element-ui colors */
import './assets/sass/element_variables.scss'
