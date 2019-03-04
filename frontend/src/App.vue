<template>
  <div id="app">
    <div class="container-fluid header">
      <div class="row bg-primary">
        <div class="col">
          <h1 class="text-center">{{$t('master.title')}}</h1>
          <ul class="nav">
            <li class="nav-item">
              <router-link to="/" class="nav-link active">{{$t('master.menu-home')}}</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/list" class="nav-link">{{$t('master.menu-list-view')}}</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/about" class="nav-link">{{$t('master.menu-about')}}</router-link>
            </li>
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
        </div>
      </div>
    </div>
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
