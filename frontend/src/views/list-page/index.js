import loadingButton from '../../components/loading-button.vue';
import imageListSlider from "../../components/image-list-slider";


export default {
  name: "list-page",
  data() {
    return {
      roiImageList:null,
      cancerTypeList:null,
      machineTypeList:null,
      pathologyList:[{"key":"malignant","label":"Malignant"},{"key":"benign","label":"Benign"}],
      isSearching:false,
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
        $offset:(this.page.page_index -1) * this.page.limit
      };
      this.$http.get('/api/db/v_roi_image',{params:{q:JSON.stringify(query)}} )
          .then(r=>{
            if(r.body.response)
            {
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
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  },
  components:{
    loadingButton
  }
};
