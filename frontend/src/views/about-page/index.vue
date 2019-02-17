<template>
  <div class="wrapper about-page">
    <div class="container-fluid">
      <div class="row justify-content-center">
        <div class="col">
          <h1></h1>
          <div class="jumbotron">
            <h1 class="display-4">Ten-D ACD System </h1>
            <p class="lead clearfix">A web based automatic ultrasound cancer diagnosis system</p>
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
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="item in aggCancerTypeList">
                    <td scope="row">{{item.cancer_type_name}}</td>
                    <td scope="row">{{item.number_diagnostics}}</td>
                    <td scope="row">{{item.max}}</td>
                    <td scope="row">{{item.min}}</td>
                    <td scope="row">{{item.avg}}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <p class="pt-3">Version number:0.0.3-SNAPSHOT</p>
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
