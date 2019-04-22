import Vue from "vue";
import VueI18n from 'vue-i18n'
import cn from './cn'
import en from './en'
import enElement from 'element-ui/lib/locale/lang/en'
import cnElement from 'element-ui/lib/locale/lang/zh-CN'
import ElementLocale from 'element-ui/lib/locale'

Vue.use(VueI18n);

const locale = getCurrentLocale();
const messages = {
     cn:{
         ...cn,
         ...cnElement
     }
    ,en:{
         ...en,
         ...enElement
    }
};
const i18n = new VueI18n({
    locale,
    messages
});
ElementLocale.i18n((key, value) => i18n.t(key, value))
function getCurrentLocale() {
    if(!localStorage.locale)
    {
        localStorage.locale = getBrowserLocale();
    }
    console.log(`current locale is ${localStorage.locale}`)
    return localStorage.locale;
}
function getBrowserLocale() {
    let browserLang = navigator.language;
    if(browserLang.indexOf("zh")>=0) return "cn";
    return "en";
}
function loadLanguageAsync() {

}
//this.$i18n.setLang('cn') call it on your page for switch the language
function setLang(lang){
    localStorage.locale = i18n.locale = lang;
    console.log(`switch locale to ${localStorage.locale}`)
}
i18n.loadLanguageAsync = loadLanguageAsync;
i18n.setLang = setLang;
export default i18n