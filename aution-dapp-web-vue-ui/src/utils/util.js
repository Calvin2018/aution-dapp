/**
 * Created by Administrator on 2017/9/21 0021.
 * 封装常用工具函数,包含对原生基本类型的扩展
 */
export const formatJson = function(filterVal, list) {
  return list.map(v => filterVal.map(j => v[j]))
}

var weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
export const util = {
  extend: function() {
    var options
    var name
    var src
    var copy
    var copyIsArray
    var clone
    var target = arguments[0] || {}
    var i = 1
    var length = arguments.length
    var deep = false

    // Handle a deep copy situation
    if (typeof target === 'boolean') {
      deep = target

      // Skip the boolean and the target
      target = arguments[i] || {}
      i++
    }

    // Handle case when target is a string or something (possible in deep copy)
    if (typeof target !== 'object' && typeof target !== 'function') {
      target = {}
    }

    // Extend jQuery itself if only one argument is passed
    if (i === length) {
      target = this
      i--
    }

    for (; i < length; i++) {
      // Only deal with non-null/undefined values
      if ((options = arguments[i]) != null) {
        // Extend the base object
        for (name in options) {
          src = target[name]
          copy = options[name]

          // Prevent never-ending loop
          if (target === copy) {
            continue
          }

          // Recurse if we're merging plain objects or arrays
          if (deep && copy && (copy.constructor === Object || (copyIsArray = Array.isArray(copy)))) {
            if (copyIsArray) {
              copyIsArray = false
              clone = src && Array.isArray(src) ? src : []
            } else {
              clone = src && src.constructor === Object ? src : {}
            }

            // Never move original objects, clone them
            target[name] = this.extend(deep, clone, copy)

            // Don't bring in undefined values
          } else if (copy !== undefined) {
            target[name] = copy
          }
        }
      }
    }
    // Return the modified object
    return target
  },
 getuuid:  function() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}
