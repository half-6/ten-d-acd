<template>
  <div class="hospital-page">
    <el-row class="tool-bar panel">
      <el-button type="primary" class="pull-right" icon="el-icon-plus" @click="add">Add Hospital</el-button>
    </el-row>
    <el-row class="panel">
      <dynamic-table ref="hospitalTable" :meta="hospitalMeta"></dynamic-table>
    </el-row>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
