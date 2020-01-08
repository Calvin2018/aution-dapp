import store from '@/store/store'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPerm(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.permissions
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })

    if (!hasPermission) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like v-if="checkPerm['op_editor']"`)
    return false
  }
}
