<template>
  <div class="statistics-page">
    <el-row class="panel tool-bar" :gutter="10">
      <el-select v-model="search.hospital_id" placeholder="Hospital">
        <el-option value="" label="Hospital"></el-option>
        <el-option
                v-for="item in $hospital"
                :key="item.hospital_id"
                :label="item.hospital_name"
                :value="item.hospital_id">
        </el-option>
      </el-select>
      <el-select v-model="search.ai_version" placeholder="AI Version">
        <el-option value="" label="AI Version"></el-option>
        <el-option
                v-for="item in $aiVersion"
                :key="item.ai_version"
                :label="item.ai_version"
                :value="item.ai_version">
        </el-option>
      </el-select>
      <el-select v-model="search.machine_type_id" placeholder="Machine Type">
        <el-option value="" label="Machine Type"></el-option>
        <el-option
                v-for="item in $machineType"
                :key="item.machine_type_id"
                :label="item.machine_type_name"
                :value="item.machine_type_id">
        </el-option>
      </el-select>
      <el-button type="primary" :loading="isLoading" icon="el-icon-search" @click="agg()">Search</el-button>
    </el-row>
    <el-row class="panel">
      <el-table
              :data="aggCancerTypeList"
              stripe
              v-loading="isLoading"
              :summary-method="getSummaries"  show-summary
      >
        <el-table-column fixed label="Cancer Type" prop="cancer_type_name" width="140"/>
        <el-table-column label="# of diagnostics" prop="number_diagnostics" width="160" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="Lowest process time" prop="max" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="Fastest process time" prop="min" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="Average process time" prop="avg" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="TP" prop="tp" width="60" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="TN" prop="tn" width="60" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="FP" prop="fp" width="60" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="FN" prop="fn" width="60" :formatter="$formatters.numberFormat.number"/>
        <el-table-column label="Accuracy(T)" width="110" prop="accuracy" :formatter="$formatters.numberFormat.percent"/>
        <el-table-column label="TPR(T)" width="80" prop="tpr" :formatter="$formatters.numberFormat.percent"/>
        <el-table-column label="TNR(T)" width="80" prop="tnr" :formatter="$formatters.numberFormat.percent"/>
        <el-table-column label="Precision(T)" width="110" prop="precision" :formatter="$formatters.numberFormat.percent"/>
      </el-table>
    </el-row>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
