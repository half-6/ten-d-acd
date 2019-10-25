import _ from 'lodash'
import api from '@/api'

export default {
  name: "list-page",
  data() {
    return {
      roiImageList:null,
      cancerTypeList:this.$cancerType,
      machineTypeList:this.$machineType,
      pathologyList:this.$pathology,
      predictionList:this.$pathology,
      isSearching:false,
      message:null,
      selectedRoiImage:null,
      showDetailDialog:null,
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
    },
    getImageUrl(file){
      return "/uploads/" + file + ".png";
    },
    searchImage(){
      this.isSearching=true;
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

            this.isSearching = false;
          });
    },
    edit(item){
       item.$edit = true;
       item.$back = item.pathology;
    },
    detail(item){
       this.selectedRoiImage = item;
       this.showDetailDialog = true;
    },
    del(item){
      this.selectedRoiImage = item;
      this.$util_confirm("master.msg-delete").then(()=>{
          this.delRoiImage();
      })
    },
    delRoiImage(){
      api.updateROIImage({$where:{roi_image_id:this.selectedRoiImage.roi_image_id},status:'deleted'})
      .then(r=>{
            console.log(`delete roi image success ${r}`);
            this.searchImage();
          },err=>{
            console.error(err);
            this.$util_alert("master.msg-delete-failed");
            this.$refs.errModal.show();
          }
      )
    },
    async save(item){
      item.$isSaving=true;
      item.pathology && await api.updateROIImage({$where:{roi_image_id:item.roi_image_id},pathology:item.pathology})
      item.machine_type_id && await api.updateRecord({$where:{record_id:item.record_id},machine_type_id:item.machine_type_id})
      _.forEach(this.roiImageList,o=>{
          if(o.record_id === item.record_id)
          {
              o.machine_type_id = item.machine_type_id
          }
      })
      console.log(`update roi image success`);
      item.$edit = false
      item.$isSaving=false
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
        query.$limit= 100000;
        query.$offset = 0;
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
