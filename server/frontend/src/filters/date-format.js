import moment from 'moment';
import Vue from "vue";
import _ from "lodash";
function format(value,format) {
  return moment(value).format(format);
}
Vue.filter('date-format', format)
export default {
  format,
  dateTime:_.partialRight(format,"YYYY-MM-DD HH:mm")
}