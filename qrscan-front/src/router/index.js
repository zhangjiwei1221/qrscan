import Vue from 'vue'
import VueRouter from 'vue-router'
import { TOKEN, WEBSITE_NAME } from '@/assets/js/const'

const originalPush = VueRouter.prototype.push

VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const Home = () => import('@/views/Home')
const Login = () => import('@/views/Login')
const Error = () => import('@/views/Error')

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '*',
    meta: {
      title: '出错啦。。。'
    },
    component: Error
  },
  {
    path: '/home',
    meta: {
      title: '主页'
    },
    component: Home
  },
  {
    path: '/login',
    meta: {
      title: '登录'
    },
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  const filter = ['/login']
  const token = localStorage.getItem(TOKEN)
  if (filter.includes(to.path) || token) {
    setNewTitle()
    next()
  } else {
    next('/login')
  }

  function setNewTitle() {
    to.meta.title && (document.title = `${to.meta.title} | ${WEBSITE_NAME}`)
  }
})

export default router
