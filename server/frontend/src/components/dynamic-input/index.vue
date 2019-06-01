<template>
   <div>
     <el-input :ref="meta.type" v-if="meta.type==='input'" v-model="value" :placeholder="meta.placeholder" @change="handleChange"></el-input>
     <el-select :ref="meta.type" v-if="meta.type==='select'" :placeholder="meta.placeholder">
       <el-option
           v-for="item in meta.options"
           :key="item.value"
           :label="item.label"
           :value="item.value"
           @change="handleChange"
       >
       </el-option>
     </el-select>
     <el-date-picker
             v-if="meta.type==='date-picker'"
             :ref="meta.type"
             v-model="value"
             :type="meta.dateType"
             :placeholder="meta.placeholder"
             @change="handleChange"
     >
     </el-date-picker>
   </div>
</template>
<script src="./index.js"></script>

