import Vue from "vue";


import App from "./App";
import router from "./router";
import store from "./store";
import _ from 'lodash'

//import "./assets/css/source-sans-pro/source-sans-pro.css";
//import "./assets/css/app.css";

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


import '@/assets/css/index.scss' // global css
import 'normalize.css/normalize.css' // A modern alternative to CSS resets


import i18n from './lang';
import filters from "./filters/";
import "./components/";
import api from '@/api/image'

import '@/permission' // permission control


Vue.use(ElementUI);
Vue.config.productionTip = false;

async function init(){
  Vue.prototype.$machineType = (await api.getMachineType()).data
  Vue.prototype.$cancerType = (await api.getCancerType()).data
  Vue.prototype.$hospital = (await api.getHospital()).data
  Vue.prototype.$pathology = [{"value":"Malignant","text":"Malignant"},{"value":"Benign","text":"Benign"}];
  Vue.prototype.$filters = filters
  Vue.prototype.$formatters = {};
  _.forIn(filters,(f,k)=>{
        _.forIn(f,(sf,sk)=>{
          _.set(Vue.prototype.$formatters,`${k}.${sk}`,(r,c,v,i)=>{
            return sf(v);
          })
        })
      }
  )
  i18n.loadLanguageAsync();
  new Vue({
    el:'#app',
    router,
    store,
    i18n,
    render: h => h(App)
  })
}
init();

