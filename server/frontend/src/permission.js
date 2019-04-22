import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { Message } from 'element-ui'
import _ from 'lodash'
import { getToken } from '@/utils/auth' // getToken from cookie

NProgress.configure({ showSpinner: false })// NProgress configuration

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const token = getToken();
  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      if (!store.getters.hasLogin) {
        try {
          await store.dispatch('GetInfo');
          next()
        }
        catch (e) {
          await store.dispatch('FedLogOut');
          Message.error(err || 'Verification failed, please login again')
          next({ path: '/' })
        }
      } else {
        next()
      }
    }
  } else {
    if (!_.get(to,"meta.auth")) {
      next()
    } else {
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})
router.afterEach(() => {
  NProgress.done() // 结束Progress
})
