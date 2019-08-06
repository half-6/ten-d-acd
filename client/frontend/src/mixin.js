import Vue from "vue";
Vue.mixin({
    methods: {
        $util_alert: function (msg) {
            this.$alert(this.$t(msg), this.$t('master.dialog-alert'), {
                confirmButtonText:  this.$t('master.dialog-ok')
            });
        },
        $util_confirm: function (msg) {
            return this.$confirm(this.$t(msg), this.$t('master.dialog-warning'), {
                confirmButtonText:  this.$t('master.dialog-ok'),
                cancelButtonText:  this.$t('master.dialog-cancel'),
                type: 'warning'
            })
        },
    }
})