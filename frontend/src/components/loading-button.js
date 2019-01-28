export default {
  name: 'loading-button',
  props: {
    isLoading:Object,
    disabled:Object,
    value:String,
    id:String,
    loadingLabel:{
      type:String,
      default:"Loading..."
    },
  },

}