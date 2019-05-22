<template>
  <div v-loading="isLoading">
    <img v-if="!isLoading" :src="src"/>
  </div>
</template>
<script src="./index.js"></script>

