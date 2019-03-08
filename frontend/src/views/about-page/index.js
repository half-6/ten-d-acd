import _ from "lodash";
import api from '@/api';

export default {
    name: "list-page",
    data() {
        return {
            aggCancerTypeList: [],
            totalDiagnostics: 0,
            totalTP: 0,
            totalTN: 0,
            totalFP: 0,
            totalFN: 0,
            accuracy: 0,
            tpr: 0,
            tnr: 0,
            precision: 0,
        };
    },
    methods: {
        async agg() {
            const r = await api.getAggTable()
            if (r.data) {
                this.aggCancerTypeList = r.data;
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
        },
    },
    mounted: function () {
        this.agg()
    }
};
