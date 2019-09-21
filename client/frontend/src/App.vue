<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top header bg-primary">
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <h1 class="text-center">{{$t('master.title')}}</h1>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item active">
            <router-link to="/" class="nav-link active" data-toggle="collapse" data-target="#navbarSupportedContent">{{$t('master.menu-home')}}</router-link>
          </li>
<!--          <li class="nav-item">-->
<!--            <router-link to="/list" class="nav-link" data-toggle="collapse" data-target="#navbarSupportedContent">{{$t('master.menu-list-view')}}</router-link>-->
<!--          </li>-->
<!--          <li class="nav-item">-->
<!--            <router-link to="/import" class="nav-link" data-toggle="collapse" data-target="#navbarSupportedContent">{{$t('master.menu-import')}}</router-link>-->
<!--          </li>-->
          <li class="nav-item">
            <router-link to="/about" class="nav-link" data-toggle="collapse" data-target="#navbarSupportedContent">{{$t('master.menu-about')}}</router-link>
          </li>
        </ul>
      </div>
      <ul class="navbar-nav language">
        <li class="nav-item">
          <el-dropdown trigger="click" @command="setLang">
                    <span class="el-dropdown-link">
                      {{$t('master.menu-language')}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="en" :disabled="$i18n.locale=='en'">{{$t('master.menu-language-english')}}</el-dropdown-item>
              <el-dropdown-item command="cn" :disabled="$i18n.locale=='cn'">{{$t('master.menu-language-chinese')}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "ultersound-diagnosis",
  methods:{
    setLang(lang){
      localStorage.locale = this.$i18n.locale = lang;
    }
  }
};
</script>
