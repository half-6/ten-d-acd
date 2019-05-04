import _ from 'lodash'
import api from '@/api'
import uuid from "uuid/v4";

export default {
    name: "import-page",
    data() {
        return {
            roiImageList: null,
            cancerTypeList: this.$cancerType,
            cancerType: this.$cancerType[0],
            isLoading: false,
        };
    },
    methods: {
        init() {

        },
        getImageUrl(file) {
            return "/uploads/" + file + ".png";
        },
        enableImport(){
            return this.cancerType===""
        },
        async openFile(e) {
            await this.buildGallery(e.target.files);
            e.target.value = "";
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
            this.isLoading = true;
            this.roiImageList = [];
            for (let i = 0; i < images.length; i++) {
                let item = images[i];
                let src = await this.loadImage(item);
                let image = {
                    src,id:uuid(),
                    name:item.name,
                    loading:true,
                    cancer_type_id:this.cancerType.cancer_type_id,
                    prediction:"",
                    probability:"",
                }
                this.roiImageList.push(image);
            }
            for (let i = 0; i < this.roiImageList.length; i=i+2) {
                const queue = [];
                queue.push(this.detect(this.roiImageList[i]))
                if(i+1<this.roiImageList.length)
                {
                    queue.push(this.detect(this.roiImageList[i+1]))
                }
                await Promise.all(queue)
            }
            this.isLoading = false;
        },
        async detect(image) {
            image.loading = true;
            const results= await api.importImage(
                {
                    roi_image_src:image.src,
                    cancer_type:this.cancerType.cancer_type_short_name
                });
            image.prediction =  _.get(results,"Prediction");
            image.probability =  _.get(results,"Probability");
            image.loading =false;
        },
    },
    mounted: function () {
        this.$nextTick(this.init);
    }
};
