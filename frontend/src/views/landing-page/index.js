import defaultImagePath from "../../assets/i/default-thumbnail.png";
import Cropper from 'cropperjs/dist/cropper.js';
import 'cropperjs/dist/cropper.css';
import imageListSlider from '../../components/image-list-slider.vue';

export default {
  name: "landing-page",
  data() {
    return {
      cropImg:null,
      selectedImage: null,
      defaultImagePath,
      allImages: [],
      imageList: [],
      croppedImageList:[],
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
        this.cropper.replace(this.selectedImage,false);
      }
    },
    onSelectCroppedImage(file){
      if(file)
      {
        this.cropImg = file;
      }
    },
    recognition(){

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
    async buildGallery() {
      this.imageList = [];
      for (let i = 0; i < this.allImages.length; i++) {
        let item = this.allImages[i];
        let src = await this.loadImage(item);
        this.imageList.push(src);
      }
    },
    async rebuildGallery(images) {
      this.selectedImage = null;
      this.allImages = images;
      await this.buildGallery(0);
    },
    crop(){
      this.cropImg = this.cropper.getCroppedCanvas().toDataURL();
      if(this.croppedImageList.indexOf(this.cropImg)<0)
      {
        this.croppedImageList.splice(0,0,this.cropImg);
      }
    },
    init(){
      this.selectedImage = null;
      this.imageList = [];
      this.cropper = new Cropper(this.$refs.selectedImg,{autoCrop:false})
    }
  },
  mounted: function() {
    this.$nextTick(this.init);
  },
  components:{
    imageListSlider,
  }
};
