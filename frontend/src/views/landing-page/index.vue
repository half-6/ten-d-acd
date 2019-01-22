<template>
  <div class="wrapper landing-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 left">
          <div class="btn-toolbar">
            <div class="btn-group" role="group" aria-label="First group">
              <input class="form-control" placeholder="Please enter ID">
              <select class="custom-select ml-1">
                <option selected>Please select cancer type</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
              <select class="custom-select ml-1">
                <option selected>Please select machine type</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
            </div>
          </div>
          <div class="img-container">
              <div class="img-rounded rounded fill">
                <div class="center text-center" v-if="imageList.length==0">Please click "Import Image" button to choose a b-model image</div>
                <img v-show="selectedImage" :src="selectedImage"  ref="selectedImg">
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
                  <button data-target="#imageList" data-slide-to="0" class="btn btn-danger" @click="delSelectedImage()" :class="{disabled:selectedImage==null}" :disabled="selectedImage==null" >Delete</button>
                </div>
              </div>
            </div>
            <div class="row">
              <image-list-slider id="imageList" class="col" :imageList="imageList" :selectedImage="selectedImage" v-on:select-image="onSelectFile"/>
            </div>
          </div>
        </div>
        <div class="col-md-6 right line">
          <!--<h2>Extracted Region</h2>-->
          <div class="btn-toolbar">
            <div class="btn-group float-md-left">
              <div class="diagnostic-type">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                  <label class="btn btn-secondary">
                    <input type="radio" name="options" autocomplete="off" checked> Malignant
                  </label>
                  <label class="btn btn-secondary">
                    <input type="radio" name="options" autocomplete="off"> Benign
                  </label>
                </div>
                <!--<div class="type">-->
                  <!--Type-->
                <!--</div>-->
                <!--<div class="custom-control custom-radio custom-control-inline">-->
                  <!--<input type="radio" id="customRadioInline1" name="customRadioInline1" class="custom-control-input">-->
                  <!--<label class="custom-control-label" for="customRadioInline1">Malignant</label>-->
                <!--</div>-->
                <!--<div class="custom-control custom-radio custom-control-inline">-->
                  <!--<input type="radio" id="customRadioInline2" name="customRadioInline1" class="custom-control-input">-->
                  <!--<label class="custom-control-label" for="customRadioInline2">Benign</label>-->
                <!--</div>-->
              </div>
            </div>
            <div class="btn-group float-md-right">
              <button class="btn btn-primary btn-save" :class="{disabled:croppedImageList.length == 0}" :disabled="croppedImageList.length == 0" >SAVE</button>
            </div>
          </div>
          <div class="img-container">
            <!--<div v-if="cropImg" class="img-rounded rounded fill cropImg" :style="{ 'background-image': 'url(' + cropImg + ')' }"></div>-->
            <img v-if="cropImg" class="img-rounded rounded fill cropImg"  :src="cropImg">
            <div class="center text-center img-rounded rounded" v-else>Please click "Cut ROI Image" button on the bottom of the left image to cut an ROI image</div>
          </div>
          <div class="image-prediction container bottom">
            <div class="row prediction">
              <div class="col-md-2">
                <div>tabby</div>
                <div>Prediction</div>
              </div>
              <div class="col-md-2">
                <div>0.6128</div>
                <div>Probability</div>
              </div>
              <div class="col-md-2">
                <div>0.17188</div>
                <div>Processing Time</div>
              </div>
              <div class="col-md-6">
                <button data-target="#roiImageList" data-slide-to="0" class="btn btn-danger float-md-right" @click="delCroppedImage()" :class="{disabled:cropImg==null}" :disabled="cropImg==null" >Delete</button>
                <button class="btn btn-primary float-md-right" @click="recognition()" :class="{disabled:cropImg==null}" :disabled="cropImg==null" >Recognition</button>

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
