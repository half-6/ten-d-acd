<template>
  <button :id="id" type="submit" class="btn btn-primary" @click="$emit('click')" :class="{disabled:disabled || isLoading}" :disabled="disabled || isLoading" >
    <span v-if="!isLoading">{{value}}</span>
    <span v-if="isLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
    <span v-if="isLoading" class="pl-1">{{loadingLabel}}</span>
  </button>
</template>
<script src="./loading-button.js"></script>

