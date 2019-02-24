import _ from "lodash";

export default {
    name: "list-page",
    data() {
        return {
            aggCancerTypeList: [],
            totalDiagnostics: null,
            totalTP: null,
            totalTN: null,
            totalFP: null,
            totalFN: null,
        };
    },
    methods: {
        agg() {
            this.$http.get('/api/db/v_agg_cancer_type')
                .then(r => {
                    if (r.body.response) {
                        this.aggCancerTypeList = r.body.response.data;
                        _.each(this.aggCancerTypeList,o=>{
                            o.accuracy = (o.tp + o.tn)/(o.tp + o.fp + o.tn + o.fn);
                            o.tpr = o.tp/(o.tp + o.fn);
                            o.tnr = o.tn/(o.fp + o.tn);
                            o.precision = o.tp/(o.tp + o.fp);
                        })
                        this.totalDiagnostics = _.sumBy(this.aggCancerTypeList, 'number_diagnostics');
                        this.totalTP = _.sumBy(this.aggCancerTypeList, 'tp');
                        this.totalTN = _.sumBy(this.aggCancerTypeList, 'tn');
                        this.totalFP = _.sumBy(this.aggCancerTypeList, 'fp');
                        this.totalFN = _.sumBy(this.aggCancerTypeList, 'fn');
                        this.accuracy = (this.totalTP + this.totalTN)/(this.totalTP + this.totalFP + this.totalTN + this.totalFN);
                        this.tpr = this.totalTP/(this.totalTP + this.totalFN);
                        this.tnr = this.totalTN/(this.totalFP + this.totalTN);
                        this.precision = this.totalTP/(this.totalTP + this.totalFP);
                    }
                });
        },
    },
    mounted: function () {
        this.agg()
    }
};
