import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "./assets/css/source-sans-pro/source-sans-pro.css";
import "./assets/css/app.css";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import i18n from './lang';

import "./filters/";
import "./components/";
import api from '@/api'


Vue.use(ElementUI);
Vue.config.productionTip = false;

async function init(){
  i18n.loadLanguageAsync();
  new Vue({
    router,
    store,
    i18n,
    render: function(h) {
      return h(App);
    }
  }).$mount("#app");
}
init();

