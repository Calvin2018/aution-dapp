/**
 * @author Xuan
 * @description Common Storage
 */
export const LocalStorage = {
  set(name, obj) {
    if (typeof obj === 'object') {
      localStorage.setItem(name, JSON.stringify(obj))
    } else {
      localStorage.setItem(name, obj)
    }
  },
  get(name) {
    const obj = localStorage.getItem(name)
    if (obj && (obj.startsWith('[') || obj.startsWith('{'))) {
      return JSON.parse(obj)
    } else {
      return obj
    }
  }
}
export const SessionStorage = {
  set(name, obj) {
    if (typeof obj === 'object') {
      sessionStorage.setItem(name, JSON.stringify(obj))
    } else {
      sessionStorage.setItem(name, obj)
    }
  },
  get(name) {
    const obj = sessionStorage.getItem(name)
    if (obj && (obj.startsWith('[') || obj.startsWith('{'))) {
      return JSON.parse(obj)
    } else {
      return obj
    }
  }
}
