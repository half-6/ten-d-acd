<template>
  <div class="wrapper import-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col">
           <div class="search-bar row">
             <div class="form-group col-lg-2 col-md col-sm">
               <select class="custom-select" v-model="cancerType">
                 <option selected value="">{{ $t('import.cancer-type') }}</option>
                 <option v-for="item in cancerTypeList" :value="item">{{$t('master.cancer-type-' + item.cancer_type_id)}}</option>
               </select>
             </div>
             <div class="form-group col-lg-2">
               <file-uploader :disabled="enableImport()"  :loadingLabel="$t('import.button-importing')" :isLoading="isLoading" class="btn btn-primary btn-file" :title="$t('import.button-import')" @change="openFile"></file-uploader>
               <!--<loading-button v-on:click="importImage" :value="$t('import.button-import')" :isLoading="isLoading" :loadingLabel="$t('import.button-importing')" />-->
             </div>
           </div>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <table class="table table-striped table-hover table-responsive-sm">
            <thead>
            <tr>
              <th scope="col">{{$t('import.table-image-name')}}</th>
              <th scope="col">{{$t('import.table-roi-image')}}</th>
              <th scope="col">{{$t('import.table-cancer-type')}}</th>
              <th scope="col">{{$t('import.table-prediction')}}</th>
              <th scope="col">{{$t('import.table-probability')}}</th>
            </tr>
            </thead>
            <tbody>
              <tr v-for="item in roiImageList">
                <td scope="row">{{item.name}}</td>
                <td>
                  <div v-loading="item.loading" class="img-thumbnail">
                    <a :href="item.src" target="_blank"><img :src="item.src"/></a>
                  </div>
                </td>
                <td>{{$t('master.cancer-type-' + item.cancer_type_id)}} </td>
                <td><span v-if="item.prediction!==''">{{$t('master.pathology-' + item.prediction)}}</span></td>
                <td><span v-if="item.probability!==''">{{item.probability | number-format('0.[00]%')}}</span></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
