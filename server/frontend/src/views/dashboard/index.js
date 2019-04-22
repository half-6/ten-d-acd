import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'info',
    ])
  },
  methods: {
    init(){
    },
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
}