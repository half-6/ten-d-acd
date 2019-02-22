<template>
  <div class="wrapper landing-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 left">
          <div class="btn-toolbar">
            <div class="btn-group" role="group" aria-label="First group">
              <input class="form-control" placeholder="Please enter ID" v-model="record.record_external_id">
              <select class="custom-select ml-1" v-model="record.cancer_type_id">
                <option selected value="">Cancer type</option>
                <option v-for="item in cancerTypeList" :value="item.cancer_type_id">{{item.cancer_type_name}}</option>
              </select>
              <select class="custom-select ml-1" v-model="record.machine_type_id">
                <option selected value="">Machine type</option>
                <option v-for="item in machineTypeList" :value="item.machine_type_id">{{item.machine_type_name}}</option>
              </select>
            </div>
          </div>
          <div class="img-container">
              <div class="img-rounded rounded fill">
                <div class="center text-center" v-if="imageList.length==0">Please click "Import Image" button to choose a b-model image</div>
                <img v-show="selectedImage" :src="selectedImage?selectedImage.src:''"  ref="selectedImg">
              </div>
           </div>
          <div class="container bottom">
            <div class="row">
              <div class="col">
                <ul class="nav nav-pills float-md-left">
                  <li class="nav-item">
                    <label class="btn btn-primary btn-file">
                      Import Image <input type="file" @change="openFile" class="h" multiple/>
                    </label>
                  </li>
                  <li class="nav-item">
                    <button class="btn btn-primary" :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" @click="crop()">Cut ROI Image</button>
                  </li>
                </ul>
                <div class="float-md-right">
                  <button data-target="#imageList" data-slide-to="0" class="btn btn-danger" v-b-modal.delModalSelectedImage :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" >Delete</button>
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
              <loading-button v-on:click="save" :disabled="!enableSaveButton()" value="Save record" :isLoading="isSaving" loadingLabel="Saving..." />
              <!--<loading-button v-on:click="reset" value="RESET" />-->
            </div>
          </div>
          <div class="img-container">
            <img v-if="cropImg" class="img-rounded rounded fill cropImg"  :src="cropImg.src">
            <div class="center text-center img-rounded rounded" v-else>Please click "Cut ROI Image" button on the bottom of the left image to cut an ROI image</div>
          </div>
          <div class="image-prediction container bottom">
            <div class="row">
              <div class="col">
                <div class="d-flex justify-content-between">
                  <div class="pb-2 d-flex align-items-end">
                    <loading-button v-on:click="recognition" :disabled="cropImg==null || cropImg.prediction!=null" value="Start detecting" :isLoading="isRecognition" loadingLabel="Start detecting..." />
                    <a :href="cropImg && cropImg.src" class="btn btn-primary m-l10" download="crop.png" :class="{disabled:!cropImg}">Export</a>
                  </div>
                  <div class="pb-2" v-if="cropImg">
                    <div v-for="(value, propertyName) in cropImg.prediction" class="prediction" >
                      <div>{{value  | number-format('0.[0000]') }}</div>
                      <div>{{propertyName}}</div>
                    </div>
                  </div>
                  <div class="pb-2 d-flex align-items-end">
                    <button data-target="#roiImageList" data-slide-to="0" class="btn btn-danger float-md-right" v-b-modal.delModalCroppedImage :class="{disabled:cropImg==null}" :disabled="cropImg==null" >Delete</button>
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

    <b-modal id="delModalSelectedImage" title="Confirm Deletion" @ok="delSelectedImage">
      <p class="my-4">Are you sure you want to permanently remove this item?</p>
    </b-modal>
    <b-modal id="delModalCroppedImage" title="Confirm Deletion" @ok="delCroppedImage">
      <p class="my-4">Are you sure you want to permanently remove this item?</p>
    </b-modal>
    <b-modal id="errModal" ref="errModal" title="Information" ok-only>
      <p class="my-4">{{message}}</p>
    </b-modal>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
