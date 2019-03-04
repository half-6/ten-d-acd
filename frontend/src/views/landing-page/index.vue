<template>
  <div class="wrapper landing-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 left">
          <div class="btn-toolbar">
            <div class="btn-group" role="group" aria-label="First group">
              <input class="form-control" :placeholder="$t('home.enter-id')" v-model="record.record_external_id">
              <select class="custom-select ml-1" v-model="record.cancer_type">
                <option selected value="">{{ $t("home.cancer-type") }}</option>
                <option v-for="item in cancerTypeList" :value="item">{{item.cancer_type_name}}</option>
              </select>
              <select class="custom-select ml-1" v-model="record.machine_type_id">
                <option selected value="">{{ $t("home.machine-type") }}</option>
                <option v-for="item in machineTypeList" :value="item.machine_type_id">{{item.machine_type_name}}</option>
              </select>
            </div>
          </div>
          <div class="img-container">
              <div class="img-rounded rounded fill">
                <div class="center text-center" v-if="imageList.length==0">{{ $t("home.roi-import-desc") }}</div>
                <img v-show="selectedImage!=null" :src="selectedImage?selectedImage.src:''"  ref="selectedImg">
              </div>
           </div>
          <div class="container bottom">
            <div class="row">
              <div class="col">
                <ul class="nav nav-pills float-md-left">
                  <li class="nav-item">
                    <label class="btn btn-primary btn-file">
                      {{ $t("home.button-import") }} <input type="file" @change="openFile" class="h" multiple/>
                    </label>
                  </li>
                  <li class="nav-item">
                    <button class="btn btn-primary" :class="{disabled:selectedImage==null}" :disabled="selectedImage==null || record.cancer_type==''" @click="crop()">{{ $t("home.button-cut-roi-image") }}</button>
                  </li>
                </ul>
                <div class="float-md-right">
                  <button @click="delSelectedImage" data-slide-to="0" class="btn btn-danger" :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" >{{ $t("home.button-delete") }}</button>
                </div>
              </div>
            </div>
            <div class="row">
              <image-list-slider id="imageList" class="col" :imageList="imageList" :selectedImage="selectedImage" v-on:select-image="onSelectFile"/>
            </div>
          </div>
        </div>
        <div class="col-md-6 right line">
          <div class="btn-toolbar">
            <div class="btn-group float-md-left">
              <div class="diagnostic-type" v-if="cropImg">
                <b-form-radio-group buttons
                                    button-variant="outline-primary"
                                    v-model="cropImg.pathology"
                                    :options="pathologyList" />
              </div>
            </div>
            <div class="btn-group float-md-right">
              <loading-button v-on:click="save" :disabled="!enableSaveButton()" :value="$t('home.button-save')" :isLoading="isSaving" :loadingLabel="$t('home.button-saving')" />
            </div>
          </div>
          <div class="img-container">
            <img v-if="cropImg" class="img-rounded rounded fill cropImg"  :src="cropImg.src">
            <div class="center text-center img-rounded rounded" v-else>{{ $t("home.roi-cut-desc") }}</div>
          </div>
          <div class="image-prediction container bottom">
            <div class="row">
              <div class="col">
                <div class="d-flex justify-content-between">
                  <div class="pb-2 d-flex align-items-end">
                    <loading-button v-on:click="recognition" :disabled="cropImg==null || cropImg.prediction!=null || record.cancer_type==''" :value="$t('home.button-detect')" :isLoading="isRecognition" :loadingLabel="$t('home.button-detect-loading')" />
                    <a :href="cropImg && cropImg.src" class="btn btn-primary m-l10" download="crop.png" :class="{disabled:!cropImg}">{{$t('home.button-export')}}</a>
                  </div>
                  <div class="pb-2" v-if="cropImg">
                    <div v-for="(value, propertyName) in cropImg.prediction" class="prediction" >
                      <div>{{value  | number-format('0.[0000]') }}</div>
                      <div>{{$t('home.result-' + propertyName)}}</div>
                    </div>
                  </div>
                  <div class="pb-2 d-flex align-items-end">
                    <button @click="delCroppedImage" data-slide-to="0" class="btn btn-danger float-md-right" :class="{disabled:cropImg==null}" :disabled="cropImg==null" >{{ $t("home.button-delete") }}</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <image-list-slider id="roiImageList"  class="col" :imageList="croppedImageList" :selectedImage="cropImg"  v-on:select-image="onSelectCroppedImage"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
