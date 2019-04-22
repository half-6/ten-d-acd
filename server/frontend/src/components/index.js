import _ from "lodash";
const requireModule = require.context(".", true, /\.vue$/); //extract js files inside modules folder
const modules = {};
import Vue from "vue";
requireModule.keys().forEach(fileName => {
  const moduleName = _.kebabCase(fileName.replace(/(index.vue)/g, "")); //
  modules[moduleName] = requireModule(fileName).default;
  console.log(`register component => ${moduleName}`)
  Vue.component(moduleName,modules[moduleName])
});
export default modules;
