import defaultImagePath from "../assets/i/default-thumbnail.png";
export default {
  name: 'image-list-slider',
  data(){
    return {
      defaultImagePath
    }
  },
  props: ['imageList','selectedImage',"id"]
}