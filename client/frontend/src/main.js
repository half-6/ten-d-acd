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
import BootstrapVue from 'bootstrap-vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import i18n from './lang';

import './mixin';
import "./filters/";
import "./components/";
import api from '@/api'


Vue.use(BootstrapVue);
Vue.use(ElementUI);
Vue.prototype.$tendConfig = window.tendConfig;
Vue.prototype.$pathology = [{"value":"Malignant","text":"Malignant"},{"value":"Benign","text":"Benign"}];
Vue.config.productionTip = false;

async function init(){

  Vue.prototype.$machineType = (await api.getMachineType()).data
  //Vue.prototype.$cancerType = (await api.getCancerType()).data
  Vue.prototype.$cancerType =[{"cancer_type_name":"Thyroid","cancer_type_chinese_name":"甲状腺","cancer_type_id":1,"cancer_type_short_name":"TH","date_registered":"2019-05-15 22:52:32.733+0000","status":"active"}]

  Vue.prototype.$hospital = (await api.getHospital({$limit:1})).data
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

