const NODE_ENV = process.env.environment;

// if(process.env.npm_lifecycle_script) {
//     const NODE_ENV = process.env.npm_lifecycle_script.match(/=(\S*)&&/)[1];

// }


// console.log(process.env)
// console.log(process.env.npm_lifecycle_script)
// console.log(process.env.npm_lifecycle_script.match(/=(\S*)&&/)[1])
// console.log('*************************')
console.log(NODE_ENV)
// console.log('*************************')


const config = {
    // DEV2 local,TEST 最新,PRODUCTION 上线，dev 
    // 线上环境
    production: {
        BASE_API: 'https://49.234.145.52/cesbaas/',
        BASE_API2: 'https://49.234.145.52/gateway/',
        BASE_API3: 'https://49.234.145.52/explorer/',
        BASE_API4: 'https://49.234.145.52/eth/',
    },
    development: {
       FOO_API: 'development.foo.api.com',
       BAR_API: 'development.bar.api.com',
       BAZ_API: 'development.baz.api.com',
    },
    // 当前最新环境
    test: {
        BASE_API: 'http://testcbaas.cclcloud.net/cesbaas/',
        BASE_API2: 'http://testcbaas.cclcloud.net/gateway/',
        BASE_API3: 'http://testcbaas.cclcloud.net/explorer/',
        BASE_API4: 'http://testcbaas.cclcloud.net/eth/',
    },
    // 本地环境
    dev2:{
        BASE_API: 'http://devcbaas.cclcloud.net/cesbaas/',
        BASE_API2: 'http://devcbaas.cclcloud.net/gateway/',
        BASE_API3: 'http://devcbaas.cclcloud.net/explorer/',
        BASE_API4: 'http://devcbaas.cclcloud.net/eth/',
    }
}
module.exports = config[NODE_ENV];
