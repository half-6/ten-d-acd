import _ from 'lodash'
export default {
  name: "list-page",
  data() {
    return {
      roiImageList:null,
      cancerTypeList:null,
      machineTypeList:null,
      pathologyList:[{"value":"malignant","text":"Malignant"},{"value":"benign","text":"Benign"}],
      isSearching:false,
      message:null,
      selectedRoiImage:null,
      search:{
        machine_type_id:"",
        cancer_type_id:"",
        pathology:"",
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
      this.$http.get('/api/db/public.cancer_type').then(r=>this.cancerTypeList = r.body.response.data)
      this.$http.get('/api/db/public.machine_type').then(r=>this.machineTypeList = r.body.response.data)
    },
    getImageUrl(file){
      return "/uploads/" + file + ".png";
    },
    searchImage(){
      this.isSearching=true;
      var query = {
        $where:{
              cancer_type_id:this.search.cancer_type_id?this.search.cancer_type_id:undefined,
              machine_type_id:this.search.machine_type_id?this.search.machine_type_id:undefined,
              pathology:this.search.pathology?this.search.pathology:undefined,
              record_external_id:this.search.id?{$like: "%"+ this.search.id + "%"}:undefined,
            },
        $limit:this.page.limit,
        $offset:(this.page.page_index -1) * this.page.limit,
        $sort:{date_registered:"DESC"}
      };
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
      download(){
          let query = {
              $where:{
                  cancer_type_id:this.search.cancer_type_id?this.search.cancer_type_id:undefined,
                  machine_type_id:this.search.machine_type_id?this.search.machine_type_id:undefined,
                  pathology:this.search.pathology?this.search.pathology:undefined,
                  record_external_id:this.search.id?{$like: "%"+ this.search.id + "%"}:undefined,
              },
              $limit:this.page.limit,
              $offset:(this.page.page_index -1) * this.page.limit,
              $sort:{date_registered:"DESC"}
          };
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
