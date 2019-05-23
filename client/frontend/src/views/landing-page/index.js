import Cropper from 'cropperjs/dist/cropper.js';
import 'cropperjs/dist/cropper.css';
import _ from 'lodash';
import uuid from 'uuid/v4';
import api from '@/api'
import storage from '@/utils/local-storage';

const KEY_CANCER_ID = "KEY_CANCER_ID";
const KEY_MACHINE_ID = "KEY_MACHINE_ID";

export default {
  name: "landing-page",
  data() {
    return {
      message:null,
      cropImg:null,
      selectedImage: null,
      imageList: [],
      croppedImageList:[],
      record:{
        record_external_id:null,
        cancer_type:"",
        machine_type_id:"",
        roi_image:[],
        hospital_id:null,
        original_image:[]
      },
      cancerTypeList:[],
      machineTypeList:[],
      pathologyList:null,
      isSaving:false,
      isDetecting:false
    };
  },
  methods: {
    async openFile(e) {
      await this.buildGallery(e.target.files);
      this.onSelectFile(this.imageList[0]);
      e.target.value = "";
    },
    async handleChange(file,fileList){
      const files = _.map(fileList,item=>item.raw);
      await this.buildGallery(files);
      this.onSelectFile(this.imageList[0]);
    },
    beforeAvatarUpload(){
      return false;
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
    delSelectedImage(){
      this.$util_confirm('master.msg-delete').then(() => {
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
      })
    },
    delCroppedImage(){
      this.$util_confirm('master.msg-delete').then(() => {
        this.croppedImageList.splice(this.croppedImageList.indexOf(this.cropImg), 1);
        if (this.croppedImageList.length == 0) {
          this.cropImg = null;
        } else {
          this.onSelectCroppedImage(this.croppedImageList[0]);
        }
      })
    },
    loadImage(file) {
      return new Promise(resolve => {
        if (FileReader && file.type.indexOf("image") >= 0) {
          let fr = new FileReader();
          fr.onload = function() {
            resolve(fr.result);
          };
          fr.readAsDataURL(file);
        }
      });
    },
    async buildGallery(images) {
      this.selectedImage = null;
      this.imageList = [];
      //$("#imageList").carousel({pause: true,interval: false}).carousel(0);
      for (let i = 0; i < images.length; i++) {
        let item = images[i];
        let src = await this.loadImage(item);
        this.imageList.push({src,id:uuid(),name:item.name});
      }
    },
    crop(){
      let tmpImg = this.cropper.getCroppedCanvas().toDataURL();
      if(!_.find(this.croppedImageList,{src:tmpImg}))
      {
        this.cropImg = {
          src:this.cropper.getCroppedCanvas().toDataURL(),
          roi_image_id:uuid(),
          original_image_id:this.selectedImage.id,
          coordinate:_.pick(this.cropper.getData(false),["x","y","width","height"]),
          prediction:null,
          pathology:undefined,
        };
        this.croppedImageList.splice(0,0,this.cropImg);
        this.detect(this.cropImg);
      }
    },
    async detect(img){
      this.isDetecting = true;
      img.loading = true;
      img.prediction =  await api.detectImage(
          {
            roi_image_src:img.src,
            coordinate:img.coordinate,
            original_image_src:this.selectedImage.src,
            hospital_id:this.$hospital[0].hospital_id,
            cancer_type_id:this.record.cancer_type.cancer_type_id,
            cancer_type:this.record.cancer_type.cancer_type_short_name
          });
      img.loading = false;
      this.isDetecting = false;
      console.log("detected " + img.roi_image_id + ">" + JSON.stringify(img.prediction));
    },
    save(){
      //build save object
      this.record.original_image = [];
      this.record.roi_image = [];
      for(let i=0;i<this.croppedImageList.length;i++)
      {
        let image = this.croppedImageList[i];
        let oImage = _.find(this.imageList,{id:image.original_image_id});
        if(oImage)
        {
          if(this.record.original_image.indexOf(oImage)<0)
          {
            this.record.original_image.push(oImage);
          }
        }
        else
        {
          this.$util_alert("master.msg-missing-original-image")
          return;
        }
        this.record.cancer_type_id = this.record.cancer_type.cancer_type_id;
        this.record.roi_image.push({
          src:image.src,
          coordinate:image.coordinate,
          roi_image_id:image.roi_image_id,
          original_image_id:image.original_image_id,
          pathology:image.pathology,
          prediction:_.get(image.prediction,"Prediction"),
          probability:_.get(image.prediction,"Probability"),
          processing_time:_.get(image.prediction,"ProcessingTime"),
        })
      }
      console.log(this.record);
      this.isSaving = true;
      storage.set(KEY_CANCER_ID,this.record.cancer_type)
      storage.set(KEY_MACHINE_ID,this.record.machine_type_id)
      api.saveImage(this.record)
      .then(r=>{
            this.isSaving = false;
            console.log(r);
            this.reset();
            this.$util_alert("home.save-success");
          },err=>{
            this.isSaving = false;
            console.error(err);
            this.$util_alert("home.save-failed");
          }
      )
    },
    enableSaveButton(){
        return this.record.record_external_id
            && this.record.cancer_type
            && this.record.machine_type_id
            && this.croppedImageList.length > 0
            && _.filter(this.croppedImageList,img=>{return img.prediction}).length === this.croppedImageList.length;

    },
    init(){
      this.selectedImage=null;
      this.pathologyList = this.$pathology;
      this.cancerTypeList = this.$cancerType;
      this.machineTypeList = this.$machineType;
      this.record.hospital_id = this.$hospital[0].hospital_id;
      this.record.cancer_type = storage.get(KEY_CANCER_ID) || "";
      this.record.machine_type_id = storage.get(KEY_MACHINE_ID) || "";
      if(this.cropper) this.cropper.destroy();
      this.cropper = new Cropper(this.$refs.selectedImg,{autoCrop:false});
    },
    reset(){
      Object.assign(this.$data, this.$options.data())
      //$("#imageList").carousel({pause: true,interval: false}).carousel(0);
      this.init();
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
