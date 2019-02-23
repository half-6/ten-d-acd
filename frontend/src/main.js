import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";


import "bootstrap/dist/css/bootstrap.min.css";
import "popper.js";
import 'bootstrap';
import 'jquery';
import "./assets/css/source-sans-pro/source-sans-pro.css";
import "./assets/css/app.css";
import VueResource from 'vue-resource';
import BootstrapVue from 'bootstrap-vue'

import "./filters/";
import "./components/";


Vue.use(VueResource);
Vue.use(BootstrapVue);
Vue.prototype.$tendConfig = window.tendConfig;
Vue.prototype.$pathology = [{"value":"Malignant","text":"Malignant"},{"value":"Benign","text":"Benign"}];

Vue.config.productionTip = false;
new Vue({
  router,
  store,
  render: function(h) {
    return h(App);
  }
}).$mount("#app");
