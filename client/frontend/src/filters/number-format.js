import numeral from 'numeral';
import Vue from "vue";
Vue.filter('number-format', function (value,format) {
  if(typeof value == 'number'){
    return numeral(value).format(format);
  }
  return value;
})