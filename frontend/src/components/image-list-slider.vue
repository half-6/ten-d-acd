<template>
  <div :id="id" class="carousel slide carouselSlider" data-ride="carousel" data-interval="false" data-wrap="false">
    <div class="carousel-inner" v-if="imageList.length>0">
      <div class="carousel-item" v-for="i in Math.ceil(imageList.length / 5)" :class="{active:i==1}">
        <div class="carousel-item-image w-20" v-for="item in imageList.slice((i - 1) * 5, i * 5)" :class="{active:selectedImage==item}" v-loading="item.loading">
          <img :src="item.src" :tmp="JSON.stringify(item)" v-on:click="$emit('select-image',item)">
        </div>
      </div>
    </div>
    <div class="carousel-inner" v-else>
      <div class="carousel-item active">
        <img class="w-20" :src="defaultImagePath" >
        <img class="w-20" :src="defaultImagePath" >
        <img class="w-20" :src="defaultImagePath" >
        <img class="w-20" :src="defaultImagePath" >
        <img class="w-20" :src="defaultImagePath" >
      </div>
    </div>
    <a class="carousel-control-prev" v-if="imageList.length>5" :href="'#' + id" role="button" data-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" v-if="imageList.length>5" :href="'#' + id" role="button" data-slide="next">
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
  </div>
</template>
<script src="./image-list-slider.js"></script>
<style>
  .carouselSlider{
    min-height: 100px;
  }
  .carouselSlider .carousel-item-image{
    border: 2px solid transparent;

  }
  .carouselSlider .carousel-item-image.active{
    border:2px solid #007bff;
  }
  .carouselSlider .w-20{
    border:1px #EDEEF0 solid;
    height: 100px;
    float: left;
  }

  .carouselSlider .carousel-item img{
    width: 100%;
    height: 100%;
  }
  .carouselSlider .carousel-control-prev,.carousel-control-next {
    width: 50px !important;
  }
</style>