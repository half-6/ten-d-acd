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
        $crop:function (image,imgPos) {
            let canvas=document.createElement("CANVAS");
            canvas.width = imgPos.width;
            canvas.height = imgPos.height;
            let ctx=canvas.getContext("2d");
            ctx.save();
            let img = new Image;
            img.src = image;
            ctx.drawImage(img,imgPos.x, imgPos.y, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
            ctx.restore();
            return canvas.toDataURL()
        }
    }
})