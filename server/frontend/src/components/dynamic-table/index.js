import _ from "lodash";
import api from '@/api/db'
export default {
  name: 'dynamic-table',
  data() {
    return {
      isLoading:true,
      insertDialogFormVisible:false,
      updateDialogFormVisible:false,
      list:null,
      fieldList:{},
      newFieldList:{},
      updateFieldList:{},
      newItem:{},
      selectedItem:{},
      api:{}
    }
  },
  /*
  {
        tableName: "v_hospital",
        delete:false, //[optional(true)] show delete action button
        update:false, //[optional(true)] show insert action button
        definedFieldList: [
          // name: field name
          // label: label for the field
          // width: field width for list table
          // readonly: [optional(false)] updateable or insertable
          // list: [optional(true)] show on the list
          // identity: id for the row, design for delete
          // formatter: a function to format value
          // rules: [optional] check element ui form rules for details
          {name: "hospital_id", label: "#", width: 300, readonly: true, list: false, identity: true, rules:[]},
          {name: "hospital_name", label: "Hospital Name",readonly: false,default:'<default value>'},
          {
            name: "date_registered", label: "Date Registered", width: 140, formatter: (input) => {
              return this.format(input);
            }, readonly: true
          },
        ],
        //select: [optional] overwrite select function
        //remove: [optional] overwrite delete function
        //update: [optional] overwrite update function
        //insert: [optional] overwrite insert function
        //selectTableName: [optional(root tableName)] overwrite select table name
        //insertTableName: [optional(root tableName)] overwrite insert table name
        //removeTableName: [optional(root tableName)] overwrite remove table name
        //updateTableName: [optional(root tableName)] overwrite update table name
        api: {
          remove: async (row) => {
            let updatedField = {"status": "deleted", "$where": {hospital_id: row.hospital_id}};
            return await api.updateHospital(updatedField);
          },
          updateTableName: "hospital",
          insertTableName: "hospital",
          removeTableName: "hospital",
        },
        //actionList [optional] add custom action button
        //type: type of button, check el-button for more
        //icon: icon of button, check el-button for more
        //click: action of button
        actionList: [
          {
            type:"primary",
            icon:"el-icon-medal",
            click:(row)=>{
              this.$router.push({name:"certificate",params:{hospitalId:row.hospital_id}})
            }
          }
        ]
      }
  */
  props: {
    meta:{
        tableName:null,
        definedFieldList:[],
        api:{}
    },
  },
  mounted() {
      if(_.get(this.meta,"tableName")!=null)
      {
          this.$nextTick(this.init);
      }
      this.$watch("meta",this.init)
  },
  computed:{
      getActionWidth(){
          let width = 120 + _.get(this.meta,"actionList.length",0) * 60
          if(this.meta.delete === false)
          {
              width = width - 60;
          }
          if(this.meta.insert === false)
          {
              width = width - 60;
          }
          return width;
      }
  },
  methods: {
    init(){
        this.fieldList = _.filter(this.meta.definedFieldList,item=>item.list!==false);
        this.newFieldList = _.filter(this.meta.definedFieldList,item=>!item.readonly);
        this.updateFieldList = _.filter(this.meta.definedFieldList,item=>item.list!==false);
        this.api = {
            select:_.partial(api.select, this.meta.api.selectTableName || this.meta.tableName),
            update:_.partial(api.update, this.meta.api.updateTableName || this.meta.tableName),
            remove:async (row)=>{
                const identityFieldName = this.findIdentity();
                const where = {[identityFieldName]:row[identityFieldName]};
                return await api.remove(this.meta.api.removeTableName || this.meta.tableName, {q:JSON.stringify(where)})
            },
            insert:_.partial(api.insert, this.meta.api.insertTableName || this.meta.tableName),
        }
        this.api = _.merge(this.api,this.meta.api);
        this.select()
    },
    async select(){
        this.isLoading = true;
        let results = await this.api.select();
        results = _.get(results,"data");
        this.list = _.map(results,item=>{item.$edit=false;return item});
        this.isLoading = false;
    },
    onCreate(){
        this.newItem = {};
        _.forEach(this.meta.definedFieldList,item=>{
            if(item.default && !this.newItem[item.name])
            {
                this.newItem[item.name] = item.default;
            }
        })
    },
    reset(){
        if(this.$refs.insertForm) this.$refs.insertForm.resetFields();
        if(this.$refs.updateForm) this.$refs.updateForm.resetFields();
    },
    async insert(){
        this.$refs.insertForm.validate(async (valid)=>{
            if(valid){
                try {
                    await this.api.insert([this.newItem]);
                    this.$notify({
                        title: 'Success',
                        message: `Insert item success`,
                        type: 'success'
                    });
                    await this.select()
                    this.insertDialogFormVisible = false;
                }
                catch (e) {
                    this.$notify({
                        title: 'Failed',
                        message: `Insert item failed, please try again`,
                        type: 'warn'
                    });
                }
            }
            else
            {
                return false;
            }
        });

    },
    async save(){
        this.$refs.updateForm.validate(async (valid)=> {
            if(valid){
                let updatedField = {};
                for(let index in this.meta.definedFieldList)
                {
                    let item = this.meta.definedFieldList[index];
                    if(!item.readonly && !item.identity)
                    {
                        updatedField[item.name] = this.selectedItem[item.name];
                    }
                    if(item.identity)
                    {
                        updatedField["$where"] = {[item.name]:this.selectedItem[item.name]};
                    }
                }
                const r = await this.api.update(updatedField);
                this.$notify({
                    title: 'Success',
                    message: `Update ${ r } item success`,
                    type: 'success'
                });
                this.updateDialogFormVisible = false
                await this.select()
            }
            else
            {
                return false;
            }
        })

    },
    async edit(row){
        this.selectedItem = _.clone(row);
        this.updateDialogFormVisible = true
     },
    async cancel(row){
         row.$edit = null
     },
    findIdentity(){
        for(let index in this.meta.definedFieldList)
        {
            let item = this.meta.definedFieldList[index];
            if(item.identity)
            {
                return item.name;
            }
        }
    },
    async remove(row){
        try {
            await this.$confirm("This will delete selected item, Continue?"," Warning",{
                confirmButtonText: 'OK',
                cancelButtonText: 'Cancel',
                type: 'warning'
            })
            const r = await this.api.remove(row);
            this.select()
            // const i = this.list.indexOf(row);
            // this.list.splice(i,1)
            this.$notify({
                title: 'Success',
                message: `Delete ${ r } item success`,
                type: 'success'
            });
        }
        catch (e) {
            //do nothing
        }
     },
  }
}