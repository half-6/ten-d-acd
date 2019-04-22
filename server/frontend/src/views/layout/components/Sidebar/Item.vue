<template>
    <div>
        <svg-icon v-if="icon" :icon-class="icon"/>
        <span slot='title' v-html="title"></span>
    </div>
</template>
<script>
export default {
  name: 'MenuItem',
  // functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  // render(h, context) {
  //   const { icon, title } = context.props
  //   const vnodes = []
  //
  //   if (icon) {
  //     vnodes.push(<svg-icon icon-class={icon}/>)
  //   }
  //
  //   if (title) {
  //     vnodes.push(<span slot='title'>{(title)}</span>)
  //   }
  //   return vnodes
  // }
}
</script>
