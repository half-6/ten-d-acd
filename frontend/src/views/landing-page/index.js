import defaultImagePath from "../../assets/i/default-thumbnail.png";
import Cropper from 'cropperjs/dist/cropper.js';
import 'cropperjs/dist/cropper.css';
import imageListSlider from '../../components/image-list-slider.vue';
import loadingButton from '../../components/loading-button.vue';
import _ from 'lodash';
export default {
  name: "landing-page",
  data() {
    return {
      picked:null,
      cropImg:null,
      selectedImage: null,
      defaultImagePath,
      allImages: [],
      imageList: [],
      croppedImageList:[],
      record:{
        record_external_id:null,
        cancer_type_id:"",
        machine_type_id:"",
        roi_image:[]
      },
      cancerTypeList:[],
      machineTypeList:[],
      pathologyList:[{"key":"malignant","label":"Malignant"},{"key":"benign","label":"Benign"}],
      isRecognition:false,
    };
  },
  methods: {
    async openFile(e) {
      if (e.target.files.length > 0) {
        await this.rebuildGallery(e.target.files);
        this.onSelectFile(this.imageList[0])
      }
    },
    onSelectFile(file) {
      if(file)
      {
        this.selectedImage = file;
        this.cropper.replace(this.selectedImage.src,false);
      }
    },
    onSelectCroppedImage(file){
      if(file)
      {
        this.cropImg = file;
      }
    },
    recognition(){
      if(this.cropImg)
      {
        this.isRecognition = true;
        let currentCropImg = this.cropImg;
        this.$http.post('/api/image/recognition',{image:this.cropImg.src},{emulateJSON:true,timeout:0})
        .then(r=>{
              console.log(r.body)
              currentCropImg.prediction = r.body.response;
              this.isRecognition = false;
            },err=>{
              console.error(err)
            }
        )
      }
    },
    delSelectedImage(){
      this.imageList.splice( this.imageList.indexOf(this.selectedImage), 1 );
      if(this.imageList.length ==0)
      {
        this.cropper.destroy();
        this.init();
      }
      else
      {
        this.onSelectFile(this.imageList[0]);
      }
    },
    delCroppedImage(){
      this.croppedImageList.splice( this.croppedImageList.indexOf(this.cropImg), 1 );
      if(this.croppedImageList.length ==0)
      {
        this.cropImg = null;
      }
      else
      {
        this.onSelectCroppedImage(this.croppedImageList[0]);
      }
    },
    loadImage(file) {
      return new Promise(resolve => {
        if (file === defaultImagePath) {
          resolve(file);
          return;
        }
        if (FileReader && file.type.indexOf("image") >= 0) {
          var fr = new FileReader();
          fr.onload = function() {
            resolve(fr.result);
          };
          fr.readAsDataURL(file);
        }
      });
    },
    onPathology(option){
       this.cropImg.pathology = option;
    },
    async buildGallery() {
      this.imageList = [];
      for (let i = 0; i < this.allImages.length; i++) {
        let item = this.allImages[i];
        let src = await this.loadImage(item);
        this.imageList.push({src});
      }
    },
    async rebuildGallery(images) {
      this.selectedImage = null;
      this.allImages = images;
      await this.buildGallery(0);
    },
    crop(){
      let tmpImg = this.cropper.getCroppedCanvas().toDataURL();
      if(!_.find(this.croppedImageList,{src:tmpImg}))
      {
        this.cropImg = {
          src:this.cropper.getCroppedCanvas().toDataURL(),
          prediction:null,
          pathology:null,
      };
        this.croppedImageList.splice(0,0,this.cropImg);
      }

    },
    init(){
      this.selectedImage = null;
      this.imageList = [];
      this.cropper = new Cropper(this.$refs.selectedImg,{autoCrop:false});
      this.$http.get('/api/db/public.cancer_type').then(r=>this.cancerTypeList = r.body.response.data)
      this.$http.get('/api/db/public.machine_type').then(r=>this.machineTypeList = r.body.response.data)
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  },
  components:{
    imageListSlider,
    loadingButton
  }
};
