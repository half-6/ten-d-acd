import Vue from "vue";
import VueI18n from 'vue-i18n'
import cn from './cn'
import en from './en'

Vue.use(VueI18n);

const locale = getCurrentLocale();
const messages = {
     cn
    ,en
};
const i18n = new VueI18n({
    /** 默认值 */
    locale,
    messages
});

function getCurrentLocale() {
    if(!localStorage.locale)
    {
        localStorage.locale = getBrowserLocale();
    }
    return localStorage.locale;
}
function getBrowserLocale() {
    let browserLang = navigator.language;
    if(browserLang.indexOf("zh")>=0) return "cn";
    return "en";
}
function loadLanguageAsync() {
    const cancerType = Vue.prototype.$cancerType;
    cancerType.forEach(item=>{
        messages.en.master["cancer-type-" + item.cancer_type_id] = item.cancer_type_name;
        messages.cn.master["cancer-type-" + item.cancer_type_id] = item.cancer_type_chinese_name;
    })
    const machineType = Vue.prototype.$machineType;
    machineType.forEach(item=>{
        messages.en.master["machine-type-" + item.machine_type_id] = item.machine_type_name;
        messages.cn.master["machine-type-" + item.machine_type_id] = item.machine_type_chinese_name;
    })
}
i18n.loadLanguageAsync = loadLanguageAsync;
export default i18n