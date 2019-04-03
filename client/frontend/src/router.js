import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
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
      path: "/list",
      name: "list-page",
      component: require("@/views/list-page/index.vue").default
    }
  ]
});
