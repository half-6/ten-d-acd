import _ from "lodash";

export default {
  name: "list-page",
  data() {
    return {
      aggCancerTypeList:[]
    };
  },
  methods: {
    agg(){
      this.$http.get('/api/db/v_agg_cancer_type')
          .then(r=>{
            if(r.body.response)
            {
              this.aggCancerTypeList = r.body.response.data;
            }
          });
    },
  },
  mounted: function() {
    this.agg()
  }
};
