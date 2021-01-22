/*module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  outputDir: '../resources/static'
}*/

'use strict';

// https://webpack.js.org/configuration/dev-server/#devserver-proxy

//const targetHost = 'localhost';
//const targetPort = 33080;
//
//const port = 8080;
//let cookie;
//
//const replaceCookie = function(cookie) {
//    return cookie
//        .replace(/\s+domain=[^\s;]+;?/, '')
//        .replace(/\s+secure;?/, '')
//};
//
//module.exports = {
//    "transpileDependencies": [
//        "vuetify"
//      ],
//  outputDir: '../resources/static',
//
//  devServer: {
//    port: port,
//    clientLogLevel: 'debug',
//    historyApiFallback: true,
//    hot: true,
//    inline: true,
//    publicPath: `http://localhost:${port}/`,
//    transportMode: 'sockjs',
//    headers: {
//      'Access-Control-Allow-Origin': '*'
//    },
//    proxy: {
//      '^/ws': {
//        target: `http://${targetHost}:${targetPort}`,
//        changeOrigin: true,
//        ws: true,
//        onProxyReq: (proxyReq) => {
//          if (proxyReq.getHeader('origin')) {
//            proxyReq.setHeader('origin', `http://${targetHost}:${targetPort}`);
//          }
//
//          if (proxyReq.getHeader('set-cookie')) {
//            proxyReq.headers['set-cookie'] = proxyReq.headers['set-cookie'].map(replaceCookie)
//          }
//
//          if (cookie) proxyReq.setHeader('Cookie', cookie);
//        },
//        onProxyRes: (proxyRes) => {
//          if (proxyRes.headers['set-cookie']) {
//            cookie = proxyRes.headers['set-cookie'] = proxyRes.headers['set-cookie'].map(replaceCookie)
//          }
//        },
//      },
//      '^/(sse|api|sign|resources)': {
//        target: `http://${targetHost}:${targetPort}`,
//        changeOrigin: true,
//        onProxyRes: (proxyRes) => {
//          if (proxyRes.headers['set-cookie']) {
//            cookie = proxyRes.headers['set-cookie'] = proxyRes.headers['set-cookie'].map(replaceCookie)
//          }
//        },
//      }
//    }
//  },
//
//  pluginOptions: {
//    moment: {
//      locales: ['ko_kr']
//    }
//  }
//};


const path = require('path');

const axios = require('axios');

axios.defaults.baseURL = 'https://localhost:8080'

module.exports = {
  outputDir: path.resolve(__dirname, "../" + "src/main/resources/public"),
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8090",
        ws: true,
        changeOrigin: true
      },
    }
  }
}