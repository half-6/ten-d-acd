import _ from 'lodash'
import api from '@/api/image'

export default {
  name: "home-page",
  data() {
    return {
      roiImageList:null,
      isLoading:false,
      search:{
        machine_type_id:"",
        cancer_type_id:"",
        hospital_id:"",
        pathology:"",
        prediction:"",
        id:""
      },
      page:{
        offset:0,
        limit:10,
        page_index: 1
      },
    };
  },
  methods: {
    init(){
      this.searchImage(1);
    },
    getImageUrl(file){
      return this.$tendConfig.imageUrl.replace("{file}",file);
    },
    buildWhere(){
      return {
        $where:{
          cancer_type_id:this.search.cancer_type_id?this.search.cancer_type_id:undefined,
          machine_type_id:this.search.machine_type_id?this.search.machine_type_id:undefined,
          pathology: this.search.pathology?this.search.pathology:undefined,
          prediction:this.search.prediction?this.search.prediction:undefined,
          hospital_id:this.search.hospital_id?this.search.hospital_id:undefined,
          record_external_id:this.search.id?{$like: "%"+ this.search.id + "%"}:undefined,
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
          });
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
