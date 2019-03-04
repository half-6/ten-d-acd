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
            <div class="row">
              <div class="col">
                <table class="table table-striped table-hover table-responsive-sm">
                  <thead>
                    <tr>
                    <th scope="col">{{$t('about.table-cancer-type')}}</th>
                    <th scope="col">{{$t('about.table-diagnostics')}}</th>
                    <th scope="col">{{$t('about.table-lowest-process-time')}}</th>
                    <th scope="col">{{$t('about.table-fastest-process-time')}}</th>
                    <th scope="col">{{$t('about.table-average-process-time')}}</th>
                    <th scope="col">{{$t('about.table-tp')}}</th>
                    <th scope="col">{{$t('about.table-tn')}}</th>
                    <th scope="col">{{$t('about.table-fp')}}</th>
                    <th scope="col">{{$t('about.table-fn')}}</th>
                    <th scope="col">{{$t('about.table-accuracy')}}</th>
                    <th scope="col">{{$t('about.table-tpr')}}</th>
                    <th scope="col">{{$t('about.table-tnr')}}</th>
                    <th scope="col">{{$t('about.table-precision')}}</th>
                  </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in aggCancerTypeList">
                      <td scope="row">{{item.cancer_type_name}}</td>
                      <td scope="row">{{item.number_diagnostics| number-format('0,0')}}</td>
                      <td scope="row">{{item.max | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.min | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.avg | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.tp | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.tn | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.fp | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.fn | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.accuracy | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.tpr | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.tnr | number-format('0.[0000]')}}</td>
                      <td scope="row">{{item.precision | number-format('0.[0000]')}}</td>
                    </tr>
                  </tbody>
                  <tfoot>
                    <tr>
                      <th>{{$t('about.table-total')}}</th>
                      <th>{{totalDiagnostics | number-format('0,0')}}</th>
                      <th></th>
                      <th></th>
                      <th></th>
                      <th>{{totalTP | number-format('0,0')}}</th>
                      <th>{{totalTN | number-format('0,0')}}</th>
                      <th>{{totalFP | number-format('0,0')}}</th>
                      <th>{{totalFN | number-format('0,0')}}</th>
                      <th>{{accuracy | number-format('0.[0000]')}}</th>
                      <th>{{tpr | number-format('0.[0000]')}}</th>
                      <th>{{tnr | number-format('0.[0000]')}}</th>
                      <th>{{precision | number-format('0.[0000]')}}</th>
                    </tr>
                  </tfoot>
                </table>

              </div>
            </div>
            <p class="pt-3">{{$t('about.app-versiontable-app-version')}}:{{$tendConfig.applicationVersion}}</p>
            <p>{{$t('about.recognition-version')}}:{{$tendConfig.recognitionVersion}}</p>
            <p class="power"><img :src="require('../../assets/i/logo.png')" >{{$t('about.power-by')}}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
