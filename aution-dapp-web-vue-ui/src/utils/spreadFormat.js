import GC from '@grapecity/spread-sheets'
import moment from 'moment'
export function UnixToDateTime() {}
export function CustomNumber() {}
UnixToDateTime.prototype = new GC.Spread.Formatter.FormatterBase()
UnixToDateTime.prototype.format = function(obj, formattedData) {
  return moment(obj).format('YYYY-MM-DD HH:mm:ss')
}
CustomNumber.prototype = new GC.Spread.Formatter.FormatterBase()
CustomNumber.prototype.format = function(obj, formattedData) {}
