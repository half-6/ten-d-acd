<template>
  <div class="certificate-page">
    <el-row class="tool-bar panel">
      <div class="text-center w100" v-if="hospital">
        {{hospital.hospital_name}}
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="add">Add Certificate</el-button>
      <!--<dynamic-input :value.sync="dValue" :meta="dMeta"/>-->
      <!--{{dValue}}-->
    </el-row>
    <el-row class="panel">
      <dynamic-table ref="certificateTable" :meta="certificateMeta"></dynamic-table>
    </el-row>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
