import _ from 'lodash'
export default {
  name: "list-page",
  data() {
    return {
      roiImageList:null,
      cancerTypeList:null,
      machineTypeList:null,
      pathologyList:this.$pathology,
      predictionList:this.$pathology,
      isSearching:false,
      message:null,
      selectedRoiImage:null,
      search:{
        machine_type_id:"",
        cancer_type_id:"",
        pathology:"",
        prediction:"",
        id:""
      },
      page:{
        offset:0,
        limit:5,
        page_index: 1
      },
    };



  },
  methods: {
    init(){
      this.searchImage();
      this.$http.get('/api/db/public.v_cancer_type').then(r=>this.cancerTypeList = r.body.response.data)
      this.$http.get('/api/db/public.v_machine_type').then(r=>this.machineTypeList = r.body.response.data)
    },
    getImageUrl(file){
      return "/uploads/" + file + ".png";
    },
    searchImage(){
      this.isSearching=true;
      let query = this.buildWhere();
      this.$http.get('/api/db/v_roi_image',{params:{q:JSON.stringify(query)}} )
          .then(r=>{
            if(r.body.response)
            {
              _.map(r.body.response.data,item=>{item.$edit = false; item.$isSaving=false})
              this.roiImageList = r.body.response.data;
              this.page.total_count = r.body.response.page.total_count;
              //this.page.page_index = this.page.offset / this.page.limit
            }
            else
            {
              this.roiImageList = null;
            }

            this.isSearching = false;
          });
    },
    edit(item){
       item.$edit = true;
       item.$back = item.pathology;
    },
    del(item){
      this.selectedRoiImage = item;
      this.$refs.delModal.show();
    },
    delRoiImage(){
      this.$http.post('/api/db/roi_image',{$where:{roi_image_id:this.selectedRoiImage.roi_image_id},status:'deleted'})
      .then(r=>{
            console.log(r.body);
            this.searchImage();
          },err=>{
            console.error(err);
            this.message = "Delete failed,Please try again";
            this.$refs.errModal.show();
          }
      )
    },
    save(item){
      item.$isSaving=true;
      this.$http.post('/api/db/roi_image',{$where:{roi_image_id:item.roi_image_id},pathology:item.pathology})
      .then(r=>{
            console.log(r.body);
            item.$edit = false;
            item.$isSaving=false;
          },err=>{
            console.error(err);
            this.message = "Save failed,Please try again";
            this.$refs.errModal.show();
            item.$edit = false;
            item.$isSaving=false;
          }
      )
    },
    getPathology(){
        if(!this.search.pathology){ return undefined}
        if(this.search.pathology=="null") return null;
        return this.search.pathology;
    },
    buildWhere(){
      return {
          $where:{
              cancer_type_id:this.search.cancer_type_id?this.search.cancer_type_id:undefined,
              machine_type_id:this.search.machine_type_id?this.search.machine_type_id:undefined,
              pathology: this.getPathology(),
              prediction:this.search.prediction?this.search.prediction:undefined,
              record_external_id:this.search.id?{$like: "%"+ this.search.id + "%"}:undefined,
          },
          $limit: parseInt(this.page.limit),
          $offset:(this.page.page_index -1) * this.page.limit,
          $sort:{date_registered:"DESC"}
      }
    },
    download(){
        let query = this.buildWhere();
        window.open(`/api/image/download?input=${encodeURIComponent(JSON.stringify(query))}`);
    },
    cancel(item){
      item.pathology = item.$back;
      item.$edit = false;
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
