import defaultImagePath from "../assets/i/default-thumbnail.png";
import $ from 'jquery';

export default {
  name: 'image-list-slider',
  data(){
    return {
      defaultImagePath
    }
  },
  props: ['imageList','selectedImage',"id"],
  created() {
    this.$watch('imageList', ()=>{
      $(this.$refs.carousel).carousel({pause: true,interval: false}).carousel(0);
    });
  },
}