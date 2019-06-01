import moment from 'moment';
import _ from 'lodash';
import Vue from "vue";
function format(value,format) {
  if(!value) return null;
  return moment(value).format(format);
}
Vue.filter('date-format', format)
export default {
  format:format,
  dateTime:_.partialRight(format,"YYYY-MM-DD HH:mm")
}