<template>
  <div class="roi-image">
    <div class="tool-bar panel">
      <el-row :gutter="10">
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.hospital_id" placeholder="Hospital">
            <el-option value="" label="Hospital"></el-option>
            <el-option
                    v-for="item in $hospital"
                    :key="item.hospital_id"
                    :label="item.hospital_name"
                    :value="item.hospital_id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.ai_version" placeholder="AI Version">
            <el-option value="" label="AI Version"></el-option>
            <el-option
                    v-for="item in $aiVersion"
                    :key="item.ai_version"
                    :label="item.ai_version"
                    :value="item.ai_version">
            </el-option>
          </el-select>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.machine_type_id" placeholder="Machine Type">
            <el-option value="" label="Machine Type"></el-option>
            <el-option
                    v-for="item in $machineType"
                    :key="item.machine_type_id"
                    :label="item.machine_type_name"
                    :value="item.machine_type_id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.cancer_type_id" placeholder="Cancer Type">
            <el-option value="" label="Cancer Type"></el-option>
            <el-option
                    v-for="item in $cancerType"
                    :key="item.cancer_type_id"
                    :label="item.cancer_type_name"
                    :value="item.cancer_type_id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.pathology" placeholder="Pathology">
            <el-option value="" label="Pathology"></el-option>
            <el-option
                    v-for="item in $pathology"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-select v-model="search.prediction" placeholder="Prediction">
            <el-option value="" label="Prediction"></el-option>
            <el-option
                    v-for="item in $pathology"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :sm="12" :xs="24" :lg="6">
          <el-date-picker
                  v-model="search.date"
                  type="daterange"
                  unlink-panels
                  range-separator="~"
                  start-placeholder="Start date"
                  end-placeholder="End date"
                  :picker-options="pickerOptions">
          </el-date-picker>
        </el-col>
        <el-col :md="8" :sm="6" :xs="12" :lg="6">
          <el-input  v-model="search.id" placeholder="Search by ID"></el-input>
        </el-col>
        <el-col :md="4" :sm="6" :xs="12" :lg="3">
          <el-button type="primary" :loading="isLoading" icon="el-icon-search" @click="searchImage(1)">Search</el-button>
        </el-col>
      </el-row>
    </div>
    <el-row class="panel" :gutter="10">
      <el-table
              :data="roiImageList"
              stripe
              v-loading="isLoading"
      >
        <el-table-column fixed label="#" prop="record_external_id" width="80"/>
        <el-table-column label="Original Image" prop="original_image">
          <template slot-scope="scope">
            <a @click="showImage(scope.row.original_image)" target="_blank">
              <loading-image class="image-container" :imageKey="scope.row.original_image" ></loading-image>
            </a>
          </template>
        </el-table-column>
        <el-table-column label="Roi Image" prop="roi_image">
          <template slot-scope="scope">
            <a @click="showImage(scope.row.roi_image)" target="_blank">
              <loading-image class="image-container" :imageKey="scope.row.roi_image" ></loading-image>
            </a>
          </template>
        </el-table-column>
        <el-table-column label="Cancer Type" prop="cancer_type_name"/>
        <el-table-column label="Machine Type" prop="machine_type_name"/>
        <el-table-column label="Pathology" prop="pathology" width="90"/>
        <el-table-column label="Prediction" prop="prediction" width="90"/>
        <el-table-column label="Probability" prop="probability" :formatter="$formatters.numberFormat.percent"/>
        <el-table-column label="Date" prop="date_registered" width="140" :formatter="$formatters.dateFormat.dateTime"/>
      </el-table>
      <el-pagination
              class="p-t20 pull-right"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="page.limit"
              :current-page.sync ="page.page_index"
              background
              layout="total,sizes, prev, pager, next"
              @current-change="searchImage"
              @size-change="handleSizeChange"
              :total="page.total_count">
      </el-pagination>
    </el-row>
    <el-dialog
            title="Image"
            :visible.sync="showImageDialog"
            width="80%"
            center>
      <loading-image class="popup-image" :imageKey="selectedImage" ></loading-image>
    </el-dialog>
  </div>
</template>
<style src="./index.css"></style>
<script src="./index.js"></script>
