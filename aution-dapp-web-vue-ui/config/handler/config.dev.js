module.exports = {
    // 开发环境代理服务器
    devProxy: {
        host: '0.0.0.0', // ip/localhost都可以访问
        port: 8080
    },
    // 后端服务器地址
    servers: {
        '/abc':'http://10.250.218.104:8089',
    }
 }
