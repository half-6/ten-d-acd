import Vue from "vue";
import Router from "vue-router";
import Layout from './views/layout/Layout'

Vue.use(Router);


/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in subMenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
 **/

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    { path: '/login', component: () => import('@/views/login/index.vue'), hidden: true },
    { path: '/404', component: () => import('@/views/404/index.vue'), hidden: true },
    {
      path: "",
      component: Layout,
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          name: 'dashboard',
          meta: { title: 'Dashboard', icon: 'dashboard', noCache: true, affix: true, auth:true  }
        }
      ]
    },
    {
      path: "/roi-image",
      component: Layout,
      redirect: 'noredirect',
      children: [
        {
          path: '',
          component: () => import('@/views/roi-image/index.vue'),
          name: 'roi-image',
          meta: { title: 'Roi Images', icon: 'documentation', noCache: true, affix: true, auth:true  }
        }
      ]
    },
    {
      path: "/roi-history",
      component: Layout,
      redirect: 'noredirect',
      hidden: true,
      children: [
        {
          path: '',
          component: () => import('@/views/roi-history/index.vue'),
          name: 'roi-history',
          meta: { title: 'Roi History', icon: 'table', noCache: true, affix: true, auth:true  }
        }
      ]
    },
    {
      path: "/statistics",
      component: Layout,
      redirect: 'noredirect',
      children: [
        {
          path: '',
          component: () => import('@/views/statistics/index.vue'),
          name: 'statistics',
          meta: { title: 'Statistics',  icon: 'list', noCache: true, affix: true, auth:true  }
        }
      ]
    },
    {
      path: "/hospital",
      component: Layout,
      redirect: 'noredirect',
      meta: { title: 'Hospital Manage',  icon: 'list', noCache: true, affix: true, auth:true  },
      children: [
        {
          path: '',
          component: () => import('@/views/hospital/index.vue'),
          name: 'hospital',
          meta: { title: 'Hospital Manage',  icon: 'table', noCache: true, affix: true, auth:true  }
        },
        {
          path: 'certificate/:hospitalId',
          component: () => import('@/views/certificate/index.vue'),
          name: 'certificate',
          meta: { title: 'Certificate Manage',  icon: 'lock', noCache: true,  auth:true },
          hidden:true
        }
      ]
    },
    { path: '*', redirect: '/404', hidden: true }
  ]
});
