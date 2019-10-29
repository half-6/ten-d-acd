<template>
  <div class="wrapper list-page">
    <div class="container-fluid">
      <form class="row" v-on:submit.prevent="searchImage">
        <div class="col">
           <div class="search-bar row">
             <div class="form-group col-lg-2 col-md col-sm">
               <select class="custom-select" v-model="search.machine_type_id">
                 <option selected value="">{{ $t('list.machine-type') }}</option>
                 <option v-for="item in machineTypeList" :value="item.machine_type_id">{{$t('master.machine-type-' + item.machine_type_id)}}</option>
               </select>
             </div>
             <div class="form-group col-lg-2 col-md col-sm">
               <select class="custom-select" v-model="search.cancer_type_id">
                 <option selected value="">{{ $t('list.cancer-type') }}</option>
                 <option v-for="item in cancerTypeList" :value="item.cancer_type_id">{{$t('master.cancer-type-' + item.cancer_type_id)}}</option>
               </select>
             </div>
<!--             <div class="form-group col-lg-2 col-md col-sm">-->
<!--               <select class="custom-select" v-model="search.pathology">-->
<!--                 <option selected value="">{{ $t('list.pathology') }}</option>-->
<!--                 <option selected value="null">{{ $t('master.pathology-null')}}</option>-->
<!--                 <option v-for="item in pathologyList" :value="item.value">{{ $t('master.pathology-' + item.value)}}</option>-->
<!--               </select>-->
<!--             </div>-->
<!--             <div class="form-group col-lg-2 col-md col-sm">-->
<!--               <select class="custom-select" v-model="search.prediction">-->
<!--                 <option selected value="">{{ $t('list.prediction') }}</option>-->
<!--                 <option v-for="item in predictionList" :value="item.value">{{ $t('master.pathology-' + item.value)}}</option>-->
<!--               </select>-->
<!--             </div>-->
             <div class="form-group col-lg-2 col-md col-sm">
               <input type="text" class="form-control" v-model="search.id" :placeholder="$t('list.keyword-placeholder')">
             </div>
             <div class="form-group col-lg-2">
               <loading-button v-on:click="searchImage" :value="$t('list.button-search')" :isLoading="isSearching" :loadingLabel="$t('list.button-searching')" />
<!--               <loading-button v-on:click="download" :value="$t('list.button-download')" class="float-right"  />-->
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
              <th scope="col">{{$t('list.table-original-image')}}</th>
              <th scope="col">{{$t('list.table-roi-image')}}</th>
              <th scope="col">{{$t('list.table-cancer-type')}}</th>
              <th scope="col">{{$t('list.table-machine-type')}}</th>
<!--              <th scope="col">{{$t('list.table-pathology')}}</th>-->
<!--              <th scope="col">{{$t('list.table-prediction')}}</th>-->
<!--              <th scope="col">{{$t('list.table-probability')}}</th>-->
              <th scope="col">{{$t('list.table-date')}}</th>
              <th scope="col">{{$t('list.table-operation')}}</th>
            </tr>
            </thead>
            <tbody>
              <tr v-for="item in roiImageList">
                <td scope="row">{{item.record_external_id}}</td>
                <td><a :href="getImageUrl(item.original_image)" target="_blank"><img class="img-thumbnail" :src="getImageUrl(item.original_image)"/></a></td>
                <td><a :href="getImageUrl(item.roi_image)" target="_blank"><img class="img-thumbnail" :src="getImageUrl(item.roi_image)"/></a></td>
                <td>{{$t('master.cancer-type-' + item.cancer_type_id)}} </td>
                <td v-if="!item.$edit">{{$t('master.machine-type-' + item.machine_type_id)}}</td>
                <td v-else>
                  <select class="custom-select" v-model="item.machine_type_id">
                    <option v-for="item in machineTypeList" :value="item.machine_type_id">{{$t('master.machine-type-' + item.machine_type_id)}}</option>
                  </select>
                </td>
