module.exports = {
  lintOnSave: false,
  devServer: {
    open: true,
    port: 8080,
    host: '127.0.0.1',
    https: false,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:9090',
        ws: true,
        changOrigin: true,
        logLevel: 'debug',
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
