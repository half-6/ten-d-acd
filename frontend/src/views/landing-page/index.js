import Cropper from 'cropperjs/dist/cropper.js';
import 'cropperjs/dist/cropper.css';
import _ from 'lodash';
import uuid from 'uuid/v4';
import api from '@/api'

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
        original_image:[]
      },
      cancerTypeList:[],
      machineTypeList:[],
      queue:[],
      queueRunning:false,
      pathologyList:null,
      isRecognition:false,
      isSaving:false,
    };
  },
  methods: {
    async openFile(e) {
      if (e.target.files.length > 0) {
        await this.buildGallery(e.target.files);
        this.onSelectFile(this.imageList[0]);
        e.target.value = "";
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
        if(this.queue.indexOf(currentCropImg) >= 0)
        {
          this.queue.splice(this.queue.indexOf(currentCropImg),1);
        }
        currentCropImg.isAsking = true;
        this.detect(currentCropImg)
          .then(r=>{
                currentCropImg.prediction = currentCropImg.p_prediction;
                this.isRecognition = false;
              },err=>{
                console.error(err);
                this.$util_alert("master.msg-detection-failed")
                this.isRecognition = false;
              }
          )
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
        this.queue.splice(this.queue.indexOf(this.cropImg), 1);
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
          var fr = new FileReader();
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
      for (let i = 0; i < images.length; i++) {
        let item = images[i];
        let src = await this.loadImage(item);
        this.imageList.push({src,id:uuid()});
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
          prediction:null,
          pathology:undefined,
        };
        this.croppedImageList.splice(0,0,this.cropImg);
        this.queue.push(this.cropImg);
        this.backendJob();
      }
    },
    async backendJob(){
      if(!this.queueRunning)
      {
        while(this.queue.length>0)
        {
          this.queueRunning = true;
          let img = this.queue.shift();
          await this.detect(img);
        }
        this.queueRunning = false;
      }
    },
    async detect(img){
      if(!img.isDetecting && !img.p_prediction)
      {
        console.log("detecting " + img.roi_image_id);
        img.isDetecting = true;
        let result =  await api.detectImage({image:img.src,cancerType:this.record.cancer_type.cancer_type_short_name},{emulateJSON:true,timeout:0});
        img.p_prediction = result;
        img.isDetecting = false;
        if(img.isAsking){
          img.prediction = img.p_prediction;
        }
        console.log("detected " + img.roi_image_id + ">" + JSON.stringify(img.p_prediction));
      }
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
      if(this.cropper) this.cropper.destroy();
      this.cropper = new Cropper(this.$refs.selectedImg,{autoCrop:false});
    },
    reset(){
      let cancerType = this.record.cancer_type;
      Object.assign(this.$data, this.$options.data())
      this.init();
      this.record.cancer_type = cancerType;
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