<!--                <td v-if="!item.$edit">{{item.pathology?$t('master.pathology-' + item.pathology):''}}</td>-->
<!--                <td v-else>-->
<!--                  <el-radio-group v-model="item.pathology">-->
<!--                    <el-radio-button v-for="item in pathologyList" :key="item.text" :label="item.text">{{$t('master.pathology-' + item.text)}}</el-radio-button>-->
<!--                  </el-radio-group>-->
<!--                </td>-->
<!--                <td>{{$t('master.pathology-' + item.prediction)}}</td>-->
<!--                <td>{{item.probability | number-format('0.[00]%')}}</td>-->
                <td>{{item.date_registered | date-format('YYYY-MM-DD HH:mm')}}</td>
                <td class="operation">
                  <button v-if="item.detection_result" @click="detail(item)" type="button" class="btn btn-outline btn-primary btn-sm pl-1"><i class="fa fa-edit fa-fw"></i>{{$t('list.button-detail')}}</button>
<!--                  <button v-if="!item.$edit" @click="edit(item)" type="button" class="btn btn-outline btn-primary btn-sm pl-1"><i class="fa fa-edit fa-fw"></i>{{$t('list.button-edit')}}</button>-->
<!--                  <button v-else @click="save(item)" type="button" class="btn btn-outline btn-primary btn-sm pl-1"><i class="fa fa-edit fa-fw"></i>{{$t('list.button-save')}}</button>-->
                  <button v-if="!item.$edit" @click="del(item)" type="button" class="btn btn-outline btn-danger btn-sm pl-1"><i class="fa fa-edit fa-fw"></i>{{$t('list.button-delete')}}</button>
                  <button v-else @click="cancel(item)" type="button" class="btn btn-outline btn-secondary btn-sm pl-1"><i class="fa fa-edit fa-fw"></i>{{$t('list.button-cancel')}}</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="row justify-content-between" v-show="this.roiImageList">
        <div>
          <select class="custom-select" v-model="page.limit" @change="searchImage">
            <option selected value="5">{{$t('pagination.per-page',{num:5})}}</option>
            <option selected value="10">{{$t('pagination.per-page',{num:10})}}</option>
            <option selected value="20">{{$t('pagination.per-page',{num:20})}}</option>
            <option selected value="50">{{$t('pagination.per-page',{num:50})}}</option>
          </select>
        </div>
        <div>{{$t('pagination.total',{total:page.total_count})}}</div>
        <div>
          <b-pagination align="right" :total-rows="page.total_count" v-model="page.page_index"  @input="searchImage" :per-page="page.limit"></b-pagination>
        </div>
      </div>
    </div>
    <el-dialog
            :title="$t('list.table-report')"
            :visible.sync="showDetailDialog"
            width="50%"
            center>
      <div class="roi-detail" v-if="selectedRoiImage && selectedRoiImage.detection_result">
          <b>{{$t('home.result-roi')}}</b>
          <ul>
            <li>{{$t('home.result-echos')}}: {{$t('home.result-echo-low')}}{{selectedRoiImage.detection_result.Echos[0] | number-format('0.[00]')}} {{$t('home.result-echo-medium')}}{{selectedRoiImage.detection_result.Echos[1] | number-format('0.[00]')}} {{$t('home.result-echo-high')}}{{selectedRoiImage.detection_result.Echos[2] | number-format('0.[00]')}}</li>
            <li>{{$t(('home.result-' + selectedRoiImage.detection_result.Echo_Label).replace(" ","-"))}}</li>
          </ul>
          <b>{{$t('home.result-Shape')}}</b>
          <ul>
            <li>{{$t('home.result-Ratio')}}: {{$t('home.result-' + selectedRoiImage.detection_result.Shape_Ratio.split(" ")[0] )}}</li>
          </ul>
          <b>{{$t('home.result-Calcification-title')}}</b>
          <ul>
            <li v-if="selectedRoiImage.detection_result.Calcification_index>0">{{$t('home.result-Calcification')}}</li>
            <li v-if="selectedRoiImage.detection_result.Calcification_index===0">{{$t('home.result-No-Calcification')}}</li>
            <li>{{$t('home.result-Amount')}}: {{selectedRoiImage.detection_result.Calcification_index}}%</li>
          </ul>
          <b>{{$t('home.result-Margin')}}</b>
          <ul>
            <li v-if="selectedRoiImage.detection_result">{{$t('home.result-Irregularity')}}: {{selectedRoiImage.detection_result.Irregularity_Ratio}}%</li>
            <li v-if="selectedRoiImage.detection_result">{{$t('home.result-Smoothness')}}: {{selectedRoiImage.detection_result.Smoothness_Ratio }}%</li>
          </ul>
      </div>
    </el-dialog>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
