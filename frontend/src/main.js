import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";


import "bootstrap/dist/css/bootstrap.min.css";
import "popper.js";
import 'bootstrap';
import "./assets/css/source-sans-pro/source-sans-pro.css";
import "./assets/css/app.css";


Vue.config.productionTip = false;
new Vue({
  router,
  store,
  render: function(h) {
    return h(App);
  }
}).$mount("#app");