,
  getCurDate: function() {
    var date = new Date()

    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()
    month = month.toString().length > 1 ? month : '0' + month
    day = day.toString().length > 1 ? day : '0' + day
    return { year, month, day }
  },
  getCurTime: function() {
    var date = new Date()

    var hours = date.getHours()
    var minutes = date.getMinutes()
    var seconds = date.getSeconds()

    hours = hours.toString().length > 1 ? hours : '0' + hours
    minutes = minutes.toString().length > 1 ? minutes : '0' + minutes
    seconds = seconds.toString().length > 1 ? seconds : '0' + seconds

    return { hours, minutes, seconds }
  },
  getWeek: function() {
    var date = new Date()
    var week = weeks[date.getDay()]
    return { week }
  },

  getCurDateTimeWeek: function() {
    var date = this.getCurDate()
    var time = this.getCurTime()
    var week = this.getWeek()

    return (
      date.year +
      '-' +
      date.month +
      '-' +
      date.day +
      ' ' +
      time.hours +
      ':' +
      time.minutes +
      ':' +
      time.seconds +
      ' ' +
      week.week
    )
  },
  dateToStr: function(now, flag) {
    if (!now) return ''
    if (typeof now === 'string') return now
    var year = now.getFullYear()
    var month = now.getMonth() + 1
    var date = now.getDate()
    var hour = now.getHours()
    var min = now.getMinutes()
    var sec = now.getSeconds()
    if (month < 10) {
      month = '0' + month
    }
    if (date < 10) {
      date = '0' + date
    }
    if (hour < 10) {
      hour = '0' + hour
    }
    if (min < 10) {
      min = '0' + min
    }
    if (sec < 10) {
      sec = '0' + sec
    }
    if (flag === 1) {
      return year + '-' + month + '-' + date
    } else if (flag === 2) {
      return hour + ':' + min
    } else if (flag === 3) {
      return year + '-' + month + '-' + date + ' ' + hour + ':' + min
    }else if(flag === 4) {
      return year + '-' + month
    }else if(flag === 5) {
      return year + '/' + month + '/' + date + ' ' + hour + ':' + min
    }
  },
  deepCopy(obj, cache = []) {
    // just return if obj is immutable value
    if (obj === null || typeof obj !== 'object') {
      return obj
    }

    // if obj is hit, it is in circular structure
    const hit = find(cache, c => c.original === obj)
    if (hit) {
      return hit.copy
    }

    const copy = Array.isArray(obj) ? [] : {}
    // put the copy into cache at first
    // because we want to refer it in recursive deepCopy
    cache.push({
      original: obj,
      copy
    })

    Object.keys(obj).forEach(key => {
      copy[key] = this.deepCopy(obj[key], cache)
    })

    return copy
  },
  isObject: input => typeof input === 'object' && !(input instanceof Array),
  getWindowsParams(key) {
    let params = window.location.search
    if (!params) return
    params = params.split('?')[1].split('/')[0]
    const paramsArray = params.split('&')
    const tempObj = {}
    for (let i = 0; i < paramsArray.length; i++) {
      const tempArr = paramsArray[i].split('=')
      tempObj[tempArr[0]] = tempArr[1]
    }
    return tempObj[key] || ''
  },
  getBackUrlObj() {
    const backUrl = this.getWindowsParams('back_url') || ''
    const returnObj = {}
    if (backUrl) {
      returnObj.name = backUrl
      let query = this.getWindowsParams('query') || ''
      if (query) {
        returnObj.query = {}
        query = decodeURIComponent(query)
        returnObj.query = JSON.parse(query)
      }
      let params = this.getWindowsParams('params') || ''
      if (params) {
        returnObj.params = {}
        params = decodeURIComponent(params)
        returnObj.params = JSON.parse(params)
      }
    }
    return returnObj
  },
  isEmptyObject(e) {
    let t
    for (t in e) {
      return !1
    }
    return !0
  },
  // 将数组进行切割
  group(array, subGroupLength) {
    let index = 0;
    let newArray = [];
    while(index < array.length) {
        newArray.push(array.slice(index, index += subGroupLength));
    }
    return newArray;
  },
  // 比较版本号大小
  versionfunegt (ver1,ver2) {
    var version1pre = parseFloat(ver1);
    var version2pre = parseFloat(ver2);
    var version1next =  ver1.replace(version1pre + ".","");
    var version2next =  ver2.replace(version2pre + ".","");
    if(version1pre > version2pre){
        return true;
    }else if(version1pre <= version2pre){
        return false;
    }else{
        if(version1next >= version2next){
            return true;
        }else{
            return false;
        }
    }
},
  recombinationUrl(url,allianceId){
    let explorerIndex =url.indexOf("explorer");
    let apiIndex=url.indexOf("api");
    let eplIndex=url.indexOf("epl");
    let newUrl="";
    if(explorerIndex >0){
      //服务器地址
      newUrl=url.substring(0,Number(explorerIndex)+9)+""+allianceId+"/"+url.substring(apiIndex)
    }else {
      //测试地址
      newUrl=url.substring(0,5) +""+allianceId+"/"+url.substring(apiIndex)
    }
    // let allianceIndex=url.indexOf("/"+allianceId+"/");
    // let newUrl="";
    // if(allianceIndex >0){
    //   newUrl=url.substring(0,allianceIndex+1)+""+allianceId+"/"+ url.substring(apiIndex);
    // }else {
    //   newUrl=url.substring(0,apiIndex)+""+allianceId+"/"+ url.substring(apiIndex);
    // }
    return newUrl;
  },
  recombinationEthUrl(url,allianceId){
    let ethIndex =url.indexOf("etf");
    let apiIndex=url.indexOf("api");
    let newUrl="";
    if(ethIndex >0){
      //服务器地址
      newUrl=url.substring(0,Number(ethIndex)+4)+""+allianceId+"/"+url.substring(apiIndex)
    }else {
      //测试地址
      newUrl=url.substring(0,5) +""+allianceId+"/"+url.substring(apiIndex)
    }
    return newUrl;
  },

  // 切割数组
  chunk(array, size) {
    //获取数组的长度，如果你传入的不是数组，那么获取到的就是undefined
    const length = array.length
    //判断不是数组，或者size没有设置，size小于1，就返回空数组
    if (!length || !size || size < 1) {
      return []
    }
    //核心部分
    let index = 0 //用来表示切割元素的范围start
    let resIndex = 0 //用来递增表示输出数组的下标

    //根据length和size算出输出数组的长度，并且创建它。
    let result = new Array(Math.ceil(length / size))
    //进行循环
    while (index < length) {
      //循环过程中设置result[0]和result[1]的值。该值根据array.slice切割得到。
      result[resIndex++] = array.slice(index, (index += size))
    }
    //输出新数组
    return result
  }
}
