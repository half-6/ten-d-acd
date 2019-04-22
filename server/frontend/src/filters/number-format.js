import numeral from 'numeral';
import Vue from "vue";
import _ from 'lodash'

function format(value,format) {
  if(typeof value == 'number'){
    return numeral(value).format(format);
  }
  return value;
}
Vue.filter('number-format', format)

export default {
  format,
  percent:_.partialRight(format,"0.[00]%"),
  number:_.partialRight(format,"0.[0000]"),
}
