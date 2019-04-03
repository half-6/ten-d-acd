import camelCase from "lodash/camelCase";
const requireModule = require.context(".", false, /\.vue$/); //extract js files inside modules folder
const modules = {};
import Vue from "vue";

requireModule.keys().forEach(fileName => {
  const moduleName = camelCase(fileName.replace(/(\.\/|\.vue)/g, "")); //
  modules[moduleName] = requireModule(fileName).default;
  Vue.component(moduleName,modules[moduleName])
});
export default modules;
