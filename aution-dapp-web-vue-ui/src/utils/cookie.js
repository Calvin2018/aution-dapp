import Cookie from 'js-cookie'

export function getCookie(name) {
  return Cookie.get(name)
}

export function setCookie(name, value) {
  return Cookie.set(name, value)
}

export function removeCookie(name) {
  return Cookie.remove(name)
}

export function clearAllCookie() {
  console.log('清空所有cookie')
  // var keys = document.cookie.match(/[^ =;]+(?=\=)/g);

  // if(keys) {
  //   for(var i = keys.length; i--;)
  //     document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
  // }


  // var cookies = document.cookie.split(";");
  // var domain = '.'+location.host;
  // console.log(cookies);
  // for (var i = 0; i < cookies.length; i++) {
  //   var cookie = cookies[i];
  //   var eqPos = cookie.indexOf("=");
  //   var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
  //   document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT; Domain="+domain+"; path=/";
  //   }
  // if(cookies.length > 0){
  //   for (var i = 0; i < cookies.length; i++) {
  //     var cookie = cookies[i];
  //     var eqPos = cookie.indexOf("=");
  //     var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
  //     document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT; Domain="+domain+"; path=/";
  //   }
  // }



var domain = '.'+location.host;
var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
console.log(keys)
if(keys) {  
for(var i = keys.length; i--;) {
    var date=new Date();
    date.setTime(date.getTime()-10000);
    document.cookie=keys[i]+"=; expire="+date.toUTCString()+"; Domain="+domain+"; Path=/";
        } 
      }
}