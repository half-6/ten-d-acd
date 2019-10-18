import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "landing-page",
      component: require("@/views/landing-page/index.vue").default
    },
    {
      path: "/about",
      name: "about-page",
      component: require("@/views/about-page/index.vue").default
    },
    {
      path: "/expire",
      name: "expire-page",
      component: require("@/views/expire-page/index.vue").default
    },
    {
      path: "/list",
      name: "list-page",
      component: require("@/views/list-page/index.vue").default
    },
    {
      path: "/import",
      name: "import-page",
      component: require("@/views/import-page/index.vue").default
    }
  ]
});
router.beforeEach(async (to, from, next) => {
  console.log(`nav to ${to.path}`)
  if (to.path === '/expire') {
    next();
  }
  else
  {
    const expiredDate = new Date(window.tendConfig.certificateEntity.expire_date)
    if(expiredDate > new Date())
    {
      next();
    }
    else
    {
      next({ path: "expire" })
    }
  }
})

Router.prototype.open = function (routeObject) {
  const {href} = this.resolve(routeObject)
  window.open(href, '_blank')
}

export default router;
