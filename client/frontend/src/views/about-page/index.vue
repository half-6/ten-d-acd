<template>
  <div class="wrapper about-page">
    <div class="container-fluid">
      <div class="row justify-content-center">
        <div class="col">
          <h1></h1>
          <div class="jumbotron">
            <h1><img class="tend-logo" :src="require('../../assets/i/tenDlogo.png')" >{{$t('about.title')}}</h1>
            <p class="lead clearfix">{{$t('about.desc')}}</p>
            <hr class="my-4">
<!--            <p class="pt-3">{{$t('about.app-version')}}:{{$tendConfig.applicationVersion}}</p>-->
<!--            <p>{{$t('about.recognition-version')}}:{{$tendConfig.recognitionVersion}}</p>-->
            <p class="pt-3">{{$t('about.app-version')}}:1.0.0</p>
            <p>{{$t('about.recognition-version')}}:1.0.0</p>
            <p>{{$t('about.expire-on')}}:{{new Date($tendConfig.certificateEntity.expire_date)}}</p>
            <p class="power"><img :src="require('../../assets/i/logo.png')" >{{$t('about.power-by')}}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
