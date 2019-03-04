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

export default i18n