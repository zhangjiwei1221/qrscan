<template>
  <vue-qr
    :size="size"
    :margin="0"
    :auto-color="true"
    :dot-scale="1"
    :text="code"
  />
</template>
<script>
import VueQr from 'vue-qr'

export default {
  name: 'QrCode',
  props: {
    // 二维码内容
    code: {
      type: String,
      default: ''
    },
    // 二维码尺寸
    size: {
      type: Number,
      default: 150
    }
  },
  components: {
    VueQr
  }
}
</script>
