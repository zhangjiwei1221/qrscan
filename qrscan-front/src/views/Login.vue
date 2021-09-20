<template>
  <div>
    <div class="login-form">
      <div class="main">
        main
      </div>
    </div>
  </div>
</template>

<script>
import {
  Form,
  Input,
  Button,
  Divider,
  Popover,
  FormItem,
  Checkbox
} from 'element-ui'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        account: '',
        password: '',
        remember: false
      },
      rules: {
        account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]}
    }
  },
  methods: {

  }
}
</script>

<style lang="less" scoped>
@blue: #409EFF;

.login-form {
  width: 100%;
  height: 680px;
  display: flex;
  align-items: center;
  background-size: cover;
  justify-content: flex-end;
}

.main {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
