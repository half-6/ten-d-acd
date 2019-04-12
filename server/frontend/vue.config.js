// vue.config.js
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
      },
      "/uploads": {
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
  }
};
