<template>
  <div class="wrapper about-page">
    <div class="container-fluid">
      <div class="row justify-content-center">
        <div class="col">
          <h1></h1>
          <div class="jumbotron">
            <h1><img class="tend-logo" :src="require('../../assets/i/tenDlogo.png')" > Ten-D ACD System </h1>
            <p class="lead clearfix">Powered by Ten-D Innovation's J-Eye Cancer Diagnosis Engine, and under the licensing agreement with the University of Buckingham</p>
            <hr class="my-4">
            <div class="row">
              <div class="col">
                <table class="table table-striped table-hover table-responsive-sm">
                  <thead>
                    <tr>
                    <th scope="col">Cancer Type</th>
                    <th scope="col"># of diagnostics</th>
                    <th scope="col">Lowest process time </th>
                    <th scope="col">Fastest process time</th>
                    <th scope="col">Average process time</th>
                    <th scope="col">TP</th>
                    <th scope="col">TN</th>
                    <th scope="col">FP</th>
                    <th scope="col">FN</th>
                    <th scope="col">Accuracy(T)</th>
                    <th scope="col">TPR(T)</th>
                    <th scope="col">TNR(T)</th>
                    <th scope="col">Precision(T)</th>
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
                      <th>Total</th>
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
            <p class="pt-3">Application Version number:{{$tendConfig.applicationVersion}}</p>
            <p>Recognition Version number:{{$tendConfig.recognitionVersion}}</p>
            <p class="power"><img :src="require('../../assets/i/logo.png')" >Powered by The University of Buckingham</p>
            <!--<a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a>-->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
