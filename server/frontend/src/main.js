import Vue from "vue";


import App from "./App";
import router from "./router";
import store from "./store";
import _ from 'lodash';
import IdleVue from 'idle-vue';


//import "./assets/css/source-sans-pro/source-sans-pro.css";
//import "./assets/css/app.css";

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


import '@/assets/css/index.scss' // global css
import 'normalize.css/normalize.css' // A modern alternative to CSS resets


import i18n from './lang';
import filters from "./filters/";
import "./components/";
import directives from "./directives/";
import api from '@/api/image'

import '@/permission' // permission control


Vue.use(ElementUI);
Vue.config.productionTip = false;

async function init(){
  Vue.prototype.$machineType = await readData(api.getMachineType)
  Vue.prototype.$cancerType = await readData(api.getCancerType)
  Vue.prototype.$hospital = await readData(api.getHospital)
  Vue.prototype.$aiVersion = await readData(api.getAIVersion)
  Vue.prototype.$pathology = [{"value":"Malignant","text":"Malignant"},{"value":"Benign","text":"Benign"}];
  Vue.prototype.$tendConfig = window.tendConfig;

  Vue.prototype.$filters = filters
  Vue.prototype.$directives = directives

  Vue.prototype.$formatters = {};
  Vue.prototype.$functions = {};
  _.forIn(filters,(f,k)=>{
        _.forIn(f,(sf,sk)=>{
          //register formatter for element ui components
          console.log(`register $formatters ${k}.${sk}`)
          _.set(Vue.prototype.$formatters,`${k}.${sk}`,(r,c,v,i)=>{
            return sf(v);
          })
          //register global function
          console.log(`register $functions ${k}.${sk}`)
          _.set(Vue.prototype.$functions,`${k}.${sk}`,sf)
        })
      }
  )
  i18n.loadLanguageAsync();
  Vue.use(IdleVue, {
    eventEmitter: new Vue(),
    idleTime: 1000 * 60 * 20 //20m
  })
  new Vue({
    el:'#app',
    router,
    store,
    i18n,
    render: h => h(App),
    onIdle() {
      if(this.$router.currentRoute.path!=='/login')
      {
        console.log("i am idle and auto logout");
        this.$store.dispatch('LogOut').then(() => {
          location.reload()
        })
      }
      else
      {
        console.log("i am idle");
      }
    }
  })
}
async function readData(asyncFun,defaultValue) {
  const res = await asyncFun()
  return _.get(res,"data",defaultValue)
}
init();

