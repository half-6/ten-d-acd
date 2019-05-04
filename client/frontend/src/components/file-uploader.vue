<template>
    <label :class="{'disabled':disabled || isLoading}">
        <span v-if="!isLoading">{{title}}</span>
        <span v-if="isLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        <span v-if="isLoading" class="pl-1">{{loadingLabel}}</span>
        <input type="file" @change="openFile" :disabled="disabled || isLoading" class="h" multiple/>
    </label>
</template>

<script>
    export default {
        name: "file-uploader",
        props:{
            title:String,
            isLoading:Boolean,
            disabled:Boolean,
            loadingLabel:{
                type:String,
                default:"Loading..."
            },
        },
        methods:{
            async openFile(e){
                if (e.target.files.length > 0 && !this.disabled) {
                    await this.$emit("change",e)
                }
            }
        }
    }
</script>
