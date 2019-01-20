import defaultImagePath from "../../assets/i/default-thumbnail.png";
import Cropper from 'cropperjs/dist/cropper.js';
import 'cropperjs/dist/cropper.css';

export default {
  name: "landing-page",
  data() {
    return {
      cropImg:null,
      selectedImage: null,
      defaultImagePath,
      pager: {
        pageIndex: 0,
        pageLimit: 10,
        pageSize: 0
      },
      allImages: [],
      imageList1: [],
      imageList2: []
    };
  },
  methods: {
    openFile(e) {
      if (e.target.files.length > 0) {
        this.rebuildGallery(e.target.files);
      }
    },
    onSelectFile(file) {
      this.selectedImage = file;
      if(this.selectedImage && this.selectedImage != defaultImagePath)
      {
        this.cropper.replace(this.selectedImage,false);
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
    async buildGallery(pageIndex) {
      this.pager.pageIndex = pageIndex;
      let start = this.pager.pageIndex * this.pager.pageLimit;
      let end = (this.pager.pageIndex + 1) * this.pager.pageLimit;
      if (end > this.allImages.length) {
        end = this.allImages.length;
      }
      this.pager.pageSize = Math.ceil(
        this.allImages.length / this.pager.pageLimit
      );
      this.imageList1 = [];
      this.imageList2 = [];
      for (let i = start; i < end; i++) {
        let item = this.allImages[i];
        if (this.imageList1.length < 5) {
          let src = await this.loadImage(item);
          this.imageList1.push(src);
          continue;
        }
        if (this.imageList2.length < 5) {
          let src = await this.loadImage(item);
          this.imageList2.push(src);
        }
      }
      while (this.imageList1.length < 5) {
        this.imageList1.push(defaultImagePath);
      }
      while (this.imageList2.length < 5) {
        this.imageList2.push(defaultImagePath);
      }
      //this.onSelectFile(this.imageList1[0]);
    },
    async rebuildGallery(images) {
      this.selectedImage = null;
      this.allImages = images;
      await this.buildGallery(0);
    },
    crop(){
      this.cropImg = this.cropper.getCroppedCanvas().toDataURL();
    }
  },
  mounted: function() {
    this.$nextTick(function() {
      let images = [];
      for (let i = 0; i < this.pager.pageLimit; i++) {
        images.push(defaultImagePath);
      }
      this.rebuildGallery(images);
      this.cropper = new Cropper(this.$refs.selectedImg)
    });
  }
};
