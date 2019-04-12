<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "ultersound-diagnosis-server",
  methods:{
    setLang(lang){
      localStorage.locale = this.$i18n.locale = lang;
    }
  }
};
</script>
