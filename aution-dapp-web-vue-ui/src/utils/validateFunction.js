/** 合法Url **/
export const validateURL = textval => {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}
/**
 * 验证邮箱
 * @param 邮箱地址
 * @returns {boolean}
 */
export const validateEmail = email => {
  const re = /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(email)
}

/*
 * 判断开始时间是否小于结束时间
 *时间无需转化格式直接使用element时间组件的中国标准时间即可
 *@param startTime 开始时间 endTime 结束时间   num相差多少天
 *@returns {boolean}
 */
export const validateDate = (startTime, endTime, num) => {
  const _startTime = new Date(startTime)
  const _endTime = new Date(endTime)
  const _diff = _endTime.getTime() - _startTime.getTime()
  if (_diff < 0) {
    return false
  } else {
    return Math.floor(_diff / (24 * 3600 * 1000)) >= num
  }
}

/**
 * 验证手机号
 * @param 手机号
 * @returns {boolean}
 */
export const validPhone = str => {
  const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
  return reg.test(str)
}

// 验证只能由数字字母_组成
export const validOne = str => {
  const reg = /^[0-9a-zA-Z_]{1,}$/
  return reg.test(str)
}

// 验证只能由数字字母.组成
export const validOne2 = str => {
  const reg = /^[0-9\.]{1,}$/
  return reg.test(str)
}

// 验证只能由中文数字字母_组成
export const validOne3 = str => {
  const reg = /^[\u4e00-\u9fa50-9a-zA-Z_]{1,}$/
  return reg.test(str)
}