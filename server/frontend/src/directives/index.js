import camelCase from "lodash/camelCase";
import modules from "../filters";

const requireModule = require.context(".", false, /\.js/);

requireModule.keys().forEach(fileName => {
  if (fileName === "./index.js") return; //reject the index.js file
  const moduleName = camelCase(fileName.replace(/(\.\/|\.js)/g, "")); //
  console.log(`register filter => ${moduleName}`)
  modules[moduleName] = requireModule(fileName).default;
})
export default modules;
