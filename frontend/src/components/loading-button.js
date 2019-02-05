export default {
  name: 'loading-button',
  props: {
    isLoading:Boolean,
    disabled:Boolean,
    value:String,
    id:String,
    loadingLabel:{
      type:String,
      default:"Loading..."
    },
  },

}