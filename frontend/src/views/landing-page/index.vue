<template>
  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 left">
          <ul class="nav nav-pills justify-content-end">
            <li class="nav-item">
              <label class="btn btn-primary btn-file">
                Auto Import <input type="file" @change="openFile" class="h" multiple/>
              </label>
            </li>
            <li class="nav-item">
              <button class="btn btn-primary" @click="crop()">Crop</button>
            </li>
          </ul>
          <div class="img-container">
              <div class="img-rounded rounded fill">
                <img v-show="selectedImage && selectedImage!=defaultImagePath" :src="selectedImage"  ref="selectedImg">
              </div>
           </div>
          <div class="img-list-container container bottom">
            <div class="row">
              <a class="col fill" v-for="(item, index) in imageList1" :key="index" @click="onSelectFile(item)">
                <img :src="item">
              </a>
            </div>
            <div class="row">
              <a class="col fill" v-for="(item, index) in imageList2" :key="index"  @click="onSelectFile(item)">
                <img :src="item">
              </a>
            </div>
            <div class="row">
              <div class="col">
                <nav aria-label="Page navigation">
                  <ul class="pagination justify-content-end" v-if="pager && pager.pageSize>0">
                    <li class="page-item"><a class="page-link" @click="buildGallery(0)">Previous</a></li>
                    <li class="page-item" v-for="index in pager.pageSize" :key="index" :class="[{'active':index == pager.pageIndex + 1}]"><a class="page-link" @click="buildGallery(index -1)">{{index}}</a></li>
                    <li class="page-item"><a class="page-link" @click="buildGallery(pager.pageSize - 1)">Next</a></li>
                  </ul>
                </nav>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6 right line">
          <h2>Extracted Region</h2>
          <div class="img-container">
            <div class="img-rounded rounded fill">
                <img :src="cropImg" v-if="cropImg">
            </div>
          </div>
          <div class="image-prediction container bottom">
            <div class="row">
              <div class="col">
                <h2>Automatic Prediction</h2>
              </div>
            </div>
            <div class="row prediction">
              <div class="col">
                <div>--</div>
                <div>Type</div>
              </div>
              <div class="col">
                <div>--</div>
                <div>Probability</div>
              </div>
              <div class="col">
                <div>--</div>
                <div>Computation Time</div>
              </div>
            </div>
            <h2 class="row col diagnostic p-t15">
                Doctor Diagnostic
            </h2>
            <div class="row col diagnostic-type p-t15">
              <div class="type">
                Type
              </div>
              <div class="custom-control custom-radio custom-control-inline">
                <input type="radio" id="customRadioInline1" name="customRadioInline1" class="custom-control-input">
                <label class="custom-control-label" for="customRadioInline1">Malignant</label>
              </div>
              <div class="custom-control custom-radio custom-control-inline">
                <input type="radio" id="customRadioInline2" name="customRadioInline1" class="custom-control-input">
                <label class="custom-control-label" for="customRadioInline2">Benign</label>
              </div>
            </div>
            <div class="row p-t20">
              <div class="col">
                <button class="btn btn-primary btn-save">SAVE</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style src="./index.css">
</style>
<script src="./index.js">
</script>
