import api from '@/api/image'
import moment from 'moment';
export default {
  name: "hospital-page",
  data() {
    return {
      hospitalMeta: {
        tableName: "v_hospital",
        definedFieldList: [
          {name: "hospital_id", label: "#", width: 300, insert: true, update:false, list: true, identity: true, placeholder:"Please enter UUID, leave it blank if you want to auto generate the id"},
          {name: "hospital_name", label: "Hospital Name",rules:[{required:true,message:"Hospital name is required"}]},
          {name: "hospital_chinese_name", label: "Chinese Name" ,rules:[{required:true,message:"Hospital chinese name is required"}]},
          {name: "start_date", label: "Start Date", width: 140, insert: false, update:false, formatter: this.$functions.dateFormat.dateTime},
          {name: "expire_date", label: "Expire Date", width: 140, insert: false, update:false,formatter: this.$functions.dateFormat.dateTime},
          {name: "status", label: "Status", width: 80, insert: false, update:false, list: false,formatter: this.$functions.dateFormat.dateTime},
          {name: "date_registered", label: "Date Registered", width: 140, insert: false, update:false,formatter: this.$functions.dateFormat.dateTime},
        ],
        api: {
          remove: async (row) => {
            let updatedField = {"status": "deleted", "$where": {hospital_id: row.hospital_id}};
            return await api.updateHospital(updatedField);
          },
          updateTableName: "hospital",
          insertTableName: "hospital",
          removeTableName: "hospital",
        },
        actionList: [
          {
            type:"primary",
            icon:"el-icon-medal",
            label:"Certificate",
            click:(row)=>{
              this.$router.push({name:"certificate",params:{hospitalId:row.hospital_id}})
            }
          }
        ]
      }
    };
  },
  methods: {
    init(){
      //console.log("aaa" + JSON.stringify(this.$formatters.dateFormat.dateTime))
    },
    add(){
      this.$refs.hospitalTable.insertDialogFormVisible = true;
    },
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
