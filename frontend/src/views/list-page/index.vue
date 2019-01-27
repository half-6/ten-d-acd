<template>
  <div class="wrapper list-page">
    <div class="container-fluid">
      <form class="row" v-on:submit.prevent="searchImage">
        <div class="col">
           <div class="float-md-left search-bar row">
             <div class="form-group col-md-6">
               <input type="text" class="form-control" v-model="search.id" placeholder="Search by ID">
             </div>
             <div class="form-group col-md-6">
               <loading-button v-on:click="searchImage" value="Search" :isLoading="isSearching" />
             </div>
           </div>
           <div class="float-md-right filter-bar row">
             <div class="form-group col-md-4">
               <select class="custom-select" v-model="search.machine_type_id">
                 <option selected value="">Select machine type</option>
                 <option v-for="item in machineTypeList" :value="item.machine_type_id">{{item.machine_type_name}}</option>
               </select>
             </div>
             <div class="form-group col-md-4">
               <select class="custom-select" v-model="search.cancer_type_id">
                 <option selected value="">Select cancer type</option>
                 <option v-for="item in cancerTypeList" :value="item.cancer_type_id">{{item.cancer_type_name}}</option>
               </select>
             </div>
             <div class="form-group col-md-4">
               <select class="custom-select" v-model="search.pathology">
                 <option selected value="">Select pathology</option>
                 <option v-for="item in pathologyList" :value="item.key">{{item.label}}</option>
               </select>
             </div>
           </div>
        </div>
      </form>
      <div class="row">
        <div class="col">
          <table class="table table-striped table-hover table-responsive-sm">
            <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Original Image</th>
              <th scope="col">Roi Image</th>
              <th scope="col">Cancer Type</th>
              <th scope="col">Machine Type</th>
              <th scope="col">Pathology</th>
              <th scope="col">Prediction</th>
              <th scope="col">Probability</th>
              <th scope="col">Date</th>
              <!--<th scope="col">Operation</th>-->
            </tr>
            </thead>
            <tbody>
              <tr v-for="item in roiImageList">
                <td scope="row">{{item.record_external_id}}</td>
                <td><img class="img-thumbnail" :src="getImageUrl(item.original_image)"/></td>
                <td><img class="img-thumbnail" :src="getImageUrl(item.roi_image)"/></td>
                <td>{{item.cancer_type_name}}</td>
                <td>{{item.machine_type_name}}</td>
                <td>{{item.pathology}}</td>
                <td>{{item.prediction}}</td>
                <td>{{item.probability}}</td>
                <td>{{item.date_registered}}</td>
                <!--<td>Edit</td>-->
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="row justify-content-between" v-show="this.roiImageList">
        <div class="">
          <select class="custom-select" v-model="page.limit" @change="searchImage">
            <option selected value="5">5 records per page</option>
            <option selected value="10">10 records per page</option>
            <option selected value="20">20 records per page</option>
            <option selected value="50">50 records per page</option>
          </select>
        </div>
        <div class="col">
          <b-pagination align="right" :total-rows="page.total_count" v-model="page.page_index"  @input="searchImage" :per-page="page.limit"></b-pagination>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
