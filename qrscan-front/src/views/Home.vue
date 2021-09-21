<template>
  <div class="home">
    <img src="../assets/img/bg.jpg"/>
    <el-button
      type="danger"
      class="logout"
      @click="logout"
      plain
    >注销</el-button>
  </div>
</template>

<script>
import {
  Button,
  Message
} from 'element-ui'
import store from "@/store";

export default {
  name: 'Home',
  components: {
    'el-button': Button
  },
  methods: {
    logout() {
      store.commit('removeToken')
      this.$router.push('/login')
      Message.success('注销成功')
    }
  }
}
</script>

<style scoped>
.home {
  width: 100%;
  text-align: center;
  margin-top: calc(50vh - 386px);
}

.logout {
  position: absolute;
  top: 10px;
  right: 50px;
  width: 90px;
  height: 32px;
  line-height: 10px;
}
</style>
