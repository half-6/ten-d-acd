import _ from 'lodash'
import api from '@/api/image'
import moment from 'moment';
export default {
  name: "home-page",
  data() {
    return {
      roiImageList:null,
      isLoading:false,
      selectedImage:null,
      showImageDialog:false,
      search:{
        machine_type_id:"",
        cancer_type_id:"",
        hospital_id:"",
        ai_version:"",
        pathology:"",
        prediction:"",
        date:"",
        id:""
      },
      page:{
        offset:0,
        limit:10,
        page_index: 1
      },
      pickerOptions: {
        shortcuts: [{
          text: 'Last week',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: 'Last month',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: 'Last 3 months',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    };
  },
  methods: {
    init(){
      this.searchImage(1);
    },
    showImage(imageId){
      this.selectedImage = imageId;
      this.showImageDialog = true;
    },
    buildWhere(){
      return {
        $where:{
          cancer_type_id:this.search.cancer_type_id?this.search.cancer_type_id:undefined,
          machine_type_id:this.search.machine_type_id?this.search.machine_type_id:undefined,
          pathology: this.search.pathology?this.search.pathology:undefined,
          prediction:this.search.prediction?this.search.prediction:undefined,
          hospital_id:this.search.hospital_id?this.search.hospital_id:undefined,
          ai_version:this.search.ai_version?this.search.ai_version:undefined,
          record_external_id:this.search.id?{$like: "%"+ this.search.id + "%"}:undefined,
          date_registered:this.search.date?{
            $gte: moment(this.search.date[0]).utc().format("YYYY/MM/DDTHH:mm:ss.SSS")+"Z",
            $lte: moment(this.search.date[1]).utc().format("YYYY/MM/DDTHH:mm:ss.SSS")+"Z",
          }:undefined,
        },
        $limit: parseInt(this.page.limit),
        $offset:(this.page.page_index -1) * this.page.limit,
        $sort:{date_registered:"DESC"}
      }
    },
    handleSizeChange(size){
      if(size) this.page.limit = size;
      this.searchImage(1);
    },
    searchImage(pageIndex){
      this.page.page_index = pageIndex;
      this.isLoading=true;
      let query = this.buildWhere();
      api.getROIImages({q:JSON.stringify(query)})
          .then(r=>{
            if(r)
            {
              _.map(r.data,item=>{item.$edit = false; item.$isSaving=false})
              this.roiImageList = r.data;
              this.page.total_count = r.page.total_count;
              //this.page.page_index = this.page.offset / this.page.limit
            }
            else
            {
              this.roiImageList = null;
            }
            this.isLoading = false;
          }).catch(err=>{
            this.isLoading = false;
          });
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
