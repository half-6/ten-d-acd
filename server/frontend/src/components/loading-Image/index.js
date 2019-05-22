import defaultImage from '../../assets/i/default-thumbnail.png';
export default {
  name: 'loading-image',
  data() {
    return {
      isLoading:true,
      src:null,
      error:false,
    }
  },
  props: {
    imageKey:String,
  },
  created() {
    this.loadingImage()
    this.$watch('imageKey', ()=>{
      this.loadingImage()
    });
  },
  methods: {
    async loadingImage(){
      this.isLoading = true;
      this.$directives.authImage.loadImage(this.imageKey).then(
          r=> {
            this.src = r;
            this.isLoading = false;
          }
     ).catch(err=>{
        this.isLoading = false;
        this.error = true;
        this.src = defaultImage;
      })

    }
  }
}