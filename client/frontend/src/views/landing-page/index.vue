<template>
  <div class="wrapper landing-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 left">
          <div class="btn-toolbar">
            <div class="btn-group" role="group">
              <input class="form-control" :placeholder="$t('home.enter-id')" v-model="record.record_external_id">
              <select class="custom-select ml-1" v-model="record.cancer_type">
                <option selected value="">{{ $t("home.cancer-type") }}</option>
                <option v-for="item in cancerTypeList" :value="item">{{$t('master.cancer-type-' + item.cancer_type_id)}}</option>
              </select>
              <select class="custom-select ml-1" v-model="record.machine_type_id">
                <option selected value="">{{ $t("home.machine-type") }}</option>
                <option v-for="item in machineTypeList" :value="item.machine_type_id">{{$t('master.machine-type-' + item.machine_type_id)}}</option>
              </select>
            </div>
            <el-tag v-if="selectedImage" class="float-md-right m-t3 img-name">{{selectedImage.name}}</el-tag>
          </div>
          <div class="img-container">
              <div class="img-rounded rounded fill">
                <el-upload v-if="imageList.length===0"
                        class="f-wh"
                        action="http://www.google.com"
                        :show-file-list="false"
                        drag
                        :multiple="true"
                        :before-upload="beforeAvatarUpload"
                        :on-change="handleChange"
                  >
                  <div class="table-center text-center"><div>{{ $t("home.roi-import-desc") }}</div></div>
                </el-upload>
                <canvas v-show="imageList.length>0" id="canvas" resize></canvas>
              </div>
           </div>
          <div class="container bottom">
            <div class="row">
              <div class="col btn-toolbar">
                <ul class="nav nav-pills float-md-left">
                  <li class="nav-item">
                    <file-uploader class="btn btn-primary btn-file" :title="$t('home.button-import')" @change="openFile"></file-uploader>
                  </li>
                  <li class="nav-item">
                    <button :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" class="btn btn-primary btn-file" @click="startPen">{{$t('home.button-Pen')}}</button>
                  </li>
                  <li class="nav-item">
                    <loading-button v-on:click="crop" :disabled="selectedImage==null || !record.cancer_type" :value="$t('home.button-cut-roi-image')" :isLoading="isDetecting" :loadingLabel="$t('home.button-cut-roi-image-loading')" />
                  </li>
                </ul>
                <div class="float-md-right">
                  <loading-button v-on:click="save" :disabled="!enableSaveButton()" :value="$t('home.button-save')" :isLoading="isSaving" :loadingLabel="$t('home.button-saving')" />
                  <button @click="delSelectedImage" data-slide-to="0" class="btn btn-danger m-l10" :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" >{{ $t("home.button-delete") }}</button>
                </div>
              </div>
            </div>
            <div class="row">
              <image-list-slider id="imageList" class="col" :imageList="imageList" :selectedImage="selectedImage" v-on:select-image="onSelectFile"/>
            </div>
          </div>
        </div>
        <div class="col-md-6 right line">
<!--          <div class="btn-toolbar">-->
<!--            <div class="btn-group float-md-left">-->
<!--              <div class="diagnostic-type" v-if="cropImg">-->
<!--                <span class="mr-2">{{$t('home.label-pathology')}}:</span>-->
<!--                <el-radio-group v-model="cropImg.pathology">-->
<!--                  <el-radio-button v-for="item in pathologyList" :key="item.text" :label="item.text">{{$t('master.pathology-' + item.text)}}</el-radio-button>-->
<!--                </el-radio-group>-->
<!--              </div>-->
<!--            </div>-->
<!--            <div class="btn-group float-md-right">-->
<!--              <loading-button v-on:click="save" :disabled="!enableSaveButton()" :value="$t('home.button-save')" :isLoading="isSaving" :loadingLabel="$t('home.button-saving')" />-->
<!--            </div>-->
<!--          </div>-->
          <div class="img-container" :class="{'active':cropImg!=null}">
            <div class="detection-detail" v-if="cropImg">
                 <div class="row title-row">
                   <div class="col">
                     <b>{{$t('home.result-roi')}}</b>
                     <ul v-if="cropImg.prediction">
                         <li>{{$t('home.result-echos')}}: {{$t('home.result-echo-low')}}{{cropImg.prediction.Echos[0] | number-format('0.[00]')}} {{$t('home.result-echo-medium')}}{{cropImg.prediction.Echos[1] | number-format('0.[00]')}} {{$t('home.result-echo-high')}}{{cropImg.prediction.Echos[2] | number-format('0.[00]')}}</li>
                         <li>{{$t(('home.result-' + cropImg.prediction.Echo_Label).replace(" ","-"))}}</li>
                     </ul>
                   </div>
                   <div class="col">
                     <b>{{$t('home.result-Shape')}}</b>
                     <ul v-if="cropImg.prediction">
<!--                       <li v-if="cropImg.prediction.Shape_Ratio>1">{{$t('home.result-Taller')}}</li>-->
<!--                       <li v-if="cropImg.prediction.Shape_Ratio<1">{{$t('home.result-Wider')}}</li>-->
                       <li>{{$t('home.result-Ratio')}}: {{$t('home.result-' + cropImg.prediction.Shape_Ratio.split(" ")[0] )}}</li>
                     </ul>
                   </div>
                 </div>
                 <div class="row image-row">
                   <div class="col">
                     <img class="img-rounded rounded fill cropImg roi"  :src="cropImg.src">
                   </div>
                   <div class="col" v-loading="isDetecting==true">
                     <img class="img-rounded rounded fill cropImg sharp"  :src="cropImg.sharpSrc">
                   </div>
                 </div>
              <div class="row title-row">
                <div class="col">
                  <b>{{$t('home.result-Calcification-title')}}</b>
                  <ul v-if="cropImg.prediction">
                    <li v-if="cropImg.prediction.Calcification_index>0">{{$t('home.result-Calcification')}}</li>
                    <li v-if="cropImg.prediction.Calcification_index===0">{{$t('home.result-No-Calcification')}}</li>
                    <li>{{$t('home.result-Amount')}}: {{cropImg.prediction.Calcification_index}}%</li>
                  </ul>
                </div>
                <div class="col">
                  <b>{{$t('home.result-Margin')}}</b>
                  <ul>
                    <li v-if="cropImg.prediction">{{$t('home.result-Irregularity')}}: {{cropImg.prediction.Irregularity_Ratio}}%</li>
                    <li v-if="cropImg.prediction">{{$t('home.result-Smoothness')}}: {{cropImg.prediction.Smoothness_Ratio }}%</li>
                  </ul>
                </div>
              </div>
              <div class="row image-row">
                <div class="col" v-loading="isDetecting==true">
                    <img class="img-rounded rounded fill cropImg calc"  :src="cropImg.calcSrc">
                </div>
                <div class="col" v-loading="isDetecting==true">
                    <img class="img-rounded rounded fill cropImg sharp"  :src="cropImg.marginSrc">
                </div>
              </div>
            </div>
            <div class="table-center text-center img-rounded rounded" v-else><div>{{ $t("home.roi-cut-desc") }}</div></div>
          </div>
          <div class="image-prediction container bottom">
            <div class="row">
              <div class="col">
                <div class="d-flex justify-content-center">
                  <div class="pb-2" v-if="cropImg && cropImg.prediction">
                    <div class="prediction">
<!--                      <div class="p-title">{{$t('home.result-inspection')}}:{{$t('master.' + $prediction_format(cropImg.prediction["Prediction"],cropImg.prediction["Probability"]))}}</div>-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
