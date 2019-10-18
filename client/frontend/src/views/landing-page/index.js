import $cropper from '@linkfuture/cropper';
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
  computed:{

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
        if(!this.cropper)
        {
          this.cropper = new $cropper('canvas');
        }
        else
        {
          this.cropper.clear();
        }
        this.cropper.setImage(this.selectedImage.src);
      }
    },
    onSelectCroppedImage(file){
      if(file)
      {
        this.cropImg = file;
      }
    },
    startPen(){
      if(this.cropper) this.cropper.startPen();
    },
    delSelectedImage(){
      this.$util_confirm('master.msg-delete').then(() => {
        this.imageList.splice( this.imageList.indexOf(this.selectedImage), 1 );
        if(this.imageList.length === 0)
        {
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
        if (this.croppedImageList.length === 0) {
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
      for (let i = 0; i < images.length; i++) {
        let item = images[i];
        let src = await this.loadImage(item);
        this.imageList.push({src,id:uuid(),name:item.name});
      }
    },
    increaseBounds(bounds){
      const increasePercentage = 0.08;
      bounds.width = bounds.width + bounds.width * increasePercentage;
      bounds.height = bounds.height + bounds.height * increasePercentage;
      bounds.x = bounds.x - bounds.width * increasePercentage /2;
      bounds.y = bounds.y - bounds.height * increasePercentage /2;
    },
    crop(){
      let imgPosList = this.cropper.getPos();
      for(let i=0;i < imgPosList.length; i++ )
      {
        let imgPos = imgPosList[i];
        let newBounds = _.clone(imgPos.bounds)
        this.increaseBounds(newBounds);
        let tmpImg = this.cropper.cropBounds({bounds:newBounds});
        if(!_.find(this.croppedImageList,{src:tmpImg}))
        {
           delete imgPos.boundPos;
           let cropImgObj = {
             src:tmpImg,
             roi_image_id:uuid(),
             original_image_id:this.selectedImage.id,
             coordinate:imgPos,
             prediction:null,
             pathology:undefined,
           };
          this.croppedImageList.splice(0,0,cropImgObj);
          this.detect(cropImgObj,newBounds);
        }
        this.cropImg = this.croppedImageList[0];
      }
    },
    pointsToString(points){
       let output = [];
       points.forEach(point=>{
         output.push(`[${point.x},${point.y}]`)
       })
       return `[${output.join(";")}]`;
    },
    boundsToString(bounds){
      let points = [];
      points.push({x:bounds.x,y:bounds.y});
      points.push({x:bounds.x + bounds.width,y:bounds.y});
      points.push({x:bounds.x + bounds.width,y:bounds.y + bounds.height});
      points.push({x:bounds.x,y:bounds.y + bounds.height});
      return this.pointsToString(points)
    },
    async detect(img,newBounds){
      this.isDetecting = true;
      img.loading = true;
      try{
          storage.set(KEY_CANCER_ID,this.record.cancer_type)
          storage.set(KEY_MACHINE_ID,this.record.machine_type_id)
          img.prediction =  await api.detectImage(
              {
                  roi_image_src:img.src,
                  //roi_image_src:this.selectedImage.src,
                  coordinate:img.coordinate,
                  roi_coordinates:this.pointsToString(img.coordinate.points),
                  roi_corners:this.boundsToString(newBounds),
                  original_image_src:this.selectedImage.src,
                  hospital_id:this.$hospital[0].hospital_id,
                  cancer_type_id:this.record.cancer_type.cancer_type_id,
                  cancer_type:this.record.cancer_type.cancer_type_short_name
              });
          img.loading = false;
          img.sharpSrc = await this.drawLine(this.selectedImage.src,img.prediction.Shape_Axises,newBounds);
          img.calcSrc = await this.drawDots(this.selectedImage.src,img.prediction.Calcification_Coods,newBounds);
          img.marginSrc = await this.drawDots(this.selectedImage.src,img.prediction.Margin_Levels,newBounds);
          this.isDetecting = false;
          console.log("detected " + img.roi_image_id + ">" + JSON.stringify(img.prediction));
      }
      catch (e) {
          this.isDetecting = false;
          console.warn("detected failed");
          this.croppedImageList.shift();
          this.cropImg = _.get(this.croppedImageList,"[0]",null);
          this.$util_alert("home.result-detect-failed");
      }
    },
    async drawLine(imgSrc,lines,bounds){
        return new Promise((resolve, reject) =>{
            let image = new Image();
            image.src = imgSrc;
            image.onload = async ()=>{
                let canvas=document.createElement("CANVAS");
                canvas.width = image.width;
                canvas.height = image.height;
                let ctx=canvas.getContext("2d");
                ctx.save();
                ctx.drawImage(image,0, 0, canvas.width, canvas.height);
                ctx.strokeStyle = 'white';
                ctx.lineWidth=2;
                for(let i = 0; i < lines.length; i = i +2)
                {
                    ctx.beginPath();
                    ctx.moveTo(lines[i][0], lines[i][1]);
                    ctx.lineTo(lines[i+1][0], lines[i+1][1]);
                    ctx.closePath();
                    ctx.stroke();
                }
                resolve(await this.cropBounds(canvas.toDataURL(),this.findBounds(lines,bounds)));
            }
        })
    },
    findBounds(points,bounds){
        let minX = bounds.x;
        let minY = bounds.y;
        let maxX = bounds.x + bounds.width;
        let maxY = bounds.y + bounds.height;
        for(let i = 0; i < points.length; i++){
            let newX = points[i][points[i].length -2];
            let newY = points[i][points[i].length -1];
            if(minX>newX) minX = newX;
            if(maxX<newX) maxX = newX;
            if(minY>newY) minY = newY;
            if(maxY<newY) maxY = newY;
        }
        let boundWidth = maxX-minX;
        let boundHeight = maxY-minY;
        return {x:minX,y:minY,width:boundWidth,height:boundHeight}
    },
    async drawDots(imgSrc,dots,bounds){
        return new Promise((resolve, reject) =>{
            let image = new Image();
            image.src = imgSrc;
            image.onload = async ()=>{
                let canvas=document.createElement("CANVAS");
                canvas.width = image.width;
                canvas.height = image.height;
                let ctx=canvas.getContext("2d");
                ctx.save();
                ctx.drawImage(image,0, 0, canvas.width, canvas.height);

                ctx.lineWidth=1;
                for(let i = 0; i < dots.length; i = i +1)
                {
                    ctx.beginPath();
                    let dot = dots[i];
                    let x,y;
                    if(dot.length>2)
                    {
                        x = dot[1];
                        y = dot[2];
                        let colorIndex = dot[0];
                        let colors=["#17FF11","#FFFF11","#FF0016"];
                        ctx.strokeStyle = colors[colorIndex-1];
                        ctx.fillStyle = colors[colorIndex-1];
                    }
                    else
                    {
                        x = dot[0];
                        y = dot[1];
                        ctx.strokeStyle = ctx.fillStyle = '#126bec';
                    }
                    ctx.arc(x, y, 3, 0, 2 * Math.PI);
                    ctx.stroke();
                    ctx.fill();

                }
                resolve(await this.cropBounds(canvas.toDataURL(),this.findBounds(dots,bounds)));
            }
        })
    },
    async cropBounds(imgSrc, bounds){
        return new Promise((resolve, reject) =>{
            let canvas=document.createElement("CANVAS");
            canvas.width = bounds.width;
            canvas.height = bounds.height;
            let ctx=canvas.getContext("2d");
            ctx.save();
            let img = new Image();
            img.src = imgSrc;
            img.onload = ()=>{
                ctx.drawImage(img,bounds.x, bounds.y, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
                ctx.restore();
                resolve(canvas.toDataURL())
            }
        } )
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
          probability:_.get(image.prediction,"Probability")/100,
          processing_time:_.get(image.prediction,"ProcessingTime"),
          detection_result:image.prediction
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
    enableCutButton(){
      return this.selectedImage && this.record.cancer_type && this.cropper && this.cropper.getPos().length > 0;
    },
    init(){
      this.selectedImage=null;
      //this.cropper = null;
      this.pathologyList = this.$pathology;
      this.cancerTypeList = this.$cancerType;
      this.machineTypeList = this.$machineType;
      this.record.hospital_id = this.$hospital[0].hospital_id;
      this.record.cancer_type = storage.get(KEY_CANCER_ID) || "";
      this.record.machine_type_id = storage.get(KEY_MACHINE_ID) || "";
    },
    reset(){
      Object.assign(this.$data, this.$options.data())
      this.init();
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  },
  destroyed:function () {
      if(this.cropper) this.cropper.destroy();
  }
};
