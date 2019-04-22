// vue.config.js
const path = require('path')
function resolve(dir) {
  return path.join(__dirname, '.', dir)
}
module.exports = {
  // proxy all webpack dev-server requests starting with /api
  // to our Spring Boot backend (localhost:8088) using http-proxy-middleware
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    port:3000,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        ws: true,
        changeOrigin: true
      }
    }
  },
  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: "../backend/src/main/resources/public",
  assetsDir: "static",
  chainWebpack: config => {
    config.module
      .rule("eslint")
      .use("eslint-loader")
      .options({
        fix: true
      });
    config.module.rules.delete("svg");
    config.module
        .rule('svg')
        .test(/\.svg$/)
        .include
          .add(resolve('src/assets/svg')) //处理svg目录
          .end()
        .use('svg-sprite-loader')
          .loader('svg-sprite-loader')
          .options({
            symbolId: 'icon-[name]'
          })
          .end()
  }
};
