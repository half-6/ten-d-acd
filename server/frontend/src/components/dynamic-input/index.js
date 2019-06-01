export default {
  name: 'dynamic-input',
  props: {
    value:String,
    meta:{
      type:"",
      placeholder:"",
      options:[] //select
    }
  },
  created() {
    this.$watch('value', ()=>{
      this.$emit('update:value', this.$refs[this.meta.type].value)
    });
  },
  methods: {
    handleInput(event) {
      this.$emit('update:value', this.$refs[this.meta.type].value)
    },
    handleChange(event) {
      this.$emit('update:value', this.$refs[this.meta.type].value)
    },
  }
}