import _ from 'lodash'
import api from '@/api/image'

export default {
  name: "statistics-page",
  data() {
    return {
      search:{
        ai_version:"",
        hospital_id:"",
        machine_type_id:"",
      },
      aggCancerTypeList: [],
      totalTP: 0,
      totalTN: 0,
      totalFP: 0,
      totalFN: 0,
      isLoading:false,
    };
  },
  methods: {
    async agg() {
      this.isLoading = true;
      const query = {
        aiVersion:this.search.ai_version?this.search.ai_version:undefined,
        machineTypeId:this.search.machine_type_id?this.search.machine_type_id:undefined,
        hospitalId:this.search.hospital_id?this.search.hospital_id:undefined,
      }
      const r = await api.getAggTable(query)
      if (r) {
        this.aggCancerTypeList = r.data;
        _.each(this.aggCancerTypeList,o=>{
          o.accuracy = (o.tp + o.tn)/(o.tp + o.fp + o.tn + o.fn);
          o.tpr = o.tp/(o.tp + o.fn);
          o.tnr = o.tn/(o.fp + o.tn);
          o.precision = o.tp/(o.tp + o.fp);
        })
      }
      else {
        this.aggCancerTypeList = null
      }
      this.isLoading = false;
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = `Total`
          return
        }
      })
      sums[1] = this.$filters.numberFormat.number(_.sumBy(data, 'number_diagnostics'))
      this.totalTP = _.sumBy(data, 'tp');
      sums[5] = this.$filters.numberFormat.number(this.totalTP);
      this.totalTN = _.sumBy(data, 'tn');
      sums[6] = this.$filters.numberFormat.number(this.totalTN);
      this.totalFP = _.sumBy(data, 'fp');
      sums[7] = this.$filters.numberFormat.number(this.totalFP);
      this.totalFN = _.sumBy(data, 'fn');
      sums[8] = this.$filters.numberFormat.number(this.totalFN);
      sums[9] = this.$filters.numberFormat.percent((this.totalTP + this.totalTN)/(this.totalTP + this.totalFP + this.totalTN + this.totalFN));
      sums[10] = this.$filters.numberFormat.percent(this.totalTP/(this.totalTP + this.totalFN));
      sums[11] = this.$filters.numberFormat.percent(this.totalTN/(this.totalFP + this.totalTN));
      sums[12] = this.$filters.numberFormat.percent(this.totalTP/(this.totalTP + this.totalFP));
      return sums
    },
    init(){
      this.agg()
    },
  },
  mounted: function() {
    this.$nextTick(this.init);
  }
};
