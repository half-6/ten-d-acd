<template>
  <div class="dynamic-table">
    <el-table
            :data="list"
            stripe
            v-loading="isLoading"
    >
      <el-table-column v-for="item in fieldList" :key="item.name" :label="item.label" :width="item.width" >
        <template slot-scope="scope">
          <template v-if="item.formatter">
            {{ item.formatter(scope.row[item.name])}}
          </template>
          <template v-else>
            {{ scope.row[item.name]}}
          </template>
        </template>
      </el-table-column>
      <el-table-column align="center" label="Actions" :width="getActionWidth" v-if="meta.update!==false || meta.delete!==false || (meta.actionList && meta.actionList.length > 0)">
        <template slot-scope="scope">
          <el-button v-if="meta.update!==false" size="small" icon="el-icon-edit" @click="edit(scope.row)"></el-button>
          <el-button v-if="meta.delete!==false" type="danger" size="small" icon="el-icon-delete" @click="remove(scope.row)"></el-button>
          <template v-if="meta.actionList">
            <el-button v-for="(item,index) in meta.actionList" :key="index" :type="item.type" size="small" :icon="item.icon" @click="item.click(scope.row)"></el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="insertDialogFormVisible" title="Create" @open="onCreate" @closed="reset" :close-on-click-modal="false">
      <el-form ref="insertForm" :model="newItem">
        <el-form-item v-for="item in newFieldList" :key="item.name" :label="item.label" :rules="item.rules" :prop="item.name">
          <template v-if="!item.readonly">
            <template v-if="item.control">
              <el-input v-if="item.control.type==='input'" v-model="newItem[item.name]" :placeholder="item.control.placeholder"/>
              <el-date-picker
                      v-if="item.control.type==='date-picker'"
                      v-model="newItem[item.name]"
                      type="date"
                      :placeholder="item.control.placeholder"
              />
            </template>
            <el-input v-else v-model="newItem[item.name]"/>
          </template>
          <template v-else>{{item.formatter? item.formatter(newItem[item.name]):newItem[item.name]}}</template>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="insertDialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="insert()">Confirm</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="updateDialogFormVisible" title="Update" @closed="reset" :close-on-click-modal="false">
      <el-form ref="updateForm" :model="selectedItem">
        <el-form-item v-for="item in updateFieldList" :key="item.name" :label="item.label" :rules="item.rules" :prop="item.name">
          <template v-if="!item.readonly">
            <template v-if="item.control">
              <el-input v-if="item.control.type==='input'" v-model="selectedItem[item.name]" :placeholder="item.control.placeholder"/>
              <el-date-picker
                      v-if="item.control.type==='date-picker'"
                      v-model="selectedItem[item.name]"
                      type="date"
                      :placeholder="item.control.placeholder"
              />
            </template>
            <el-input v-else v-model="selectedItem[item.name]"/>
          </template>
          <template v-else>{{item.formatter? item.formatter(selectedItem[item.name]):selectedItem[item.name]}}</template>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save()">Confirm</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script src="./index.js"></script>
<style>
  .dynamic-table .el-form-item__content{
    display: inline-block;
    width: 100%;
  }
</style>

