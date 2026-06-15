// Front/vue.config.js
module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        // ws: true,
        pathRewrite: {
          '^/api': '/api'
        },
        secure: false,
      }
    }
  }
};

