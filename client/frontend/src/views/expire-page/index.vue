<template>
  <div class="wrapper expire-page">
    <div class="container-fluid">
      <div class="row justify-content-center">
        <div class="col">
          <h1></h1>
          <div class="jumbotron">
            <h1><img class="tend-logo" :src="require('../../assets/i/tenDlogo.png')" >{{$t('expire.title')}}</h1>
            <p class="lead clearfix">{{$t('expire.desc')}}</p>
            <hr class="my-4">
            <p class="pt-3">{{$t('expire.expire')}}</p>
            <p>{{$t('about.expire-on')}}:{{new Date($tendConfig.certificateEntity.expired_time)}}</p>
            <p class="power"><img :src="require('../../assets/i/logo.png')" >{{$t('expire.power-by')}}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
